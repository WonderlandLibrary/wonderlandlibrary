/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.mapped;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface MappedType {
    public int padding() default 0;

    public boolean cacheLinePadding() default false;

    public int align() default 4;

    public boolean autoGenerateOffsets() default true;
}

