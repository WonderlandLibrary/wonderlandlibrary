package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBInstancedArrays {
   public static final int GL_VERTEX_ATTRIB_ARRAY_DIVISOR_ARB = 35070;

   private ARBInstancedArrays() {
   }

   public static void glVertexAttribDivisorARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribDivisorARB;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexAttribDivisorARB(var0, var1, var3);
   }

   static native void nglVertexAttribDivisorARB(int var0, int var1, long var2);
}
