package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;
import java.nio.FloatBuffer;
import java.util.HashMap;

public class PPSLinearConvolvePeer extends PPSOneSamplerPeer {
   public PPSLinearConvolvePeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   protected final Effect getEffect() {
      return super.getEffect();
   }

   public Rectangle getResultBounds(BaseTransform var1, Rectangle var2, ImageData... var3) {
      Rectangle var4 = var3[0].getTransformedBounds((Rectangle)null);
      var4 = ((LinearConvolveRenderState)this.getRenderState()).getPassResultBounds(var4, var2);
      return var4;
   }

   private int getCount() {
      return (((LinearConvolveRenderState)this.getRenderState()).getPassKernelSize() + 3) / 4;
   }

   private float[] getOffset() {
      return ((LinearConvolveRenderState)this.getRenderState()).getPassVector();
   }

   private FloatBuffer getWeights() {
      return ((LinearConvolveRenderState)this.getRenderState()).getPassWeights();
   }

   private int getWeightsArrayLength() {
      return ((LinearConvolveRenderState)this.getRenderState()).getPassWeightsArrayLength();
   }

   protected boolean isSamplerLinear(int var1) {
      switch (var1) {
         default:
            return false;
      }
   }

   protected Shader createShader() {
      HashMap var1 = new HashMap();
      var1.put("img", 0);
      HashMap var2 = new HashMap();
      var2.put("offset", 1);
      var2.put("count", 0);
      var2.put("weights", 2);
      return this.getRenderer().createShader(this.getShaderName(), var1, var2, false);
   }

   protected void updateShader(Shader var1) {
      float[] var2 = this.getOffset();
      var1.setConstant("offset", var2[0], var2[1], var2[2], var2[3]);
      var1.setConstant("count", this.getCount());
      var1.setConstants("weights", (FloatBuffer)this.getWeights(), 0, this.getWeightsArrayLength());
   }
}
