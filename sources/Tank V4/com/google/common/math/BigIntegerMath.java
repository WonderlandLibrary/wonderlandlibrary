package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@GwtCompatible(
   emulated = true
)
public final class BigIntegerMath {
   @VisibleForTesting
   static final int SQRT2_PRECOMPUTE_THRESHOLD = 256;
   @VisibleForTesting
   static final BigInteger SQRT2_PRECOMPUTED_BITS = new BigInteger("16a09e667f3bcc908b2fb1366ea957d3e3adec17512775099da2f590b0667322a", 16);
   private static final double LN_10 = Math.log(10.0D);
   private static final double LN_2 = Math.log(2.0D);

   public static int log2(BigInteger var0, RoundingMode var1) {
      MathPreconditions.checkPositive("x", (BigInteger)Preconditions.checkNotNull(var0));
      int var2 = var0.bitLength() - 1;
      switch(var1) {
      case UNNECESSARY:
         MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(var0));
      case DOWN:
      case FLOOR:
         return var2;
      case UP:
      case CEILING:
         return var0 > 0 ? var2 : var2 + 1;
      case HALF_DOWN:
      case HALF_UP:
      case HALF_EVEN:
         BigInteger var3;
         if (var2 < 256) {
            var3 = SQRT2_PRECOMPUTED_BITS.shiftRight(256 - var2);
            if (var0.compareTo(var3) <= 0) {
               return var2;
            }

            return var2 + 1;
         }

         var3 = var0.pow(2);
         int var4 = var3.bitLength() - 1;
         return var4 < 2 * var2 + 1 ? var2 : var2 + 1;
      default:
         throw new AssertionError();
      }
   }

   @GwtIncompatible("TODO")
   public static int log10(BigInteger param0, RoundingMode param1) {
      // $FF: Couldn't be decompiled
   }

   @GwtIncompatible("TODO")
   public static BigInteger sqrt(BigInteger param0, RoundingMode param1) {
      // $FF: Couldn't be decompiled
   }

   @GwtIncompatible("TODO")
   private static BigInteger sqrtFloor(BigInteger var0) {
      int var2 = log2(var0, RoundingMode.FLOOR);
      BigInteger var1;
      if (var2 < 1023) {
         var1 = sqrtApproxWithDoubles(var0);
      } else {
         int var3 = var2 - 52 & -2;
         var1 = sqrtApproxWithDoubles(var0.shiftRight(var3)).shiftLeft(var3 >> 1);
      }

      BigInteger var4 = var1.add(var0.divide(var1)).shiftRight(1);
      if (var1.equals(var4)) {
         return var1;
      } else {
         do {
            var1 = var4;
            var4 = var4.add(var0.divide(var4)).shiftRight(1);
         } while(var4.compareTo(var1) < 0);

         return var1;
      }
   }

   @GwtIncompatible("TODO")
   private static BigInteger sqrtApproxWithDoubles(BigInteger var0) {
      return DoubleMath.roundToBigInteger(Math.sqrt(DoubleUtils.bigToDouble(var0)), RoundingMode.HALF_EVEN);
   }

   @GwtIncompatible("TODO")
   public static BigInteger divide(BigInteger var0, BigInteger var1, RoundingMode var2) {
      BigDecimal var3 = new BigDecimal(var0);
      BigDecimal var4 = new BigDecimal(var1);
      return var3.divide(var4, 0, var2).toBigIntegerExact();
   }

   public static BigInteger factorial(int var0) {
      MathPreconditions.checkNonNegative("n", var0);
      if (var0 < LongMath.factorials.length) {
         return BigInteger.valueOf(LongMath.factorials[var0]);
      } else {
         int var1 = IntMath.divide(var0 * IntMath.log2(var0, RoundingMode.CEILING), 64, RoundingMode.CEILING);
         ArrayList var2 = new ArrayList(var1);
         int var3 = LongMath.factorials.length;
         long var4 = LongMath.factorials[var3 - 1];
         int var6 = Long.numberOfTrailingZeros(var4);
         var4 >>= var6;
         int var7 = LongMath.log2(var4, RoundingMode.FLOOR) + 1;
         int var8 = LongMath.log2((long)var3, RoundingMode.FLOOR) + 1;
         int var9 = 1 << var8 - 1;

         for(long var10 = (long)var3; var10 <= (long)var0; ++var10) {
            if ((var10 & (long)var9) != 0L) {
               var9 <<= 1;
               ++var8;
            }

            int var12 = Long.numberOfTrailingZeros(var10);
            long var13 = var10 >> var12;
            var6 += var12;
            int var15 = var8 - var12;
            if (var15 + var7 >= 64) {
               var2.add(BigInteger.valueOf(var4));
               var4 = 1L;
               boolean var16 = false;
            }

            var4 *= var13;
            var7 = LongMath.log2(var4, RoundingMode.FLOOR) + 1;
         }

         if (var4 > 1L) {
            var2.add(BigInteger.valueOf(var4));
         }

         return listProduct(var2).shiftLeft(var6);
      }
   }

   static BigInteger listProduct(List var0) {
      return listProduct(var0, 0, var0.size());
   }

   static BigInteger listProduct(List var0, int var1, int var2) {
      switch(var2 - var1) {
      case 0:
         return BigInteger.ONE;
      case 1:
         return (BigInteger)var0.get(var1);
      case 2:
         return ((BigInteger)var0.get(var1)).multiply((BigInteger)var0.get(var1 + 1));
      case 3:
         return ((BigInteger)var0.get(var1)).multiply((BigInteger)var0.get(var1 + 1)).multiply((BigInteger)var0.get(var1 + 2));
      default:
         int var3 = var2 + var1 >>> 1;
         return listProduct(var0, var1, var3).multiply(listProduct(var0, var3, var2));
      }
   }

   public static BigInteger binomial(int var0, int var1) {
      MathPreconditions.checkNonNegative("n", var0);
      MathPreconditions.checkNonNegative("k", var1);
      Preconditions.checkArgument(var1 <= var0, "k (%s) > n (%s)", var1, var0);
      if (var1 > var0 >> 1) {
         var1 = var0 - var1;
      }

      if (var1 < LongMath.biggestBinomials.length && var0 <= LongMath.biggestBinomials[var1]) {
         return BigInteger.valueOf(LongMath.binomial(var0, var1));
      } else {
         BigInteger var2 = BigInteger.ONE;
         long var3 = (long)var0;
         long var5 = 1L;
         int var7 = LongMath.log2((long)var0, RoundingMode.CEILING);
         int var8 = var7;

         for(int var9 = 1; var9 < var1; ++var9) {
            int var10 = var0 - var9;
            int var11 = var9 + 1;
            if (var8 + var7 >= 63) {
               var2 = var2.multiply(BigInteger.valueOf(var3)).divide(BigInteger.valueOf(var5));
               var3 = (long)var10;
               var5 = (long)var11;
               var8 = var7;
            } else {
               var3 *= (long)var10;
               var5 *= (long)var11;
               var8 += var7;
            }
         }

         return var2.multiply(BigInteger.valueOf(var3)).divide(BigInteger.valueOf(var5));
      }
   }

   private BigIntegerMath() {
   }
}
