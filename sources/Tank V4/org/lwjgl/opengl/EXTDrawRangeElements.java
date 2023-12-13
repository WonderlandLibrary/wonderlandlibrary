package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class EXTDrawRangeElements {
   public static final int GL_MAX_ELEMENTS_VERTICES_EXT = 33000;
   public static final int GL_MAX_ELEMENTS_INDICES_EXT = 33001;

   private EXTDrawRangeElements() {
   }

   public static void glDrawRangeElementsEXT(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawRangeElementsEXT;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureElementVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglDrawRangeElementsEXT(var0, var1, var2, var3.remaining(), 5121, MemoryUtil.getAddress(var3), var5);
   }

   public static void glDrawRangeElementsEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawRangeElementsEXT;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureElementVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglDrawRangeElementsEXT(var0, var1, var2, var3.remaining(), 5125, MemoryUtil.getAddress(var3), var5);
   }

   public static void glDrawRangeElementsEXT(int var0, int var1, int var2, ShortBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawRangeElementsEXT;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureElementVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglDrawRangeElementsEXT(var0, var1, var2, var3.remaining(), 5123, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglDrawRangeElementsEXT(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glDrawRangeElementsEXT(int var0, int var1, int var2, int var3, int var4, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glDrawRangeElementsEXT;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureElementVBOenabled(var7);
      nglDrawRangeElementsEXTBO(var0, var1, var2, var3, var4, var5, var8);
   }

   static native void nglDrawRangeElementsEXTBO(int var0, int var1, int var2, int var3, int var4, long var5, long var7);
}
