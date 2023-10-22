package com.sun.prism.impl.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.LinearGradient;

final class LinearGradientContext extends MultipleGradientContext {
   private float dgdX;
   private float dgdY;
   private float gc;

   LinearGradientContext(LinearGradient var1, BaseTransform var2, float var3, float var4, float var5, float var6, float[] var7, Color[] var8, int var9) {
      super(var1, var2, var7, var8, var9);
      float var10 = var5 - var3;
      float var11 = var6 - var4;
      float var12 = var10 * var10 + var11 * var11;
      float var13 = var10 / var12;
      float var14 = var11 / var12;
      this.dgdX = this.a00 * var13 + this.a10 * var14;
      this.dgdY = this.a01 * var13 + this.a11 * var14;
      this.gc = (this.a02 - var3) * var13 + (this.a12 - var4) * var14;
   }

   protected void fillRaster(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      float var8 = 0.0F;
      int var9 = var2 + var6;
      float var10 = this.dgdX * (float)var4 + this.gc;

      for(int var11 = 0; var11 < var7; ++var11) {
         for(var8 = var10 + this.dgdY * (float)(var5 + var11); var2 < var9; var8 += this.dgdX) {
            var1[var2++] = this.indexIntoGradientsArrays(var8);
         }

         var2 += var3;
         var9 = var2 + var6;
      }

   }
}
