package com.sun.javafx.geom;

public class Vec3f {
   public float x;
   public float y;
   public float z;

   public Vec3f() {
   }

   public Vec3f(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public Vec3f(Vec3f var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
   }

   public void set(Vec3f var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
   }

   public void set(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public final void mul(float var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
   }

   public void sub(Vec3f var1, Vec3f var2) {
      this.x = var1.x - var2.x;
      this.y = var1.y - var2.y;
      this.z = var1.z - var2.z;
   }

   public void sub(Vec3f var1) {
      this.x -= var1.x;
      this.y -= var1.y;
      this.z -= var1.z;
   }

   public void add(Vec3f var1, Vec3f var2) {
      this.x = var1.x + var2.x;
      this.y = var1.y + var2.y;
      this.z = var1.z + var2.z;
   }

   public void add(Vec3f var1) {
      this.x += var1.x;
      this.y += var1.y;
      this.z += var1.z;
   }

   public float length() {
      return (float)Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z));
   }

   public void normalize() {
      float var1 = 1.0F / this.length();
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
   }

   public void cross(Vec3f var1, Vec3f var2) {
      float var3 = var1.y * var2.z - var1.z * var2.y;
      float var4 = var2.x * var1.z - var2.z * var1.x;
      this.z = var1.x * var2.y - var1.y * var2.x;
      this.x = var3;
      this.y = var4;
   }

   public float dot(Vec3f var1) {
      return this.x * var1.x + this.y * var1.y + this.z * var1.z;
   }

   public int hashCode() {
      int var1 = 7;
      var1 = 31 * var1 + Float.floatToIntBits(this.x);
      var1 = 31 * var1 + Float.floatToIntBits(this.y);
      var1 = 31 * var1 + Float.floatToIntBits(this.z);
      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Vec3f)) {
         return false;
      } else {
         Vec3f var2 = (Vec3f)var1;
         return this.x == var2.x && this.y == var2.y && this.z == var2.z;
      }
   }

   public String toString() {
      return "Vec3f[" + this.x + ", " + this.y + ", " + this.z + "]";
   }
}
