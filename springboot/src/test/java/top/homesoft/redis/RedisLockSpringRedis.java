package top.homesoft.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import top.homesoft.java.App;
import top.homesoft.java.redis.IBuyAction;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class RedisLockSpringRedis {
    private static ExecutorService pool;

    @Autowired
    StringRedisTemplate template;
    //Checks autowiring problems in a bean class
    //buyAction
    @Resource(name="springBootRedisBuyAction")
    IBuyAction buyAction;



    @Test
    public void test() {
        //pool = new ThreadPoolExecutor(1, 10, 100000, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0; i <10 ; i++) {
            Thread t =  new Thread(buyAction);
            t.setName(i+"");
            t.start();
        }
        try {
            Thread.sleep(1000*100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
