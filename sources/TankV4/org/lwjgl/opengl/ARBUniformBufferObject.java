package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class ARBUniformBufferObject {
   public static final int GL_UNIFORM_BUFFER = 35345;
   public static final int GL_UNIFORM_BUFFER_BINDING = 35368;
   public static final int GL_UNIFORM_BUFFER_START = 35369;
   public static final int GL_UNIFORM_BUFFER_SIZE = 35370;
   public static final int GL_MAX_VERTEX_UNIFORM_BLOCKS = 35371;
   public static final int GL_MAX_GEOMETRY_UNIFORM_BLOCKS = 35372;
   public static final int GL_MAX_FRAGMENT_UNIFORM_BLOCKS = 35373;
   public static final int GL_MAX_COMBINED_UNIFORM_BLOCKS = 35374;
   public static final int GL_MAX_UNIFORM_BUFFER_BINDINGS = 35375;
   public static final int GL_MAX_UNIFORM_BLOCK_SIZE = 35376;
   public static final int GL_MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS = 35377;
   public static final int GL_MAX_COMBINED_GEOMETRY_UNIFORM_COMPONENTS = 35378;
   public static final int GL_MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS = 35379;
   public static final int GL_UNIFORM_BUFFER_OFFSET_ALIGNMENT = 35380;
   public static final int GL_ACTIVE_UNIFORM_BLOCK_MAX_NAME_LENGTH = 35381;
   public static final int GL_ACTIVE_UNIFORM_BLOCKS = 35382;
   public static final int GL_UNIFORM_TYPE = 35383;
   public static final int GL_UNIFORM_SIZE = 35384;
   public static final int GL_UNIFORM_NAME_LENGTH = 35385;
   public static final int GL_UNIFORM_BLOCK_INDEX = 35386;
   public static final int GL_UNIFORM_OFFSET = 35387;
   public static final int GL_UNIFORM_ARRAY_STRIDE = 35388;
   public static final int GL_UNIFORM_MATRIX_STRIDE = 35389;
   public static final int GL_UNIFORM_IS_ROW_MAJOR = 35390;
   public static final int GL_UNIFORM_BLOCK_BINDING = 35391;
   public static final int GL_UNIFORM_BLOCK_DATA_SIZE = 35392;
   public static final int GL_UNIFORM_BLOCK_NAME_LENGTH = 35393;
   public static final int GL_UNIFORM_BLOCK_ACTIVE_UNIFORMS = 35394;
   public static final int GL_UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES = 35395;
   public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER = 35396;
   public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_GEOMETRY_SHADER = 35397;
   public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER = 35398;
   public static final int GL_INVALID_INDEX = -1;

   private ARBUniformBufferObject() {
   }

   public static void glGetUniformIndices(int var0, ByteBuffer var1, IntBuffer var2) {
      GL31.glGetUniformIndices(var0, var1, var2);
   }

   public static void glGetUniformIndices(int var0, CharSequence[] var1, IntBuffer var2) {
      GL31.glGetUniformIndices(var0, var1, var2);
   }

   public static void glGetActiveUniforms(int var0, IntBuffer var1, int var2, IntBuffer var3) {
      GL31.glGetActiveUniforms(var0, var1, var2, var3);
   }

   /** @deprecated */
   @Deprecated
   public static int glGetActiveUniforms(int var0, int var1, int var2) {
      return GL31.glGetActiveUniformsi(var0, var1, var2);
   }

   public static int glGetActiveUniformsi(int var0, int var1, int var2) {
      return GL31.glGetActiveUniformsi(var0, var1, var2);
   }

   public static void glGetActiveUniformName(int var0, int var1, IntBuffer var2, ByteBuffer var3) {
      GL31.glGetActiveUniformName(var0, var1, var2, var3);
   }

   public static String glGetActiveUniformName(int var0, int var1, int var2) {
      return GL31.glGetActiveUniformName(var0, var1, var2);
   }

   public static int glGetUniformBlockIndex(int var0, ByteBuffer var1) {
      return GL31.glGetUniformBlockIndex(var0, var1);
   }

   public static int glGetUniformBlockIndex(int var0, CharSequence var1) {
      return GL31.glGetUniformBlockIndex(var0, var1);
   }

   public static void glGetActiveUniformBlock(int var0, int var1, int var2, IntBuffer var3) {
      GL31.glGetActiveUniformBlock(var0, var1, var2, var3);
   }

   /** @deprecated */
   @Deprecated
   public static int glGetActiveUniformBlock(int var0, int var1, int var2) {
      return GL31.glGetActiveUniformBlocki(var0, var1, var2);
   }

   public static int glGetActiveUniformBlocki(int var0, int var1, int var2) {
      return GL31.glGetActiveUniformBlocki(var0, var1, var2);
   }

   public static void glGetActiveUniformBlockName(int var0, int var1, IntBuffer var2, ByteBuffer var3) {
      GL31.glGetActiveUniformBlockName(var0, var1, var2, var3);
   }

   public static String glGetActiveUniformBlockName(int var0, int var1, int var2) {
      return GL31.glGetActiveUniformBlockName(var0, var1, var2);
   }

   public static void glBindBufferRange(int var0, int var1, int var2, long var3, long var5) {
      GL30.glBindBufferRange(var0, var1, var2, var3, var5);
   }

   public static void glBindBufferBase(int var0, int var1, int var2) {
      GL30.glBindBufferBase(var0, var1, var2);
   }

   public static void glGetInteger(int var0, int var1, IntBuffer var2) {
      GL30.glGetInteger(var0, var1, var2);
   }

   public static int glGetInteger(int var0, int var1) {
      return GL30.glGetInteger(var0, var1);
   }

   public static void glUniformBlockBinding(int var0, int var1, int var2) {
      GL31.glUniformBlockBinding(var0, var1, var2);
   }
}
