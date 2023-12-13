package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVEvaluators {
   public static final int GL_EVAL_2D_NV = 34496;
   public static final int GL_EVAL_TRIANGULAR_2D_NV = 34497;
   public static final int GL_MAP_TESSELLATION_NV = 34498;
   public static final int GL_MAP_ATTRIB_U_ORDER_NV = 34499;
   public static final int GL_MAP_ATTRIB_V_ORDER_NV = 34500;
   public static final int GL_EVAL_FRACTIONAL_TESSELLATION_NV = 34501;
   public static final int GL_EVAL_VERTEX_ATTRIB0_NV = 34502;
   public static final int GL_EVAL_VERTEX_ATTRIB1_NV = 34503;
   public static final int GL_EVAL_VERTEX_ATTRIB2_NV = 34504;
   public static final int GL_EVAL_VERTEX_ATTRIB3_NV = 34505;
   public static final int GL_EVAL_VERTEX_ATTRIB4_NV = 34506;
   public static final int GL_EVAL_VERTEX_ATTRIB5_NV = 34507;
   public static final int GL_EVAL_VERTEX_ATTRIB6_NV = 34508;
   public static final int GL_EVAL_VERTEX_ATTRIB7_NV = 34509;
   public static final int GL_EVAL_VERTEX_ATTRIB8_NV = 34510;
   public static final int GL_EVAL_VERTEX_ATTRIB9_NV = 34511;
   public static final int GL_EVAL_VERTEX_ATTRIB10_NV = 34512;
   public static final int GL_EVAL_VERTEX_ATTRIB11_NV = 34513;
   public static final int GL_EVAL_VERTEX_ATTRIB12_NV = 34514;
   public static final int GL_EVAL_VERTEX_ATTRIB13_NV = 34515;
   public static final int GL_EVAL_VERTEX_ATTRIB14_NV = 34516;
   public static final int GL_EVAL_VERTEX_ATTRIB15_NV = 34517;
   public static final int GL_MAX_MAP_TESSELLATION_NV = 34518;
   public static final int GL_MAX_RATIONAL_EVAL_ORDER_NV = 34519;

   private NVEvaluators() {
   }

   public static void glGetMapControlPointsNV(int var0, int var1, int var2, int var3, int var4, boolean var5, FloatBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glGetMapControlPointsNV;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var6);
      nglGetMapControlPointsNV(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), var8);
   }

   static native void nglGetMapControlPointsNV(int var0, int var1, int var2, int var3, int var4, boolean var5, long var6, long var8);

   public static void glMapControlPointsNV(int var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7, FloatBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glMapControlPointsNV;
      BufferChecks.checkFunctionAddress(var10);
      BufferChecks.checkDirect(var8);
      nglMapControlPointsNV(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddress(var8), var10);
   }

   static native void nglMapControlPointsNV(int var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7, long var8, long var10);

   public static void glMapParameterNV(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMapParameterfvNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglMapParameterfvNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglMapParameterfvNV(int var0, int var1, long var2, long var4);

   public static void glMapParameterNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMapParameterivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglMapParameterivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglMapParameterivNV(int var0, int var1, long var2, long var4);

   public static void glGetMapParameterNV(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetMapParameterfvNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetMapParameterfvNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetMapParameterfvNV(int var0, int var1, long var2, long var4);

   public static void glGetMapParameterNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetMapParameterivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetMapParameterivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetMapParameterivNV(int var0, int var1, long var2, long var4);

   public static void glGetMapAttribParameterNV(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetMapAttribParameterfvNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((FloatBuffer)var3, 4);
      nglGetMapAttribParameterfvNV(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetMapAttribParameterfvNV(int var0, int var1, int var2, long var3, long var5);

   public static void glGetMapAttribParameterNV(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetMapAttribParameterivNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetMapAttribParameterivNV(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetMapAttribParameterivNV(int var0, int var1, int var2, long var3, long var5);

   public static void glEvalMapsNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glEvalMapsNV;
      BufferChecks.checkFunctionAddress(var3);
      nglEvalMapsNV(var0, var1, var3);
   }

   static native void nglEvalMapsNV(int var0, int var1, long var2);
}
