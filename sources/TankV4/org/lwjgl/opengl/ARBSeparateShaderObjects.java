package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class ARBSeparateShaderObjects {
   public static final int GL_VERTEX_SHADER_BIT = 1;
   public static final int GL_FRAGMENT_SHADER_BIT = 2;
   public static final int GL_GEOMETRY_SHADER_BIT = 4;
   public static final int GL_TESS_CONTROL_SHADER_BIT = 8;
   public static final int GL_TESS_EVALUATION_SHADER_BIT = 16;
   public static final int GL_ALL_SHADER_BITS = -1;
   public static final int GL_PROGRAM_SEPARABLE = 33368;
   public static final int GL_ACTIVE_PROGRAM = 33369;
   public static final int GL_PROGRAM_PIPELINE_BINDING = 33370;

   private ARBSeparateShaderObjects() {
   }

   public static void glUseProgramStages(int var0, int var1, int var2) {
      GL41.glUseProgramStages(var0, var1, var2);
   }

   public static void glActiveShaderProgram(int var0, int var1) {
      GL41.glActiveShaderProgram(var0, var1);
   }

   public static int glCreateShaderProgram(int var0, ByteBuffer var1) {
      return GL41.glCreateShaderProgram(var0, var1);
   }

   public static int glCreateShaderProgram(int var0, int var1, ByteBuffer var2) {
      return GL41.glCreateShaderProgram(var0, var1, var2);
   }

   public static int glCreateShaderProgram(int var0, ByteBuffer[] var1) {
      return GL41.glCreateShaderProgram(var0, var1);
   }

   public static int glCreateShaderProgram(int var0, CharSequence var1) {
      return GL41.glCreateShaderProgram(var0, var1);
   }

   public static int glCreateShaderProgram(int var0, CharSequence[] var1) {
      return GL41.glCreateShaderProgram(var0, var1);
   }

   public static void glBindProgramPipeline(int var0) {
      GL41.glBindProgramPipeline(var0);
   }

   public static void glDeleteProgramPipelines(IntBuffer var0) {
      GL41.glDeleteProgramPipelines(var0);
   }

   public static void glDeleteProgramPipelines(int var0) {
      GL41.glDeleteProgramPipelines(var0);
   }

   public static void glGenProgramPipelines(IntBuffer var0) {
      GL41.glGenProgramPipelines(var0);
   }

   public static int glGenProgramPipelines() {
      return GL41.glGenProgramPipelines();
   }

   public static boolean glIsProgramPipeline(int var0) {
      return GL41.glIsProgramPipeline(var0);
   }

   public static void glProgramParameteri(int var0, int var1, int var2) {
      GL41.glProgramParameteri(var0, var1, var2);
   }

   public static void glGetProgramPipeline(int var0, int var1, IntBuffer var2) {
      GL41.glGetProgramPipeline(var0, var1, var2);
   }

   public static int glGetProgramPipelinei(int var0, int var1) {
      return GL41.glGetProgramPipelinei(var0, var1);
   }

   public static void glProgramUniform1i(int var0, int var1, int var2) {
      GL41.glProgramUniform1i(var0, var1, var2);
   }

   public static void glProgramUniform2i(int var0, int var1, int var2, int var3) {
      GL41.glProgramUniform2i(var0, var1, var2, var3);
   }

   public static void glProgramUniform3i(int var0, int var1, int var2, int var3, int var4) {
      GL41.glProgramUniform3i(var0, var1, var2, var3, var4);
   }

   public static void glProgramUniform4i(int var0, int var1, int var2, int var3, int var4, int var5) {
      GL41.glProgramUniform4i(var0, var1, var2, var3, var4, var5);
   }

   public static void glProgramUniform1f(int var0, int var1, float var2) {
      GL41.glProgramUniform1f(var0, var1, var2);
   }

   public static void glProgramUniform2f(int var0, int var1, float var2, float var3) {
      GL41.glProgramUniform2f(var0, var1, var2, var3);
   }

   public static void glProgramUniform3f(int var0, int var1, float var2, float var3, float var4) {
      GL41.glProgramUniform3f(var0, var1, var2, var3, var4);
   }

   public static void glProgramUniform4f(int var0, int var1, float var2, float var3, float var4, float var5) {
      GL41.glProgramUniform4f(var0, var1, var2, var3, var4, var5);
   }

   public static void glProgramUniform1d(int var0, int var1, double var2) {
      GL41.glProgramUniform1d(var0, var1, var2);
   }

   public static void glProgramUniform2d(int var0, int var1, double var2, double var4) {
      GL41.glProgramUniform2d(var0, var1, var2, var4);
   }

   public static void glProgramUniform3d(int var0, int var1, double var2, double var4, double var6) {
      GL41.glProgramUniform3d(var0, var1, var2, var4, var6);
   }

   public static void glProgramUniform4d(int var0, int var1, double var2, double var4, double var6, double var8) {
      GL41.glProgramUniform4d(var0, var1, var2, var4, var6, var8);
   }

   public static void glProgramUniform1(int var0, int var1, IntBuffer var2) {
      GL41.glProgramUniform1(var0, var1, var2);
   }

   public static void glProgramUniform2(int var0, int var1, IntBuffer var2) {
      GL41.glProgramUniform2(var0, var1, var2);
   }

   public static void glProgramUniform3(int var0, int var1, IntBuffer var2) {
      GL41.glProgramUniform3(var0, var1, var2);
   }

   public static void glProgramUniform4(int var0, int var1, IntBuffer var2) {
      GL41.glProgramUniform4(var0, var1, var2);
   }

   public static void glProgramUniform1(int var0, int var1, FloatBuffer var2) {
      GL41.glProgramUniform1(var0, var1, var2);
   }

   public static void glProgramUniform2(int var0, int var1, FloatBuffer var2) {
      GL41.glProgramUniform2(var0, var1, var2);
   }

   public static void glProgramUniform3(int var0, int var1, FloatBuffer var2) {
      GL41.glProgramUniform3(var0, var1, var2);
   }

   public static void glProgramUniform4(int var0, int var1, FloatBuffer var2) {
      GL41.glProgramUniform4(var0, var1, var2);
   }

   public static void glProgramUniform1(int var0, int var1, DoubleBuffer var2) {
      GL41.glProgramUniform1(var0, var1, var2);
   }

   public static void glProgramUniform2(int var0, int var1, DoubleBuffer var2) {
      GL41.glProgramUniform2(var0, var1, var2);
   }

   public static void glProgramUniform3(int var0, int var1, DoubleBuffer var2) {
      GL41.glProgramUniform3(var0, var1, var2);
   }

   public static void glProgramUniform4(int var0, int var1, DoubleBuffer var2) {
      GL41.glProgramUniform4(var0, var1, var2);
   }

   public static void glProgramUniform1ui(int var0, int var1, int var2) {
      GL41.glProgramUniform1ui(var0, var1, var2);
   }

   public static void glProgramUniform2ui(int var0, int var1, int var2, int var3) {
      GL41.glProgramUniform2ui(var0, var1, var2, var3);
   }

   public static void glProgramUniform3ui(int var0, int var1, int var2, int var3, int var4) {
      GL41.glProgramUniform3ui(var0, var1, var2, var3, var4);
   }

   public static void glProgramUniform4ui(int var0, int var1, int var2, int var3, int var4, int var5) {
      GL41.glProgramUniform4ui(var0, var1, var2, var3, var4, var5);
   }

   public static void glProgramUniform1u(int var0, int var1, IntBuffer var2) {
      GL41.glProgramUniform1u(var0, var1, var2);
   }

   public static void glProgramUniform2u(int var0, int var1, IntBuffer var2) {
      GL41.glProgramUniform2u(var0, var1, var2);
   }

   public static void glProgramUniform3u(int var0, int var1, IntBuffer var2) {
      GL41.glProgramUniform3u(var0, var1, var2);
   }

   public static void glProgramUniform4u(int var0, int var1, IntBuffer var2) {
      GL41.glProgramUniform4u(var0, var1, var2);
   }

   public static void glProgramUniformMatrix2(int var0, int var1, boolean var2, FloatBuffer var3) {
      GL41.glProgramUniformMatrix2(var0, var1, var2, var3);
   }

   public static void glProgramUniformMatrix3(int var0, int var1, boolean var2, FloatBuffer var3) {
      GL41.glProgramUniformMatrix3(var0, var1, var2, var3);
   }

   public static void glProgramUniformMatrix4(int var0, int var1, boolean var2, FloatBuffer var3) {
      GL41.glProgramUniformMatrix4(var0, var1, var2, var3);
   }

   public static void glProgramUniformMatrix2(int var0, int var1, boolean var2, DoubleBuffer var3) {
      GL41.glProgramUniformMatrix2(var0, var1, var2, var3);
   }

   public static void glProgramUniformMatrix3(int var0, int var1, boolean var2, DoubleBuffer var3) {
      GL41.glProgramUniformMatrix3(var0, var1, var2, var3);
   }

   public static void glProgramUniformMatrix4(int var0, int var1, boolean var2, DoubleBuffer var3) {
      GL41.glProgramUniformMatrix4(var0, var1, var2, var3);
   }

   public static void glProgramUniformMatrix2x3(int var0, int var1, boolean var2, FloatBuffer var3) {
      GL41.glProgramUniformMatrix2x3(var0, var1, var2, var3);
   }

   public static void glProgramUniformMatrix3x2(int var0, int var1, boolean var2, FloatBuffer var3) {
      GL41.glProgramUniformMatrix3x2(var0, var1, var2, var3);
   }

   public static void glProgramUniformMatrix2x4(int var0, int var1, boolean var2, FloatBuffer var3) {
      GL41.glProgramUniformMatrix2x4(var0, var1, var2, var3);
   }

   public static void glProgramUniformMatrix4x2(int var0, int var1, boolean var2, FloatBuffer var3) {
      GL41.glProgramUniformMatrix4x2(var0, var1, var2, var3);
   }

   public static void glProgramUniformMatrix3x4(int var0, int var1, boolean var2, FloatBuffer var3) {
      GL41.glProgramUniformMatrix3x4(var0, var1, var2, var3);
   }

   public static void glProgramUniformMatrix4x3(int var0, int var1, boolean var2, FloatBuffer var3) {
      GL41.glProgramUniformMatrix4x3(var0, var1, var2, var3);
   }

   public static void glProgramUniformMatrix2x3(int var0, int var1, boolean var2, DoubleBuffer var3) {
      GL41.glProgramUniformMatrix2x3(var0, var1, var2, var3);
   }

   public static void glProgramUniformMatrix3x2(int var0, int var1, boolean var2, DoubleBuffer var3) {
      GL41.glProgramUniformMatrix3x2(var0, var1, var2, var3);
   }

   public static void glProgramUniformMatrix2x4(int var0, int var1, boolean var2, DoubleBuffer var3) {
      GL41.glProgramUniformMatrix2x4(var0, var1, var2, var3);
   }

   public static void glProgramUniformMatrix4x2(int var0, int var1, boolean var2, DoubleBuffer var3) {
      GL41.glProgramUniformMatrix4x2(var0, var1, var2, var3);
   }

   public static void glProgramUniformMatrix3x4(int var0, int var1, boolean var2, DoubleBuffer var3) {
      GL41.glProgramUniformMatrix3x4(var0, var1, var2, var3);
   }

   public static void glProgramUniformMatrix4x3(int var0, int var1, boolean var2, DoubleBuffer var3) {
      GL41.glProgramUniformMatrix4x3(var0, var1, var2, var3);
   }

   public static void glValidateProgramPipeline(int var0) {
      GL41.glValidateProgramPipeline(var0);
   }

   public static void glGetProgramPipelineInfoLog(int var0, IntBuffer var1, ByteBuffer var2) {
      GL41.glGetProgramPipelineInfoLog(var0, var1, var2);
   }

   public static String glGetProgramPipelineInfoLog(int var0, int var1) {
      return GL41.glGetProgramPipelineInfoLog(var0, var1);
   }
}
