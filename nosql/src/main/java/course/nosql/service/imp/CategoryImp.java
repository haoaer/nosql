package course.nosql.service.imp;

import course.nosql.reality.Category;
import course.nosql.reality.Article;

import course.nosql.service.ArticleService;
import course.nosql.service.CategotyService;
import course.nosql.util.GetId;
import course.nosql.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class CategoryImp implements CategotyService {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void add(Category category) {
        Map<String,Object> map= ThreadLocalUtil.get();
        String userid= (String)map.get("id");
        category.setCreateUser(userid);
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateTime(LocalDateTime.now());
        category.setId(GetId.GetUUId());

        mongoTemplate.insert(category);

    }

    // 获取当前用户的分类种类
    @Override
    public List<Category> list() {
        Map<String,Object> map= ThreadLocalUtil.get();
        String userid= (String) map.get("id");
        Query query=Query.query(Criteria.where("createUser").is(userid));
        return mongoTemplate.find(query,Category.class);
    }

    @Override
    public Category findById(String id) {
        Query query=Query.query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query,Category.class);

    }

    @Override
    public void update(Category category) {

        Query query=Query.query(Criteria.where("id").is(category.getId()));
        Update update=Update.update("categoryName",category.getCategoryName())
                .set("categoryAlias",category.getCategoryAlias())
                .set("updateTime",LocalDateTime.now());
        mongoTemplate.upsert(query,update, Category.class);
    }

    @Override
    public void delete(String id) {
        Query query=Query.query(Criteria.where("id").is(id));
        mongoTemplate.remove(query,Category.class);
        query=Query.query(Criteria.where("categoryId").is(id));
        mongoTemplate.remove(query, Article.class);
    }

    // 获取所有文章的种类名 相比list目的是为了过滤重复值
    @Override
    public List<Category> listall() {

        List<Category> list=mongoTemplate.findAll(Category.class);
        List<Category>ca=new ArrayList<Category>();
        List<String> ll=new ArrayList<String>();
        for(Category i:list){
            if(!ll.contains(i.getCategoryName())){
                ll.add(i.getCategoryName());
                ca.add(i);
            }
        }
        return ca;
    }

}
