package org.lwjgl.util.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

public class Vector4f extends Vector implements Serializable, ReadableVector4f, WritableVector4f {
   private static final long serialVersionUID = 1L;
   public float x;
   public float y;
   public float z;
   public float w;

   public Vector4f() {
   }

   public Vector4f(ReadableVector4f var1) {
      this.set(var1);
   }

   public Vector4f(float var1, float var2, float var3, float var4) {
      this.set(var1, var2, var3, var4);
   }

   public void set(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   public void set(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public void set(float var1, float var2, float var3, float var4) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.w = var4;
   }

   public Vector4f set(ReadableVector4f var1) {
      this.x = var1.getX();
      this.y = var1.getY();
      this.z = var1.getZ();
      this.w = var1.getW();
      return this;
   }

   public float lengthSquared() {
      return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
   }

   public Vector4f translate(float var1, float var2, float var3, float var4) {
      this.x += var1;
      this.y += var2;
      this.z += var3;
      this.w += var4;
      return this;
   }

   public static Vector4f add(Vector4f var0, Vector4f var1, Vector4f var2) {
      if (var2 == null) {
         return new Vector4f(var0.x + var1.x, var0.y + var1.y, var0.z + var1.z, var0.w + var1.w);
      } else {
         var2.set(var0.x + var1.x, var0.y + var1.y, var0.z + var1.z, var0.w + var1.w);
         return var2;
      }
   }

   public static Vector4f sub(Vector4f var0, Vector4f var1, Vector4f var2) {
      if (var2 == null) {
         return new Vector4f(var0.x - var1.x, var0.y - var1.y, var0.z - var1.z, var0.w - var1.w);
      } else {
         var2.set(var0.x - var1.x, var0.y - var1.y, var0.z - var1.z, var0.w - var1.w);
         return var2;
      }
   }

   public Vector negate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
      this.w = -this.w;
      return this;
   }

   public Vector4f negate(Vector4f var1) {
      if (var1 == null) {
         var1 = new Vector4f();
      }

      var1.x = -this.x;
      var1.y = -this.y;
      var1.z = -this.z;
      var1.w = -this.w;
      return var1;
   }

   public Vector4f normalise(Vector4f var1) {
      float var2 = this.length();
      if (var1 == null) {
         var1 = new Vector4f(this.x / var2, this.y / var2, this.z / var2, this.w / var2);
      } else {
         var1.set(this.x / var2, this.y / var2, this.z / var2, this.w / var2);
      }

      return var1;
   }

   public static float dot(Vector4f var0, Vector4f var1) {
      return var0.x * var1.x + var0.y * var1.y + var0.z * var1.z + var0.w * var1.w;
   }

   public static float angle(Vector4f var0, Vector4f var1) {
      float var2 = dot(var0, var1) / (var0.length() * var1.length());
      if (var2 < -1.0F) {
         var2 = -1.0F;
      } else if (var2 > 1.0F) {
         var2 = 1.0F;
      }

      return (float)Math.acos((double)var2);
   }

   public Vector load(FloatBuffer var1) {
      this.x = var1.get();
      this.y = var1.get();
      this.z = var1.get();
      this.w = var1.get();
      return this;
   }

   public Vector scale(float var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      this.w *= var1;
      return this;
   }

   public Vector store(FloatBuffer var1) {
      var1.put(this.x);
      var1.put(this.y);
      var1.put(this.z);
      var1.put(this.w);
      return this;
   }

   public String toString() {
      return "Vector4f: " + this.x + " " + this.y + " " + this.z + " " + this.w;
   }

   public final float getX() {
      return this.x;
   }

   public final float getY() {
      return this.y;
   }

   public final void setX(float var1) {
      this.x = var1;
   }

   public final void setY(float var1) {
      this.y = var1;
   }

   public void setZ(float var1) {
      this.z = var1;
   }

   public float getZ() {
      return this.z;
   }

   public void setW(float var1) {
      this.w = var1;
   }

   public float getW() {
      return this.w;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         Vector4f var2 = (Vector4f)var1;
         return this.x == var2.x && this.y == var2.y && this.z == var2.z && this.w == var2.w;
      }
   }
}
