package com.sun.webkit.graphics;

import java.util.Arrays;

public final class WCTransform extends Ref {
   private final double[] m;
   private final boolean is3D;

   public WCTransform(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23, double var25, double var27, double var29, double var31) {
      this.m = new double[16];
      this.m[0] = var1;
      this.m[1] = var9;
      this.m[2] = var17;
      this.m[3] = var25;
      this.m[4] = var3;
      this.m[5] = var11;
      this.m[6] = var19;
      this.m[7] = var27;
      this.m[8] = var5;
      this.m[9] = var13;
      this.m[10] = var21;
      this.m[11] = var29;
      this.m[12] = var7;
      this.m[13] = var15;
      this.m[14] = var23;
      this.m[15] = var31;
      this.is3D = true;
   }

   public WCTransform(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.m = new double[6];
      this.m[0] = var1;
      this.m[1] = var3;
      this.m[2] = var5;
      this.m[3] = var7;
      this.m[4] = var9;
      this.m[5] = var11;
      this.is3D = false;
   }

   public double[] getMatrix() {
      return Arrays.copyOf(this.m, this.m.length);
   }

   public String toString() {
      String var1 = "WCTransform:";
      if (this.is3D) {
         var1 = var1 + "(" + this.m[0] + "," + this.m[1] + "," + this.m[2] + "," + this.m[3] + ")(" + this.m[4] + "," + this.m[5] + "," + this.m[6] + "," + this.m[7] + ")(" + this.m[8] + "," + this.m[9] + "," + this.m[10] + "," + this.m[11] + ")(" + this.m[12] + "," + this.m[13] + "," + this.m[14] + "," + this.m[15] + ")";
      } else {
         var1 = var1 + "(" + this.m[0] + "," + this.m[1] + "," + this.m[2] + ")(" + this.m[3] + "," + this.m[4] + "," + this.m[5] + ")";
      }

      return var1;
   }
}
