/*
 * Decompiled with CFR 0.152.
 */
package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonPropertyOrder {
    public String[] value() default {};

    public boolean alphabetic() default false;
}

