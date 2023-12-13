package org.lwjgl.opengl;

import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class EXTTimerQuery {
   public static final int GL_TIME_ELAPSED_EXT = 35007;

   private EXTTimerQuery() {
   }

   public static void glGetQueryObjectEXT(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetQueryObjecti64vEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((LongBuffer)var2, 1);
      nglGetQueryObjecti64vEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetQueryObjecti64vEXT(int var0, int var1, long var2, long var4);

   public static long glGetQueryObjectEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetQueryObjecti64vEXT;
      BufferChecks.checkFunctionAddress(var3);
      LongBuffer var5 = APIUtil.getBufferLong(var2);
      nglGetQueryObjecti64vEXT(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetQueryObjectuEXT(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetQueryObjectui64vEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((LongBuffer)var2, 1);
      nglGetQueryObjectui64vEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetQueryObjectui64vEXT(int var0, int var1, long var2, long var4);

   public static long glGetQueryObjectuEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetQueryObjectui64vEXT;
      BufferChecks.checkFunctionAddress(var3);
      LongBuffer var5 = APIUtil.getBufferLong(var2);
      nglGetQueryObjectui64vEXT(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }
}
