/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.concurrent;

import io.netty.util.concurrent.GenericFutureListener;
import java.util.concurrent.TimeUnit;

public interface Future<V>
extends java.util.concurrent.Future<V> {
    public boolean await(long var1, TimeUnit var3) throws InterruptedException;

    public Future<V> await() throws InterruptedException;

    public Future<V> addListener(GenericFutureListener<? extends Future<? super V>> var1);

    public boolean awaitUninterruptibly(long var1, TimeUnit var3);

    public V getNow();

    public Future<V> removeListener(GenericFutureListener<? extends Future<? super V>> var1);

    public boolean isSuccess();

    public Throwable cause();

    public Future<V> syncUninterruptibly();

    public Future<V> sync() throws InterruptedException;

    public Future<V> removeListeners(GenericFutureListener<? extends Future<? super V>> ... var1);

    public Future<V> awaitUninterruptibly();

    public Future<V> addListeners(GenericFutureListener<? extends Future<? super V>> ... var1);

    public boolean await(long var1) throws InterruptedException;

    public boolean isCancellable();

    @Override
    public boolean cancel(boolean var1);

    public boolean awaitUninterruptibly(long var1);
}

