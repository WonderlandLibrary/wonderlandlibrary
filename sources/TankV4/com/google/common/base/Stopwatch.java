package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.TimeUnit;

@Beta
@GwtCompatible(
   emulated = true
)
public final class Stopwatch {
   private final Ticker ticker;
   private boolean isRunning;
   private long elapsedNanos;
   private long startTick;

   public static Stopwatch createUnstarted() {
      return new Stopwatch();
   }

   public static Stopwatch createUnstarted(Ticker var0) {
      return new Stopwatch(var0);
   }

   public static Stopwatch createStarted() {
      return (new Stopwatch()).start();
   }

   public static Stopwatch createStarted(Ticker var0) {
      return (new Stopwatch(var0)).start();
   }

   /** @deprecated */
   @Deprecated
   Stopwatch() {
      this(Ticker.systemTicker());
   }

   /** @deprecated */
   @Deprecated
   Stopwatch(Ticker var1) {
      this.ticker = (Ticker)Preconditions.checkNotNull(var1, "ticker");
   }

   public boolean isRunning() {
      return this.isRunning;
   }

   public Stopwatch start() {
      Preconditions.checkState(!this.isRunning, "This stopwatch is already running.");
      this.isRunning = true;
      this.startTick = this.ticker.read();
      return this;
   }

   public Stopwatch stop() {
      long var1 = this.ticker.read();
      Preconditions.checkState(this.isRunning, "This stopwatch is already stopped.");
      this.isRunning = false;
      this.elapsedNanos += var1 - this.startTick;
      return this;
   }

   public Stopwatch reset() {
      this.elapsedNanos = 0L;
      this.isRunning = false;
      return this;
   }

   private long elapsedNanos() {
      return this.isRunning ? this.ticker.read() - this.startTick + this.elapsedNanos : this.elapsedNanos;
   }

   public long elapsed(TimeUnit var1) {
      return var1.convert(this.elapsedNanos(), TimeUnit.NANOSECONDS);
   }

   @GwtIncompatible("String.format()")
   public String toString() {
      long var1 = this.elapsedNanos();
      TimeUnit var3 = chooseUnit(var1);
      double var4 = (double)var1 / (double)TimeUnit.NANOSECONDS.convert(1L, var3);
      return String.format("%.4g %s", var4, abbreviate(var3));
   }

   private static TimeUnit chooseUnit(long var0) {
      if (TimeUnit.DAYS.convert(var0, TimeUnit.NANOSECONDS) > 0L) {
         return TimeUnit.DAYS;
      } else if (TimeUnit.HOURS.convert(var0, TimeUnit.NANOSECONDS) > 0L) {
         return TimeUnit.HOURS;
      } else if (TimeUnit.MINUTES.convert(var0, TimeUnit.NANOSECONDS) > 0L) {
         return TimeUnit.MINUTES;
      } else if (TimeUnit.SECONDS.convert(var0, TimeUnit.NANOSECONDS) > 0L) {
         return TimeUnit.SECONDS;
      } else if (TimeUnit.MILLISECONDS.convert(var0, TimeUnit.NANOSECONDS) > 0L) {
         return TimeUnit.MILLISECONDS;
      } else {
         return TimeUnit.MICROSECONDS.convert(var0, TimeUnit.NANOSECONDS) > 0L ? TimeUnit.MICROSECONDS : TimeUnit.NANOSECONDS;
      }
   }

   private static String abbreviate(TimeUnit var0) {
      switch(var0) {
      case NANOSECONDS:
         return "ns";
      case MICROSECONDS:
         return "μs";
      case MILLISECONDS:
         return "ms";
      case SECONDS:
         return "s";
      case MINUTES:
         return "min";
      case HOURS:
         return "h";
      case DAYS:
         return "d";
      default:
         throw new AssertionError();
      }
   }
}
