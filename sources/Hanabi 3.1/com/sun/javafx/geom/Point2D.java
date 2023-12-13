package com.sun.javafx.geom;

public class Point2D {
   public float x;
   public float y;

   public Point2D() {
   }

   public Point2D(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   public void setLocation(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   public void setLocation(Point2D var1) {
      this.setLocation(var1.x, var1.y);
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

   public float distanceSq(Point2D var1) {
      float var2 = var1.x - this.x;
      float var3 = var1.y - this.y;
      return var2 * var2 + var3 * var3;
   }

   public float distance(float var1, float var2) {
      var1 -= this.x;
      var2 -= this.y;
      return (float)Math.sqrt((double)(var1 * var1 + var2 * var2));
   }

   public float distance(Point2D var1) {
      float var2 = var1.x - this.x;
      float var3 = var1.y - this.y;
      return (float)Math.sqrt((double)(var2 * var2 + var3 * var3));
   }

   public int hashCode() {
      int var1 = Float.floatToIntBits(this.x);
      var1 ^= Float.floatToIntBits(this.y) * 31;
      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Point2D)) {
         return false;
      } else {
         Point2D var2 = (Point2D)var1;
         return this.x == var2.x && this.y == var2.y;
      }
   }

   public String toString() {
      return "Point2D[" + this.x + ", " + this.y + "]";
   }
}
