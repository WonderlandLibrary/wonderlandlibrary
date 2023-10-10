package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.math.RoundingMode;

@GwtCompatible(
   emulated = true
)
public final class LongMath {
   @VisibleForTesting
   static final long MAX_POWER_OF_SQRT2_UNSIGNED = -5402926248376769404L;
   @VisibleForTesting
   static final byte[] maxLog10ForLeadingZeros = new byte[]{19, 18, 18, 18, 18, 17, 17, 17, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 13, 13, 13, 12, 12, 12, 12, 11, 11, 11, 10, 10, 10, 9, 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0};
   @GwtIncompatible("TODO")
   @VisibleForTesting
   static final long[] powersOf10 = new long[]{1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L};
   @GwtIncompatible("TODO")
   @VisibleForTesting
   static final long[] halfPowersOf10 = new long[]{3L, 31L, 316L, 3162L, 31622L, 316227L, 3162277L, 31622776L, 316227766L, 3162277660L, 31622776601L, 316227766016L, 3162277660168L, 31622776601683L, 316227766016837L, 3162277660168379L, 31622776601683793L, 316227766016837933L, 3162277660168379331L};
   @VisibleForTesting
   static final long FLOOR_SQRT_MAX_LONG = 3037000499L;
   static final long[] factorials = new long[]{1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L, 479001600L, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L};
   static final int[] biggestBinomials = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 3810779, 121977, 16175, 4337, 1733, 887, 534, 361, 265, 206, 169, 143, 125, 111, 101, 94, 88, 83, 79, 76, 74, 72, 70, 69, 68, 67, 67, 66, 66, 66, 66};
   @VisibleForTesting
   static final int[] biggestSimpleBinomials = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 2642246, 86251, 11724, 3218, 1313, 684, 419, 287, 214, 169, 139, 119, 105, 95, 87, 81, 76, 73, 70, 68, 66, 64, 63, 62, 62, 61, 61, 61};

   public static boolean isPowerOfTwo(long var0) {
      return var0 > 0L & (var0 & var0 - 1L) == 0L;
   }

   @VisibleForTesting
   static int lessThanBranchFree(long var0, long var2) {
      return (int)(~(~(var0 - var2)) >>> 63);
   }

   public static int log2(long var0, RoundingMode var2) {
      MathPreconditions.checkPositive("x", var0);
      switch(var2) {
      case UNNECESSARY:
         MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(var0));
      case DOWN:
      case FLOOR:
         return 63 - Long.numberOfLeadingZeros(var0);
      case UP:
      case CEILING:
         return 64 - Long.numberOfLeadingZeros(var0 - 1L);
      case HALF_DOWN:
      case HALF_UP:
      case HALF_EVEN:
         int var3 = Long.numberOfLeadingZeros(var0);
         long var4 = -5402926248376769404L >>> var3;
         int var6 = 63 - var3;
         return var6 + lessThanBranchFree(var4, var0);
      default:
         throw new AssertionError("impossible");
      }
   }

   @GwtIncompatible("TODO")
   public static int log10(long var0, RoundingMode var2) {
      MathPreconditions.checkPositive("x", var0);
      int var3 = log10Floor(var0);
      long var4 = powersOf10[var3];
      switch(var2) {
      case UNNECESSARY:
         MathPreconditions.checkRoundingUnnecessary(var0 == var4);
      case DOWN:
      case FLOOR:
         return var3;
      case UP:
      case CEILING:
         return var3 + lessThanBranchFree(var4, var0);
      case HALF_DOWN:
      case HALF_UP:
      case HALF_EVEN:
         return var3 + lessThanBranchFree(halfPowersOf10[var3], var0);
      default:
         throw new AssertionError();
      }
   }

   @GwtIncompatible("TODO")
   static int log10Floor(long var0) {
      byte var2 = maxLog10ForLeadingZeros[Long.numberOfLeadingZeros(var0)];
      return var2 - lessThanBranchFree(var0, powersOf10[var2]);
   }

   @GwtIncompatible("TODO")
   public static long pow(long var0, int var2) {
      MathPreconditions.checkNonNegative("exponent", var2);
      if (-2L <= var0 && var0 <= 2L) {
         switch((int)var0) {
         case -2:
            if (var2 < 64) {
               return (var2 & 1) == 0 ? 1L << var2 : -(1L << var2);
            }

            return 0L;
         case -1:
            return (var2 & 1) == 0 ? 1L : -1L;
         case 0:
            return var2 == 0 ? 1L : 0L;
         case 1:
            return 1L;
         case 2:
            return var2 < 64 ? 1L << var2 : 0L;
         default:
            throw new AssertionError();
         }
      } else {
         long var3 = 1L;

         while(true) {
            switch(var2) {
            case 0:
               return var3;
            case 1:
               return var3 * var0;
            }

            var3 *= (var2 & 1) == 0 ? 1L : var0;
            var0 *= var0;
            var2 >>= 1;
         }
      }
   }

   @GwtIncompatible("TODO")
   public static long sqrt(long var0, RoundingMode var2) {
      MathPreconditions.checkNonNegative("x", var0);
      if (var0 == false) {
         return (long)IntMath.sqrt((int)var0, var2);
      } else {
         long var3 = (long)Math.sqrt((double)var0);
         long var5 = var3 * var3;
         switch(var2) {
         case UNNECESSARY:
            MathPreconditions.checkRoundingUnnecessary(var5 == var0);
            return var3;
         case DOWN:
         case FLOOR:
            if (var0 < var5) {
               return var3 - 1L;
            }

            return var3;
         case UP:
         case CEILING:
            if (var0 > var5) {
               return var3 + 1L;
            }

            return var3;
         case HALF_DOWN:
         case HALF_UP:
         case HALF_EVEN:
            long var7 = var3 - (long)(var0 < var5 ? 1 : 0);
            long var9 = var7 * var7 + var7;
            return var7 + (long)lessThanBranchFree(var9, var0);
         default:
            throw new AssertionError();
         }
      }
   }

   @GwtIncompatible("TODO")
   public static long divide(long var0, long var2, RoundingMode var4) {
      Preconditions.checkNotNull(var4);
      long var5 = var0 / var2;
      long var7 = var0 - var2 * var5;
      if (var7 == 0L) {
         return var5;
      } else {
         int var9 = 1 | (int)((var0 ^ var2) >> 63);
         boolean var10;
         switch(var4) {
         case UNNECESSARY:
            MathPreconditions.checkRoundingUnnecessary(var7 == 0L);
         case DOWN:
            var10 = false;
            break;
         case FLOOR:
            var10 = var9 < 0;
            break;
         case UP:
            var10 = true;
            break;
         case CEILING:
            var10 = var9 > 0;
            break;
         case HALF_DOWN:
         case HALF_UP:
         case HALF_EVEN:
            long var11 = Math.abs(var7);
            long var13 = var11 - (Math.abs(var2) - var11);
            if (var13 == 0L) {
               var10 = var4 == RoundingMode.HALF_UP | var4 == RoundingMode.HALF_EVEN & (var5 & 1L) != 0L;
            } else {
               var10 = var13 > 0L;
            }
            break;
         default:
            throw new AssertionError();
         }

         return var10 ? var5 + (long)var9 : var5;
      }
   }

   @GwtIncompatible("TODO")
   public static int mod(long var0, int var2) {
      return (int)mod(var0, (long)var2);
   }

   @GwtIncompatible("TODO")
   public static long mod(long var0, long var2) {
      if (var2 <= 0L) {
         throw new ArithmeticException("Modulus must be positive");
      } else {
         long var4 = var0 % var2;
         return var4 >= 0L ? var4 : var4 + var2;
      }
   }

   public static long gcd(long var0, long var2) {
      MathPreconditions.checkNonNegative("a", var0);
      MathPreconditions.checkNonNegative("b", var2);
      if (var0 == 0L) {
         return var2;
      } else if (var2 == 0L) {
         return var0;
      } else {
         int var4 = Long.numberOfTrailingZeros(var0);
         var0 >>= var4;
         int var5 = Long.numberOfTrailingZeros(var2);

         for(var2 >>= var5; var0 != var2; var0 >>= Long.numberOfTrailingZeros(var0)) {
            long var6 = var0 - var2;
            long var8 = var6 & var6 >> 63;
            var0 = var6 - var8 - var8;
            var2 += var8;
         }

         return var0 << Math.min(var4, var5);
      }
   }

   @GwtIncompatible("TODO")
   public static long checkedAdd(long var0, long var2) {
      long var4 = var0 + var2;
      MathPreconditions.checkNoOverflow((var0 ^ var2) < 0L | (var0 ^ var4) >= 0L);
      return var4;
   }

   @GwtIncompatible("TODO")
   public static long checkedSubtract(long var0, long var2) {
      long var4 = var0 - var2;
      MathPreconditions.checkNoOverflow((var0 ^ var2) >= 0L | (var0 ^ var4) >= 0L);
      return var4;
   }

   @GwtIncompatible("TODO")
   public static long checkedMultiply(long var0, long var2) {
      int var4 = Long.numberOfLeadingZeros(var0) + Long.numberOfLeadingZeros(~var0) + Long.numberOfLeadingZeros(var2) + Long.numberOfLeadingZeros(~var2);
      if (var4 > 65) {
         return var0 * var2;
      } else {
         MathPreconditions.checkNoOverflow(var4 >= 64);
         MathPreconditions.checkNoOverflow(var0 >= 0L | var2 != Long.MIN_VALUE);
         long var5 = var0 * var2;
         MathPreconditions.checkNoOverflow(var0 == 0L || var5 / var0 == var2);
         return var5;
      }
   }

   @GwtIncompatible("TODO")
   public static long checkedPow(long var0, int var2) {
      MathPreconditions.checkNonNegative("exponent", var2);
      if (var0 >= -2L & var0 <= 2L) {
         switch((int)var0) {
         case -2:
            MathPreconditions.checkNoOverflow(var2 < 64);
            return (var2 & 1) == 0 ? 1L << var2 : -1L << var2;
         case -1:
            return (var2 & 1) == 0 ? 1L : -1L;
         case 0:
            return var2 == 0 ? 1L : 0L;
         case 1:
            return 1L;
         case 2:
            MathPreconditions.checkNoOverflow(var2 < 63);
            return 1L << var2;
         default:
            throw new AssertionError();
         }
      } else {
         long var3 = 1L;

         while(true) {
            switch(var2) {
            case 0:
               return var3;
            case 1:
               return checkedMultiply(var3, var0);
            }

            if ((var2 & 1) != 0) {
               var3 = checkedMultiply(var3, var0);
            }

            var2 >>= 1;
            if (var2 > 0) {
               MathPreconditions.checkNoOverflow(var0 <= 3037000499L);
               var0 *= var0;
            }
         }
      }
   }

   @GwtIncompatible("TODO")
   public static long factorial(int var0) {
      MathPreconditions.checkNonNegative("n", var0);
      return var0 < factorials.length ? factorials[var0] : Long.MAX_VALUE;
   }

   public static long binomial(int var0, int var1) {
      MathPreconditions.checkNonNegative("n", var0);
      MathPreconditions.checkNonNegative("k", var1);
      Preconditions.checkArgument(var1 <= var0, "k (%s) > n (%s)", var1, var0);
      if (var1 > var0 >> 1) {
         var1 = var0 - var1;
      }

      switch(var1) {
      case 0:
         return 1L;
      case 1:
         return (long)var0;
      default:
         if (var0 < factorials.length) {
            return factorials[var0] / (factorials[var1] * factorials[var0 - var1]);
         } else if (var1 < biggestBinomials.length && var0 <= biggestBinomials[var1]) {
            if (var1 < biggestSimpleBinomials.length && var0 <= biggestSimpleBinomials[var1]) {
               long var11 = (long)(var0--);

               for(int var4 = 2; var4 <= var1; ++var4) {
                  var11 *= (long)var0;
                  var11 /= (long)var4;
                  --var0;
               }

               return var11;
            } else {
               int var2 = log2((long)var0, RoundingMode.CEILING);
               long var3 = 1L;
               long var5 = (long)(var0--);
               long var7 = 1L;
               int var9 = var2;

               for(int var10 = 2; var10 <= var1; --var0) {
                  if (var9 + var2 < 63) {
                     var5 *= (long)var0;
                     var7 *= (long)var10;
                     var9 += var2;
                  } else {
                     var3 = multiplyFraction(var3, var5, var7);
                     var5 = (long)var0;
                     var7 = (long)var10;
                     var9 = var2;
                  }

                  ++var10;
               }

               return multiplyFraction(var3, var5, var7);
            }
         } else {
            return Long.MAX_VALUE;
         }
      }
   }

   static long multiplyFraction(long var0, long var2, long var4) {
      if (var0 == 1L) {
         return var2 / var4;
      } else {
         long var6 = gcd(var0, var4);
         var0 /= var6;
         var4 /= var6;
         return var0 * (var2 / var4);
      }
   }

   public static long mean(long var0, long var2) {
      return (var0 & var2) + ((var0 ^ var2) >> 1);
   }

   private LongMath() {
   }
}
