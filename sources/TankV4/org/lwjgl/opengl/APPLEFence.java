package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class APPLEFence {
   public static final int GL_DRAW_PIXELS_APPLE = 35338;
   public static final int GL_FENCE_APPLE = 35339;

   private APPLEFence() {
   }

   public static void glGenFencesAPPLE(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenFencesAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenFencesAPPLE(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenFencesAPPLE(int var0, long var1, long var3);

   public static int glGenFencesAPPLE() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenFencesAPPLE;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenFencesAPPLE(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static void glDeleteFencesAPPLE(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteFencesAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteFencesAPPLE(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteFencesAPPLE(int var0, long var1, long var3);

   public static void glDeleteFencesAPPLE(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteFencesAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteFencesAPPLE(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glSetFenceAPPLE(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glSetFenceAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      nglSetFenceAPPLE(var0, var2);
   }

   static native void nglSetFenceAPPLE(int var0, long var1);

   public static boolean glIsFenceAPPLE(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsFenceAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsFenceAPPLE(var0, var2);
      return var4;
   }

   static native boolean nglIsFenceAPPLE(int var0, long var1);

   public static boolean glTestFenceAPPLE(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glTestFenceAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglTestFenceAPPLE(var0, var2);
      return var4;
   }

   static native boolean nglTestFenceAPPLE(int var0, long var1);

   public static void glFinishFenceAPPLE(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glFinishFenceAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      nglFinishFenceAPPLE(var0, var2);
   }

   static native void nglFinishFenceAPPLE(int var0, long var1);

   public static boolean glTestObjectAPPLE(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glTestObjectAPPLE;
      BufferChecks.checkFunctionAddress(var3);
      boolean var5 = nglTestObjectAPPLE(var0, var1, var3);
      return var5;
   }

   static native boolean nglTestObjectAPPLE(int var0, int var1, long var2);

   public static void glFinishObjectAPPLE(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glFinishObjectAPPLE;
      BufferChecks.checkFunctionAddress(var3);
      nglFinishObjectAPPLE(var0, var1, var3);
   }

   static native void nglFinishObjectAPPLE(int var0, int var1, long var2);
}
