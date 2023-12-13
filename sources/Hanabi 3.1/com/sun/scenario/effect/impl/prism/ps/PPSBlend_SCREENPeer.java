package com.sun.scenario.effect.impl.prism.ps;

import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import java.util.HashMap;

public class PPSBlend_SCREENPeer extends PPSTwoSamplerPeer {
   public PPSBlend_SCREENPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   protected final Blend getEffect() {
      return (Blend)super.getEffect();
   }

   private float getOpacity() {
      return this.getEffect().getOpacity();
   }

   protected boolean isSamplerLinear(int var1) {
      switch (var1) {
         default:
            return false;
      }
   }

   protected Shader createShader() {
      HashMap var1 = new HashMap();
      var1.put("botImg", 0);
      var1.put("topImg", 1);
      HashMap var2 = new HashMap();
      var2.put("opacity", 0);
      return this.getRenderer().createShader(this.getShaderName(), var1, var2, false);
   }

   protected void updateShader(Shader var1) {
      var1.setConstant("opacity", this.getOpacity());
   }
}
