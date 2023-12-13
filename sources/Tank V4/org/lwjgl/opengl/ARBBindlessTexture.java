package org.lwjgl.opengl;

import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBBindlessTexture {
   public static final int GL_UNSIGNED_INT64_ARB = 5135;

   private ARBBindlessTexture() {
   }

   public static long glGetTextureHandleARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGetTextureHandleARB;
      BufferChecks.checkFunctionAddress(var2);
      long var4 = nglGetTextureHandleARB(var0, var2);
      return var4;
   }

   static native long nglGetTextureHandleARB(int var0, long var1);

   public static long glGetTextureSamplerHandleARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetTextureSamplerHandleARB;
      BufferChecks.checkFunctionAddress(var3);
      long var5 = nglGetTextureSamplerHandleARB(var0, var1, var3);
      return var5;
   }

   static native long nglGetTextureSamplerHandleARB(int var0, int var1, long var2);

   public static void glMakeTextureHandleResidentARB(long var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMakeTextureHandleResidentARB;
      BufferChecks.checkFunctionAddress(var3);
      nglMakeTextureHandleResidentARB(var0, var3);
   }

   static native void nglMakeTextureHandleResidentARB(long var0, long var2);

   public static void glMakeTextureHandleNonResidentARB(long var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMakeTextureHandleNonResidentARB;
      BufferChecks.checkFunctionAddress(var3);
      nglMakeTextureHandleNonResidentARB(var0, var3);
   }

   static native void nglMakeTextureHandleNonResidentARB(long var0, long var2);

   public static long glGetImageHandleARB(int var0, int var1, boolean var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetImageHandleARB;
      BufferChecks.checkFunctionAddress(var6);
      long var8 = nglGetImageHandleARB(var0, var1, var2, var3, var4, var6);
      return var8;
   }

   static native long nglGetImageHandleARB(int var0, int var1, boolean var2, int var3, int var4, long var5);

   public static void glMakeImageHandleResidentARB(long var0, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMakeImageHandleResidentARB;
      BufferChecks.checkFunctionAddress(var4);
      nglMakeImageHandleResidentARB(var0, var2, var4);
   }

   static native void nglMakeImageHandleResidentARB(long var0, int var2, long var3);

   public static void glMakeImageHandleNonResidentARB(long var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMakeImageHandleNonResidentARB;
      BufferChecks.checkFunctionAddress(var3);
      nglMakeImageHandleNonResidentARB(var0, var3);
   }

   static native void nglMakeImageHandleNonResidentARB(long var0, long var2);

   public static void glUniformHandleui64ARB(int var0, long var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformHandleui64ARB;
      BufferChecks.checkFunctionAddress(var4);
      nglUniformHandleui64ARB(var0, var1, var4);
   }

   static native void nglUniformHandleui64ARB(int var0, long var1, long var3);

   public static void glUniformHandleuARB(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniformHandleui64vARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniformHandleui64vARB(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniformHandleui64vARB(int var0, int var1, long var2, long var4);

   public static void glProgramUniformHandleui64ARB(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformHandleui64ARB;
      BufferChecks.checkFunctionAddress(var5);
      nglProgramUniformHandleui64ARB(var0, var1, var2, var5);
   }

   static native void nglProgramUniformHandleui64ARB(int var0, int var1, long var2, long var4);

   public static void glProgramUniformHandleuARB(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniformHandleui64vARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniformHandleui64vARB(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniformHandleui64vARB(int var0, int var1, int var2, long var3, long var5);

   public static boolean glIsTextureHandleResidentARB(long var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glIsTextureHandleResidentARB;
      BufferChecks.checkFunctionAddress(var3);
      boolean var5 = nglIsTextureHandleResidentARB(var0, var3);
      return var5;
   }

   static native boolean nglIsTextureHandleResidentARB(long var0, long var2);

   public static boolean glIsImageHandleResidentARB(long var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glIsImageHandleResidentARB;
      BufferChecks.checkFunctionAddress(var3);
      boolean var5 = nglIsImageHandleResidentARB(var0, var3);
      return var5;
   }

   static native boolean nglIsImageHandleResidentARB(long var0, long var2);

   public static void glVertexAttribL1ui64ARB(int var0, long var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttribL1ui64ARB;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttribL1ui64ARB(var0, var1, var4);
   }

   static native void nglVertexAttribL1ui64ARB(int var0, long var1, long var3);

   public static void glVertexAttribL1uARB(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribL1ui64vARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((LongBuffer)var1, 1);
      nglVertexAttribL1ui64vARB(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribL1ui64vARB(int var0, long var1, long var3);

   public static void glGetVertexAttribLuARB(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribLui64vARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((LongBuffer)var2, 4);
      nglGetVertexAttribLui64vARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribLui64vARB(int var0, int var1, long var2, long var4);
}
