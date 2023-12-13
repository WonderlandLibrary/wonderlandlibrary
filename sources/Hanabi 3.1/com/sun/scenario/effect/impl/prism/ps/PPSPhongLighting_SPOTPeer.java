package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.PhongLighting;
import com.sun.scenario.effect.impl.BufferUtil;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.light.PointLight;
import com.sun.scenario.effect.light.SpotLight;
import java.nio.FloatBuffer;
import java.util.HashMap;

public class PPSPhongLighting_SPOTPeer extends PPSTwoSamplerPeer {
   private FloatBuffer kvals;

   public PPSPhongLighting_SPOTPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   protected final PhongLighting getEffect() {
      return (PhongLighting)super.getEffect();
   }

   private float getSurfaceScale() {
      return this.getEffect().getSurfaceScale();
   }

   private float getDiffuseConstant() {
      return this.getEffect().getDiffuseConstant();
   }

   private float getSpecularConstant() {
      return this.getEffect().getSpecularConstant();
   }

   private float getSpecularExponent() {
      return this.getEffect().getSpecularExponent();
   }

   private float[] getNormalizedLightPosition() {
      return this.getEffect().getLight().getNormalizedLightPosition();
   }

   private float[] getLightPosition() {
      PointLight var1 = (PointLight)this.getEffect().getLight();
      return new float[]{var1.getX(), var1.getY(), var1.getZ()};
   }

   private float[] getLightColor() {
      return this.getEffect().getLight().getColor().getPremultipliedRGBComponents();
   }

   private float getLightSpecularExponent() {
      return ((SpotLight)this.getEffect().getLight()).getSpecularExponent();
   }

   private float[] getNormalizedLightDirection() {
      return ((SpotLight)this.getEffect().getLight()).getNormalizedLightDirection();
   }

   private FloatBuffer getKvals() {
      Rectangle var1 = this.getInputNativeBounds(0);
      float var2 = 1.0F / (float)var1.width;
      float var3 = 1.0F / (float)var1.height;
      float[] var4 = new float[]{-1.0F, 0.0F, 1.0F, -2.0F, 0.0F, 2.0F, -1.0F, 0.0F, 1.0F};
      float[] var5 = new float[]{-1.0F, -2.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F};
      if (this.kvals == null) {
         this.kvals = BufferUtil.newFloatBuffer(32);
      }

      this.kvals.clear();
      int var6 = 0;
      float var7 = -this.getSurfaceScale() * 0.25F;

      for(int var8 = -1; var8 <= 1; ++var8) {
         for(int var9 = -1; var9 <= 1; ++var9) {
            if (var8 != 0 || var9 != 0) {
               this.kvals.put((float)var9 * var2);
               this.kvals.put((float)var8 * var3);
               this.kvals.put(var4[var6] * var7);
               this.kvals.put(var5[var6] * var7);
            }

            ++var6;
         }
      }

      this.kvals.rewind();
      return this.kvals;
   }

   private int getKvalsArrayLength() {
      return 8;
   }

   protected boolean isSamplerLinear(int var1) {
      switch (var1) {
         default:
            return false;
      }
   }

   protected Shader createShader() {
      HashMap var1 = new HashMap();
      var1.put("bumpImg", 0);
      var1.put("origImg", 1);
      HashMap var2 = new HashMap();
      var2.put("lightPosition", 13);
      var2.put("lightSpecularExponent", 15);
      var2.put("specularExponent", 2);
      var2.put("kvals", 4);
      var2.put("diffuseConstant", 0);
      var2.put("lightColor", 3);
      var2.put("normalizedLightDirection", 14);
      var2.put("specularConstant", 1);
      var2.put("surfaceScale", 12);
      return this.getRenderer().createShader(this.getShaderName(), var1, var2, true);
   }

   protected void updateShader(Shader var1) {
      float[] var2 = this.getLightPosition();
      var1.setConstant("lightPosition", var2[0], var2[1], var2[2]);
      var1.setConstant("lightSpecularExponent", this.getLightSpecularExponent());
      var1.setConstant("specularExponent", this.getSpecularExponent());
      var1.setConstants("kvals", (FloatBuffer)this.getKvals(), 0, this.getKvalsArrayLength());
      var1.setConstant("diffuseConstant", this.getDiffuseConstant());
      float[] var3 = this.getLightColor();
      var1.setConstant("lightColor", var3[0], var3[1], var3[2]);
      float[] var4 = this.getNormalizedLightDirection();
      var1.setConstant("normalizedLightDirection", var4[0], var4[1], var4[2]);
      var1.setConstant("specularConstant", this.getSpecularConstant());
      var1.setConstant("surfaceScale", this.getSurfaceScale());
   }
}
