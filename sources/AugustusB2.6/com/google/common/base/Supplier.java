// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface Supplier<T> extends java.util.function.Supplier<T>
{
    @ParametricNullness
    @CanIgnoreReturnValue
    T get();
}
