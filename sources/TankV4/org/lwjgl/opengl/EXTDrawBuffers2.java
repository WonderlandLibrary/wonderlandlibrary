package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class EXTDrawBuffers2 {
   private EXTDrawBuffers2() {
   }

   public static void glColorMaskIndexedEXT(int var0, boolean var1, boolean var2, boolean var3, boolean var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glColorMaskIndexedEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglColorMaskIndexedEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglColorMaskIndexedEXT(int var0, boolean var1, boolean var2, boolean var3, boolean var4, long var5);

   public static void glGetBooleanIndexedEXT(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetBooleanIndexedvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((ByteBuffer)var2, 4);
      nglGetBooleanIndexedvEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetBooleanIndexedvEXT(int var0, int var1, long var2, long var4);

   public static boolean glGetBooleanIndexedEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetBooleanIndexedvEXT;
      BufferChecks.checkFunctionAddress(var3);
      ByteBuffer var5 = APIUtil.getBufferByte(var2, 1);
      nglGetBooleanIndexedvEXT(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0) == 1;
   }

   public static void glGetIntegerIndexedEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetIntegerIndexedvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetIntegerIndexedvEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetIntegerIndexedvEXT(int var0, int var1, long var2, long var4);

   public static int glGetIntegerIndexedEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetIntegerIndexedvEXT;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetIntegerIndexedvEXT(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glEnableIndexedEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glEnableIndexedEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglEnableIndexedEXT(var0, var1, var3);
   }

   static native void nglEnableIndexedEXT(int var0, int var1, long var2);

   public static void glDisableIndexedEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDisableIndexedEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglDisableIndexedEXT(var0, var1, var3);
   }

   static native void nglDisableIndexedEXT(int var0, int var1, long var2);

   public static boolean glIsEnabledIndexedEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glIsEnabledIndexedEXT;
      BufferChecks.checkFunctionAddress(var3);
      boolean var5 = nglIsEnabledIndexedEXT(var0, var1, var3);
      return var5;
   }

   static native boolean nglIsEnabledIndexedEXT(int var0, int var1, long var2);
}
