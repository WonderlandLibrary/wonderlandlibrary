package org.lwjgl.util.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

public class Vector2f extends Vector implements Serializable, ReadableVector2f, WritableVector2f {
   private static final long serialVersionUID = 1L;
   public float x;
   public float y;

   public Vector2f() {
   }

   public Vector2f(ReadableVector2f var1) {
      this.set(var1);
   }

   public Vector2f(float var1, float var2) {
      this.set(var1, var2);
   }

   public void set(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   public Vector2f set(ReadableVector2f var1) {
      this.x = var1.getX();
      this.y = var1.getY();
      return this;
   }

   public float lengthSquared() {
      return this.x * this.x + this.y * this.y;
   }

   public Vector2f translate(float var1, float var2) {
      this.x += var1;
      this.y += var2;
      return this;
   }

   public Vector negate() {
      this.x = -this.x;
      this.y = -this.y;
      return this;
   }

   public Vector2f negate(Vector2f var1) {
      if (var1 == null) {
         var1 = new Vector2f();
      }

      var1.x = -this.x;
      var1.y = -this.y;
      return var1;
   }

   public Vector2f normalise(Vector2f var1) {
      float var2 = this.length();
      if (var1 == null) {
         var1 = new Vector2f(this.x / var2, this.y / var2);
      } else {
         var1.set(this.x / var2, this.y / var2);
      }

      return var1;
   }

   public static float dot(Vector2f var0, Vector2f var1) {
      return var0.x * var1.x + var0.y * var1.y;
   }

   public static float angle(Vector2f var0, Vector2f var1) {
      float var2 = dot(var0, var1) / (var0.length() * var1.length());
      if (var2 < -1.0F) {
         var2 = -1.0F;
      } else if (var2 > 1.0F) {
         var2 = 1.0F;
      }

      return (float)Math.acos((double)var2);
   }

   public static Vector2f add(Vector2f var0, Vector2f var1, Vector2f var2) {
      if (var2 == null) {
         return new Vector2f(var0.x + var1.x, var0.y + var1.y);
      } else {
         var2.set(var0.x + var1.x, var0.y + var1.y);
         return var2;
      }
   }

   public static Vector2f sub(Vector2f var0, Vector2f var1, Vector2f var2) {
      if (var2 == null) {
         return new Vector2f(var0.x - var1.x, var0.y - var1.y);
      } else {
         var2.set(var0.x - var1.x, var0.y - var1.y);
         return var2;
      }
   }

   public Vector store(FloatBuffer var1) {
      var1.put(this.x);
      var1.put(this.y);
      return this;
   }

   public Vector load(FloatBuffer var1) {
      this.x = var1.get();
      this.y = var1.get();
      return this;
   }

   public Vector scale(float var1) {
      this.x *= var1;
      this.y *= var1;
      return this;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(64);
      var1.append("Vector2f[");
      var1.append(this.x);
      var1.append(", ");
      var1.append(this.y);
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

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         Vector2f var2 = (Vector2f)var1;
         return this.x == var2.x && this.y == var2.y;
      }
   }
}
