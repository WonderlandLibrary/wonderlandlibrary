// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander;

import com.beust.jcommander.converters.CommaParameterSplitter;
import com.beust.jcommander.converters.IParameterSplitter;
import com.beust.jcommander.validators.NoValueValidator;
import com.beust.jcommander.validators.NoValidator;
import com.beust.jcommander.converters.NoConverter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Parameter {
    String[] names() default {};
    
    String description() default "";
    
    boolean required() default false;
    
    String descriptionKey() default "";
    
    int arity() default -1;
    
    boolean password() default false;
    
    Class<? extends IStringConverter<?>> converter() default NoConverter.class;
    
    Class<? extends IStringConverter<?>> listConverter() default NoConverter.class;
    
    boolean hidden() default false;
    
    Class<? extends IParameterValidator>[] validateWith() default { NoValidator.class };
    
    Class<? extends IValueValidator>[] validateValueWith() default { NoValueValidator.class };
    
    boolean variableArity() default false;
    
    Class<? extends IParameterSplitter> splitter() default CommaParameterSplitter.class;
    
    boolean echoInput() default false;
    
    boolean help() default false;
    
    boolean forceNonOverwritable() default false;
    
    int order() default -1;
}
