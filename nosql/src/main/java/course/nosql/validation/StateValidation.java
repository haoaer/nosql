package course.nosql.validation;

import course.nosql.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.util.annotation.ConstraintAnnotationDescriptor;

// state自动定义校验
public class StateValidation implements ConstraintValidator<State,String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value==null)
            return false;
        if(value.equals("已发布")||value.equals("草稿")){
            return true;
        }
        return false;
    }
}
