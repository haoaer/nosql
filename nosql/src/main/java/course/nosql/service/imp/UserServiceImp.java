package course.nosql.service.imp;

import course.nosql.reality.User;
import course.nosql.service.UserSerivce;
import course.nosql.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserServiceImp implements UserSerivce {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public User findByUserName(String username) {
        Query query = Query.query(Criteria.where("username").is(username));


        User user=mongoTemplate.findOne(query,User.class);
        return user;
    }

    @Override
    public void update(User user) {
        Query query=Query.query(Criteria.where("id").is(user.getId()));
        Update update=Update.update("nickname",user.getNickname())
                .set("email",user.getEmail())
                .set("updateTime",LocalDateTime.now());

        mongoTemplate.upsert(query,update,User.class);
    }

    @Override
    public void updateAvatar(String avatarurl) {
        Map<String,Object> map= ThreadLocalUtil.get();
        String id= (String)map.get("id");
        Query query=Query.query(Criteria.where("id").is(id));
        Update update=Update.update("userPic",avatarurl);
        mongoTemplate.upsert(query,update,User.class);


    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String,Object>map=ThreadLocalUtil.get();
        String id=(String)map.get("id");
        Query query=Query.query(Criteria.where("id").is(id));
        Update update=Update.update("password",newPwd);
        mongoTemplate.upsert(query,update,User.class);
    }

    @Override
    public void  register(String username,String password){
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        mongoTemplate.insert(user);
    }



}
