package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class APPLEElementArray {
   public static final int GL_ELEMENT_ARRAY_APPLE = 34664;
   public static final int GL_ELEMENT_ARRAY_TYPE_APPLE = 34665;
   public static final int GL_ELEMENT_ARRAY_POINTER_APPLE = 34666;

   private APPLEElementArray() {
   }

   public static void glElementPointerAPPLE(ByteBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glElementPointerAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglElementPointerAPPLE(5121, MemoryUtil.getAddress(var0), var2);
   }

   public static void glElementPointerAPPLE(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glElementPointerAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglElementPointerAPPLE(5125, MemoryUtil.getAddress(var0), var2);
   }

   public static void glElementPointerAPPLE(ShortBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glElementPointerAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglElementPointerAPPLE(5123, MemoryUtil.getAddress(var0), var2);
   }

   static native void nglElementPointerAPPLE(int var0, long var1, long var3);

   public static void glDrawElementArrayAPPLE(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glDrawElementArrayAPPLE;
      BufferChecks.checkFunctionAddress(var4);
      nglDrawElementArrayAPPLE(var0, var1, var2, var4);
   }

   static native void nglDrawElementArrayAPPLE(int var0, int var1, int var2, long var3);

   public static void glDrawRangeElementArrayAPPLE(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDrawRangeElementArrayAPPLE;
      BufferChecks.checkFunctionAddress(var6);
      nglDrawRangeElementArrayAPPLE(var0, var1, var2, var3, var4, var6);
   }

   static native void nglDrawRangeElementArrayAPPLE(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glMultiDrawElementArrayAPPLE(int var0, IntBuffer var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiDrawElementArrayAPPLE;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkBuffer(var2, var1.remaining());
      nglMultiDrawElementArrayAPPLE(var0, MemoryUtil.getAddress(var1), MemoryUtil.getAddress(var2), var1.remaining(), var4);
   }

   static native void nglMultiDrawElementArrayAPPLE(int var0, long var1, long var3, int var5, long var6);

   public static void glMultiDrawRangeElementArrayAPPLE(int var0, int var1, int var2, IntBuffer var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMultiDrawRangeElementArrayAPPLE;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkBuffer(var4, var3.remaining());
      nglMultiDrawRangeElementArrayAPPLE(var0, var1, var2, MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), var3.remaining(), var6);
   }

   static native void nglMultiDrawRangeElementArrayAPPLE(int var0, int var1, int var2, long var3, long var5, int var7, long var8);
}
