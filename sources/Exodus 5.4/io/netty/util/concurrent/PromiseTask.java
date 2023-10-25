/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.concurrent;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Promise;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

class PromiseTask<V>
extends DefaultPromise<V>
implements RunnableFuture<V> {
    protected final Callable<V> task;

    @Override
    public final boolean tryFailure(Throwable throwable) {
        return false;
    }

    @Override
    public final boolean trySuccess(V v) {
        return false;
    }

    protected final Promise<V> setFailureInternal(Throwable throwable) {
        super.setFailure(throwable);
        return this;
    }

    protected final Promise<V> setSuccessInternal(V v) {
        super.setSuccess(v);
        return this;
    }

    protected final boolean tryFailureInternal(Throwable throwable) {
        return super.tryFailure(throwable);
    }

    PromiseTask(EventExecutor eventExecutor, Callable<V> callable) {
        super(eventExecutor);
        this.task = callable;
    }

    static <T> Callable<T> toCallable(Runnable runnable, T t) {
        return new RunnableAdapter<T>(runnable, t);
    }

    public final int hashCode() {
        return System.identityHashCode(this);
    }

    protected final boolean setUncancellableInternal() {
        return super.setUncancellable();
    }

    @Override
    public final Promise<V> setSuccess(V v) {
        throw new IllegalStateException();
    }

    @Override
    protected StringBuilder toStringBuilder() {
        StringBuilder stringBuilder = super.toStringBuilder();
        stringBuilder.setCharAt(stringBuilder.length() - 1, ',');
        stringBuilder.append(" task: ");
        stringBuilder.append(this.task);
        stringBuilder.append(')');
        return stringBuilder;
    }

    public final boolean equals(Object object) {
        return this == object;
    }

    protected final boolean trySuccessInternal(V v) {
        return super.trySuccess(v);
    }

    PromiseTask(EventExecutor eventExecutor, Runnable runnable, V v) {
        this(eventExecutor, PromiseTask.toCallable(runnable, v));
    }

    @Override
    public final boolean setUncancellable() {
        throw new IllegalStateException();
    }

    @Override
    public final Promise<V> setFailure(Throwable throwable) {
        throw new IllegalStateException();
    }

    @Override
    public void run() {
        try {
            if (this.setUncancellableInternal()) {
                V v = this.task.call();
                this.setSuccessInternal(v);
            }
        }
        catch (Throwable throwable) {
            this.setFailureInternal(throwable);
        }
    }

    private static final class RunnableAdapter<T>
    implements Callable<T> {
        final Runnable task;
        final T result;

        public String toString() {
            return "Callable(task: " + this.task + ", result: " + this.result + ')';
        }

        @Override
        public T call() {
            this.task.run();
            return this.result;
        }

        RunnableAdapter(Runnable runnable, T t) {
            this.task = runnable;
            this.result = t;
        }
    }
}

