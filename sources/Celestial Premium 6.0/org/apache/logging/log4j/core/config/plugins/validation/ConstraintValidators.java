/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.plugins.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.logging.log4j.core.config.plugins.validation.Constraint;
import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidator;
import org.apache.logging.log4j.core.util.ReflectionUtil;

public final class ConstraintValidators {
    private ConstraintValidators() {
    }

    public static Collection<ConstraintValidator<?>> findValidators(Annotation ... annotations) {
        ArrayList validators = new ArrayList();
        for (Annotation annotation : annotations) {
            ConstraintValidator<? extends Annotation> validator;
            Class<? extends Annotation> type = annotation.annotationType();
            if (!type.isAnnotationPresent(Constraint.class) || (validator = ConstraintValidators.getValidator(annotation, type)) == null) continue;
            validators.add(validator);
        }
        return validators;
    }

    private static <A extends Annotation> ConstraintValidator<A> getValidator(A annotation, Class<? extends A> type) {
        Constraint constraint = type.getAnnotation(Constraint.class);
        Class<? extends ConstraintValidator<? extends Annotation>> validatorClass = constraint.value();
        if (type.equals(ConstraintValidators.getConstraintValidatorAnnotationType(validatorClass))) {
            ConstraintValidator<? extends Annotation> validator = ReflectionUtil.instantiate(validatorClass);
            validator.initialize(annotation);
            return validator;
        }
        return null;
    }

    private static Type getConstraintValidatorAnnotationType(Class<? extends ConstraintValidator<?>> type) {
        for (Type parentType : type.getGenericInterfaces()) {
            ParameterizedType parameterizedType;
            if (!(parentType instanceof ParameterizedType) || !ConstraintValidator.class.equals((Object)(parameterizedType = (ParameterizedType)parentType).getRawType())) continue;
            return parameterizedType.getActualTypeArguments()[0];
        }
        return Void.TYPE;
    }
}

