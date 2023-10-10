package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBDrawBuffersBlend {
   private ARBDrawBuffersBlend() {
   }

   public static void glBlendEquationiARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBlendEquationiARB;
      BufferChecks.checkFunctionAddress(var3);
      nglBlendEquationiARB(var0, var1, var3);
   }

   static native void nglBlendEquationiARB(int var0, int var1, long var2);

   public static void glBlendEquationSeparateiARB(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBlendEquationSeparateiARB;
      BufferChecks.checkFunctionAddress(var4);
      nglBlendEquationSeparateiARB(var0, var1, var2, var4);
   }

   static native void nglBlendEquationSeparateiARB(int var0, int var1, int var2, long var3);

   public static void glBlendFunciARB(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBlendFunciARB;
      BufferChecks.checkFunctionAddress(var4);
      nglBlendFunciARB(var0, var1, var2, var4);
   }

   static native void nglBlendFunciARB(int var0, int var1, int var2, long var3);

   public static void glBlendFuncSeparateiARB(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glBlendFuncSeparateiARB;
      BufferChecks.checkFunctionAddress(var6);
      nglBlendFuncSeparateiARB(var0, var1, var2, var3, var4, var6);
   }

   static native void nglBlendFuncSeparateiARB(int var0, int var1, int var2, int var3, int var4, long var5);
}
