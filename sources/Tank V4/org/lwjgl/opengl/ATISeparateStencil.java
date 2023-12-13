package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ATISeparateStencil {
   public static final int GL_STENCIL_BACK_FUNC_ATI = 34816;
   public static final int GL_STENCIL_BACK_FAIL_ATI = 34817;
   public static final int GL_STENCIL_BACK_PASS_DEPTH_FAIL_ATI = 34818;
   public static final int GL_STENCIL_BACK_PASS_DEPTH_PASS_ATI = 34819;

   private ATISeparateStencil() {
   }

   public static void glStencilOpSeparateATI(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glStencilOpSeparateATI;
      BufferChecks.checkFunctionAddress(var5);
      nglStencilOpSeparateATI(var0, var1, var2, var3, var5);
   }

   static native void nglStencilOpSeparateATI(int var0, int var1, int var2, int var3, long var4);

   public static void glStencilFuncSeparateATI(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glStencilFuncSeparateATI;
      BufferChecks.checkFunctionAddress(var5);
      nglStencilFuncSeparateATI(var0, var1, var2, var3, var5);
   }

   static native void nglStencilFuncSeparateATI(int var0, int var1, int var2, int var3, long var4);
}
