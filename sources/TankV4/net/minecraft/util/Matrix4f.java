package net.minecraft.util;

public class Matrix4f extends org.lwjgl.util.vector.Matrix4f {
   public Matrix4f() {
      this.m00 = this.m01 = this.m02 = this.m03 = this.m10 = this.m11 = this.m12 = this.m13 = this.m20 = this.m21 = this.m22 = this.m23 = this.m30 = this.m31 = this.m32 = this.m33 = 0.0F;
   }

   public Matrix4f(float[] var1) {
      this.m00 = var1[0];
      this.m01 = var1[1];
      this.m02 = var1[2];
      this.m03 = var1[3];
      this.m10 = var1[4];
      this.m11 = var1[5];
      this.m12 = var1[6];
      this.m13 = var1[7];
      this.m20 = var1[8];
      this.m21 = var1[9];
      this.m22 = var1[10];
      this.m23 = var1[11];
      this.m30 = var1[12];
      this.m31 = var1[13];
      this.m32 = var1[14];
      this.m33 = var1[15];
   }
}
