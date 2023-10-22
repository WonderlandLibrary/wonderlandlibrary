package com.sun.scenario.effect.impl.sw.sse;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;
import java.nio.FloatBuffer;

public class SSELinearConvolvePeer extends SSEEffectPeer {
   public SSELinearConvolvePeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   public ImageData filter(Effect var1, LinearConvolveRenderState var2, BaseTransform var3, Rectangle var4, ImageData... var5) {
      this.setRenderState(var2);
      Rectangle var6 = var5[0].getTransformedBounds((Rectangle)null);
      Rectangle var7 = var2.getPassResultBounds(var6, (Rectangle)null);
      Rectangle var8 = var2.getPassResultBounds(var6, var4);
      this.setDestBounds(var8);
      int var9 = var8.width;
      int var10 = var8.height;
      HeapImage var11 = (HeapImage)var5[0].getUntransformedImage();
      int var12 = var11.getPhysicalWidth();
      int var13 = var11.getPhysicalHeight();
      int var14 = var11.getScanlineStride();
      int[] var15 = var11.getPixelArray();
      Rectangle var16 = var5[0].getUntransformedBounds();
      BaseTransform var17 = var5[0].getTransform();
      Rectangle var18 = new Rectangle(0, 0, var12, var13);
      this.setInputBounds(0, var16);
      this.setInputTransform(0, var17);
      this.setInputNativeBounds(0, var18);
      HeapImage var19 = (HeapImage)this.getRenderer().getCompatibleImage(var9, var10);
      this.setDestNativeBounds(var19.getPhysicalWidth(), var19.getPhysicalHeight());
      int var20 = var19.getScanlineStride();
      int[] var21 = var19.getPixelArray();
      int var22 = var2.getPassKernelSize();
      FloatBuffer var23 = var2.getPassWeights();
      LinearConvolveRenderState.PassType var24 = var2.getPassType();
      if (!var17.isIdentity() || !var8.contains(var7.x, var7.y)) {
         var24 = LinearConvolveRenderState.PassType.GENERAL_VECTOR;
      }

      float[] var25;
      if (var24 == LinearConvolveRenderState.PassType.HORIZONTAL_CENTERED) {
         var25 = new float[var22 * 2];
         var23.get(var25, 0, var22);
         var23.rewind();
         var23.get(var25, var22, var22);
         this.filterHV(var21, var9, var10, 1, var20, var15, var12, var13, 1, var14, var25);
      } else if (var24 == LinearConvolveRenderState.PassType.VERTICAL_CENTERED) {
         var25 = new float[var22 * 2];
         var23.get(var25, 0, var22);
         var23.rewind();
         var23.get(var25, var22, var22);
         this.filterHV(var21, var10, var9, var20, 1, var15, var13, var12, var14, 1, var25);
      } else {
         var25 = new float[var22];
         var23.get(var25, 0, var22);
         float[] var26 = new float[8];
         int var27 = this.getTextureCoordinates(0, var26, (float)var16.x, (float)var16.y, (float)var18.width, (float)var18.height, var8, var17);
         float var28 = var26[0] * (float)var12;
         float var29 = var26[1] * (float)var13;
         float var30;
         float var31;
         float var32;
         float var33;
         if (var27 < 8) {
            var30 = (var26[2] - var26[0]) * (float)var12 / (float)var8.width;
            var31 = 0.0F;
            var32 = 0.0F;
            var33 = (var26[3] - var26[1]) * (float)var13 / (float)var8.height;
         } else {
            var30 = (var26[4] - var26[0]) * (float)var12 / (float)var8.width;
            var31 = (var26[5] - var26[1]) * (float)var13 / (float)var8.height;
            var32 = (var26[6] - var26[0]) * (float)var12 / (float)var8.width;
            var33 = (var26[7] - var26[1]) * (float)var13 / (float)var8.height;
         }

         float[] var34 = var2.getPassVector();
         float var35 = var34[0] * (float)var12;
         float var36 = var34[1] * (float)var13;
         float var37 = var34[2] * (float)var12;
         float var38 = var34[3] * (float)var13;
         this.filterVector(var21, var9, var10, var20, var15, var12, var13, var14, var25, var22, var28, var29, var37, var38, var35, var36, var30, var31, var32, var33);
      }

      return new ImageData(this.getFilterContext(), var19, var8);
   }

   native void filterVector(int[] var1, int var2, int var3, int var4, int[] var5, int var6, int var7, int var8, float[] var9, int var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20);

   native void filterHV(int[] var1, int var2, int var3, int var4, int var5, int[] var6, int var7, int var8, int var9, int var10, float[] var11);
}
