package koks.api.manager.value.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Value {
    String name();

    String displayName() default "";

    boolean colorPicker() default false;

    boolean visual() default false;

    double minimum() default 0;

    double maximum() default 0;

    String[] modes() default {};
}
