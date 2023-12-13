// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import me.gong.mcleaks.util.google.common.annotations.Beta;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class SettableFuture<V> extends TrustedFuture<V>
{
    public static <V> SettableFuture<V> create() {
        return new SettableFuture<V>();
    }
    
    @CanIgnoreReturnValue
    public boolean set(@Nullable final V value) {
        return super.set(value);
    }
    
    @CanIgnoreReturnValue
    public boolean setException(final Throwable throwable) {
        return super.setException(throwable);
    }
    
    @Beta
    @CanIgnoreReturnValue
    public boolean setFuture(final ListenableFuture<? extends V> future) {
        return super.setFuture(future);
    }
    
    private SettableFuture() {
    }
}
