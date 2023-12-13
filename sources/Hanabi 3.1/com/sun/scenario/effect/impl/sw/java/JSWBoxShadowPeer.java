package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.BoxRenderState;

public class JSWBoxShadowPeer extends JSWEffectPeer {
   public JSWBoxShadowPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   public ImageData filter(Effect var1, BoxRenderState var2, BaseTransform var3, Rectangle var4, ImageData... var5) {
      this.setRenderState(var2);
      boolean var6 = this.getPass() == 0;
      int var7 = var6 ? var2.getBoxPixelSize(0) - 1 : 0;
      int var8 = var6 ? 0 : var2.getBoxPixelSize(1) - 1;
      if (var7 < 0) {
         var7 = 0;
      }

      if (var8 < 0) {
         var8 = 0;
      }

      int var9 = var2.getBlurPasses();
      float var10 = var2.getSpread();
      if (!var6 || var9 >= 1 && (var7 >= 1 || var8 >= 1)) {
         int var11 = var7 * var9 + 1 & -2;
         int var12 = var8 * var9 + 1 & -2;
         HeapImage var13 = (HeapImage)var5[0].getUntransformedImage();
         Rectangle var14 = var5[0].getUntransformedBounds();
         HeapImage var15 = var13;
         int var16 = var14.width;
         int var17 = var14.height;
         int var18 = var13.getScanlineStride();
         int[] var19 = var13.getPixelArray();
         int var20 = var16 + var11;
         int var21 = var17 + var12;

         int var26;
         for(boolean var22 = !var6; var22 || var16 < var20 || var17 < var21; var18 = var26) {
            int var23 = var16 + var7;
            int var24 = var17 + var8;
            if (var23 > var20) {
               var23 = var20;
            }

            if (var24 > var21) {
               var24 = var21;
            }

            HeapImage var25 = (HeapImage)this.getRenderer().getCompatibleImage(var23, var24);
            var26 = var25.getScanlineStride();
            int[] var27 = var25.getPixelArray();
            if (var9 == 0) {
               var10 = 0.0F;
            }

            if (var6) {
               this.filterHorizontalBlack(var27, var23, var24, var26, var19, var16, var17, var18, var10);
            } else if (var23 >= var20 && var24 >= var21) {
               float[] var28 = var2.getShadowColor().getPremultipliedRGBComponents();
               if (var28[3] == 1.0F && var28[0] == 0.0F && var28[1] == 0.0F && var28[2] == 0.0F) {
                  this.filterVerticalBlack(var27, var23, var24, var26, var19, var16, var17, var18, var10);
               } else {
                  this.filterVertical(var27, var23, var24, var26, var19, var16, var17, var18, var10, var28);
               }
            } else {
               this.filterVerticalBlack(var27, var23, var24, var26, var19, var16, var17, var18, var10);
            }

            if (var15 != var13) {
               this.getRenderer().releaseCompatibleImage(var15);
            }

            --var9;
            var22 = false;
            var15 = var25;
            var16 = var23;
            var17 = var24;
            var19 = var27;
         }

         Rectangle var29 = new Rectangle(var14.x - var11 / 2, var14.y - var12 / 2, var16, var17);
         return new ImageData(this.getFilterContext(), var15, var29);
      } else {
         var5[0].addref();
         return var5[0];
      }
   }

   protected void filterHorizontalBlack(int[] var1, int var2, int var3, int var4, int[] var5, int var6, int var7, int var8, float var9) {
      int var10 = var2 - var6 + 1;
      int var11 = var10 * 255;
      var11 = (int)((float)var11 + (float)(255 - var11) * var9);
      int var12 = Integer.MAX_VALUE / var11;
      int var13 = var11 / 255;
      int var14 = 0;
      int var15 = 0;

      for(int var16 = 0; var16 < var3; ++var16) {
         int var17 = 0;

         for(int var18 = 0; var18 < var2; ++var18) {
            int var19 = var18 >= var10 ? var5[var14 + var18 - var10] : 0;
            var17 -= var19 >>> 24;
            var19 = var18 < var6 ? var5[var14 + var18] : 0;
            var17 += var19 >>> 24;
            var1[var15 + var18] = var17 < var13 ? 0 : (var17 >= var11 ? -16777216 : var17 * var12 >> 23 << 24);
         }

         var14 += var8;
         var15 += var4;
      }

   }

   protected void filterVerticalBlack(int[] var1, int var2, int var3, int var4, int[] var5, int var6, int var7, int var8, float var9) {
      int var10 = var3 - var7 + 1;
      int var11 = var10 * 255;
      var11 = (int)((float)var11 + (float)(255 - var11) * var9);
      int var12 = Integer.MAX_VALUE / var11;
      int var13 = var11 / 255;
      int var14 = var10 * var8;

      for(int var15 = 0; var15 < var2; ++var15) {
         int var16 = 0;
         int var17 = var15;
         int var18 = var15;

         for(int var19 = 0; var19 < var3; ++var19) {
            int var20 = var17 >= var14 ? var5[var17 - var14] : 0;
            var16 -= var20 >>> 24;
            var20 = var19 < var7 ? var5[var17] : 0;
            var16 += var20 >>> 24;
            var1[var18] = var16 < var13 ? 0 : (var16 >= var11 ? -16777216 : var16 * var12 >> 23 << 24);
            var17 += var8;
            var18 += var4;
         }
      }

   }

   protected void filterVertical(int[] var1, int var2, int var3, int var4, int[] var5, int var6, int var7, int var8, float var9, float[] var10) {
      int var11 = var3 - var7 + 1;
      int var12 = var11 * 255;
      var12 = (int)((float)var12 + (float)(255 - var12) * var9);
      int var13 = Integer.MAX_VALUE / var12;
      int var14 = (int)((float)var13 * var10[0]);
      int var15 = (int)((float)var13 * var10[1]);
      int var16 = (int)((float)var13 * var10[2]);
      var13 = (int)((float)var13 * var10[3]);
      int var17 = var12 / 255;
      int var18 = var11 * var8;
      int var19 = (int)(var10[0] * 255.0F) << 16 | (int)(var10[1] * 255.0F) << 8 | (int)(var10[2] * 255.0F) | (int)(var10[3] * 255.0F) << 24;

      for(int var20 = 0; var20 < var2; ++var20) {
         int var21 = 0;
         int var22 = var20;
         int var23 = var20;

         for(int var24 = 0; var24 < var3; ++var24) {
            int var25 = var22 >= var18 ? var5[var22 - var18] : 0;
            var21 -= var25 >>> 24;
            var25 = var24 < var7 ? var5[var22] : 0;
            var21 += var25 >>> 24;
            var1[var23] = var21 < var17 ? 0 : (var21 >= var12 ? var19 : var21 * var13 >> 23 << 24 | var21 * var14 >> 23 << 16 | var21 * var15 >> 23 << 8 | var21 * var16 >> 23);
            var22 += var8;
            var23 += var4;
         }
      }

   }
}
