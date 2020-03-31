package s;

import java.util.List;

import redis.clients.jedis.Jedis;
import s.IdGenerator;

public class Station extends Thread {
 
	 //  定义连接Redis 数据库方法
	 public static void connectRedis (){ 
		 String tab = "order";
			long userId = 123456789;

			IdGenerator idGenerator = IdGenerator.builder()
					.addHost("127.0.0.1", 6379, "asafdsjhvhdisuahfiwe123")  
					.build();

			long id=0;
			id = idGenerator.next(tab, userId);
			System.out.println("id:" + id);
			
			List<Long> result = IdGenerator.parseId(id);

			System.out.println("miliSeconds:" + result.get(0) + ", partition:"
					+ result.get(1) + ", seq:" + result.get(2));
	    }
    public Station(String name) {
       super(name);
    }
    static int tick = 20;
    static Object ob = "aa";
    @Override
    public void run() {
      while (tick > 0) {
        synchronized (ob) {
          if (tick > 0) {
        	connectRedis();
            tick--;
          } else {
            System.out.println("完成");
          }
        }
        try {
           sleep(100);//休息
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
    public void run1(){
    	
    }
}