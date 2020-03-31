package top.homesoft.java8;

import org.junit.Test;
import top.homesoft.java.common.User;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class OptionalDemo {

    public static void main(String[] args){
        OptionalDemo demo = new OptionalDemo();
        //demo.test();
        demo.whenMap_thenOk();
    }

    public Long defaultLong(){
        return 0L;
    }

    public void test(){
        Long testValue =null;
        Optional<Long> opt = Optional.ofNullable(testValue);
        testValue = opt.orElseGet(()->0L);
       // testValue = opt.orElseGet(new Long(0L));

        System.out.println( testValue);
    }
    
    @Test
    public void whenMap_thenOk() {
        User user = new User("anna@gmail.com", "1234");
          user = null;
        String email = Optional.ofNullable(user)
                .map(u -> u.getEmail()).orElse("default@gmail.com");

        System.out.println( email);
    }
}
