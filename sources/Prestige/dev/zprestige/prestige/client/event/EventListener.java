package dev.zprestige.prestige.client.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener {
    public Priority getPriority() default Priority.DEFAULT;
}