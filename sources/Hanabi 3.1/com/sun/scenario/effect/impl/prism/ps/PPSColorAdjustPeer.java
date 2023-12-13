package com.sun.scenario.effect.impl.prism.ps;

import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.ColorAdjust;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import java.util.HashMap;

public class PPSColorAdjustPeer extends PPSOneSamplerPeer {
   public PPSColorAdjustPeer(FilterContext var1, Renderer var2, String var3) {
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

   protected boolean isSamplerLinear(int var1) {
      switch (var1) {
         default:
            return false;
      }
   }

   protected Shader createShader() {
      HashMap var1 = new HashMap();
      var1.put("baseImg", 0);
      HashMap var2 = new HashMap();
      var2.put("saturation", 1);
      var2.put("brightness", 2);
      var2.put("contrast", 3);
      var2.put("hue", 0);
      return this.getRenderer().createShader(this.getShaderName(), var1, var2, false);
   }

   protected void updateShader(Shader var1) {
      var1.setConstant("saturation", this.getSaturation());
      var1.setConstant("brightness", this.getBrightness());
      var1.setConstant("contrast", this.getContrast());
      var1.setConstant("hue", this.getHue());
   }
}
