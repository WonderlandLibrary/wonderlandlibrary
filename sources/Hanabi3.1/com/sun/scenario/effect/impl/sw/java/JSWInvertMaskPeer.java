package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.InvertMask;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

public class JSWInvertMaskPeer extends JSWEffectPeer {
   public JSWInvertMaskPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   protected final InvertMask getEffect() {
      return (InvertMask)super.getEffect();
   }

   private float[] getOffset() {
      float var1 = (float)this.getEffect().getOffsetX();
      float var2 = (float)this.getEffect().getOffsetY();
      float[] var3 = new float[]{var1, var2};

      try {
         this.getInputTransform(0).inverseDeltaTransform(var3, 0, var3, 0, 1);
      } catch (Exception var5) {
      }

      var3[0] /= (float)this.getInputNativeBounds(0).width;
      var3[1] /= (float)this.getInputNativeBounds(0).height;
      return var3;
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
      float[] var30 = this.getOffset();
      float var31 = var30[0];
      float var32 = var30[1];
      float var33 = (var17[2] - var17[0]) / (float)var20;
      float var34 = (var17[3] - var17[1]) / (float)var21;
      float var35 = var17[1] + var34 * 0.5F;

      for(int var36 = 0; var36 < 0 + var21; ++var36) {
         float var37 = (float)var36;
         int var25 = var36 * var23;
         float var38 = var17[0] + var33 * 0.5F;

         for(int var39 = 0; var39 < 0 + var20; ++var39) {
            float var40 = (float)var39;
            float var42 = var38 - var31;
            float var43 = var35 - var32;
            int var44;
            if (var42 >= 0.0F && var43 >= 0.0F) {
               int var45 = (int)(var42 * (float)var10);
               int var46 = (int)(var43 * (float)var11);
               boolean var47 = var45 >= var10 || var46 >= var11;
               var44 = var47 ? 0 : var13[var46 * var12 + var45];
            } else {
               var44 = 0;
            }

            float var41 = (float)(var44 >>> 24) / 255.0F;
            var43 = 1.0F - var41;
            float var26 = var43;
            float var27 = var43;
            float var28 = var43;
            float var29 = var43;
            if (var43 < 0.0F) {
               var29 = 0.0F;
            } else if (var43 > 1.0F) {
               var29 = 1.0F;
            }

            if (var43 < 0.0F) {
               var26 = 0.0F;
            } else if (var43 > var29) {
               var26 = var29;
            }

            if (var43 < 0.0F) {
               var27 = 0.0F;
            } else if (var43 > var29) {
               var27 = var29;
            }

            if (var43 < 0.0F) {
               var28 = 0.0F;
            } else if (var43 > var29) {
               var28 = var29;
            }

            var24[var25 + var39] = (int)(var26 * 255.0F) << 16 | (int)(var27 * 255.0F) << 8 | (int)(var28 * 255.0F) << 0 | (int)(var29 * 255.0F) << 24;
            var38 += var33;
         }

         var35 += var34;
      }

      var5[0].releaseTransformedImage(var7);
      return new ImageData(this.getFilterContext(), var22, var6);
   }
}
