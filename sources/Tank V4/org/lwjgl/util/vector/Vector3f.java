package org.lwjgl.util.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

public class Vector3f extends Vector implements Serializable, ReadableVector3f, WritableVector3f {
   private static final long serialVersionUID = 1L;
   public float x;
   public float y;
   public float z;

   public Vector3f() {
   }

   public Vector3f(ReadableVector3f var1) {
      this.set(var1);
   }

   public Vector3f(float var1, float var2, float var3) {
      this.set(var1, var2, var3);
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

   public Vector3f set(ReadableVector3f var1) {
      this.x = var1.getX();
      this.y = var1.getY();
      this.z = var1.getZ();
      return this;
   }

   public float lengthSquared() {
      return this.x * this.x + this.y * this.y + this.z * this.z;
   }

   public Vector3f translate(float var1, float var2, float var3) {
      this.x += var1;
      this.y += var2;
      this.z += var3;
      return this;
   }

   public static Vector3f add(Vector3f var0, Vector3f var1, Vector3f var2) {
      if (var2 == null) {
         return new Vector3f(var0.x + var1.x, var0.y + var1.y, var0.z + var1.z);
      } else {
         var2.set(var0.x + var1.x, var0.y + var1.y, var0.z + var1.z);
         return var2;
      }
   }

   public static Vector3f sub(Vector3f var0, Vector3f var1, Vector3f var2) {
      if (var2 == null) {
         return new Vector3f(var0.x - var1.x, var0.y - var1.y, var0.z - var1.z);
      } else {
         var2.set(var0.x - var1.x, var0.y - var1.y, var0.z - var1.z);
         return var2;
      }
   }

   public static Vector3f cross(Vector3f var0, Vector3f var1, Vector3f var2) {
      if (var2 == null) {
         var2 = new Vector3f();
      }

      var2.set(var0.y * var1.z - var0.z * var1.y, var1.x * var0.z - var1.z * var0.x, var0.x * var1.y - var0.y * var1.x);
      return var2;
   }

   public Vector negate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
      return this;
   }

   public Vector3f negate(Vector3f var1) {
      if (var1 == null) {
         var1 = new Vector3f();
      }

      var1.x = -this.x;
      var1.y = -this.y;
      var1.z = -this.z;
      return var1;
   }

   public Vector3f normalise(Vector3f var1) {
      float var2 = this.length();
      if (var1 == null) {
         var1 = new Vector3f(this.x / var2, this.y / var2, this.z / var2);
      } else {
         var1.set(this.x / var2, this.y / var2, this.z / var2);
      }

      return var1;
   }

   public static float dot(Vector3f var0, Vector3f var1) {
      return var0.x * var1.x + var0.y * var1.y + var0.z * var1.z;
   }

   public static float angle(Vector3f var0, Vector3f var1) {
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
      return this;
   }

   public Vector scale(float var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      return this;
   }

   public Vector store(FloatBuffer var1) {
      var1.put(this.x);
      var1.put(this.y);
      var1.put(this.z);
      return this;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(64);
      var1.append("Vector3f[");
      var1.append(this.x);
      var1.append(", ");
      var1.append(this.y);
      var1.append(", ");
      var1.append(this.z);
      var1.append(']');
      return var1.toString();
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

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         Vector3f var2 = (Vector3f)var1;
         return this.x == var2.x && this.y == var2.y && this.z == var2.z;
      }
   }
}
