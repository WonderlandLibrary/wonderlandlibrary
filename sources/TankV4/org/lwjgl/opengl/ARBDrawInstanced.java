package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBDrawInstanced {
   private ARBDrawInstanced() {
   }

   public static void glDrawArraysInstancedARB(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawArraysInstancedARB;
      BufferChecks.checkFunctionAddress(var5);
      nglDrawArraysInstancedARB(var0, var1, var2, var3, var5);
   }

   static native void nglDrawArraysInstancedARB(int var0, int var1, int var2, int var3, long var4);

   public static void glDrawElementsInstancedARB(int var0, ByteBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glDrawElementsInstancedARB;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureElementVBOdisabled(var3);
      BufferChecks.checkDirect(var1);
      nglDrawElementsInstancedARB(var0, var1.remaining(), 5121, MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glDrawElementsInstancedARB(int var0, IntBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glDrawElementsInstancedARB;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureElementVBOdisabled(var3);
      BufferChecks.checkDirect(var1);
      nglDrawElementsInstancedARB(var0, var1.remaining(), 5125, MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glDrawElementsInstancedARB(int var0, ShortBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glDrawElementsInstancedARB;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureElementVBOdisabled(var3);
      BufferChecks.checkDirect(var1);
      nglDrawElementsInstancedARB(var0, var1.remaining(), 5123, MemoryUtil.getAddress(var1), var2, var4);
   }

   static native void nglDrawElementsInstancedARB(int var0, int var1, int var2, long var3, int var5, long var6);

   public static void glDrawElementsInstancedARB(int var0, int var1, int var2, long var3, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glDrawElementsInstancedARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureElementVBOenabled(var6);
      nglDrawElementsInstancedARBBO(var0, var1, var2, var3, var5, var7);
   }

   static native void nglDrawElementsInstancedARBBO(int var0, int var1, int var2, long var3, int var5, long var6);
}
