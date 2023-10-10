// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import javax.annotation.CheckForNull;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface Function<F, T> extends java.util.function.Function<F, T>
{
    @ParametricNullness
    @CanIgnoreReturnValue
    T apply(@ParametricNullness final F p0);
    
    boolean equals(@CheckForNull final Object p0);
}
