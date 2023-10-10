package org.lwjgl.opengl;

import java.nio.DoubleBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class EXTVertexAttrib64bit {
   public static final int GL_DOUBLE_VEC2_EXT = 36860;
   public static final int GL_DOUBLE_VEC3_EXT = 36861;
   public static final int GL_DOUBLE_VEC4_EXT = 36862;
   public static final int GL_DOUBLE_MAT2_EXT = 36678;
   public static final int GL_DOUBLE_MAT3_EXT = 36679;
   public static final int GL_DOUBLE_MAT4_EXT = 36680;
   public static final int GL_DOUBLE_MAT2x3_EXT = 36681;
   public static final int GL_DOUBLE_MAT2x4_EXT = 36682;
   public static final int GL_DOUBLE_MAT3x2_EXT = 36683;
   public static final int GL_DOUBLE_MAT3x4_EXT = 36684;
   public static final int GL_DOUBLE_MAT4x2_EXT = 36685;
   public static final int GL_DOUBLE_MAT4x3_EXT = 36686;

   private EXTVertexAttrib64bit() {
   }

   public static void glVertexAttribL1dEXT(int var0, double var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttribL1dEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttribL1dEXT(var0, var1, var4);
   }

   static native void nglVertexAttribL1dEXT(int var0, double var1, long var3);

   public static void glVertexAttribL2dEXT(int var0, double var1, double var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribL2dEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttribL2dEXT(var0, var1, var3, var6);
   }

   static native void nglVertexAttribL2dEXT(int var0, double var1, double var3, long var5);

   public static void glVertexAttribL3dEXT(int var0, double var1, double var3, double var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glVertexAttribL3dEXT;
      BufferChecks.checkFunctionAddress(var8);
      nglVertexAttribL3dEXT(var0, var1, var3, var5, var8);
   }

   static native void nglVertexAttribL3dEXT(int var0, double var1, double var3, double var5, long var7);

   public static void glVertexAttribL4dEXT(int var0, double var1, double var3, double var5, double var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glVertexAttribL4dEXT;
      BufferChecks.checkFunctionAddress(var10);
      nglVertexAttribL4dEXT(var0, var1, var3, var5, var7, var10);
   }

   static native void nglVertexAttribL4dEXT(int var0, double var1, double var3, double var5, double var7, long var9);

   public static void glVertexAttribL1EXT(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribL1dvEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 1);
      nglVertexAttribL1dvEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribL1dvEXT(int var0, long var1, long var3);

   public static void glVertexAttribL2EXT(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribL2dvEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 2);
      nglVertexAttribL2dvEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribL2dvEXT(int var0, long var1, long var3);

   public static void glVertexAttribL3EXT(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribL3dvEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 3);
      nglVertexAttribL3dvEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribL3dvEXT(int var0, long var1, long var3);

   public static void glVertexAttribL4EXT(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribL4dvEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 4);
      nglVertexAttribL4dvEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribL4dvEXT(int var0, long var1, long var3);

   public static void glVertexAttribLPointerEXT(int var0, int var1, int var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribLPointerEXT;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var4).glVertexAttribPointer_buffer[var0] = var3;
      }

      nglVertexAttribLPointerEXT(var0, var1, 5130, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglVertexAttribLPointerEXT(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glVertexAttribLPointerEXT(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribLPointerEXT;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOenabled(var5);
      nglVertexAttribLPointerEXTBO(var0, var1, 5130, var2, var3, var6);
   }

   static native void nglVertexAttribLPointerEXTBO(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glGetVertexAttribLEXT(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribLdvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((DoubleBuffer)var2, 4);
      nglGetVertexAttribLdvEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribLdvEXT(int var0, int var1, long var2, long var4);

   public static void glVertexArrayVertexAttribLOffsetEXT(int var0, int var1, int var2, int var3, int var4, int var5, long var6) {
      ARBVertexAttrib64bit.glVertexArrayVertexAttribLOffsetEXT(var0, var1, var2, var3, var4, var5, var6);
   }
}
