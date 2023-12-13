package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class APPLEObjectPurgeable {
   public static final int GL_RELEASED_APPLE = 35353;
   public static final int GL_VOLATILE_APPLE = 35354;
   public static final int GL_RETAINED_APPLE = 35355;
   public static final int GL_UNDEFINED_APPLE = 35356;
   public static final int GL_PURGEABLE_APPLE = 35357;
   public static final int GL_BUFFER_OBJECT_APPLE = 34227;

   private APPLEObjectPurgeable() {
   }

   public static int glObjectPurgeableAPPLE(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glObjectPurgeableAPPLE;
      BufferChecks.checkFunctionAddress(var4);
      int var6 = nglObjectPurgeableAPPLE(var0, var1, var2, var4);
      return var6;
   }

   static native int nglObjectPurgeableAPPLE(int var0, int var1, int var2, long var3);

   public static int glObjectUnpurgeableAPPLE(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glObjectUnpurgeableAPPLE;
      BufferChecks.checkFunctionAddress(var4);
      int var6 = nglObjectUnpurgeableAPPLE(var0, var1, var2, var4);
      return var6;
   }

   static native int nglObjectUnpurgeableAPPLE(int var0, int var1, int var2, long var3);

   public static void glGetObjectParameterAPPLE(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetObjectParameterivAPPLE;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      nglGetObjectParameterivAPPLE(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetObjectParameterivAPPLE(int var0, int var1, int var2, long var3, long var5);

   public static int glGetObjectParameteriAPPLE(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetObjectParameterivAPPLE;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetObjectParameterivAPPLE(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }
}
