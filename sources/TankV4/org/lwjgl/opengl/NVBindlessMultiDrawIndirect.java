package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVBindlessMultiDrawIndirect {
   private NVBindlessMultiDrawIndirect() {
   }

   public static void glMultiDrawArraysIndirectBindlessNV(int var0, ByteBuffer var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMultiDrawArraysIndirectBindlessNV;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureIndirectBOdisabled(var5);
      BufferChecks.checkBuffer(var1, (var3 == 0 ? 20 + 24 * var4 : var3) * var2);
      nglMultiDrawArraysIndirectBindlessNV(var0, MemoryUtil.getAddress(var1), var2, var3, var4, var6);
   }

   static native void nglMultiDrawArraysIndirectBindlessNV(int var0, long var1, int var3, int var4, int var5, long var6);

   public static void glMultiDrawArraysIndirectBindlessNV(int var0, long var1, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glMultiDrawArraysIndirectBindlessNV;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureIndirectBOenabled(var6);
      nglMultiDrawArraysIndirectBindlessNVBO(var0, var1, var3, var4, var5, var7);
   }

   static native void nglMultiDrawArraysIndirectBindlessNVBO(int var0, long var1, int var3, int var4, int var5, long var6);

   public static void glMultiDrawElementsIndirectBindlessNV(int var0, int var1, ByteBuffer var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glMultiDrawElementsIndirectBindlessNV;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureIndirectBOdisabled(var6);
      BufferChecks.checkBuffer(var2, (var4 == 0 ? 48 + 24 * var5 : var4) * var3);
      nglMultiDrawElementsIndirectBindlessNV(var0, var1, MemoryUtil.getAddress(var2), var3, var4, var5, var7);
   }

   static native void nglMultiDrawElementsIndirectBindlessNV(int var0, int var1, long var2, int var4, int var5, int var6, long var7);

   public static void glMultiDrawElementsIndirectBindlessNV(int var0, int var1, long var2, int var4, int var5, int var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glMultiDrawElementsIndirectBindlessNV;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureIndirectBOenabled(var7);
      nglMultiDrawElementsIndirectBindlessNVBO(var0, var1, var2, var4, var5, var6, var8);
   }

   static native void nglMultiDrawElementsIndirectBindlessNVBO(int var0, int var1, long var2, int var4, int var5, int var6, long var7);
}
