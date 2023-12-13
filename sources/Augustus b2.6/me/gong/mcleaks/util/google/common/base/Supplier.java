// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.base;

import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@FunctionalInterface
@GwtCompatible
public interface Supplier<T> extends java.util.function.Supplier<T>
{
    @CanIgnoreReturnValue
    T get();
}
