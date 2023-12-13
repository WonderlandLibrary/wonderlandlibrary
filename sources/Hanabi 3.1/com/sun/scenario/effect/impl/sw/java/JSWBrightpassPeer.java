package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Brightpass;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

public class JSWBrightpassPeer extends JSWEffectPeer {
   public JSWBrightpassPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   protected final Brightpass getEffect() {
      return (Brightpass)super.getEffect();
   }

   private float getThreshold() {
      return this.getEffect().getThreshold();
   }

   public ImageData filter(Effect var1, RenderState var2, BaseTransform var3, Rectangle var4, ImageData... var5) {
      this.setEffect(var1);
      Rectangle var6 = this.getResultBounds(var3, var4, var5);
      this.setDestBounds(var6);
      HeapImage var7 = (HeapImage)var5[0].getTransformedImage(var6);
      byte var8 = 0;
      byte var9 = 0;
      int var10 = var7.getPhysicalWidth();
      int var11 = var7.getPhysicalHeight();
      int var12 = var7.getScanlineStride();
      int[] var13 = var7.getPixelArray();
      Rectangle var14 = new Rectangle(var8, var9, var10, var11);
      Rectangle var15 = var5[0].getTransformedBounds(var6);
      BaseTransform var16 = BaseTransform.IDENTITY_TRANSFORM;
      this.setInputBounds(0, var15);
      this.setInputNativeBounds(0, var14);
      float[] var17 = new float[4];
      this.getTextureCoordinates(0, var17, (float)var15.x, (float)var15.y, (float)var10, (float)var11, var6, var16);
      int var20 = var6.width;
      int var21 = var6.height;
      HeapImage var22 = (HeapImage)this.getRenderer().getCompatibleImage(var20, var21);
      this.setDestNativeBounds(var22.getPhysicalWidth(), var22.getPhysicalHeight());
      int var23 = var22.getScanlineStride();
      int[] var24 = var22.getPixelArray();
      float var30 = this.getThreshold();
      float var31 = (var17[2] - var17[0]) / (float)var20;
      float var32 = (var17[3] - var17[1]) / (float)var21;
      float var33 = var17[1] + var32 * 0.5F;

      for(int var34 = 0; var34 < 0 + var21; ++var34) {
         float var35 = (float)var34;
         int var25 = var34 * var23;
         float var36 = var17[0] + var31 * 0.5F;

         for(int var37 = 0; var37 < 0 + var20; ++var37) {
            float var38 = (float)var37;
            float var39 = 0.2125F;
            float var40 = 0.7154F;
            float var41 = 0.0721F;
            int var48;
            if (var36 >= 0.0F && var33 >= 0.0F) {
               int var49 = (int)(var36 * (float)var10);
               int var50 = (int)(var33 * (float)var11);
               boolean var51 = var49 >= var10 || var50 >= var11;
               var48 = var51 ? 0 : var13[var50 * var12 + var49];
            } else {
               var48 = 0;
            }

            float var42 = (float)(var48 >> 16 & 255) / 255.0F;
            float var43 = (float)(var48 >> 8 & 255) / 255.0F;
            float var44 = (float)(var48 & 255) / 255.0F;
            float var45 = (float)(var48 >>> 24) / 255.0F;
            float var57 = var39 * var42 + var40 * var43 + var41 * var44;
            float var53 = 0.0F;
            float var54 = var57 - var45 * var30;
            float var52 = var53 > var54 ? var53 : var54;
            var53 = Math.signum(var52);
            float var26 = var42 * var53;
            float var27 = var43 * var53;
            float var28 = var44 * var53;
            float var29 = var45 * var53;
            if (var29 < 0.0F) {
               var29 = 0.0F;
            } else if (var29 > 1.0F) {
               var29 = 1.0F;
            }

            if (var26 < 0.0F) {
               var26 = 0.0F;
            } else if (var26 > var29) {
               var26 = var29;
            }

            if (var27 < 0.0F) {
               var27 = 0.0F;
            } else if (var27 > var29) {
               var27 = var29;
            }

            if (var28 < 0.0F) {
               var28 = 0.0F;
            } else if (var28 > var29) {
               var28 = var29;
            }

            var24[var25 + var37] = (int)(var26 * 255.0F) << 16 | (int)(var27 * 255.0F) << 8 | (int)(var28 * 255.0F) << 0 | (int)(var29 * 255.0F) << 24;
            var36 += var31;
         }

         var33 += var32;
      }

      var5[0].releaseTransformedImage(var7);
      return new ImageData(this.getFilterContext(), var22, var6);
   }
}
