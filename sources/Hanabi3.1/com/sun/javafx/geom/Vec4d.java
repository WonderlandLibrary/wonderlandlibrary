package com.sun.javafx.geom;

public class Vec4d {
   public double x;
   public double y;
   public double z;
   public double w;

   public Vec4d() {
   }

   public Vec4d(Vec4d var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = var1.w;
   }

   public Vec4d(double var1, double var3, double var5, double var7) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
      this.w = var7;
   }

   public void set(Vec4d var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = var1.w;
   }
}
