package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class GL14 {
   public static final int GL_GENERATE_MIPMAP = 33169;
   public static final int GL_GENERATE_MIPMAP_HINT = 33170;
   public static final int GL_DEPTH_COMPONENT16 = 33189;
   public static final int GL_DEPTH_COMPONENT24 = 33190;
   public static final int GL_DEPTH_COMPONENT32 = 33191;
   public static final int GL_TEXTURE_DEPTH_SIZE = 34890;
   public static final int GL_DEPTH_TEXTURE_MODE = 34891;
   public static final int GL_TEXTURE_COMPARE_MODE = 34892;
   public static final int GL_TEXTURE_COMPARE_FUNC = 34893;
   public static final int GL_COMPARE_R_TO_TEXTURE = 34894;
   public static final int GL_FOG_COORDINATE_SOURCE = 33872;
   public static final int GL_FOG_COORDINATE = 33873;
   public static final int GL_FRAGMENT_DEPTH = 33874;
   public static final int GL_CURRENT_FOG_COORDINATE = 33875;
   public static final int GL_FOG_COORDINATE_ARRAY_TYPE = 33876;
   public static final int GL_FOG_COORDINATE_ARRAY_STRIDE = 33877;
   public static final int GL_FOG_COORDINATE_ARRAY_POINTER = 33878;
   public static final int GL_FOG_COORDINATE_ARRAY = 33879;
   public static final int GL_POINT_SIZE_MIN = 33062;
   public static final int GL_POINT_SIZE_MAX = 33063;
   public static final int GL_POINT_FADE_THRESHOLD_SIZE = 33064;
   public static final int GL_POINT_DISTANCE_ATTENUATION = 33065;
   public static final int GL_COLOR_SUM = 33880;
   public static final int GL_CURRENT_SECONDARY_COLOR = 33881;
   public static final int GL_SECONDARY_COLOR_ARRAY_SIZE = 33882;
   public static final int GL_SECONDARY_COLOR_ARRAY_TYPE = 33883;
   public static final int GL_SECONDARY_COLOR_ARRAY_STRIDE = 33884;
   public static final int GL_SECONDARY_COLOR_ARRAY_POINTER = 33885;
   public static final int GL_SECONDARY_COLOR_ARRAY = 33886;
   public static final int GL_BLEND_DST_RGB = 32968;
   public static final int GL_BLEND_SRC_RGB = 32969;
   public static final int GL_BLEND_DST_ALPHA = 32970;
   public static final int GL_BLEND_SRC_ALPHA = 32971;
   public static final int GL_INCR_WRAP = 34055;
   public static final int GL_DECR_WRAP = 34056;
   public static final int GL_TEXTURE_FILTER_CONTROL = 34048;
   public static final int GL_TEXTURE_LOD_BIAS = 34049;
   public static final int GL_MAX_TEXTURE_LOD_BIAS = 34045;
   public static final int GL_MIRRORED_REPEAT = 33648;
   public static final int GL_BLEND_COLOR = 32773;
   public static final int GL_BLEND_EQUATION = 32777;
   public static final int GL_FUNC_ADD = 32774;
   public static final int GL_FUNC_SUBTRACT = 32778;
   public static final int GL_FUNC_REVERSE_SUBTRACT = 32779;
   public static final int GL_MIN = 32775;
   public static final int GL_MAX = 32776;

   private GL14() {
   }

   public static void glBlendEquation(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glBlendEquation;
      BufferChecks.checkFunctionAddress(var2);
      nglBlendEquation(var0, var2);
   }

   static native void nglBlendEquation(int var0, long var1);

   public static void glBlendColor(float var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBlendColor;
      BufferChecks.checkFunctionAddress(var5);
      nglBlendColor(var0, var1, var2, var3, var5);
   }

   static native void nglBlendColor(float var0, float var1, float var2, float var3, long var4);

   public static void glFogCoordf(float var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glFogCoordf;
      BufferChecks.checkFunctionAddress(var2);
      nglFogCoordf(var0, var2);
   }

   static native void nglFogCoordf(float var0, long var1);

   public static void glFogCoordd(double var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glFogCoordd;
      BufferChecks.checkFunctionAddress(var3);
      nglFogCoordd(var0, var3);
   }

   static native void nglFogCoordd(double var0, long var2);

   public static void glFogCoordPointer(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glFogCoordPointer;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureArrayVBOdisabled(var2);
      BufferChecks.checkDirect(var1);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var2).GL14_glFogCoordPointer_data = var1;
      }

      nglFogCoordPointer(5130, var0, MemoryUtil.getAddress(var1), var3);
   }

   public static void glFogCoordPointer(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glFogCoordPointer;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureArrayVBOdisabled(var2);
      BufferChecks.checkDirect(var1);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var2).GL14_glFogCoordPointer_data = var1;
      }

      nglFogCoordPointer(5126, var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglFogCoordPointer(int var0, int var1, long var2, long var4);

   public static void glFogCoordPointer(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glFogCoordPointer;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOenabled(var4);
      nglFogCoordPointerBO(var0, var1, var2, var5);
   }

   static native void nglFogCoordPointerBO(int var0, int var1, long var2, long var4);

   public static void glMultiDrawArrays(int var0, IntBuffer var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiDrawArrays;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkBuffer(var2, var1.remaining());
      nglMultiDrawArrays(var0, MemoryUtil.getAddress(var1), MemoryUtil.getAddress(var2), var1.remaining(), var4);
   }

   static native void nglMultiDrawArrays(int var0, long var1, long var3, int var5, long var6);

   public static void glPointParameteri(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPointParameteri;
      BufferChecks.checkFunctionAddress(var3);
      nglPointParameteri(var0, var1, var3);
   }

   static native void nglPointParameteri(int var0, int var1, long var2);

   public static void glPointParameterf(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPointParameterf;
      BufferChecks.checkFunctionAddress(var3);
      nglPointParameterf(var0, var1, var3);
   }

   static native void nglPointParameterf(int var0, float var1, long var2);

   public static void glPointParameter(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPointParameteriv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglPointParameteriv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglPointParameteriv(int var0, long var1, long var3);

   public static void glPointParameter(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPointParameterfv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 4);
      nglPointParameterfv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglPointParameterfv(int var0, long var1, long var3);

   public static void glSecondaryColor3b(byte var0, byte var1, byte var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSecondaryColor3b;
      BufferChecks.checkFunctionAddress(var4);
      nglSecondaryColor3b(var0, var1, var2, var4);
   }

   static native void nglSecondaryColor3b(byte var0, byte var1, byte var2, long var3);

   public static void glSecondaryColor3f(float var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSecondaryColor3f;
      BufferChecks.checkFunctionAddress(var4);
      nglSecondaryColor3f(var0, var1, var2, var4);
   }

   static native void nglSecondaryColor3f(float var0, float var1, float var2, long var3);

   public static void glSecondaryColor3d(double var0, double var2, double var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glSecondaryColor3d;
      BufferChecks.checkFunctionAddress(var7);
      nglSecondaryColor3d(var0, var2, var4, var7);
   }

   static native void nglSecondaryColor3d(double var0, double var2, double var4, long var6);

   public static void glSecondaryColor3ub(byte var0, byte var1, byte var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSecondaryColor3ub;
      BufferChecks.checkFunctionAddress(var4);
      nglSecondaryColor3ub(var0, var1, var2, var4);
   }

   static native void nglSecondaryColor3ub(byte var0, byte var1, byte var2, long var3);

   public static void glSecondaryColorPointer(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSecondaryColorPointer;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      nglSecondaryColorPointer(var0, 5130, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glSecondaryColorPointer(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSecondaryColorPointer;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      nglSecondaryColorPointer(var0, 5126, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glSecondaryColorPointer(int var0, boolean var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glSecondaryColorPointer;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglSecondaryColorPointer(var0, var1 ? 5121 : 5120, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglSecondaryColorPointer(int var0, int var1, int var2, long var3, long var5);

   public static void glSecondaryColorPointer(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glSecondaryColorPointer;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOenabled(var5);
      nglSecondaryColorPointerBO(var0, var1, var2, var3, var6);
   }

   static native void nglSecondaryColorPointerBO(int var0, int var1, int var2, long var3, long var5);

   public static void glBlendFuncSeparate(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBlendFuncSeparate;
      BufferChecks.checkFunctionAddress(var5);
      nglBlendFuncSeparate(var0, var1, var2, var3, var5);
   }

   static native void nglBlendFuncSeparate(int var0, int var1, int var2, int var3, long var4);

   public static void glWindowPos2f(float var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glWindowPos2f;
      BufferChecks.checkFunctionAddress(var3);
      nglWindowPos2f(var0, var1, var3);
   }

   static native void nglWindowPos2f(float var0, float var1, long var2);

   public static void glWindowPos2d(double var0, double var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glWindowPos2d;
      BufferChecks.checkFunctionAddress(var5);
      nglWindowPos2d(var0, var2, var5);
   }

   static native void nglWindowPos2d(double var0, double var2, long var4);

   public static void glWindowPos2i(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glWindowPos2i;
      BufferChecks.checkFunctionAddress(var3);
      nglWindowPos2i(var0, var1, var3);
   }

   static native void nglWindowPos2i(int var0, int var1, long var2);

   public static void glWindowPos3f(float var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glWindowPos3f;
      BufferChecks.checkFunctionAddress(var4);
      nglWindowPos3f(var0, var1, var2, var4);
   }

   static native void nglWindowPos3f(float var0, float var1, float var2, long var3);

   public static void glWindowPos3d(double var0, double var2, double var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glWindowPos3d;
      BufferChecks.checkFunctionAddress(var7);
      nglWindowPos3d(var0, var2, var4, var7);
   }

   static native void nglWindowPos3d(double var0, double var2, double var4, long var6);

   public static void glWindowPos3i(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glWindowPos3i;
      BufferChecks.checkFunctionAddress(var4);
      nglWindowPos3i(var0, var1, var2, var4);
   }

   static native void nglWindowPos3i(int var0, int var1, int var2, long var3);
}
