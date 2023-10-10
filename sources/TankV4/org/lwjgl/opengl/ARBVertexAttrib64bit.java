package org.lwjgl.opengl;

import java.nio.DoubleBuffer;
import org.lwjgl.BufferChecks;

public final class ARBVertexAttrib64bit {
   public static final int GL_DOUBLE_VEC2 = 36860;
   public static final int GL_DOUBLE_VEC3 = 36861;
   public static final int GL_DOUBLE_VEC4 = 36862;
   public static final int GL_DOUBLE_MAT2 = 36678;
   public static final int GL_DOUBLE_MAT3 = 36679;
   public static final int GL_DOUBLE_MAT4 = 36680;
   public static final int GL_DOUBLE_MAT2x3 = 36681;
   public static final int GL_DOUBLE_MAT2x4 = 36682;
   public static final int GL_DOUBLE_MAT3x2 = 36683;
   public static final int GL_DOUBLE_MAT3x4 = 36684;
   public static final int GL_DOUBLE_MAT4x2 = 36685;
   public static final int GL_DOUBLE_MAT4x3 = 36686;

   private ARBVertexAttrib64bit() {
   }

   public static void glVertexAttribL1d(int var0, double var1) {
      GL41.glVertexAttribL1d(var0, var1);
   }

   public static void glVertexAttribL2d(int var0, double var1, double var3) {
      GL41.glVertexAttribL2d(var0, var1, var3);
   }

   public static void glVertexAttribL3d(int var0, double var1, double var3, double var5) {
      GL41.glVertexAttribL3d(var0, var1, var3, var5);
   }

   public static void glVertexAttribL4d(int var0, double var1, double var3, double var5, double var7) {
      GL41.glVertexAttribL4d(var0, var1, var3, var5, var7);
   }

   public static void glVertexAttribL1(int var0, DoubleBuffer var1) {
      GL41.glVertexAttribL1(var0, var1);
   }

   public static void glVertexAttribL2(int var0, DoubleBuffer var1) {
      GL41.glVertexAttribL2(var0, var1);
   }

   public static void glVertexAttribL3(int var0, DoubleBuffer var1) {
      GL41.glVertexAttribL3(var0, var1);
   }

   public static void glVertexAttribL4(int var0, DoubleBuffer var1) {
      GL41.glVertexAttribL4(var0, var1);
   }

   public static void glVertexAttribLPointer(int var0, int var1, int var2, DoubleBuffer var3) {
      GL41.glVertexAttribLPointer(var0, var1, var2, var3);
   }

   public static void glVertexAttribLPointer(int var0, int var1, int var2, long var3) {
      GL41.glVertexAttribLPointer(var0, var1, var2, var3);
   }

   public static void glGetVertexAttribL(int var0, int var1, DoubleBuffer var2) {
      GL41.glGetVertexAttribL(var0, var1, var2);
   }

   public static void glVertexArrayVertexAttribLOffsetEXT(int var0, int var1, int var2, int var3, int var4, int var5, long var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glVertexArrayVertexAttribLOffsetEXT;
      BufferChecks.checkFunctionAddress(var9);
      nglVertexArrayVertexAttribLOffsetEXT(var0, var1, var2, var3, var4, var5, var6, var9);
   }

   static native void nglVertexArrayVertexAttribLOffsetEXT(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8);
}
