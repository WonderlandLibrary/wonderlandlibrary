package org.newdawn.slick.geom;

import org.newdawn.slick.util.FastTrig;

public class Transform {
   private float[] matrixPosition;

   public Transform() {
      this.matrixPosition = new float[]{1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F};
   }

   public Transform(Transform var1) {
      this.matrixPosition = new float[9];

      for(int var2 = 0; var2 < 9; ++var2) {
         this.matrixPosition[var2] = var1.matrixPosition[var2];
      }

   }

   public Transform(Transform var1, Transform var2) {
      this(var1);
      this.concatenate(var2);
   }

   public Transform(float[] var1) {
      if (var1.length != 6) {
         throw new RuntimeException("The parameter must be a float array of length 6.");
      } else {
         this.matrixPosition = new float[]{var1[0], var1[1], var1[2], var1[3], var1[4], var1[5], 0.0F, 0.0F, 1.0F};
      }
   }

   public Transform(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.matrixPosition = new float[]{var1, var2, var3, var4, var5, var6, 0.0F, 0.0F, 1.0F};
   }

   public void transform(float[] var1, int var2, float[] var3, int var4, int var5) {
      float[] var6 = new float[var5 * 2];

      int var7;
      for(var7 = 0; var7 < var5 * 2; var7 += 2) {
         for(int var8 = 0; var8 < 6; var8 += 3) {
            var6[var7 + var8 / 3] = var1[var7 + var2] * this.matrixPosition[var8] + var1[var7 + var2 + 1] * this.matrixPosition[var8 + 1] + 1.0F * this.matrixPosition[var8 + 2];
         }
      }

      for(var7 = 0; var7 < var5 * 2; var7 += 2) {
         var3[var7 + var4] = var6[var7];
         var3[var7 + var4 + 1] = var6[var7 + 1];
      }

   }

   public Transform concatenate(Transform var1) {
      float[] var2 = new float[9];
      float var3 = this.matrixPosition[0] * var1.matrixPosition[0] + this.matrixPosition[1] * var1.matrixPosition[3];
      float var4 = this.matrixPosition[0] * var1.matrixPosition[1] + this.matrixPosition[1] * var1.matrixPosition[4];
      float var5 = this.matrixPosition[0] * var1.matrixPosition[2] + this.matrixPosition[1] * var1.matrixPosition[5] + this.matrixPosition[2];
      float var6 = this.matrixPosition[3] * var1.matrixPosition[0] + this.matrixPosition[4] * var1.matrixPosition[3];
      float var7 = this.matrixPosition[3] * var1.matrixPosition[1] + this.matrixPosition[4] * var1.matrixPosition[4];
      float var8 = this.matrixPosition[3] * var1.matrixPosition[2] + this.matrixPosition[4] * var1.matrixPosition[5] + this.matrixPosition[5];
      var2[0] = var3;
      var2[1] = var4;
      var2[2] = var5;
      var2[3] = var6;
      var2[4] = var7;
      var2[5] = var8;
      this.matrixPosition = var2;
      return this;
   }

   public String toString() {
      String var1 = "Transform[[" + this.matrixPosition[0] + "," + this.matrixPosition[1] + "," + this.matrixPosition[2] + "][" + this.matrixPosition[3] + "," + this.matrixPosition[4] + "," + this.matrixPosition[5] + "][" + this.matrixPosition[6] + "," + this.matrixPosition[7] + "," + this.matrixPosition[8] + "]]";
      return var1.toString();
   }

   public float[] getMatrixPosition() {
      return this.matrixPosition;
   }

   public static Transform createRotateTransform(float var0) {
      return new Transform((float)FastTrig.cos((double)var0), -((float)FastTrig.sin((double)var0)), 0.0F, (float)FastTrig.sin((double)var0), (float)FastTrig.cos((double)var0), 0.0F);
   }

   public static Transform createRotateTransform(float var0, float var1, float var2) {
      Transform var3 = createRotateTransform(var0);
      float var4 = var3.matrixPosition[3];
      float var5 = 1.0F - var3.matrixPosition[4];
      var3.matrixPosition[2] = var1 * var5 + var2 * var4;
      var3.matrixPosition[5] = var2 * var5 - var1 * var4;
      return var3;
   }

   public static Transform createTranslateTransform(float var0, float var1) {
      return new Transform(1.0F, 0.0F, var0, 0.0F, 1.0F, var1);
   }

   public static Transform createScaleTransform(float var0, float var1) {
      return new Transform(var0, 0.0F, 0.0F, 0.0F, var1, 0.0F);
   }

   public Vector2f transform(Vector2f var1) {
      float[] var2 = new float[]{var1.x, var1.y};
      float[] var3 = new float[2];
      this.transform(var2, 0, var3, 0, 1);
      return new Vector2f(var3[0], var3[1]);
   }
}
