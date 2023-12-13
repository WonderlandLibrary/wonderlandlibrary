package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class AMDInterleavedElements {
   public static final int GL_VERTEX_ELEMENT_SWIZZLE_AMD = 37284;
   public static final int GL_VERTEX_ID_SWIZZLE_AMD = 37285;

   private AMDInterleavedElements() {
   }

   public static void glVertexAttribParameteriAMD(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttribParameteriAMD;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttribParameteriAMD(var0, var1, var2, var4);
   }

   static native void nglVertexAttribParameteriAMD(int var0, int var1, int var2, long var3);
}
