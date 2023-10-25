/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.InterruptibleTask;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;
import javax.annotation.Nullable;

@GwtCompatible
class TrustedListenableFutureTask<V>
extends AbstractFuture.TrustedFuture<V>
implements RunnableFuture<V> {
    private TrustedFutureInterruptibleTask task;

    static <V> TrustedListenableFutureTask<V> create(Callable<V> callable) {
        return new TrustedListenableFutureTask<V>(callable);
    }

    static <V> TrustedListenableFutureTask<V> create(Runnable runnable, @Nullable V result) {
        return new TrustedListenableFutureTask<V>(Executors.callable(runnable, result));
    }

    TrustedListenableFutureTask(Callable<V> callable) {
        this.task = new TrustedFutureInterruptibleTask(callable);
    }

    @Override
    public void run() {
        TrustedFutureInterruptibleTask localTask = this.task;
        if (localTask != null) {
            localTask.run();
        }
    }

    @Override
    protected void afterDone() {
        TrustedFutureInterruptibleTask localTask;
        super.afterDone();
        if (this.wasInterrupted() && (localTask = this.task) != null) {
            localTask.interruptTask();
        }
        this.task = null;
    }

    public String toString() {
        return super.toString() + " (delegate = " + this.task + ")";
    }

    private final class TrustedFutureInterruptibleTask
    extends InterruptibleTask {
        private final Callable<V> callable;

        TrustedFutureInterruptibleTask(Callable<V> callable) {
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

        public String toString() {
            return this.callable.toString();
        }
    }
}

