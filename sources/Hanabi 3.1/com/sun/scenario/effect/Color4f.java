package com.sun.scenario.effect;

public final class Color4f {
   public static final Color4f BLACK = new Color4f(0.0F, 0.0F, 0.0F, 1.0F);
   public static final Color4f WHITE = new Color4f(1.0F, 1.0F, 1.0F, 1.0F);
   private final float r;
   private final float g;
   private final float b;
   private final float a;

   public Color4f(float var1, float var2, float var3, float var4) {
      this.r = var1;
      this.g = var2;
      this.b = var3;
      this.a = var4;
   }

   public float getRed() {
      return this.r;
   }

   public float getGreen() {
      return this.g;
   }

   public float getBlue() {
      return this.b;
   }

   public float getAlpha() {
      return this.a;
   }

   public float[] getPremultipliedRGBComponents() {
      float[] var1 = new float[]{this.r * this.a, this.g * this.a, this.b * this.a, this.a};
      return var1;
   }
}
