package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class AMDDrawBuffersBlend {
   private AMDDrawBuffersBlend() {
   }

   public static void glBlendFuncIndexedAMD(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBlendFuncIndexedAMD;
      BufferChecks.checkFunctionAddress(var4);
      nglBlendFuncIndexedAMD(var0, var1, var2, var4);
   }

   static native void nglBlendFuncIndexedAMD(int var0, int var1, int var2, long var3);

   public static void glBlendFuncSeparateIndexedAMD(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glBlendFuncSeparateIndexedAMD;
      BufferChecks.checkFunctionAddress(var6);
      nglBlendFuncSeparateIndexedAMD(var0, var1, var2, var3, var4, var6);
   }

   static native void nglBlendFuncSeparateIndexedAMD(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glBlendEquationIndexedAMD(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBlendEquationIndexedAMD;
      BufferChecks.checkFunctionAddress(var3);
      nglBlendEquationIndexedAMD(var0, var1, var3);
   }

   static native void nglBlendEquationIndexedAMD(int var0, int var1, long var2);

   public static void glBlendEquationSeparateIndexedAMD(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBlendEquationSeparateIndexedAMD;
      BufferChecks.checkFunctionAddress(var4);
      nglBlendEquationSeparateIndexedAMD(var0, var1, var2, var4);
   }

   static native void nglBlendEquationSeparateIndexedAMD(int var0, int var1, int var2, long var3);
}
