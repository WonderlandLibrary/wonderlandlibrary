package org.lwjgl.util.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

public class Matrix2f extends Matrix implements Serializable {
   private static final long serialVersionUID = 1L;
   public float m00;
   public float m01;
   public float m10;
   public float m11;

   public Matrix2f() {
      this.setIdentity();
   }

   public Matrix2f(Matrix2f var1) {
      this.load(var1);
   }

   public Matrix2f load(Matrix2f var1) {
      return load(var1, this);
   }

   public static Matrix2f load(Matrix2f var0, Matrix2f var1) {
      if (var1 == null) {
         var1 = new Matrix2f();
      }

      var1.m00 = var0.m00;
      var1.m01 = var0.m01;
      var1.m10 = var0.m10;
      var1.m11 = var0.m11;
      return var1;
   }

   public Matrix load(FloatBuffer var1) {
      this.m00 = var1.get();
      this.m01 = var1.get();
      this.m10 = var1.get();
      this.m11 = var1.get();
      return this;
   }

   public Matrix loadTranspose(FloatBuffer var1) {
      this.m00 = var1.get();
      this.m10 = var1.get();
      this.m01 = var1.get();
      this.m11 = var1.get();
      return this;
   }

   public Matrix store(FloatBuffer var1) {
      var1.put(this.m00);
      var1.put(this.m01);
      var1.put(this.m10);
      var1.put(this.m11);
      return this;
   }

   public Matrix storeTranspose(FloatBuffer var1) {
      var1.put(this.m00);
      var1.put(this.m10);
      var1.put(this.m01);
      var1.put(this.m11);
      return this;
   }

   public static Matrix2f add(Matrix2f var0, Matrix2f var1, Matrix2f var2) {
      if (var2 == null) {
         var2 = new Matrix2f();
      }

      var2.m00 = var0.m00 + var1.m00;
      var2.m01 = var0.m01 + var1.m01;
      var2.m10 = var0.m10 + var1.m10;
      var2.m11 = var0.m11 + var1.m11;
      return var2;
   }

   public static Matrix2f sub(Matrix2f var0, Matrix2f var1, Matrix2f var2) {
      if (var2 == null) {
         var2 = new Matrix2f();
      }

      var2.m00 = var0.m00 - var1.m00;
      var2.m01 = var0.m01 - var1.m01;
      var2.m10 = var0.m10 - var1.m10;
      var2.m11 = var0.m11 - var1.m11;
      return var2;
   }

   public static Matrix2f mul(Matrix2f var0, Matrix2f var1, Matrix2f var2) {
      if (var2 == null) {
         var2 = new Matrix2f();
      }

      float var3 = var0.m00 * var1.m00 + var0.m10 * var1.m01;
      float var4 = var0.m01 * var1.m00 + var0.m11 * var1.m01;
      float var5 = var0.m00 * var1.m10 + var0.m10 * var1.m11;
      float var6 = var0.m01 * var1.m10 + var0.m11 * var1.m11;
      var2.m00 = var3;
      var2.m01 = var4;
      var2.m10 = var5;
      var2.m11 = var6;
      return var2;
   }

   public static Vector2f transform(Matrix2f var0, Vector2f var1, Vector2f var2) {
      if (var2 == null) {
         var2 = new Vector2f();
      }

      float var3 = var0.m00 * var1.x + var0.m10 * var1.y;
      float var4 = var0.m01 * var1.x + var0.m11 * var1.y;
      var2.x = var3;
      var2.y = var4;
      return var2;
   }

   public Matrix transpose() {
      return this.transpose(this);
   }

   public Matrix2f transpose(Matrix2f var1) {
      return transpose(this, var1);
   }

   public static Matrix2f transpose(Matrix2f var0, Matrix2f var1) {
      if (var1 == null) {
         var1 = new Matrix2f();
      }

      float var2 = var0.m10;
      float var3 = var0.m01;
      var1.m01 = var2;
      var1.m10 = var3;
      return var1;
   }

   public Matrix invert() {
      return invert(this, this);
   }

   public static Matrix2f invert(Matrix2f var0, Matrix2f var1) {
      float var2 = var0.determinant();
      if (var2 != 0.0F) {
         if (var1 == null) {
            var1 = new Matrix2f();
         }

         float var3 = 1.0F / var2;
         float var4 = var0.m11 * var3;
         float var5 = -var0.m01 * var3;
         float var6 = var0.m00 * var3;
         float var7 = -var0.m10 * var3;
         var1.m00 = var4;
         var1.m01 = var5;
         var1.m10 = var7;
         var1.m11 = var6;
         return var1;
      } else {
         return null;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.m00).append(' ').append(this.m10).append(' ').append('\n');
      var1.append(this.m01).append(' ').append(this.m11).append(' ').append('\n');
      return var1.toString();
   }

   public Matrix negate() {
      return this.negate(this);
   }

   public Matrix2f negate(Matrix2f var1) {
      return negate(this, var1);
   }

   public static Matrix2f negate(Matrix2f var0, Matrix2f var1) {
      if (var1 == null) {
         var1 = new Matrix2f();
      }

      var1.m00 = -var0.m00;
      var1.m01 = -var0.m01;
      var1.m10 = -var0.m10;
      var1.m11 = -var0.m11;
      return var1;
   }

   public Matrix setIdentity() {
      return setIdentity(this);
   }

   public static Matrix2f setIdentity(Matrix2f var0) {
      var0.m00 = 1.0F;
      var0.m01 = 0.0F;
      var0.m10 = 0.0F;
      var0.m11 = 1.0F;
      return var0;
   }

   public Matrix setZero() {
      return setZero(this);
   }

   public static Matrix2f setZero(Matrix2f var0) {
      var0.m00 = 0.0F;
      var0.m01 = 0.0F;
      var0.m10 = 0.0F;
      var0.m11 = 0.0F;
      return var0;
   }

   public float determinant() {
      return this.m00 * this.m11 - this.m01 * this.m10;
   }
}
