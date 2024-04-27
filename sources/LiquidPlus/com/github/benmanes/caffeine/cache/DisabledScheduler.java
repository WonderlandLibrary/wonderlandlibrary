/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.DisabledFuture;
import com.github.benmanes.caffeine.cache.Scheduler;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

enum DisabledScheduler implements Scheduler
{
    INSTANCE;


    public Future<Void> schedule(Executor executor, Runnable command, long delay, TimeUnit unit) {
        Objects.requireNonNull(executor);
        Objects.requireNonNull(command);
        Objects.requireNonNull(unit);
        return DisabledFuture.INSTANCE;
    }
}

