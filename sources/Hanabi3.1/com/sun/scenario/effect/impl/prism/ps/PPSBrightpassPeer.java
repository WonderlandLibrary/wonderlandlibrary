package com.sun.scenario.effect.impl.prism.ps;

import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.Brightpass;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import java.util.HashMap;

public class PPSBrightpassPeer extends PPSOneSamplerPeer {
   public PPSBrightpassPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   protected final Brightpass getEffect() {
      return (Brightpass)super.getEffect();
   }

   private float getThreshold() {
      return this.getEffect().getThreshold();
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
      var2.put("threshold", 0);
      return this.getRenderer().createShader(this.getShaderName(), var1, var2, false);
   }

   protected void updateShader(Shader var1) {
      var1.setConstant("threshold", this.getThreshold());
   }
}
