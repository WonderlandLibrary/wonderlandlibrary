/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.concurrent;

import io.netty.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractFuture<V>
implements Future<V> {
    @Override
    public V get(long l, TimeUnit timeUnit) throws TimeoutException, InterruptedException, ExecutionException {
        if (this.await(l, timeUnit)) {
            Throwable throwable = this.cause();
            if (throwable == null) {
                return this.getNow();
            }
            throw new ExecutionException(throwable);
        }
        throw new TimeoutException();
    }

    @Override
    public V get() throws ExecutionException, InterruptedException {
        this.await();
        Throwable throwable = this.cause();
        if (throwable == null) {
            return this.getNow();
        }
        throw new ExecutionException(throwable);
    }
}

