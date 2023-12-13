package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBColorBufferFloat {
   public static final int GL_RGBA_FLOAT_MODE_ARB = 34848;
   public static final int GL_CLAMP_VERTEX_COLOR_ARB = 35098;
   public static final int GL_CLAMP_FRAGMENT_COLOR_ARB = 35099;
   public static final int GL_CLAMP_READ_COLOR_ARB = 35100;
   public static final int GL_FIXED_ONLY_ARB = 35101;
   public static final int WGL_TYPE_RGBA_FLOAT_ARB = 8608;
   public static final int GLX_RGBA_FLOAT_TYPE = 8377;
   public static final int GLX_RGBA_FLOAT_BIT = 4;

   private ARBColorBufferFloat() {
   }

   public static void glClampColorARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glClampColorARB;
      BufferChecks.checkFunctionAddress(var3);
      nglClampColorARB(var0, var1, var3);
   }

   static native void nglClampColorARB(int var0, int var1, long var2);
}
