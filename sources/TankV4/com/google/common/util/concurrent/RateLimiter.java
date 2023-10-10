package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@Beta
public abstract class RateLimiter {
   private final RateLimiter.SleepingTicker ticker;
   private final long offsetNanos;
   double storedPermits;
   double maxPermits;
   volatile double stableIntervalMicros;
   private final Object mutex;
   private long nextFreeTicketMicros;

   public static RateLimiter create(double var0) {
      return create(RateLimiter.SleepingTicker.SYSTEM_TICKER, var0);
   }

   @VisibleForTesting
   static RateLimiter create(RateLimiter.SleepingTicker var0, double var1) {
      RateLimiter.Bursty var3 = new RateLimiter.Bursty(var0, 1.0D);
      var3.setRate(var1);
      return var3;
   }

   public static RateLimiter create(double var0, long var2, TimeUnit var4) {
      return create(RateLimiter.SleepingTicker.SYSTEM_TICKER, var0, var2, var4);
   }

   @VisibleForTesting
   static RateLimiter create(RateLimiter.SleepingTicker var0, double var1, long var3, TimeUnit var5) {
      RateLimiter.WarmingUp var6 = new RateLimiter.WarmingUp(var0, var3, var5);
      var6.setRate(var1);
      return var6;
   }

   @VisibleForTesting
   static RateLimiter createWithCapacity(RateLimiter.SleepingTicker var0, double var1, long var3, TimeUnit var5) {
      double var6 = (double)var5.toNanos(var3) / 1.0E9D;
      RateLimiter.Bursty var8 = new RateLimiter.Bursty(var0, var6);
      var8.setRate(var1);
      return var8;
   }

   private RateLimiter(RateLimiter.SleepingTicker var1) {
      this.mutex = new Object();
      this.nextFreeTicketMicros = 0L;
      this.ticker = var1;
      this.offsetNanos = var1.read();
   }

   public final void setRate(double var1) {
      Preconditions.checkArgument(var1 > 0.0D && !Double.isNaN(var1), "rate must be positive");
      Object var3;
      synchronized(var3 = this.mutex){}
      this.resync(this.readSafeMicros());
      double var4 = (double)TimeUnit.SECONDS.toMicros(1L) / var1;
      this.stableIntervalMicros = var4;
      this.doSetRate(var1, var4);
   }

   abstract void doSetRate(double var1, double var3);

   public final double getRate() {
      return (double)TimeUnit.SECONDS.toMicros(1L) / this.stableIntervalMicros;
   }

   public double acquire() {
      return this.acquire(1);
   }

   public double acquire(int var1) {
      long var2 = this.reserve(var1);
      this.ticker.sleepMicrosUninterruptibly(var2);
      return 1.0D * (double)var2 / (double)TimeUnit.SECONDS.toMicros(1L);
   }

   long reserve() {
      return this.reserve(1);
   }

   long reserve(int var1) {
      checkPermits(var1);
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.reserveNextTicket((double)var1, this.readSafeMicros());
   }

   public boolean tryAcquire(long var1, TimeUnit var3) {
      return this.tryAcquire(1, var1, var3);
   }

   public boolean tryAcquire(int var1) {
      return this.tryAcquire(var1, 0L, TimeUnit.MICROSECONDS);
   }

   public boolean tryAcquire() {
      return this.tryAcquire(1, 0L, TimeUnit.MICROSECONDS);
   }

   public boolean tryAcquire(int var1, long var2, TimeUnit var4) {
      long var5 = var4.toMicros(var2);
      checkPermits(var1);
      Object var9;
      synchronized(var9 = this.mutex){}
      long var10 = this.readSafeMicros();
      if (this.nextFreeTicketMicros > var10 + var5) {
         return false;
      } else {
         long var7 = this.reserveNextTicket((double)var1, var10);
         this.ticker.sleepMicrosUninterruptibly(var7);
         return true;
      }
   }

   private static void checkPermits(int var0) {
      Preconditions.checkArgument(var0 > 0, "Requested permits must be positive");
   }

   private long reserveNextTicket(double var1, long var3) {
      this.resync(var3);
      long var5 = Math.max(0L, this.nextFreeTicketMicros - var3);
      double var7 = Math.min(var1, this.storedPermits);
      double var9 = var1 - var7;
      long var11 = this.storedPermitsToWaitTime(this.storedPermits, var7) + (long)(var9 * this.stableIntervalMicros);
      this.nextFreeTicketMicros += var11;
      this.storedPermits -= var7;
      return var5;
   }

   abstract long storedPermitsToWaitTime(double var1, double var3);

   private void resync(long var1) {
      if (var1 > this.nextFreeTicketMicros) {
         this.storedPermits = Math.min(this.maxPermits, this.storedPermits + (double)(var1 - this.nextFreeTicketMicros) / this.stableIntervalMicros);
         this.nextFreeTicketMicros = var1;
      }

   }

   private long readSafeMicros() {
      return TimeUnit.NANOSECONDS.toMicros(this.ticker.read() - this.offsetNanos);
   }

   public String toString() {
      return String.format("RateLimiter[stableRate=%3.1fqps]", 1000000.0D / this.stableIntervalMicros);
   }

   RateLimiter(RateLimiter.SleepingTicker var1, Object var2) {
      this(var1);
   }

   @VisibleForTesting
   abstract static class SleepingTicker extends Ticker {
      static final RateLimiter.SleepingTicker SYSTEM_TICKER = new RateLimiter.SleepingTicker() {
         public long read() {
            return systemTicker().read();
         }

         public void sleepMicrosUninterruptibly(long var1) {
            if (var1 > 0L) {
               Uninterruptibles.sleepUninterruptibly(var1, TimeUnit.MICROSECONDS);
            }

         }
      };

      abstract void sleepMicrosUninterruptibly(long var1);
   }

   private static class Bursty extends RateLimiter {
      final double maxBurstSeconds;

      Bursty(RateLimiter.SleepingTicker var1, double var2) {
         super(var1, null);
         this.maxBurstSeconds = var2;
      }

      void doSetRate(double var1, double var3) {
         double var5 = this.maxPermits;
         this.maxPermits = this.maxBurstSeconds * var1;
         this.storedPermits = var5 == 0.0D ? 0.0D : this.storedPermits * this.maxPermits / var5;
      }

      long storedPermitsToWaitTime(double var1, double var3) {
         return 0L;
      }
   }

   private static class WarmingUp extends RateLimiter {
      final long warmupPeriodMicros;
      private double slope;
      private double halfPermits;

      WarmingUp(RateLimiter.SleepingTicker var1, long var2, TimeUnit var4) {
         super(var1, null);
         this.warmupPeriodMicros = var4.toMicros(var2);
      }

      void doSetRate(double var1, double var3) {
         double var5 = this.maxPermits;
         this.maxPermits = (double)this.warmupPeriodMicros / var3;
         this.halfPermits = this.maxPermits / 2.0D;
         double var7 = var3 * 3.0D;
         this.slope = (var7 - var3) / this.halfPermits;
         if (var5 == Double.POSITIVE_INFINITY) {
            this.storedPermits = 0.0D;
         } else {
            this.storedPermits = var5 == 0.0D ? this.maxPermits : this.storedPermits * this.maxPermits / var5;
         }

      }

      long storedPermitsToWaitTime(double var1, double var3) {
         double var5 = var1 - this.halfPermits;
         long var7 = 0L;
         if (var5 > 0.0D) {
            double var9 = Math.min(var5, var3);
            var7 = (long)(var9 * (this.permitsToTime(var5) + this.permitsToTime(var5 - var9)) / 2.0D);
            var3 -= var9;
         }

         var7 = (long)((double)var7 + this.stableIntervalMicros * var3);
         return var7;
      }

      private double permitsToTime(double var1) {
         return this.stableIntervalMicros + var1 * this.slope;
      }
   }
}
