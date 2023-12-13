package org.lwjgl.util.glu;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Project extends Util {
   private static final float[] IDENTITY_MATRIX = new float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F};
   private static final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
   private static final FloatBuffer finalMatrix = BufferUtils.createFloatBuffer(16);
   private static final FloatBuffer tempMatrix = BufferUtils.createFloatBuffer(16);
   private static final float[] in = new float[4];
   private static final float[] out = new float[4];
   private static final float[] forward = new float[3];
   private static final float[] side = new float[3];
   private static final float[] up = new float[3];

   private static void __gluMakeIdentityf(FloatBuffer var0) {
      int var1 = var0.position();
      var0.put(IDENTITY_MATRIX);
      var0.position(var1);
   }

   private static void __gluMultMatrixVecf(FloatBuffer var0, float[] var1, float[] var2) {
      for(int var3 = 0; var3 < 4; ++var3) {
         var2[var3] = var1[0] * var0.get(var0.position() + 0 + var3) + var1[1] * var0.get(var0.position() + 4 + var3) + var1[2] * var0.get(var0.position() + 8 + var3) + var1[3] * var0.get(var0.position() + 12 + var3);
      }

   }

   private static void __gluMultMatricesf(FloatBuffer var0, FloatBuffer var1, FloatBuffer var2) {
      for(int var3 = 0; var3 < 4; ++var3) {
         for(int var4 = 0; var4 < 4; ++var4) {
            var2.put(var2.position() + var3 * 4 + var4, var0.get(var0.position() + var3 * 4 + 0) * var1.get(var1.position() + 0 + var4) + var0.get(var0.position() + var3 * 4 + 1) * var1.get(var1.position() + 4 + var4) + var0.get(var0.position() + var3 * 4 + 2) * var1.get(var1.position() + 8 + var4) + var0.get(var0.position() + var3 * 4 + 3) * var1.get(var1.position() + 12 + var4));
         }
      }

   }

   public static void gluPerspective(float var0, float var1, float var2, float var3) {
      float var7 = var0 / 2.0F * 3.1415927F / 180.0F;
      float var6 = var3 - var2;
      float var4 = (float)Math.sin((double)var7);
      if (var6 != 0.0F && var4 != 0.0F && var1 != 0.0F) {
         float var5 = (float)Math.cos((double)var7) / var4;
         __gluMakeIdentityf(matrix);
         matrix.put(0, var5 / var1);
         matrix.put(5, var5);
         matrix.put(10, -(var3 + var2) / var6);
         matrix.put(11, -1.0F);
         matrix.put(14, -2.0F * var2 * var3 / var6);
         matrix.put(15, 0.0F);
         GL11.glMultMatrix(matrix);
      }
   }

   public static void gluLookAt(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float[] var9 = forward;
      float[] var10 = side;
      float[] var11 = up;
      var9[0] = var3 - var0;
      var9[1] = var4 - var1;
      var9[2] = var5 - var2;
      var11[0] = var6;
      var11[1] = var7;
      var11[2] = var8;
      normalize(var9);
      cross(var9, var11, var10);
      normalize(var10);
      cross(var10, var9, var11);
      __gluMakeIdentityf(matrix);
      matrix.put(0, var10[0]);
      matrix.put(4, var10[1]);
      matrix.put(8, var10[2]);
      matrix.put(1, var11[0]);
      matrix.put(5, var11[1]);
      matrix.put(9, var11[2]);
      matrix.put(2, -var9[0]);
      matrix.put(6, -var9[1]);
      matrix.put(10, -var9[2]);
      GL11.glMultMatrix(matrix);
      GL11.glTranslatef(-var0, -var1, -var2);
   }

   public static boolean gluProject(float var0, float var1, float var2, FloatBuffer var3, FloatBuffer var4, IntBuffer var5, FloatBuffer var6) {
      float[] var7 = in;
      float[] var8 = out;
      var7[0] = var0;
      var7[1] = var1;
      var7[2] = var2;
      var7[3] = 1.0F;
      __gluMultMatrixVecf(var3, var7, var8);
      __gluMultMatrixVecf(var4, var8, var7);
      if ((double)var7[3] == 0.0D) {
         return false;
      } else {
         var7[3] = 1.0F / var7[3] * 0.5F;
         var7[0] = var7[0] * var7[3] + 0.5F;
         var7[1] = var7[1] * var7[3] + 0.5F;
         var7[2] = var7[2] * var7[3] + 0.5F;
         var6.put(0, var7[0] * (float)var5.get(var5.position() + 2) + (float)var5.get(var5.position() + 0));
         var6.put(1, var7[1] * (float)var5.get(var5.position() + 3) + (float)var5.get(var5.position() + 1));
         var6.put(2, var7[2]);
         return true;
      }
   }

   public static boolean gluUnProject(float var0, float var1, float var2, FloatBuffer var3, FloatBuffer var4, IntBuffer var5, FloatBuffer var6) {
      float[] var7 = in;
      float[] var8 = out;
      __gluMultMatricesf(var3, var4, finalMatrix);
      if (finalMatrix < finalMatrix) {
         return false;
      } else {
         var7[0] = var0;
         var7[1] = var1;
         var7[2] = var2;
         var7[3] = 1.0F;
         var7[0] = (var7[0] - (float)var5.get(var5.position() + 0)) / (float)var5.get(var5.position() + 2);
         var7[1] = (var7[1] - (float)var5.get(var5.position() + 1)) / (float)var5.get(var5.position() + 3);
         var7[0] = var7[0] * 2.0F - 1.0F;
         var7[1] = var7[1] * 2.0F - 1.0F;
         var7[2] = var7[2] * 2.0F - 1.0F;
         __gluMultMatrixVecf(finalMatrix, var7, var8);
         if ((double)var8[3] == 0.0D) {
            return false;
         } else {
            var8[3] = 1.0F / var8[3];
            var6.put(var6.position() + 0, var8[0] * var8[3]);
            var6.put(var6.position() + 1, var8[1] * var8[3]);
            var6.put(var6.position() + 2, var8[2] * var8[3]);
            return true;
         }
      }
   }

   public static void gluPickMatrix(float var0, float var1, float var2, float var3, IntBuffer var4) {
      if (!(var2 <= 0.0F) && !(var3 <= 0.0F)) {
         GL11.glTranslatef(((float)var4.get(var4.position() + 2) - 2.0F * (var0 - (float)var4.get(var4.position() + 0))) / var2, ((float)var4.get(var4.position() + 3) - 2.0F * (var1 - (float)var4.get(var4.position() + 1))) / var3, 0.0F);
         GL11.glScalef((float)var4.get(var4.position() + 2) / var2, (float)var4.get(var4.position() + 3) / var3, 1.0F);
      }
   }
}
