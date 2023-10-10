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
public interface Predicate<T> extends java.util.function.Predicate<T>
{
    @CanIgnoreReturnValue
    boolean apply(@ParametricNullness final T p0);
    
    boolean equals(@CheckForNull final Object p0);
    
    default boolean test(@ParametricNullness final T input) {
        return this.apply(input);
    }
}
