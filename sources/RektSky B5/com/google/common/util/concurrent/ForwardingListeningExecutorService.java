/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.util.concurrent;

import com.google.common.util.concurrent.ForwardingExecutorService;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import java.util.concurrent.Callable;

public abstract class ForwardingListeningExecutorService
extends ForwardingExecutorService
implements ListeningExecutorService {
    protected ForwardingListeningExecutorService() {
    }

    @Override
    protected abstract ListeningExecutorService delegate();

    @Override
    public <T> ListenableFuture<T> submit(Callable<T> task) {
        return this.delegate().submit(task);
    }

    @Override
    public ListenableFuture<?> submit(Runnable task) {
        return this.delegate().submit(task);
    }

    @Override
    public <T> ListenableFuture<T> submit(Runnable task, T result) {
        return this.delegate().submit(task, result);
    }
}

