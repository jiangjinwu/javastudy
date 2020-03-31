package top.homesoft.java.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpringBootRedisBuyAction implements IBuyAction {

    public static int count=100;

//    @Autowired
//    RedissonClient redissonClient;

    //@Lock(keys = "#user.name",keyConstant = "常量"))
    public   void run() {
        RedisLockImitateSpringBoot redissonClient = new RedisLockImitateSpringBoot();

        //RLock rLock = redissonClient.getLock("test");
        redissonClient.lock("xx");
        while(count>0){

            redissonClient.lock("aaa");

           // rLock.lock();
//            synchronized(this) {
                if (count > 0) {
                    System.out.println(String.format("%s 卖出第%d张票", Thread.currentThread().getName(), count));
                    count--;
                }

//            }
             redissonClient.unlock("aaa");
          //  rLock.unlock();
        }

    }
}
