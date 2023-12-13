// 
// Decompiled by Procyon v0.6.0
// 

package net.lenni0451.eventapi.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface EventTarget {
    byte priority() default 2;
}
