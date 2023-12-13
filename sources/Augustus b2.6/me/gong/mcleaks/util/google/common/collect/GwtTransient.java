// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.Annotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@GwtCompatible
@interface GwtTransient {
}
