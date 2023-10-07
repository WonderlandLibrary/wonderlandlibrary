package io.mxngo.echo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code Callback} annotation is used to mark fields that represent event callbacks within a subscriber.
 * It supports setting priority levels to determine the order of execution for multiple subscribers.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Callback {
    byte priority() default 0;
}
