// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.base;

import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@FunctionalInterface
@GwtCompatible
public interface Predicate<T> extends java.util.function.Predicate<T>
{
    @CanIgnoreReturnValue
    boolean apply(@Nullable final T p0);
    
    boolean equals(@Nullable final Object p0);
    
    default boolean test(@Nullable final T input) {
        return this.apply(input);
    }
}
