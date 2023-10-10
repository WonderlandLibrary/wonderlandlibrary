package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class AMDMultiDrawIndirect {
   private AMDMultiDrawIndirect() {
   }

   public static void glMultiDrawArraysIndirectAMD(int var0, ByteBuffer var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiDrawArraysIndirectAMD;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureIndirectBOdisabled(var4);
      BufferChecks.checkBuffer(var1, (var3 == 0 ? 16 : var3) * var2);
      nglMultiDrawArraysIndirectAMD(var0, MemoryUtil.getAddress(var1), var2, var3, var5);
   }

   static native void nglMultiDrawArraysIndirectAMD(int var0, long var1, int var3, int var4, long var5);

   public static void glMultiDrawArraysIndirectAMD(int var0, long var1, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMultiDrawArraysIndirectAMD;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureIndirectBOenabled(var5);
      nglMultiDrawArraysIndirectAMDBO(var0, var1, var3, var4, var6);
   }

   static native void nglMultiDrawArraysIndirectAMDBO(int var0, long var1, int var3, int var4, long var5);

   public static void glMultiDrawArraysIndirectAMD(int var0, IntBuffer var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiDrawArraysIndirectAMD;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureIndirectBOdisabled(var4);
      BufferChecks.checkBuffer(var1, (var3 == 0 ? 4 : var3 >> 2) * var2);
      nglMultiDrawArraysIndirectAMD(var0, MemoryUtil.getAddress(var1), var2, var3, var5);
   }

   public static void glMultiDrawElementsIndirectAMD(int var0, int var1, ByteBuffer var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMultiDrawElementsIndirectAMD;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureIndirectBOdisabled(var5);
      BufferChecks.checkBuffer(var2, (var4 == 0 ? 20 : var4) * var3);
      nglMultiDrawElementsIndirectAMD(var0, var1, MemoryUtil.getAddress(var2), var3, var4, var6);
   }

   static native void nglMultiDrawElementsIndirectAMD(int var0, int var1, long var2, int var4, int var5, long var6);

   public static void glMultiDrawElementsIndirectAMD(int var0, int var1, long var2, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glMultiDrawElementsIndirectAMD;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureIndirectBOenabled(var6);
      nglMultiDrawElementsIndirectAMDBO(var0, var1, var2, var4, var5, var7);
   }

   static native void nglMultiDrawElementsIndirectAMDBO(int var0, int var1, long var2, int var4, int var5, long var6);

   public static void glMultiDrawElementsIndirectAMD(int var0, int var1, IntBuffer var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMultiDrawElementsIndirectAMD;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureIndirectBOdisabled(var5);
      BufferChecks.checkBuffer(var2, (var4 == 0 ? 5 : var4 >> 2) * var3);
      nglMultiDrawElementsIndirectAMD(var0, var1, MemoryUtil.getAddress(var2), var3, var4, var6);
   }
}
