package org.lwjgl.util.vector;

import java.nio.FloatBuffer;

public class Quaternion extends Vector implements ReadableVector4f {
   private static final long serialVersionUID = 1L;
   public float x;
   public float y;
   public float z;
   public float w;

   public Quaternion() {
      this.setIdentity();
   }

   public Quaternion(ReadableVector4f var1) {
      this.set(var1);
   }

   public Quaternion(float var1, float var2, float var3, float var4) {
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

   public Quaternion set(ReadableVector4f var1) {
      this.x = var1.getX();
      this.y = var1.getY();
      this.z = var1.getZ();
      this.w = var1.getW();
      return this;
   }

   public Quaternion setIdentity() {
      return setIdentity(this);
   }

   public static Quaternion setIdentity(Quaternion var0) {
      var0.x = 0.0F;
      var0.y = 0.0F;
      var0.z = 0.0F;
      var0.w = 1.0F;
      return var0;
   }

   public float lengthSquared() {
      return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
   }

   public static Quaternion normalise(Quaternion var0, Quaternion var1) {
      float var2 = 1.0F / var0.length();
      if (var1 == null) {
         var1 = new Quaternion();
      }

      var1.set(var0.x * var2, var0.y * var2, var0.z * var2, var0.w * var2);
      return var1;
   }

   public Quaternion normalise(Quaternion var1) {
      return normalise(this, var1);
   }

   public static float dot(Quaternion var0, Quaternion var1) {
      return var0.x * var1.x + var0.y * var1.y + var0.z * var1.z + var0.w * var1.w;
   }

   public Quaternion negate(Quaternion var1) {
      return negate(this, var1);
   }

   public static Quaternion negate(Quaternion var0, Quaternion var1) {
      if (var1 == null) {
         var1 = new Quaternion();
      }

      var1.x = -var0.x;
      var1.y = -var0.y;
      var1.z = -var0.z;
      var1.w = var0.w;
      return var1;
   }

   public Vector negate() {
      return negate(this, this);
   }

   public Vector load(FloatBuffer var1) {
      this.x = var1.get();
      this.y = var1.get();
      this.z = var1.get();
      this.w = var1.get();
      return this;
   }

   public Vector scale(float var1) {
      return scale(var1, this, this);
   }

   public static Quaternion scale(float var0, Quaternion var1, Quaternion var2) {
      if (var2 == null) {
         var2 = new Quaternion();
      }

      var2.x = var1.x * var0;
      var2.y = var1.y * var0;
      var2.z = var1.z * var0;
      var2.w = var1.w * var0;
      return var2;
   }

   public Vector store(FloatBuffer var1) {
      var1.put(this.x);
      var1.put(this.y);
      var1.put(this.z);
      var1.put(this.w);
      return this;
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

   public String toString() {
      return "Quaternion: " + this.x + " " + this.y + " " + this.z + " " + this.w;
   }

   public static Quaternion mul(Quaternion var0, Quaternion var1, Quaternion var2) {
      if (var2 == null) {
         var2 = new Quaternion();
      }

      var2.set(var0.x * var1.w + var0.w * var1.x + var0.y * var1.z - var0.z * var1.y, var0.y * var1.w + var0.w * var1.y + var0.z * var1.x - var0.x * var1.z, var0.z * var1.w + var0.w * var1.z + var0.x * var1.y - var0.y * var1.x, var0.w * var1.w - var0.x * var1.x - var0.y * var1.y - var0.z * var1.z);
      return var2;
   }

   public static Quaternion mulInverse(Quaternion var0, Quaternion var1, Quaternion var2) {
      float var3 = var1.lengthSquared();
      var3 = (double)var3 == 0.0D ? var3 : 1.0F / var3;
      if (var2 == null) {
         var2 = new Quaternion();
      }

      var2.set((var0.x * var1.w - var0.w * var1.x - var0.y * var1.z + var0.z * var1.y) * var3, (var0.y * var1.w - var0.w * var1.y - var0.z * var1.x + var0.x * var1.z) * var3, (var0.z * var1.w - var0.w * var1.z - var0.x * var1.y + var0.y * var1.x) * var3, (var0.w * var1.w + var0.x * var1.x + var0.y * var1.y + var0.z * var1.z) * var3);
      return var2;
   }

   public final void setFromAxisAngle(Vector4f var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      float var2 = (float)Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z));
      float var3 = (float)(Math.sin(0.5D * (double)var1.w) / (double)var2);
      this.x *= var3;
      this.y *= var3;
      this.z *= var3;
      this.w = (float)Math.cos(0.5D * (double)var1.w);
   }

   public final Quaternion setFromMatrix(Matrix4f var1) {
      return setFromMatrix(var1, this);
   }

   public static Quaternion setFromMatrix(Matrix4f var0, Quaternion var1) {
      return var1.setFromMat(var0.m00, var0.m01, var0.m02, var0.m10, var0.m11, var0.m12, var0.m20, var0.m21, var0.m22);
   }

   public final Quaternion setFromMatrix(Matrix3f var1) {
      return setFromMatrix(var1, this);
   }

   public static Quaternion setFromMatrix(Matrix3f var0, Quaternion var1) {
      return var1.setFromMat(var0.m00, var0.m01, var0.m02, var0.m10, var0.m11, var0.m12, var0.m20, var0.m21, var0.m22);
   }

   private Quaternion setFromMat(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      float var11 = var1 + var5 + var9;
      float var10;
      if ((double)var11 >= 0.0D) {
         var10 = (float)Math.sqrt((double)var11 + 1.0D);
         this.w = var10 * 0.5F;
         var10 = 0.5F / var10;
         this.x = (var8 - var6) * var10;
         this.y = (var3 - var7) * var10;
         this.z = (var4 - var2) * var10;
      } else {
         float var12 = Math.max(Math.max(var1, var5), var9);
         if (var12 == var1) {
            var10 = (float)Math.sqrt((double)(var1 - (var5 + var9)) + 1.0D);
            this.x = var10 * 0.5F;
            var10 = 0.5F / var10;
            this.y = (var2 + var4) * var10;
            this.z = (var7 + var3) * var10;
            this.w = (var8 - var6) * var10;
         } else if (var12 == var5) {
            var10 = (float)Math.sqrt((double)(var5 - (var9 + var1)) + 1.0D);
            this.y = var10 * 0.5F;
            var10 = 0.5F / var10;
            this.z = (var6 + var8) * var10;
            this.x = (var2 + var4) * var10;
            this.w = (var3 - var7) * var10;
         } else {
            var10 = (float)Math.sqrt((double)(var9 - (var1 + var5)) + 1.0D);
            this.z = var10 * 0.5F;
            var10 = 0.5F / var10;
            this.x = (var7 + var3) * var10;
            this.y = (var6 + var8) * var10;
            this.w = (var4 - var2) * var10;
         }
      }

      return this;
   }
}
