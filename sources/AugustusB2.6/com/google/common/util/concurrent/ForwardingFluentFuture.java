// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class ForwardingFluentFuture<V> extends FluentFuture<V>
{
    private final ListenableFuture<V> delegate;
    
    ForwardingFluentFuture(final ListenableFuture<V> delegate) {
        this.delegate = Preconditions.checkNotNull(delegate);
    }
    
    @Override
    public void addListener(final Runnable listener, final Executor executor) {
        this.delegate.addListener(listener, executor);
    }
    
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return this.delegate.cancel(mayInterruptIfRunning);
    }
    
    @Override
    public boolean isCancelled() {
        return this.delegate.isCancelled();
    }
    
    @Override
    public boolean isDone() {
        return this.delegate.isDone();
    }
    
    @ParametricNullness
    @Override
    public V get() throws InterruptedException, ExecutionException {
        return this.delegate.get();
    }
    
    @ParametricNullness
    @Override
    public V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.delegate.get(timeout, unit);
    }
    
    @Override
    public String toString() {
        return this.delegate.toString();
    }
}
