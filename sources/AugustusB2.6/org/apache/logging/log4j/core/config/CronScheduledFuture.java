// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

public class CronScheduledFuture<V> implements ScheduledFuture<V>
{
    private volatile FutureData futureData;
    
    public CronScheduledFuture(final ScheduledFuture<V> future, final Date runDate) {
        this.futureData = new FutureData(future, runDate);
    }
    
    public Date getFireTime() {
        return this.futureData.runDate;
    }
    
    void reset(final ScheduledFuture<?> future, final Date runDate) {
        this.futureData = new FutureData(future, runDate);
    }
    
    @Override
    public long getDelay(final TimeUnit unit) {
        return this.futureData.scheduledFuture.getDelay(unit);
    }
    
    @Override
    public int compareTo(final Delayed delayed) {
        return this.futureData.scheduledFuture.compareTo(delayed);
    }
    
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
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
        return (V)this.futureData.scheduledFuture.get();
    }
    
    @Override
    public V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return (V)this.futureData.scheduledFuture.get(timeout, unit);
    }
    
    private class FutureData
    {
        private final ScheduledFuture<?> scheduledFuture;
        private final Date runDate;
        
        FutureData(final ScheduledFuture<?> future, final Date runDate) {
            this.scheduledFuture = future;
            this.runDate = runDate;
        }
    }
}
