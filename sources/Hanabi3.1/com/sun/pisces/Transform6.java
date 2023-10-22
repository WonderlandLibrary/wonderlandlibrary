package com.sun.pisces;

public final class Transform6 {
   public int m00;
   public int m01;
   public int m10;
   public int m11;
   public int m02;
   public int m12;

   public Transform6() {
      this(65536, 0, 0, 65536, 0, 0);
   }

   public Transform6(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.initialize();
      this.m00 = var1;
      this.m01 = var2;
      this.m10 = var3;
      this.m11 = var4;
      this.m02 = var5;
      this.m12 = var6;
   }

   public Transform6(Transform6 var1) {
      this(var1.m00, var1.m01, var1.m10, var1.m11, var1.m02, var1.m12);
   }

   public void postMultiply(Transform6 var1) {
      long var2 = (long)this.m00 * (long)var1.m00 + (long)this.m01 * (long)var1.m10 >> 16;
      long var4 = (long)this.m00 * (long)var1.m01 + (long)this.m01 * (long)var1.m11 >> 16;
      long var6 = (long)this.m10 * (long)var1.m00 + (long)this.m11 * (long)var1.m10 >> 16;
      long var8 = (long)this.m10 * (long)var1.m01 + (long)this.m11 * (long)var1.m11 >> 16;
      long var10 = ((long)this.m02 << 16) + (long)this.m00 * (long)var1.m02 + (long)this.m01 * (long)var1.m12 >> 16;
      long var12 = ((long)this.m12 << 16) + (long)this.m10 * (long)var1.m02 + (long)this.m11 * (long)var1.m12 >> 16;
      this.m00 = (int)var2;
      this.m01 = (int)var4;
      this.m02 = (int)var10;
      this.m10 = (int)var6;
      this.m11 = (int)var8;
      this.m12 = (int)var12;
   }

   public Transform6 inverse() {
      float var1 = (float)this.m00 / 65536.0F;
      float var2 = (float)this.m01 / 65536.0F;
      float var3 = (float)this.m02 / 65536.0F;
      float var4 = (float)this.m10 / 65536.0F;
      float var5 = (float)this.m11 / 65536.0F;
      float var6 = (float)this.m12 / 65536.0F;
      float var7 = var1 * var5 - var2 * var4;
      float var8 = var5 / var7;
      float var9 = -var2 / var7;
      float var10 = -var4 / var7;
      float var11 = var1 / var7;
      float var12 = (var2 * var6 - var3 * var5) / var7;
      float var13 = (var3 * var4 - var1 * var6) / var7;
      int var14 = (int)((double)var8 * 65536.0);
      int var15 = (int)(var9 * 65536.0F);
      int var16 = (int)(var10 * 65536.0F);
      int var17 = (int)(var11 * 65536.0F);
      int var18 = (int)(var12 * 65536.0F);
      int var19 = (int)(var13 * 65536.0F);
      return new Transform6(var14, var15, var16, var17, var18, var19);
   }

   public boolean isIdentity() {
      return this.m00 == 65536 && this.m01 == 0 && this.m10 == 0 && this.m11 == 65536 && this.m02 == 0 && this.m12 == 0;
   }

   public Transform6 setTransform(Transform6 var1) {
      this.m00 = var1.m00;
      this.m10 = var1.m10;
      this.m01 = var1.m01;
      this.m11 = var1.m11;
      this.m02 = var1.m02;
      this.m12 = var1.m12;
      return this;
   }

   public String toString() {
      return "Transform6[m00=" + (double)this.m00 / 65536.0 + ", m01=" + (double)this.m01 / 65536.0 + ", m02=" + (double)this.m02 / 65536.0 + ", m10=" + (double)this.m10 / 65536.0 + ", m11=" + (double)this.m11 / 65536.0 + ", m12=" + (double)this.m12 / 65536.0 + "]";
   }

   private native void initialize();
}
