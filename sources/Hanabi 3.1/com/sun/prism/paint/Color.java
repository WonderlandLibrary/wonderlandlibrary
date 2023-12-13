package com.sun.prism.paint;

import java.nio.ByteBuffer;

public final class Color extends Paint {
   public static final Color WHITE = new Color(1.0F, 1.0F, 1.0F, 1.0F);
   public static final Color BLACK = new Color(0.0F, 0.0F, 0.0F, 1.0F);
   public static final Color RED = new Color(1.0F, 0.0F, 0.0F, 1.0F);
   public static final Color GREEN = new Color(0.0F, 1.0F, 0.0F, 1.0F);
   public static final Color BLUE = new Color(0.0F, 0.0F, 1.0F, 1.0F);
   public static final Color TRANSPARENT = new Color(0.0F, 0.0F, 0.0F, 0.0F);
   private final int argb;
   private final float r;
   private final float g;
   private final float b;
   private final float a;

   public Color(float var1, float var2, float var3, float var4) {
      super(Paint.Type.COLOR, false, false);
      int var5 = (int)(255.0 * (double)var4);
      int var6 = (int)(255.0 * (double)var1 * (double)var4);
      int var7 = (int)(255.0 * (double)var2 * (double)var4);
      int var8 = (int)(255.0 * (double)var3 * (double)var4);
      this.argb = var5 << 24 | var6 << 16 | var7 << 8 | var8 << 0;
      this.r = var1;
      this.g = var2;
      this.b = var3;
      this.a = var4;
   }

   public int getIntArgbPre() {
      return this.argb;
   }

   public void putRgbaPreBytes(byte[] var1, int var2) {
      var1[var2 + 0] = (byte)(this.argb >> 16 & 255);
      var1[var2 + 1] = (byte)(this.argb >> 8 & 255);
      var1[var2 + 2] = (byte)(this.argb & 255);
      var1[var2 + 3] = (byte)(this.argb >> 24 & 255);
   }

   public void putBgraPreBytes(ByteBuffer var1) {
      var1.put((byte)(this.argb & 255));
      var1.put((byte)(this.argb >> 8 & 255));
      var1.put((byte)(this.argb >> 16 & 255));
      var1.put((byte)(this.argb >> 24 & 255));
   }

   public float getRed() {
      return this.r;
   }

   public float getRedPremult() {
      return this.r * this.a;
   }

   public float getGreen() {
      return this.g;
   }

   public float getGreenPremult() {
      return this.g * this.a;
   }

   public float getBlue() {
      return this.b;
   }

   public float getBluePremult() {
      return this.b * this.a;
   }

   public float getAlpha() {
      return this.a;
   }

   public boolean isOpaque() {
      return this.a >= 1.0F;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Color)) {
         return false;
      } else {
         Color var2 = (Color)var1;
         return this.r == var2.r && this.g == var2.g && this.b == var2.b && this.a == var2.a;
      }
   }

   public int hashCode() {
      int var1 = 3;
      var1 = 53 * var1 + Float.floatToIntBits(this.r);
      var1 = 53 * var1 + Float.floatToIntBits(this.g);
      var1 = 53 * var1 + Float.floatToIntBits(this.b);
      var1 = 53 * var1 + Float.floatToIntBits(this.a);
      return var1;
   }

   public String toString() {
      return "Color[r=" + this.r + ", g=" + this.g + ", b=" + this.b + ", a=" + this.a + "]";
   }
}
