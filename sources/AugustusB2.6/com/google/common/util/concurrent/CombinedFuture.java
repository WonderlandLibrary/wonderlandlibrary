// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import com.google.common.base.Preconditions;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import com.google.common.collect.ImmutableCollection;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class CombinedFuture<V> extends AggregateFuture<Object, V>
{
    @CheckForNull
    private CombinedFutureInterruptibleTask<?> task;
    
    CombinedFuture(final ImmutableCollection<? extends ListenableFuture<?>> futures, final boolean allMustSucceed, final Executor listenerExecutor, final AsyncCallable<V> callable) {
        super(futures, allMustSucceed, false);
        this.task = new AsyncCallableInterruptibleTask(callable, listenerExecutor);
        this.init();
    }
    
    CombinedFuture(final ImmutableCollection<? extends ListenableFuture<?>> futures, final boolean allMustSucceed, final Executor listenerExecutor, final Callable<V> callable) {
        super(futures, allMustSucceed, false);
        this.task = new CallableInterruptibleTask(callable, listenerExecutor);
        this.init();
    }
    
    @Override
    void collectOneValue(final int index, @CheckForNull final Object returnValue) {
    }
    
    @Override
    void handleAllCompleted() {
        final CombinedFutureInterruptibleTask<?> localTask = this.task;
        if (localTask != null) {
            localTask.execute();
        }
    }
    
    @Override
    void releaseResources(final ReleaseResourcesReason reason) {
        super.releaseResources(reason);
        if (reason == ReleaseResourcesReason.OUTPUT_FUTURE_DONE) {
            this.task = null;
        }
    }
    
    @Override
    protected void interruptTask() {
        final CombinedFutureInterruptibleTask<?> localTask = this.task;
        if (localTask != null) {
            localTask.interruptTask();
        }
    }
    
    private abstract class CombinedFutureInterruptibleTask<T> extends InterruptibleTask<T>
    {
        private final Executor listenerExecutor;
        
        CombinedFutureInterruptibleTask(final Executor listenerExecutor) {
            this.listenerExecutor = Preconditions.checkNotNull(listenerExecutor);
        }
        
        @Override
        final boolean isDone() {
            return CombinedFuture.this.isDone();
        }
        
        final void execute() {
            try {
                this.listenerExecutor.execute(this);
            }
            catch (RejectedExecutionException e) {
                CombinedFuture.this.setException(e);
            }
        }
        
        @Override
        final void afterRanInterruptiblySuccess(@ParametricNullness final T result) {
            CombinedFuture.this.task = null;
            this.setValue(result);
        }
        
        @Override
        final void afterRanInterruptiblyFailure(final Throwable error) {
            CombinedFuture.this.task = null;
            if (error instanceof ExecutionException) {
                CombinedFuture.this.setException(((ExecutionException)error).getCause());
            }
            else if (error instanceof CancellationException) {
                CombinedFuture.this.cancel(false);
            }
            else {
                CombinedFuture.this.setException(error);
            }
        }
        
        abstract void setValue(@ParametricNullness final T p0);
    }
    
    private final class AsyncCallableInterruptibleTask extends CombinedFutureInterruptibleTask<ListenableFuture<V>>
    {
        private final AsyncCallable<V> callable;
        
        AsyncCallableInterruptibleTask(final AsyncCallable<V> callable, final Executor listenerExecutor) {
            super(listenerExecutor);
            this.callable = Preconditions.checkNotNull(callable);
        }
        
        @Override
        ListenableFuture<V> runInterruptibly() throws Exception {
            final ListenableFuture<V> result = this.callable.call();
            return Preconditions.checkNotNull(result, "AsyncCallable.call returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", this.callable);
        }
        
        @Override
        void setValue(final ListenableFuture<V> value) {
            CombinedFuture.this.setFuture(value);
        }
        
        @Override
        String toPendingString() {
            return this.callable.toString();
        }
    }
    
    private final class CallableInterruptibleTask extends CombinedFutureInterruptibleTask<V>
    {
        private final Callable<V> callable;
        
        CallableInterruptibleTask(final Callable<V> callable, final Executor listenerExecutor) {
            super(listenerExecutor);
            this.callable = Preconditions.checkNotNull(callable);
        }
        
        @ParametricNullness
        @Override
        V runInterruptibly() throws Exception {
            return this.callable.call();
        }
        
        @Override
        void setValue(@ParametricNullness final V value) {
            CombinedFuture.this.set(value);
        }
        
        @Override
        String toPendingString() {
            return this.callable.toString();
        }
    }
}
