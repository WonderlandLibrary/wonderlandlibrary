// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;
import java.util.concurrent.RunnableFuture;

@ElementTypesAreNonnullByDefault
@GwtCompatible
class TrustedListenableFutureTask<V> extends TrustedFuture<V> implements RunnableFuture<V>
{
    @CheckForNull
    private volatile InterruptibleTask<?> task;
    
    static <V> TrustedListenableFutureTask<V> create(final AsyncCallable<V> callable) {
        return new TrustedListenableFutureTask<V>(callable);
    }
    
    static <V> TrustedListenableFutureTask<V> create(final Callable<V> callable) {
        return new TrustedListenableFutureTask<V>(callable);
    }
    
    static <V> TrustedListenableFutureTask<V> create(final Runnable runnable, @ParametricNullness final V result) {
        return new TrustedListenableFutureTask<V>(Executors.callable(runnable, result));
    }
    
    TrustedListenableFutureTask(final Callable<V> callable) {
        this.task = new TrustedFutureInterruptibleTask(callable);
    }
    
    TrustedListenableFutureTask(final AsyncCallable<V> callable) {
        this.task = new TrustedFutureInterruptibleAsyncTask(callable);
    }
    
    @Override
    public void run() {
        final InterruptibleTask<?> localTask = this.task;
        if (localTask != null) {
            localTask.run();
        }
        this.task = null;
    }
    
    @Override
    protected void afterDone() {
        super.afterDone();
        if (this.wasInterrupted()) {
            final InterruptibleTask<?> localTask = this.task;
            if (localTask != null) {
                localTask.interruptTask();
            }
        }
        this.task = null;
    }
    
    @CheckForNull
    @Override
    protected String pendingToString() {
        final InterruptibleTask<?> localTask = this.task;
        if (localTask != null) {
            final String value = String.valueOf(localTask);
            return new StringBuilder(7 + String.valueOf(value).length()).append("task=[").append(value).append("]").toString();
        }
        return super.pendingToString();
    }
    
    private final class TrustedFutureInterruptibleTask extends InterruptibleTask<V>
    {
        private final Callable<V> callable;
        
        TrustedFutureInterruptibleTask(final Callable<V> callable) {
            this.callable = Preconditions.checkNotNull(callable);
        }
        
        @Override
        final boolean isDone() {
            return TrustedListenableFutureTask.this.isDone();
        }
        
        @ParametricNullness
        @Override
        V runInterruptibly() throws Exception {
            return this.callable.call();
        }
        
        @Override
        void afterRanInterruptiblySuccess(@ParametricNullness final V result) {
            TrustedListenableFutureTask.this.set(result);
        }
        
        @Override
        void afterRanInterruptiblyFailure(final Throwable error) {
            TrustedListenableFutureTask.this.setException(error);
        }
        
        @Override
        String toPendingString() {
            return this.callable.toString();
        }
    }
    
    private final class TrustedFutureInterruptibleAsyncTask extends InterruptibleTask<ListenableFuture<V>>
    {
        private final AsyncCallable<V> callable;
        
        TrustedFutureInterruptibleAsyncTask(final AsyncCallable<V> callable) {
            this.callable = Preconditions.checkNotNull(callable);
        }
        
        @Override
        final boolean isDone() {
            return TrustedListenableFutureTask.this.isDone();
        }
        
        @Override
        ListenableFuture<V> runInterruptibly() throws Exception {
            return Preconditions.checkNotNull(this.callable.call(), "AsyncCallable.call returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", this.callable);
        }
        
        @Override
        void afterRanInterruptiblySuccess(final ListenableFuture<V> result) {
            TrustedListenableFutureTask.this.setFuture(result);
        }
        
        @Override
        void afterRanInterruptiblyFailure(final Throwable error) {
            TrustedListenableFutureTask.this.setException(error);
        }
        
        @Override
        String toPendingString() {
            return this.callable.toString();
        }
    }
}
