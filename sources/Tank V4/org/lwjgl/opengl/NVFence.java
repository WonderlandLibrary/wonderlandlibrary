package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVFence {
   public static final int GL_ALL_COMPLETED_NV = 34034;
   public static final int GL_FENCE_STATUS_NV = 34035;
   public static final int GL_FENCE_CONDITION_NV = 34036;

   private NVFence() {
   }

   public static void glGenFencesNV(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenFencesNV;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenFencesNV(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenFencesNV(int var0, long var1, long var3);

   public static int glGenFencesNV() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenFencesNV;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenFencesNV(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static void glDeleteFencesNV(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteFencesNV;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteFencesNV(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteFencesNV(int var0, long var1, long var3);

   public static void glDeleteFencesNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteFencesNV;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteFencesNV(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glSetFenceNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glSetFenceNV;
      BufferChecks.checkFunctionAddress(var3);
      nglSetFenceNV(var0, var1, var3);
   }

   static native void nglSetFenceNV(int var0, int var1, long var2);

   public static boolean glTestFenceNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glTestFenceNV;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglTestFenceNV(var0, var2);
      return var4;
   }

   static native boolean nglTestFenceNV(int var0, long var1);

   public static void glFinishFenceNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glFinishFenceNV;
      BufferChecks.checkFunctionAddress(var2);
      nglFinishFenceNV(var0, var2);
   }

   static native void nglFinishFenceNV(int var0, long var1);

   public static boolean glIsFenceNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsFenceNV;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsFenceNV(var0, var2);
      return var4;
   }

   static native boolean nglIsFenceNV(int var0, long var1);

   public static void glGetFenceivNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetFenceivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetFenceivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetFenceivNV(int var0, int var1, long var2, long var4);
}
