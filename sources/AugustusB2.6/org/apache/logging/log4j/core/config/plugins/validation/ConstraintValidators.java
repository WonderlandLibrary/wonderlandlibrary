// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.validation;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.apache.logging.log4j.core.util.ReflectionUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.lang.annotation.Annotation;

public final class ConstraintValidators
{
    private ConstraintValidators() {
    }
    
    public static Collection<ConstraintValidator<?>> findValidators(final Annotation... annotations) {
        final Collection<ConstraintValidator<?>> validators = new ArrayList<ConstraintValidator<?>>();
        for (final Annotation annotation : annotations) {
            final Class<? extends Annotation> type = annotation.annotationType();
            if (type.isAnnotationPresent(Constraint.class)) {
                final ConstraintValidator<?> validator = getValidator((Object)annotation, type);
                if (validator != null) {
                    validators.add(validator);
                }
            }
        }
        return validators;
    }
    
    private static <A extends Annotation> ConstraintValidator<A> getValidator(final A annotation, final Class<? extends A> type) {
        final Constraint constraint = type.getAnnotation(Constraint.class);
        final Class<? extends ConstraintValidator<?>> validatorClass = constraint.value();
        if (type.equals(getConstraintValidatorAnnotationType(validatorClass))) {
            final ConstraintValidator<A> validator = ReflectionUtil.instantiate(validatorClass);
            validator.initialize(annotation);
            return validator;
        }
        return null;
    }
    
    private static Type getConstraintValidatorAnnotationType(final Class<? extends ConstraintValidator<?>> type) {
        for (final Type parentType : type.getGenericInterfaces()) {
            if (parentType instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType)parentType;
                if (ConstraintValidator.class.equals(parameterizedType.getRawType())) {
                    return parameterizedType.getActualTypeArguments()[0];
                }
            }
        }
        return Void.TYPE;
    }
}
