package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import java.math.BigInteger;
import javax.annotation.Nullable;

@GwtCompatible
final class MathPreconditions {
   static int checkPositive(@Nullable String var0, int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException(var0 + " (" + var1 + ") must be > 0");
      } else {
         return var1;
      }
   }

   static long checkPositive(@Nullable String var0, long var1) {
      if (var1 <= 0L) {
         throw new IllegalArgumentException(var0 + " (" + var1 + ") must be > 0");
      } else {
         return var1;
      }
   }

   static BigInteger checkPositive(@Nullable String var0, BigInteger var1) {
      if (var1.signum() <= 0) {
         throw new IllegalArgumentException(var0 + " (" + var1 + ") must be > 0");
      } else {
         return var1;
      }
   }

   static int checkNonNegative(@Nullable String var0, int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException(var0 + " (" + var1 + ") must be >= 0");
      } else {
         return var1;
      }
   }

   static long checkNonNegative(@Nullable String var0, long var1) {
      if (var1 < 0L) {
         throw new IllegalArgumentException(var0 + " (" + var1 + ") must be >= 0");
      } else {
         return var1;
      }
   }

   static BigInteger checkNonNegative(@Nullable String var0, BigInteger var1) {
      if (var1.signum() < 0) {
         throw new IllegalArgumentException(var0 + " (" + var1 + ") must be >= 0");
      } else {
         return var1;
      }
   }

   static double checkNonNegative(@Nullable String var0, double var1) {
      if (!(var1 >= 0.0D)) {
         throw new IllegalArgumentException(var0 + " (" + var1 + ") must be >= 0");
      } else {
         return var1;
      }
   }

   static void checkRoundingUnnecessary(boolean var0) {
      if (!var0) {
         throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
      }
   }

   static void checkInRange(boolean var0) {
      if (!var0) {
         throw new ArithmeticException("not in range");
      }
   }

   static void checkNoOverflow(boolean var0) {
      if (!var0) {
         throw new ArithmeticException("overflow");
      }
   }

   private MathPreconditions() {
   }
}
