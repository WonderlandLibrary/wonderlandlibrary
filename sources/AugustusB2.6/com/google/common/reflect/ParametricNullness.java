// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import javax.annotation.meta.TypeQualifierNickname;
import com.google.common.annotations.GwtCompatible;
import javax.annotation.meta.When;
import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Nonnull(when = When.UNKNOWN)
@GwtCompatible
@TypeQualifierNickname
@interface ParametricNullness {
}
