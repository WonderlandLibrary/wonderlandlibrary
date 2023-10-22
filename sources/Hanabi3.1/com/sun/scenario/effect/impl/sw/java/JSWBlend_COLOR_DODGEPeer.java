package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

public class JSWBlend_COLOR_DODGEPeer extends JSWEffectPeer {
   public JSWBlend_COLOR_DODGEPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   protected final Blend getEffect() {
      return (Blend)super.getEffect();
   }

   private float getOpacity() {
      return this.getEffect().getOpacity();
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
      HeapImage var17 = (HeapImage)var5[1].getTransformedImage(var6);
      byte var18 = 0;
      byte var19 = 0;
      int var20 = var17.getPhysicalWidth();
      int var21 = var17.getPhysicalHeight();
      int var22 = var17.getScanlineStride();
      int[] var23 = var17.getPixelArray();
      Rectangle var24 = new Rectangle(var18, var19, var20, var21);
      Rectangle var25 = var5[1].getTransformedBounds(var6);
      BaseTransform var26 = BaseTransform.IDENTITY_TRANSFORM;
      this.setInputBounds(1, var25);
      this.setInputNativeBounds(1, var24);
      float[] var27 = new float[4];
      this.getTextureCoordinates(0, var27, (float)var15.x, (float)var15.y, (float)var10, (float)var11, var6, var16);
      float[] var28 = new float[4];
      this.getTextureCoordinates(1, var28, (float)var25.x, (float)var25.y, (float)var20, (float)var21, var6, var26);
      int var31 = var6.width;
      int var32 = var6.height;
      HeapImage var33 = (HeapImage)this.getRenderer().getCompatibleImage(var31, var32);
      this.setDestNativeBounds(var33.getPhysicalWidth(), var33.getPhysicalHeight());
      int var34 = var33.getScanlineStride();
      int[] var35 = var33.getPixelArray();
      float var41 = this.getOpacity();
      float var42 = (var27[2] - var27[0]) / (float)var31;
      float var43 = (var27[3] - var27[1]) / (float)var32;
      float var44 = (var28[2] - var28[0]) / (float)var31;
      float var45 = (var28[3] - var28[1]) / (float)var32;
      float var46 = var27[1] + var43 * 0.5F;
      float var47 = var28[1] + var45 * 0.5F;

      for(int var48 = 0; var48 < 0 + var32; ++var48) {
         float var49 = (float)var48;
         int var36 = var48 * var34;
         float var50 = var27[0] + var42 * 0.5F;
         float var51 = var28[0] + var44 * 0.5F;

         for(int var52 = 0; var52 < 0 + var31; ++var52) {
            float var53 = (float)var52;
            int var60;
            if (var50 >= 0.0F && var46 >= 0.0F) {
               int var61 = (int)(var50 * (float)var10);
               int var62 = (int)(var46 * (float)var11);
               boolean var63 = var61 >= var10 || var62 >= var11;
               var60 = var63 ? 0 : var13[var62 * var12 + var61];
            } else {
               var60 = 0;
            }

            float var54 = (float)(var60 >> 16 & 255) / 255.0F;
            float var55 = (float)(var60 >> 8 & 255) / 255.0F;
            float var56 = (float)(var60 & 255) / 255.0F;
            float var57 = (float)(var60 >>> 24) / 255.0F;
            float var58 = var54;
            float var59 = var55;
            float var86 = var56;
            float var87 = var57;
            int var64;
            if (var51 >= 0.0F && var47 >= 0.0F) {
               int var65 = (int)(var51 * (float)var20);
               int var66 = (int)(var47 * (float)var21);
               boolean var67 = var65 >= var20 || var66 >= var21;
               var64 = var67 ? 0 : var23[var66 * var22 + var65];
            } else {
               var64 = 0;
            }

            var54 = (float)(var64 >> 16 & 255) / 255.0F;
            var55 = (float)(var64 >> 8 & 255) / 255.0F;
            var56 = (float)(var64 & 255) / 255.0F;
            var57 = (float)(var64 >>> 24) / 255.0F;
            float var88 = var54 * var41;
            float var89 = var55 * var41;
            float var90 = var56 * var41;
            float var91 = var57 * var41;
            float var81 = var87 + var91 - var87 * var91;
            float var78 = (1.0F - var91) * var58 + (1.0F - var87) * var88;
            float var79 = (1.0F - var91) * var59 + (1.0F - var87) * var89;
            float var80 = (1.0F - var91) * var86 + (1.0F - var87) * var90;
            float var82 = var87 * var91;
            float var83;
            if (var58 == 0.0F) {
               var83 = 0.0F;
            } else if (var91 == var88) {
               var83 = var82;
            } else {
               var83 = var91 * var91 * var58 / (var91 - var88);
               if (var83 > var82) {
                  var83 = var82;
               }
            }

            float var84;
            if (var59 == 0.0F) {
               var84 = 0.0F;
            } else if (var91 == var89) {
               var84 = var82;
            } else {
               var84 = var91 * var91 * var59 / (var91 - var89);
               if (var84 > var82) {
                  var84 = var82;
               }
            }

            float var85;
            if (var86 == 0.0F) {
               var85 = 0.0F;
            } else if (var91 == var90) {
               var85 = var82;
            } else {
               var85 = var91 * var91 * var86 / (var91 - var90);
               if (var85 > var82) {
                  var85 = var82;
               }
            }

            var78 += var83;
            var79 += var84;
            var80 += var85;
            float var37 = var78;
            float var38 = var79;
            float var39 = var80;
            float var40 = var81;
            if (var81 < 0.0F) {
               var40 = 0.0F;
            } else if (var81 > 1.0F) {
               var40 = 1.0F;
            }

            if (var78 < 0.0F) {
               var37 = 0.0F;
            } else if (var78 > var40) {
               var37 = var40;
            }

            if (var79 < 0.0F) {
               var38 = 0.0F;
            } else if (var79 > var40) {
               var38 = var40;
            }

            if (var80 < 0.0F) {
               var39 = 0.0F;
            } else if (var80 > var40) {
               var39 = var40;
            }

            var35[var36 + var52] = (int)(var37 * 255.0F) << 16 | (int)(var38 * 255.0F) << 8 | (int)(var39 * 255.0F) << 0 | (int)(var40 * 255.0F) << 24;
            var50 += var42;
            var51 += var44;
         }

         var46 += var43;
         var47 += var45;
      }

      var5[0].releaseTransformedImage(var7);
      var5[1].releaseTransformedImage(var17);
      return new ImageData(this.getFilterContext(), var33, var6);
   }
}
