package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ATIElementArray {
   public static final int GL_ELEMENT_ARRAY_ATI = 34664;
   public static final int GL_ELEMENT_ARRAY_TYPE_ATI = 34665;
   public static final int GL_ELEMENT_ARRAY_POINTER_ATI = 34666;

   private ATIElementArray() {
   }

   public static void glElementPointerATI(ByteBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glElementPointerATI;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglElementPointerATI(5121, MemoryUtil.getAddress(var0), var2);
   }

   public static void glElementPointerATI(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glElementPointerATI;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglElementPointerATI(5125, MemoryUtil.getAddress(var0), var2);
   }

   public static void glElementPointerATI(ShortBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glElementPointerATI;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglElementPointerATI(5123, MemoryUtil.getAddress(var0), var2);
   }

   static native void nglElementPointerATI(int var0, long var1, long var3);

   public static void glDrawElementArrayATI(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDrawElementArrayATI;
      BufferChecks.checkFunctionAddress(var3);
      nglDrawElementArrayATI(var0, var1, var3);
   }

   static native void nglDrawElementArrayATI(int var0, int var1, long var2);

   public static void glDrawRangeElementArrayATI(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawRangeElementArrayATI;
      BufferChecks.checkFunctionAddress(var5);
      nglDrawRangeElementArrayATI(var0, var1, var2, var3, var5);
   }

   static native void nglDrawRangeElementArrayATI(int var0, int var1, int var2, int var3, long var4);
}
