package com.sun.javafx.animation;

import javafx.util.Duration;

public class TickCalculation {
   public static final int TICKS_PER_SECOND = 6000;
   private static final double TICKS_PER_MILI = 6.0;
   private static final double TICKS_PER_NANO = 6.0E-6;

   private TickCalculation() {
   }

   public static long add(long var0, long var2) {
      assert var0 >= 0L;

      if (var0 != Long.MAX_VALUE && var2 != Long.MAX_VALUE) {
         if (var2 == Long.MIN_VALUE) {
            return 0L;
         } else if (var2 >= 0L) {
            long var4 = var0 + var2;
            return var4 < 0L ? Long.MAX_VALUE : var4;
         } else {
            return Math.max(0L, var0 + var2);
         }
      } else {
         return Long.MAX_VALUE;
      }
   }

   public static long sub(long var0, long var2) {
      assert var0 >= 0L;

      if (var0 != Long.MAX_VALUE && var2 != Long.MIN_VALUE) {
         if (var2 == Long.MAX_VALUE) {
            return 0L;
         } else if (var2 >= 0L) {
            return Math.max(0L, var0 - var2);
         } else {
            long var4 = var0 - var2;
            return var4 < 0L ? Long.MAX_VALUE : var4;
         }
      } else {
         return Long.MAX_VALUE;
      }
   }

   public static long fromMillis(double var0) {
      return Math.round(6.0 * var0);
   }

   public static long fromNano(long var0) {
      return Math.round(6.0E-6 * (double)var0);
   }

   public static long fromDuration(Duration var0) {
      return fromMillis(var0.toMillis());
   }

   public static long fromDuration(Duration var0, double var1) {
      return Math.round(6.0 * var0.toMillis() / Math.abs(var1));
   }

   public static Duration toDuration(long var0) {
      return Duration.millis(toMillis(var0));
   }

   public static double toMillis(long var0) {
      return (double)var0 / 6.0;
   }
}
