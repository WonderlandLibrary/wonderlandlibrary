/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.nullness.qual.Nullable;

final class Pacer {
    static final long TOLERANCE = Caffeine.ceilingPowerOfTwo(TimeUnit.SECONDS.toNanos(1L));
    final Scheduler scheduler;
    long nextFireTime;
    @Nullable Future<?> future;

    Pacer(Scheduler scheduler) {
        this.scheduler = Objects.requireNonNull(scheduler);
    }

    public void schedule(Executor executor, Runnable command, long now, long delay) {
        long scheduleAt = now + delay;
        if (this.future == null) {
            if (this.nextFireTime != 0L) {
                return;
            }
        } else if (this.nextFireTime - now > 0L) {
            if (this.maySkip(scheduleAt)) {
                return;
            }
            this.future.cancel(false);
        }
        long actualDelay = this.calculateSchedule(now, delay, scheduleAt);
        this.future = this.scheduler.schedule(executor, command, actualDelay, TimeUnit.NANOSECONDS);
    }

    boolean maySkip(long scheduleAt) {
        long delta = scheduleAt - this.nextFireTime;
        return delta >= 0L || -delta <= TOLERANCE;
    }

    long calculateSchedule(long now, long delay, long scheduleAt) {
        if (delay <= TOLERANCE) {
            this.nextFireTime = now + TOLERANCE;
            return TOLERANCE;
        }
        this.nextFireTime = scheduleAt;
        return delay;
    }
}

