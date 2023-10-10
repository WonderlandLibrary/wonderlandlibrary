package org.lwjgl.opengl;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class APPLEVertexProgramEvaluators {
   public static final int GL_VERTEX_ATTRIB_MAP1_APPLE = 35328;
   public static final int GL_VERTEX_ATTRIB_MAP2_APPLE = 35329;
   public static final int GL_VERTEX_ATTRIB_MAP1_SIZE_APPLE = 35330;
   public static final int GL_VERTEX_ATTRIB_MAP1_COEFF_APPLE = 35331;
   public static final int GL_VERTEX_ATTRIB_MAP1_ORDER_APPLE = 35332;
   public static final int GL_VERTEX_ATTRIB_MAP1_DOMAIN_APPLE = 35333;
   public static final int GL_VERTEX_ATTRIB_MAP2_SIZE_APPLE = 35334;
   public static final int GL_VERTEX_ATTRIB_MAP2_COEFF_APPLE = 35335;
   public static final int GL_VERTEX_ATTRIB_MAP2_ORDER_APPLE = 35336;
   public static final int GL_VERTEX_ATTRIB_MAP2_DOMAIN_APPLE = 35337;

   private APPLEVertexProgramEvaluators() {
   }

   public static void glEnableVertexAttribAPPLE(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glEnableVertexAttribAPPLE;
      BufferChecks.checkFunctionAddress(var3);
      nglEnableVertexAttribAPPLE(var0, var1, var3);
   }

   static native void nglEnableVertexAttribAPPLE(int var0, int var1, long var2);

   public static void glDisableVertexAttribAPPLE(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDisableVertexAttribAPPLE;
      BufferChecks.checkFunctionAddress(var3);
      nglDisableVertexAttribAPPLE(var0, var1, var3);
   }

   static native void nglDisableVertexAttribAPPLE(int var0, int var1, long var2);

   public static boolean glIsVertexAttribEnabledAPPLE(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glIsVertexAttribEnabledAPPLE;
      BufferChecks.checkFunctionAddress(var3);
      boolean var5 = nglIsVertexAttribEnabledAPPLE(var0, var1, var3);
      return var5;
   }

   static native boolean nglIsVertexAttribEnabledAPPLE(int var0, int var1, long var2);

   public static void glMapVertexAttrib1dAPPLE(int var0, int var1, double var2, double var4, int var6, int var7, DoubleBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glMapVertexAttrib1dAPPLE;
      BufferChecks.checkFunctionAddress(var10);
      BufferChecks.checkDirect(var8);
      nglMapVertexAttrib1dAPPLE(var0, var1, var2, var4, var6, var7, MemoryUtil.getAddress(var8), var10);
   }

   static native void nglMapVertexAttrib1dAPPLE(int var0, int var1, double var2, double var4, int var6, int var7, long var8, long var10);

   public static void glMapVertexAttrib1fAPPLE(int var0, int var1, float var2, float var3, int var4, int var5, FloatBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glMapVertexAttrib1fAPPLE;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var6);
      nglMapVertexAttrib1fAPPLE(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), var8);
   }

   static native void nglMapVertexAttrib1fAPPLE(int var0, int var1, float var2, float var3, int var4, int var5, long var6, long var8);

   public static void glMapVertexAttrib2dAPPLE(int var0, int var1, double var2, double var4, int var6, int var7, double var8, double var10, int var12, int var13, DoubleBuffer var14) {
      ContextCapabilities var15 = GLContext.getCapabilities();
      long var16 = var15.glMapVertexAttrib2dAPPLE;
      BufferChecks.checkFunctionAddress(var16);
      BufferChecks.checkDirect(var14);
      nglMapVertexAttrib2dAPPLE(var0, var1, var2, var4, var6, var7, var8, var10, var12, var13, MemoryUtil.getAddress(var14), var16);
   }

   static native void nglMapVertexAttrib2dAPPLE(int var0, int var1, double var2, double var4, int var6, int var7, double var8, double var10, int var12, int var13, long var14, long var16);

   public static void glMapVertexAttrib2fAPPLE(int var0, int var1, float var2, float var3, int var4, int var5, float var6, float var7, int var8, int var9, FloatBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glMapVertexAttrib2fAPPLE;
      BufferChecks.checkFunctionAddress(var12);
      BufferChecks.checkDirect(var10);
      nglMapVertexAttrib2fAPPLE(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddress(var10), var12);
   }

   static native void nglMapVertexAttrib2fAPPLE(int var0, int var1, float var2, float var3, int var4, int var5, float var6, float var7, int var8, int var9, long var10, long var12);
}
