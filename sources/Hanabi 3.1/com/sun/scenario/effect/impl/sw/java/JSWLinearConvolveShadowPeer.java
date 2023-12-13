package com.sun.scenario.effect.impl.sw.java;

import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;

public class JSWLinearConvolveShadowPeer extends JSWLinearConvolvePeer {
   public JSWLinearConvolveShadowPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   private float[] getShadowColor() {
      return ((LinearConvolveRenderState)this.getRenderState()).getPassShadowColorComponents();
   }

   protected void filterVector(int[] var1, int var2, int var3, int var4, int[] var5, int var6, int var7, int var8, float[] var9, int var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20) {
      float[] var21 = this.getShadowColor();
      int var22 = 0;
      var11 += (var19 + var17) * 0.5F;
      var12 += (var20 + var18) * 0.5F;

      for(int var23 = 0; var23 < var3; ++var23) {
         float var24 = var11;
         float var25 = var12;

         for(int var26 = 0; var26 < var2; ++var26) {
            float var27 = 0.0F;
            float var28 = var24 + var13;
            float var29 = var25 + var14;

            for(int var30 = 0; var30 < var10; ++var30) {
               if (var28 >= 0.0F && var29 >= 0.0F) {
                  int var31 = (int)var28;
                  int var32 = (int)var29;
                  if (var31 < var6 && var32 < var7) {
                     int var33 = var5[var32 * var8 + var31];
                     var27 += (float)(var33 >>> 24) * var9[var30];
                  }
               }

               var28 += var15;
               var29 += var16;
            }

            var27 = var27 < 0.0F ? 0.0F : (var27 > 255.0F ? 255.0F : var27);
            var1[var22 + var26] = (int)(var21[0] * var27) << 16 | (int)(var21[1] * var27) << 8 | (int)(var21[2] * var27) | (int)(var21[3] * var27) << 24;
            var24 += var17;
            var25 += var18;
         }

         var11 += var19;
         var12 += var20;
         var22 += var4;
      }

   }

   protected void filterHV(int[] var1, int var2, int var3, int var4, int var5, int[] var6, int var7, int var8, int var9, int var10, float[] var11) {
      float[] var12 = this.getShadowColor();
      int var13 = var11.length / 2;
      float[] var14 = new float[var13];
      int var15 = 0;
      int var16 = 0;
      int[] var17 = new int[256];

      int var18;
      for(var18 = 0; var18 < var17.length; ++var18) {
         var17[var18] = (int)(var12[0] * (float)var18) << 16 | (int)(var12[1] * (float)var18) << 8 | (int)(var12[2] * (float)var18) | (int)(var12[3] * (float)var18) << 24;
      }

      for(var18 = 0; var18 < var3; ++var18) {
         int var19 = var15;
         int var20 = var16;

         int var21;
         for(var21 = 0; var21 < var14.length; ++var21) {
            var14[var21] = 0.0F;
         }

         var21 = var13;

         for(int var22 = 0; var22 < var2; ++var22) {
            var14[var13 - var21] = (float)((var22 < var7 ? var6[var20] : 0) >>> 24);
            --var21;
            if (var21 <= 0) {
               var21 += var13;
            }

            float var23 = -0.5F;

            for(int var24 = 0; var24 < var14.length; ++var24) {
               var23 += var14[var24] * var11[var21 + var24];
            }

            var1[var19] = var23 < 0.0F ? 0 : (var23 >= 254.0F ? var17[255] : var17[(int)var23 + 1]);
            var19 += var4;
            var20 += var9;
         }

         var15 += var5;
         var16 += var10;
      }

   }
}
