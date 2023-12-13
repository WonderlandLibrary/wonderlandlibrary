package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class AMDStencilOperationExtended {
   public static final int GL_SET_AMD = 34634;
   public static final int GL_AND = 5377;
   public static final int GL_XOR = 5382;
   public static final int GL_OR = 5383;
   public static final int GL_NOR = 5384;
   public static final int GL_EQUIV = 5385;
   public static final int GL_NAND = 5390;
   public static final int GL_REPLACE_VALUE_AMD = 34635;
   public static final int GL_STENCIL_OP_VALUE_AMD = 34636;
   public static final int GL_STENCIL_BACK_OP_VALUE_AMD = 34637;

   private AMDStencilOperationExtended() {
   }

   public static void glStencilOpValueAMD(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glStencilOpValueAMD;
      BufferChecks.checkFunctionAddress(var3);
      nglStencilOpValueAMD(var0, var1, var3);
   }

   static native void nglStencilOpValueAMD(int var0, int var1, long var2);
}
