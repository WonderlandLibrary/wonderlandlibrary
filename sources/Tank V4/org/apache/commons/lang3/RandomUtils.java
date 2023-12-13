package org.apache.commons.lang3;

import java.util.Random;

public class RandomUtils {
   private static final Random RANDOM = new Random();

   public static byte[] nextBytes(int var0) {
      Validate.isTrue(var0 >= 0, "Count cannot be negative.");
      byte[] var1 = new byte[var0];
      RANDOM.nextBytes(var1);
      return var1;
   }

   public static int nextInt(int var0, int var1) {
      Validate.isTrue(var1 >= var0, "Start value must be smaller or equal to end value.");
      Validate.isTrue(var0 >= 0, "Both range values must be non-negative.");
      return var0 == var1 ? var0 : var0 + RANDOM.nextInt(var1 - var0);
   }

   public static long nextLong(long var0, long var2) {
      Validate.isTrue(var2 >= var0, "Start value must be smaller or equal to end value.");
      Validate.isTrue(var0 >= 0L, "Both range values must be non-negative.");
      return var0 == var2 ? var0 : (long)nextDouble((double)var0, (double)var2);
   }

   public static double nextDouble(double var0, double var2) {
      Validate.isTrue(var2 >= var0, "Start value must be smaller or equal to end value.");
      Validate.isTrue(var0 >= 0.0D, "Both range values must be non-negative.");
      return var0 == var2 ? var0 : var0 + (var2 - var0) * RANDOM.nextDouble();
   }

   public static float nextFloat(float var0, float var1) {
      Validate.isTrue(var1 >= var0, "Start value must be smaller or equal to end value.");
      Validate.isTrue(var0 >= 0.0F, "Both range values must be non-negative.");
      return var0 == var1 ? var0 : var0 + (var1 - var0) * RANDOM.nextFloat();
   }
}
