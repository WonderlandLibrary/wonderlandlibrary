package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class APPLETextureRange {
   public static final int GL_TEXTURE_STORAGE_HINT_APPLE = 34236;
   public static final int GL_STORAGE_PRIVATE_APPLE = 34237;
   public static final int GL_STORAGE_CACHED_APPLE = 34238;
   public static final int GL_STORAGE_SHARED_APPLE = 34239;
   public static final int GL_TEXTURE_RANGE_LENGTH_APPLE = 34231;
   public static final int GL_TEXTURE_RANGE_POINTER_APPLE = 34232;

   private APPLETextureRange() {
   }

   public static void glTextureRangeAPPLE(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glTextureRangeAPPLE;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglTextureRangeAPPLE(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglTextureRangeAPPLE(int var0, int var1, long var2, long var4);

   public static Buffer glGetTexParameterPointervAPPLE(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetTexParameterPointervAPPLE;
      BufferChecks.checkFunctionAddress(var5);
      Buffer var7 = nglGetTexParameterPointervAPPLE(var0, var1, var2, var5);
      return var7;
   }

   static native Buffer nglGetTexParameterPointervAPPLE(int var0, int var1, long var2, long var4);
}
