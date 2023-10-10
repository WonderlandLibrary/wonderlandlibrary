// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander;

import java.lang.annotation.Inherited;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface Parameters {
    String resourceBundle() default "";
    
    String separators() default " ";
    
    String commandDescription() default "";
    
    String commandDescriptionKey() default "";
    
    String[] commandNames() default {};
    
    boolean hidden() default false;
}
