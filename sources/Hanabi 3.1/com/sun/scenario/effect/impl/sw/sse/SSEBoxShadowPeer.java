package com.sun.scenario.effect.impl.sw.sse;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.BoxRenderState;

public class SSEBoxShadowPeer extends SSEEffectPeer {
   public SSEBoxShadowPeer(FilterContext var1, Renderer var2, String var3) {
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
               filterHorizontalBlack(var27, var23, var24, var26, var19, var16, var17, var18, var10);
            } else if (var23 >= var20 && var24 >= var21) {
               float[] var28 = var2.getShadowColor().getPremultipliedRGBComponents();
               if (var28[3] == 1.0F && var28[0] == 0.0F && var28[1] == 0.0F && var28[2] == 0.0F) {
                  filterVerticalBlack(var27, var23, var24, var26, var19, var16, var17, var18, var10);
               } else {
                  filterVertical(var27, var23, var24, var26, var19, var16, var17, var18, var10, var28);
               }
            } else {
               filterVerticalBlack(var27, var23, var24, var26, var19, var16, var17, var18, var10);
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
         return new ImageData(this.getFilterContext(), var15, var29, var5[0].getTransform());
      } else {
         var5[0].addref();
         return var5[0];
      }
   }

   private static native void filterHorizontalBlack(int[] var0, int var1, int var2, int var3, int[] var4, int var5, int var6, int var7, float var8);

   private static native void filterVerticalBlack(int[] var0, int var1, int var2, int var3, int[] var4, int var5, int var6, int var7, float var8);

   private static native void filterVertical(int[] var0, int var1, int var2, int var3, int[] var4, int var5, int var6, int var7, float var8, float[] var9);
}
