package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class APPLEFlushBufferRange {
   public static final int GL_BUFFER_SERIALIZED_MODIFY_APPLE = 35346;
   public static final int GL_BUFFER_FLUSHING_UNMAP_APPLE = 35347;

   private APPLEFlushBufferRange() {
   }

   public static void glBufferParameteriAPPLE(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBufferParameteriAPPLE;
      BufferChecks.checkFunctionAddress(var4);
      nglBufferParameteriAPPLE(var0, var1, var2, var4);
   }

   static native void nglBufferParameteriAPPLE(int var0, int var1, int var2, long var3);

   public static void glFlushMappedBufferRangeAPPLE(int var0, long var1, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glFlushMappedBufferRangeAPPLE;
      BufferChecks.checkFunctionAddress(var6);
      nglFlushMappedBufferRangeAPPLE(var0, var1, var3, var6);
   }

   static native void nglFlushMappedBufferRangeAPPLE(int var0, long var1, long var3, long var5);
}
