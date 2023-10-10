// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander;

import com.beust.jcommander.validators.NoValueValidator;
import com.beust.jcommander.validators.NoValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface DynamicParameter {
    String[] names() default {};
    
    boolean required() default false;
    
    String description() default "";
    
    String descriptionKey() default "";
    
    boolean hidden() default false;
    
    Class<? extends IParameterValidator>[] validateWith() default { NoValidator.class };
    
    String assignment() default "=";
    
    Class<? extends IValueValidator>[] validateValueWith() default { NoValueValidator.class };
}
