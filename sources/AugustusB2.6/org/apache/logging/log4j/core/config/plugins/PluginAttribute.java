// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins;

import org.apache.logging.log4j.core.config.plugins.visitors.PluginAttributeVisitor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.Annotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@PluginVisitorStrategy(PluginAttributeVisitor.class)
public @interface PluginAttribute {
    boolean defaultBoolean() default false;
    
    byte defaultByte() default 0;
    
    char defaultChar() default '\0';
    
    Class<?> defaultClass() default Object.class;
    
    double defaultDouble() default 0.0;
    
    float defaultFloat() default 0.0f;
    
    int defaultInt() default 0;
    
    long defaultLong() default 0L;
    
    short defaultShort() default 0;
    
    String defaultString() default "";
    
    String value();
    
    boolean sensitive() default false;
}
