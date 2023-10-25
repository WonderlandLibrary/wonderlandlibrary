/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@GwtCompatible
final class Partially {
    private Partially() {
    }

    @Retention(value=RetentionPolicy.CLASS)
    @Target(value={ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
    @Documented
    static @interface GwtIncompatible {
        public String value();
    }
}

