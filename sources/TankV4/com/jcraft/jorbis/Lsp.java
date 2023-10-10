package com.jcraft.jorbis;

class Lsp {
   static final float M_PI = 3.1415927F;

   static void lsp_to_curve(float[] var0, int[] var1, int var2, int var3, float[] var4, int var5, float var6, float var7) {
      float var9 = 3.1415927F / (float)var3;

      int var8;
      for(var8 = 0; var8 < var5; ++var8) {
         var4[var8] = Lookup.coslook(var4[var8]);
      }

      int var10 = var5 / 2 * 2;
      var8 = 0;

      while(var8 < var2) {
         int var11 = var1[var8];
         float var12 = 0.70710677F;
         float var13 = 0.70710677F;
         float var14 = Lookup.coslook(var9 * (float)var11);

         int var15;
         for(var15 = 0; var15 < var10; var15 += 2) {
            var13 *= var4[var15] - var14;
            var12 *= var4[var15 + 1] - var14;
         }

         if ((var5 & 1) != 0) {
            var13 *= var4[var5 - 1] - var14;
            var13 *= var13;
            var12 *= var12 * (1.0F - var14 * var14);
         } else {
            var13 *= var13 * (1.0F + var14);
            var12 *= var12 * (1.0F - var14);
         }

         var13 += var12;
         var15 = Float.floatToIntBits(var13);
         int var16 = Integer.MAX_VALUE & var15;
         int var17 = 0;
         if (var16 < 2139095040 && var16 != 0) {
            if (var16 < 8388608) {
               var13 = (float)((double)var13 * 3.3554432E7D);
               var15 = Float.floatToIntBits(var13);
               var16 = Integer.MAX_VALUE & var15;
               var17 = -25;
            }

            var17 += (var16 >>> 23) - 126;
            var15 = var15 & -2139095041 | 1056964608;
            var13 = Float.intBitsToFloat(var15);
         }

         var13 = Lookup.fromdBlook(var6 * Lookup.invsqlook(var13) * Lookup.invsq2explook(var17 + var5) - var7);

         while(true) {
            int var10001 = var8++;
            var0[var10001] *= var13;
            if (var8 >= var2 || var1[var8] != var11) {
               break;
            }
         }
      }

   }
}
