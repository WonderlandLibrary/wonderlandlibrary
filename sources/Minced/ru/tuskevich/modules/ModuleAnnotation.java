// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleAnnotation {
    String name();
    
    String desc() default "";
    
    Type type();
}
