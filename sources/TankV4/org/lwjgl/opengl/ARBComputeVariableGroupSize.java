package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBComputeVariableGroupSize {
   public static final int GL_MAX_COMPUTE_VARIABLE_GROUP_INVOCATIONS_ARB = 37700;
   public static final int GL_MAX_COMPUTE_FIXED_GROUP_INVOCATIONS_ARB = 37099;
   public static final int GL_MAX_COMPUTE_VARIABLE_GROUP_SIZE_ARB = 37701;
   public static final int GL_MAX_COMPUTE_FIXED_GROUP_SIZE_ARB = 37311;

   private ARBComputeVariableGroupSize() {
   }

   public static void glDispatchComputeGroupSizeARB(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glDispatchComputeGroupSizeARB;
      BufferChecks.checkFunctionAddress(var7);
      nglDispatchComputeGroupSizeARB(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglDispatchComputeGroupSizeARB(int var0, int var1, int var2, int var3, int var4, int var5, long var6);
}
