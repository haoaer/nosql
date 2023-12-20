package course.nosql.service;

import course.nosql.reality.User;
import org.springframework.stereotype.Service;

public interface UserSerivce {


    //用户注册
    void register(String username, String password);

    // 获取用户信息
    User findByUserName(String username);

    //更新用户
    void update(User user);

    //更新头像
    void updateAvatar(String avatarurl);

    //更新密码
    void updatePwd(String newPwd);
}
