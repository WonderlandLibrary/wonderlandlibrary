// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.Annotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Plugin {
    public static final String EMPTY = "";
    
    String name();
    
    String category();
    
    String elementType() default "";
    
    boolean printObject() default false;
    
    boolean deferChildren() default false;
}
