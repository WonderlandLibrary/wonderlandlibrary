package com.sun.javafx.geom;

public class Quat4f {
   static final double EPS2 = 1.0E-30;
   public float x;
   public float y;
   public float z;
   public float w;

   public Quat4f() {
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 0.0F;
      this.w = 0.0F;
   }

   public Quat4f(float var1, float var2, float var3, float var4) {
      float var5 = (float)(1.0 / Math.sqrt((double)(var1 * var1 + var2 * var2 + var3 * var3 + var4 * var4)));
      this.x = var1 * var5;
      this.y = var2 * var5;
      this.z = var3 * var5;
      this.w = var4 * var5;
   }

   public Quat4f(float[] var1) {
      float var2 = (float)(1.0 / Math.sqrt((double)(var1[0] * var1[0] + var1[1] * var1[1] + var1[2] * var1[2] + var1[3] * var1[3])));
      this.x = var1[0] * var2;
      this.y = var1[1] * var2;
      this.z = var1[2] * var2;
      this.w = var1[3] * var2;
   }

   public Quat4f(Quat4f var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = var1.w;
   }

   public final void normalize() {
      float var1 = this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
      if (var1 > 0.0F) {
         var1 = 1.0F / (float)Math.sqrt((double)var1);
         this.x *= var1;
         this.y *= var1;
         this.z *= var1;
         this.w *= var1;
      } else {
         this.x = 0.0F;
         this.y = 0.0F;
         this.z = 0.0F;
         this.w = 0.0F;
      }

   }

   public final void set(Matrix3f var1) {
      float var2 = 0.25F * (var1.m00 + var1.m11 + var1.m22 + 1.0F);
      if (var2 >= 0.0F) {
         if ((double)var2 >= 1.0E-30) {
            this.w = (float)Math.sqrt((double)var2);
            var2 = 0.25F / this.w;
            this.x = (var1.m21 - var1.m12) * var2;
            this.y = (var1.m02 - var1.m20) * var2;
            this.z = (var1.m10 - var1.m01) * var2;
         } else {
            this.w = 0.0F;
            var2 = -0.5F * (var1.m11 + var1.m22);
            if (var2 >= 0.0F) {
               if ((double)var2 >= 1.0E-30) {
                  this.x = (float)Math.sqrt((double)var2);
                  var2 = 0.5F / this.x;
                  this.y = var1.m10 * var2;
                  this.z = var1.m20 * var2;
               } else {
                  this.x = 0.0F;
                  var2 = 0.5F * (1.0F - var1.m22);
                  if ((double)var2 >= 1.0E-30) {
                     this.y = (float)Math.sqrt((double)var2);
                     this.z = var1.m21 / (2.0F * this.y);
                  } else {
                     this.y = 0.0F;
                     this.z = 1.0F;
                  }
               }
            } else {
               this.x = 0.0F;
               this.y = 0.0F;
               this.z = 1.0F;
            }
         }
      } else {
         this.w = 0.0F;
         this.x = 0.0F;
         this.y = 0.0F;
         this.z = 1.0F;
      }
   }

   public final void set(float[][] var1) {
      float var2 = 0.25F * (var1[0][0] + var1[1][1] + var1[2][2] + 1.0F);
      if (var2 >= 0.0F) {
         if ((double)var2 >= 1.0E-30) {
            this.w = (float)Math.sqrt((double)var2);
            var2 = 0.25F / this.w;
            this.x = (var1[2][1] - var1[1][2]) * var2;
            this.y = (var1[0][2] - var1[2][0]) * var2;
            this.z = (var1[1][0] - var1[0][1]) * var2;
         } else {
            this.w = 0.0F;
            var2 = -0.5F * (var1[1][1] + var1[2][2]);
            if (var2 >= 0.0F) {
               if ((double)var2 >= 1.0E-30) {
                  this.x = (float)Math.sqrt((double)var2);
                  var2 = 0.5F / this.x;
                  this.y = var1[1][0] * var2;
                  this.z = var1[2][0] * var2;
               } else {
                  this.x = 0.0F;
                  var2 = 0.5F * (1.0F - var1[2][2]);
                  if ((double)var2 >= 1.0E-30) {
                     this.y = (float)Math.sqrt((double)var2);
                     this.z = var1[2][1] / (2.0F * this.y);
                  } else {
                     this.y = 0.0F;
                     this.z = 1.0F;
                  }
               }
            } else {
               this.x = 0.0F;
               this.y = 0.0F;
               this.z = 1.0F;
            }
         }
      } else {
         this.w = 0.0F;
         this.x = 0.0F;
         this.y = 0.0F;
         this.z = 1.0F;
      }
   }

   public final void scale(float var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      this.w *= var1;
   }

   public String toString() {
      return "Quat4f[" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + "]";
   }
}
