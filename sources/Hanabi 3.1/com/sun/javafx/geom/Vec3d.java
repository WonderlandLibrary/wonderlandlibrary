package com.sun.javafx.geom;

public class Vec3d {
   public double x;
   public double y;
   public double z;

   public Vec3d() {
   }

   public Vec3d(double var1, double var3, double var5) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
   }

   public Vec3d(Vec3d var1) {
      this.set(var1);
   }

   public Vec3d(Vec3f var1) {
      this.set(var1);
   }

   public void set(Vec3f var1) {
      this.x = (double)var1.x;
      this.y = (double)var1.y;
      this.z = (double)var1.z;
   }

   public void set(Vec3d var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
   }

   public void set(double var1, double var3, double var5) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
   }

   public void mul(double var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
   }

   public void sub(Vec3f var1, Vec3f var2) {
      this.x = (double)(var1.x - var2.x);
      this.y = (double)(var1.y - var2.y);
      this.z = (double)(var1.z - var2.z);
   }

   public void sub(Vec3d var1, Vec3d var2) {
      this.x = var1.x - var2.x;
      this.y = var1.y - var2.y;
      this.z = var1.z - var2.z;
   }

   public void sub(Vec3d var1) {
      this.x -= var1.x;
      this.y -= var1.y;
      this.z -= var1.z;
   }

   public void add(Vec3d var1, Vec3d var2) {
      this.x = var1.x + var2.x;
      this.y = var1.y + var2.y;
      this.z = var1.z + var2.z;
   }

   public void add(Vec3d var1) {
      this.x += var1.x;
      this.y += var1.y;
      this.z += var1.z;
   }

   public double length() {
      return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
   }

   public void normalize() {
      double var1 = 1.0 / this.length();
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
   }

   public void cross(Vec3d var1, Vec3d var2) {
      double var3 = var1.y * var2.z - var1.z * var2.y;
      double var5 = var2.x * var1.z - var2.z * var1.x;
      this.z = var1.x * var2.y - var1.y * var2.x;
      this.x = var3;
      this.y = var5;
   }

   public double dot(Vec3d var1) {
      return this.x * var1.x + this.y * var1.y + this.z * var1.z;
   }

   public int hashCode() {
      long var1 = 7L;
      var1 = 31L * var1 + Double.doubleToLongBits(this.x);
      var1 = 31L * var1 + Double.doubleToLongBits(this.y);
      var1 = 31L * var1 + Double.doubleToLongBits(this.z);
      return (int)(var1 ^ var1 >> 32);
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Vec3d)) {
         return false;
      } else {
         Vec3d var2 = (Vec3d)var1;
         return this.x == var2.x && this.y == var2.y && this.z == var2.z;
      }
   }

   public String toString() {
      return "Vec3d[" + this.x + ", " + this.y + ", " + this.z + "]";
   }
}
