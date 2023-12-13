// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import me.gong.mcleaks.util.google.common.math.LongMath;
import java.util.concurrent.TimeUnit;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;

@GwtIncompatible
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
        this.nextFreeTicketMicros = LongMath.saturatedAdd(this.nextFreeTicketMicros, waitMicros);
        this.storedPermits -= storedPermitsToSpend;
        return returnValue;
    }
    
    abstract long storedPermitsToWaitTime(final double p0, final double p1);
    
    abstract double coolDownIntervalMicros();
    
    void resync(final long nowMicros) {
        if (nowMicros > this.nextFreeTicketMicros) {
            final double newPermits = (nowMicros - this.nextFreeTicketMicros) / this.coolDownIntervalMicros();
            this.storedPermits = Math.min(this.maxPermits, this.storedPermits + newPermits);
            this.nextFreeTicketMicros = nowMicros;
        }
    }
    
    static final class SmoothWarmingUp extends SmoothRateLimiter
    {
        private final long warmupPeriodMicros;
        private double slope;
        private double thresholdPermits;
        private double coldFactor;
        
        SmoothWarmingUp(final SleepingStopwatch stopwatch, final long warmupPeriod, final TimeUnit timeUnit, final double coldFactor) {
            super(stopwatch, null);
            this.warmupPeriodMicros = timeUnit.toMicros(warmupPeriod);
            this.coldFactor = coldFactor;
        }
        
        @Override
        void doSetRate(final double permitsPerSecond, final double stableIntervalMicros) {
            final double oldMaxPermits = this.maxPermits;
            final double coldIntervalMicros = stableIntervalMicros * this.coldFactor;
            this.thresholdPermits = 0.5 * this.warmupPeriodMicros / stableIntervalMicros;
            this.maxPermits = this.thresholdPermits + 2.0 * this.warmupPeriodMicros / (stableIntervalMicros + coldIntervalMicros);
            this.slope = (coldIntervalMicros - stableIntervalMicros) / (this.maxPermits - this.thresholdPermits);
            if (oldMaxPermits == Double.POSITIVE_INFINITY) {
                this.storedPermits = 0.0;
            }
            else {
                this.storedPermits = ((oldMaxPermits == 0.0) ? this.maxPermits : (this.storedPermits * this.maxPermits / oldMaxPermits));
            }
        }
        
        @Override
        long storedPermitsToWaitTime(final double storedPermits, double permitsToTake) {
            final double availablePermitsAboveThreshold = storedPermits - this.thresholdPermits;
            long micros = 0L;
            if (availablePermitsAboveThreshold > 0.0) {
                final double permitsAboveThresholdToTake = Math.min(availablePermitsAboveThreshold, permitsToTake);
                final double length = this.permitsToTime(availablePermitsAboveThreshold) + this.permitsToTime(availablePermitsAboveThreshold - permitsAboveThresholdToTake);
                micros = (long)(permitsAboveThresholdToTake * length / 2.0);
                permitsToTake -= permitsAboveThresholdToTake;
            }
            micros += (long)(this.stableIntervalMicros * permitsToTake);
            return micros;
        }
        
        private double permitsToTime(final double permits) {
            return this.stableIntervalMicros + permits * this.slope;
        }
        
        @Override
        double coolDownIntervalMicros() {
            return this.warmupPeriodMicros / this.maxPermits;
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
        
        @Override
        double coolDownIntervalMicros() {
            return this.stableIntervalMicros;
        }
    }
}
