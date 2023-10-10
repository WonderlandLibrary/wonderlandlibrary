package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class INTELMapTexture {
   public static final int GL_TEXTURE_MEMORY_LAYOUT_INTEL = 33791;
   public static final int GL_LAYOUT_DEFAULT_INTEL = 0;
   public static final int GL_LAYOUT_LINEAR_INTEL = 1;
   public static final int GL_LAYOUT_LINEAR_CPU_CACHED_INTEL = 2;

   private INTELMapTexture() {
   }

   public static ByteBuffer glMapTexture2DINTEL(int var0, int var1, long var2, int var4, IntBuffer var5, IntBuffer var6, ByteBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glMapTexture2DINTEL;
      BufferChecks.checkFunctionAddress(var9);
      BufferChecks.checkBuffer((IntBuffer)var5, 1);
      BufferChecks.checkBuffer((IntBuffer)var6, 1);
      if (var7 != null) {
         BufferChecks.checkDirect(var7);
      }

      ByteBuffer var11 = nglMapTexture2DINTEL(var0, var1, var2, var4, MemoryUtil.getAddress(var5), MemoryUtil.getAddress(var6), var7, var9);
      return LWJGLUtil.CHECKS && var11 == null ? null : var11.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglMapTexture2DINTEL(int var0, int var1, long var2, int var4, long var5, long var7, ByteBuffer var9, long var10);

   public static void glUnmapTexture2DINTEL(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUnmapTexture2DINTEL;
      BufferChecks.checkFunctionAddress(var3);
      nglUnmapTexture2DINTEL(var0, var1, var3);
   }

   static native void nglUnmapTexture2DINTEL(int var0, int var1, long var2);

   public static void glSyncTextureINTEL(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glSyncTextureINTEL;
      BufferChecks.checkFunctionAddress(var2);
      nglSyncTextureINTEL(var0, var2);
   }

   static native void nglSyncTextureINTEL(int var0, long var1);
}
