// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class SettableFuture<V> extends TrustedFuture<V>
{
    public static <V> SettableFuture<V> create() {
        return new SettableFuture<V>();
    }
    
    @CanIgnoreReturnValue
    public boolean set(@ParametricNullness final V value) {
        return super.set(value);
    }
    
    @CanIgnoreReturnValue
    public boolean setException(final Throwable throwable) {
        return super.setException(throwable);
    }
    
    @CanIgnoreReturnValue
    public boolean setFuture(final ListenableFuture<? extends V> future) {
        return super.setFuture(future);
    }
    
    private SettableFuture() {
    }
}
