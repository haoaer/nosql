package course.nosql.intercept;

import course.nosql.util.JwtUtil;
import course.nosql.util.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

//全局拦截器
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token=request.getHeader("Authorization");
        try {
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            if(operations.get(token)==null){
                throw new RuntimeException();
            }
            Map<String,Object>claims= JwtUtil.parseToken(token);
            ThreadLocalUtil.set(claims); //将token存入线程
            return true;
        }catch (Exception e){
            System.out.println(token);
            response.setStatus(401); //未登录状态响应码
            return false;
        }

    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        //使用完毕，清理数据 防止内存泄漏
        ThreadLocalUtil.remove();
    }
}
