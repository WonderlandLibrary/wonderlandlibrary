/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.DisabledFuture;
import com.github.benmanes.caffeine.cache.Scheduler;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;

final class GuardedScheduler
implements Scheduler,
Serializable {
    static final Logger logger = Logger.getLogger(GuardedScheduler.class.getName());
    static final long serialVersionUID = 1L;
    final Scheduler delegate;

    GuardedScheduler(Scheduler delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public @NonNull Future<?> schedule(@NonNull Executor executor, @NonNull Runnable command, long delay, @NonNull TimeUnit unit) {
        try {
            Future<?> future = this.delegate.schedule(executor, command, delay, unit);
            return future == null ? DisabledFuture.INSTANCE : future;
        }
        catch (Throwable t) {
            logger.log(Level.WARNING, "Exception thrown by scheduler; discarded task", t);
            return DisabledFuture.INSTANCE;
        }
    }
}

