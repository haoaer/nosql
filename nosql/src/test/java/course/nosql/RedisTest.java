//package course.nosql;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.connection.StringRedisConnection;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//
//@SpringBootTest // 在单元测试方法执行前，初始化Spring容器
//public class RedisTest {
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//    @Test
//    public void test(){
//        ValueOperations<String,String>operations= stringRedisTemplate.opsForValue();
//        operations.set("username","zhangsan");
//
//    }
//
//    @Test
//    public void testGet(){
//        ValueOperations<String,String>operations= stringRedisTemplate.opsForValue();
//        System.out.println( operations.get("username"));
//    }
//}
