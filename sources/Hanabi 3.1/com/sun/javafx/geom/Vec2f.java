package com.sun.javafx.geom;

public class Vec2f {
   public float x;
   public float y;

   public Vec2f() {
   }

   public Vec2f(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   public Vec2f(Vec2f var1) {
      this.x = var1.x;
      this.y = var1.y;
   }

   public void set(Vec2f var1) {
      this.x = var1.x;
      this.y = var1.y;
   }

   public void set(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   public static float distanceSq(float var0, float var1, float var2, float var3) {
      var0 -= var2;
      var1 -= var3;
      return var0 * var0 + var1 * var1;
   }

   public static float distance(float var0, float var1, float var2, float var3) {
      var0 -= var2;
      var1 -= var3;
      return (float)Math.sqrt((double)(var0 * var0 + var1 * var1));
   }

   public float distanceSq(float var1, float var2) {
      var1 -= this.x;
      var2 -= this.y;
      return var1 * var1 + var2 * var2;
   }

   public float distanceSq(Vec2f var1) {
      float var2 = var1.x - this.x;
      float var3 = var1.y - this.y;
      return var2 * var2 + var3 * var3;
   }

   public float distance(float var1, float var2) {
      var1 -= this.x;
      var2 -= this.y;
      return (float)Math.sqrt((double)(var1 * var1 + var2 * var2));
   }

   public float distance(Vec2f var1) {
      float var2 = var1.x - this.x;
      float var3 = var1.y - this.y;
      return (float)Math.sqrt((double)(var2 * var2 + var3 * var3));
   }

   public int hashCode() {
      int var1 = 7;
      var1 = 31 * var1 + Float.floatToIntBits(this.x);
      var1 = 31 * var1 + Float.floatToIntBits(this.y);
      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Vec2f)) {
         return false;
      } else {
         Vec2f var2 = (Vec2f)var1;
         return this.x == var2.x && this.y == var2.y;
      }
   }

   public String toString() {
      return "Vec2f[" + this.x + ", " + this.y + "]";
   }
}
