package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.PerspectiveTransform;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.AccessHelper;
import com.sun.scenario.effect.impl.state.PerspectiveTransformState;
import java.util.HashMap;

public class PPSPerspectiveTransformPeer extends PPSOneSamplerPeer {
   public PPSPerspectiveTransformPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   protected final PerspectiveTransform getEffect() {
      return (PerspectiveTransform)super.getEffect();
   }

   private float[][] getITX() {
      PerspectiveTransformState var1 = (PerspectiveTransformState)AccessHelper.getState(this.getEffect());
      return var1.getITX();
   }

   private float[] getTx0() {
      Rectangle var1 = this.getInputBounds(0);
      Rectangle var2 = this.getInputNativeBounds(0);
      float var3 = (float)var1.width / (float)var2.width;
      float[] var4 = this.getITX()[0];
      return new float[]{var4[0] * var3, var4[1] * var3, var4[2] * var3};
   }

   private float[] getTx1() {
      Rectangle var1 = this.getInputBounds(0);
      Rectangle var2 = this.getInputNativeBounds(0);
      float var3 = (float)var1.height / (float)var2.height;
      float[] var4 = this.getITX()[1];
      return new float[]{var4[0] * var3, var4[1] * var3, var4[2] * var3};
   }

   private float[] getTx2() {
      return this.getITX()[2];
   }

   public int getTextureCoordinates(int var1, float[] var2, float var3, float var4, float var5, float var6, Rectangle var7, BaseTransform var8) {
      var2[0] = (float)var7.x;
      var2[1] = (float)var7.y;
      var2[2] = (float)(var7.x + var7.width);
      var2[3] = (float)(var7.y + var7.height);
      return 4;
   }

   protected boolean isSamplerLinear(int var1) {
      switch (var1) {
         case 0:
            return true;
         default:
            return false;
      }
   }

   protected Shader createShader() {
      HashMap var1 = new HashMap();
      var1.put("baseImg", 0);
      HashMap var2 = new HashMap();
      var2.put("tx1", 1);
      var2.put("tx0", 0);
      var2.put("tx2", 2);
      return this.getRenderer().createShader(this.getShaderName(), var1, var2, false);
   }

   protected void updateShader(Shader var1) {
      float[] var2 = this.getTx1();
      var1.setConstant("tx1", var2[0], var2[1], var2[2]);
      float[] var3 = this.getTx0();
      var1.setConstant("tx0", var3[0], var3[1], var3[2]);
      float[] var4 = this.getTx2();
      var1.setConstant("tx2", var4[0], var4[1], var4[2]);
   }
}
