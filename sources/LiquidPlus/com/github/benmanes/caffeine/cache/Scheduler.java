/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.index.qual.Positive
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.DisabledScheduler;
import com.github.benmanes.caffeine.cache.ExecutorServiceScheduler;
import com.github.benmanes.caffeine.cache.GuardedScheduler;
import com.github.benmanes.caffeine.cache.SystemScheduler;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface Scheduler {
    public @NonNull Future<?> schedule(@NonNull Executor var1, @NonNull Runnable var2, @Positive long var3, @NonNull TimeUnit var5);

    public static @NonNull Scheduler disabledScheduler() {
        return DisabledScheduler.INSTANCE;
    }

    public static @NonNull Scheduler systemScheduler() {
        return SystemScheduler.isPresent() ? SystemScheduler.INSTANCE : Scheduler.disabledScheduler();
    }

    public static @NonNull Scheduler forScheduledExecutorService(@NonNull ScheduledExecutorService scheduledExecutorService) {
        return new ExecutorServiceScheduler(scheduledExecutorService);
    }

    public static @NonNull Scheduler guardedScheduler(@NonNull Scheduler scheduler) {
        return scheduler instanceof GuardedScheduler ? scheduler : new GuardedScheduler(scheduler);
    }
}

