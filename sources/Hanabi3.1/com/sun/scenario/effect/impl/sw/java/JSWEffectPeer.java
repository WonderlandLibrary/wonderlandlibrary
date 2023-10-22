package com.sun.scenario.effect.impl.sw.java;

import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;

public abstract class JSWEffectPeer extends EffectPeer {
   protected static final int FVALS_A = 3;
   protected static final int FVALS_R = 0;
   protected static final int FVALS_G = 1;
   protected static final int FVALS_B = 2;

   protected JSWEffectPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   protected final void laccum(int var1, float var2, float[] var3) {
      var2 /= 255.0F;
      var3[0] += (float)(var1 >> 16 & 255) * var2;
      var3[1] += (float)(var1 >> 8 & 255) * var2;
      var3[2] += (float)(var1 & 255) * var2;
      var3[3] += (float)(var1 >>> 24) * var2;
   }

   protected final void lsample(int[] var1, float var2, float var3, int var4, int var5, int var6, float[] var7) {
      var7[0] = 0.0F;
      var7[1] = 0.0F;
      var7[2] = 0.0F;
      var7[3] = 0.0F;
      var2 = var2 * (float)var4 + 0.5F;
      var3 = var3 * (float)var5 + 0.5F;
      int var8 = (int)var2;
      int var9 = (int)var3;
      if (var2 > 0.0F && var3 > 0.0F && var8 <= var4 && var9 <= var5) {
         var2 -= (float)var8;
         var3 -= (float)var9;
         int var10 = var9 * var6 + var8;
         float var11 = var2 * var3;
         if (var9 < var5) {
            if (var8 < var4) {
               this.laccum(var1[var10], var11, var7);
            }

            if (var8 > 0) {
               this.laccum(var1[var10 - 1], var3 - var11, var7);
            }
         }

         if (var9 > 0) {
            if (var8 < var4) {
               this.laccum(var1[var10 - var6], var2 - var11, var7);
            }

            if (var8 > 0) {
               this.laccum(var1[var10 - var6 - 1], 1.0F - var2 - var3 + var11, var7);
            }
         }
      }

   }

   protected final void laccumsample(int[] var1, float var2, float var3, int var4, int var5, int var6, float var7, float[] var8) {
      var7 *= 255.0F;
      var2 += 0.5F;
      var3 += 0.5F;
      int var9 = (int)var2;
      int var10 = (int)var3;
      if (var2 > 0.0F && var3 > 0.0F && var9 <= var4 && var10 <= var5) {
         var2 -= (float)var9;
         var3 -= (float)var10;
         int var11 = var10 * var6 + var9;
         float var12 = var2 * var3;
         if (var10 < var5) {
            if (var9 < var4) {
               this.laccum(var1[var11], var12 * var7, var8);
            }

            if (var9 > 0) {
               this.laccum(var1[var11 - 1], (var3 - var12) * var7, var8);
            }
         }

         if (var10 > 0) {
            if (var9 < var4) {
               this.laccum(var1[var11 - var6], (var2 - var12) * var7, var8);
            }

            if (var9 > 0) {
               this.laccum(var1[var11 - var6 - 1], (1.0F - var2 - var3 + var12) * var7, var8);
            }
         }
      }

   }

   protected final void faccum(float[] var1, int var2, float var3, float[] var4) {
      var4[0] += var1[var2] * var3;
      var4[1] += var1[var2 + 1] * var3;
      var4[2] += var1[var2 + 2] * var3;
      var4[3] += var1[var2 + 3] * var3;
   }

   protected final void fsample(float[] var1, float var2, float var3, int var4, int var5, int var6, float[] var7) {
      var7[0] = 0.0F;
      var7[1] = 0.0F;
      var7[2] = 0.0F;
      var7[3] = 0.0F;
      var2 = var2 * (float)var4 + 0.5F;
      var3 = var3 * (float)var5 + 0.5F;
      int var8 = (int)var2;
      int var9 = (int)var3;
      if (var2 > 0.0F && var3 > 0.0F && var8 <= var4 && var9 <= var5) {
         var2 -= (float)var8;
         var3 -= (float)var9;
         int var10 = 4 * (var9 * var6 + var8);
         float var11 = var2 * var3;
         if (var9 < var5) {
            if (var8 < var4) {
               this.faccum(var1, var10, var11, var7);
            }

            if (var8 > 0) {
               this.faccum(var1, var10 - 4, var3 - var11, var7);
            }
         }

         if (var9 > 0) {
            if (var8 < var4) {
               this.faccum(var1, var10 - var6 * 4, var2 - var11, var7);
            }

            if (var8 > 0) {
               this.faccum(var1, var10 - var6 * 4 - 4, 1.0F - var2 - var3 + var11, var7);
            }
         }
      }

   }
}
