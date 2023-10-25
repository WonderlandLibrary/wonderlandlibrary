/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.concurrent;

import io.netty.util.concurrent.CompleteFuture;
import io.netty.util.concurrent.EventExecutor;

public final class SucceededFuture<V>
extends CompleteFuture<V> {
    private final V result;

    @Override
    public boolean isSuccess() {
        return true;
    }

    public SucceededFuture(EventExecutor eventExecutor, V v) {
        super(eventExecutor);
        this.result = v;
    }

    @Override
    public V getNow() {
        return this.result;
    }

    @Override
    public Throwable cause() {
        return null;
    }
}

