package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTCompiledVertexArray {
   public static final int GL_ARRAY_ELEMENT_LOCK_FIRST_EXT = 33192;
   public static final int GL_ARRAY_ELEMENT_LOCK_COUNT_EXT = 33193;

   private EXTCompiledVertexArray() {
   }

   public static void glLockArraysEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glLockArraysEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglLockArraysEXT(var0, var1, var3);
   }

   static native void nglLockArraysEXT(int var0, int var1, long var2);

   public static void glUnlockArraysEXT() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glUnlockArraysEXT;
      BufferChecks.checkFunctionAddress(var1);
      nglUnlockArraysEXT(var1);
   }

   static native void nglUnlockArraysEXT(long var0);
}
