package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

public final class GL20 {
   public static final int GL_SHADING_LANGUAGE_VERSION = 35724;
   public static final int GL_CURRENT_PROGRAM = 35725;
   public static final int GL_SHADER_TYPE = 35663;
   public static final int GL_DELETE_STATUS = 35712;
   public static final int GL_COMPILE_STATUS = 35713;
   public static final int GL_LINK_STATUS = 35714;
   public static final int GL_VALIDATE_STATUS = 35715;
   public static final int GL_INFO_LOG_LENGTH = 35716;
   public static final int GL_ATTACHED_SHADERS = 35717;
   public static final int GL_ACTIVE_UNIFORMS = 35718;
   public static final int GL_ACTIVE_UNIFORM_MAX_LENGTH = 35719;
   public static final int GL_ACTIVE_ATTRIBUTES = 35721;
   public static final int GL_ACTIVE_ATTRIBUTE_MAX_LENGTH = 35722;
   public static final int GL_SHADER_SOURCE_LENGTH = 35720;
   public static final int GL_SHADER_OBJECT = 35656;
   public static final int GL_FLOAT_VEC2 = 35664;
   public static final int GL_FLOAT_VEC3 = 35665;
   public static final int GL_FLOAT_VEC4 = 35666;
   public static final int GL_INT_VEC2 = 35667;
   public static final int GL_INT_VEC3 = 35668;
   public static final int GL_INT_VEC4 = 35669;
   public static final int GL_BOOL = 35670;
   public static final int GL_BOOL_VEC2 = 35671;
   public static final int GL_BOOL_VEC3 = 35672;
   public static final int GL_BOOL_VEC4 = 35673;
   public static final int GL_FLOAT_MAT2 = 35674;
   public static final int GL_FLOAT_MAT3 = 35675;
   public static final int GL_FLOAT_MAT4 = 35676;
   public static final int GL_SAMPLER_1D = 35677;
   public static final int GL_SAMPLER_2D = 35678;
   public static final int GL_SAMPLER_3D = 35679;
   public static final int GL_SAMPLER_CUBE = 35680;
   public static final int GL_SAMPLER_1D_SHADOW = 35681;
   public static final int GL_SAMPLER_2D_SHADOW = 35682;
   public static final int GL_VERTEX_SHADER = 35633;
   public static final int GL_MAX_VERTEX_UNIFORM_COMPONENTS = 35658;
   public static final int GL_MAX_VARYING_FLOATS = 35659;
   public static final int GL_MAX_VERTEX_ATTRIBS = 34921;
   public static final int GL_MAX_TEXTURE_IMAGE_UNITS = 34930;
   public static final int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS = 35660;
   public static final int GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS = 35661;
   public static final int GL_MAX_TEXTURE_COORDS = 34929;
   public static final int GL_VERTEX_PROGRAM_POINT_SIZE = 34370;
   public static final int GL_VERTEX_PROGRAM_TWO_SIDE = 34371;
   public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED = 34338;
   public static final int GL_VERTEX_ATTRIB_ARRAY_SIZE = 34339;
   public static final int GL_VERTEX_ATTRIB_ARRAY_STRIDE = 34340;
   public static final int GL_VERTEX_ATTRIB_ARRAY_TYPE = 34341;
   public static final int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED = 34922;
   public static final int GL_CURRENT_VERTEX_ATTRIB = 34342;
   public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER = 34373;
   public static final int GL_FRAGMENT_SHADER = 35632;
   public static final int GL_MAX_FRAGMENT_UNIFORM_COMPONENTS = 35657;
   public static final int GL_FRAGMENT_SHADER_DERIVATIVE_HINT = 35723;
   public static final int GL_MAX_DRAW_BUFFERS = 34852;
   public static final int GL_DRAW_BUFFER0 = 34853;
   public static final int GL_DRAW_BUFFER1 = 34854;
   public static final int GL_DRAW_BUFFER2 = 34855;
   public static final int GL_DRAW_BUFFER3 = 34856;
   public static final int GL_DRAW_BUFFER4 = 34857;
   public static final int GL_DRAW_BUFFER5 = 34858;
   public static final int GL_DRAW_BUFFER6 = 34859;
   public static final int GL_DRAW_BUFFER7 = 34860;
   public static final int GL_DRAW_BUFFER8 = 34861;
   public static final int GL_DRAW_BUFFER9 = 34862;
   public static final int GL_DRAW_BUFFER10 = 34863;
   public static final int GL_DRAW_BUFFER11 = 34864;
   public static final int GL_DRAW_BUFFER12 = 34865;
   public static final int GL_DRAW_BUFFER13 = 34866;
   public static final int GL_DRAW_BUFFER14 = 34867;
   public static final int GL_DRAW_BUFFER15 = 34868;
   public static final int GL_POINT_SPRITE = 34913;
   public static final int GL_COORD_REPLACE = 34914;
   public static final int GL_POINT_SPRITE_COORD_ORIGIN = 36000;
   public static final int GL_LOWER_LEFT = 36001;
   public static final int GL_UPPER_LEFT = 36002;
   public static final int GL_STENCIL_BACK_FUNC = 34816;
   public static final int GL_STENCIL_BACK_FAIL = 34817;
   public static final int GL_STENCIL_BACK_PASS_DEPTH_FAIL = 34818;
   public static final int GL_STENCIL_BACK_PASS_DEPTH_PASS = 34819;
   public static final int GL_STENCIL_BACK_REF = 36003;
   public static final int GL_STENCIL_BACK_VALUE_MASK = 36004;
   public static final int GL_STENCIL_BACK_WRITEMASK = 36005;
   public static final int GL_BLEND_EQUATION_RGB = 32777;
   public static final int GL_BLEND_EQUATION_ALPHA = 34877;

   private GL20() {
   }

   public static void glShaderSource(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glShaderSource;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglShaderSource(var0, 1, MemoryUtil.getAddress(var1), var1.remaining(), var3);
   }

   static native void nglShaderSource(int var0, int var1, long var2, int var4, long var5);

   public static void glShaderSource(int var0, CharSequence var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glShaderSource;
      BufferChecks.checkFunctionAddress(var3);
      nglShaderSource(var0, 1, APIUtil.getBuffer(var2, var1), var1.length(), var3);
   }

   public static void glShaderSource(int var0, CharSequence[] var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glShaderSource;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkArray(var1);
      nglShaderSource3(var0, var1.length, APIUtil.getBuffer(var2, var1), APIUtil.getLengths(var2, var1), var3);
   }

   static native void nglShaderSource3(int var0, int var1, long var2, long var4, long var6);

   public static int glCreateShader(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glCreateShader;
      BufferChecks.checkFunctionAddress(var2);
      int var4 = nglCreateShader(var0, var2);
      return var4;
   }

   static native int nglCreateShader(int var0, long var1);

   public static boolean glIsShader(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsShader;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsShader(var0, var2);
      return var4;
   }

   static native boolean nglIsShader(int var0, long var1);

   public static void glCompileShader(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glCompileShader;
      BufferChecks.checkFunctionAddress(var2);
      nglCompileShader(var0, var2);
   }

   static native void nglCompileShader(int var0, long var1);

   public static void glDeleteShader(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteShader;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteShader(var0, var2);
   }

   static native void nglDeleteShader(int var0, long var1);

   public static int glCreateProgram() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glCreateProgram;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nglCreateProgram(var1);
      return var3;
   }

   static native int nglCreateProgram(long var0);

   public static boolean glIsProgram(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsProgram;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsProgram(var0, var2);
      return var4;
   }

   static native boolean nglIsProgram(int var0, long var1);

   public static void glAttachShader(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glAttachShader;
      BufferChecks.checkFunctionAddress(var3);
      nglAttachShader(var0, var1, var3);
   }

   static native void nglAttachShader(int var0, int var1, long var2);

   public static void glDetachShader(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDetachShader;
      BufferChecks.checkFunctionAddress(var3);
      nglDetachShader(var0, var1, var3);
   }

   static native void nglDetachShader(int var0, int var1, long var2);

   public static void glLinkProgram(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glLinkProgram;
      BufferChecks.checkFunctionAddress(var2);
      nglLinkProgram(var0, var2);
   }

   static native void nglLinkProgram(int var0, long var1);

   public static void glUseProgram(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glUseProgram;
      BufferChecks.checkFunctionAddress(var2);
      nglUseProgram(var0, var2);
   }

   static native void nglUseProgram(int var0, long var1);

   public static void glValidateProgram(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glValidateProgram;
      BufferChecks.checkFunctionAddress(var2);
      nglValidateProgram(var0, var2);
   }

   static native void nglValidateProgram(int var0, long var1);

   public static void glDeleteProgram(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteProgram;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteProgram(var0, var2);
   }

   static native void nglDeleteProgram(int var0, long var1);

   public static void glUniform1f(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform1f;
      BufferChecks.checkFunctionAddress(var3);
      nglUniform1f(var0, var1, var3);
   }

   static native void nglUniform1f(int var0, float var1, long var2);

   public static void glUniform2f(int var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniform2f;
      BufferChecks.checkFunctionAddress(var4);
      nglUniform2f(var0, var1, var2, var4);
   }

   static native void nglUniform2f(int var0, float var1, float var2, long var3);

   public static void glUniform3f(int var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glUniform3f;
      BufferChecks.checkFunctionAddress(var5);
      nglUniform3f(var0, var1, var2, var3, var5);
   }

   static native void nglUniform3f(int var0, float var1, float var2, float var3, long var4);

   public static void glUniform4f(int var0, float var1, float var2, float var3, float var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glUniform4f;
      BufferChecks.checkFunctionAddress(var6);
      nglUniform4f(var0, var1, var2, var3, var4, var6);
   }

   static native void nglUniform4f(int var0, float var1, float var2, float var3, float var4, long var5);

   public static void glUniform1i(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform1i;
      BufferChecks.checkFunctionAddress(var3);
      nglUniform1i(var0, var1, var3);
   }

   static native void nglUniform1i(int var0, int var1, long var2);

   public static void glUniform2i(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniform2i;
      BufferChecks.checkFunctionAddress(var4);
      nglUniform2i(var0, var1, var2, var4);
   }

   static native void nglUniform2i(int var0, int var1, int var2, long var3);

   public static void glUniform3i(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glUniform3i;
      BufferChecks.checkFunctionAddress(var5);
      nglUniform3i(var0, var1, var2, var3, var5);
   }

   static native void nglUniform3i(int var0, int var1, int var2, int var3, long var4);

   public static void glUniform4i(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glUniform4i;
      BufferChecks.checkFunctionAddress(var6);
      nglUniform4i(var0, var1, var2, var3, var4, var6);
   }

   static native void nglUniform4i(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glUniform1(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform1fv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform1fv(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform1fv(int var0, int var1, long var2, long var4);

   public static void glUniform2(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform2fv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform2fv(var0, var1.remaining() >> 1, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform2fv(int var0, int var1, long var2, long var4);

   public static void glUniform3(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform3fv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform3fv(var0, var1.remaining() / 3, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform3fv(int var0, int var1, long var2, long var4);

   public static void glUniform4(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform4fv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform4fv(var0, var1.remaining() >> 2, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform4fv(int var0, int var1, long var2, long var4);

   public static void glUniform1(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform1iv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform1iv(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform1iv(int var0, int var1, long var2, long var4);

   public static void glUniform2(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform2iv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform2iv(var0, var1.remaining() >> 1, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform2iv(int var0, int var1, long var2, long var4);

   public static void glUniform3(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform3iv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform3iv(var0, var1.remaining() / 3, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform3iv(int var0, int var1, long var2, long var4);

   public static void glUniform4(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform4iv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform4iv(var0, var1.remaining() >> 2, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform4iv(int var0, int var1, long var2, long var4);

   public static void glUniformMatrix2(int var0, boolean var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformMatrix2fv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglUniformMatrix2fv(var0, var2.remaining() >> 2, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglUniformMatrix2fv(int var0, int var1, boolean var2, long var3, long var5);

   public static void glUniformMatrix3(int var0, boolean var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformMatrix3fv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglUniformMatrix3fv(var0, var2.remaining() / 9, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglUniformMatrix3fv(int var0, int var1, boolean var2, long var3, long var5);

   public static void glUniformMatrix4(int var0, boolean var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformMatrix4fv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglUniformMatrix4fv(var0, var2.remaining() >> 4, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglUniformMatrix4fv(int var0, int var1, boolean var2, long var3, long var5);

   public static void glGetShader(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetShaderiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetShaderiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetShaderiv(int var0, int var1, long var2, long var4);

   /** @deprecated */
   @Deprecated
   public static int glGetShader(int var0, int var1) {
      return glGetShaderi(var0, var1);
   }

   public static int glGetShaderi(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetShaderiv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetShaderiv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetProgram(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetProgramiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramiv(int var0, int var1, long var2, long var4);

   /** @deprecated */
   @Deprecated
   public static int glGetProgram(int var0, int var1) {
      return glGetProgrami(var0, var1);
   }

   public static int glGetProgrami(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetProgramiv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetProgramiv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetShaderInfoLog(int var0, IntBuffer var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetShaderInfoLog;
      BufferChecks.checkFunctionAddress(var4);
      if (var1 != null) {
         BufferChecks.checkBuffer((IntBuffer)var1, 1);
      }

      BufferChecks.checkDirect(var2);
      nglGetShaderInfoLog(var0, var2.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetShaderInfoLog(int var0, int var1, long var2, long var4, long var6);

   public static String glGetShaderInfoLog(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetShaderInfoLog;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getLengths(var2);
      ByteBuffer var6 = APIUtil.getBufferByte(var2, var1);
      nglGetShaderInfoLog(var0, var1, MemoryUtil.getAddress0((Buffer)var5), MemoryUtil.getAddress(var6), var3);
      var6.limit(var5.get(0));
      return APIUtil.getString(var2, var6);
   }

   public static void glGetProgramInfoLog(int var0, IntBuffer var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramInfoLog;
      BufferChecks.checkFunctionAddress(var4);
      if (var1 != null) {
         BufferChecks.checkBuffer((IntBuffer)var1, 1);
      }

      BufferChecks.checkDirect(var2);
      nglGetProgramInfoLog(var0, var2.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramInfoLog(int var0, int var1, long var2, long var4, long var6);

   public static String glGetProgramInfoLog(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetProgramInfoLog;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getLengths(var2);
      ByteBuffer var6 = APIUtil.getBufferByte(var2, var1);
      nglGetProgramInfoLog(var0, var1, MemoryUtil.getAddress0((Buffer)var5), MemoryUtil.getAddress(var6), var3);
      var6.limit(var5.get(0));
      return APIUtil.getString(var2, var6);
   }

   public static void glGetAttachedShaders(int var0, IntBuffer var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetAttachedShaders;
      BufferChecks.checkFunctionAddress(var4);
      if (var1 != null) {
         BufferChecks.checkBuffer((IntBuffer)var1, 1);
      }

      BufferChecks.checkDirect(var2);
      nglGetAttachedShaders(var0, var2.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetAttachedShaders(int var0, int var1, long var2, long var4, long var6);

   public static int glGetUniformLocation(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetUniformLocation;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((ByteBuffer)var1, 1);
      BufferChecks.checkNullTerminated(var1);
      int var5 = nglGetUniformLocation(var0, MemoryUtil.getAddress(var1), var3);
      return var5;
   }

   static native int nglGetUniformLocation(int var0, long var1, long var3);

   public static int glGetUniformLocation(int var0, CharSequence var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetUniformLocation;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nglGetUniformLocation(var0, APIUtil.getBufferNT(var2, var1), var3);
      return var5;
   }

   public static void glGetActiveUniform(int var0, int var1, IntBuffer var2, IntBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetActiveUniform;
      BufferChecks.checkFunctionAddress(var7);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      BufferChecks.checkBuffer((IntBuffer)var4, 1);
      BufferChecks.checkDirect(var5);
      nglGetActiveUniform(var0, var1, var5.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   static native void nglGetActiveUniform(int var0, int var1, int var2, long var3, long var5, long var7, long var9, long var11);

   public static String glGetActiveUniform(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetActiveUniform;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 2);
      IntBuffer var7 = APIUtil.getLengths(var4);
      ByteBuffer var8 = APIUtil.getBufferByte(var4, var2);
      nglGetActiveUniform(var0, var1, var2, MemoryUtil.getAddress0((Buffer)var7), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var3, var3.position() + 1), MemoryUtil.getAddress(var8), var5);
      var8.limit(var7.get(0));
      return APIUtil.getString(var4, var8);
   }

   public static String glGetActiveUniform(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetActiveUniform;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getLengths(var3);
      ByteBuffer var7 = APIUtil.getBufferByte(var3, var2);
      nglGetActiveUniform(var0, var1, var2, MemoryUtil.getAddress0((Buffer)var6), MemoryUtil.getAddress0((Buffer)APIUtil.getBufferInt(var3)), MemoryUtil.getAddress((IntBuffer)APIUtil.getBufferInt(var3), 1), MemoryUtil.getAddress(var7), var4);
      var7.limit(var6.get(0));
      return APIUtil.getString(var3, var7);
   }

   public static int glGetActiveUniformSize(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetActiveUniform;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetActiveUniform(var0, var1, 1, 0L, MemoryUtil.getAddress(var5), MemoryUtil.getAddress((IntBuffer)var5, 1), APIUtil.getBufferByte0(var2), var3);
      return var5.get(0);
   }

   public static int glGetActiveUniformType(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetActiveUniform;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetActiveUniform(var0, var1, 0, 0L, MemoryUtil.getAddress((IntBuffer)var5, 1), MemoryUtil.getAddress(var5), APIUtil.getBufferByte0(var2), var3);
      return var5.get(0);
   }

   public static void glGetUniform(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetUniformfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetUniformfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetUniformfv(int var0, int var1, long var2, long var4);

   public static void glGetUniform(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetUniformiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetUniformiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetUniformiv(int var0, int var1, long var2, long var4);

   public static void glGetShaderSource(int var0, IntBuffer var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetShaderSource;
      BufferChecks.checkFunctionAddress(var4);
      if (var1 != null) {
         BufferChecks.checkBuffer((IntBuffer)var1, 1);
      }

      BufferChecks.checkDirect(var2);
      nglGetShaderSource(var0, var2.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetShaderSource(int var0, int var1, long var2, long var4, long var6);

   public static String glGetShaderSource(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetShaderSource;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getLengths(var2);
      ByteBuffer var6 = APIUtil.getBufferByte(var2, var1);
      nglGetShaderSource(var0, var1, MemoryUtil.getAddress0((Buffer)var5), MemoryUtil.getAddress(var6), var3);
      var6.limit(var5.get(0));
      return APIUtil.getString(var2, var6);
   }

   public static void glVertexAttrib1s(int var0, short var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttrib1s;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexAttrib1s(var0, var1, var3);
   }

   static native void nglVertexAttrib1s(int var0, short var1, long var2);

   public static void glVertexAttrib1f(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttrib1f;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexAttrib1f(var0, var1, var3);
   }

   static native void nglVertexAttrib1f(int var0, float var1, long var2);

   public static void glVertexAttrib1d(int var0, double var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttrib1d;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttrib1d(var0, var1, var4);
   }

   static native void nglVertexAttrib1d(int var0, double var1, long var3);

   public static void glVertexAttrib2s(int var0, short var1, short var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttrib2s;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttrib2s(var0, var1, var2, var4);
   }

   static native void nglVertexAttrib2s(int var0, short var1, short var2, long var3);

   public static void glVertexAttrib2f(int var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttrib2f;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttrib2f(var0, var1, var2, var4);
   }

   static native void nglVertexAttrib2f(int var0, float var1, float var2, long var3);

   public static void glVertexAttrib2d(int var0, double var1, double var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttrib2d;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttrib2d(var0, var1, var3, var6);
   }

   static native void nglVertexAttrib2d(int var0, double var1, double var3, long var5);

   public static void glVertexAttrib3s(int var0, short var1, short var2, short var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttrib3s;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttrib3s(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttrib3s(int var0, short var1, short var2, short var3, long var4);

   public static void glVertexAttrib3f(int var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttrib3f;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttrib3f(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttrib3f(int var0, float var1, float var2, float var3, long var4);

   public static void glVertexAttrib3d(int var0, double var1, double var3, double var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glVertexAttrib3d;
      BufferChecks.checkFunctionAddress(var8);
      nglVertexAttrib3d(var0, var1, var3, var5, var8);
   }

   static native void nglVertexAttrib3d(int var0, double var1, double var3, double var5, long var7);

   public static void glVertexAttrib4s(int var0, short var1, short var2, short var3, short var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttrib4s;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttrib4s(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexAttrib4s(int var0, short var1, short var2, short var3, short var4, long var5);

   public static void glVertexAttrib4f(int var0, float var1, float var2, float var3, float var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttrib4f;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttrib4f(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexAttrib4f(int var0, float var1, float var2, float var3, float var4, long var5);

   public static void glVertexAttrib4d(int var0, double var1, double var3, double var5, double var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glVertexAttrib4d;
      BufferChecks.checkFunctionAddress(var10);
      nglVertexAttrib4d(var0, var1, var3, var5, var7, var10);
   }

   static native void nglVertexAttrib4d(int var0, double var1, double var3, double var5, double var7, long var9);

   public static void glVertexAttrib4Nub(int var0, byte var1, byte var2, byte var3, byte var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttrib4Nub;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttrib4Nub(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexAttrib4Nub(int var0, byte var1, byte var2, byte var3, byte var4, long var5);

   public static void glVertexAttribPointer(int var0, int var1, boolean var2, int var3, DoubleBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribPointer;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var5).glVertexAttribPointer_buffer[var0] = var4;
      }

      nglVertexAttribPointer(var0, var1, 5130, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glVertexAttribPointer(int var0, int var1, boolean var2, int var3, FloatBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribPointer;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var5).glVertexAttribPointer_buffer[var0] = var4;
      }

      nglVertexAttribPointer(var0, var1, 5126, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glVertexAttribPointer(int var0, int var1, boolean var2, boolean var3, int var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glVertexAttribPointer;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureArrayVBOdisabled(var6);
      BufferChecks.checkDirect(var5);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var6).glVertexAttribPointer_buffer[var0] = var5;
      }

      nglVertexAttribPointer(var0, var1, var2 ? 5121 : 5120, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glVertexAttribPointer(int var0, int var1, boolean var2, boolean var3, int var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glVertexAttribPointer;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureArrayVBOdisabled(var6);
      BufferChecks.checkDirect(var5);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var6).glVertexAttribPointer_buffer[var0] = var5;
      }

      nglVertexAttribPointer(var0, var1, var2 ? 5125 : 5124, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glVertexAttribPointer(int var0, int var1, boolean var2, boolean var3, int var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glVertexAttribPointer;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureArrayVBOdisabled(var6);
      BufferChecks.checkDirect(var5);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var6).glVertexAttribPointer_buffer[var0] = var5;
      }

      nglVertexAttribPointer(var0, var1, var2 ? 5123 : 5122, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   static native void nglVertexAttribPointer(int var0, int var1, int var2, boolean var3, int var4, long var5, long var7);

   public static void glVertexAttribPointer(int var0, int var1, int var2, boolean var3, int var4, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glVertexAttribPointer;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureArrayVBOenabled(var7);
      nglVertexAttribPointerBO(var0, var1, var2, var3, var4, var5, var8);
   }

   static native void nglVertexAttribPointerBO(int var0, int var1, int var2, boolean var3, int var4, long var5, long var7);

   public static void glVertexAttribPointer(int var0, int var1, int var2, boolean var3, int var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glVertexAttribPointer;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureArrayVBOdisabled(var6);
      BufferChecks.checkDirect(var5);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var6).glVertexAttribPointer_buffer[var0] = var5;
      }

      nglVertexAttribPointer(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glEnableVertexAttribArray(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glEnableVertexAttribArray;
      BufferChecks.checkFunctionAddress(var2);
      nglEnableVertexAttribArray(var0, var2);
   }

   static native void nglEnableVertexAttribArray(int var0, long var1);

   public static void glDisableVertexAttribArray(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDisableVertexAttribArray;
      BufferChecks.checkFunctionAddress(var2);
      nglDisableVertexAttribArray(var0, var2);
   }

   static native void nglDisableVertexAttribArray(int var0, long var1);

   public static void glGetVertexAttrib(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetVertexAttribfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribfv(int var0, int var1, long var2, long var4);

   public static void glGetVertexAttrib(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribdv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((DoubleBuffer)var2, 4);
      nglGetVertexAttribdv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribdv(int var0, int var1, long var2, long var4);

   public static void glGetVertexAttrib(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetVertexAttribiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribiv(int var0, int var1, long var2, long var4);

   public static ByteBuffer glGetVertexAttribPointer(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetVertexAttribPointerv;
      BufferChecks.checkFunctionAddress(var5);
      ByteBuffer var7 = nglGetVertexAttribPointerv(var0, var1, var2, var5);
      return LWJGLUtil.CHECKS && var7 == null ? null : var7.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglGetVertexAttribPointerv(int var0, int var1, long var2, long var4);

   public static void glGetVertexAttribPointer(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribPointerv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer(var2, PointerBuffer.getPointerSize());
      nglGetVertexAttribPointerv2(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribPointerv2(int var0, int var1, long var2, long var4);

   public static void glBindAttribLocation(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBindAttribLocation;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2);
      nglBindAttribLocation(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglBindAttribLocation(int var0, int var1, long var2, long var4);

   public static void glBindAttribLocation(int var0, int var1, CharSequence var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBindAttribLocation;
      BufferChecks.checkFunctionAddress(var4);
      nglBindAttribLocation(var0, var1, APIUtil.getBufferNT(var3, var2), var4);
   }

   public static void glGetActiveAttrib(int var0, int var1, IntBuffer var2, IntBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetActiveAttrib;
      BufferChecks.checkFunctionAddress(var7);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      BufferChecks.checkBuffer((IntBuffer)var4, 1);
      BufferChecks.checkDirect(var5);
      nglGetActiveAttrib(var0, var1, var5.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   static native void nglGetActiveAttrib(int var0, int var1, int var2, long var3, long var5, long var7, long var9, long var11);

   public static String glGetActiveAttrib(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetActiveAttrib;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 2);
      IntBuffer var7 = APIUtil.getLengths(var4);
      ByteBuffer var8 = APIUtil.getBufferByte(var4, var2);
      nglGetActiveAttrib(var0, var1, var2, MemoryUtil.getAddress0((Buffer)var7), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var3, var3.position() + 1), MemoryUtil.getAddress(var8), var5);
      var8.limit(var7.get(0));
      return APIUtil.getString(var4, var8);
   }

   public static String glGetActiveAttrib(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetActiveAttrib;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getLengths(var3);
      ByteBuffer var7 = APIUtil.getBufferByte(var3, var2);
      nglGetActiveAttrib(var0, var1, var2, MemoryUtil.getAddress0((Buffer)var6), MemoryUtil.getAddress0((Buffer)APIUtil.getBufferInt(var3)), MemoryUtil.getAddress((IntBuffer)APIUtil.getBufferInt(var3), 1), MemoryUtil.getAddress(var7), var4);
      var7.limit(var6.get(0));
      return APIUtil.getString(var3, var7);
   }

   public static int glGetActiveAttribSize(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetActiveAttrib;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetActiveAttrib(var0, var1, 0, 0L, MemoryUtil.getAddress(var5), MemoryUtil.getAddress((IntBuffer)var5, 1), APIUtil.getBufferByte0(var2), var3);
      return var5.get(0);
   }

   public static int glGetActiveAttribType(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetActiveAttrib;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetActiveAttrib(var0, var1, 0, 0L, MemoryUtil.getAddress((IntBuffer)var5, 1), MemoryUtil.getAddress(var5), APIUtil.getBufferByte0(var2), var3);
      return var5.get(0);
   }

   public static int glGetAttribLocation(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetAttribLocation;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkNullTerminated(var1);
      int var5 = nglGetAttribLocation(var0, MemoryUtil.getAddress(var1), var3);
      return var5;
   }

   static native int nglGetAttribLocation(int var0, long var1, long var3);

   public static int glGetAttribLocation(int var0, CharSequence var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetAttribLocation;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nglGetAttribLocation(var0, APIUtil.getBufferNT(var2, var1), var3);
      return var5;
   }

   public static void glDrawBuffers(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDrawBuffers;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDrawBuffers(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDrawBuffers(int var0, long var1, long var3);

   public static void glDrawBuffers(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDrawBuffers;
      BufferChecks.checkFunctionAddress(var2);
      nglDrawBuffers(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glStencilOpSeparate(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glStencilOpSeparate;
      BufferChecks.checkFunctionAddress(var5);
      nglStencilOpSeparate(var0, var1, var2, var3, var5);
   }

   static native void nglStencilOpSeparate(int var0, int var1, int var2, int var3, long var4);

   public static void glStencilFuncSeparate(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glStencilFuncSeparate;
      BufferChecks.checkFunctionAddress(var5);
      nglStencilFuncSeparate(var0, var1, var2, var3, var5);
   }

   static native void nglStencilFuncSeparate(int var0, int var1, int var2, int var3, long var4);

   public static void glStencilMaskSeparate(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glStencilMaskSeparate;
      BufferChecks.checkFunctionAddress(var3);
      nglStencilMaskSeparate(var0, var1, var3);
   }

   static native void nglStencilMaskSeparate(int var0, int var1, long var2);

   public static void glBlendEquationSeparate(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBlendEquationSeparate;
      BufferChecks.checkFunctionAddress(var3);
      nglBlendEquationSeparate(var0, var1, var3);
   }

   static native void nglBlendEquationSeparate(int var0, int var1, long var2);
}
