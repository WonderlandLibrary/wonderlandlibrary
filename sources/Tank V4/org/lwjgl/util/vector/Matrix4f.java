package org.lwjgl.util.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

public class Matrix4f extends Matrix implements Serializable {
   private static final long serialVersionUID = 1L;
   public float m00;
   public float m01;
   public float m02;
   public float m03;
   public float m10;
   public float m11;
   public float m12;
   public float m13;
   public float m20;
   public float m21;
   public float m22;
   public float m23;
   public float m30;
   public float m31;
   public float m32;
   public float m33;

   public Matrix4f() {
      this.setIdentity();
   }

   public Matrix4f(Matrix4f var1) {
      this.load(var1);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.m00).append(' ').append(this.m10).append(' ').append(this.m20).append(' ').append(this.m30).append('\n');
      var1.append(this.m01).append(' ').append(this.m11).append(' ').append(this.m21).append(' ').append(this.m31).append('\n');
      var1.append(this.m02).append(' ').append(this.m12).append(' ').append(this.m22).append(' ').append(this.m32).append('\n');
      var1.append(this.m03).append(' ').append(this.m13).append(' ').append(this.m23).append(' ').append(this.m33).append('\n');
      return var1.toString();
   }

   public Matrix setIdentity() {
      return setIdentity(this);
   }

   public static Matrix4f setIdentity(Matrix4f var0) {
      var0.m00 = 1.0F;
      var0.m01 = 0.0F;
      var0.m02 = 0.0F;
      var0.m03 = 0.0F;
      var0.m10 = 0.0F;
      var0.m11 = 1.0F;
      var0.m12 = 0.0F;
      var0.m13 = 0.0F;
      var0.m20 = 0.0F;
      var0.m21 = 0.0F;
      var0.m22 = 1.0F;
      var0.m23 = 0.0F;
      var0.m30 = 0.0F;
      var0.m31 = 0.0F;
      var0.m32 = 0.0F;
      var0.m33 = 1.0F;
      return var0;
   }

   public Matrix setZero() {
      return setZero(this);
   }

   public static Matrix4f setZero(Matrix4f var0) {
      var0.m00 = 0.0F;
      var0.m01 = 0.0F;
      var0.m02 = 0.0F;
      var0.m03 = 0.0F;
      var0.m10 = 0.0F;
      var0.m11 = 0.0F;
      var0.m12 = 0.0F;
      var0.m13 = 0.0F;
      var0.m20 = 0.0F;
      var0.m21 = 0.0F;
      var0.m22 = 0.0F;
      var0.m23 = 0.0F;
      var0.m30 = 0.0F;
      var0.m31 = 0.0F;
      var0.m32 = 0.0F;
      var0.m33 = 0.0F;
      return var0;
   }

   public Matrix4f load(Matrix4f var1) {
      return load(var1, this);
   }

   public static Matrix4f load(Matrix4f var0, Matrix4f var1) {
      if (var1 == null) {
         var1 = new Matrix4f();
      }

      var1.m00 = var0.m00;
      var1.m01 = var0.m01;
      var1.m02 = var0.m02;
      var1.m03 = var0.m03;
      var1.m10 = var0.m10;
      var1.m11 = var0.m11;
      var1.m12 = var0.m12;
      var1.m13 = var0.m13;
      var1.m20 = var0.m20;
      var1.m21 = var0.m21;
      var1.m22 = var0.m22;
      var1.m23 = var0.m23;
      var1.m30 = var0.m30;
      var1.m31 = var0.m31;
      var1.m32 = var0.m32;
      var1.m33 = var0.m33;
      return var1;
   }

   public Matrix load(FloatBuffer var1) {
      this.m00 = var1.get();
      this.m01 = var1.get();
      this.m02 = var1.get();
      this.m03 = var1.get();
      this.m10 = var1.get();
      this.m11 = var1.get();
      this.m12 = var1.get();
      this.m13 = var1.get();
      this.m20 = var1.get();
      this.m21 = var1.get();
      this.m22 = var1.get();
      this.m23 = var1.get();
      this.m30 = var1.get();
      this.m31 = var1.get();
      this.m32 = var1.get();
      this.m33 = var1.get();
      return this;
   }

   public Matrix loadTranspose(FloatBuffer var1) {
      this.m00 = var1.get();
      this.m10 = var1.get();
      this.m20 = var1.get();
      this.m30 = var1.get();
      this.m01 = var1.get();
      this.m11 = var1.get();
      this.m21 = var1.get();
      this.m31 = var1.get();
      this.m02 = var1.get();
      this.m12 = var1.get();
      this.m22 = var1.get();
      this.m32 = var1.get();
      this.m03 = var1.get();
      this.m13 = var1.get();
      this.m23 = var1.get();
      this.m33 = var1.get();
      return this;
   }

   public Matrix store(FloatBuffer var1) {
      var1.put(this.m00);
      var1.put(this.m01);
      var1.put(this.m02);
      var1.put(this.m03);
      var1.put(this.m10);
      var1.put(this.m11);
      var1.put(this.m12);
      var1.put(this.m13);
      var1.put(this.m20);
      var1.put(this.m21);
      var1.put(this.m22);
      var1.put(this.m23);
      var1.put(this.m30);
      var1.put(this.m31);
      var1.put(this.m32);
      var1.put(this.m33);
      return this;
   }

   public Matrix storeTranspose(FloatBuffer var1) {
      var1.put(this.m00);
      var1.put(this.m10);
      var1.put(this.m20);
      var1.put(this.m30);
      var1.put(this.m01);
      var1.put(this.m11);
      var1.put(this.m21);
      var1.put(this.m31);
      var1.put(this.m02);
      var1.put(this.m12);
      var1.put(this.m22);
      var1.put(this.m32);
      var1.put(this.m03);
      var1.put(this.m13);
      var1.put(this.m23);
      var1.put(this.m33);
      return this;
   }

   public Matrix store3f(FloatBuffer var1) {
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

   public static Matrix4f add(Matrix4f var0, Matrix4f var1, Matrix4f var2) {
      if (var2 == null) {
         var2 = new Matrix4f();
      }

      var2.m00 = var0.m00 + var1.m00;
      var2.m01 = var0.m01 + var1.m01;
      var2.m02 = var0.m02 + var1.m02;
      var2.m03 = var0.m03 + var1.m03;
      var2.m10 = var0.m10 + var1.m10;
      var2.m11 = var0.m11 + var1.m11;
      var2.m12 = var0.m12 + var1.m12;
      var2.m13 = var0.m13 + var1.m13;
      var2.m20 = var0.m20 + var1.m20;
      var2.m21 = var0.m21 + var1.m21;
      var2.m22 = var0.m22 + var1.m22;
      var2.m23 = var0.m23 + var1.m23;
      var2.m30 = var0.m30 + var1.m30;
      var2.m31 = var0.m31 + var1.m31;
      var2.m32 = var0.m32 + var1.m32;
      var2.m33 = var0.m33 + var1.m33;
      return var2;
   }

   public static Matrix4f sub(Matrix4f var0, Matrix4f var1, Matrix4f var2) {
      if (var2 == null) {
         var2 = new Matrix4f();
      }

      var2.m00 = var0.m00 - var1.m00;
      var2.m01 = var0.m01 - var1.m01;
      var2.m02 = var0.m02 - var1.m02;
      var2.m03 = var0.m03 - var1.m03;
      var2.m10 = var0.m10 - var1.m10;
      var2.m11 = var0.m11 - var1.m11;
      var2.m12 = var0.m12 - var1.m12;
      var2.m13 = var0.m13 - var1.m13;
      var2.m20 = var0.m20 - var1.m20;
      var2.m21 = var0.m21 - var1.m21;
      var2.m22 = var0.m22 - var1.m22;
      var2.m23 = var0.m23 - var1.m23;
      var2.m30 = var0.m30 - var1.m30;
      var2.m31 = var0.m31 - var1.m31;
      var2.m32 = var0.m32 - var1.m32;
      var2.m33 = var0.m33 - var1.m33;
      return var2;
   }

   public static Matrix4f mul(Matrix4f var0, Matrix4f var1, Matrix4f var2) {
      if (var2 == null) {
         var2 = new Matrix4f();
      }

      float var3 = var0.m00 * var1.m00 + var0.m10 * var1.m01 + var0.m20 * var1.m02 + var0.m30 * var1.m03;
      float var4 = var0.m01 * var1.m00 + var0.m11 * var1.m01 + var0.m21 * var1.m02 + var0.m31 * var1.m03;
      float var5 = var0.m02 * var1.m00 + var0.m12 * var1.m01 + var0.m22 * var1.m02 + var0.m32 * var1.m03;
      float var6 = var0.m03 * var1.m00 + var0.m13 * var1.m01 + var0.m23 * var1.m02 + var0.m33 * var1.m03;
      float var7 = var0.m00 * var1.m10 + var0.m10 * var1.m11 + var0.m20 * var1.m12 + var0.m30 * var1.m13;
      float var8 = var0.m01 * var1.m10 + var0.m11 * var1.m11 + var0.m21 * var1.m12 + var0.m31 * var1.m13;
      float var9 = var0.m02 * var1.m10 + var0.m12 * var1.m11 + var0.m22 * var1.m12 + var0.m32 * var1.m13;
      float var10 = var0.m03 * var1.m10 + var0.m13 * var1.m11 + var0.m23 * var1.m12 + var0.m33 * var1.m13;
      float var11 = var0.m00 * var1.m20 + var0.m10 * var1.m21 + var0.m20 * var1.m22 + var0.m30 * var1.m23;
      float var12 = var0.m01 * var1.m20 + var0.m11 * var1.m21 + var0.m21 * var1.m22 + var0.m31 * var1.m23;
      float var13 = var0.m02 * var1.m20 + var0.m12 * var1.m21 + var0.m22 * var1.m22 + var0.m32 * var1.m23;
      float var14 = var0.m03 * var1.m20 + var0.m13 * var1.m21 + var0.m23 * var1.m22 + var0.m33 * var1.m23;
      float var15 = var0.m00 * var1.m30 + var0.m10 * var1.m31 + var0.m20 * var1.m32 + var0.m30 * var1.m33;
      float var16 = var0.m01 * var1.m30 + var0.m11 * var1.m31 + var0.m21 * var1.m32 + var0.m31 * var1.m33;
      float var17 = var0.m02 * var1.m30 + var0.m12 * var1.m31 + var0.m22 * var1.m32 + var0.m32 * var1.m33;
      float var18 = var0.m03 * var1.m30 + var0.m13 * var1.m31 + var0.m23 * var1.m32 + var0.m33 * var1.m33;
      var2.m00 = var3;
      var2.m01 = var4;
      var2.m02 = var5;
      var2.m03 = var6;
      var2.m10 = var7;
      var2.m11 = var8;
      var2.m12 = var9;
      var2.m13 = var10;
      var2.m20 = var11;
      var2.m21 = var12;
      var2.m22 = var13;
      var2.m23 = var14;
      var2.m30 = var15;
      var2.m31 = var16;
      var2.m32 = var17;
      var2.m33 = var18;
      return var2;
   }

   public static Vector4f transform(Matrix4f var0, Vector4f var1, Vector4f var2) {
      if (var2 == null) {
         var2 = new Vector4f();
      }

      float var3 = var0.m00 * var1.x + var0.m10 * var1.y + var0.m20 * var1.z + var0.m30 * var1.w;
      float var4 = var0.m01 * var1.x + var0.m11 * var1.y + var0.m21 * var1.z + var0.m31 * var1.w;
      float var5 = var0.m02 * var1.x + var0.m12 * var1.y + var0.m22 * var1.z + var0.m32 * var1.w;
      float var6 = var0.m03 * var1.x + var0.m13 * var1.y + var0.m23 * var1.z + var0.m33 * var1.w;
      var2.x = var3;
      var2.y = var4;
      var2.z = var5;
      var2.w = var6;
      return var2;
   }

   public Matrix transpose() {
      return this.transpose(this);
   }

   public Matrix4f translate(Vector2f var1) {
      return this.translate(var1, this);
   }

   public Matrix4f translate(Vector3f var1) {
      return this.translate(var1, this);
   }

   public Matrix4f scale(Vector3f var1) {
      return scale(var1, this, this);
   }

   public static Matrix4f scale(Vector3f var0, Matrix4f var1, Matrix4f var2) {
      if (var2 == null) {
         var2 = new Matrix4f();
      }

      var2.m00 = var1.m00 * var0.x;
      var2.m01 = var1.m01 * var0.x;
      var2.m02 = var1.m02 * var0.x;
      var2.m03 = var1.m03 * var0.x;
      var2.m10 = var1.m10 * var0.y;
      var2.m11 = var1.m11 * var0.y;
      var2.m12 = var1.m12 * var0.y;
      var2.m13 = var1.m13 * var0.y;
      var2.m20 = var1.m20 * var0.z;
      var2.m21 = var1.m21 * var0.z;
      var2.m22 = var1.m22 * var0.z;
      var2.m23 = var1.m23 * var0.z;
      return var2;
   }

   public Matrix4f rotate(float var1, Vector3f var2) {
      return this.rotate(var1, var2, this);
   }

   public Matrix4f rotate(float var1, Vector3f var2, Matrix4f var3) {
      return rotate(var1, var2, this, var3);
   }

   public static Matrix4f rotate(float var0, Vector3f var1, Matrix4f var2, Matrix4f var3) {
      if (var3 == null) {
         var3 = new Matrix4f();
      }

      float var4 = (float)Math.cos((double)var0);
      float var5 = (float)Math.sin((double)var0);
      float var6 = 1.0F - var4;
      float var7 = var1.x * var1.y;
      float var8 = var1.y * var1.z;
      float var9 = var1.x * var1.z;
      float var10 = var1.x * var5;
      float var11 = var1.y * var5;
      float var12 = var1.z * var5;
      float var13 = var1.x * var1.x * var6 + var4;
      float var14 = var7 * var6 + var12;
      float var15 = var9 * var6 - var11;
      float var16 = var7 * var6 - var12;
      float var17 = var1.y * var1.y * var6 + var4;
      float var18 = var8 * var6 + var10;
      float var19 = var9 * var6 + var11;
      float var20 = var8 * var6 - var10;
      float var21 = var1.z * var1.z * var6 + var4;
      float var22 = var2.m00 * var13 + var2.m10 * var14 + var2.m20 * var15;
      float var23 = var2.m01 * var13 + var2.m11 * var14 + var2.m21 * var15;
      float var24 = var2.m02 * var13 + var2.m12 * var14 + var2.m22 * var15;
      float var25 = var2.m03 * var13 + var2.m13 * var14 + var2.m23 * var15;
      float var26 = var2.m00 * var16 + var2.m10 * var17 + var2.m20 * var18;
      float var27 = var2.m01 * var16 + var2.m11 * var17 + var2.m21 * var18;
      float var28 = var2.m02 * var16 + var2.m12 * var17 + var2.m22 * var18;
      float var29 = var2.m03 * var16 + var2.m13 * var17 + var2.m23 * var18;
      var3.m20 = var2.m00 * var19 + var2.m10 * var20 + var2.m20 * var21;
      var3.m21 = var2.m01 * var19 + var2.m11 * var20 + var2.m21 * var21;
      var3.m22 = var2.m02 * var19 + var2.m12 * var20 + var2.m22 * var21;
      var3.m23 = var2.m03 * var19 + var2.m13 * var20 + var2.m23 * var21;
      var3.m00 = var22;
      var3.m01 = var23;
      var3.m02 = var24;
      var3.m03 = var25;
      var3.m10 = var26;
      var3.m11 = var27;
      var3.m12 = var28;
      var3.m13 = var29;
      return var3;
   }

   public Matrix4f translate(Vector3f var1, Matrix4f var2) {
      return translate(var1, this, var2);
   }

   public static Matrix4f translate(Vector3f var0, Matrix4f var1, Matrix4f var2) {
      if (var2 == null) {
         var2 = new Matrix4f();
      }

      var2.m30 += var1.m00 * var0.x + var1.m10 * var0.y + var1.m20 * var0.z;
      var2.m31 += var1.m01 * var0.x + var1.m11 * var0.y + var1.m21 * var0.z;
      var2.m32 += var1.m02 * var0.x + var1.m12 * var0.y + var1.m22 * var0.z;
      var2.m33 += var1.m03 * var0.x + var1.m13 * var0.y + var1.m23 * var0.z;
      return var2;
   }

   public Matrix4f translate(Vector2f var1, Matrix4f var2) {
      return translate(var1, this, var2);
   }

   public static Matrix4f translate(Vector2f var0, Matrix4f var1, Matrix4f var2) {
      if (var2 == null) {
         var2 = new Matrix4f();
      }

      var2.m30 += var1.m00 * var0.x + var1.m10 * var0.y;
      var2.m31 += var1.m01 * var0.x + var1.m11 * var0.y;
      var2.m32 += var1.m02 * var0.x + var1.m12 * var0.y;
      var2.m33 += var1.m03 * var0.x + var1.m13 * var0.y;
      return var2;
   }

   public Matrix4f transpose(Matrix4f var1) {
      return transpose(this, var1);
   }

   public static Matrix4f transpose(Matrix4f var0, Matrix4f var1) {
      if (var1 == null) {
         var1 = new Matrix4f();
      }

      float var2 = var0.m00;
      float var3 = var0.m10;
      float var4 = var0.m20;
      float var5 = var0.m30;
      float var6 = var0.m01;
      float var7 = var0.m11;
      float var8 = var0.m21;
      float var9 = var0.m31;
      float var10 = var0.m02;
      float var11 = var0.m12;
      float var12 = var0.m22;
      float var13 = var0.m32;
      float var14 = var0.m03;
      float var15 = var0.m13;
      float var16 = var0.m23;
      float var17 = var0.m33;
      var1.m00 = var2;
      var1.m01 = var3;
      var1.m02 = var4;
      var1.m03 = var5;
      var1.m10 = var6;
      var1.m11 = var7;
      var1.m12 = var8;
      var1.m13 = var9;
      var1.m20 = var10;
      var1.m21 = var11;
      var1.m22 = var12;
      var1.m23 = var13;
      var1.m30 = var14;
      var1.m31 = var15;
      var1.m32 = var16;
      var1.m33 = var17;
      return var1;
   }

   public float determinant() {
      float var1 = this.m00 * (this.m11 * this.m22 * this.m33 + this.m12 * this.m23 * this.m31 + this.m13 * this.m21 * this.m32 - this.m13 * this.m22 * this.m31 - this.m11 * this.m23 * this.m32 - this.m12 * this.m21 * this.m33);
      var1 -= this.m01 * (this.m10 * this.m22 * this.m33 + this.m12 * this.m23 * this.m30 + this.m13 * this.m20 * this.m32 - this.m13 * this.m22 * this.m30 - this.m10 * this.m23 * this.m32 - this.m12 * this.m20 * this.m33);
      var1 += this.m02 * (this.m10 * this.m21 * this.m33 + this.m11 * this.m23 * this.m30 + this.m13 * this.m20 * this.m31 - this.m13 * this.m21 * this.m30 - this.m10 * this.m23 * this.m31 - this.m11 * this.m20 * this.m33);
      var1 -= this.m03 * (this.m10 * this.m21 * this.m32 + this.m11 * this.m22 * this.m30 + this.m12 * this.m20 * this.m31 - this.m12 * this.m21 * this.m30 - this.m10 * this.m22 * this.m31 - this.m11 * this.m20 * this.m32);
      return var1;
   }

   private static float determinant3x3(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      return var0 * (var4 * var8 - var5 * var7) + var1 * (var5 * var6 - var3 * var8) + var2 * (var3 * var7 - var4 * var6);
   }

   public Matrix invert() {
      return invert(this, this);
   }

   public static Matrix4f invert(Matrix4f var0, Matrix4f var1) {
      float var2 = var0.determinant();
      if (var2 != 0.0F) {
         if (var1 == null) {
            var1 = new Matrix4f();
         }

         float var3 = 1.0F / var2;
         float var4 = determinant3x3(var0.m11, var0.m12, var0.m13, var0.m21, var0.m22, var0.m23, var0.m31, var0.m32, var0.m33);
         float var5 = -determinant3x3(var0.m10, var0.m12, var0.m13, var0.m20, var0.m22, var0.m23, var0.m30, var0.m32, var0.m33);
         float var6 = determinant3x3(var0.m10, var0.m11, var0.m13, var0.m20, var0.m21, var0.m23, var0.m30, var0.m31, var0.m33);
         float var7 = -determinant3x3(var0.m10, var0.m11, var0.m12, var0.m20, var0.m21, var0.m22, var0.m30, var0.m31, var0.m32);
         float var8 = -determinant3x3(var0.m01, var0.m02, var0.m03, var0.m21, var0.m22, var0.m23, var0.m31, var0.m32, var0.m33);
         float var9 = determinant3x3(var0.m00, var0.m02, var0.m03, var0.m20, var0.m22, var0.m23, var0.m30, var0.m32, var0.m33);
         float var10 = -determinant3x3(var0.m00, var0.m01, var0.m03, var0.m20, var0.m21, var0.m23, var0.m30, var0.m31, var0.m33);
         float var11 = determinant3x3(var0.m00, var0.m01, var0.m02, var0.m20, var0.m21, var0.m22, var0.m30, var0.m31, var0.m32);
         float var12 = determinant3x3(var0.m01, var0.m02, var0.m03, var0.m11, var0.m12, var0.m13, var0.m31, var0.m32, var0.m33);
         float var13 = -determinant3x3(var0.m00, var0.m02, var0.m03, var0.m10, var0.m12, var0.m13, var0.m30, var0.m32, var0.m33);
         float var14 = determinant3x3(var0.m00, var0.m01, var0.m03, var0.m10, var0.m11, var0.m13, var0.m30, var0.m31, var0.m33);
         float var15 = -determinant3x3(var0.m00, var0.m01, var0.m02, var0.m10, var0.m11, var0.m12, var0.m30, var0.m31, var0.m32);
         float var16 = -determinant3x3(var0.m01, var0.m02, var0.m03, var0.m11, var0.m12, var0.m13, var0.m21, var0.m22, var0.m23);
         float var17 = determinant3x3(var0.m00, var0.m02, var0.m03, var0.m10, var0.m12, var0.m13, var0.m20, var0.m22, var0.m23);
         float var18 = -determinant3x3(var0.m00, var0.m01, var0.m03, var0.m10, var0.m11, var0.m13, var0.m20, var0.m21, var0.m23);
         float var19 = determinant3x3(var0.m00, var0.m01, var0.m02, var0.m10, var0.m11, var0.m12, var0.m20, var0.m21, var0.m22);
         var1.m00 = var4 * var3;
         var1.m11 = var9 * var3;
         var1.m22 = var14 * var3;
         var1.m33 = var19 * var3;
         var1.m01 = var8 * var3;
         var1.m10 = var5 * var3;
         var1.m20 = var6 * var3;
         var1.m02 = var12 * var3;
         var1.m12 = var13 * var3;
         var1.m21 = var10 * var3;
         var1.m03 = var16 * var3;
         var1.m30 = var7 * var3;
         var1.m13 = var17 * var3;
         var1.m31 = var11 * var3;
         var1.m32 = var15 * var3;
         var1.m23 = var18 * var3;
         return var1;
      } else {
         return null;
      }
   }

   public Matrix negate() {
      return this.negate(this);
   }

   public Matrix4f negate(Matrix4f var1) {
      return negate(this, var1);
   }

   public static Matrix4f negate(Matrix4f var0, Matrix4f var1) {
      if (var1 == null) {
         var1 = new Matrix4f();
      }

      var1.m00 = -var0.m00;
      var1.m01 = -var0.m01;
      var1.m02 = -var0.m02;
      var1.m03 = -var0.m03;
      var1.m10 = -var0.m10;
      var1.m11 = -var0.m11;
      var1.m12 = -var0.m12;
      var1.m13 = -var0.m13;
      var1.m20 = -var0.m20;
      var1.m21 = -var0.m21;
      var1.m22 = -var0.m22;
      var1.m23 = -var0.m23;
      var1.m30 = -var0.m30;
      var1.m31 = -var0.m31;
      var1.m32 = -var0.m32;
      var1.m33 = -var0.m33;
      return var1;
   }
}
