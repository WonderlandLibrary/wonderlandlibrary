/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CronScheduledFuture<V>
implements ScheduledFuture<V> {
    private volatile FutureData futureData;

    public CronScheduledFuture(ScheduledFuture<V> future, Date runDate) {
        this.futureData = new FutureData(future, runDate);
    }

    public Date getFireTime() {
        return this.futureData.runDate;
    }

    void reset(ScheduledFuture<?> future, Date runDate) {
        this.futureData = new FutureData(future, runDate);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return this.futureData.scheduledFuture.getDelay(unit);
    }

    @Override
    public int compareTo(Delayed delayed) {
        return this.futureData.scheduledFuture.compareTo(delayed);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return this.futureData.scheduledFuture.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return this.futureData.scheduledFuture.isCancelled();
    }

    @Override
    public boolean isDone() {
        return this.futureData.scheduledFuture.isDone();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return this.futureData.scheduledFuture.get();
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.futureData.scheduledFuture.get(timeout, unit);
    }

    private class FutureData {
        private final ScheduledFuture<?> scheduledFuture;
        private final Date runDate;

        FutureData(ScheduledFuture<?> future, Date runDate) {
            this.scheduledFuture = future;
            this.runDate = runDate;
        }
    }
}

