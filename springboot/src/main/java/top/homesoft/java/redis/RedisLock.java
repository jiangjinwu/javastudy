package top.homesoft.java.redis;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.*;


public class RedisLock {
    private static ExecutorService pool;

    @Autowired
    IBuyAction buyAction;


    //java.lang.NoClassDefFoundError org/springframework/core/annotation/MergedAnnotations$SearchStrategy
    @Test
    public void test() {

        pool = new ThreadPoolExecutor(1, 2, 1000, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());


        for (int i = 0; i <2 ; i++) {
            pool.execute(buyAction);
        }

    }

}
