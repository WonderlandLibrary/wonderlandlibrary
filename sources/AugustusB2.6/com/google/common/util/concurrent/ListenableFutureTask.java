// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executor;
import java.util.concurrent.Callable;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.FutureTask;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public class ListenableFutureTask<V> extends FutureTask<V> implements ListenableFuture<V>
{
    private final ExecutionList executionList;
    
    public static <V> ListenableFutureTask<V> create(final Callable<V> callable) {
        return new ListenableFutureTask<V>(callable);
    }
    
    public static <V> ListenableFutureTask<V> create(final Runnable runnable, @ParametricNullness final V result) {
        return new ListenableFutureTask<V>(runnable, result);
    }
    
    ListenableFutureTask(final Callable<V> callable) {
        super(callable);
        this.executionList = new ExecutionList();
    }
    
    ListenableFutureTask(final Runnable runnable, @ParametricNullness final V result) {
        super(runnable, result);
        this.executionList = new ExecutionList();
    }
    
    @Override
    public void addListener(final Runnable listener, final Executor exec) {
        this.executionList.add(listener, exec);
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    @Override
    public V get(final long timeout, final TimeUnit unit) throws TimeoutException, InterruptedException, ExecutionException {
        final long timeoutNanos = unit.toNanos(timeout);
        if (timeoutNanos <= 2147483647999999999L) {
            return super.get(timeout, unit);
        }
        return super.get(Math.min(timeoutNanos, 2147483647999999999L), TimeUnit.NANOSECONDS);
    }
    
    @Override
    protected void done() {
        this.executionList.execute();
    }
}
