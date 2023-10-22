package com.sun.prism.impl;

import com.sun.javafx.geom.Quat4f;
import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;

class MeshUtil {
   static final float NORMAL_WELD_COS = 0.9952F;
   static final float TANGENT_WELD_COS = 0.866F;
   static final float G_UV_PARALLEL = 0.9988F;
   static final float COS_1_DEGREE = 0.9998477F;
   static final float BIG_ENOUGH_NORMA2 = 0.0625F;
   static final double PI = Math.PI;
   static final float INV_SQRT2 = 0.70710677F;
   static final float DEAD_FACE = 9.094947E-13F;
   static final float MAGIC_SMALL = 1.0E-10F;
   static final float COS110 = -0.33333334F;

   private MeshUtil() {
   }

   static boolean isDeadFace(float var0) {
      return var0 < 9.094947E-13F;
   }

   static boolean isDeadFace(int[] var0) {
      return var0[0] == var0[1] || var0[1] == var0[2] || var0[2] == var0[0];
   }

   static boolean isNormalAlmostEqual(Vec3f var0, Vec3f var1) {
      return var0.dot(var1) >= 0.9998477F;
   }

   static boolean isTangentOk(Vec3f[] var0, Vec3f[] var1) {
      return var0[0].dot(var1[0]) >= 0.9952F && var0[1].dot(var1[1]) >= 0.866F && var0[2].dot(var1[2]) >= 0.866F;
   }

   static boolean isNormalOkAfterWeld(Vec3f var0) {
      return var0.dot(var0) > 0.0625F;
   }

   static boolean isTangentOK(Vec3f[] var0) {
      return isTangentOk(var0, var0);
   }

   static boolean isOppositeLookingNormals(Vec3f[] var0, Vec3f[] var1) {
      float var2 = var0[0].dot(var1[0]);
      return var2 < -0.33333334F;
   }

   static float fabs(float var0) {
      return var0 < 0.0F ? -var0 : var0;
   }

   static void getOrt(Vec3f var0, Vec3f var1) {
      var1.cross(var0, var1);
      var1.cross(var1, var0);
   }

   static void orthogonalizeTB(Vec3f[] var0) {
      getOrt(var0[0], var0[1]);
      getOrt(var0[0], var0[2]);
      var0[1].normalize();
      var0[2].normalize();
   }

   static void computeTBNNormalized(Vec3f var0, Vec3f var1, Vec3f var2, Vec2f var3, Vec2f var4, Vec2f var5, Vec3f[] var6) {
      MeshTempState var7 = MeshTempState.getInstance();
      Vec3f var8 = var7.vec3f1;
      Vec3f var9 = var7.vec3f2;
      Vec3f var10 = var7.vec3f3;
      var9.sub(var1, var0);
      var10.sub(var2, var0);
      var8.cross(var9, var10);
      var6[0].set(var8);
      var6[0].normalize();
      var9.set(0.0F, var4.x - var3.x, var4.y - var3.y);
      var10.set(0.0F, var5.x - var3.x, var5.y - var3.y);
      if (var9.y * var10.z == var9.z * var10.y) {
         generateTB(var0, var1, var2, var6);
      } else {
         var9.x = var1.x - var0.x;
         var10.x = var2.x - var0.x;
         var8.cross(var9, var10);
         var6[1].x = -var8.y / var8.x;
         var6[2].x = -var8.z / var8.x;
         var9.x = var1.y - var0.y;
         var10.x = var2.y - var0.y;
         var8.cross(var9, var10);
         var6[1].y = -var8.y / var8.x;
         var6[2].y = -var8.z / var8.x;
         var9.x = var1.z - var0.z;
         var10.x = var2.z - var0.z;
         var8.cross(var9, var10);
         var6[1].z = -var8.y / var8.x;
         var6[2].z = -var8.z / var8.x;
         var6[1].normalize();
         var6[2].normalize();
      }
   }

   static void fixParallelTB(Vec3f[] var0) {
      MeshTempState var1 = MeshTempState.getInstance();
      Vec3f var2 = var1.vec3f1;
      var2.add(var0[1], var0[2]);
      Vec3f var3 = var1.vec3f2;
      var3.cross(var0[0], var2);
      var2.normalize();
      var3.normalize();
      var0[1].add(var2, var3);
      var0[1].mul(0.70710677F);
      var0[2].sub(var2, var3);
      var0[2].mul(0.70710677F);
   }

   static void generateTB(Vec3f var0, Vec3f var1, Vec3f var2, Vec3f[] var3) {
      MeshTempState var4 = MeshTempState.getInstance();
      Vec3f var5 = var4.vec3f1;
      var5.sub(var1, var0);
      Vec3f var6 = var4.vec3f2;
      var6.sub(var2, var0);
      if (var5.dot(var5) > var6.dot(var6)) {
         var3[1].set(var5);
         var3[1].normalize();
         var3[2].cross(var3[0], var3[1]);
      } else {
         var3[2].set(var6);
         var3[2].normalize();
         var3[1].cross(var3[2], var3[0]);
      }

   }

   static double clamp(double var0, double var2, double var4) {
      return var0 < var4 ? (var0 > var2 ? var0 : var2) : var4;
   }

   static void fixTSpace(Vec3f[] var0) {
      float var1 = var0[0].length();
      MeshTempState var2 = MeshTempState.getInstance();
      Vec3f var3 = var2.vec3f1;
      var3.set(var0[1]);
      Vec3f var4 = var2.vec3f2;
      var4.set(var0[2]);
      getOrt(var0[0], var3);
      getOrt(var0[0], var4);
      float var5 = var3.length();
      float var6 = var4.length();
      double var7 = (double)(var3.dot(var4) / (var5 * var6));
      Vec3f var9 = var2.vec3f3;
      Vec3f var10 = var2.vec3f4;
      if ((double)fabs((float)var7) > 0.998) {
         Vec3f var11 = var2.vec3f5;
         var11.cross(var0[0], var3);
         var11.normalize();
         var10.set(var11);
         if (var11.dot(var4) < 0.0F) {
            var10.mul(-1.0F);
         }

         var9.set(var3);
         var9.mul(1.0F / var5);
      } else {
         double var22 = Math.acos(clamp(var7, -1.0, 1.0));
         double var13 = (1.5707963267948966 - var22) * 0.5;
         Vec2f var15 = var2.vec2f1;
         var15.set((float)Math.sin(var13), (float)Math.cos(var13));
         Vec2f var16 = var2.vec2f2;
         var16.set((float)Math.sin(var13 + var22), (float)Math.cos(var13 + var22));
         Vec3f var17 = var2.vec3f5;
         var17.set(var4);
         getOrt(var3, var17);
         float var18 = var17.length();
         var9.set(var3);
         var9.mul(var15.y / var5);
         Vec3f var19 = var2.vec3f6;
         var19.set(var17);
         var19.mul(var15.x / var18);
         var9.sub(var19);
         var10.set(var3);
         var10.mul(var16.y / var5);
         var19.set(var17);
         var19.mul(var16.x / var18);
         var10.add(var19);
         var9.dot(var3);
         var10.dot(var4);
      }

      var0[1].set(var9);
      var0[2].set(var10);
      var0[0].mul(1.0F / var1);
   }

   static void buildQuat(Vec3f[] var0, Quat4f var1) {
      MeshTempState var2 = MeshTempState.getInstance();
      float[][] var3 = var2.matrix;
      float[] var4 = var2.vector;

      for(int var5 = 0; var5 < 3; ++var5) {
         var3[var5][0] = var0[var5].x;
         var3[var5][1] = var0[var5].y;
         var3[var5][2] = var0[var5].z;
      }

      float var12 = var3[0][0] + var3[1][1] + var3[2][2];
      if (var12 > 0.0F) {
         float var6 = (float)Math.sqrt((double)(var12 + 1.0F));
         float var7 = 0.5F / var6;
         var1.w = 0.5F * var6;
         var1.x = (var3[1][2] - var3[2][1]) * var7;
         var1.y = (var3[2][0] - var3[0][2]) * var7;
         var1.z = (var3[0][1] - var3[1][0]) * var7;
      } else {
         int[] var13 = new int[]{1, 2, 0};
         byte var14 = 0;
         if (var3[1][1] > var3[0][0]) {
            var14 = 1;
         }

         if (var3[2][2] > var3[var14][var14]) {
            var14 = 2;
         }

         int var8 = var13[var14];
         int var9 = var13[var8];
         float var10 = (float)Math.sqrt((double)(var3[var14][var14] - var3[var8][var8] - var3[var9][var9] + 1.0F));
         if (var3[var8][var9] < var3[var9][var8]) {
            var10 = -var10;
         }

         float var11 = 0.5F / var10;
         var4[var14] = 0.5F * var10;
         var1.w = (var3[var8][var9] - var3[var9][var8]) * var11;
         var4[var8] = (var3[var14][var8] + var3[var8][var14]) * var11;
         var4[var9] = (var3[var14][var9] + var3[var9][var14]) * var11;
         var1.x = var4[0];
         var1.y = var4[1];
         var1.z = var4[2];
      }

   }
}
