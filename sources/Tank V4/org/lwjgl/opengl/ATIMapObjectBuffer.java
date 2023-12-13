package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;

public final class ATIMapObjectBuffer {
   private ATIMapObjectBuffer() {
   }

   public static ByteBuffer glMapObjectBufferATI(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMapObjectBufferATI;
      BufferChecks.checkFunctionAddress(var3);
      if (var1 != null) {
         BufferChecks.checkDirect(var1);
      }

      ByteBuffer var5 = nglMapObjectBufferATI(var0, (long)GLChecks.getBufferObjectSizeATI(var2, var0), var1, var3);
      return LWJGLUtil.CHECKS && var5 == null ? null : var5.order(ByteOrder.nativeOrder());
   }

   public static ByteBuffer glMapObjectBufferATI(int var0, long var1, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMapObjectBufferATI;
      BufferChecks.checkFunctionAddress(var5);
      if (var3 != null) {
         BufferChecks.checkDirect(var3);
      }

      ByteBuffer var7 = nglMapObjectBufferATI(var0, var1, var3, var5);
      return LWJGLUtil.CHECKS && var7 == null ? null : var7.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglMapObjectBufferATI(int var0, long var1, ByteBuffer var3, long var4);

   public static void glUnmapObjectBufferATI(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glUnmapObjectBufferATI;
      BufferChecks.checkFunctionAddress(var2);
      nglUnmapObjectBufferATI(var0, var2);
   }

   static native void nglUnmapObjectBufferATI(int var0, long var1);
}
