package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ATIPnTriangles {
   public static final int GL_PN_TRIANGLES_ATI = 34800;
   public static final int GL_MAX_PN_TRIANGLES_TESSELATION_LEVEL_ATI = 34801;
   public static final int GL_PN_TRIANGLES_POINT_MODE_ATI = 34802;
   public static final int GL_PN_TRIANGLES_NORMAL_MODE_ATI = 34803;
   public static final int GL_PN_TRIANGLES_TESSELATION_LEVEL_ATI = 34804;
   public static final int GL_PN_TRIANGLES_POINT_MODE_LINEAR_ATI = 34805;
   public static final int GL_PN_TRIANGLES_POINT_MODE_CUBIC_ATI = 34806;
   public static final int GL_PN_TRIANGLES_NORMAL_MODE_LINEAR_ATI = 34807;
   public static final int GL_PN_TRIANGLES_NORMAL_MODE_QUADRATIC_ATI = 34808;

   private ATIPnTriangles() {
   }

   public static void glPNTrianglesfATI(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPNTrianglesfATI;
      BufferChecks.checkFunctionAddress(var3);
      nglPNTrianglesfATI(var0, var1, var3);
   }

   static native void nglPNTrianglesfATI(int var0, float var1, long var2);

   public static void glPNTrianglesiATI(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPNTrianglesiATI;
      BufferChecks.checkFunctionAddress(var3);
      nglPNTrianglesiATI(var0, var1, var3);
   }

   static native void nglPNTrianglesiATI(int var0, int var1, long var2);
}
