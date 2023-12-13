// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.concurrent.Future;
import java.util.concurrent.Executor;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;

@CanIgnoreReturnValue
@GwtCompatible
public abstract class ForwardingListenableFuture<V> extends ForwardingFuture<V> implements ListenableFuture<V>
{
    protected ForwardingListenableFuture() {
    }
    
    @Override
    protected abstract ListenableFuture<? extends V> delegate();
    
    @Override
    public void addListener(final Runnable listener, final Executor exec) {
        this.delegate().addListener(listener, exec);
    }
    
    public abstract static class SimpleForwardingListenableFuture<V> extends ForwardingListenableFuture<V>
    {
        private final ListenableFuture<V> delegate;
        
        protected SimpleForwardingListenableFuture(final ListenableFuture<V> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }
        
        @Override
        protected final ListenableFuture<V> delegate() {
            return this.delegate;
        }
    }
}
