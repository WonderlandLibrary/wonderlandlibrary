// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.base;

import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@FunctionalInterface
@GwtCompatible
public interface Function<F, T> extends java.util.function.Function<F, T>
{
    @Nullable
    @CanIgnoreReturnValue
    T apply(@Nullable final F p0);
    
    boolean equals(@Nullable final Object p0);
}
