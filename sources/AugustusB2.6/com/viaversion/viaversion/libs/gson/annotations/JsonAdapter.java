// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
public @interface JsonAdapter {
    Class<?> value();
    
    boolean nullSafe() default true;
}
