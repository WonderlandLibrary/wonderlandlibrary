package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBTransposeMatrix {
   public static final int GL_TRANSPOSE_MODELVIEW_MATRIX_ARB = 34019;
   public static final int GL_TRANSPOSE_PROJECTION_MATRIX_ARB = 34020;
   public static final int GL_TRANSPOSE_TEXTURE_MATRIX_ARB = 34021;
   public static final int GL_TRANSPOSE_COLOR_MATRIX_ARB = 34022;

   private ARBTransposeMatrix() {
   }

   public static void glLoadTransposeMatrixARB(FloatBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glLoadTransposeMatrixfARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkBuffer((FloatBuffer)var0, 16);
      nglLoadTransposeMatrixfARB(MemoryUtil.getAddress(var0), var2);
   }

   static native void nglLoadTransposeMatrixfARB(long var0, long var2);

   public static void glMultTransposeMatrixARB(FloatBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glMultTransposeMatrixfARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkBuffer((FloatBuffer)var0, 16);
      nglMultTransposeMatrixfARB(MemoryUtil.getAddress(var0), var2);
   }

   static native void nglMultTransposeMatrixfARB(long var0, long var2);
}
