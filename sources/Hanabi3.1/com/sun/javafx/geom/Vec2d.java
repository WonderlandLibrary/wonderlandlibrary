package com.sun.javafx.geom;

public class Vec2d {
   public double x;
   public double y;

   public Vec2d() {
   }

   public Vec2d(double var1, double var3) {
      this.x = var1;
      this.y = var3;
   }

   public Vec2d(Vec2d var1) {
      this.set(var1);
   }

   public Vec2d(Vec2f var1) {
      this.set(var1);
   }

   public void set(Vec2d var1) {
      this.x = var1.x;
      this.y = var1.y;
   }

   public void set(Vec2f var1) {
      this.x = (double)var1.x;
      this.y = (double)var1.y;
   }

   public void set(double var1, double var3) {
      this.x = var1;
      this.y = var3;
   }

   public static double distanceSq(double var0, double var2, double var4, double var6) {
      var0 -= var4;
      var2 -= var6;
      return var0 * var0 + var2 * var2;
   }

   public static double distance(double var0, double var2, double var4, double var6) {
      var0 -= var4;
      var2 -= var6;
      return Math.sqrt(var0 * var0 + var2 * var2);
   }

   public double distanceSq(double var1, double var3) {
      var1 -= this.x;
      var3 -= this.y;
      return var1 * var1 + var3 * var3;
   }

   public double distanceSq(Vec2d var1) {
      double var2 = var1.x - this.x;
      double var4 = var1.y - this.y;
      return var2 * var2 + var4 * var4;
   }

   public double distance(double var1, double var3) {
      var1 -= this.x;
      var3 -= this.y;
      return Math.sqrt(var1 * var1 + var3 * var3);
   }

   public double distance(Vec2d var1) {
      double var2 = var1.x - this.x;
      double var4 = var1.y - this.y;
      return Math.sqrt(var2 * var2 + var4 * var4);
   }

   public int hashCode() {
      long var1 = 7L;
      var1 = 31L * var1 + Double.doubleToLongBits(this.x);
      var1 = 31L * var1 + Double.doubleToLongBits(this.y);
      return (int)(var1 ^ var1 >> 32);
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Vec2d)) {
         return false;
      } else {
         Vec2d var2 = (Vec2d)var1;
         return this.x == var2.x && this.y == var2.y;
      }
   }

   public String toString() {
      return "Vec2d[" + this.x + ", " + this.y + "]";
   }
}
