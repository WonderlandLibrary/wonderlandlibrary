package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVGeometryProgram4 {
   public static final int GL_GEOMETRY_PROGRAM_NV = 35878;
   public static final int GL_MAX_PROGRAM_OUTPUT_VERTICES_NV = 35879;
   public static final int GL_MAX_PROGRAM_TOTAL_OUTPUT_COMPONENTS_NV = 35880;

   private NVGeometryProgram4() {
   }

   public static void glProgramVertexLimitNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glProgramVertexLimitNV;
      BufferChecks.checkFunctionAddress(var3);
      nglProgramVertexLimitNV(var0, var1, var3);
   }

   static native void nglProgramVertexLimitNV(int var0, int var1, long var2);

   public static void glFramebufferTextureEXT(int var0, int var1, int var2, int var3) {
      EXTGeometryShader4.glFramebufferTextureEXT(var0, var1, var2, var3);
   }

   public static void glFramebufferTextureLayerEXT(int var0, int var1, int var2, int var3, int var4) {
      EXTGeometryShader4.glFramebufferTextureLayerEXT(var0, var1, var2, var3, var4);
   }

   public static void glFramebufferTextureFaceEXT(int var0, int var1, int var2, int var3, int var4) {
      EXTGeometryShader4.glFramebufferTextureFaceEXT(var0, var1, var2, var3, var4);
   }
}
