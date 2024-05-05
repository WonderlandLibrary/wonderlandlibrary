package ru.smertnix.celestial.event;

import java.lang.annotation.*;

import ru.smertnix.celestial.event.types.Priority;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTarget {

    byte value() default Priority.MEDIUM;
}
