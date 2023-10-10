package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Booleans;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;

@GwtCompatible(
   emulated = true
)
public final class DoubleMath {
   private static final double MIN_INT_AS_DOUBLE = -2.147483648E9D;
   private static final double MAX_INT_AS_DOUBLE = 2.147483647E9D;
   private static final double MIN_LONG_AS_DOUBLE = -9.223372036854776E18D;
   private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 9.223372036854776E18D;
   private static final double LN_2 = Math.log(2.0D);
   @VisibleForTesting
   static final int MAX_FACTORIAL = 170;
   @VisibleForTesting
   static final double[] everySixteenthFactorial = new double[]{1.0D, 2.0922789888E13D, 2.631308369336935E35D, 1.2413915592536073E61D, 1.2688693218588417E89D, 7.156945704626381E118D, 9.916779348709496E149D, 1.974506857221074E182D, 3.856204823625804E215D, 5.5502938327393044E249D, 4.7147236359920616E284D};

   @GwtIncompatible("#isMathematicalInteger, com.google.common.math.DoubleUtils")
   static double roundIntermediate(double var0, RoundingMode var2) {
      if (!DoubleUtils.isFinite(var0)) {
         throw new ArithmeticException("input is infinite or NaN");
      } else {
         double var3;
         switch(var2) {
         case UNNECESSARY:
            MathPreconditions.checkRoundingUnnecessary(isMathematicalInteger(var0));
            return var0;
         case FLOOR:
            if (!(var0 >= 0.0D) && var0 == false) {
               return var0 - 1.0D;
            }

            return var0;
         case CEILING:
            if (!(var0 <= 0.0D) && var0 == false) {
               return var0 + 1.0D;
            }

            return var0;
         case DOWN:
            return var0;
         case UP:
            if (var0 != false) {
               return var0;
            }

            return var0 + Math.copySign(1.0D, var0);
         case HALF_EVEN:
            return Math.rint(var0);
         case HALF_UP:
            var3 = Math.rint(var0);
            if (Math.abs(var0 - var3) == 0.5D) {
               return var0 + Math.copySign(0.5D, var0);
            }

            return var3;
         case HALF_DOWN:
            var3 = Math.rint(var0);
            if (Math.abs(var0 - var3) == 0.5D) {
               return var0;
            }

            return var3;
         default:
            throw new AssertionError();
         }
      }
   }

   @GwtIncompatible("#roundIntermediate")
   public static int roundToInt(double var0, RoundingMode var2) {
      double var3 = roundIntermediate(var0, var2);
      MathPreconditions.checkInRange(var3 > -2.147483649E9D & var3 < 2.147483648E9D);
      return (int)var3;
   }

   @GwtIncompatible("#roundIntermediate")
   public static long roundToLong(double var0, RoundingMode var2) {
      double var3 = roundIntermediate(var0, var2);
      MathPreconditions.checkInRange(-9.223372036854776E18D - var3 < 1.0D & var3 < 9.223372036854776E18D);
      return (long)var3;
   }

   @GwtIncompatible("#roundIntermediate, java.lang.Math.getExponent, com.google.common.math.DoubleUtils")
   public static BigInteger roundToBigInteger(double var0, RoundingMode var2) {
      var0 = roundIntermediate(var0, var2);
      if (-9.223372036854776E18D - var0 < 1.0D & var0 < 9.223372036854776E18D) {
         return BigInteger.valueOf((long)var0);
      } else {
         int var3 = Math.getExponent(var0);
         long var4 = DoubleUtils.getSignificand(var0);
         BigInteger var6 = BigInteger.valueOf(var4).shiftLeft(var3 - 52);
         return var0 < 0.0D ? var6.negate() : var6;
      }
   }

   public static double log2(double var0) {
      return Math.log(var0) / LN_2;
   }

   @GwtIncompatible("java.lang.Math.getExponent, com.google.common.math.DoubleUtils")
   public static int log2(double var0, RoundingMode var2) {
      Preconditions.checkArgument(var0 > 0.0D && DoubleUtils.isFinite(var0), "x must be positive and finite");
      int var3 = Math.getExponent(var0);
      if (!DoubleUtils.isNormal(var0)) {
         return log2(var0 * 4.503599627370496E15D, var2) - 52;
      } else {
         boolean var4;
         switch(var2) {
         case UNNECESSARY:
            MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(var0));
         case FLOOR:
            var4 = false;
            break;
         case CEILING:
            var4 = var0 > 0;
            break;
         case DOWN:
            var4 = var3 < 0 & var0 > 0;
            break;
         case UP:
            var4 = var3 >= 0 & var0 > 0;
            break;
         case HALF_EVEN:
         case HALF_UP:
         case HALF_DOWN:
            double var5 = DoubleUtils.scaleNormalize(var0);
            var4 = var5 * var5 > 2.0D;
            break;
         default:
            throw new AssertionError();
         }

         return var4 ? var3 + 1 : var3;
      }
   }

   public static double factorial(int var0) {
      MathPreconditions.checkNonNegative("n", var0);
      if (var0 > 170) {
         return Double.POSITIVE_INFINITY;
      } else {
         double var1 = 1.0D;

         for(int var3 = 1 + (var0 & -16); var3 <= var0; ++var3) {
            var1 *= (double)var3;
         }

         return var1 * everySixteenthFactorial[var0 >> 4];
      }
   }

   public static int fuzzyCompare(double var0, double var2, double var4) {
      if (var4 > 0) {
         return 0;
      } else if (var0 < var2) {
         return -1;
      } else {
         return var0 > var2 ? 1 : Booleans.compare(Double.isNaN(var0), Double.isNaN(var2));
      }
   }

   @GwtIncompatible("MeanAccumulator")
   public static double mean(double... var0) {
      DoubleMath.MeanAccumulator var1 = new DoubleMath.MeanAccumulator();
      double[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         double var5 = var2[var4];
         var1.add(var5);
      }

      return var1.mean();
   }

   @GwtIncompatible("MeanAccumulator")
   public static double mean(int... var0) {
      DoubleMath.MeanAccumulator var1 = new DoubleMath.MeanAccumulator();
      int[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = var2[var4];
         var1.add((double)var5);
      }

      return var1.mean();
   }

   @GwtIncompatible("MeanAccumulator")
   public static double mean(long... var0) {
      DoubleMath.MeanAccumulator var1 = new DoubleMath.MeanAccumulator();
      long[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         long var5 = var2[var4];
         var1.add((double)var5);
      }

      return var1.mean();
   }

   @GwtIncompatible("MeanAccumulator")
   public static double mean(Iterable var0) {
      DoubleMath.MeanAccumulator var1 = new DoubleMath.MeanAccumulator();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         Number var3 = (Number)var2.next();
         var1.add(var3.doubleValue());
      }

      return var1.mean();
   }

   @GwtIncompatible("MeanAccumulator")
   public static double mean(Iterator var0) {
      DoubleMath.MeanAccumulator var1 = new DoubleMath.MeanAccumulator();

      while(var0.hasNext()) {
         var1.add(((Number)var0.next()).doubleValue());
      }

      return var1.mean();
   }

   private DoubleMath() {
   }

   @GwtIncompatible("com.google.common.math.DoubleUtils")
   private static final class MeanAccumulator {
      private long count;
      private double mean;

      private MeanAccumulator() {
         this.count = 0L;
         this.mean = 0.0D;
      }

      void add(double var1) {
         Preconditions.checkArgument(DoubleUtils.isFinite(var1));
         ++this.count;
         this.mean += (var1 - this.mean) / (double)this.count;
      }

      double mean() {
         Preconditions.checkArgument(this.count > 0L, "Cannot take mean of 0 values");
         return this.mean;
      }

      MeanAccumulator(Object var1) {
         this();
      }
   }
}
