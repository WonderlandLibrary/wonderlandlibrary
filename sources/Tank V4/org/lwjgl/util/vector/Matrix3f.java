package org.lwjgl.util.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

public class Matrix3f extends Matrix implements Serializable {
   private static final long serialVersionUID = 1L;
   public float m00;
   public float m01;
   public float m02;
   public float m10;
   public float m11;
   public float m12;
   public float m20;
   public float m21;
   public float m22;

   public Matrix3f() {
      this.setIdentity();
   }

   public Matrix3f load(Matrix3f var1) {
      return load(var1, this);
   }

   public static Matrix3f load(Matrix3f var0, Matrix3f var1) {
      if (var1 == null) {
         var1 = new Matrix3f();
      }

      var1.m00 = var0.m00;
      var1.m10 = var0.m10;
      var1.m20 = var0.m20;
      var1.m01 = var0.m01;
      var1.m11 = var0.m11;
      var1.m21 = var0.m21;
      var1.m02 = var0.m02;
      var1.m12 = var0.m12;
      var1.m22 = var0.m22;
      return var1;
   }

   public Matrix load(FloatBuffer var1) {
      this.m00 = var1.get();
      this.m01 = var1.get();
      this.m02 = var1.get();
      this.m10 = var1.get();
      this.m11 = var1.get();
      this.m12 = var1.get();
      this.m20 = var1.get();
      this.m21 = var1.get();
      this.m22 = var1.get();
      return this;
   }

   public Matrix loadTranspose(FloatBuffer var1) {
      this.m00 = var1.get();
      this.m10 = var1.get();
      this.m20 = var1.get();
      this.m01 = var1.get();
      this.m11 = var1.get();
      this.m21 = var1.get();
      this.m02 = var1.get();
      this.m12 = var1.get();
      this.m22 = var1.get();
      return this;
   }

   public Matrix store(FloatBuffer var1) {
      var1.put(this.m00);
      var1.put(this.m01);
      var1.put(this.m02);
      var1.put(this.m10);
      var1.put(this.m11);
      var1.put(this.m12);
      var1.put(this.m20);
      var1.put(this.m21);
      var1.put(this.m22);
      return this;
   }

   public Matrix storeTranspose(FloatBuffer var1) {
      var1.put(this.m00);
      var1.put(this.m10);
      var1.put(this.m20);
      var1.put(this.m01);
      var1.put(this.m11);
      var1.put(this.m21);
      var1.put(this.m02);
      var1.put(this.m12);
      var1.put(this.m22);
      return this;
   }

   public static Matrix3f add(Matrix3f var0, Matrix3f var1, Matrix3f var2) {
      if (var2 == null) {
         var2 = new Matrix3f();
      }

      var2.m00 = var0.m00 + var1.m00;
      var2.m01 = var0.m01 + var1.m01;
      var2.m02 = var0.m02 + var1.m02;
      var2.m10 = var0.m10 + var1.m10;
      var2.m11 = var0.m11 + var1.m11;
      var2.m12 = var0.m12 + var1.m12;
      var2.m20 = var0.m20 + var1.m20;
      var2.m21 = var0.m21 + var1.m21;
      var2.m22 = var0.m22 + var1.m22;
      return var2;
   }

   public static Matrix3f sub(Matrix3f var0, Matrix3f var1, Matrix3f var2) {
      if (var2 == null) {
         var2 = new Matrix3f();
      }

      var2.m00 = var0.m00 - var1.m00;
      var2.m01 = var0.m01 - var1.m01;
      var2.m02 = var0.m02 - var1.m02;
      var2.m10 = var0.m10 - var1.m10;
      var2.m11 = var0.m11 - var1.m11;
      var2.m12 = var0.m12 - var1.m12;
      var2.m20 = var0.m20 - var1.m20;
      var2.m21 = var0.m21 - var1.m21;
      var2.m22 = var0.m22 - var1.m22;
      return var2;
   }

   public static Matrix3f mul(Matrix3f var0, Matrix3f var1, Matrix3f var2) {
      if (var2 == null) {
         var2 = new Matrix3f();
      }

      float var3 = var0.m00 * var1.m00 + var0.m10 * var1.m01 + var0.m20 * var1.m02;
      float var4 = var0.m01 * var1.m00 + var0.m11 * var1.m01 + var0.m21 * var1.m02;
      float var5 = var0.m02 * var1.m00 + var0.m12 * var1.m01 + var0.m22 * var1.m02;
      float var6 = var0.m00 * var1.m10 + var0.m10 * var1.m11 + var0.m20 * var1.m12;
      float var7 = var0.m01 * var1.m10 + var0.m11 * var1.m11 + var0.m21 * var1.m12;
      float var8 = var0.m02 * var1.m10 + var0.m12 * var1.m11 + var0.m22 * var1.m12;
      float var9 = var0.m00 * var1.m20 + var0.m10 * var1.m21 + var0.m20 * var1.m22;
      float var10 = var0.m01 * var1.m20 + var0.m11 * var1.m21 + var0.m21 * var1.m22;
      float var11 = var0.m02 * var1.m20 + var0.m12 * var1.m21 + var0.m22 * var1.m22;
      var2.m00 = var3;
      var2.m01 = var4;
      var2.m02 = var5;
      var2.m10 = var6;
      var2.m11 = var7;
      var2.m12 = var8;
      var2.m20 = var9;
      var2.m21 = var10;
      var2.m22 = var11;
      return var2;
   }

   public static Vector3f transform(Matrix3f var0, Vector3f var1, Vector3f var2) {
      if (var2 == null) {
         var2 = new Vector3f();
      }

      float var3 = var0.m00 * var1.x + var0.m10 * var1.y + var0.m20 * var1.z;
      float var4 = var0.m01 * var1.x + var0.m11 * var1.y + var0.m21 * var1.z;
      float var5 = var0.m02 * var1.x + var0.m12 * var1.y + var0.m22 * var1.z;
      var2.x = var3;
      var2.y = var4;
      var2.z = var5;
      return var2;
   }

   public Matrix transpose() {
      return transpose(this, this);
   }

   public Matrix3f transpose(Matrix3f var1) {
      return transpose(this, var1);
   }

   public static Matrix3f transpose(Matrix3f var0, Matrix3f var1) {
      if (var1 == null) {
         var1 = new Matrix3f();
      }

      float var2 = var0.m00;
      float var3 = var0.m10;
      float var4 = var0.m20;
      float var5 = var0.m01;
      float var6 = var0.m11;
      float var7 = var0.m21;
      float var8 = var0.m02;
      float var9 = var0.m12;
      float var10 = var0.m22;
      var1.m00 = var2;
      var1.m01 = var3;
      var1.m02 = var4;
      var1.m10 = var5;
      var1.m11 = var6;
      var1.m12 = var7;
      var1.m20 = var8;
      var1.m21 = var9;
      var1.m22 = var10;
      return var1;
   }

   public float determinant() {
      float var1 = this.m00 * (this.m11 * this.m22 - this.m12 * this.m21) + this.m01 * (this.m12 * this.m20 - this.m10 * this.m22) + this.m02 * (this.m10 * this.m21 - this.m11 * this.m20);
      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.m00).append(' ').append(this.m10).append(' ').append(this.m20).append(' ').append('\n');
      var1.append(this.m01).append(' ').append(this.m11).append(' ').append(this.m21).append(' ').append('\n');
      var1.append(this.m02).append(' ').append(this.m12).append(' ').append(this.m22).append(' ').append('\n');
      return var1.toString();
   }

   public Matrix invert() {
      return invert(this, this);
   }

   public static Matrix3f invert(Matrix3f var0, Matrix3f var1) {
      float var2 = var0.determinant();
      if (var2 != 0.0F) {
         if (var1 == null) {
            var1 = new Matrix3f();
         }

         float var3 = 1.0F / var2;
         float var4 = var0.m11 * var0.m22 - var0.m12 * var0.m21;
         float var5 = -var0.m10 * var0.m22 + var0.m12 * var0.m20;
         float var6 = var0.m10 * var0.m21 - var0.m11 * var0.m20;
         float var7 = -var0.m01 * var0.m22 + var0.m02 * var0.m21;
         float var8 = var0.m00 * var0.m22 - var0.m02 * var0.m20;
         float var9 = -var0.m00 * var0.m21 + var0.m01 * var0.m20;
         float var10 = var0.m01 * var0.m12 - var0.m02 * var0.m11;
         float var11 = -var0.m00 * var0.m12 + var0.m02 * var0.m10;
         float var12 = var0.m00 * var0.m11 - var0.m01 * var0.m10;
         var1.m00 = var4 * var3;
         var1.m11 = var8 * var3;
         var1.m22 = var12 * var3;
         var1.m01 = var7 * var3;
         var1.m10 = var5 * var3;
         var1.m20 = var6 * var3;
         var1.m02 = var10 * var3;
         var1.m12 = var11 * var3;
         var1.m21 = var9 * var3;
         return var1;
      } else {
         return null;
      }
   }

   public Matrix negate() {
      return this.negate(this);
   }

   public Matrix3f negate(Matrix3f var1) {
      return negate(this, var1);
   }

   public static Matrix3f negate(Matrix3f var0, Matrix3f var1) {
      if (var1 == null) {
         var1 = new Matrix3f();
      }

      var1.m00 = -var0.m00;
      var1.m01 = -var0.m02;
      var1.m02 = -var0.m01;
      var1.m10 = -var0.m10;
      var1.m11 = -var0.m12;
      var1.m12 = -var0.m11;
      var1.m20 = -var0.m20;
      var1.m21 = -var0.m22;
      var1.m22 = -var0.m21;
      return var1;
   }

   public Matrix setIdentity() {
      return setIdentity(this);
   }

   public static Matrix3f setIdentity(Matrix3f var0) {
      var0.m00 = 1.0F;
      var0.m01 = 0.0F;
      var0.m02 = 0.0F;
      var0.m10 = 0.0F;
      var0.m11 = 1.0F;
      var0.m12 = 0.0F;
      var0.m20 = 0.0F;
      var0.m21 = 0.0F;
      var0.m22 = 1.0F;
      return var0;
   }

   public Matrix setZero() {
      return setZero(this);
   }

   public static Matrix3f setZero(Matrix3f var0) {
      var0.m00 = 0.0F;
      var0.m01 = 0.0F;
      var0.m02 = 0.0F;
      var0.m10 = 0.0F;
      var0.m11 = 0.0F;
      var0.m12 = 0.0F;
      var0.m20 = 0.0F;
      var0.m21 = 0.0F;
      var0.m22 = 0.0F;
      return var0;
   }
}
