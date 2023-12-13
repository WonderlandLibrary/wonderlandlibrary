package com.sun.scenario.effect.light;

import com.sun.scenario.effect.Color4f;

public class DistantLight extends Light {
   private float azimuth;
   private float elevation;

   public DistantLight() {
      this(0.0F, 0.0F, Color4f.WHITE);
   }

   public DistantLight(float var1, float var2, Color4f var3) {
      super(Light.Type.DISTANT, var3);
      this.azimuth = var1;
      this.elevation = var2;
   }

   public float getAzimuth() {
      return this.azimuth;
   }

   public void setAzimuth(float var1) {
      this.azimuth = var1;
   }

   public float getElevation() {
      return this.elevation;
   }

   public void setElevation(float var1) {
      this.elevation = var1;
   }

   public float[] getNormalizedLightPosition() {
      double var1 = Math.toRadians((double)this.azimuth);
      double var3 = Math.toRadians((double)this.elevation);
      float var5 = (float)(Math.cos(var1) * Math.cos(var3));
      float var6 = (float)(Math.sin(var1) * Math.cos(var3));
      float var7 = (float)Math.sin(var3);
      float var8 = (float)Math.sqrt((double)(var5 * var5 + var6 * var6 + var7 * var7));
      if (var8 == 0.0F) {
         var8 = 1.0F;
      }

      float[] var9 = new float[]{var5 / var8, var6 / var8, var7 / var8};
      return var9;
   }
}
