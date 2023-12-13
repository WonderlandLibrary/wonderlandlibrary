package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.BoxRenderState;

public class JSWBoxBlurPeer extends JSWEffectPeer {
   public JSWBoxBlurPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   public ImageData filter(Effect var1, BoxRenderState var2, BaseTransform var3, Rectangle var4, ImageData... var5) {
      this.setRenderState(var2);
      boolean var6 = this.getPass() == 0;
      int var7 = var6 ? var2.getBoxPixelSize(0) - 1 : 0;
      int var8 = var6 ? 0 : var2.getBoxPixelSize(1) - 1;
      int var9 = var2.getBlurPasses();
      if (var9 < 1 || var7 < 1 && var8 < 1) {
         var5[0].addref();
         return var5[0];
      } else {
         int var10 = var7 * var9 + 1 & -2;
         int var11 = var8 * var9 + 1 & -2;
         HeapImage var12 = (HeapImage)var5[0].getUntransformedImage();
         Rectangle var13 = var5[0].getUntransformedBounds();
         HeapImage var14 = var12;
         int var15 = var13.width;
         int var16 = var13.height;
         int var17 = var12.getScanlineStride();
         int[] var18 = var12.getPixelArray();
         int var19 = var15 + var10;

         int var24;
         for(int var20 = var16 + var11; var15 < var19 || var16 < var20; var17 = var24) {
            int var21 = var15 + var7;
            int var22 = var16 + var8;
            if (var21 > var19) {
               var21 = var19;
            }

            if (var22 > var20) {
               var22 = var20;
            }

            HeapImage var23 = (HeapImage)this.getRenderer().getCompatibleImage(var21, var22);
            var24 = var23.getScanlineStride();
            int[] var25 = var23.getPixelArray();
            if (var6) {
               this.filterHorizontal(var25, var21, var22, var24, var18, var15, var16, var17);
            } else {
               this.filterVertical(var25, var21, var22, var24, var18, var15, var16, var17);
            }

            if (var14 != var12) {
               this.getRenderer().releaseCompatibleImage(var14);
            }

            var14 = var23;
            var15 = var21;
            var16 = var22;
            var18 = var25;
         }

         Rectangle var26 = new Rectangle(var13.x - var10 / 2, var13.y - var11 / 2, var15, var16);
         return new ImageData(this.getFilterContext(), var14, var26);
      }
   }

   protected void filterHorizontal(int[] var1, int var2, int var3, int var4, int[] var5, int var6, int var7, int var8) {
      int var9 = var2 - var6 + 1;
      int var10 = Integer.MAX_VALUE / (var9 * 255);
      int var11 = 0;
      int var12 = 0;

      for(int var13 = 0; var13 < var3; ++var13) {
         int var14 = 0;
         int var15 = 0;
         int var16 = 0;
         int var17 = 0;

         for(int var18 = 0; var18 < var2; ++var18) {
            int var19 = var18 >= var9 ? var5[var11 + var18 - var9] : 0;
            var14 -= var19 >>> 24;
            var15 -= var19 >> 16 & 255;
            var16 -= var19 >> 8 & 255;
            var17 -= var19 & 255;
            var19 = var18 < var6 ? var5[var11 + var18] : 0;
            var14 += var19 >>> 24;
            var15 += var19 >> 16 & 255;
            var16 += var19 >> 8 & 255;
            var17 += var19 & 255;
            var1[var12 + var18] = (var14 * var10 >> 23 << 24) + (var15 * var10 >> 23 << 16) + (var16 * var10 >> 23 << 8) + (var17 * var10 >> 23);
         }

         var11 += var8;
         var12 += var4;
      }

   }

   protected void filterVertical(int[] var1, int var2, int var3, int var4, int[] var5, int var6, int var7, int var8) {
      int var9 = var3 - var7 + 1;
      int var10 = Integer.MAX_VALUE / (var9 * 255);
      int var11 = var9 * var8;

      for(int var12 = 0; var12 < var2; ++var12) {
         int var13 = 0;
         int var14 = 0;
         int var15 = 0;
         int var16 = 0;
         int var17 = var12;
         int var18 = var12;

         for(int var19 = 0; var19 < var3; ++var19) {
            int var20 = var17 >= var11 ? var5[var17 - var11] : 0;
            var13 -= var20 >>> 24;
            var14 -= var20 >> 16 & 255;
            var15 -= var20 >> 8 & 255;
            var16 -= var20 & 255;
            var20 = var19 < var7 ? var5[var17] : 0;
            var13 += var20 >>> 24;
            var14 += var20 >> 16 & 255;
            var15 += var20 >> 8 & 255;
            var16 += var20 & 255;
            var1[var18] = (var13 * var10 >> 23 << 24) + (var14 * var10 >> 23 << 16) + (var15 * var10 >> 23 << 8) + (var16 * var10 >> 23);
            var17 += var8;
            var18 += var4;
         }
      }

   }
}
