package com.sun.openpisces;

import java.util.Iterator;

final class Curve {
   float ax;
   float ay;
   float bx;
   float by;
   float cx;
   float cy;
   float dx;
   float dy;
   float dax;
   float day;
   float dbx;
   float dby;

   void set(float[] var1, int var2) {
      switch (var2) {
         case 6:
            this.set(var1[0], var1[1], var1[2], var1[3], var1[4], var1[5]);
            break;
         case 8:
            this.set(var1[0], var1[1], var1[2], var1[3], var1[4], var1[5], var1[6], var1[7]);
            break;
         default:
            throw new InternalError("Curves can only be cubic or quadratic");
      }

   }

   void set(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.ax = 3.0F * (var3 - var5) + var7 - var1;
      this.ay = 3.0F * (var4 - var6) + var8 - var2;
      this.bx = 3.0F * (var1 - 2.0F * var3 + var5);
      this.by = 3.0F * (var2 - 2.0F * var4 + var6);
      this.cx = 3.0F * (var3 - var1);
      this.cy = 3.0F * (var4 - var2);
      this.dx = var1;
      this.dy = var2;
      this.dax = 3.0F * this.ax;
      this.day = 3.0F * this.ay;
      this.dbx = 2.0F * this.bx;
      this.dby = 2.0F * this.by;
   }

   void set(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.ax = this.ay = 0.0F;
      this.bx = var1 - 2.0F * var3 + var5;
      this.by = var2 - 2.0F * var4 + var6;
      this.cx = 2.0F * (var3 - var1);
      this.cy = 2.0F * (var4 - var2);
      this.dx = var1;
      this.dy = var2;
      this.dax = 0.0F;
      this.day = 0.0F;
      this.dbx = 2.0F * this.bx;
      this.dby = 2.0F * this.by;
   }

   float xat(float var1) {
      return var1 * (var1 * (var1 * this.ax + this.bx) + this.cx) + this.dx;
   }

   float yat(float var1) {
      return var1 * (var1 * (var1 * this.ay + this.by) + this.cy) + this.dy;
   }

   float dxat(float var1) {
      return var1 * (var1 * this.dax + this.dbx) + this.cx;
   }

   float dyat(float var1) {
      return var1 * (var1 * this.day + this.dby) + this.cy;
   }

   int dxRoots(float[] var1, int var2) {
      return Helpers.quadraticRoots(this.dax, this.dbx, this.cx, var1, var2);
   }

   int dyRoots(float[] var1, int var2) {
      return Helpers.quadraticRoots(this.day, this.dby, this.cy, var1, var2);
   }

   int infPoints(float[] var1, int var2) {
      float var3 = this.dax * this.dby - this.dbx * this.day;
      float var4 = 2.0F * (this.cy * this.dax - this.day * this.cx);
      float var5 = this.cy * this.dbx - this.cx * this.dby;
      return Helpers.quadraticRoots(var3, var4, var5, var1, var2);
   }

   private int perpendiculardfddf(float[] var1, int var2) {
      assert var1.length >= var2 + 4;

      float var3 = 2.0F * (this.dax * this.dax + this.day * this.day);
      float var4 = 3.0F * (this.dax * this.dbx + this.day * this.dby);
      float var5 = 2.0F * (this.dax * this.cx + this.day * this.cy) + this.dbx * this.dbx + this.dby * this.dby;
      float var6 = this.dbx * this.cx + this.dby * this.cy;
      return Helpers.cubicRootsInAB(var3, var4, var5, var6, var1, var2, 0.0F, 1.0F);
   }

   int rootsOfROCMinusW(float[] var1, int var2, float var3, float var4) {
      assert var2 <= 6 && var1.length >= 10;

      int var5 = var2;
      int var6 = this.perpendiculardfddf(var1, var2);
      float var7 = 0.0F;
      float var8 = this.ROCsq(var7) - var3 * var3;
      var1[var2 + var6] = 1.0F;
      ++var6;

      for(int var9 = var2; var9 < var2 + var6; ++var9) {
         float var10 = var1[var9];
         float var11 = this.ROCsq(var10) - var3 * var3;
         if (var8 == 0.0F) {
            var1[var5++] = var7;
         } else if (var11 * var8 < 0.0F) {
            var1[var5++] = this.falsePositionROCsqMinusX(var7, var10, var3 * var3, var4);
         }

         var7 = var10;
         var8 = var11;
      }

      return var5 - var2;
   }

   private static float eliminateInf(float var0) {
      return var0 == Float.POSITIVE_INFINITY ? Float.MAX_VALUE : (var0 == Float.NEGATIVE_INFINITY ? Float.MIN_VALUE : var0);
   }

   private float falsePositionROCsqMinusX(float var1, float var2, float var3, float var4) {
      int var6 = 0;
      float var7 = var2;
      float var8 = eliminateInf(this.ROCsq(var2) - var3);
      float var9 = var1;
      float var10 = eliminateInf(this.ROCsq(var1) - var3);
      float var11 = var1;

      for(int var13 = 0; var13 < 100 && Math.abs(var7 - var9) > var4 * Math.abs(var7 + var9); ++var13) {
         var11 = (var10 * var7 - var8 * var9) / (var10 - var8);
         float var12 = this.ROCsq(var11) - var3;
         if (sameSign((double)var12, (double)var8)) {
            var8 = var12;
            var7 = var11;
            if (var6 < 0) {
               var10 /= (float)(1 << -var6);
               --var6;
            } else {
               var6 = -1;
            }
         } else {
            if (!(var12 * var10 > 0.0F)) {
               break;
            }

            var10 = var12;
            var9 = var11;
            if (var6 > 0) {
               var8 /= (float)(1 << var6);
               ++var6;
            } else {
               var6 = 1;
            }
         }
      }

      return var11;
   }

   private static boolean sameSign(double var0, double var2) {
      return var0 < 0.0 && var2 < 0.0 || var0 > 0.0 && var2 > 0.0;
   }

   private float ROCsq(float var1) {
      float var2 = var1 * (var1 * this.dax + this.dbx) + this.cx;
      float var3 = var1 * (var1 * this.day + this.dby) + this.cy;
      float var4 = 2.0F * this.dax * var1 + this.dbx;
      float var5 = 2.0F * this.day * var1 + this.dby;
      float var6 = var2 * var2 + var3 * var3;
      float var7 = var4 * var4 + var5 * var5;
      float var8 = var4 * var2 + var5 * var3;
      return var6 * (var6 * var6 / (var6 * var7 - var8 * var8));
   }

   static Iterator breakPtsAtTs(final float[] var0, final int var1, final float[] var2, final int var3) {
      assert var0.length >= 2 * var1 && var3 <= var2.length;

      return new Iterator() {
         final Integer i0 = 0;
         final Integer itype = var1;
         int nextCurveIdx = 0;
         Integer curCurveOff;
         float prevT;

         {
            this.curCurveOff = this.i0;
            this.prevT = 0.0F;
         }

         public boolean hasNext() {
            return this.nextCurveIdx < var3 + 1;
         }

         public Integer next() {
            Integer var1x;
            if (this.nextCurveIdx < var3) {
               float var2x = var2[this.nextCurveIdx];
               float var3x = (var2x - this.prevT) / (1.0F - this.prevT);
               Helpers.subdivideAt(var3x, var0, this.curCurveOff, var0, 0, var0, var1, var1);
               this.prevT = var2x;
               var1x = this.i0;
               this.curCurveOff = this.itype;
            } else {
               var1x = this.curCurveOff;
            }

            ++this.nextCurveIdx;
            return var1x;
         }

         public void remove() {
         }
      };
   }
}
