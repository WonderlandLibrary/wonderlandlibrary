package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.PerspectiveTransform;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.AccessHelper;
import com.sun.scenario.effect.impl.state.PerspectiveTransformState;
import com.sun.scenario.effect.impl.state.RenderState;

public class JSWPerspectiveTransformPeer extends JSWEffectPeer {
   public JSWPerspectiveTransformPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   protected final PerspectiveTransform getEffect() {
      return (PerspectiveTransform)super.getEffect();
   }

   private float[][] getITX() {
      PerspectiveTransformState var1 = (PerspectiveTransformState)AccessHelper.getState(this.getEffect());
      return var1.getITX();
   }

   private float[] getTx0() {
      Rectangle var1 = this.getInputBounds(0);
      Rectangle var2 = this.getInputNativeBounds(0);
      float var3 = (float)var1.width / (float)var2.width;
      float[] var4 = this.getITX()[0];
      return new float[]{var4[0] * var3, var4[1] * var3, var4[2] * var3};
   }

   private float[] getTx1() {
      Rectangle var1 = this.getInputBounds(0);
      Rectangle var2 = this.getInputNativeBounds(0);
      float var3 = (float)var1.height / (float)var2.height;
      float[] var4 = this.getITX()[1];
      return new float[]{var4[0] * var3, var4[1] * var3, var4[2] * var3};
   }

   private float[] getTx2() {
      return this.getITX()[2];
   }

   public int getTextureCoordinates(int var1, float[] var2, float var3, float var4, float var5, float var6, Rectangle var7, BaseTransform var8) {
      var2[0] = (float)var7.x;
      var2[1] = (float)var7.y;
      var2[2] = (float)(var7.x + var7.width);
      var2[3] = (float)(var7.y + var7.height);
      return 4;
   }

   public ImageData filter(Effect var1, RenderState var2, BaseTransform var3, Rectangle var4, ImageData... var5) {
      this.setEffect(var1);
      Rectangle var6 = this.getResultBounds(var3, var4, var5);
      this.setDestBounds(var6);
      HeapImage var7 = (HeapImage)var5[0].getUntransformedImage();
      byte var8 = 0;
      byte var9 = 0;
      int var10 = var7.getPhysicalWidth();
      int var11 = var7.getPhysicalHeight();
      int var12 = var7.getScanlineStride();
      int[] var13 = var7.getPixelArray();
      Rectangle var14 = new Rectangle(var8, var9, var10, var11);
      Rectangle var15 = var5[0].getUntransformedBounds();
      BaseTransform var16 = var5[0].getTransform();
      this.setInputBounds(0, var15);
      this.setInputNativeBounds(0, var14);
      float[] var17 = new float[4];
      float[] var18 = new float[4];
      this.getTextureCoordinates(0, var18, (float)var15.x, (float)var15.y, (float)var10, (float)var11, var6, var16);
      int var21 = var6.width;
      int var22 = var6.height;
      HeapImage var23 = (HeapImage)this.getRenderer().getCompatibleImage(var21, var22);
      this.setDestNativeBounds(var23.getPhysicalWidth(), var23.getPhysicalHeight());
      int var24 = var23.getScanlineStride();
      int[] var25 = var23.getPixelArray();
      float[] var31 = this.getTx1();
      float var32 = var31[0];
      float var33 = var31[1];
      float var34 = var31[2];
      float[] var35 = this.getTx0();
      float var36 = var35[0];
      float var37 = var35[1];
      float var38 = var35[2];
      float[] var39 = this.getTx2();
      float var40 = var39[0];
      float var41 = var39[1];
      float var42 = var39[2];
      float var43 = (var18[2] - var18[0]) / (float)var21;
      float var44 = (var18[3] - var18[1]) / (float)var22;
      float var45 = var18[1] + var44 * 0.5F;

      for(int var46 = 0; var46 < 0 + var22; ++var46) {
         float var47 = (float)var46;
         int var26 = var46 * var24;
         float var48 = var18[0] + var43 * 0.5F;

         for(int var49 = 0; var49 < 0 + var21; ++var49) {
            float var50 = (float)var49;
            float var53 = 1.0F;
            float var57 = var48 * var40 + var45 * var41 + var53 * var42;
            float var56 = var57;
            var57 = var48 * var36 + var45 * var37 + var53 * var38;
            float var54 = var57 / var56;
            var57 = var48 * var32 + var45 * var33 + var53 * var34;
            float var55 = var57 / var56;
            this.lsample(var13, var54, var55, var10, var11, var12, var17);
            float var58 = var17[0];
            float var59 = var17[1];
            float var60 = var17[2];
            float var61 = var17[3];
            float var27 = var58;
            float var28 = var59;
            float var29 = var60;
            float var30 = var61;
            if (var61 < 0.0F) {
               var30 = 0.0F;
            } else if (var61 > 1.0F) {
               var30 = 1.0F;
            }

            if (var58 < 0.0F) {
               var27 = 0.0F;
            } else if (var58 > var30) {
               var27 = var30;
            }

            if (var59 < 0.0F) {
               var28 = 0.0F;
            } else if (var59 > var30) {
               var28 = var30;
            }

            if (var60 < 0.0F) {
               var29 = 0.0F;
            } else if (var60 > var30) {
               var29 = var30;
            }

            var25[var26 + var49] = (int)(var27 * 255.0F) << 16 | (int)(var28 * 255.0F) << 8 | (int)(var29 * 255.0F) << 0 | (int)(var30 * 255.0F) << 24;
            var48 += var43;
         }

         var45 += var44;
      }

      return new ImageData(this.getFilterContext(), var23, var6);
   }
}
