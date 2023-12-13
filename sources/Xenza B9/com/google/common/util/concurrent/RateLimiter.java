// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.util.concurrent;

import com.google.common.base.Stopwatch;
import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.Beta;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@Beta
public abstract class RateLimiter
{
    private final SleepingStopwatch stopwatch;
    private volatile Object mutexDoNotUseDirectly;
    
    public static RateLimiter create(final double permitsPerSecond) {
        return create(SleepingStopwatch.createFromSystemTimer(), permitsPerSecond);
    }
    
    @VisibleForTesting
    static RateLimiter create(final SleepingStopwatch stopwatch, final double permitsPerSecond) {
        final RateLimiter rateLimiter = new SmoothRateLimiter.SmoothBursty(stopwatch, 1.0);
        rateLimiter.setRate(permitsPerSecond);
        return rateLimiter;
    }
    
    public static RateLimiter create(final double permitsPerSecond, final long warmupPeriod, final TimeUnit unit) {
        Preconditions.checkArgument(warmupPeriod >= 0L, "warmupPeriod must not be negative: %s", warmupPeriod);
        return create(SleepingStopwatch.createFromSystemTimer(), permitsPerSecond, warmupPeriod, unit);
    }
    
    @VisibleForTesting
    static RateLimiter create(final SleepingStopwatch stopwatch, final double permitsPerSecond, final long warmupPeriod, final TimeUnit unit) {
        final RateLimiter rateLimiter = new SmoothRateLimiter.SmoothWarmingUp(stopwatch, warmupPeriod, unit);
        rateLimiter.setRate(permitsPerSecond);
        return rateLimiter;
    }
    
    private Object mutex() {
        Object mutex = this.mutexDoNotUseDirectly;
        if (mutex == null) {
            synchronized (this) {
                mutex = this.mutexDoNotUseDirectly;
                if (mutex == null) {
                    mutex = (this.mutexDoNotUseDirectly = new Object());
                }
            }
        }
        return mutex;
    }
    
    RateLimiter(final SleepingStopwatch stopwatch) {
        this.stopwatch = Preconditions.checkNotNull(stopwatch);
    }
    
    public final void setRate(final double permitsPerSecond) {
        Preconditions.checkArgument(permitsPerSecond > 0.0 && !Double.isNaN(permitsPerSecond), (Object)"rate must be positive");
        synchronized (this.mutex()) {
            this.doSetRate(permitsPerSecond, this.stopwatch.readMicros());
        }
    }
    
    abstract void doSetRate(final double p0, final long p1);
    
    public final double getRate() {
        synchronized (this.mutex()) {
            return this.doGetRate();
        }
    }
    
    abstract double doGetRate();
    
    public double acquire() {
        return this.acquire(1);
    }
    
    public double acquire(final int permits) {
        final long microsToWait = this.reserve(permits);
        this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
        return 1.0 * microsToWait / TimeUnit.SECONDS.toMicros(1L);
    }
    
    final long reserve(final int permits) {
        checkPermits(permits);
        synchronized (this.mutex()) {
            return this.reserveAndGetWaitLength(permits, this.stopwatch.readMicros());
        }
    }
    
    public boolean tryAcquire(final long timeout, final TimeUnit unit) {
        return this.tryAcquire(1, timeout, unit);
    }
    
    public boolean tryAcquire(final int permits) {
        return this.tryAcquire(permits, 0L, TimeUnit.MICROSECONDS);
    }
    
    public boolean tryAcquire() {
        return this.tryAcquire(1, 0L, TimeUnit.MICROSECONDS);
    }
    
    public boolean tryAcquire(final int permits, final long timeout, final TimeUnit unit) {
        final long timeoutMicros = Math.max(unit.toMicros(timeout), 0L);
        checkPermits(permits);
        final long microsToWait;
        synchronized (this.mutex()) {
            final long nowMicros = this.stopwatch.readMicros();
            if (!this.canAcquire(nowMicros, timeoutMicros)) {
                return false;
            }
            microsToWait = this.reserveAndGetWaitLength(permits, nowMicros);
        }
        this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
        return true;
    }
    
    private boolean canAcquire(final long nowMicros, final long timeoutMicros) {
        return this.queryEarliestAvailable(nowMicros) - timeoutMicros <= nowMicros;
    }
    
    final long reserveAndGetWaitLength(final int permits, final long nowMicros) {
        final long momentAvailable = this.reserveEarliestAvailable(permits, nowMicros);
        return Math.max(momentAvailable - nowMicros, 0L);
    }
    
    abstract long queryEarliestAvailable(final long p0);
    
    abstract long reserveEarliestAvailable(final int p0, final long p1);
    
    @Override
    public String toString() {
        return String.format("RateLimiter[stableRate=%3.1fqps]", this.getRate());
    }
    
    private static int checkPermits(final int permits) {
        Preconditions.checkArgument(permits > 0, "Requested permits (%s) must be positive", permits);
        return permits;
    }
    
    @VisibleForTesting
    abstract static class SleepingStopwatch
    {
        abstract long readMicros();
        
        abstract void sleepMicrosUninterruptibly(final long p0);
        
        static final SleepingStopwatch createFromSystemTimer() {
            return new SleepingStopwatch() {
                final Stopwatch stopwatch = Stopwatch.createStarted();
                
                @Override
                long readMicros() {
                    return this.stopwatch.elapsed(TimeUnit.MICROSECONDS);
                }
                
                @Override
                void sleepMicrosUninterruptibly(final long micros) {
                    if (micros > 0L) {
                        Uninterruptibles.sleepUninterruptibly(micros, TimeUnit.MICROSECONDS);
                    }
                }
            };
        }
    }
}
