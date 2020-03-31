package top.homesoft.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.homesoft.java.App;
import top.homesoft.java.redis.IBuyAction;

import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class RedisLock {
    private static ExecutorService pool;


    //Checks autowiring problems in a bean class
    //buyAction
    @Autowired
    IBuyAction buyAction;
    @Test
    public void test() {
        //pool = new ThreadPoolExecutor(1, 10, 100000, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0; i <10 ; i++) {
            // Task top.homesoft.java.redis.BuyAction@4aaecabd rejected from java.util.concurrent.ThreadPoolExecutor@23bd0c81[Running, pool size = 2, active threads = 1, queued tasks = 0, completed tasks = 0]
            //Exception in thread "pool-2-thread-4" org.redisson.client.RedisTimeoutException: Subscribe timeout: (7500ms). Increase 'subscriptionsPerConnection' and/or 'subscriptionConnectionPoolSize' parameters.
           // pool.execute(buyAction);
           // buyAction.run();

            new Thread(buyAction).start();
        }

    }
}
