package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class AMDNameGenDelete {
   public static final int GL_DATA_BUFFER_AMD = 37201;
   public static final int GL_PERFORMANCE_MONITOR_AMD = 37202;
   public static final int GL_QUERY_OBJECT_AMD = 37203;
   public static final int GL_VERTEX_ARRAY_OBJECT_AMD = 37204;
   public static final int GL_SAMPLER_OBJECT_AMD = 37205;

   private AMDNameGenDelete() {
   }

   public static void glGenNamesAMD(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGenNamesAMD;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglGenNamesAMD(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGenNamesAMD(int var0, int var1, long var2, long var4);

   public static int glGenNamesAMD(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenNamesAMD;
      BufferChecks.checkFunctionAddress(var2);
      IntBuffer var4 = APIUtil.getBufferInt(var1);
      nglGenNamesAMD(var0, 1, MemoryUtil.getAddress(var4), var2);
      return var4.get(0);
   }

   public static void glDeleteNamesAMD(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDeleteNamesAMD;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglDeleteNamesAMD(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglDeleteNamesAMD(int var0, int var1, long var2, long var4);

   public static void glDeleteNamesAMD(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDeleteNamesAMD;
      BufferChecks.checkFunctionAddress(var3);
      nglDeleteNamesAMD(var0, 1, APIUtil.getInt(var2, var1), var3);
   }

   public static boolean glIsNameAMD(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glIsNameAMD;
      BufferChecks.checkFunctionAddress(var3);
      boolean var5 = nglIsNameAMD(var0, var1, var3);
      return var5;
   }

   static native boolean nglIsNameAMD(int var0, int var1, long var2);
}
