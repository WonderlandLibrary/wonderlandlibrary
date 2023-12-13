package com.sun.scenario.effect.impl.sw.sse;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.BoxRenderState;

public class SSEBoxBlurPeer extends SSEEffectPeer {
   public SSEBoxBlurPeer(FilterContext var1, Renderer var2, String var3) {
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
               filterHorizontal(var25, var21, var22, var24, var18, var15, var16, var17);
            } else {
               filterVertical(var25, var21, var22, var24, var18, var15, var16, var17);
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

   private static native void filterHorizontal(int[] var0, int var1, int var2, int var3, int[] var4, int var5, int var6, int var7);

   private static native void filterVertical(int[] var0, int var1, int var2, int var3, int[] var4, int var5, int var6, int var7);
}
