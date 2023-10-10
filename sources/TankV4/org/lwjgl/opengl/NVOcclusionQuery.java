package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVOcclusionQuery {
   public static final int GL_OCCLUSION_TEST_HP = 33125;
   public static final int GL_OCCLUSION_TEST_RESULT_HP = 33126;
   public static final int GL_PIXEL_COUNTER_BITS_NV = 34916;
   public static final int GL_CURRENT_OCCLUSION_QUERY_ID_NV = 34917;
   public static final int GL_PIXEL_COUNT_NV = 34918;
   public static final int GL_PIXEL_COUNT_AVAILABLE_NV = 34919;

   private NVOcclusionQuery() {
   }

   public static void glGenOcclusionQueriesNV(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenOcclusionQueriesNV;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenOcclusionQueriesNV(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenOcclusionQueriesNV(int var0, long var1, long var3);

   public static int glGenOcclusionQueriesNV() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenOcclusionQueriesNV;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenOcclusionQueriesNV(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static void glDeleteOcclusionQueriesNV(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteOcclusionQueriesNV;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteOcclusionQueriesNV(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteOcclusionQueriesNV(int var0, long var1, long var3);

   public static void glDeleteOcclusionQueriesNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteOcclusionQueriesNV;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteOcclusionQueriesNV(1, APIUtil.getInt(var1, var0), var2);
   }

   public static boolean glIsOcclusionQueryNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsOcclusionQueryNV;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsOcclusionQueryNV(var0, var2);
      return var4;
   }

   static native boolean nglIsOcclusionQueryNV(int var0, long var1);

   public static void glBeginOcclusionQueryNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glBeginOcclusionQueryNV;
      BufferChecks.checkFunctionAddress(var2);
      nglBeginOcclusionQueryNV(var0, var2);
   }

   static native void nglBeginOcclusionQueryNV(int var0, long var1);

   public static void glEndOcclusionQueryNV() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glEndOcclusionQueryNV;
      BufferChecks.checkFunctionAddress(var1);
      nglEndOcclusionQueryNV(var1);
   }

   static native void nglEndOcclusionQueryNV(long var0);

   public static void glGetOcclusionQueryuNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetOcclusionQueryuivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetOcclusionQueryuivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetOcclusionQueryuivNV(int var0, int var1, long var2, long var4);

   public static int glGetOcclusionQueryuiNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetOcclusionQueryuivNV;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetOcclusionQueryuivNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetOcclusionQueryNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetOcclusionQueryivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetOcclusionQueryivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetOcclusionQueryivNV(int var0, int var1, long var2, long var4);

   public static int glGetOcclusionQueryiNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetOcclusionQueryivNV;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetOcclusionQueryivNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }
}
