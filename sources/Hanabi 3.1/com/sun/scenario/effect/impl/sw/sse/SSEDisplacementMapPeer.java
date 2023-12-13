package com.sun.scenario.effect.impl.sw.sse;

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

public class SSEDisplacementMapPeer extends SSEEffectPeer {
   public SSEDisplacementMapPeer(FilterContext var1, Renderer var2, String var3) {
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
      FloatMap var7 = (FloatMap)this.getSamplerData(1);
      boolean var8 = false;
      boolean var9 = false;
      int var10 = var7.getWidth();
      int var11 = var7.getHeight();
      int var12 = var7.getWidth();
      float[] var13 = var7.getData();
      HeapImage var14 = (HeapImage)var5[0].getUntransformedImage();
      byte var15 = 0;
      byte var16 = 0;
      int var17 = var14.getPhysicalWidth();
      int var18 = var14.getPhysicalHeight();
      int var19 = var14.getScanlineStride();
      int[] var20 = var14.getPixelArray();
      Rectangle var21 = new Rectangle(var15, var16, var17, var18);
      Rectangle var22 = var5[0].getUntransformedBounds();
      BaseTransform var23 = var5[0].getTransform();
      this.setInputBounds(0, var22);
      this.setInputNativeBounds(0, var21);
      float[] var24 = new float[]{0.0F, 0.0F, 1.0F, 1.0F};
      float[] var25 = new float[4];
      this.getTextureCoordinates(0, var25, (float)var22.x, (float)var22.y, (float)var17, (float)var18, var6, var23);
      int var28 = var6.width;
      int var29 = var6.height;
      HeapImage var30 = (HeapImage)this.getRenderer().getCompatibleImage(var28, var29);
      this.setDestNativeBounds(var30.getPhysicalWidth(), var30.getPhysicalHeight());
      int var31 = var30.getScanlineStride();
      int[] var32 = var30.getPixelArray();
      float[] var33 = this.getImagetx();
      float[] var34 = this.getSampletx();
      float var35 = this.getWrap();
      filter(var32, 0, 0, var28, var29, var31, var33[0], var33[1], var33[2], var33[3], var13, var24[0], var24[1], var24[2], var24[3], var10, var11, var12, var20, var25[0], var25[1], var25[2], var25[3], var17, var18, var19, var34[0], var34[1], var34[2], var34[3], var35);
      return new ImageData(this.getFilterContext(), var30, var6);
   }

   private static native void filter(int[] var0, int var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, float var9, float[] var10, float var11, float var12, float var13, float var14, int var15, int var16, int var17, int[] var18, float var19, float var20, float var21, float var22, int var23, int var24, int var25, float var26, float var27, float var28, float var29, float var30);
}
