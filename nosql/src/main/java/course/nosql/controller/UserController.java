package course.nosql.controller;

import course.nosql.reality.Result;
import course.nosql.reality.User;
import course.nosql.service.UserSerivce;
import course.nosql.util.JwtUtil;
import course.nosql.util.ThreadLocalUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserSerivce userSerivce;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username,@Pattern(regexp = "^\\S{5,16}$") String password){
        User user=userSerivce.findByUserName(username);
        if(user==null){
            //用户名没被占用
            userSerivce.register(username,password);
            return Result.success();

        }
        else {
            //注册失败
            return Result.error("用户名已被占用");

        }
    }
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username,@Pattern(regexp = "^\\S{5,16}$") String password){
        User loginUser=userSerivce.findByUserName(username);
        if(loginUser==null){
            return Result.error("用户名不存在");
        }

        if(loginUser.getPassword().equals(password)){
            //登录成功  生成令牌
            Map<String,Object>claims=new HashMap<>();
            claims.put("id",loginUser.getId());
            claims.put("username",loginUser.getUsername());
            String token= JwtUtil.getToken(claims);
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,12, TimeUnit.HOURS);

            return Result.success(token);
        }

        return Result.error("密码错误");
    }

    @GetMapping("/userInfo")
    public Result<User>userInfo(/*@RequestHeader(name="Authorization") String token*/){
//        Map<String,Object>map=JwtUtil.parseToken(token);
//        String username=(String)map.get("username");
        //获取当前线程的用户信息
        Map<String,Object>map= ThreadLocalUtil.get();
        String username=(String)map.get("username");
        User user=userSerivce.findByUserName(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    // @Validated 让校验生效
    public Result update(@RequestBody @Validated User user){
        userSerivce.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updataAvatar(@RequestParam @URL String avatarUrl){ //参数必须为avatarUrl 一定要一致！！！
        userSerivce.updateAvatar(avatarUrl);
        return Result.success("修改成功");
    }
    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> params, @RequestHeader("Authorization") String token ){
        //校验参数
        String oldPwd=params.get("old_pwd");
        String newPwd=params.get("new_pwd");
        String rePwd=params.get("re_pwd");

        if(!StringUtils.hasLength(oldPwd)||!StringUtils.hasLength(newPwd)||!StringUtils.hasLength(rePwd)){
            return Result.error("参数完整");
        }
        // 原密码是否正确
        Map<String ,Object>map=ThreadLocalUtil.get();
        String username=(String)map.get("username") ;
        User user=userSerivce.findByUserName(username);
        if(!oldPwd.equals(user.getPassword())){
            return Result.error("原密码不正确");
        }
        if(newPwd.length()<5||newPwd.length()>16){
            return Result.error("密码格式不正确");
        }
        if(!newPwd.equals(rePwd)){
            return Result.error("两次密码不一致");
        }

        userSerivce.updatePwd(newPwd);
        //删除原来的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }

}
