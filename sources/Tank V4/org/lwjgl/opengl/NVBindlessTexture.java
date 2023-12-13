package org.lwjgl.opengl;

import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVBindlessTexture {
   private NVBindlessTexture() {
   }

   public static long glGetTextureHandleNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGetTextureHandleNV;
      BufferChecks.checkFunctionAddress(var2);
      long var4 = nglGetTextureHandleNV(var0, var2);
      return var4;
   }

   static native long nglGetTextureHandleNV(int var0, long var1);

   public static long glGetTextureSamplerHandleNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetTextureSamplerHandleNV;
      BufferChecks.checkFunctionAddress(var3);
      long var5 = nglGetTextureSamplerHandleNV(var0, var1, var3);
      return var5;
   }

   static native long nglGetTextureSamplerHandleNV(int var0, int var1, long var2);

   public static void glMakeTextureHandleResidentNV(long var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMakeTextureHandleResidentNV;
      BufferChecks.checkFunctionAddress(var3);
      nglMakeTextureHandleResidentNV(var0, var3);
   }

   static native void nglMakeTextureHandleResidentNV(long var0, long var2);

   public static void glMakeTextureHandleNonResidentNV(long var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMakeTextureHandleNonResidentNV;
      BufferChecks.checkFunctionAddress(var3);
      nglMakeTextureHandleNonResidentNV(var0, var3);
   }

   static native void nglMakeTextureHandleNonResidentNV(long var0, long var2);

   public static long glGetImageHandleNV(int var0, int var1, boolean var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetImageHandleNV;
      BufferChecks.checkFunctionAddress(var6);
      long var8 = nglGetImageHandleNV(var0, var1, var2, var3, var4, var6);
      return var8;
   }

   static native long nglGetImageHandleNV(int var0, int var1, boolean var2, int var3, int var4, long var5);

   public static void glMakeImageHandleResidentNV(long var0, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMakeImageHandleResidentNV;
      BufferChecks.checkFunctionAddress(var4);
      nglMakeImageHandleResidentNV(var0, var2, var4);
   }

   static native void nglMakeImageHandleResidentNV(long var0, int var2, long var3);

   public static void glMakeImageHandleNonResidentNV(long var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMakeImageHandleNonResidentNV;
      BufferChecks.checkFunctionAddress(var3);
      nglMakeImageHandleNonResidentNV(var0, var3);
   }

   static native void nglMakeImageHandleNonResidentNV(long var0, long var2);

   public static void glUniformHandleui64NV(int var0, long var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformHandleui64NV;
      BufferChecks.checkFunctionAddress(var4);
      nglUniformHandleui64NV(var0, var1, var4);
   }

   static native void nglUniformHandleui64NV(int var0, long var1, long var3);

   public static void glUniformHandleuNV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniformHandleui64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniformHandleui64vNV(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniformHandleui64vNV(int var0, int var1, long var2, long var4);

   public static void glProgramUniformHandleui64NV(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformHandleui64NV;
      BufferChecks.checkFunctionAddress(var5);
      nglProgramUniformHandleui64NV(var0, var1, var2, var5);
   }

   static native void nglProgramUniformHandleui64NV(int var0, int var1, long var2, long var4);

   public static void glProgramUniformHandleuNV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniformHandleui64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniformHandleui64vNV(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniformHandleui64vNV(int var0, int var1, int var2, long var3, long var5);

   public static boolean glIsTextureHandleResidentNV(long var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glIsTextureHandleResidentNV;
      BufferChecks.checkFunctionAddress(var3);
      boolean var5 = nglIsTextureHandleResidentNV(var0, var3);
      return var5;
   }

   static native boolean nglIsTextureHandleResidentNV(long var0, long var2);

   public static boolean glIsImageHandleResidentNV(long var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glIsImageHandleResidentNV;
      BufferChecks.checkFunctionAddress(var3);
      boolean var5 = nglIsImageHandleResidentNV(var0, var3);
      return var5;
   }

   static native boolean nglIsImageHandleResidentNV(long var0, long var2);
}
