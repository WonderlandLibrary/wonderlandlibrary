package com.sun.prism.j2d.paint;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

final class RadialGradientPaintContext extends MultipleGradientPaintContext {
   private boolean isSimpleFocus = false;
   private boolean isNonCyclic = false;
   private float radius;
   private float centerX;
   private float centerY;
   private float focusX;
   private float focusY;
   private float radiusSq;
   private float constA;
   private float constB;
   private float gDeltaDelta;
   private float trivial;
   private static final float SCALEBACK = 0.99F;
   private static final int SQRT_LUT_SIZE = 2048;
   private static float[] sqrtLut = new float[2049];

   RadialGradientPaintContext(RadialGradientPaint var1, ColorModel var2, Rectangle var3, Rectangle2D var4, AffineTransform var5, RenderingHints var6, float var7, float var8, float var9, float var10, float var11, float[] var12, Color[] var13, MultipleGradientPaint.CycleMethod var14, MultipleGradientPaint.ColorSpaceType var15) {
      super(var1, var2, var3, var4, var5, var6, var12, var13, var14, var15);
      this.centerX = var7;
      this.centerY = var8;
      this.focusX = var10;
      this.focusY = var11;
      this.radius = var9;
      this.isSimpleFocus = this.focusX == this.centerX && this.focusY == this.centerY;
      this.isNonCyclic = var14 == MultipleGradientPaint.CycleMethod.NO_CYCLE;
      this.radiusSq = this.radius * this.radius;
      float var16 = this.focusX - this.centerX;
      float var17 = this.focusY - this.centerY;
      double var18 = (double)(var16 * var16 + var17 * var17);
      if (var18 > (double)(this.radiusSq * 0.99F)) {
         float var20 = (float)Math.sqrt((double)(this.radiusSq * 0.99F) / var18);
         var16 *= var20;
         var17 *= var20;
         this.focusX = this.centerX + var16;
         this.focusY = this.centerY + var17;
      }

      this.trivial = (float)Math.sqrt((double)(this.radiusSq - var16 * var16));
      this.constA = this.a02 - this.centerX;
      this.constB = this.a12 - this.centerY;
      this.gDeltaDelta = 2.0F * (this.a00 * this.a00 + this.a10 * this.a10) / this.radiusSq;
   }

   protected void fillRaster(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      if (this.isSimpleFocus && this.isNonCyclic && this.isSimpleLookup) {
         this.simpleNonCyclicFillRaster(var1, var2, var3, var4, var5, var6, var7);
      } else {
         this.cyclicCircularGradientFillRaster(var1, var2, var3, var4, var5, var6, var7);
      }

   }

   private void simpleNonCyclicFillRaster(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      float var8 = this.a00 * (float)var4 + this.a01 * (float)var5 + this.constA;
      float var9 = this.a10 * (float)var4 + this.a11 * (float)var5 + this.constB;
      float var10 = this.gDeltaDelta;
      var3 += var6;
      int var11 = this.gradient[this.fastGradientArraySize];

      for(int var12 = 0; var12 < var7; ++var12) {
         float var13 = (var8 * var8 + var9 * var9) / this.radiusSq;
         float var14 = 2.0F * (this.a00 * var8 + this.a10 * var9) / this.radiusSq + var10 / 2.0F;

         int var15;
         for(var15 = 0; var15 < var6 && var13 >= 1.0F; ++var15) {
            var1[var2 + var15] = var11;
            var13 += var14;
            var14 += var10;
         }

         while(var15 < var6 && var13 < 1.0F) {
            int var16;
            if (var13 <= 0.0F) {
               var16 = 0;
            } else {
               float var17 = var13 * 2048.0F;
               int var18 = (int)var17;
               float var19 = sqrtLut[var18];
               float var20 = sqrtLut[var18 + 1] - var19;
               var17 = var19 + (var17 - (float)var18) * var20;
               var16 = (int)(var17 * (float)this.fastGradientArraySize);
            }

            var1[var2 + var15] = this.gradient[var16];
            var13 += var14;
            var14 += var10;
            ++var15;
         }

         while(var15 < var6) {
            var1[var2 + var15] = var11;
            ++var15;
         }

         var2 += var3;
         var8 += this.a01;
         var9 += this.a11;
      }

   }

   private void cyclicCircularGradientFillRaster(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      double var8 = (double)(-this.radiusSq + this.centerX * this.centerX + this.centerY * this.centerY);
      float var24 = this.a00 * (float)var4 + this.a01 * (float)var5 + this.a02;
      float var25 = this.a10 * (float)var4 + this.a11 * (float)var5 + this.a12;
      float var26 = 2.0F * this.centerY;
      float var27 = -2.0F * this.centerX;
      int var34 = var2;
      int var35 = var6 + var3;
      int var36;
      if (this.trivial == 0.0F) {
         var36 = this.indexIntoGradientsArrays(0.0F);

         for(int var40 = 0; var40 < var7; ++var40) {
            for(int var41 = 0; var41 < var6; ++var41) {
               var1[var34 + var41] = var36;
            }

            var34 += var35;
         }

      } else {
         for(var36 = 0; var36 < var7; ++var36) {
            float var37 = this.a01 * (float)var36 + var24;
            float var38 = this.a11 * (float)var36 + var25;

            for(int var39 = 0; var39 < var6; ++var39) {
               double var20;
               double var22;
               if (var37 == this.focusX) {
                  var20 = (double)this.focusX;
                  var22 = (double)this.centerY;
                  var22 += var38 > this.focusY ? (double)this.trivial : (double)(-this.trivial);
               } else {
                  double var16 = (double)((var38 - this.focusY) / (var37 - this.focusX));
                  double var18 = (double)var38 - var16 * (double)var37;
                  double var10 = var16 * var16 + 1.0;
                  double var12 = (double)var27 + -2.0 * var16 * ((double)this.centerY - var18);
                  double var14 = var8 + var18 * (var18 - (double)var26);
                  float var29 = (float)Math.sqrt(var12 * var12 - 4.0 * var10 * var14);
                  var20 = -var12;
                  var20 += var37 < this.focusX ? (double)(-var29) : (double)var29;
                  var20 /= 2.0 * var10;
                  var22 = var16 * var20 + var18;
               }

               float var32 = var37 - this.focusX;
               var32 *= var32;
               float var33 = var38 - this.focusY;
               var33 *= var33;
               float var30 = var32 + var33;
               var32 = (float)var20 - this.focusX;
               var32 *= var32;
               var33 = (float)var22 - this.focusY;
               var33 *= var33;
               float var31 = var32 + var33;
               if (var31 == 0.0F) {
                  var31 = var22 >= (double)this.focusY ? this.trivial : -this.trivial;
               }

               float var28 = (float)Math.sqrt((double)(var30 / var31));
               var1[var34 + var39] = this.indexIntoGradientsArrays(var28);
               var37 += this.a00;
               var38 += this.a10;
            }

            var34 += var35;
         }

      }
   }

   static {
      for(int var0 = 0; var0 < sqrtLut.length; ++var0) {
         sqrtLut[var0] = (float)Math.sqrt((double)((float)var0 / 2048.0F));
      }

   }
}
