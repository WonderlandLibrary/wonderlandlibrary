/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.concurrent;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ProgressivePromise;

public class DefaultProgressivePromise<V>
extends DefaultPromise<V>
implements ProgressivePromise<V> {
    @Override
    public ProgressivePromise<V> setSuccess(V v) {
        super.setSuccess(v);
        return this;
    }

    @Override
    public ProgressivePromise<V> awaitUninterruptibly() {
        super.awaitUninterruptibly();
        return this;
    }

    @Override
    public ProgressivePromise<V> removeListener(GenericFutureListener<? extends Future<? super V>> genericFutureListener) {
        super.removeListener((GenericFutureListener)genericFutureListener);
        return this;
    }

    @Override
    public ProgressivePromise<V> removeListeners(GenericFutureListener<? extends Future<? super V>> ... genericFutureListenerArray) {
        super.removeListeners((GenericFutureListener[])genericFutureListenerArray);
        return this;
    }

    @Override
    public ProgressivePromise<V> addListener(GenericFutureListener<? extends Future<? super V>> genericFutureListener) {
        super.addListener((GenericFutureListener)genericFutureListener);
        return this;
    }

    @Override
    public ProgressivePromise<V> syncUninterruptibly() {
        super.syncUninterruptibly();
        return this;
    }

    @Override
    public ProgressivePromise<V> setFailure(Throwable throwable) {
        super.setFailure(throwable);
        return this;
    }

    @Override
    public boolean tryProgress(long l, long l2) {
        if (l2 < 0L) {
            l2 = -1L;
            if (l < 0L || this.isDone()) {
                return false;
            }
        } else if (l < 0L || l > l2 || this.isDone()) {
            return false;
        }
        this.notifyProgressiveListeners(l, l2);
        return true;
    }

    public DefaultProgressivePromise(EventExecutor eventExecutor) {
        super(eventExecutor);
    }

    @Override
    public ProgressivePromise<V> setProgress(long l, long l2) {
        if (l2 < 0L) {
            l2 = -1L;
            if (l < 0L) {
                throw new IllegalArgumentException("progress: " + l + " (expected: >= 0)");
            }
        } else if (l < 0L || l > l2) {
            throw new IllegalArgumentException("progress: " + l + " (expected: 0 <= progress <= total (" + l2 + "))");
        }
        if (this.isDone()) {
            throw new IllegalStateException("complete already");
        }
        this.notifyProgressiveListeners(l, l2);
        return this;
    }

    protected DefaultProgressivePromise() {
    }

    @Override
    public ProgressivePromise<V> sync() throws InterruptedException {
        super.sync();
        return this;
    }

    @Override
    public ProgressivePromise<V> await() throws InterruptedException {
        super.await();
        return this;
    }

    @Override
    public ProgressivePromise<V> addListeners(GenericFutureListener<? extends Future<? super V>> ... genericFutureListenerArray) {
        super.addListeners((GenericFutureListener[])genericFutureListenerArray);
        return this;
    }
}

