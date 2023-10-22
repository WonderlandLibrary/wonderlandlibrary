package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;
import java.nio.FloatBuffer;

public class JSWLinearConvolvePeer extends JSWEffectPeer {
   private static final float cmin = 1.0F;
   private static final float cmax = 254.9375F;

   public JSWLinearConvolvePeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   private Rectangle getResultBounds(LinearConvolveRenderState var1, Rectangle var2, ImageData... var3) {
      Rectangle var4 = var3[0].getTransformedBounds((Rectangle)null);
      var4 = var1.getPassResultBounds(var4, var2);
      return var4;
   }

   public ImageData filter(Effect var1, LinearConvolveRenderState var2, BaseTransform var3, Rectangle var4, ImageData... var5) {
      this.setRenderState(var2);
      Rectangle var6 = this.getResultBounds(var2, (Rectangle)null, var5);
      Rectangle var7 = new Rectangle(var6);
      var7.intersectWith(var4);
      this.setDestBounds(var7);
      int var8 = var7.width;
      int var9 = var7.height;
      HeapImage var10 = (HeapImage)var5[0].getUntransformedImage();
      int var11 = var10.getPhysicalWidth();
      int var12 = var10.getPhysicalHeight();
      int var13 = var10.getScanlineStride();
      int[] var14 = var10.getPixelArray();
      Rectangle var15 = var5[0].getUntransformedBounds();
      BaseTransform var16 = var5[0].getTransform();
      Rectangle var17 = new Rectangle(0, 0, var11, var12);
      this.setInputBounds(0, var15);
      this.setInputTransform(0, var16);
      this.setInputNativeBounds(0, var17);
      HeapImage var18 = (HeapImage)this.getRenderer().getCompatibleImage(var8, var9);
      this.setDestNativeBounds(var18.getPhysicalWidth(), var18.getPhysicalHeight());
      int var19 = var18.getScanlineStride();
      int[] var20 = var18.getPixelArray();
      int var21 = var2.getPassKernelSize();
      FloatBuffer var22 = var2.getPassWeights();
      LinearConvolveRenderState.PassType var23 = var2.getPassType();
      if (!var16.isIdentity() || !var7.contains(var6.x, var6.y)) {
         var23 = LinearConvolveRenderState.PassType.GENERAL_VECTOR;
      }

      if (var21 >= 0) {
         var23 = LinearConvolveRenderState.PassType.GENERAL_VECTOR;
      }

      float[] var24;
      if (var23 == LinearConvolveRenderState.PassType.HORIZONTAL_CENTERED) {
         var24 = new float[var21 * 2];
         var22.get(var24, 0, var21);
         var22.rewind();
         var22.get(var24, var21, var21);
         this.filterHV(var20, var8, var9, 1, var19, var14, var11, var12, 1, var13, var24);
      } else if (var23 == LinearConvolveRenderState.PassType.VERTICAL_CENTERED) {
         var24 = new float[var21 * 2];
         var22.get(var24, 0, var21);
         var22.rewind();
         var22.get(var24, var21, var21);
         this.filterHV(var20, var9, var8, var19, 1, var14, var12, var11, var13, 1, var24);
      } else {
         var24 = new float[var21];
         var22.get(var24, 0, var21);
         float[] var25 = new float[8];
         int var26 = this.getTextureCoordinates(0, var25, (float)var15.x, (float)var15.y, (float)var17.width, (float)var17.height, var7, var16);
         float var27 = var25[0] * (float)var11;
         float var28 = var25[1] * (float)var12;
         float var29;
         float var30;
         float var31;
         float var32;
         if (var26 < 8) {
            var29 = (var25[2] - var25[0]) * (float)var11 / (float)var7.width;
            var30 = 0.0F;
            var31 = 0.0F;
            var32 = (var25[3] - var25[1]) * (float)var12 / (float)var7.height;
         } else {
            var29 = (var25[4] - var25[0]) * (float)var11 / (float)var7.width;
            var30 = (var25[5] - var25[1]) * (float)var12 / (float)var7.height;
            var31 = (var25[6] - var25[0]) * (float)var11 / (float)var7.width;
            var32 = (var25[7] - var25[1]) * (float)var12 / (float)var7.height;
         }

         float[] var33 = var2.getPassVector();
         float var34 = var33[0] * (float)var11;
         float var35 = var33[1] * (float)var12;
         float var36 = var33[2] * (float)var11;
         float var37 = var33[3] * (float)var12;
         this.filterVector(var20, var8, var9, var19, var14, var11, var12, var13, var24, var21, var27, var28, var36, var37, var34, var35, var29, var30, var31, var32);
      }

      return new ImageData(this.getFilterContext(), var18, var7);
   }

   protected void filterVector(int[] var1, int var2, int var3, int var4, int[] var5, int var6, int var7, int var8, float[] var9, int var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20) {
      int var21 = 0;
      float[] var22 = new float[4];
      var11 += (var19 + var17) * 0.5F;
      var12 += (var20 + var18) * 0.5F;

      for(int var23 = 0; var23 < var3; ++var23) {
         float var24 = var11;
         float var25 = var12;

         for(int var26 = 0; var26 < var2; ++var26) {
            var22[0] = var22[1] = var22[2] = var22[3] = 0.0F;
            float var27 = var24 + var13;
            float var28 = var25 + var14;

            for(int var29 = 0; var29 < var10; ++var29) {
               this.laccumsample(var5, var27, var28, var6, var7, var8, var9[var29], var22);
               var27 += var15;
               var28 += var16;
            }

            var1[var21 + var26] = ((var22[3] < 1.0F ? 0 : (var22[3] > 254.9375F ? 255 : (int)var22[3])) << 24) + ((var22[0] < 1.0F ? 0 : (var22[0] > 254.9375F ? 255 : (int)var22[0])) << 16) + ((var22[1] < 1.0F ? 0 : (var22[1] > 254.9375F ? 255 : (int)var22[1])) << 8) + (var22[2] < 1.0F ? 0 : (var22[2] > 254.9375F ? 255 : (int)var22[2]));
            var24 += var17;
            var25 += var18;
         }

         var11 += var19;
         var12 += var20;
         var21 += var4;
      }

   }

   protected void filterHV(int[] var1, int var2, int var3, int var4, int var5, int[] var6, int var7, int var8, int var9, int var10, float[] var11) {
      int var12 = var11.length / 2;
      float[] var13 = new float[var12 * 4];
      int var14 = 0;
      int var15 = 0;

      for(int var16 = 0; var16 < var3; ++var16) {
         int var17 = var14;
         int var18 = var15;

         int var19;
         for(var19 = 0; var19 < var13.length; ++var19) {
            var13[var19] = 0.0F;
         }

         var19 = var12;

         for(int var20 = 0; var20 < var2; ++var20) {
            int var21 = (var12 - var19) * 4;
            int var22 = var20 < var7 ? var6[var18] : 0;
            var13[var21 + 0] = (float)(var22 >>> 24);
            var13[var21 + 1] = (float)(var22 >> 16 & 255);
            var13[var21 + 2] = (float)(var22 >> 8 & 255);
            var13[var21 + 3] = (float)(var22 & 255);
            --var19;
            if (var19 <= 0) {
               var19 += var12;
            }

            float var23 = 0.0F;
            float var24 = 0.0F;
            float var25 = 0.0F;
            float var26 = 0.0F;

            for(var21 = 0; var21 < var13.length; var21 += 4) {
               float var27 = var11[var19 + (var21 >> 2)];
               var23 += var13[var21 + 0] * var27;
               var24 += var13[var21 + 1] * var27;
               var25 += var13[var21 + 2] * var27;
               var26 += var13[var21 + 3] * var27;
            }

            var1[var17] = ((var23 < 1.0F ? 0 : (var23 > 254.9375F ? 255 : (int)var23)) << 24) + ((var24 < 1.0F ? 0 : (var24 > 254.9375F ? 255 : (int)var24)) << 16) + ((var25 < 1.0F ? 0 : (var25 > 254.9375F ? 255 : (int)var25)) << 8) + (var26 < 1.0F ? 0 : (var26 > 254.9375F ? 255 : (int)var26));
            var17 += var4;
            var18 += var9;
         }

         var14 += var5;
         var15 += var10;
      }

   }
}
