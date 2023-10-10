/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ExecutionList;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import javax.annotation.Nullable;

public abstract class AbstractFuture<V>
implements ListenableFuture<V> {
    private final Sync<V> sync = new Sync();
    private final ExecutionList executionList = new ExecutionList();

    protected AbstractFuture() {
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
        return this.sync.get(unit.toNanos(timeout));
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return this.sync.get();
    }

    @Override
    public boolean isDone() {
        return this.sync.isDone();
    }

    @Override
    public boolean isCancelled() {
        return this.sync.isCancelled();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (!this.sync.cancel(mayInterruptIfRunning)) {
            return false;
        }
        this.executionList.execute();
        if (mayInterruptIfRunning) {
            this.interruptTask();
        }
        return true;
    }

    protected void interruptTask() {
    }

    protected final boolean wasInterrupted() {
        return this.sync.wasInterrupted();
    }

    @Override
    public void addListener(Runnable listener, Executor exec) {
        this.executionList.add(listener, exec);
    }

    protected boolean set(@Nullable V value) {
        boolean result = this.sync.set(value);
        if (result) {
            this.executionList.execute();
        }
        return result;
    }

    protected boolean setException(Throwable throwable) {
        boolean result = this.sync.setException(Preconditions.checkNotNull(throwable));
        if (result) {
            this.executionList.execute();
        }
        return result;
    }

    static final CancellationException cancellationExceptionWithCause(@Nullable String message, @Nullable Throwable cause) {
        CancellationException exception = new CancellationException(message);
        exception.initCause(cause);
        return exception;
    }

    static final class Sync<V>
    extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 0L;
        static final int RUNNING = 0;
        static final int COMPLETING = 1;
        static final int COMPLETED = 2;
        static final int CANCELLED = 4;
        static final int INTERRUPTED = 8;
        private V value;
        private Throwable exception;

        Sync() {
        }

        @Override
        protected int tryAcquireShared(int ignored) {
            if (this.isDone()) {
                return 1;
            }
            return -1;
        }

        @Override
        protected boolean tryReleaseShared(int finalState) {
            this.setState(finalState);
            return true;
        }

        V get(long nanos) throws TimeoutException, CancellationException, ExecutionException, InterruptedException {
            if (!this.tryAcquireSharedNanos(-1, nanos)) {
                throw new TimeoutException("Timeout waiting for task.");
            }
            return this.getValue();
        }

        V get() throws CancellationException, ExecutionException, InterruptedException {
            this.acquireSharedInterruptibly(-1);
            return this.getValue();
        }

        private V getValue() throws CancellationException, ExecutionException {
            int state = this.getState();
            switch (state) {
                case 2: {
                    if (this.exception != null) {
                        throw new ExecutionException(this.exception);
                    }
                    return this.value;
                }
                case 4: 
                case 8: {
                    throw AbstractFuture.cancellationExceptionWithCause("Task was cancelled.", this.exception);
                }
            }
            throw new IllegalStateException("Error, synchronizer in invalid state: " + state);
        }

        boolean isDone() {
            return (this.getState() & 0xE) != 0;
        }

        boolean isCancelled() {
            return (this.getState() & 0xC) != 0;
        }

        boolean wasInterrupted() {
            return this.getState() == 8;
        }

        boolean set(@Nullable V v2) {
            return this.complete(v2, null, 2);
        }

        boolean setException(Throwable t2) {
            return this.complete(null, t2, 2);
        }

        boolean cancel(boolean interrupt) {
            return this.complete(null, null, interrupt ? 8 : 4);
        }

        private boolean complete(@Nullable V v2, @Nullable Throwable t2, int finalState) {
            boolean doCompletion = this.compareAndSetState(0, 1);
            if (doCompletion) {
                this.value = v2;
                this.exception = (finalState & 0xC) != 0 ? new CancellationException("Future.cancel() was called.") : t2;
                this.releaseShared(finalState);
            } else if (this.getState() == 1) {
                this.acquireShared(-1);
            }
            return doCompletion;
        }
    }
}

