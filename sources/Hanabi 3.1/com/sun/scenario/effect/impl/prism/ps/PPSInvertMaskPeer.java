package com.sun.scenario.effect.impl.prism.ps;

import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.InvertMask;
import com.sun.scenario.effect.impl.Renderer;
import java.util.HashMap;

public class PPSInvertMaskPeer extends PPSOneSamplerPeer {
   public PPSInvertMaskPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   protected final InvertMask getEffect() {
      return (InvertMask)super.getEffect();
   }

   private float[] getOffset() {
      float var1 = (float)this.getEffect().getOffsetX();
      float var2 = (float)this.getEffect().getOffsetY();
      float[] var3 = new float[]{var1, var2};

      try {
         this.getInputTransform(0).inverseDeltaTransform(var3, 0, var3, 0, 1);
      } catch (Exception var5) {
      }

      var3[0] /= (float)this.getInputNativeBounds(0).width;
      var3[1] /= (float)this.getInputNativeBounds(0).height;
      return var3;
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
      var2.put("offset", 0);
      return this.getRenderer().createShader(this.getShaderName(), var1, var2, false);
   }

   protected void updateShader(Shader var1) {
      float[] var2 = this.getOffset();
      var1.setConstant("offset", var2[0], var2[1]);
   }
}
