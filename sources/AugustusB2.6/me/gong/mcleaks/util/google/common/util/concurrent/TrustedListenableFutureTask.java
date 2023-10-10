// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.concurrent.Executors;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import java.util.concurrent.RunnableFuture;

@GwtCompatible
class TrustedListenableFutureTask<V> extends TrustedFuture<V> implements RunnableFuture<V>
{
    private TrustedFutureInterruptibleTask task;
    
    static <V> TrustedListenableFutureTask<V> create(final Callable<V> callable) {
        return new TrustedListenableFutureTask<V>(callable);
    }
    
    static <V> TrustedListenableFutureTask<V> create(final Runnable runnable, @Nullable final V result) {
        return new TrustedListenableFutureTask<V>(Executors.callable(runnable, result));
    }
    
    TrustedListenableFutureTask(final Callable<V> callable) {
        this.task = new TrustedFutureInterruptibleTask(callable);
    }
    
    @Override
    public void run() {
        final TrustedFutureInterruptibleTask localTask = this.task;
        if (localTask != null) {
            localTask.run();
        }
    }
    
    @Override
    protected void afterDone() {
        super.afterDone();
        if (this.wasInterrupted()) {
            final TrustedFutureInterruptibleTask localTask = this.task;
            if (localTask != null) {
                localTask.interruptTask();
            }
        }
        this.task = null;
    }
    
    @Override
    public String toString() {
        return super.toString() + " (delegate = " + this.task + ")";
    }
    
    private final class TrustedFutureInterruptibleTask extends InterruptibleTask
    {
        private final Callable<V> callable;
        
        TrustedFutureInterruptibleTask(final Callable<V> callable) {
            this.callable = Preconditions.checkNotNull(callable);
        }
        
        @Override
        void runInterruptibly() {
            if (!TrustedListenableFutureTask.this.isDone()) {
                try {
                    TrustedListenableFutureTask.this.set(this.callable.call());
                }
                catch (Throwable t) {
                    TrustedListenableFutureTask.this.setException(t);
                }
            }
        }
        
        @Override
        boolean wasInterrupted() {
            return TrustedListenableFutureTask.this.wasInterrupted();
        }
        
        @Override
        public String toString() {
            return this.callable.toString();
        }
    }
}
