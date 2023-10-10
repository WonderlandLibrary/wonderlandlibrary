// 
// Decompiled by Procyon v0.5.36
// 

package oshi.annotation.concurrent;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface GuardedBy {
    String value();
}
