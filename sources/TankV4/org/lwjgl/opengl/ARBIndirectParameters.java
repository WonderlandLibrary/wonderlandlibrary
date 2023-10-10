package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBIndirectParameters {
   public static final int GL_PARAMETER_BUFFER_ARB = 33006;
   public static final int GL_PARAMETER_BUFFER_BINDING_ARB = 33007;

   private ARBIndirectParameters() {
   }

   public static void glMultiDrawArraysIndirectCountARB(int var0, ByteBuffer var1, long var2, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glMultiDrawArraysIndirectCountARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureIndirectBOdisabled(var6);
      BufferChecks.checkBuffer(var1, (var5 == 0 ? 16 : var5) * var4);
      nglMultiDrawArraysIndirectCountARB(var0, MemoryUtil.getAddress(var1), var2, var4, var5, var7);
   }

   static native void nglMultiDrawArraysIndirectCountARB(int var0, long var1, long var3, int var5, int var6, long var7);

   public static void glMultiDrawArraysIndirectCountARB(int var0, long var1, long var3, int var5, int var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glMultiDrawArraysIndirectCountARB;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureIndirectBOenabled(var7);
      nglMultiDrawArraysIndirectCountARBBO(var0, var1, var3, var5, var6, var8);
   }

   static native void nglMultiDrawArraysIndirectCountARBBO(int var0, long var1, long var3, int var5, int var6, long var7);

   public static void glMultiDrawArraysIndirectCountARB(int var0, IntBuffer var1, long var2, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glMultiDrawArraysIndirectCountARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureIndirectBOdisabled(var6);
      BufferChecks.checkBuffer(var1, (var5 == 0 ? 4 : var5 >> 2) * var4);
      nglMultiDrawArraysIndirectCountARB(var0, MemoryUtil.getAddress(var1), var2, var4, var5, var7);
   }

   public static void glMultiDrawElementsIndirectCountARB(int var0, int var1, ByteBuffer var2, long var3, int var5, int var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glMultiDrawElementsIndirectCountARB;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureIndirectBOdisabled(var7);
      BufferChecks.checkBuffer(var2, (var6 == 0 ? 20 : var6) * var5);
      nglMultiDrawElementsIndirectCountARB(var0, var1, MemoryUtil.getAddress(var2), var3, var5, var6, var8);
   }

   static native void nglMultiDrawElementsIndirectCountARB(int var0, int var1, long var2, long var4, int var6, int var7, long var8);

   public static void glMultiDrawElementsIndirectCountARB(int var0, int var1, long var2, long var4, int var6, int var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glMultiDrawElementsIndirectCountARB;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureIndirectBOenabled(var8);
      nglMultiDrawElementsIndirectCountARBBO(var0, var1, var2, var4, var6, var7, var9);
   }

   static native void nglMultiDrawElementsIndirectCountARBBO(int var0, int var1, long var2, long var4, int var6, int var7, long var8);

   public static void glMultiDrawElementsIndirectCountARB(int var0, int var1, IntBuffer var2, long var3, int var5, int var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glMultiDrawElementsIndirectCountARB;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureIndirectBOdisabled(var7);
      BufferChecks.checkBuffer(var2, (var6 == 0 ? 5 : var6 >> 2) * var5);
      nglMultiDrawElementsIndirectCountARB(var0, var1, MemoryUtil.getAddress(var2), var3, var5, var6, var8);
   }
}
