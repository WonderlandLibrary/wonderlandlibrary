package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.DisplacementMap;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.FloatMap;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

public class JSWDisplacementMapPeer extends JSWEffectPeer {
   public JSWDisplacementMapPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   protected final DisplacementMap getEffect() {
      return (DisplacementMap)super.getEffect();
   }

   private float[] getSampletx() {
      return new float[]{this.getEffect().getOffsetX(), this.getEffect().getOffsetY(), this.getEffect().getScaleX(), this.getEffect().getScaleY()};
   }

   private float[] getImagetx() {
      float var1 = this.getEffect().getWrap() ? 0.5F : 0.0F;
      return new float[]{var1 / (float)this.getInputNativeBounds(0).width, var1 / (float)this.getInputNativeBounds(0).height, ((float)this.getInputBounds(0).width - 2.0F * var1) / (float)this.getInputNativeBounds(0).width, ((float)this.getInputBounds(0).height - 2.0F * var1) / (float)this.getInputNativeBounds(0).height};
   }

   private float getWrap() {
      return this.getEffect().getWrap() ? 1.0F : 0.0F;
   }

   protected Object getSamplerData(int var1) {
      return this.getEffect().getMapData();
   }

   public int getTextureCoordinates(int var1, float[] var2, float var3, float var4, float var5, float var6, Rectangle var7, BaseTransform var8) {
      var2[0] = var2[1] = 0.0F;
      var2[2] = var2[3] = 1.0F;
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
      FloatMap var18 = (FloatMap)this.getSamplerData(1);
      boolean var19 = false;
      boolean var20 = false;
      int var21 = var18.getWidth();
      int var22 = var18.getHeight();
      int var23 = var18.getWidth();
      float[] var24 = var18.getData();
      float[] var25 = new float[4];
      float[] var26 = new float[4];
      this.getTextureCoordinates(0, var26, (float)var15.x, (float)var15.y, (float)var10, (float)var11, var6, var16);
      float[] var27 = new float[]{0.0F, 0.0F, 1.0F, 1.0F};
      int var30 = var6.width;
      int var31 = var6.height;
      HeapImage var32 = (HeapImage)this.getRenderer().getCompatibleImage(var30, var31);
      this.setDestNativeBounds(var32.getPhysicalWidth(), var32.getPhysicalHeight());
      int var33 = var32.getScanlineStride();
      int[] var34 = var32.getPixelArray();
      float[] var40 = this.getImagetx();
      float var41 = var40[0];
      float var42 = var40[1];
      float var43 = var40[2];
      float var44 = var40[3];
      float var45 = this.getWrap();
      float[] var46 = this.getSampletx();
      float var47 = var46[0];
      float var48 = var46[1];
      float var49 = var46[2];
      float var50 = var46[3];
      float var51 = (var26[2] - var26[0]) / (float)var30;
      float var52 = (var26[3] - var26[1]) / (float)var31;
      float var53 = (var27[2] - var27[0]) / (float)var30;
      float var54 = (var27[3] - var27[1]) / (float)var31;
      float var55 = var26[1] + var52 * 0.5F;
      float var56 = var27[1] + var54 * 0.5F;

      for(int var57 = 0; var57 < 0 + var31; ++var57) {
         float var58 = (float)var57;
         int var35 = var57 * var33;
         float var59 = var26[0] + var51 * 0.5F;
         float var60 = var27[0] + var53 * 0.5F;

         for(int var61 = 0; var61 < 0 + var30; ++var61) {
            float var62 = (float)var61;
            this.fsample(var24, var60, var56, var21, var22, var23, var25);
            float var63 = var25[0];
            float var64 = var25[1];
            float var65 = var25[2];
            float var66 = var25[3];
            float var71 = var59 + var49 * (var63 + var47);
            float var72 = var55 + var50 * (var64 + var48);
            float var73 = (float)Math.floor((double)var71);
            float var74 = (float)Math.floor((double)var72);
            var71 -= var45 * var73;
            var72 -= var45 * var74;
            var71 = var41 + var71 * var43;
            var72 = var42 + var72 * var44;
            this.lsample(var13, var71, var72, var10, var11, var12, var17);
            var63 = var17[0];
            var64 = var17[1];
            var65 = var17[2];
            var66 = var17[3];
            float var36 = var63;
            float var37 = var64;
            float var38 = var65;
            float var39 = var66;
            if (var66 < 0.0F) {
               var39 = 0.0F;
            } else if (var66 > 1.0F) {
               var39 = 1.0F;
            }

            if (var63 < 0.0F) {
               var36 = 0.0F;
            } else if (var63 > var39) {
               var36 = var39;
            }

            if (var64 < 0.0F) {
               var37 = 0.0F;
            } else if (var64 > var39) {
               var37 = var39;
            }

            if (var65 < 0.0F) {
               var38 = 0.0F;
            } else if (var65 > var39) {
               var38 = var39;
            }

            var34[var35 + var61] = (int)(var36 * 255.0F) << 16 | (int)(var37 * 255.0F) << 8 | (int)(var38 * 255.0F) << 0 | (int)(var39 * 255.0F) << 24;
            var59 += var51;
            var60 += var53;
         }

         var55 += var52;
         var56 += var54;
      }

      return new ImageData(this.getFilterContext(), var32, var6);
   }
}
