package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class ARBES2Compatibility {
   public static final int GL_SHADER_COMPILER = 36346;
   public static final int GL_NUM_SHADER_BINARY_FORMATS = 36345;
   public static final int GL_MAX_VERTEX_UNIFORM_VECTORS = 36347;
   public static final int GL_MAX_VARYING_VECTORS = 36348;
   public static final int GL_MAX_FRAGMENT_UNIFORM_VECTORS = 36349;
   public static final int GL_IMPLEMENTATION_COLOR_READ_TYPE = 35738;
   public static final int GL_IMPLEMENTATION_COLOR_READ_FORMAT = 35739;
   public static final int GL_FIXED = 5132;
   public static final int GL_LOW_FLOAT = 36336;
   public static final int GL_MEDIUM_FLOAT = 36337;
   public static final int GL_HIGH_FLOAT = 36338;
   public static final int GL_LOW_INT = 36339;
   public static final int GL_MEDIUM_INT = 36340;
   public static final int GL_HIGH_INT = 36341;
   public static final int GL_RGB565 = 36194;

   private ARBES2Compatibility() {
   }

   public static void glReleaseShaderCompiler() {
      GL41.glReleaseShaderCompiler();
   }

   public static void glShaderBinary(IntBuffer var0, int var1, ByteBuffer var2) {
      GL41.glShaderBinary(var0, var1, var2);
   }

   public static void glGetShaderPrecisionFormat(int var0, int var1, IntBuffer var2, IntBuffer var3) {
      GL41.glGetShaderPrecisionFormat(var0, var1, var2, var3);
   }

   public static void glDepthRangef(float var0, float var1) {
      GL41.glDepthRangef(var0, var1);
   }

   public static void glClearDepthf(float var0) {
      GL41.glClearDepthf(var0);
   }
}
