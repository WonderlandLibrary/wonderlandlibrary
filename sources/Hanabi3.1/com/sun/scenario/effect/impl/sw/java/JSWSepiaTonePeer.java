package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.SepiaTone;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

public class JSWSepiaTonePeer extends JSWEffectPeer {
   public JSWSepiaTonePeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   protected final SepiaTone getEffect() {
      return (SepiaTone)super.getEffect();
   }

   private float getLevel() {
      return this.getEffect().getLevel();
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
      float var30 = this.getLevel();
      float var31 = (var17[2] - var17[0]) / (float)var20;
      float var32 = (var17[3] - var17[1]) / (float)var21;
      float var33 = var17[1] + var32 * 0.5F;

      for(int var34 = 0; var34 < 0 + var21; ++var34) {
         float var35 = (float)var34;
         int var25 = var34 * var23;
         float var36 = var17[0] + var31 * 0.5F;

         for(int var37 = 0; var37 < 0 + var20; ++var37) {
            float var38 = (float)var37;
            float var39 = 0.3F;
            float var40 = 0.59F;
            float var41 = 0.11F;
            float var42 = 1.6F;
            float var43 = 1.2F;
            float var44 = 0.9F;
            int var51;
            if (var36 >= 0.0F && var33 >= 0.0F) {
               int var52 = (int)(var36 * (float)var10);
               int var53 = (int)(var33 * (float)var11);
               boolean var54 = var52 >= var10 || var53 >= var11;
               var51 = var54 ? 0 : var13[var53 * var12 + var52];
            } else {
               var51 = 0;
            }

            float var45 = (float)(var51 >> 16 & 255) / 255.0F;
            float var46 = (float)(var51 >> 8 & 255) / 255.0F;
            float var47 = (float)(var51 & 255) / 255.0F;
            float var48 = (float)(var51 >>> 24) / 255.0F;
            float var71 = var45 * var39 + var46 * var40 + var47 * var41;
            float var58 = var71 * var42;
            float var59 = var71 * var43;
            float var60 = var71 * var44;
            float var70 = 1.0F - var30;
            float var61 = var58 * (1.0F - var70) + var45 * var70;
            float var62 = var59 * (1.0F - var70) + var46 * var70;
            float var63 = var60 * (1.0F - var70) + var47 * var70;
            float var26 = var61;
            float var27 = var62;
            float var28 = var63;
            float var29 = var48;
            if (var48 < 0.0F) {
               var29 = 0.0F;
            } else if (var48 > 1.0F) {
               var29 = 1.0F;
            }

            if (var61 < 0.0F) {
               var26 = 0.0F;
            } else if (var61 > var29) {
               var26 = var29;
            }

            if (var62 < 0.0F) {
               var27 = 0.0F;
            } else if (var62 > var29) {
               var27 = var29;
            }

            if (var63 < 0.0F) {
               var28 = 0.0F;
            } else if (var63 > var29) {
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
