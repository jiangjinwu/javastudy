package top.homesoft.java.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;


public class RedisLockImitateSpringBoot implements RedisCacheWriter {
    private final RedisConnectionFactory connectionFactory;
    private final Duration sleepTime;

    RedisLockImitateSpringBoot() {
        this(getConnectionFactory(), Duration.ofMillis(50));
    }

    RedisLockImitateSpringBoot(RedisConnectionFactory connectionFactory, Duration sleepTime) {
        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
        Assert.notNull(sleepTime, "SleepTime must not be null!");
        this.connectionFactory = connectionFactory;
        this.sleepTime = sleepTime;
    }

    public void put(String name, byte[] key, byte[] value, @Nullable Duration ttl) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");
        this.execute(name, (connection) -> {
            if (shouldExpireWithin(ttl)) {
                connection.set(key, value, Expiration.from(ttl.toMillis(), TimeUnit.MILLISECONDS), RedisStringCommands.SetOption.upsert());
            } else {
                connection.set(key, value);
            }

            return "OK";
        });
    }

    public byte[] get(String name, byte[] key) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        return (byte[])this.execute(name, (connection) -> {
            return connection.get(key);
        });
    }

    public byte[] putIfAbsent(String name, byte[] key, byte[] value, @Nullable Duration ttl) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");
        return (byte[])this.execute(name, (connection) -> {
            if (this.isLockingCacheWriter()) {
                this.doLock(name, connection);
            }

            byte[] var6;
            try {
                if (connection.setNX(key, value)) {
                    if (shouldExpireWithin(ttl)) {
                        connection.pExpire(key, ttl.toMillis());
                    }

                    Object var10 = null;
                    return (byte[])var10;
                }

                var6 = connection.get(key);
            } finally {
                if (this.isLockingCacheWriter()) {
                    this.doUnlock(name, connection);
                }

            }

            return var6;
        });
    }

    public void remove(String name, byte[] key) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        this.execute(name, (connection) -> {
            return connection.del(new byte[][]{key});
        });
    }

    public void clean(String name, byte[] pattern) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(pattern, "Pattern must not be null!");
        this.execute(name, (connection) -> {
            boolean wasLocked = false;

            try {
                if (this.isLockingCacheWriter()) {
                    this.doLock(name, connection);
                    wasLocked = true;
                }

                byte[][] keys = (byte[][])((Set) Optional.ofNullable(connection.keys(pattern)).orElse(Collections.emptySet())).toArray(new byte[0][]);
                if (keys.length > 0) {
                    connection.del(keys);
                }
            } finally {
                if (wasLocked && this.isLockingCacheWriter()) {
                    this.doUnlock(name, connection);
                }

            }

            return "OK";
        });
    }

   public void lock(String name) {
        this.execute(name, (connection) -> {
            return this.doLock(name, connection);
        });
    }

    void unlock(String name) {
        this.executeLockFree((connection) -> {
            this.doUnlock(name, connection);
        });
    }

    private Boolean doLock(String name, RedisConnection connection) {
        return connection.setNX(createCacheLockKey(name), new byte[0]);
    }

    private Long doUnlock(String name, RedisConnection connection) {
        return connection.del(new byte[][]{createCacheLockKey(name)});
    }

    boolean doCheckLock(String name, RedisConnection connection) {
        return connection.exists(createCacheLockKey(name));
    }

    private boolean isLockingCacheWriter() {
        return !this.sleepTime.isZero() && !this.sleepTime.isNegative();
    }

    private <T> T execute(String name, Function<RedisConnection, T> callback) {
        RedisConnection connection = this.connectionFactory.getConnection();

        Object var4;
        try {
            this.checkAndPotentiallyWaitUntilUnlocked(name, connection);
            var4 = callback.apply(connection);
        } finally {
            connection.close();
        }

        return (T)var4;
    }

    private void executeLockFree(Consumer<RedisConnection> callback) {
        RedisConnection connection = this.connectionFactory.getConnection();

        try {
            callback.accept(connection);
        } finally {
            connection.close();
        }

    }

    private void checkAndPotentiallyWaitUntilUnlocked(String name, RedisConnection connection) {
        if (this.isLockingCacheWriter()) {
            try {
                while(this.doCheckLock(name, connection)) {
                    Thread.sleep(this.sleepTime.toMillis());
                }

            } catch (InterruptedException var4) {
                Thread.currentThread().interrupt();
                throw new PessimisticLockingFailureException(String.format("Interrupted while waiting to unlock cache %s", name), var4);
            }
        }
    }

    private static boolean shouldExpireWithin(@Nullable Duration ttl) {
        return ttl != null && !ttl.isZero() && !ttl.isNegative();
    }

    private static byte[] createCacheLockKey(String name) {
        return (name + "~lock").getBytes(StandardCharsets.UTF_8);
    }

    public  static JedisConnectionFactory getConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("127.0.0.1");
        redisStandaloneConfiguration.setPort(6379);
        redisStandaloneConfiguration.setDatabase(0);
        //redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(Duration.ofMillis(50));//  connection timeout

        JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration,
                jedisClientConfiguration.build());
        return factory;
    }


}
