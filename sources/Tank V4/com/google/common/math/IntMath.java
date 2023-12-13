package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.math.RoundingMode;

@GwtCompatible(
   emulated = true
)
public final class IntMath {
   @VisibleForTesting
   static final int MAX_POWER_OF_SQRT2_UNSIGNED = -1257966797;
   @VisibleForTesting
   static final byte[] maxLog10ForLeadingZeros = new byte[]{9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0, 0};
   @VisibleForTesting
   static final int[] powersOf10 = new int[]{1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};
   @VisibleForTesting
   static final int[] halfPowersOf10 = new int[]{3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, Integer.MAX_VALUE};
   @VisibleForTesting
   static final int FLOOR_SQRT_MAX_INT = 46340;
   private static final int[] factorials = new int[]{1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600};
   @VisibleForTesting
   static int[] biggestBinomials = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, 65536, 2345, 477, 193, 110, 75, 58, 49, 43, 39, 37, 35, 34, 34, 33};

   public static boolean isPowerOfTwo(int var0) {
      return var0 > 0 & (var0 & var0 - 1) == 0;
   }

   @VisibleForTesting
   static int lessThanBranchFree(int var0, int var1) {
      return ~(~(var0 - var1)) >>> 31;
   }

   public static int log2(int var0, RoundingMode var1) {
      MathPreconditions.checkPositive("x", var0);
      switch(var1) {
      case UNNECESSARY:
         MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(var0));
      case DOWN:
      case FLOOR:
         return 31 - Integer.numberOfLeadingZeros(var0);
      case UP:
      case CEILING:
         return 32 - Integer.numberOfLeadingZeros(var0 - 1);
      case HALF_DOWN:
      case HALF_UP:
      case HALF_EVEN:
         int var2 = Integer.numberOfLeadingZeros(var0);
         int var3 = -1257966797 >>> var2;
         int var4 = 31 - var2;
         return var4 + lessThanBranchFree(var3, var0);
      default:
         throw new AssertionError();
      }
   }

   @GwtIncompatible("need BigIntegerMath to adequately test")
   public static int log10(int var0, RoundingMode var1) {
      MathPreconditions.checkPositive("x", var0);
      int var2 = log10Floor(var0);
      int var3 = powersOf10[var2];
      switch(var1) {
      case UNNECESSARY:
         MathPreconditions.checkRoundingUnnecessary(var0 == var3);
      case DOWN:
      case FLOOR:
         return var2;
      case UP:
      case CEILING:
         return var2 + lessThanBranchFree(var3, var0);
      case HALF_DOWN:
      case HALF_UP:
      case HALF_EVEN:
         return var2 + lessThanBranchFree(halfPowersOf10[var2], var0);
      default:
         throw new AssertionError();
      }
   }

   private static int log10Floor(int var0) {
      byte var1 = maxLog10ForLeadingZeros[Integer.numberOfLeadingZeros(var0)];
      return var1 - lessThanBranchFree(var0, powersOf10[var1]);
   }

   @GwtIncompatible("failing tests")
   public static int pow(int var0, int var1) {
      MathPreconditions.checkNonNegative("exponent", var1);
      switch(var0) {
      case -2:
         if (var1 < 32) {
            return (var1 & 1) == 0 ? 1 << var1 : -(1 << var1);
         }

         return 0;
      case -1:
         return (var1 & 1) == 0 ? 1 : -1;
      case 0:
         return var1 == 0 ? 1 : 0;
      case 1:
         return 1;
      case 2:
         return var1 < 32 ? 1 << var1 : 0;
      default:
         int var2 = 1;

         while(true) {
            switch(var1) {
            case 0:
               return var2;
            case 1:
               return var0 * var2;
            }

            var2 *= (var1 & 1) == 0 ? 1 : var0;
            var0 *= var0;
            var1 >>= 1;
         }
      }
   }

   @GwtIncompatible("need BigIntegerMath to adequately test")
   public static int sqrt(int var0, RoundingMode var1) {
      MathPreconditions.checkNonNegative("x", var0);
      int var2 = sqrtFloor(var0);
      switch(var1) {
      case UNNECESSARY:
         MathPreconditions.checkRoundingUnnecessary(var2 * var2 == var0);
      case DOWN:
      case FLOOR:
         return var2;
      case UP:
      case CEILING:
         return var2 + lessThanBranchFree(var2 * var2, var0);
      case HALF_DOWN:
      case HALF_UP:
      case HALF_EVEN:
         int var3 = var2 * var2 + var2;
         return var2 + lessThanBranchFree(var3, var0);
      default:
         throw new AssertionError();
      }
   }

   private static int sqrtFloor(int var0) {
      return (int)Math.sqrt((double)var0);
   }

   public static int divide(int var0, int var1, RoundingMode var2) {
      Preconditions.checkNotNull(var2);
      if (var1 == 0) {
         throw new ArithmeticException("/ by zero");
      } else {
         int var3 = var0 / var1;
         int var4 = var0 - var1 * var3;
         if (var4 == 0) {
            return var3;
         } else {
            int var5 = 1 | (var0 ^ var1) >> 31;
            boolean var6;
            switch(var2) {
            case UNNECESSARY:
               MathPreconditions.checkRoundingUnnecessary(var4 == 0);
            case DOWN:
               var6 = false;
               break;
            case FLOOR:
               var6 = var5 < 0;
               break;
            case UP:
               var6 = true;
               break;
            case CEILING:
               var6 = var5 > 0;
               break;
            case HALF_DOWN:
            case HALF_UP:
            case HALF_EVEN:
               int var7 = Math.abs(var4);
               int var8 = var7 - (Math.abs(var1) - var7);
               if (var8 == 0) {
                  var6 = var2 == RoundingMode.HALF_UP || var2 == RoundingMode.HALF_EVEN & (var3 & 1) != 0;
               } else {
                  var6 = var8 > 0;
               }
               break;
            default:
               throw new AssertionError();
            }

            return var6 ? var3 + var5 : var3;
         }
      }
   }

   public static int mod(int var0, int var1) {
      if (var1 <= 0) {
         throw new ArithmeticException("Modulus " + var1 + " must be > 0");
      } else {
         int var2 = var0 % var1;
         return var2 >= 0 ? var2 : var2 + var1;
      }
   }

   public static int gcd(int var0, int var1) {
      MathPreconditions.checkNonNegative("a", var0);
      MathPreconditions.checkNonNegative("b", var1);
      if (var0 == 0) {
         return var1;
      } else if (var1 == 0) {
         return var0;
      } else {
         int var2 = Integer.numberOfTrailingZeros(var0);
         var0 >>= var2;
         int var3 = Integer.numberOfTrailingZeros(var1);

         for(var1 >>= var3; var0 != var1; var0 >>= Integer.numberOfTrailingZeros(var0)) {
            int var4 = var0 - var1;
            int var5 = var4 & var4 >> 31;
            var0 = var4 - var5 - var5;
            var1 += var5;
         }

         return var0 << Math.min(var2, var3);
      }
   }

   public static int checkedAdd(int var0, int var1) {
      long var2 = (long)var0 + (long)var1;
      MathPreconditions.checkNoOverflow(var2 == (long)((int)var2));
      return (int)var2;
   }

   public static int checkedSubtract(int var0, int var1) {
      long var2 = (long)var0 - (long)var1;
      MathPreconditions.checkNoOverflow(var2 == (long)((int)var2));
      return (int)var2;
   }

   public static int checkedMultiply(int var0, int var1) {
      long var2 = (long)var0 * (long)var1;
      MathPreconditions.checkNoOverflow(var2 == (long)((int)var2));
      return (int)var2;
   }

   public static int checkedPow(int var0, int var1) {
      MathPreconditions.checkNonNegative("exponent", var1);
      switch(var0) {
      case -2:
         MathPreconditions.checkNoOverflow(var1 < 32);
         return (var1 & 1) == 0 ? 1 << var1 : -1 << var1;
      case -1:
         return (var1 & 1) == 0 ? 1 : -1;
      case 0:
         return var1 == 0 ? 1 : 0;
      case 1:
         return 1;
      case 2:
         MathPreconditions.checkNoOverflow(var1 < 31);
         return 1 << var1;
      default:
         int var2 = 1;

         while(true) {
            switch(var1) {
            case 0:
               return var2;
            case 1:
               return checkedMultiply(var2, var0);
            }

            if ((var1 & 1) != 0) {
               var2 = checkedMultiply(var2, var0);
            }

            var1 >>= 1;
            if (var1 > 0) {
               MathPreconditions.checkNoOverflow(-46340 <= var0 & var0 <= 46340);
               var0 *= var0;
            }
         }
      }
   }

   public static int factorial(int var0) {
      MathPreconditions.checkNonNegative("n", var0);
      return var0 < factorials.length ? factorials[var0] : Integer.MAX_VALUE;
   }

   @GwtIncompatible("need BigIntegerMath to adequately test")
   public static int binomial(int var0, int var1) {
      MathPreconditions.checkNonNegative("n", var0);
      MathPreconditions.checkNonNegative("k", var1);
      Preconditions.checkArgument(var1 <= var0, "k (%s) > n (%s)", var1, var0);
      if (var1 > var0 >> 1) {
         var1 = var0 - var1;
      }

      if (var1 < biggestBinomials.length && var0 <= biggestBinomials[var1]) {
         switch(var1) {
         case 0:
            return 1;
         case 1:
            return var0;
         default:
            long var2 = 1L;

            for(int var4 = 0; var4 < var1; ++var4) {
               var2 *= (long)(var0 - var4);
               var2 /= (long)(var4 + 1);
            }

            return (int)var2;
         }
      } else {
         return Integer.MAX_VALUE;
      }
   }

   public static int mean(int var0, int var1) {
      return (var0 & var1) + ((var0 ^ var1) >> 1);
   }

   private IntMath() {
   }
}
