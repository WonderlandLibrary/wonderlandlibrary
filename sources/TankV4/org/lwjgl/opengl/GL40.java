package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class GL40 {
   public static final int GL_DRAW_INDIRECT_BUFFER = 36671;
   public static final int GL_DRAW_INDIRECT_BUFFER_BINDING = 36675;
   public static final int GL_GEOMETRY_SHADER_INVOCATIONS = 34943;
   public static final int GL_MAX_GEOMETRY_SHADER_INVOCATIONS = 36442;
   public static final int GL_MIN_FRAGMENT_INTERPOLATION_OFFSET = 36443;
   public static final int GL_MAX_FRAGMENT_INTERPOLATION_OFFSET = 36444;
   public static final int GL_FRAGMENT_INTERPOLATION_OFFSET_BITS = 36445;
   public static final int GL_MAX_VERTEX_STREAMS = 36465;
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
   public static final int GL_SAMPLE_SHADING = 35894;
   public static final int GL_MIN_SAMPLE_SHADING_VALUE = 35895;
   public static final int GL_ACTIVE_SUBROUTINES = 36325;
   public static final int GL_ACTIVE_SUBROUTINE_UNIFORMS = 36326;
   public static final int GL_ACTIVE_SUBROUTINE_UNIFORM_LOCATIONS = 36423;
   public static final int GL_ACTIVE_SUBROUTINE_MAX_LENGTH = 36424;
   public static final int GL_ACTIVE_SUBROUTINE_UNIFORM_MAX_LENGTH = 36425;
   public static final int GL_MAX_SUBROUTINES = 36327;
   public static final int GL_MAX_SUBROUTINE_UNIFORM_LOCATIONS = 36328;
   public static final int GL_NUM_COMPATIBLE_SUBROUTINES = 36426;
   public static final int GL_COMPATIBLE_SUBROUTINES = 36427;
   public static final int GL_UNIFORM_SIZE = 35384;
   public static final int GL_UNIFORM_NAME_LENGTH = 35385;
   public static final int GL_PATCHES = 14;
   public static final int GL_PATCH_VERTICES = 36466;
   public static final int GL_PATCH_DEFAULT_INNER_LEVEL = 36467;
   public static final int GL_PATCH_DEFAULT_OUTER_LEVEL = 36468;
   public static final int GL_TESS_CONTROL_OUTPUT_VERTICES = 36469;
   public static final int GL_TESS_GEN_MODE = 36470;
   public static final int GL_TESS_GEN_SPACING = 36471;
   public static final int GL_TESS_GEN_VERTEX_ORDER = 36472;
   public static final int GL_TESS_GEN_POINT_MODE = 36473;
   public static final int GL_ISOLINES = 36474;
   public static final int GL_FRACTIONAL_ODD = 36475;
   public static final int GL_FRACTIONAL_EVEN = 36476;
   public static final int GL_MAX_PATCH_VERTICES = 36477;
   public static final int GL_MAX_TESS_GEN_LEVEL = 36478;
   public static final int GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS = 36479;
   public static final int GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS = 36480;
   public static final int GL_MAX_TESS_CONTROL_TEXTURE_IMAGE_UNITS = 36481;
   public static final int GL_MAX_TESS_EVALUATION_TEXTURE_IMAGE_UNITS = 36482;
   public static final int GL_MAX_TESS_CONTROL_OUTPUT_COMPONENTS = 36483;
   public static final int GL_MAX_TESS_PATCH_COMPONENTS = 36484;
   public static final int GL_MAX_TESS_CONTROL_TOTAL_OUTPUT_COMPONENTS = 36485;
   public static final int GL_MAX_TESS_EVALUATION_OUTPUT_COMPONENTS = 36486;
   public static final int GL_MAX_TESS_CONTROL_UNIFORM_BLOCKS = 36489;
   public static final int GL_MAX_TESS_EVALUATION_UNIFORM_BLOCKS = 36490;
   public static final int GL_MAX_TESS_CONTROL_INPUT_COMPONENTS = 34924;
   public static final int GL_MAX_TESS_EVALUATION_INPUT_COMPONENTS = 34925;
   public static final int GL_MAX_COMBINED_TESS_CONTROL_UNIFORM_COMPONENTS = 36382;
   public static final int GL_MAX_COMBINED_TESS_EVALUATION_UNIFORM_COMPONENTS = 36383;
   public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_CONTROL_SHADER = 34032;
   public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_EVALUATION_SHADER = 34033;
   public static final int GL_TESS_EVALUATION_SHADER = 36487;
   public static final int GL_TESS_CONTROL_SHADER = 36488;
   public static final int GL_TEXTURE_CUBE_MAP_ARRAY = 36873;
   public static final int GL_TEXTURE_BINDING_CUBE_MAP_ARRAY = 36874;
   public static final int GL_PROXY_TEXTURE_CUBE_MAP_ARRAY = 36875;
   public static final int GL_SAMPLER_CUBE_MAP_ARRAY = 36876;
   public static final int GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW = 36877;
   public static final int GL_INT_SAMPLER_CUBE_MAP_ARRAY = 36878;
   public static final int GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY = 36879;
   public static final int GL_MIN_PROGRAM_TEXTURE_GATHER_OFFSET_ARB = 36446;
   public static final int GL_MAX_PROGRAM_TEXTURE_GATHER_OFFSET_ARB = 36447;
   public static final int GL_MAX_PROGRAM_TEXTURE_GATHER_COMPONENTS_ARB = 36767;
   public static final int GL_TRANSFORM_FEEDBACK = 36386;
   public static final int GL_TRANSFORM_FEEDBACK_PAUSED = 36387;
   public static final int GL_TRANSFORM_FEEDBACK_ACTIVE = 36388;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED = 36387;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE = 36388;
   public static final int GL_TRANSFORM_FEEDBACK_BINDING = 36389;
   public static final int GL_MAX_TRANSFORM_FEEDBACK_BUFFERS = 36464;

   private GL40() {
   }

   public static void glBlendEquationi(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBlendEquationi;
      BufferChecks.checkFunctionAddress(var3);
      nglBlendEquationi(var0, var1, var3);
   }

   static native void nglBlendEquationi(int var0, int var1, long var2);

   public static void glBlendEquationSeparatei(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBlendEquationSeparatei;
      BufferChecks.checkFunctionAddress(var4);
      nglBlendEquationSeparatei(var0, var1, var2, var4);
   }

   static native void nglBlendEquationSeparatei(int var0, int var1, int var2, long var3);

   public static void glBlendFunci(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBlendFunci;
      BufferChecks.checkFunctionAddress(var4);
      nglBlendFunci(var0, var1, var2, var4);
   }

   static native void nglBlendFunci(int var0, int var1, int var2, long var3);

   public static void glBlendFuncSeparatei(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glBlendFuncSeparatei;
      BufferChecks.checkFunctionAddress(var6);
      nglBlendFuncSeparatei(var0, var1, var2, var3, var4, var6);
   }

   static native void nglBlendFuncSeparatei(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glDrawArraysIndirect(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDrawArraysIndirect;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureIndirectBOdisabled(var2);
      BufferChecks.checkBuffer((ByteBuffer)var1, 16);
      nglDrawArraysIndirect(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglDrawArraysIndirect(int var0, long var1, long var3);

   public static void glDrawArraysIndirect(int var0, long var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glDrawArraysIndirect;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureIndirectBOenabled(var3);
      nglDrawArraysIndirectBO(var0, var1, var4);
   }

   static native void nglDrawArraysIndirectBO(int var0, long var1, long var3);

   public static void glDrawArraysIndirect(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDrawArraysIndirect;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureIndirectBOdisabled(var2);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglDrawArraysIndirect(var0, MemoryUtil.getAddress(var1), var3);
   }

   public static void glDrawElementsIndirect(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glDrawElementsIndirect;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureIndirectBOdisabled(var3);
      BufferChecks.checkBuffer((ByteBuffer)var2, 20);
      nglDrawElementsIndirect(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglDrawElementsIndirect(int var0, int var1, long var2, long var4);

   public static void glDrawElementsIndirect(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawElementsIndirect;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureIndirectBOenabled(var4);
      nglDrawElementsIndirectBO(var0, var1, var2, var5);
   }

   static native void nglDrawElementsIndirectBO(int var0, int var1, long var2, long var4);

   public static void glDrawElementsIndirect(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glDrawElementsIndirect;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureIndirectBOdisabled(var3);
      BufferChecks.checkBuffer((IntBuffer)var2, 5);
      nglDrawElementsIndirect(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glUniform1d(int var0, double var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniform1d;
      BufferChecks.checkFunctionAddress(var4);
      nglUniform1d(var0, var1, var4);
   }

   static native void nglUniform1d(int var0, double var1, long var3);

   public static void glUniform2d(int var0, double var1, double var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glUniform2d;
      BufferChecks.checkFunctionAddress(var6);
      nglUniform2d(var0, var1, var3, var6);
   }

   static native void nglUniform2d(int var0, double var1, double var3, long var5);

   public static void glUniform3d(int var0, double var1, double var3, double var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glUniform3d;
      BufferChecks.checkFunctionAddress(var8);
      nglUniform3d(var0, var1, var3, var5, var8);
   }

   static native void nglUniform3d(int var0, double var1, double var3, double var5, long var7);

   public static void glUniform4d(int var0, double var1, double var3, double var5, double var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glUniform4d;
      BufferChecks.checkFunctionAddress(var10);
      nglUniform4d(var0, var1, var3, var5, var7, var10);
   }

   static native void nglUniform4d(int var0, double var1, double var3, double var5, double var7, long var9);

   public static void glUniform1(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform1dv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform1dv(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform1dv(int var0, int var1, long var2, long var4);

   public static void glUniform2(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform2dv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform2dv(var0, var1.remaining() >> 1, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform2dv(int var0, int var1, long var2, long var4);

   public static void glUniform3(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform3dv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform3dv(var0, var1.remaining() / 3, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform3dv(int var0, int var1, long var2, long var4);

   public static void glUniform4(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform4dv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform4dv(var0, var1.remaining() >> 2, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform4dv(int var0, int var1, long var2, long var4);

   public static void glUniformMatrix2(int var0, boolean var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformMatrix2dv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglUniformMatrix2dv(var0, var2.remaining() >> 2, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglUniformMatrix2dv(int var0, int var1, boolean var2, long var3, long var5);

   public static void glUniformMatrix3(int var0, boolean var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformMatrix3dv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglUniformMatrix3dv(var0, var2.remaining() / 9, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglUniformMatrix3dv(int var0, int var1, boolean var2, long var3, long var5);

   public static void glUniformMatrix4(int var0, boolean var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformMatrix4dv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglUniformMatrix4dv(var0, var2.remaining() >> 4, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglUniformMatrix4dv(int var0, int var1, boolean var2, long var3, long var5);

   public static void glUniformMatrix2x3(int var0, boolean var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformMatrix2x3dv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglUniformMatrix2x3dv(var0, var2.remaining() / 6, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglUniformMatrix2x3dv(int var0, int var1, boolean var2, long var3, long var5);

   public static void glUniformMatrix2x4(int var0, boolean var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformMatrix2x4dv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglUniformMatrix2x4dv(var0, var2.remaining() >> 3, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglUniformMatrix2x4dv(int var0, int var1, boolean var2, long var3, long var5);

   public static void glUniformMatrix3x2(int var0, boolean var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformMatrix3x2dv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglUniformMatrix3x2dv(var0, var2.remaining() / 6, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglUniformMatrix3x2dv(int var0, int var1, boolean var2, long var3, long var5);

   public static void glUniformMatrix3x4(int var0, boolean var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformMatrix3x4dv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglUniformMatrix3x4dv(var0, var2.remaining() / 12, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglUniformMatrix3x4dv(int var0, int var1, boolean var2, long var3, long var5);

   public static void glUniformMatrix4x2(int var0, boolean var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformMatrix4x2dv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglUniformMatrix4x2dv(var0, var2.remaining() >> 3, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglUniformMatrix4x2dv(int var0, int var1, boolean var2, long var3, long var5);

   public static void glUniformMatrix4x3(int var0, boolean var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformMatrix4x3dv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglUniformMatrix4x3dv(var0, var2.remaining() / 12, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglUniformMatrix4x3dv(int var0, int var1, boolean var2, long var3, long var5);

   public static void glGetUniform(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetUniformdv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetUniformdv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetUniformdv(int var0, int var1, long var2, long var4);

   public static void glMinSampleShading(float var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glMinSampleShading;
      BufferChecks.checkFunctionAddress(var2);
      nglMinSampleShading(var0, var2);
   }

   static native void nglMinSampleShading(float var0, long var1);

   public static int glGetSubroutineUniformLocation(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetSubroutineUniformLocation;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2);
      int var6 = nglGetSubroutineUniformLocation(var0, var1, MemoryUtil.getAddress(var2), var4);
      return var6;
   }

   static native int nglGetSubroutineUniformLocation(int var0, int var1, long var2, long var4);

   public static int glGetSubroutineUniformLocation(int var0, int var1, CharSequence var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetSubroutineUniformLocation;
      BufferChecks.checkFunctionAddress(var4);
      int var6 = nglGetSubroutineUniformLocation(var0, var1, APIUtil.getBufferNT(var3, var2), var4);
      return var6;
   }

   public static int glGetSubroutineIndex(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetSubroutineIndex;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2);
      int var6 = nglGetSubroutineIndex(var0, var1, MemoryUtil.getAddress(var2), var4);
      return var6;
   }

   static native int nglGetSubroutineIndex(int var0, int var1, long var2, long var4);

   public static int glGetSubroutineIndex(int var0, int var1, CharSequence var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetSubroutineIndex;
      BufferChecks.checkFunctionAddress(var4);
      int var6 = nglGetSubroutineIndex(var0, var1, APIUtil.getBufferNT(var3, var2), var4);
      return var6;
   }

   public static void glGetActiveSubroutineUniform(int var0, int var1, int var2, int var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetActiveSubroutineUniformiv;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkBuffer((IntBuffer)var4, 1);
      nglGetActiveSubroutineUniformiv(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglGetActiveSubroutineUniformiv(int var0, int var1, int var2, int var3, long var4, long var6);

   /** @deprecated */
   @Deprecated
   public static int glGetActiveSubroutineUniform(int var0, int var1, int var2, int var3) {
      return glGetActiveSubroutineUniformi(var0, var1, var2, var3);
   }

   public static int glGetActiveSubroutineUniformi(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetActiveSubroutineUniformiv;
      BufferChecks.checkFunctionAddress(var5);
      IntBuffer var7 = APIUtil.getBufferInt(var4);
      nglGetActiveSubroutineUniformiv(var0, var1, var2, var3, MemoryUtil.getAddress(var7), var5);
      return var7.get(0);
   }

   public static void glGetActiveSubroutineUniformName(int var0, int var1, int var2, IntBuffer var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetActiveSubroutineUniformName;
      BufferChecks.checkFunctionAddress(var6);
      if (var3 != null) {
         BufferChecks.checkBuffer((IntBuffer)var3, 1);
      }

      BufferChecks.checkDirect(var4);
      nglGetActiveSubroutineUniformName(var0, var1, var2, var4.remaining(), MemoryUtil.getAddressSafe(var3), MemoryUtil.getAddress(var4), var6);
   }

   static native void nglGetActiveSubroutineUniformName(int var0, int var1, int var2, int var3, long var4, long var6, long var8);

   public static String glGetActiveSubroutineUniformName(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetActiveSubroutineUniformName;
      BufferChecks.checkFunctionAddress(var5);
      IntBuffer var7 = APIUtil.getLengths(var4);
      ByteBuffer var8 = APIUtil.getBufferByte(var4, var3);
      nglGetActiveSubroutineUniformName(var0, var1, var2, var3, MemoryUtil.getAddress0((Buffer)var7), MemoryUtil.getAddress(var8), var5);
      var8.limit(var7.get(0));
      return APIUtil.getString(var4, var8);
   }

   public static void glGetActiveSubroutineName(int var0, int var1, int var2, IntBuffer var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetActiveSubroutineName;
      BufferChecks.checkFunctionAddress(var6);
      if (var3 != null) {
         BufferChecks.checkBuffer((IntBuffer)var3, 1);
      }

      BufferChecks.checkDirect(var4);
      nglGetActiveSubroutineName(var0, var1, var2, var4.remaining(), MemoryUtil.getAddressSafe(var3), MemoryUtil.getAddress(var4), var6);
   }

   static native void nglGetActiveSubroutineName(int var0, int var1, int var2, int var3, long var4, long var6, long var8);

   public static String glGetActiveSubroutineName(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetActiveSubroutineName;
      BufferChecks.checkFunctionAddress(var5);
      IntBuffer var7 = APIUtil.getLengths(var4);
      ByteBuffer var8 = APIUtil.getBufferByte(var4, var3);
      nglGetActiveSubroutineName(var0, var1, var2, var3, MemoryUtil.getAddress0((Buffer)var7), MemoryUtil.getAddress(var8), var5);
      var8.limit(var7.get(0));
      return APIUtil.getString(var4, var8);
   }

   public static void glUniformSubroutinesu(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniformSubroutinesuiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniformSubroutinesuiv(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniformSubroutinesuiv(int var0, int var1, long var2, long var4);

   public static void glGetUniformSubroutineu(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetUniformSubroutineuiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetUniformSubroutineuiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetUniformSubroutineuiv(int var0, int var1, long var2, long var4);

   /** @deprecated */
   @Deprecated
   public static int glGetUniformSubroutineu(int var0, int var1) {
      return glGetUniformSubroutineui(var0, var1);
   }

   public static int glGetUniformSubroutineui(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetUniformSubroutineuiv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetUniformSubroutineuiv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetProgramStage(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetProgramStageiv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      nglGetProgramStageiv(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetProgramStageiv(int var0, int var1, int var2, long var3, long var5);

   /** @deprecated */
   @Deprecated
   public static int glGetProgramStage(int var0, int var1, int var2) {
      return glGetProgramStagei(var0, var1, var2);
   }

   public static int glGetProgramStagei(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramStageiv;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetProgramStageiv(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glPatchParameteri(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPatchParameteri;
      BufferChecks.checkFunctionAddress(var3);
      nglPatchParameteri(var0, var1, var3);
   }

   static native void nglPatchParameteri(int var0, int var1, long var2);

   public static void glPatchParameter(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPatchParameterfv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 4);
      nglPatchParameterfv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglPatchParameterfv(int var0, long var1, long var3);

   public static void glBindTransformFeedback(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBindTransformFeedback;
      BufferChecks.checkFunctionAddress(var3);
      nglBindTransformFeedback(var0, var1, var3);
   }

   static native void nglBindTransformFeedback(int var0, int var1, long var2);

   public static void glDeleteTransformFeedbacks(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteTransformFeedbacks;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteTransformFeedbacks(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteTransformFeedbacks(int var0, long var1, long var3);

   public static void glDeleteTransformFeedbacks(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteTransformFeedbacks;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteTransformFeedbacks(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glGenTransformFeedbacks(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenTransformFeedbacks;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenTransformFeedbacks(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenTransformFeedbacks(int var0, long var1, long var3);

   public static int glGenTransformFeedbacks() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenTransformFeedbacks;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenTransformFeedbacks(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static boolean glIsTransformFeedback(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsTransformFeedback;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsTransformFeedback(var0, var2);
      return var4;
   }

   static native boolean nglIsTransformFeedback(int var0, long var1);

   public static void glPauseTransformFeedback() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glPauseTransformFeedback;
      BufferChecks.checkFunctionAddress(var1);
      nglPauseTransformFeedback(var1);
   }

   static native void nglPauseTransformFeedback(long var0);

   public static void glResumeTransformFeedback() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glResumeTransformFeedback;
      BufferChecks.checkFunctionAddress(var1);
      nglResumeTransformFeedback(var1);
   }

   static native void nglResumeTransformFeedback(long var0);

   public static void glDrawTransformFeedback(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDrawTransformFeedback;
      BufferChecks.checkFunctionAddress(var3);
      nglDrawTransformFeedback(var0, var1, var3);
   }

   static native void nglDrawTransformFeedback(int var0, int var1, long var2);

   public static void glDrawTransformFeedbackStream(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glDrawTransformFeedbackStream;
      BufferChecks.checkFunctionAddress(var4);
      nglDrawTransformFeedbackStream(var0, var1, var2, var4);
   }

   static native void nglDrawTransformFeedbackStream(int var0, int var1, int var2, long var3);

   public static void glBeginQueryIndexed(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBeginQueryIndexed;
      BufferChecks.checkFunctionAddress(var4);
      nglBeginQueryIndexed(var0, var1, var2, var4);
   }

   static native void nglBeginQueryIndexed(int var0, int var1, int var2, long var3);

   public static void glEndQueryIndexed(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glEndQueryIndexed;
      BufferChecks.checkFunctionAddress(var3);
      nglEndQueryIndexed(var0, var1, var3);
   }

   static native void nglEndQueryIndexed(int var0, int var1, long var2);

   public static void glGetQueryIndexed(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetQueryIndexediv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      nglGetQueryIndexediv(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetQueryIndexediv(int var0, int var1, int var2, long var3, long var5);

   /** @deprecated */
   @Deprecated
   public static int glGetQueryIndexed(int var0, int var1, int var2) {
      return glGetQueryIndexedi(var0, var1, var2);
   }

   public static int glGetQueryIndexedi(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetQueryIndexediv;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetQueryIndexediv(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }
}
