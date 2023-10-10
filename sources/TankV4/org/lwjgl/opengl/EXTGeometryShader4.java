package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTGeometryShader4 {
   public static final int GL_GEOMETRY_SHADER_EXT = 36313;
   public static final int GL_GEOMETRY_VERTICES_OUT_EXT = 36314;
   public static final int GL_GEOMETRY_INPUT_TYPE_EXT = 36315;
   public static final int GL_GEOMETRY_OUTPUT_TYPE_EXT = 36316;
   public static final int GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS_EXT = 35881;
   public static final int GL_MAX_GEOMETRY_VARYING_COMPONENTS_EXT = 36317;
   public static final int GL_MAX_VERTEX_VARYING_COMPONENTS_EXT = 36318;
   public static final int GL_MAX_VARYING_COMPONENTS_EXT = 35659;
   public static final int GL_MAX_GEOMETRY_UNIFORM_COMPONENTS_EXT = 36319;
   public static final int GL_MAX_GEOMETRY_OUTPUT_VERTICES_EXT = 36320;
   public static final int GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS_EXT = 36321;
   public static final int GL_LINES_ADJACENCY_EXT = 10;
   public static final int GL_LINE_STRIP_ADJACENCY_EXT = 11;
   public static final int GL_TRIANGLES_ADJACENCY_EXT = 12;
   public static final int GL_TRIANGLE_STRIP_ADJACENCY_EXT = 13;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS_EXT = 36264;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_COUNT_EXT = 36265;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_LAYERED_EXT = 36263;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER_EXT = 36052;
   public static final int GL_PROGRAM_POINT_SIZE_EXT = 34370;

   private EXTGeometryShader4() {
   }

   public static void glProgramParameteriEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramParameteriEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglProgramParameteriEXT(var0, var1, var2, var4);
   }

   static native void nglProgramParameteriEXT(int var0, int var1, int var2, long var3);

   public static void glFramebufferTextureEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glFramebufferTextureEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglFramebufferTextureEXT(var0, var1, var2, var3, var5);
   }

   static native void nglFramebufferTextureEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glFramebufferTextureLayerEXT(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glFramebufferTextureLayerEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglFramebufferTextureLayerEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglFramebufferTextureLayerEXT(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glFramebufferTextureFaceEXT(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glFramebufferTextureFaceEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglFramebufferTextureFaceEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglFramebufferTextureFaceEXT(int var0, int var1, int var2, int var3, int var4, long var5);
}
