package com.initial.events;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTarget {
    byte value() default 2;
}
