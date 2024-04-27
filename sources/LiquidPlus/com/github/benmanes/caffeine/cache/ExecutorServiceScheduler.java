/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.DisabledFuture;
import com.github.benmanes.caffeine.cache.Scheduler;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

final class ExecutorServiceScheduler
implements Scheduler,
Serializable {
    static final Logger logger = Logger.getLogger(ExecutorServiceScheduler.class.getName());
    static final long serialVersionUID = 1L;
    final ScheduledExecutorService scheduledExecutorService;

    ExecutorServiceScheduler(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = Objects.requireNonNull(scheduledExecutorService);
    }

    @Override
    public Future<?> schedule(Executor executor, Runnable command, long delay, TimeUnit unit) {
        Objects.requireNonNull(executor);
        Objects.requireNonNull(command);
        Objects.requireNonNull(unit);
        if (this.scheduledExecutorService.isShutdown()) {
            return DisabledFuture.INSTANCE;
        }
        return this.scheduledExecutorService.schedule(() -> {
            try {
                executor.execute(command);
            }
            catch (Throwable t) {
                logger.log(Level.WARNING, "Exception thrown when submitting scheduled task", t);
                throw t;
            }
        }, delay, unit);
    }
}

