package com.sun.pisces;

public final class GradientColorMap {
   public static final int CYCLE_NONE = 0;
   public static final int CYCLE_REPEAT = 1;
   public static final int CYCLE_REFLECT = 2;
   int cycleMethod;
   private static final int LG_RAMP_SIZE = 8;
   private static final int RAMP_SIZE = 256;
   int[] fractions = null;
   int[] rgba = null;
   int[] colors = null;

   GradientColorMap(int[] var1, int[] var2, int var3) {
      this.cycleMethod = var3;
      int var4 = var1.length;
      int[] var5;
      int[] var6;
      if (var1[0] != 0) {
         var5 = new int[var4 + 1];
         var6 = new int[var4 + 1];
         System.arraycopy(var1, 0, var5, 1, var4);
         System.arraycopy(var2, 0, var6, 1, var4);
         var5[0] = 0;
         var6[0] = var2[0];
         var1 = var5;
         var2 = var6;
         ++var4;
      }

      if (var1[var4 - 1] != 65536) {
         var5 = new int[var4 + 1];
         var6 = new int[var4 + 1];
         System.arraycopy(var1, 0, var5, 0, var4);
         System.arraycopy(var2, 0, var6, 0, var4);
         var5[var4] = 65536;
         var6[var4] = var2[var4 - 1];
         var1 = var5;
         var2 = var6;
      }

      this.fractions = new int[var1.length];
      System.arraycopy(var1, 0, this.fractions, 0, var1.length);
      this.rgba = new int[var2.length];
      System.arraycopy(var2, 0, this.rgba, 0, var2.length);
      this.createRamp();
   }

   private int pad(int var1) {
      switch (this.cycleMethod) {
         case 0:
            if (var1 < 0) {
               return 0;
            } else {
               if (var1 > 65535) {
                  return 65535;
               }

               return var1;
            }
         case 1:
            return var1 & '\uffff';
         case 2:
            if (var1 < 0) {
               var1 = -var1;
            }

            var1 &= 131071;
            if (var1 > 65535) {
               var1 = 131071 - var1;
            }

            return var1;
         default:
            throw new RuntimeException("Unknown cycle method: " + this.cycleMethod);
      }
   }

   private int findStop(int var1) {
      int var2 = this.fractions.length;

      for(int var3 = 1; var3 < var2; ++var3) {
         if (this.fractions[var3] > var1) {
            return var3;
         }
      }

      return 1;
   }

   private void accumColor(int var1, int[] var2, int[] var3, int[] var4, int[] var5, int[] var6, int[] var7, int[] var8, int[] var9) {
      int var10 = this.findStop(var1);
      var1 -= this.fractions[var10 - 1];
      int var11 = this.fractions[var10] - this.fractions[var10 - 1];
      var6[0] += var2[var10 - 1] + var1 * (var2[var10] - var2[var10 - 1]) / var11;
      var7[0] += var3[var10 - 1] + var1 * (var3[var10] - var3[var10 - 1]) / var11;
      var8[0] += var4[var10 - 1] + var1 * (var4[var10] - var4[var10 - 1]) / var11;
      var9[0] += var5[var10 - 1] + var1 * (var5[var10] - var5[var10 - 1]) / var11;
   }

   private int getColorAA(int var1, int[] var2, int[] var3, int[] var4, int[] var5, int[] var6, int[] var7, int[] var8, int[] var9) {
      int var10 = this.findStop(var1);
      short var11 = 192;
      if (this.fractions[var10 - 1] < this.pad(var1 - var11) && this.pad(var1 + var11) < this.fractions[var10]) {
         var11 = 0;
      }

      byte var12 = 64;
      int var13 = 0;

      for(int var14 = -var11; var14 <= var11; var14 += var12) {
         int var15 = this.pad(var1 + var14);
         this.accumColor(var15, var2, var3, var4, var5, var6, var7, var8, var9);
         ++var13;
      }

      var9[0] /= var13;
      var6[0] /= var13;
      var7[0] /= var13;
      var8[0] /= var13;
      return var9[0] << 24 | var6[0] << 16 | var7[0] << 8 | var8[0];
   }

   private void createRamp() {
      this.colors = new int[256];
      int[] var1 = new int[1];
      int[] var2 = new int[1];
      int[] var3 = new int[1];
      int[] var4 = new int[1];
      int var5 = this.fractions.length;
      int[] var6 = new int[var5];
      int[] var7 = new int[var5];
      int[] var8 = new int[var5];
      int[] var9 = new int[var5];

      for(int var10 = 0; var10 < var5; ++var10) {
         var6[var10] = this.rgba[var10] >> 24 & 255;
         var7[var10] = this.rgba[var10] >> 16 & 255;
         var8[var10] = this.rgba[var10] >> 8 & 255;
         var9[var10] = this.rgba[var10] & 255;
      }

      short var13 = 255;
      byte var11 = 8;
      this.colors[0] = this.rgba[0];
      this.colors[var13] = this.rgba[var5 - 1];

      for(int var12 = 1; var12 < var13; ++var12) {
         var2[0] = var3[0] = var4[0] = var1[0] = 0;
         this.colors[var12] = this.getColorAA(var12 << var11, var7, var8, var9, var6, var2, var3, var4, var1);
      }

   }
}
