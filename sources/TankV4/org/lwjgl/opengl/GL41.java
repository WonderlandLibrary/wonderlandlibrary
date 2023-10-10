package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class GL41 {
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
   public static final int GL_PROGRAM_BINARY_RETRIEVABLE_HINT = 33367;
   public static final int GL_PROGRAM_BINARY_LENGTH = 34625;
   public static final int GL_NUM_PROGRAM_BINARY_FORMATS = 34814;
   public static final int GL_PROGRAM_BINARY_FORMATS = 34815;
   public static final int GL_VERTEX_SHADER_BIT = 1;
   public static final int GL_FRAGMENT_SHADER_BIT = 2;
   public static final int GL_GEOMETRY_SHADER_BIT = 4;
   public static final int GL_TESS_CONTROL_SHADER_BIT = 8;
   public static final int GL_TESS_EVALUATION_SHADER_BIT = 16;
   public static final int GL_ALL_SHADER_BITS = -1;
   public static final int GL_PROGRAM_SEPARABLE = 33368;
   public static final int GL_ACTIVE_PROGRAM = 33369;
   public static final int GL_PROGRAM_PIPELINE_BINDING = 33370;
   public static final int GL_DOUBLE_VEC2 = 36860;
   public static final int GL_DOUBLE_VEC3 = 36861;
   public static final int GL_DOUBLE_VEC4 = 36862;
   public static final int GL_DOUBLE_MAT2 = 36678;
   public static final int GL_DOUBLE_MAT3 = 36679;
   public static final int GL_DOUBLE_MAT4 = 36680;
   public static final int GL_DOUBLE_MAT2x3 = 36681;
   public static final int GL_DOUBLE_MAT2x4 = 36682;
   public static final int GL_DOUBLE_MAT3x2 = 36683;
   public static final int GL_DOUBLE_MAT3x4 = 36684;
   public static final int GL_DOUBLE_MAT4x2 = 36685;
   public static final int GL_DOUBLE_MAT4x3 = 36686;
   public static final int GL_MAX_VIEWPORTS = 33371;
   public static final int GL_VIEWPORT_SUBPIXEL_BITS = 33372;
   public static final int GL_VIEWPORT_BOUNDS_RANGE = 33373;
   public static final int GL_LAYER_PROVOKING_VERTEX = 33374;
   public static final int GL_VIEWPORT_INDEX_PROVOKING_VERTEX = 33375;
   public static final int GL_SCISSOR_BOX = 3088;
   public static final int GL_VIEWPORT = 2978;
   public static final int GL_DEPTH_RANGE = 2928;
   public static final int GL_SCISSOR_TEST = 3089;
   public static final int GL_FIRST_VERTEX_CONVENTION = 36429;
   public static final int GL_LAST_VERTEX_CONVENTION = 36430;
   public static final int GL_PROVOKING_VERTEX = 36431;
   public static final int GL_UNDEFINED_VERTEX = 33376;

   private GL41() {
   }

   public static void glReleaseShaderCompiler() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glReleaseShaderCompiler;
      BufferChecks.checkFunctionAddress(var1);
      nglReleaseShaderCompiler(var1);
   }

   static native void nglReleaseShaderCompiler(long var0);

   public static void glShaderBinary(IntBuffer var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glShaderBinary;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var0);
      BufferChecks.checkDirect(var2);
      nglShaderBinary(var0.remaining(), MemoryUtil.getAddress(var0), var1, MemoryUtil.getAddress(var2), var2.remaining(), var4);
   }

   static native void nglShaderBinary(int var0, long var1, int var3, long var4, int var6, long var7);

   public static void glGetShaderPrecisionFormat(int var0, int var1, IntBuffer var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetShaderPrecisionFormat;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var2, 2);
      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      nglGetShaderPrecisionFormat(var0, var1, MemoryUtil.getAddress(var2), MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetShaderPrecisionFormat(int var0, int var1, long var2, long var4, long var6);

   public static void glDepthRangef(float var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDepthRangef;
      BufferChecks.checkFunctionAddress(var3);
      nglDepthRangef(var0, var1, var3);
   }

   static native void nglDepthRangef(float var0, float var1, long var2);

   public static void glClearDepthf(float var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glClearDepthf;
      BufferChecks.checkFunctionAddress(var2);
      nglClearDepthf(var0, var2);
   }

   static native void nglClearDepthf(float var0, long var1);

   public static void glGetProgramBinary(int var0, IntBuffer var1, IntBuffer var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetProgramBinary;
      BufferChecks.checkFunctionAddress(var5);
      if (var1 != null) {
         BufferChecks.checkBuffer((IntBuffer)var1, 1);
      }

      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      BufferChecks.checkDirect(var3);
      nglGetProgramBinary(var0, var3.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddress(var2), MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetProgramBinary(int var0, int var1, long var2, long var4, long var6, long var8);

   public static void glProgramBinary(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramBinary;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramBinary(var0, var1, MemoryUtil.getAddress(var2), var2.remaining(), var4);
   }

   static native void nglProgramBinary(int var0, int var1, long var2, int var4, long var5);

   public static void glProgramParameteri(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramParameteri;
      BufferChecks.checkFunctionAddress(var4);
      nglProgramParameteri(var0, var1, var2, var4);
   }

   static native void nglProgramParameteri(int var0, int var1, int var2, long var3);

   public static void glUseProgramStages(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUseProgramStages;
      BufferChecks.checkFunctionAddress(var4);
      nglUseProgramStages(var0, var1, var2, var4);
   }

   static native void nglUseProgramStages(int var0, int var1, int var2, long var3);

   public static void glActiveShaderProgram(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glActiveShaderProgram;
      BufferChecks.checkFunctionAddress(var3);
      nglActiveShaderProgram(var0, var1, var3);
   }

   static native void nglActiveShaderProgram(int var0, int var1, long var2);

   public static int glCreateShaderProgram(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glCreateShaderProgramv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkNullTerminated(var1);
      int var5 = nglCreateShaderProgramv(var0, 1, MemoryUtil.getAddress(var1), var3);
      return var5;
   }

   static native int nglCreateShaderProgramv(int var0, int var1, long var2, long var4);

   public static int glCreateShaderProgram(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glCreateShaderProgramv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2, var1);
      int var6 = nglCreateShaderProgramv2(var0, var1, MemoryUtil.getAddress(var2), var4);
      return var6;
   }

   static native int nglCreateShaderProgramv2(int var0, int var1, long var2, long var4);

   public static int glCreateShaderProgram(int var0, ByteBuffer[] var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glCreateShaderProgramv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkArray((Object[])var1, 1);
      int var5 = nglCreateShaderProgramv3(var0, var1.length, var1, var3);
      return var5;
   }

   static native int nglCreateShaderProgramv3(int var0, int var1, ByteBuffer[] var2, long var3);

   public static int glCreateShaderProgram(int var0, CharSequence var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glCreateShaderProgramv;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nglCreateShaderProgramv(var0, 1, APIUtil.getBufferNT(var2, var1), var3);
      return var5;
   }

   public static int glCreateShaderProgram(int var0, CharSequence[] var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glCreateShaderProgramv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkArray(var1);
      int var5 = nglCreateShaderProgramv2(var0, var1.length, APIUtil.getBufferNT(var2, var1), var3);
      return var5;
   }

   public static void glBindProgramPipeline(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glBindProgramPipeline;
      BufferChecks.checkFunctionAddress(var2);
      nglBindProgramPipeline(var0, var2);
   }

   static native void nglBindProgramPipeline(int var0, long var1);

   public static void glDeleteProgramPipelines(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteProgramPipelines;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteProgramPipelines(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteProgramPipelines(int var0, long var1, long var3);

   public static void glDeleteProgramPipelines(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteProgramPipelines;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteProgramPipelines(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glGenProgramPipelines(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenProgramPipelines;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenProgramPipelines(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenProgramPipelines(int var0, long var1, long var3);

   public static int glGenProgramPipelines() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenProgramPipelines;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenProgramPipelines(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static boolean glIsProgramPipeline(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsProgramPipeline;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsProgramPipeline(var0, var2);
      return var4;
   }

   static native boolean nglIsProgramPipeline(int var0, long var1);

   public static void glGetProgramPipeline(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramPipelineiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetProgramPipelineiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramPipelineiv(int var0, int var1, long var2, long var4);

   public static int glGetProgramPipelinei(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetProgramPipelineiv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetProgramPipelineiv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glProgramUniform1i(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform1i;
      BufferChecks.checkFunctionAddress(var4);
      nglProgramUniform1i(var0, var1, var2, var4);
   }

   static native void nglProgramUniform1i(int var0, int var1, int var2, long var3);

   public static void glProgramUniform2i(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniform2i;
      BufferChecks.checkFunctionAddress(var5);
      nglProgramUniform2i(var0, var1, var2, var3, var5);
   }

   static native void nglProgramUniform2i(int var0, int var1, int var2, int var3, long var4);

   public static void glProgramUniform3i(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glProgramUniform3i;
      BufferChecks.checkFunctionAddress(var6);
      nglProgramUniform3i(var0, var1, var2, var3, var4, var6);
   }

   static native void nglProgramUniform3i(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glProgramUniform4i(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramUniform4i;
      BufferChecks.checkFunctionAddress(var7);
      nglProgramUniform4i(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglProgramUniform4i(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glProgramUniform1f(int var0, int var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform1f;
      BufferChecks.checkFunctionAddress(var4);
      nglProgramUniform1f(var0, var1, var2, var4);
   }

   static native void nglProgramUniform1f(int var0, int var1, float var2, long var3);

   public static void glProgramUniform2f(int var0, int var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniform2f;
      BufferChecks.checkFunctionAddress(var5);
      nglProgramUniform2f(var0, var1, var2, var3, var5);
   }

   static native void nglProgramUniform2f(int var0, int var1, float var2, float var3, long var4);

   public static void glProgramUniform3f(int var0, int var1, float var2, float var3, float var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glProgramUniform3f;
      BufferChecks.checkFunctionAddress(var6);
      nglProgramUniform3f(var0, var1, var2, var3, var4, var6);
   }

   static native void nglProgramUniform3f(int var0, int var1, float var2, float var3, float var4, long var5);

   public static void glProgramUniform4f(int var0, int var1, float var2, float var3, float var4, float var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramUniform4f;
      BufferChecks.checkFunctionAddress(var7);
      nglProgramUniform4f(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglProgramUniform4f(int var0, int var1, float var2, float var3, float var4, float var5, long var6);

   public static void glProgramUniform1d(int var0, int var1, double var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniform1d;
      BufferChecks.checkFunctionAddress(var5);
      nglProgramUniform1d(var0, var1, var2, var5);
   }

   static native void nglProgramUniform1d(int var0, int var1, double var2, long var4);

   public static void glProgramUniform2d(int var0, int var1, double var2, double var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramUniform2d;
      BufferChecks.checkFunctionAddress(var7);
      nglProgramUniform2d(var0, var1, var2, var4, var7);
   }

   static native void nglProgramUniform2d(int var0, int var1, double var2, double var4, long var6);

   public static void glProgramUniform3d(int var0, int var1, double var2, double var4, double var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glProgramUniform3d;
      BufferChecks.checkFunctionAddress(var9);
      nglProgramUniform3d(var0, var1, var2, var4, var6, var9);
   }

   static native void nglProgramUniform3d(int var0, int var1, double var2, double var4, double var6, long var8);

   public static void glProgramUniform4d(int var0, int var1, double var2, double var4, double var6, double var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glProgramUniform4d;
      BufferChecks.checkFunctionAddress(var11);
      nglProgramUniform4d(var0, var1, var2, var4, var6, var8, var11);
   }

   static native void nglProgramUniform4d(int var0, int var1, double var2, double var4, double var6, double var8, long var10);

   public static void glProgramUniform1(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform1iv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform1iv(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform1iv(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform2(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform2iv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform2iv(var0, var1, var2.remaining() >> 1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform2iv(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform3(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform3iv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform3iv(var0, var1, var2.remaining() / 3, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform3iv(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform4(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform4iv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform4iv(var0, var1, var2.remaining() >> 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform4iv(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform1(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform1fv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform1fv(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform1fv(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform2(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform2fv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform2fv(var0, var1, var2.remaining() >> 1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform2fv(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform3(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform3fv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform3fv(var0, var1, var2.remaining() / 3, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform3fv(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform4(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform4fv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform4fv(var0, var1, var2.remaining() >> 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform4fv(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform1(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform1dv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform1dv(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform1dv(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform2(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform2dv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform2dv(var0, var1, var2.remaining() >> 1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform2dv(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform3(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform3dv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform3dv(var0, var1, var2.remaining() / 3, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform3dv(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform4(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform4dv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform4dv(var0, var1, var2.remaining() >> 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform4dv(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform1ui(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform1ui;
      BufferChecks.checkFunctionAddress(var4);
      nglProgramUniform1ui(var0, var1, var2, var4);
   }

   static native void nglProgramUniform1ui(int var0, int var1, int var2, long var3);

   public static void glProgramUniform2ui(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniform2ui;
      BufferChecks.checkFunctionAddress(var5);
      nglProgramUniform2ui(var0, var1, var2, var3, var5);
   }

   static native void nglProgramUniform2ui(int var0, int var1, int var2, int var3, long var4);

   public static void glProgramUniform3ui(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glProgramUniform3ui;
      BufferChecks.checkFunctionAddress(var6);
      nglProgramUniform3ui(var0, var1, var2, var3, var4, var6);
   }

   static native void nglProgramUniform3ui(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glProgramUniform4ui(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramUniform4ui;
      BufferChecks.checkFunctionAddress(var7);
      nglProgramUniform4ui(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglProgramUniform4ui(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glProgramUniform1u(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform1uiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform1uiv(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform1uiv(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform2u(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform2uiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform2uiv(var0, var1, var2.remaining() >> 1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform2uiv(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform3u(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform3uiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform3uiv(var0, var1, var2.remaining() / 3, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform3uiv(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform4u(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform4uiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform4uiv(var0, var1, var2.remaining() >> 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform4uiv(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniformMatrix2(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix2fv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix2fv(var0, var1, var3.remaining() >> 2, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix2fv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix3(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix3fv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix3fv(var0, var1, var3.remaining() / 9, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix3fv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix4(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix4fv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix4fv(var0, var1, var3.remaining() >> 4, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix4fv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix2(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix2dv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix2dv(var0, var1, var3.remaining() >> 2, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix2dv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix3(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix3dv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix3dv(var0, var1, var3.remaining() / 9, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix3dv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix4(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix4dv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix4dv(var0, var1, var3.remaining() >> 4, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix4dv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix2x3(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix2x3fv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix2x3fv(var0, var1, var3.remaining() / 6, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix2x3fv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix3x2(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix3x2fv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix3x2fv(var0, var1, var3.remaining() / 6, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix3x2fv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix2x4(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix2x4fv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix2x4fv(var0, var1, var3.remaining() >> 3, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix2x4fv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix4x2(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix4x2fv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix4x2fv(var0, var1, var3.remaining() >> 3, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix4x2fv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix3x4(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix3x4fv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix3x4fv(var0, var1, var3.remaining() / 12, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix3x4fv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix4x3(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix4x3fv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix4x3fv(var0, var1, var3.remaining() / 12, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix4x3fv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix2x3(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix2x3dv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix2x3dv(var0, var1, var3.remaining() / 6, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix2x3dv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix3x2(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix3x2dv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix3x2dv(var0, var1, var3.remaining() / 6, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix3x2dv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix2x4(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix2x4dv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix2x4dv(var0, var1, var3.remaining() >> 3, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix2x4dv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix4x2(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix4x2dv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix4x2dv(var0, var1, var3.remaining() >> 3, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix4x2dv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix3x4(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix3x4dv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix3x4dv(var0, var1, var3.remaining() / 12, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix3x4dv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix4x3(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix4x3dv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix4x3dv(var0, var1, var3.remaining() / 12, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix4x3dv(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glValidateProgramPipeline(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glValidateProgramPipeline;
      BufferChecks.checkFunctionAddress(var2);
      nglValidateProgramPipeline(var0, var2);
   }

   static native void nglValidateProgramPipeline(int var0, long var1);

   public static void glGetProgramPipelineInfoLog(int var0, IntBuffer var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramPipelineInfoLog;
      BufferChecks.checkFunctionAddress(var4);
      if (var1 != null) {
         BufferChecks.checkBuffer((IntBuffer)var1, 1);
      }

      BufferChecks.checkDirect(var2);
      nglGetProgramPipelineInfoLog(var0, var2.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramPipelineInfoLog(int var0, int var1, long var2, long var4, long var6);

   public static String glGetProgramPipelineInfoLog(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetProgramPipelineInfoLog;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getLengths(var2);
      ByteBuffer var6 = APIUtil.getBufferByte(var2, var1);
      nglGetProgramPipelineInfoLog(var0, var1, MemoryUtil.getAddress0((Buffer)var5), MemoryUtil.getAddress(var6), var3);
      var6.limit(var5.get(0));
      return APIUtil.getString(var2, var6);
   }

   public static void glVertexAttribL1d(int var0, double var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttribL1d;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttribL1d(var0, var1, var4);
   }

   static native void nglVertexAttribL1d(int var0, double var1, long var3);

   public static void glVertexAttribL2d(int var0, double var1, double var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribL2d;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttribL2d(var0, var1, var3, var6);
   }

   static native void nglVertexAttribL2d(int var0, double var1, double var3, long var5);

   public static void glVertexAttribL3d(int var0, double var1, double var3, double var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glVertexAttribL3d;
      BufferChecks.checkFunctionAddress(var8);
      nglVertexAttribL3d(var0, var1, var3, var5, var8);
   }

   static native void nglVertexAttribL3d(int var0, double var1, double var3, double var5, long var7);

   public static void glVertexAttribL4d(int var0, double var1, double var3, double var5, double var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glVertexAttribL4d;
      BufferChecks.checkFunctionAddress(var10);
      nglVertexAttribL4d(var0, var1, var3, var5, var7, var10);
   }

   static native void nglVertexAttribL4d(int var0, double var1, double var3, double var5, double var7, long var9);

   public static void glVertexAttribL1(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribL1dv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 1);
      nglVertexAttribL1dv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribL1dv(int var0, long var1, long var3);

   public static void glVertexAttribL2(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribL2dv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 2);
      nglVertexAttribL2dv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribL2dv(int var0, long var1, long var3);

   public static void glVertexAttribL3(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribL3dv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 3);
      nglVertexAttribL3dv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribL3dv(int var0, long var1, long var3);

   public static void glVertexAttribL4(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribL4dv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 4);
      nglVertexAttribL4dv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribL4dv(int var0, long var1, long var3);

   public static void glVertexAttribLPointer(int var0, int var1, int var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribLPointer;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var4).glVertexAttribPointer_buffer[var0] = var3;
      }

      nglVertexAttribLPointer(var0, var1, 5130, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglVertexAttribLPointer(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glVertexAttribLPointer(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribLPointer;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOenabled(var5);
      nglVertexAttribLPointerBO(var0, var1, 5130, var2, var3, var6);
   }

   static native void nglVertexAttribLPointerBO(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glGetVertexAttribL(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribLdv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((DoubleBuffer)var2, 4);
      nglGetVertexAttribLdv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribLdv(int var0, int var1, long var2, long var4);

   public static void glViewportArray(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glViewportArrayv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglViewportArrayv(var0, var1.remaining() >> 2, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglViewportArrayv(int var0, int var1, long var2, long var4);

   public static void glViewportIndexedf(int var0, float var1, float var2, float var3, float var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glViewportIndexedf;
      BufferChecks.checkFunctionAddress(var6);
      nglViewportIndexedf(var0, var1, var2, var3, var4, var6);
   }

   static native void nglViewportIndexedf(int var0, float var1, float var2, float var3, float var4, long var5);

   public static void glViewportIndexed(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glViewportIndexedfv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 4);
      nglViewportIndexedfv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglViewportIndexedfv(int var0, long var1, long var3);

   public static void glScissorArray(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glScissorArrayv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglScissorArrayv(var0, var1.remaining() >> 2, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglScissorArrayv(int var0, int var1, long var2, long var4);

   public static void glScissorIndexed(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glScissorIndexed;
      BufferChecks.checkFunctionAddress(var6);
      nglScissorIndexed(var0, var1, var2, var3, var4, var6);
   }

   static native void nglScissorIndexed(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glScissorIndexed(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glScissorIndexedv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglScissorIndexedv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglScissorIndexedv(int var0, long var1, long var3);

   public static void glDepthRangeArray(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDepthRangeArrayv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglDepthRangeArrayv(var0, var1.remaining() >> 1, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglDepthRangeArrayv(int var0, int var1, long var2, long var4);

   public static void glDepthRangeIndexed(int var0, double var1, double var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDepthRangeIndexed;
      BufferChecks.checkFunctionAddress(var6);
      nglDepthRangeIndexed(var0, var1, var3, var6);
   }

   static native void nglDepthRangeIndexed(int var0, double var1, double var3, long var5);

   public static void glGetFloat(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetFloati_v;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetFloati_v(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetFloati_v(int var0, int var1, long var2, long var4);

   public static float glGetFloat(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetFloati_v;
      BufferChecks.checkFunctionAddress(var3);
      FloatBuffer var5 = APIUtil.getBufferFloat(var2);
      nglGetFloati_v(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetDouble(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetDoublei_v;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetDoublei_v(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetDoublei_v(int var0, int var1, long var2, long var4);

   public static double glGetDouble(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetDoublei_v;
      BufferChecks.checkFunctionAddress(var3);
      DoubleBuffer var5 = APIUtil.getBufferDouble(var2);
      nglGetDoublei_v(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }
}
