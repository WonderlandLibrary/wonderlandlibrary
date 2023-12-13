package com.sun.scenario.effect.light;

import com.sun.scenario.effect.Color4f;

public class SpotLight extends PointLight {
   private float pointsAtX;
   private float pointsAtY;
   private float pointsAtZ;
   private float specularExponent;

   public SpotLight() {
      this(0.0F, 0.0F, 0.0F, Color4f.WHITE);
   }

   public SpotLight(float var1, float var2, float var3, Color4f var4) {
      super(Light.Type.SPOT, var1, var2, var3, var4);
      this.pointsAtX = 0.0F;
      this.pointsAtY = 0.0F;
      this.pointsAtZ = 0.0F;
      this.specularExponent = 1.0F;
   }

   public float getPointsAtX() {
      return this.pointsAtX;
   }

   public void setPointsAtX(float var1) {
      this.pointsAtX = var1;
   }

   public float getPointsAtY() {
      return this.pointsAtY;
   }

   public void setPointsAtY(float var1) {
      float var2 = this.pointsAtY;
      this.pointsAtY = var1;
   }

   public float getPointsAtZ() {
      return this.pointsAtZ;
   }

   public void setPointsAtZ(float var1) {
      this.pointsAtZ = var1;
   }

   public float getSpecularExponent() {
      return this.specularExponent;
   }

   public void setSpecularExponent(float var1) {
      if (!(var1 < 0.0F) && !(var1 > 4.0F)) {
         this.specularExponent = var1;
      } else {
         throw new IllegalArgumentException("Specular exponent must be in the range [0,4]");
      }
   }

   public float[] getNormalizedLightPosition() {
      float var1 = this.getX();
      float var2 = this.getY();
      float var3 = this.getZ();
      float var4 = (float)Math.sqrt((double)(var1 * var1 + var2 * var2 + var3 * var3));
      if (var4 == 0.0F) {
         var4 = 1.0F;
      }

      float[] var5 = new float[]{var1 / var4, var2 / var4, var3 / var4};
      return var5;
   }

   public float[] getNormalizedLightDirection() {
      float var1 = this.pointsAtX - this.getX();
      float var2 = this.pointsAtY - this.getY();
      float var3 = this.pointsAtZ - this.getZ();
      float var4 = (float)Math.sqrt((double)(var1 * var1 + var2 * var2 + var3 * var3));
      if (var4 == 0.0F) {
         var4 = 1.0F;
      }

      float[] var5 = new float[]{var1 / var4, var2 / var4, var3 / var4};
      return var5;
   }
}
