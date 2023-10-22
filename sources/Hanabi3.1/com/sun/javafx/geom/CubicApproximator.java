package com.sun.javafx.geom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CubicApproximator {
   private float accuracy;
   private float maxCubicSize;

   public CubicApproximator(float var1, float var2) {
      this.accuracy = var1;
      this.maxCubicSize = var2;
   }

   public void setAccuracy(float var1) {
      this.accuracy = var1;
   }

   public float getAccuracy() {
      return this.accuracy;
   }

   public void setMaxCubicSize(float var1) {
      this.maxCubicSize = var1;
   }

   public float getMaxCubicSize() {
      return this.maxCubicSize;
   }

   public float approximate(List var1, List var2, CubicCurve2D var3) {
      float var4 = getApproxError(var3);
      if (var4 < this.accuracy) {
         var2.add(var3);
         var1.add(this.approximate(var3));
         return var4;
      } else {
         this.SplitCubic(var2, new float[]{var3.x1, var3.y1, var3.ctrlx1, var3.ctrly1, var3.ctrlx2, var3.ctrly2, var3.x2, var3.y2});
         return this.approximate(var2, var1);
      }
   }

   public float approximate(List var1, CubicCurve2D var2) {
      ArrayList var3 = new ArrayList();
      return this.approximate(var1, var3, var2);
   }

   private QuadCurve2D approximate(CubicCurve2D var1) {
      return new QuadCurve2D(var1.x1, var1.y1, (3.0F * var1.ctrlx1 - var1.x1 + 3.0F * var1.ctrlx2 - var1.x2) / 4.0F, (3.0F * var1.ctrly1 - var1.y1 + 3.0F * var1.ctrly2 - var1.y2) / 4.0F, var1.x2, var1.y2);
   }

   private float approximate(List var1, List var2) {
      QuadCurve2D var3 = this.approximate((CubicCurve2D)var1.get(0));
      float var4 = compareCPs((CubicCurve2D)var1.get(0), elevate(var3));
      var2.add(var3);

      for(int var5 = 1; var5 < var1.size(); ++var5) {
         var3 = this.approximate((CubicCurve2D)var1.get(var5));
         float var6 = compareCPs((CubicCurve2D)var1.get(var5), elevate(var3));
         if (var6 > var4) {
            var4 = var6;
         }

         var2.add(var3);
      }

      return var4;
   }

   private static CubicCurve2D elevate(QuadCurve2D var0) {
      return new CubicCurve2D(var0.x1, var0.y1, (var0.x1 + 2.0F * var0.ctrlx) / 3.0F, (var0.y1 + 2.0F * var0.ctrly) / 3.0F, (2.0F * var0.ctrlx + var0.x2) / 3.0F, (2.0F * var0.ctrly + var0.y2) / 3.0F, var0.x2, var0.y2);
   }

   private static float compare(CubicCurve2D var0, CubicCurve2D var1) {
      float var2 = Math.abs(var0.x1 - var1.x1);
      float var3 = Math.abs(var0.y1 - var1.y1);
      if (var2 < var3) {
         var2 = var3;
      }

      var3 = Math.abs(var0.ctrlx1 - var1.ctrlx1);
      if (var2 < var3) {
         var2 = var3;
      }

      var3 = Math.abs(var0.ctrly1 - var1.ctrly1);
      if (var2 < var3) {
         var2 = var3;
      }

      var3 = Math.abs(var0.ctrlx2 - var1.ctrlx2);
      if (var2 < var3) {
         var2 = var3;
      }

      var3 = Math.abs(var0.ctrly2 - var1.ctrly2);
      if (var2 < var3) {
         var2 = var3;
      }

      var3 = Math.abs(var0.x2 - var1.x2);
      if (var2 < var3) {
         var2 = var3;
      }

      var3 = Math.abs(var0.y2 - var1.y2);
      if (var2 < var3) {
         var2 = var3;
      }

      return var2;
   }

   private static float getApproxError(float[] var0) {
      float var1 = (-3.0F * var0[2] + var0[0] + 3.0F * var0[4] - var0[6]) / 6.0F;
      float var2 = (-3.0F * var0[3] + var0[1] + 3.0F * var0[5] - var0[7]) / 6.0F;
      if (var1 < var2) {
         var1 = var2;
      }

      var2 = (3.0F * var0[2] - var0[0] - 3.0F * var0[4] + var0[6]) / 6.0F;
      if (var1 < var2) {
         var1 = var2;
      }

      var2 = (3.0F * var0[3] - var0[1] - 3.0F * var0[5] + var0[7]) / 6.0F;
      if (var1 < var2) {
         var1 = var2;
      }

      return var1;
   }

   public static float getApproxError(CubicCurve2D var0) {
      return getApproxError(new float[]{var0.x1, var0.y1, var0.ctrlx1, var0.ctrly1, var0.ctrlx2, var0.ctrly2, var0.x2, var0.y2});
   }

   private static float compareCPs(CubicCurve2D var0, CubicCurve2D var1) {
      float var2 = Math.abs(var0.ctrlx1 - var1.ctrlx1);
      float var3 = Math.abs(var0.ctrly1 - var1.ctrly1);
      if (var2 < var3) {
         var2 = var3;
      }

      var3 = Math.abs(var0.ctrlx2 - var1.ctrlx2);
      if (var2 < var3) {
         var2 = var3;
      }

      var3 = Math.abs(var0.ctrly2 - var1.ctrly2);
      if (var2 < var3) {
         var2 = var3;
      }

      return var2;
   }

   private void ProcessMonotonicCubic(List var1, float[] var2) {
      float[] var3 = new float[8];
      float var7;
      float var6 = var7 = var2[0];
      float var9;
      float var8 = var9 = var2[1];

      for(int var10 = 2; var10 < 8; var10 += 2) {
         var6 = var6 > var2[var10] ? var2[var10] : var6;
         var7 = var7 < var2[var10] ? var2[var10] : var7;
         var8 = var8 > var2[var10 + 1] ? var2[var10 + 1] : var8;
         var9 = var9 < var2[var10 + 1] ? var2[var10 + 1] : var9;
      }

      if (!(var7 - var6 > this.maxCubicSize) && !(var9 - var8 > this.maxCubicSize) && !(getApproxError(var2) > this.accuracy)) {
         var1.add(new CubicCurve2D(var2[0], var2[1], var2[2], var2[3], var2[4], var2[5], var2[6], var2[7]));
      } else {
         var3[6] = var2[6];
         var3[7] = var2[7];
         var3[4] = (var2[4] + var2[6]) / 2.0F;
         var3[5] = (var2[5] + var2[7]) / 2.0F;
         float var4 = (var2[2] + var2[4]) / 2.0F;
         float var5 = (var2[3] + var2[5]) / 2.0F;
         var3[2] = (var4 + var3[4]) / 2.0F;
         var3[3] = (var5 + var3[5]) / 2.0F;
         var2[2] = (var2[0] + var2[2]) / 2.0F;
         var2[3] = (var2[1] + var2[3]) / 2.0F;
         var2[4] = (var2[2] + var4) / 2.0F;
         var2[5] = (var2[3] + var5) / 2.0F;
         var2[6] = var3[0] = (var2[4] + var3[2]) / 2.0F;
         var2[7] = var3[1] = (var2[5] + var3[3]) / 2.0F;
         this.ProcessMonotonicCubic(var1, var2);
         this.ProcessMonotonicCubic(var1, var3);
      }

   }

   public void SplitCubic(List var1, float[] var2) {
      float[] var3 = new float[4];
      float[] var4 = new float[3];
      float[] var5 = new float[2];
      int var6 = 0;
      int var7;
      int var8;
      if ((var2[0] > var2[2] || var2[2] > var2[4] || var2[4] > var2[6]) && (var2[0] < var2[2] || var2[2] < var2[4] || var2[4] < var2[6])) {
         var4[2] = -var2[0] + 3.0F * var2[2] - 3.0F * var2[4] + var2[6];
         var4[1] = 2.0F * (var2[0] - 2.0F * var2[2] + var2[4]);
         var4[0] = -var2[0] + var2[2];
         var7 = QuadCurve2D.solveQuadratic(var4, var5);

         for(var8 = 0; var8 < var7; ++var8) {
            if (var5[var8] > 0.0F && var5[var8] < 1.0F) {
               var3[var6++] = var5[var8];
            }
         }
      }

      if ((var2[1] > var2[3] || var2[3] > var2[5] || var2[5] > var2[7]) && (var2[1] < var2[3] || var2[3] < var2[5] || var2[5] < var2[7])) {
         var4[2] = -var2[1] + 3.0F * var2[3] - 3.0F * var2[5] + var2[7];
         var4[1] = 2.0F * (var2[1] - 2.0F * var2[3] + var2[5]);
         var4[0] = -var2[1] + var2[3];
         var7 = QuadCurve2D.solveQuadratic(var4, var5);

         for(var8 = 0; var8 < var7; ++var8) {
            if (var5[var8] > 0.0F && var5[var8] < 1.0F) {
               var3[var6++] = var5[var8];
            }
         }
      }

      if (var6 > 0) {
         Arrays.sort(var3, 0, var6);
         this.ProcessFirstMonotonicPartOfCubic(var1, var2, var3[0]);

         for(var7 = 1; var7 < var6; ++var7) {
            float var9 = var3[var7] - var3[var7 - 1];
            if (var9 > 0.0F) {
               this.ProcessFirstMonotonicPartOfCubic(var1, var2, var9 / (1.0F - var3[var7 - 1]));
            }
         }
      }

      this.ProcessMonotonicCubic(var1, var2);
   }

   private void ProcessFirstMonotonicPartOfCubic(List var1, float[] var2, float var3) {
      float[] var4 = new float[8];
      var4[0] = var2[0];
      var4[1] = var2[1];
      float var5 = var2[2] + var3 * (var2[4] - var2[2]);
      float var6 = var2[3] + var3 * (var2[5] - var2[3]);
      var4[2] = var2[0] + var3 * (var2[2] - var2[0]);
      var4[3] = var2[1] + var3 * (var2[3] - var2[1]);
      var4[4] = var4[2] + var3 * (var5 - var4[2]);
      var4[5] = var4[3] + var3 * (var6 - var4[3]);
      var2[4] += var3 * (var2[6] - var2[4]);
      var2[5] += var3 * (var2[7] - var2[5]);
      var2[2] = var5 + var3 * (var2[4] - var5);
      var2[3] = var6 + var3 * (var2[5] - var6);
      var2[0] = var4[6] = var4[4] + var3 * (var2[2] - var4[4]);
      var2[1] = var4[7] = var4[5] + var3 * (var2[3] - var4[5]);
      this.ProcessMonotonicCubic(var1, var4);
   }
}
