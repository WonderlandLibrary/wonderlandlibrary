/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.event.eventbus;

import com.wallhacks.losebypass.event.eventbus.EventPriority;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface SubscribeEvent {
    public EventPriority priority() default EventPriority.NONE;
}

