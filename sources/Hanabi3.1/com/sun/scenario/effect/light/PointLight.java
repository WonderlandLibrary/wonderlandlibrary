package com.sun.scenario.effect.light;

import com.sun.scenario.effect.Color4f;

public class PointLight extends Light {
   private float x;
   private float y;
   private float z;

   public PointLight() {
      this(0.0F, 0.0F, 0.0F, Color4f.WHITE);
   }

   public PointLight(float var1, float var2, float var3, Color4f var4) {
      this(Light.Type.POINT, var1, var2, var3, var4);
   }

   PointLight(Light.Type var1, float var2, float var3, float var4, Color4f var5) {
      super(var1, var5);
      this.x = var2;
      this.y = var3;
      this.z = var4;
   }

   public float getX() {
      return this.x;
   }

   public void setX(float var1) {
      this.x = var1;
   }

   public float getY() {
      return this.y;
   }

   public void setY(float var1) {
      this.y = var1;
   }

   public float getZ() {
      return this.z;
   }

   public void setZ(float var1) {
      this.z = var1;
   }

   public float[] getNormalizedLightPosition() {
      float var1 = (float)Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z));
      if (var1 == 0.0F) {
         var1 = 1.0F;
      }

      float[] var2 = new float[]{this.x / var1, this.y / var1, this.z / var1};
      return var2;
   }
}
