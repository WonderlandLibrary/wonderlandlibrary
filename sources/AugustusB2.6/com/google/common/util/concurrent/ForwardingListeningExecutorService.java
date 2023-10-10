// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@ElementTypesAreNonnullByDefault
@CanIgnoreReturnValue
@GwtIncompatible
public abstract class ForwardingListeningExecutorService extends ForwardingExecutorService implements ListeningExecutorService
{
    protected ForwardingListeningExecutorService() {
    }
    
    @Override
    protected abstract ListeningExecutorService delegate();
    
    @Override
    public <T> ListenableFuture<T> submit(final Callable<T> task) {
        return this.delegate().submit(task);
    }
    
    @Override
    public ListenableFuture<?> submit(final Runnable task) {
        return this.delegate().submit(task);
    }
    
    @Override
    public <T> ListenableFuture<T> submit(final Runnable task, @ParametricNullness final T result) {
        return this.delegate().submit(task, result);
    }
}
