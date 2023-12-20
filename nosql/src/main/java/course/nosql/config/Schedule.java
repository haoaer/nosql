package course.nosql.config;

import course.nosql.reality.Articlelike;
import course.nosql.reality.Articlescan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Schedule {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();//序列化为String
        //不能反序列化
        //Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);//序列化为Json
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(serializer);
        this.redisTemplate = redisTemplate;
    }
    //十小时 “0 0 0/10 * * ? ”
    //十分钟 "0 */10 * * * ?"
    //五秒钟 "* /5 * * * * ?"
//十分钟执行一次
    @Scheduled(cron = "0 */10 * * * ?")
    private void printNowDate() {
        Set<String> keys=redisTemplate.keys("scan"+"*");
        for(String t:keys){
            Query query= Query.query(Criteria.where("id").is(t));
            Update update=Update.update("count",redisTemplate.opsForValue().get(t));
            mongoTemplate.upsert(query, update, Articlescan.class);
            redisTemplate.delete(t);
        }
        keys=redisTemplate.keys("like"+"*");
        for(String t:keys){
            Set<String>key=redisTemplate.boundHashOps(t).keys();
            for(String userid:key){
                Query query= Query.query(Criteria.where("articleid").is(t));
                query.addCriteria(Criteria.where("userid").is(userid));
                // 没有数据则插入数据
                if(mongoTemplate.findOne(query, Articlelike.class)==null){
                    Articlelike articlelike=new Articlelike();
                    articlelike.setArticleid(t);
                    articlelike.setUserid(userid);
                    articlelike.setValue((String) redisTemplate.opsForHash().get(t,userid));
                    mongoTemplate.insert(articlelike);
                }
                // 有数据则更新数据
                else{
                    Update update=Update.update("value",(String) redisTemplate.opsForHash().get(t,userid));
                    mongoTemplate.upsert(query,update,Articlelike.class);
                }

            }


            redisTemplate.delete(t);

        }

    }

}
