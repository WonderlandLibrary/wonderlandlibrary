package com.sun.scenario.effect.impl.prism.ps;

import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.SepiaTone;
import com.sun.scenario.effect.impl.Renderer;
import java.util.HashMap;

public class PPSSepiaTonePeer extends PPSOneSamplerPeer {
   public PPSSepiaTonePeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   protected final SepiaTone getEffect() {
      return (SepiaTone)super.getEffect();
   }

   private float getLevel() {
      return this.getEffect().getLevel();
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
      var2.put("level", 0);
      return this.getRenderer().createShader(this.getShaderName(), var1, var2, false);
   }

   protected void updateShader(Shader var1) {
      var1.setConstant("level", this.getLevel());
   }
}
