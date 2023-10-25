package org.apache.logging.log4j.core.config.plugins.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.logging.log4j.core.util.ReflectionUtil;

public final class ConstraintValidators {
  public static Collection<ConstraintValidator<?>> findValidators(Annotation... annotations) {
    Collection<ConstraintValidator<?>> validators = new ArrayList<>();
    for (Annotation annotation : annotations) {
      Class<? extends Annotation> type = annotation.annotationType();
      if (type.isAnnotationPresent((Class)Constraint.class)) {
        ConstraintValidator<?> validator = getValidator(annotation, type);
        if (validator != null)
          validators.add(validator); 
      } 
    } 
    return validators;
  }
  
  private static <A extends Annotation> ConstraintValidator<A> getValidator(A annotation, Class<? extends A> type) {
    Constraint constraint = type.<Constraint>getAnnotation(Constraint.class);
    Class<? extends ConstraintValidator<?>> validatorClass = constraint.value();
    if (type.equals(getConstraintValidatorAnnotationType(validatorClass))) {
      ConstraintValidator<A> validator = (ConstraintValidator<A>)ReflectionUtil.instantiate(validatorClass);
      validator.initialize(annotation);
      return validator;
    } 
    return null;
  }
  
  private static Type getConstraintValidatorAnnotationType(Class<? extends ConstraintValidator<?>> type) {
    for (Type parentType : type.getGenericInterfaces()) {
      if (parentType instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType)parentType;
        if (ConstraintValidator.class.equals(parameterizedType.getRawType()))
          return parameterizedType.getActualTypeArguments()[0]; 
      } 
    } 
    return void.class;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\validation\ConstraintValidators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */