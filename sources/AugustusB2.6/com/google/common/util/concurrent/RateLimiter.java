// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.base.Stopwatch;
import java.util.Locale;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import java.time.Duration;
import com.google.common.annotations.VisibleForTesting;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public abstract class RateLimiter
{
    private final SleepingStopwatch stopwatch;
    @CheckForNull
    private volatile Object mutexDoNotUseDirectly;
    
    public static RateLimiter create(final double permitsPerSecond) {
        return create(permitsPerSecond, SleepingStopwatch.createFromSystemTimer());
    }
    
    @VisibleForTesting
    static RateLimiter create(final double permitsPerSecond, final SleepingStopwatch stopwatch) {
        final RateLimiter rateLimiter = new SmoothRateLimiter.SmoothBursty(stopwatch, 1.0);
        rateLimiter.setRate(permitsPerSecond);
        return rateLimiter;
    }
    
    public static RateLimiter create(final double permitsPerSecond, final Duration warmupPeriod) {
        return create(permitsPerSecond, Internal.toNanosSaturated(warmupPeriod), TimeUnit.NANOSECONDS);
    }
    
    public static RateLimiter create(final double permitsPerSecond, final long warmupPeriod, final TimeUnit unit) {
        Preconditions.checkArgument(warmupPeriod >= 0L, "warmupPeriod must not be negative: %s", warmupPeriod);
        return create(permitsPerSecond, warmupPeriod, unit, 3.0, SleepingStopwatch.createFromSystemTimer());
    }
    
    @VisibleForTesting
    static RateLimiter create(final double permitsPerSecond, final long warmupPeriod, final TimeUnit unit, final double coldFactor, final SleepingStopwatch stopwatch) {
        final RateLimiter rateLimiter = new SmoothRateLimiter.SmoothWarmingUp(stopwatch, warmupPeriod, unit, coldFactor);
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
    
    @CanIgnoreReturnValue
    public double acquire() {
        return this.acquire(1);
    }
    
    @CanIgnoreReturnValue
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
    
    public boolean tryAcquire(final Duration timeout) {
        return this.tryAcquire(1, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
    
    public boolean tryAcquire(final int permits, final Duration timeout) {
        return this.tryAcquire(permits, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
        return String.format(Locale.ROOT, "RateLimiter[stableRate=%3.1fqps]", this.getRate());
    }
    
    private static void checkPermits(final int permits) {
        Preconditions.checkArgument(permits > 0, "Requested permits (%s) must be positive", permits);
    }
    
    abstract static class SleepingStopwatch
    {
        protected SleepingStopwatch() {
        }
        
        protected abstract long readMicros();
        
        protected abstract void sleepMicrosUninterruptibly(final long p0);
        
        public static SleepingStopwatch createFromSystemTimer() {
            return new SleepingStopwatch() {
                final Stopwatch stopwatch = Stopwatch.createStarted();
                
                @Override
                protected long readMicros() {
                    return this.stopwatch.elapsed(TimeUnit.MICROSECONDS);
                }
                
                @Override
                protected void sleepMicrosUninterruptibly(final long micros) {
                    if (micros > 0L) {
                        Uninterruptibles.sleepUninterruptibly(micros, TimeUnit.MICROSECONDS);
                    }
                }
            };
        }
    }
}
