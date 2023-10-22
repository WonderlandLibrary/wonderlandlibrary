package com.sun.javafx.geom;

public class Matrix3f {
   public float m00;
   public float m01;
   public float m02;
   public float m10;
   public float m11;
   public float m12;
   public float m20;
   public float m21;
   public float m22;

   public Matrix3f(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      this.m00 = var1;
      this.m01 = var2;
      this.m02 = var3;
      this.m10 = var4;
      this.m11 = var5;
      this.m12 = var6;
      this.m20 = var7;
      this.m21 = var8;
      this.m22 = var9;
   }

   public Matrix3f(float[] var1) {
      this.m00 = var1[0];
      this.m01 = var1[1];
      this.m02 = var1[2];
      this.m10 = var1[3];
      this.m11 = var1[4];
      this.m12 = var1[5];
      this.m20 = var1[6];
      this.m21 = var1[7];
      this.m22 = var1[8];
   }

   public Matrix3f(Vec3f[] var1) {
      this.m00 = var1[0].x;
      this.m01 = var1[0].y;
      this.m02 = var1[0].z;
      this.m10 = var1[1].x;
      this.m11 = var1[1].x;
      this.m12 = var1[1].x;
      this.m20 = var1[2].x;
      this.m21 = var1[2].x;
      this.m22 = var1[2].x;
   }

   public Matrix3f(Matrix3f var1) {
      this.m00 = var1.m00;
      this.m01 = var1.m01;
      this.m02 = var1.m02;
      this.m10 = var1.m10;
      this.m11 = var1.m11;
      this.m12 = var1.m12;
      this.m20 = var1.m20;
      this.m21 = var1.m21;
      this.m22 = var1.m22;
   }

   public Matrix3f() {
      this.m00 = 1.0F;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = 1.0F;
      this.m12 = 0.0F;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = 1.0F;
   }

   public String toString() {
      return this.m00 + ", " + this.m01 + ", " + this.m02 + "\n" + this.m10 + ", " + this.m11 + ", " + this.m12 + "\n" + this.m20 + ", " + this.m21 + ", " + this.m22 + "\n";
   }

   public final void setIdentity() {
      this.m00 = 1.0F;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = 1.0F;
      this.m12 = 0.0F;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = 1.0F;
   }

   public final void setRow(int var1, float[] var2) {
      switch (var1) {
         case 0:
            this.m00 = var2[0];
            this.m01 = var2[1];
            this.m02 = var2[2];
            break;
         case 1:
            this.m10 = var2[0];
            this.m11 = var2[1];
            this.m12 = var2[2];
            break;
         case 2:
            this.m20 = var2[0];
            this.m21 = var2[1];
            this.m22 = var2[2];
            break;
         default:
            throw new ArrayIndexOutOfBoundsException("Matrix3f");
      }

   }

   public final void setRow(int var1, Vec3f var2) {
      switch (var1) {
         case 0:
            this.m00 = var2.x;
            this.m01 = var2.y;
            this.m02 = var2.z;
            break;
         case 1:
            this.m10 = var2.x;
            this.m11 = var2.y;
            this.m12 = var2.z;
            break;
         case 2:
            this.m20 = var2.x;
            this.m21 = var2.y;
            this.m22 = var2.z;
            break;
         default:
            throw new ArrayIndexOutOfBoundsException("Matrix3f");
      }

   }

   public final void getRow(int var1, Vec3f var2) {
      if (var1 == 0) {
         var2.x = this.m00;
         var2.y = this.m01;
         var2.z = this.m02;
      } else if (var1 == 1) {
         var2.x = this.m10;
         var2.y = this.m11;
         var2.z = this.m12;
      } else {
         if (var1 != 2) {
            throw new ArrayIndexOutOfBoundsException("Matrix3f");
         }

         var2.x = this.m20;
         var2.y = this.m21;
         var2.z = this.m22;
      }

   }

   public final void getRow(int var1, float[] var2) {
      if (var1 == 0) {
         var2[0] = this.m00;
         var2[1] = this.m01;
         var2[2] = this.m02;
      } else if (var1 == 1) {
         var2[0] = this.m10;
         var2[1] = this.m11;
         var2[2] = this.m12;
      } else {
         if (var1 != 2) {
            throw new ArrayIndexOutOfBoundsException("Matrix3f");
         }

         var2[0] = this.m20;
         var2[1] = this.m21;
         var2[2] = this.m22;
      }

   }
}
