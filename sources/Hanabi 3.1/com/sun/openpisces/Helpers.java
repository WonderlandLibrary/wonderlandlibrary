package com.sun.openpisces;

import java.util.Arrays;

final class Helpers {
   private Helpers() {
      throw new Error("This is a non instantiable class");
   }

   static boolean within(float var0, float var1, float var2) {
      float var3 = var1 - var0;
      return var3 <= var2 && var3 >= -var2;
   }

   static boolean within(double var0, double var2, double var4) {
      double var6 = var2 - var0;
      return var6 <= var4 && var6 >= -var4;
   }

   static int quadraticRoots(float var0, float var1, float var2, float[] var3, int var4) {
      int var5 = var4;
      float var6;
      if (var0 != 0.0F) {
         float var7 = var1 * var1 - 4.0F * var0 * var2;
         if (var7 > 0.0F) {
            float var8 = (float)Math.sqrt((double)var7);
            if (var1 >= 0.0F) {
               var5 = var4 + 1;
               var3[var4] = 2.0F * var2 / (-var1 - var8);
               var3[var5++] = (-var1 - var8) / (2.0F * var0);
            } else {
               var5 = var4 + 1;
               var3[var4] = (-var1 + var8) / (2.0F * var0);
               var3[var5++] = 2.0F * var2 / (-var1 + var8);
            }
         } else if (var7 == 0.0F) {
            var6 = -var1 / (2.0F * var0);
            var5 = var4 + 1;
            var3[var4] = var6;
         }
      } else if (var1 != 0.0F) {
         var6 = -var2 / var1;
         var5 = var4 + 1;
         var3[var4] = var6;
      }

      return var5 - var4;
   }

   static int cubicRootsInAB(float var0, float var1, float var2, float var3, float[] var4, int var5, float var6, float var7) {
      if (var0 == 0.0F) {
         int var25 = quadraticRoots(var1, var2, var3, var4, var5);
         return filterOutNotInAB(var4, var5, var25, var6, var7) - var5;
      } else {
         var1 /= var0;
         var2 /= var0;
         var3 /= var0;
         double var8 = (double)(var1 * var1);
         double var10 = 0.3333333333333333 * (-0.3333333333333333 * var8 + (double)var2);
         double var12 = 0.5 * (0.07407407407407407 * (double)var1 * var8 - 0.3333333333333333 * (double)var1 * (double)var2 + (double)var3);
         double var14 = var10 * var10 * var10;
         double var16 = var12 * var12 + var14;
         byte var18;
         double var19;
         double var21;
         if (var16 < 0.0) {
            var19 = 0.3333333333333333 * Math.acos(-var12 / Math.sqrt(-var14));
            var21 = 2.0 * Math.sqrt(-var10);
            var4[var5 + 0] = (float)(var21 * Math.cos(var19));
            var4[var5 + 1] = (float)(-var21 * Math.cos(var19 + 1.0471975511965976));
            var4[var5 + 2] = (float)(-var21 * Math.cos(var19 - 1.0471975511965976));
            var18 = 3;
         } else {
            var19 = Math.sqrt(var16);
            var21 = Math.cbrt(var19 - var12);
            double var23 = -Math.cbrt(var19 + var12);
            var4[var5] = (float)(var21 + var23);
            var18 = 1;
            if (within(var16, 0.0, 1.0E-8)) {
               var4[var5 + 1] = -(var4[var5] / 2.0F);
               var18 = 2;
            }
         }

         float var26 = 0.33333334F * var1;

         for(int var20 = 0; var20 < var18; ++var20) {
            var4[var5 + var20] -= var26;
         }

         return filterOutNotInAB(var4, var5, var18, var6, var7) - var5;
      }
   }

   static float[] widenArray(float[] var0, int var1, int var2) {
      return var0.length >= var1 + var2 ? var0 : Arrays.copyOf(var0, 2 * (var1 + var2));
   }

   static int[] widenArray(int[] var0, int var1, int var2) {
      return var0.length >= var1 + var2 ? var0 : Arrays.copyOf(var0, 2 * (var1 + var2));
   }

   static float evalCubic(float var0, float var1, float var2, float var3, float var4) {
      return var4 * (var4 * (var4 * var0 + var1) + var2) + var3;
   }

   static float evalQuad(float var0, float var1, float var2, float var3) {
      return var3 * (var3 * var0 + var1) + var2;
   }

   static int filterOutNotInAB(float[] var0, int var1, int var2, float var3, float var4) {
      int var5 = var1;

      for(int var6 = var1; var6 < var1 + var2; ++var6) {
         if (var0[var6] >= var3 && var0[var6] < var4) {
            var0[var5++] = var0[var6];
         }
      }

      return var5;
   }

   static float polyLineLength(float[] var0, int var1, int var2) {
      assert var2 % 2 == 0 && var0.length >= var1 + var2 : "";

      float var3 = 0.0F;

      for(int var4 = var1 + 2; var4 < var1 + var2; var4 += 2) {
         var3 += linelen(var0[var4], var0[var4 + 1], var0[var4 - 2], var0[var4 - 1]);
      }

      return var3;
   }

   static float linelen(float var0, float var1, float var2, float var3) {
      float var4 = var2 - var0;
      float var5 = var3 - var1;
      return (float)Math.sqrt((double)(var4 * var4 + var5 * var5));
   }

   static void subdivide(float[] var0, int var1, float[] var2, int var3, float[] var4, int var5, int var6) {
      switch (var6) {
         case 6:
            subdivideQuad(var0, var1, var2, var3, var4, var5);
            break;
         case 8:
            subdivideCubic(var0, var1, var2, var3, var4, var5);
            break;
         default:
            throw new InternalError("Unsupported curve type");
      }

   }

   static void isort(float[] var0, int var1, int var2) {
      for(int var3 = var1 + 1; var3 < var1 + var2; ++var3) {
         float var4 = var0[var3];

         int var5;
         for(var5 = var3 - 1; var5 >= var1 && var0[var5] > var4; --var5) {
            var0[var5 + 1] = var0[var5];
         }

         var0[var5 + 1] = var4;
      }

   }

   static void subdivideCubic(float[] var0, int var1, float[] var2, int var3, float[] var4, int var5) {
      float var6 = var0[var1 + 0];
      float var7 = var0[var1 + 1];
      float var8 = var0[var1 + 2];
      float var9 = var0[var1 + 3];
      float var10 = var0[var1 + 4];
      float var11 = var0[var1 + 5];
      float var12 = var0[var1 + 6];
      float var13 = var0[var1 + 7];
      if (var2 != null) {
         var2[var3 + 0] = var6;
         var2[var3 + 1] = var7;
      }

      if (var4 != null) {
         var4[var5 + 6] = var12;
         var4[var5 + 7] = var13;
      }

      var6 = (var6 + var8) / 2.0F;
      var7 = (var7 + var9) / 2.0F;
      var12 = (var12 + var10) / 2.0F;
      var13 = (var13 + var11) / 2.0F;
      float var14 = (var8 + var10) / 2.0F;
      float var15 = (var9 + var11) / 2.0F;
      var8 = (var6 + var14) / 2.0F;
      var9 = (var7 + var15) / 2.0F;
      var10 = (var12 + var14) / 2.0F;
      var11 = (var13 + var15) / 2.0F;
      var14 = (var8 + var10) / 2.0F;
      var15 = (var9 + var11) / 2.0F;
      if (var2 != null) {
         var2[var3 + 2] = var6;
         var2[var3 + 3] = var7;
         var2[var3 + 4] = var8;
         var2[var3 + 5] = var9;
         var2[var3 + 6] = var14;
         var2[var3 + 7] = var15;
      }

      if (var4 != null) {
         var4[var5 + 0] = var14;
         var4[var5 + 1] = var15;
         var4[var5 + 2] = var10;
         var4[var5 + 3] = var11;
         var4[var5 + 4] = var12;
         var4[var5 + 5] = var13;
      }

   }

   static void subdivideCubicAt(float var0, float[] var1, int var2, float[] var3, int var4, float[] var5, int var6) {
      float var7 = var1[var2 + 0];
      float var8 = var1[var2 + 1];
      float var9 = var1[var2 + 2];
      float var10 = var1[var2 + 3];
      float var11 = var1[var2 + 4];
      float var12 = var1[var2 + 5];
      float var13 = var1[var2 + 6];
      float var14 = var1[var2 + 7];
      if (var3 != null) {
         var3[var4 + 0] = var7;
         var3[var4 + 1] = var8;
      }

      if (var5 != null) {
         var5[var6 + 6] = var13;
         var5[var6 + 7] = var14;
      }

      var7 += var0 * (var9 - var7);
      var8 += var0 * (var10 - var8);
      var13 = var11 + var0 * (var13 - var11);
      var14 = var12 + var0 * (var14 - var12);
      float var15 = var9 + var0 * (var11 - var9);
      float var16 = var10 + var0 * (var12 - var10);
      var9 = var7 + var0 * (var15 - var7);
      var10 = var8 + var0 * (var16 - var8);
      var11 = var15 + var0 * (var13 - var15);
      var12 = var16 + var0 * (var14 - var16);
      var15 = var9 + var0 * (var11 - var9);
      var16 = var10 + var0 * (var12 - var10);
      if (var3 != null) {
         var3[var4 + 2] = var7;
         var3[var4 + 3] = var8;
         var3[var4 + 4] = var9;
         var3[var4 + 5] = var10;
         var3[var4 + 6] = var15;
         var3[var4 + 7] = var16;
      }

      if (var5 != null) {
         var5[var6 + 0] = var15;
         var5[var6 + 1] = var16;
         var5[var6 + 2] = var11;
         var5[var6 + 3] = var12;
         var5[var6 + 4] = var13;
         var5[var6 + 5] = var14;
      }

   }

   static void subdivideQuad(float[] var0, int var1, float[] var2, int var3, float[] var4, int var5) {
      float var6 = var0[var1 + 0];
      float var7 = var0[var1 + 1];
      float var8 = var0[var1 + 2];
      float var9 = var0[var1 + 3];
      float var10 = var0[var1 + 4];
      float var11 = var0[var1 + 5];
      if (var2 != null) {
         var2[var3 + 0] = var6;
         var2[var3 + 1] = var7;
      }

      if (var4 != null) {
         var4[var5 + 4] = var10;
         var4[var5 + 5] = var11;
      }

      var6 = (var6 + var8) / 2.0F;
      var7 = (var7 + var9) / 2.0F;
      var10 = (var10 + var8) / 2.0F;
      var11 = (var11 + var9) / 2.0F;
      var8 = (var6 + var10) / 2.0F;
      var9 = (var7 + var11) / 2.0F;
      if (var2 != null) {
         var2[var3 + 2] = var6;
         var2[var3 + 3] = var7;
         var2[var3 + 4] = var8;
         var2[var3 + 5] = var9;
      }

      if (var4 != null) {
         var4[var5 + 0] = var8;
         var4[var5 + 1] = var9;
         var4[var5 + 2] = var10;
         var4[var5 + 3] = var11;
      }

   }

   static void subdivideQuadAt(float var0, float[] var1, int var2, float[] var3, int var4, float[] var5, int var6) {
      float var7 = var1[var2 + 0];
      float var8 = var1[var2 + 1];
      float var9 = var1[var2 + 2];
      float var10 = var1[var2 + 3];
      float var11 = var1[var2 + 4];
      float var12 = var1[var2 + 5];
      if (var3 != null) {
         var3[var4 + 0] = var7;
         var3[var4 + 1] = var8;
      }

      if (var5 != null) {
         var5[var6 + 4] = var11;
         var5[var6 + 5] = var12;
      }

      var7 += var0 * (var9 - var7);
      var8 += var0 * (var10 - var8);
      var11 = var9 + var0 * (var11 - var9);
      var12 = var10 + var0 * (var12 - var10);
      var9 = var7 + var0 * (var11 - var7);
      var10 = var8 + var0 * (var12 - var8);
      if (var3 != null) {
         var3[var4 + 2] = var7;
         var3[var4 + 3] = var8;
         var3[var4 + 4] = var9;
         var3[var4 + 5] = var10;
      }

      if (var5 != null) {
         var5[var6 + 0] = var9;
         var5[var6 + 1] = var10;
         var5[var6 + 2] = var11;
         var5[var6 + 3] = var12;
      }

   }

   static void subdivideAt(float var0, float[] var1, int var2, float[] var3, int var4, float[] var5, int var6, int var7) {
      switch (var7) {
         case 6:
            subdivideQuadAt(var0, var1, var2, var3, var4, var5, var6);
            break;
         case 8:
            subdivideCubicAt(var0, var1, var2, var3, var4, var5, var6);
      }

   }
}
