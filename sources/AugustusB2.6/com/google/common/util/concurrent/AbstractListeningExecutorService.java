// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;
import java.util.concurrent.AbstractExecutorService;

@ElementTypesAreNonnullByDefault
@Beta
@CanIgnoreReturnValue
@GwtIncompatible
public abstract class AbstractListeningExecutorService extends AbstractExecutorService implements ListeningExecutorService
{
    @Override
    protected final <T> RunnableFuture<T> newTaskFor(final Runnable runnable, @ParametricNullness final T value) {
        return TrustedListenableFutureTask.create(runnable, value);
    }
    
    @Override
    protected final <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
        return TrustedListenableFutureTask.create(callable);
    }
    
    @Override
    public ListenableFuture<?> submit(final Runnable task) {
        return (ListenableFuture<?>)(ListenableFuture)super.submit(task);
    }
    
    @Override
    public <T> ListenableFuture<T> submit(final Runnable task, @ParametricNullness final T result) {
        return (ListenableFuture<T>)(ListenableFuture)super.submit(task, result);
    }
    
    @Override
    public <T> ListenableFuture<T> submit(final Callable<T> task) {
        return (ListenableFuture<T>)(ListenableFuture)super.submit(task);
    }
}
