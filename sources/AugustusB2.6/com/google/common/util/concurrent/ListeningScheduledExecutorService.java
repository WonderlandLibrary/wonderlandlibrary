// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Callable;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.ScheduledExecutorService;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public interface ListeningScheduledExecutorService extends ScheduledExecutorService, ListeningExecutorService
{
    ListenableScheduledFuture<?> schedule(final Runnable p0, final long p1, final TimeUnit p2);
    
    default ListenableScheduledFuture<?> schedule(final Runnable command, final Duration delay) {
        return this.schedule(command, Internal.toNanosSaturated(delay), TimeUnit.NANOSECONDS);
    }
    
     <V> ListenableScheduledFuture<V> schedule(final Callable<V> p0, final long p1, final TimeUnit p2);
    
    default <V> ListenableScheduledFuture<V> schedule(final Callable<V> callable, final Duration delay) {
        return this.schedule(callable, Internal.toNanosSaturated(delay), TimeUnit.NANOSECONDS);
    }
    
    ListenableScheduledFuture<?> scheduleAtFixedRate(final Runnable p0, final long p1, final long p2, final TimeUnit p3);
    
    default ListenableScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final Duration initialDelay, final Duration period) {
        return this.scheduleAtFixedRate(command, Internal.toNanosSaturated(initialDelay), Internal.toNanosSaturated(period), TimeUnit.NANOSECONDS);
    }
    
    ListenableScheduledFuture<?> scheduleWithFixedDelay(final Runnable p0, final long p1, final long p2, final TimeUnit p3);
    
    default ListenableScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final Duration initialDelay, final Duration delay) {
        return this.scheduleWithFixedDelay(command, Internal.toNanosSaturated(initialDelay), Internal.toNanosSaturated(delay), TimeUnit.NANOSECONDS);
    }
}
