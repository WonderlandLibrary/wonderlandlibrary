package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class ARBMatrixPalette {
   public static final int GL_MATRIX_PALETTE_ARB = 34880;
   public static final int GL_MAX_MATRIX_PALETTE_STACK_DEPTH_ARB = 34881;
   public static final int GL_MAX_PALETTE_MATRICES_ARB = 34882;
   public static final int GL_CURRENT_PALETTE_MATRIX_ARB = 34883;
   public static final int GL_MATRIX_INDEX_ARRAY_ARB = 34884;
   public static final int GL_CURRENT_MATRIX_INDEX_ARB = 34885;
   public static final int GL_MATRIX_INDEX_ARRAY_SIZE_ARB = 34886;
   public static final int GL_MATRIX_INDEX_ARRAY_TYPE_ARB = 34887;
   public static final int GL_MATRIX_INDEX_ARRAY_STRIDE_ARB = 34888;
   public static final int GL_MATRIX_INDEX_ARRAY_POINTER_ARB = 34889;

   private ARBMatrixPalette() {
   }

   public static void glCurrentPaletteMatrixARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glCurrentPaletteMatrixARB;
      BufferChecks.checkFunctionAddress(var2);
      nglCurrentPaletteMatrixARB(var0, var2);
   }

   static native void nglCurrentPaletteMatrixARB(int var0, long var1);

   public static void glMatrixIndexPointerARB(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMatrixIndexPointerARB;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).ARB_matrix_palette_glMatrixIndexPointerARB_pPointer = var2;
      }

      nglMatrixIndexPointerARB(var0, 5121, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glMatrixIndexPointerARB(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMatrixIndexPointerARB;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).ARB_matrix_palette_glMatrixIndexPointerARB_pPointer = var2;
      }

      nglMatrixIndexPointerARB(var0, 5125, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glMatrixIndexPointerARB(int var0, int var1, ShortBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMatrixIndexPointerARB;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).ARB_matrix_palette_glMatrixIndexPointerARB_pPointer = var2;
      }

      nglMatrixIndexPointerARB(var0, 5123, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglMatrixIndexPointerARB(int var0, int var1, int var2, long var3, long var5);

   public static void glMatrixIndexPointerARB(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMatrixIndexPointerARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOenabled(var5);
      nglMatrixIndexPointerARBBO(var0, var1, var2, var3, var6);
   }

   static native void nglMatrixIndexPointerARBBO(int var0, int var1, int var2, long var3, long var5);

   public static void glMatrixIndexuARB(ByteBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glMatrixIndexubvARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglMatrixIndexubvARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglMatrixIndexubvARB(int var0, long var1, long var3);

   public static void glMatrixIndexuARB(ShortBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glMatrixIndexusvARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglMatrixIndexusvARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglMatrixIndexusvARB(int var0, long var1, long var3);

   public static void glMatrixIndexuARB(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glMatrixIndexuivARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglMatrixIndexuivARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglMatrixIndexuivARB(int var0, long var1, long var3);
}
