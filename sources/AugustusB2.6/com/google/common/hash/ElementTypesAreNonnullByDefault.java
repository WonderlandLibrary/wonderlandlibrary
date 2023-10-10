// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@TypeQualifierDefault({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Nonnull
@GwtCompatible
@interface ElementTypesAreNonnullByDefault {
}
