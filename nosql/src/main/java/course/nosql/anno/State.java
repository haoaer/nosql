package course.nosql.anno;

import course.nosql.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


//自定义注解
@Documented
@Target({FIELD})  //注解的作用是属性
@Retention(RUNTIME)
@Constraint(validatedBy = {StateValidation.class}) //注解内容是这个类的
public @interface State {

    //提供校验失败后的信息
    String message() default "state 参数只能是发布或草稿";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
