package org.apache.commons.lang3.math;

public class IEEE754rUtils {
   public static double min(double[] var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("The Array must not be null");
      } else if (var0.length == 0) {
         throw new IllegalArgumentException("Array cannot be empty.");
      } else {
         double var1 = var0[0];

         for(int var3 = 1; var3 < var0.length; ++var3) {
            var1 = min(var0[var3], var1);
         }

         return var1;
      }
   }

   public static float min(float[] var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("The Array must not be null");
      } else if (var0.length == 0) {
         throw new IllegalArgumentException("Array cannot be empty.");
      } else {
         float var1 = var0[0];

         for(int var2 = 1; var2 < var0.length; ++var2) {
            var1 = min(var0[var2], var1);
         }

         return var1;
      }
   }

   public static double min(double var0, double var2, double var4) {
      return min(min(var0, var2), var4);
   }

   public static double min(double var0, double var2) {
      if (Double.isNaN(var0)) {
         return var2;
      } else {
         return Double.isNaN(var2) ? var0 : Math.min(var0, var2);
      }
   }

   public static float min(float var0, float var1, float var2) {
      return min(min(var0, var1), var2);
   }

   public static float min(float var0, float var1) {
      if (Float.isNaN(var0)) {
         return var1;
      } else {
         return Float.isNaN(var1) ? var0 : Math.min(var0, var1);
      }
   }

   public static double max(double[] var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("The Array must not be null");
      } else if (var0.length == 0) {
         throw new IllegalArgumentException("Array cannot be empty.");
      } else {
         double var1 = var0[0];

         for(int var3 = 1; var3 < var0.length; ++var3) {
            var1 = max(var0[var3], var1);
         }

         return var1;
      }
   }

   public static float max(float[] var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("The Array must not be null");
      } else if (var0.length == 0) {
         throw new IllegalArgumentException("Array cannot be empty.");
      } else {
         float var1 = var0[0];

         for(int var2 = 1; var2 < var0.length; ++var2) {
            var1 = max(var0[var2], var1);
         }

         return var1;
      }
   }

   public static double max(double var0, double var2, double var4) {
      return max(max(var0, var2), var4);
   }

   public static double max(double var0, double var2) {
      if (Double.isNaN(var0)) {
         return var2;
      } else {
         return Double.isNaN(var2) ? var0 : Math.max(var0, var2);
      }
   }

   public static float max(float var0, float var1, float var2) {
      return max(max(var0, var1), var2);
   }

   public static float max(float var0, float var1) {
      if (Float.isNaN(var0)) {
         return var1;
      } else {
         return Float.isNaN(var1) ? var0 : Math.max(var0, var1);
      }
   }
}
