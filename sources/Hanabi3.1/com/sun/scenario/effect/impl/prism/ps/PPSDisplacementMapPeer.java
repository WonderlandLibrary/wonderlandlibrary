package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.DisplacementMap;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import java.util.HashMap;

public class PPSDisplacementMapPeer extends PPSTwoSamplerPeer {
   public PPSDisplacementMapPeer(FilterContext var1, Renderer var2, String var3) {
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

   protected boolean isSamplerLinear(int var1) {
      switch (var1) {
         case 0:
            return true;
         case 1:
            return true;
         default:
            return false;
      }
   }

   protected Shader createShader() {
      HashMap var1 = new HashMap();
      var1.put("origImg", 0);
      var1.put("mapImg", 1);
      HashMap var2 = new HashMap();
      var2.put("imagetx", 1);
      var2.put("wrap", 2);
      var2.put("sampletx", 0);
      return this.getRenderer().createShader(this.getShaderName(), var1, var2, false);
   }

   protected void updateShader(Shader var1) {
      float[] var2 = this.getImagetx();
      var1.setConstant("imagetx", var2[0], var2[1], var2[2], var2[3]);
      var1.setConstant("wrap", this.getWrap());
      float[] var3 = this.getSampletx();
      var1.setConstant("sampletx", var3[0], var3[1], var3[2], var3[3]);
   }
}
