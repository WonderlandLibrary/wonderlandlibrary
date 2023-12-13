// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
final class Partially
{
    private Partially() {
    }
    
    @Retention(RetentionPolicy.CLASS)
    @Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD })
    @Documented
    @interface GwtIncompatible {
        String value();
    }
}
