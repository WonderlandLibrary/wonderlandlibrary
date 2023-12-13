package org.lwjgl.opengl;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVPresentVideo {
   public static final int GL_FRAME_NV = 36390;
   public static final int FIELDS_NV = 36391;
   public static final int GL_CURRENT_TIME_NV = 36392;
   public static final int GL_NUM_FILL_STREAMS_NV = 36393;
   public static final int GL_PRESENT_TIME_NV = 36394;
   public static final int GL_PRESENT_DURATION_NV = 36395;
   public static final int GL_NUM_VIDEO_SLOTS_NV = 8432;

   private NVPresentVideo() {
   }

   public static void glPresentFrameKeyedNV(int var0, long var1, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glPresentFrameKeyedNV;
      BufferChecks.checkFunctionAddress(var13);
      nglPresentFrameKeyedNV(var0, var1, var3, var4, var5, var6, var7, var8, var9, var10, var11, var13);
   }

   static native void nglPresentFrameKeyedNV(int var0, long var1, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, long var12);

   public static void glPresentFrameDualFillNV(int var0, long var1, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13) {
      ContextCapabilities var14 = GLContext.getCapabilities();
      long var15 = var14.glPresentFrameDualFillNV;
      BufferChecks.checkFunctionAddress(var15);
      nglPresentFrameDualFillNV(var0, var1, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var15);
   }

   static native void nglPresentFrameDualFillNV(int var0, long var1, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, long var14);

   public static void glGetVideoNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVideoivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetVideoivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVideoivNV(int var0, int var1, long var2, long var4);

   public static int glGetVideoiNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetVideoivNV;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetVideoivNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetVideouNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVideouivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetVideouivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVideouivNV(int var0, int var1, long var2, long var4);

   public static int glGetVideouiNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetVideouivNV;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetVideouivNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetVideoNV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVideoi64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((LongBuffer)var2, 1);
      nglGetVideoi64vNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVideoi64vNV(int var0, int var1, long var2, long var4);

   public static long glGetVideoi64NV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetVideoi64vNV;
      BufferChecks.checkFunctionAddress(var3);
      LongBuffer var5 = APIUtil.getBufferLong(var2);
      nglGetVideoi64vNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetVideouNV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVideoui64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((LongBuffer)var2, 1);
      nglGetVideoui64vNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVideoui64vNV(int var0, int var1, long var2, long var4);

   public static long glGetVideoui64NV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetVideoui64vNV;
      BufferChecks.checkFunctionAddress(var3);
      LongBuffer var5 = APIUtil.getBufferLong(var2);
      nglGetVideoui64vNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }
}
