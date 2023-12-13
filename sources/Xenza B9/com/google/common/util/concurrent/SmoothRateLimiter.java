// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.util.concurrent;

import java.util.concurrent.TimeUnit;

abstract class SmoothRateLimiter extends RateLimiter
{
    double storedPermits;
    double maxPermits;
    double stableIntervalMicros;
    private long nextFreeTicketMicros;
    
    private SmoothRateLimiter(final SleepingStopwatch stopwatch) {
        super(stopwatch);
        this.nextFreeTicketMicros = 0L;
    }
    
    @Override
    final void doSetRate(final double permitsPerSecond, final long nowMicros) {
        this.resync(nowMicros);
        final double stableIntervalMicros = TimeUnit.SECONDS.toMicros(1L) / permitsPerSecond;
        this.doSetRate(permitsPerSecond, this.stableIntervalMicros = stableIntervalMicros);
    }
    
    abstract void doSetRate(final double p0, final double p1);
    
    @Override
    final double doGetRate() {
        return TimeUnit.SECONDS.toMicros(1L) / this.stableIntervalMicros;
    }
    
    @Override
    final long queryEarliestAvailable(final long nowMicros) {
        return this.nextFreeTicketMicros;
    }
    
    @Override
    final long reserveEarliestAvailable(final int requiredPermits, final long nowMicros) {
        this.resync(nowMicros);
        final long returnValue = this.nextFreeTicketMicros;
        final double storedPermitsToSpend = Math.min(requiredPermits, this.storedPermits);
        final double freshPermits = requiredPermits - storedPermitsToSpend;
        final long waitMicros = this.storedPermitsToWaitTime(this.storedPermits, storedPermitsToSpend) + (long)(freshPermits * this.stableIntervalMicros);
        this.nextFreeTicketMicros += waitMicros;
        this.storedPermits -= storedPermitsToSpend;
        return returnValue;
    }
    
    abstract long storedPermitsToWaitTime(final double p0, final double p1);
    
    private void resync(final long nowMicros) {
        if (nowMicros > this.nextFreeTicketMicros) {
            this.storedPermits = Math.min(this.maxPermits, this.storedPermits + (nowMicros - this.nextFreeTicketMicros) / this.stableIntervalMicros);
            this.nextFreeTicketMicros = nowMicros;
        }
    }
    
    static final class SmoothWarmingUp extends SmoothRateLimiter
    {
        private final long warmupPeriodMicros;
        private double slope;
        private double halfPermits;
        
        SmoothWarmingUp(final SleepingStopwatch stopwatch, final long warmupPeriod, final TimeUnit timeUnit) {
            super(stopwatch, null);
            this.warmupPeriodMicros = timeUnit.toMicros(warmupPeriod);
        }
        
        @Override
        void doSetRate(final double permitsPerSecond, final double stableIntervalMicros) {
            final double oldMaxPermits = this.maxPermits;
            this.maxPermits = this.warmupPeriodMicros / stableIntervalMicros;
            this.halfPermits = this.maxPermits / 2.0;
            final double coldIntervalMicros = stableIntervalMicros * 3.0;
            this.slope = (coldIntervalMicros - stableIntervalMicros) / this.halfPermits;
            if (oldMaxPermits == Double.POSITIVE_INFINITY) {
                this.storedPermits = 0.0;
            }
            else {
                this.storedPermits = ((oldMaxPermits == 0.0) ? this.maxPermits : (this.storedPermits * this.maxPermits / oldMaxPermits));
            }
        }
        
        @Override
        long storedPermitsToWaitTime(final double storedPermits, double permitsToTake) {
            final double availablePermitsAboveHalf = storedPermits - this.halfPermits;
            long micros = 0L;
            if (availablePermitsAboveHalf > 0.0) {
                final double permitsAboveHalfToTake = Math.min(availablePermitsAboveHalf, permitsToTake);
                micros = (long)(permitsAboveHalfToTake * (this.permitsToTime(availablePermitsAboveHalf) + this.permitsToTime(availablePermitsAboveHalf - permitsAboveHalfToTake)) / 2.0);
                permitsToTake -= permitsAboveHalfToTake;
            }
            micros += (long)(this.stableIntervalMicros * permitsToTake);
            return micros;
        }
        
        private double permitsToTime(final double permits) {
            return this.stableIntervalMicros + permits * this.slope;
        }
    }
    
    static final class SmoothBursty extends SmoothRateLimiter
    {
        final double maxBurstSeconds;
        
        SmoothBursty(final SleepingStopwatch stopwatch, final double maxBurstSeconds) {
            super(stopwatch, null);
            this.maxBurstSeconds = maxBurstSeconds;
        }
        
        @Override
        void doSetRate(final double permitsPerSecond, final double stableIntervalMicros) {
            final double oldMaxPermits = this.maxPermits;
            this.maxPermits = this.maxBurstSeconds * permitsPerSecond;
            if (oldMaxPermits == Double.POSITIVE_INFINITY) {
                this.storedPermits = this.maxPermits;
            }
            else {
                this.storedPermits = ((oldMaxPermits == 0.0) ? 0.0 : (this.storedPermits * this.maxPermits / oldMaxPermits));
            }
        }
        
        @Override
        long storedPermitsToWaitTime(final double storedPermits, final double permitsToTake) {
            return 0L;
        }
    }
}
