/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.CanIgnoreReturnValue
 */
package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;

@FunctionalInterface
@GwtCompatible
public interface Function<F, T>
extends java.util.function.Function<F, T> {
    @Override
    @Nullable
    @CanIgnoreReturnValue
    public T apply(@Nullable F var1);

    public boolean equals(@Nullable Object var1);
}

