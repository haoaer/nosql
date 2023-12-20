package course.nosql.service.imp;

import course.nosql.reality.*;
import course.nosql.service.ArticleService;
import course.nosql.util.GetId;
import course.nosql.util.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.Set;


@Service
public class ArticleImp implements ArticleService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();//序列化为String
        //不能反序列化
        //Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);//序列化为Json
//        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        GenericToStringSerializer serializer = new GenericToStringSerializer<>(Object.class);
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(serializer);
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void add(Article article) {
        Map<String,Object> map= ThreadLocalUtil.get();
        String userid= (String)map.get("id");
        article.setCreateUser(userid);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());

        Query query=Query.query(Criteria.where("id").is(article.getCategoryId()));
        Category category=mongoTemplate.findOne(query,Category.class);
        article.setCategoryName(category.getCategoryName());
        String id=GetId.GetUUId();
        article.setId(id);

        Articlescan articlescan=new Articlescan();
        id="scan"+id;
        articlescan.setId(id);
        articlescan.setCount("0");

        mongoTemplate.insert(article);

        mongoTemplate.insert(articlescan);
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, String categoryId, String state) {
        //创建PageBean对象
        PageBean<Article>pageBean=new PageBean<>();
        Map<String,Object>map=ThreadLocalUtil.get();
        String userId=(String) map.get("id");
        Query query = Query.query(Criteria.where("createUser").is(userId));
        if(categoryId!=null){
            query.addCriteria(Criteria.where("categoryId").is(categoryId));
        }
        if(state!=null){
            query.addCriteria(Criteria.where("state").is(state));
        }

// 设置起始页和每页查询条数 pageNum从0开始的
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
// 查询记录总数
        Long totalCount = mongoTemplate.count(query,Article.class);
//查询分页后的记录
        List<Article> result = mongoTemplate.find(query.with(pageable), Article.class,"article");
        pageBean.setTotal(totalCount);
        pageBean.setItems(result);
        return pageBean;

    }

    @Override
    public void update(Article article) {
        Query query=Query.query(Criteria.where("id").is(article.getCategoryId()));
        Category category=mongoTemplate.findOne(query,Category.class);
        query=Query.query(Criteria.where("id").is(article.getId()));
        Update update=Update.update("title",article.getTitle())
                .set("content",article.getContent())
                .set("coverImg",article.getCoverImg())
                .set("state",article.getState())
                .set("categoryId",article.getCategoryId())
                .set("categoryName",category.getCategoryName())
                .set("updateTime",LocalDateTime.now());
        mongoTemplate.upsert(query,update,Article.class);

    }

    @Override
    public Article detail(String id) {
        Query query=Query.query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query,Article.class);
    }

    @Override
    public void delete(String id) {
        Query query=Query.query(Criteria.where("id").is(id));
        // 删除文章信息
        mongoTemplate.remove(query,Article.class);
        //删除浏览量
        redisTemplate.delete(id);
        //删除点赞
        redisTemplate.delete(id+"like");

    }

    @Override
    public PageBean<Article> listall(Integer pageNum, Integer pageSize,String categoryId ) {
        PageBean<Article>pageBean=new PageBean<>();
        Query query = Query.query(Criteria.where("state").is("已发布"));
        if(categoryId!=null){
            query.addCriteria(Criteria.where("categoryId").is(categoryId));
        }
// 设置起始页和每页查询条数 pageNum从0开始的
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
// 查询记录总数
        Long totalCount = mongoTemplate.count(query,Article.class);
//查询分页后的记录
        List<Article> result = mongoTemplate.find(query.with(pageable), Article.class,"article");
        pageBean.setTotal(totalCount);
        pageBean.setItems(result);
        return pageBean;
    }

    @Override
    public Long scan(String id) {
        Long count;
        id="scan"+id;

        // redis中没有数据，从mongodb里面读取
        if(!redisTemplate.hasKey(id)){

            Articlescan articlescan= new Articlescan();
            Query query=Query.query(Criteria.where("id").is(id));
            articlescan= mongoTemplate.findOne(query,Articlescan.class);
            redisTemplate.opsForValue().set(id,(String) (articlescan.getCount()));
        }
        count=redisTemplate.opsForValue().increment(id,1);

        return count;

    }

    @Override
    public Like getlike(String articleId) {
        // 此处用的是hash 但已经有articleid的key了 于是加一个在后面加一个like
        articleId="like"+articleId;
        Like like=new Like();
        Map<String,Object> map= ThreadLocalUtil.get();
        String userid= (String)map.get("id");

        // 没有找到  从mongodb里面读取
        if(!redisTemplate.hasKey(articleId)){
            Query query = Query.query(Criteria.where("articleid").is(articleId));
            List<Articlelike> result = mongoTemplate.find(query, Articlelike.class,"articlelike");
            for(Articlelike articlelike:result){
                redisTemplate.opsForHash().put(articleId,userid,articlelike.getValue());
            }
        }
        // 是否对改文章点赞
        if(redisTemplate.opsForHash().get(articleId,userid)==null){
            like.setTag(false);
        }
        else if(redisTemplate.opsForHash().get(articleId,userid).equals("1")){
            like.setTag(true);
        }
        else{
            like.setTag(false);
        }
        int count=0;
        Set<String>key=redisTemplate.boundHashOps(articleId).keys();
        for(String t:key){
            if(redisTemplate.opsForHash().get(articleId,t).equals("1"))
                count++;
        }

        like.setLike(count);
        return like;
    }

    @Override
    public void changelike(String articleId) {


        articleId="like"+articleId ;
        Map<String, Object> map = ThreadLocalUtil.get();
        String userid= (String)map.get("id");
        if(!redisTemplate.hasKey(articleId)){
            Query query = Query.query(Criteria.where("articleid").is(articleId));
            List<Articlelike> result = mongoTemplate.find(query, Articlelike.class,"articlelike");
            for(Articlelike articlelike:result){
                redisTemplate.opsForHash().put(articleId,userid,articlelike.getValue());
            }
        }
        // 已经点赞就删掉 没点赞就变成点赞
        if(redisTemplate.opsForHash().get(articleId,userid)==null){
            redisTemplate.opsForHash().put(articleId,userid,"1");
        }
        if (redisTemplate.opsForHash().get(articleId,userid).equals("1")) {
            redisTemplate.opsForHash().put(articleId,userid,"0");
        }
        else{
            redisTemplate.opsForHash().put(articleId,userid,"1");
        }

    }
}
