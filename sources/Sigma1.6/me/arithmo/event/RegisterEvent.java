/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.event;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import me.arithmo.event.Event;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface RegisterEvent {
    public Class<? extends Event>[] events();
}

