package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.ColorAdjust;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

public class JSWColorAdjustPeer extends JSWEffectPeer {
   public JSWColorAdjustPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   protected final ColorAdjust getEffect() {
      return (ColorAdjust)super.getEffect();
   }

   private float getHue() {
      return this.getEffect().getHue() / 2.0F;
   }

   private float getSaturation() {
      return this.getEffect().getSaturation() + 1.0F;
   }

   private float getBrightness() {
      return this.getEffect().getBrightness() + 1.0F;
   }

   private float getContrast() {
      float var1 = this.getEffect().getContrast();
      if (var1 > 0.0F) {
         var1 *= 3.0F;
      }

      return var1 + 1.0F;
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
      float var30 = this.getSaturation();
      float var31 = this.getBrightness();
      float var32 = this.getContrast();
      float var33 = this.getHue();
      float var34 = (var17[2] - var17[0]) / (float)var20;
      float var35 = (var17[3] - var17[1]) / (float)var21;
      float var36 = var17[1] + var35 * 0.5F;

      for(int var37 = 0; var37 < 0 + var21; ++var37) {
         float var38 = (float)var37;
         int var25 = var37 * var23;
         float var39 = var17[0] + var34 * 0.5F;

         for(int var40 = 0; var40 < 0 + var20; ++var40) {
            float var41 = (float)var40;
            int var48;
            if (var39 >= 0.0F && var36 >= 0.0F) {
               int var49 = (int)(var39 * (float)var10);
               int var50 = (int)(var36 * (float)var11);
               boolean var51 = var49 >= var10 || var50 >= var11;
               var48 = var51 ? 0 : var13[var50 * var12 + var49];
            } else {
               var48 = 0;
            }

            float var42 = (float)(var48 >> 16 & 255) / 255.0F;
            float var43 = (float)(var48 >> 8 & 255) / 255.0F;
            float var44 = (float)(var48 & 255) / 255.0F;
            float var45 = (float)(var48 >>> 24) / 255.0F;
            float var46 = var42;
            float var47 = var43;
            float var76 = var44;
            if (var45 > 0.0F) {
               var46 = var42 / var45;
               var47 = var43 / var45;
               var76 = var44 / var45;
            }

            var46 = (var46 - 0.5F) * var32 + 0.5F;
            var47 = (var47 - 0.5F) * var32 + 0.5F;
            var76 = (var76 - 0.5F) * var32 + 0.5F;
            float var59 = var46 > var47 ? var46 : var47;
            var59 = var59 > var76 ? var59 : var76;
            float var61 = var46 < var47 ? var46 : var47;
            var61 = var61 < var76 ? var61 : var76;
            float var56;
            float var57;
            float var64;
            float var65;
            if (var59 > var61) {
               float var63 = (var59 - var46) / (var59 - var61);
               var64 = (var59 - var47) / (var59 - var61);
               var65 = (var59 - var76) / (var59 - var61);
               if (var46 == var59) {
                  var56 = var65 - var64;
               } else if (var47 == var59) {
                  var56 = 2.0F + var63 - var65;
               } else {
                  var56 = 4.0F + var64 - var63;
               }

               var56 /= 6.0F;
               if (var56 < 0.0F) {
                  ++var56;
               }

               var57 = (var59 - var61) / var59;
            } else {
               var56 = 0.0F;
               var57 = 0.0F;
            }

            float var53 = var56 + var33;
            if (var53 < 0.0F) {
               ++var53;
            } else if (var53 > 1.0F) {
               --var53;
            }

            float var54;
            if (var30 > 1.0F) {
               var56 = var30 - 1.0F;
               var54 = var57 + (1.0F - var57) * var56;
            } else {
               var54 = var57 * var30;
            }

            float var55;
            if (var31 > 1.0F) {
               var56 = var31 - 1.0F;
               var54 *= 1.0F - var56;
               var55 = var59 + (1.0F - var59) * var56;
            } else {
               var55 = var59 * var31;
            }

            float var60 = 0.0F;
            var61 = 1.0F;
            var56 = var54 < var60 ? var60 : (var54 > var61 ? var61 : var54);
            var57 = var55 < var60 ? var60 : (var55 > var61 ? var61 : var55);
            var64 = 0.0F;
            var65 = 0.0F;
            float var66 = 0.0F;
            float var70 = (float)Math.floor((double)var53);
            float var67 = (var53 - var70) * 6.0F;
            var70 = (float)Math.floor((double)var67);
            float var71 = var67 - var70;
            float var72 = var57 * (1.0F - var56);
            float var73 = var57 * (1.0F - var56 * var71);
            float var74 = var57 * (1.0F - var56 * (1.0F - var71));
            var70 = (float)Math.floor((double)var67);
            if (var70 < 1.0F) {
               var64 = var57;
               var65 = var74;
               var66 = var72;
            } else if (var70 < 2.0F) {
               var64 = var73;
               var65 = var57;
               var66 = var72;
            } else if (var70 < 3.0F) {
               var64 = var72;
               var65 = var57;
               var66 = var74;
            } else if (var70 < 4.0F) {
               var64 = var72;
               var65 = var73;
               var66 = var57;
            } else if (var70 < 5.0F) {
               var64 = var74;
               var65 = var72;
               var66 = var57;
            } else {
               var64 = var57;
               var65 = var72;
               var66 = var73;
            }

            float var26 = var45 * var64;
            float var27 = var45 * var65;
            float var28 = var45 * var66;
            float var29 = var45;
            if (var45 < 0.0F) {
               var29 = 0.0F;
            } else if (var45 > 1.0F) {
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

            var24[var25 + var40] = (int)(var26 * 255.0F) << 16 | (int)(var27 * 255.0F) << 8 | (int)(var28 * 255.0F) << 0 | (int)(var29 * 255.0F) << 24;
            var39 += var34;
         }

         var36 += var35;
      }

      var5[0].releaseTransformedImage(var7);
      return new ImageData(this.getFilterContext(), var22, var6);
   }
}
