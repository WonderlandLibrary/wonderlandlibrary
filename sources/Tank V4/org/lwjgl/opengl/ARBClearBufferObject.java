package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBClearBufferObject {
   private ARBClearBufferObject() {
   }

   public static void glClearBufferData(int var0, int var1, int var2, int var3, ByteBuffer var4) {
      GL43.glClearBufferData(var0, var1, var2, var3, var4);
   }

   public static void glClearBufferSubData(int var0, int var1, long var2, int var4, int var5, ByteBuffer var6) {
      GL43.glClearBufferSubData(var0, var1, var2, var4, var5, var6);
   }

   public static void glClearNamedBufferDataEXT(int var0, int var1, int var2, int var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glClearNamedBufferDataEXT;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkBuffer((ByteBuffer)var4, 1);
      nglClearNamedBufferDataEXT(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglClearNamedBufferDataEXT(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glClearNamedBufferSubDataEXT(int var0, int var1, long var2, int var4, int var5, ByteBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glClearNamedBufferSubDataEXT;
      BufferChecks.checkFunctionAddress(var8);
      BufferChecks.checkDirect(var6);
      nglClearNamedBufferSubDataEXT(var0, var1, var2, (long)var6.remaining(), var4, var5, MemoryUtil.getAddress(var6), var8);
   }

   static native void nglClearNamedBufferSubDataEXT(int var0, int var1, long var2, long var4, int var6, int var7, long var8, long var10);
}
