package com.sun.scenario.effect.impl.state;

public class PerspectiveTransformState {
   private float[][] itx = new float[3][3];

   public float[][] getITX() {
      return this.itx;
   }

   public void updateTx(float[][] var1) {
      float var2 = get3x3Determinant(var1);
      if ((double)Math.abs(var2) < 1.0E-10) {
         this.itx[0][0] = this.itx[1][0] = this.itx[2][0] = 0.0F;
         this.itx[0][1] = this.itx[1][1] = this.itx[2][1] = 0.0F;
         this.itx[0][2] = this.itx[1][2] = -1.0F;
         this.itx[2][2] = 1.0F;
      } else {
         float var3 = 1.0F / var2;
         this.itx[0][0] = var3 * (var1[1][1] * var1[2][2] - var1[1][2] * var1[2][1]);
         this.itx[1][0] = var3 * (var1[1][2] * var1[2][0] - var1[1][0] * var1[2][2]);
         this.itx[2][0] = var3 * (var1[1][0] * var1[2][1] - var1[1][1] * var1[2][0]);
         this.itx[0][1] = var3 * (var1[0][2] * var1[2][1] - var1[0][1] * var1[2][2]);
         this.itx[1][1] = var3 * (var1[0][0] * var1[2][2] - var1[0][2] * var1[2][0]);
         this.itx[2][1] = var3 * (var1[0][1] * var1[2][0] - var1[0][0] * var1[2][1]);
         this.itx[0][2] = var3 * (var1[0][1] * var1[1][2] - var1[0][2] * var1[1][1]);
         this.itx[1][2] = var3 * (var1[0][2] * var1[1][0] - var1[0][0] * var1[1][2]);
         this.itx[2][2] = var3 * (var1[0][0] * var1[1][1] - var1[0][1] * var1[1][0]);
      }

   }

   private static float get3x3Determinant(float[][] var0) {
      return var0[0][0] * (var0[1][1] * var0[2][2] - var0[1][2] * var0[2][1]) - var0[0][1] * (var0[1][0] * var0[2][2] - var0[1][2] * var0[2][0]) + var0[0][2] * (var0[1][0] * var0[2][1] - var0[1][1] * var0[2][0]);
   }
}
