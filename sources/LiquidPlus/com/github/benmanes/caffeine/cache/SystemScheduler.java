/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Scheduler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.nullness.qual.Nullable;

enum SystemScheduler implements Scheduler
{
    INSTANCE;

    static final @Nullable Method delayedExecutor;

    @Override
    public Future<?> schedule(Executor executor, Runnable command, long delay, TimeUnit unit) {
        Objects.requireNonNull(executor);
        Objects.requireNonNull(command);
        Objects.requireNonNull(unit);
        try {
            Executor scheduler = (Executor)delayedExecutor.invoke(CompletableFuture.class, new Object[]{delay, unit, executor});
            return CompletableFuture.runAsync(command, scheduler);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    static @Nullable Method getDelayedExecutorMethod() {
        try {
            return CompletableFuture.class.getMethod("delayedExecutor", Long.TYPE, TimeUnit.class, Executor.class);
        }
        catch (NoSuchMethodException | SecurityException e) {
            return null;
        }
    }

    static boolean isPresent() {
        return delayedExecutor != null;
    }

    static {
        delayedExecutor = SystemScheduler.getDelayedExecutorMethod();
    }
}

