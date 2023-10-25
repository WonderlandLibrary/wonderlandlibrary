/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.concurrent;

import io.netty.util.concurrent.AbstractFuture;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.concurrent.TimeUnit;

public abstract class CompleteFuture<V>
extends AbstractFuture<V> {
    private final EventExecutor executor;

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public Future<V> awaitUninterruptibly() {
        return this;
    }

    @Override
    public Future<V> await() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return this;
    }

    @Override
    public Future<V> removeListeners(GenericFutureListener<? extends Future<? super V>> ... genericFutureListenerArray) {
        return this;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean cancel(boolean bl) {
        return false;
    }

    @Override
    public Future<V> syncUninterruptibly() {
        return this;
    }

    @Override
    public boolean await(long l) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return true;
    }

    @Override
    public Future<V> removeListener(GenericFutureListener<? extends Future<? super V>> genericFutureListener) {
        return this;
    }

    @Override
    public Future<V> sync() throws InterruptedException {
        return this;
    }

    @Override
    public boolean await(long l, TimeUnit timeUnit) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return true;
    }

    @Override
    public boolean awaitUninterruptibly(long l, TimeUnit timeUnit) {
        return true;
    }

    @Override
    public Future<V> addListeners(GenericFutureListener<? extends Future<? super V>> ... genericFutureListenerArray) {
        if (genericFutureListenerArray == null) {
            throw new NullPointerException("listeners");
        }
        for (GenericFutureListener<? extends Future<? super V>> genericFutureListener : genericFutureListenerArray) {
            if (genericFutureListener == null) break;
            DefaultPromise.notifyListener(this.executor(), this, genericFutureListener);
        }
        return this;
    }

    @Override
    public Future<V> addListener(GenericFutureListener<? extends Future<? super V>> genericFutureListener) {
        if (genericFutureListener == null) {
            throw new NullPointerException("listener");
        }
        DefaultPromise.notifyListener(this.executor(), this, genericFutureListener);
        return this;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }

    protected EventExecutor executor() {
        return this.executor;
    }

    @Override
    public boolean awaitUninterruptibly(long l) {
        return true;
    }

    protected CompleteFuture(EventExecutor eventExecutor) {
        this.executor = eventExecutor;
    }
}

