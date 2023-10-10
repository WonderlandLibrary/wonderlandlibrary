package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class EXTMultiDrawArrays {
   private EXTMultiDrawArrays() {
   }

   public static void glMultiDrawArraysEXT(int var0, IntBuffer var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiDrawArraysEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkBuffer(var2, var1.remaining());
      nglMultiDrawArraysEXT(var0, MemoryUtil.getAddress(var1), MemoryUtil.getAddress(var2), var1.remaining(), var4);
   }

   static native void nglMultiDrawArraysEXT(int var0, long var1, long var3, int var5, long var6);
}
