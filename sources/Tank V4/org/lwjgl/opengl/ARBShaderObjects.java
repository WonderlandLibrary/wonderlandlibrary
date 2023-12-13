package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBShaderObjects {
   public static final int GL_PROGRAM_OBJECT_ARB = 35648;
   public static final int GL_OBJECT_TYPE_ARB = 35662;
   public static final int GL_OBJECT_SUBTYPE_ARB = 35663;
   public static final int GL_OBJECT_DELETE_STATUS_ARB = 35712;
   public static final int GL_OBJECT_COMPILE_STATUS_ARB = 35713;
   public static final int GL_OBJECT_LINK_STATUS_ARB = 35714;
   public static final int GL_OBJECT_VALIDATE_STATUS_ARB = 35715;
   public static final int GL_OBJECT_INFO_LOG_LENGTH_ARB = 35716;
   public static final int GL_OBJECT_ATTACHED_OBJECTS_ARB = 35717;
   public static final int GL_OBJECT_ACTIVE_UNIFORMS_ARB = 35718;
   public static final int GL_OBJECT_ACTIVE_UNIFORM_MAX_LENGTH_ARB = 35719;
   public static final int GL_OBJECT_SHADER_SOURCE_LENGTH_ARB = 35720;
   public static final int GL_SHADER_OBJECT_ARB = 35656;
   public static final int GL_FLOAT_VEC2_ARB = 35664;
   public static final int GL_FLOAT_VEC3_ARB = 35665;
   public static final int GL_FLOAT_VEC4_ARB = 35666;
   public static final int GL_INT_VEC2_ARB = 35667;
   public static final int GL_INT_VEC3_ARB = 35668;
   public static final int GL_INT_VEC4_ARB = 35669;
   public static final int GL_BOOL_ARB = 35670;
   public static final int GL_BOOL_VEC2_ARB = 35671;
   public static final int GL_BOOL_VEC3_ARB = 35672;
   public static final int GL_BOOL_VEC4_ARB = 35673;
   public static final int GL_FLOAT_MAT2_ARB = 35674;
   public static final int GL_FLOAT_MAT3_ARB = 35675;
   public static final int GL_FLOAT_MAT4_ARB = 35676;
   public static final int GL_SAMPLER_1D_ARB = 35677;
   public static final int GL_SAMPLER_2D_ARB = 35678;
   public static final int GL_SAMPLER_3D_ARB = 35679;
   public static final int GL_SAMPLER_CUBE_ARB = 35680;
   public static final int GL_SAMPLER_1D_SHADOW_ARB = 35681;
   public static final int GL_SAMPLER_2D_SHADOW_ARB = 35682;
   public static final int GL_SAMPLER_2D_RECT_ARB = 35683;
   public static final int GL_SAMPLER_2D_RECT_SHADOW_ARB = 35684;

   private ARBShaderObjects() {
   }

   public static void glDeleteObjectARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteObjectARB;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteObjectARB(var0, var2);
   }

   static native void nglDeleteObjectARB(int var0, long var1);

   public static int glGetHandleARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGetHandleARB;
      BufferChecks.checkFunctionAddress(var2);
      int var4 = nglGetHandleARB(var0, var2);
      return var4;
   }

   static native int nglGetHandleARB(int var0, long var1);

   public static void glDetachObjectARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDetachObjectARB;
      BufferChecks.checkFunctionAddress(var3);
      nglDetachObjectARB(var0, var1, var3);
   }

   static native void nglDetachObjectARB(int var0, int var1, long var2);

   public static int glCreateShaderObjectARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glCreateShaderObjectARB;
      BufferChecks.checkFunctionAddress(var2);
      int var4 = nglCreateShaderObjectARB(var0, var2);
      return var4;
   }

   static native int nglCreateShaderObjectARB(int var0, long var1);

   public static void glShaderSourceARB(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glShaderSourceARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglShaderSourceARB(var0, 1, MemoryUtil.getAddress(var1), var1.remaining(), var3);
   }

   static native void nglShaderSourceARB(int var0, int var1, long var2, int var4, long var5);

   public static void glShaderSourceARB(int var0, CharSequence var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glShaderSourceARB;
      BufferChecks.checkFunctionAddress(var3);
      nglShaderSourceARB(var0, 1, APIUtil.getBuffer(var2, var1), var1.length(), var3);
   }

   public static void glShaderSourceARB(int var0, CharSequence[] var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glShaderSourceARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkArray(var1);
      nglShaderSourceARB3(var0, var1.length, APIUtil.getBuffer(var2, var1), APIUtil.getLengths(var2, var1), var3);
   }

   static native void nglShaderSourceARB3(int var0, int var1, long var2, long var4, long var6);

   public static void glCompileShaderARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glCompileShaderARB;
      BufferChecks.checkFunctionAddress(var2);
      nglCompileShaderARB(var0, var2);
   }

   static native void nglCompileShaderARB(int var0, long var1);

   public static int glCreateProgramObjectARB() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glCreateProgramObjectARB;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nglCreateProgramObjectARB(var1);
      return var3;
   }

   static native int nglCreateProgramObjectARB(long var0);

   public static void glAttachObjectARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glAttachObjectARB;
      BufferChecks.checkFunctionAddress(var3);
      nglAttachObjectARB(var0, var1, var3);
   }

   static native void nglAttachObjectARB(int var0, int var1, long var2);

   public static void glLinkProgramARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glLinkProgramARB;
      BufferChecks.checkFunctionAddress(var2);
      nglLinkProgramARB(var0, var2);
   }

   static native void nglLinkProgramARB(int var0, long var1);

   public static void glUseProgramObjectARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glUseProgramObjectARB;
      BufferChecks.checkFunctionAddress(var2);
      nglUseProgramObjectARB(var0, var2);
   }

   static native void nglUseProgramObjectARB(int var0, long var1);

   public static void glValidateProgramARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glValidateProgramARB;
      BufferChecks.checkFunctionAddress(var2);
      nglValidateProgramARB(var0, var2);
   }

   static native void nglValidateProgramARB(int var0, long var1);

   public static void glUniform1fARB(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform1fARB;
      BufferChecks.checkFunctionAddress(var3);
      nglUniform1fARB(var0, var1, var3);
   }

   static native void nglUniform1fARB(int var0, float var1, long var2);

   public static void glUniform2fARB(int var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniform2fARB;
      BufferChecks.checkFunctionAddress(var4);
      nglUniform2fARB(var0, var1, var2, var4);
   }

   static native void nglUniform2fARB(int var0, float var1, float var2, long var3);

   public static void glUniform3fARB(int var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glUniform3fARB;
      BufferChecks.checkFunctionAddress(var5);
      nglUniform3fARB(var0, var1, var2, var3, var5);
   }

   static native void nglUniform3fARB(int var0, float var1, float var2, float var3, long var4);

   public static void glUniform4fARB(int var0, float var1, float var2, float var3, float var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glUniform4fARB;
      BufferChecks.checkFunctionAddress(var6);
      nglUniform4fARB(var0, var1, var2, var3, var4, var6);
   }

   static native void nglUniform4fARB(int var0, float var1, float var2, float var3, float var4, long var5);

   public static void glUniform1iARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform1iARB;
      BufferChecks.checkFunctionAddress(var3);
      nglUniform1iARB(var0, var1, var3);
   }

   static native void nglUniform1iARB(int var0, int var1, long var2);

   public static void glUniform2iARB(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniform2iARB;
      BufferChecks.checkFunctionAddress(var4);
      nglUniform2iARB(var0, var1, var2, var4);
   }

   static native void nglUniform2iARB(int var0, int var1, int var2, long var3);

   public static void glUniform3iARB(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glUniform3iARB;
      BufferChecks.checkFunctionAddress(var5);
      nglUniform3iARB(var0, var1, var2, var3, var5);
   }

   static native void nglUniform3iARB(int var0, int var1, int var2, int var3, long var4);

   public static void glUniform4iARB(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glUniform4iARB;
      BufferChecks.checkFunctionAddress(var6);
      nglUniform4iARB(var0, var1, var2, var3, var4, var6);
   }

   static native void nglUniform4iARB(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glUniform1ARB(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform1fvARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform1fvARB(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform1fvARB(int var0, int var1, long var2, long var4);

   public static void glUniform2ARB(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform2fvARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform2fvARB(var0, var1.remaining() >> 1, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform2fvARB(int var0, int var1, long var2, long var4);

   public static void glUniform3ARB(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform3fvARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform3fvARB(var0, var1.remaining() / 3, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform3fvARB(int var0, int var1, long var2, long var4);

   public static void glUniform4ARB(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform4fvARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform4fvARB(var0, var1.remaining() >> 2, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform4fvARB(int var0, int var1, long var2, long var4);

   public static void glUniform1ARB(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform1ivARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform1ivARB(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform1ivARB(int var0, int var1, long var2, long var4);

   public static void glUniform2ARB(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform2ivARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform2ivARB(var0, var1.remaining() >> 1, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform2ivARB(int var0, int var1, long var2, long var4);

   public static void glUniform3ARB(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform3ivARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform3ivARB(var0, var1.remaining() / 3, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform3ivARB(int var0, int var1, long var2, long var4);

   public static void glUniform4ARB(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform4ivARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform4ivARB(var0, var1.remaining() >> 2, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform4ivARB(int var0, int var1, long var2, long var4);

   public static void glUniformMatrix2ARB(int var0, boolean var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformMatrix2fvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglUniformMatrix2fvARB(var0, var2.remaining() >> 2, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglUniformMatrix2fvARB(int var0, int var1, boolean var2, long var3, long var5);

   public static void glUniformMatrix3ARB(int var0, boolean var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformMatrix3fvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglUniformMatrix3fvARB(var0, var2.remaining() / 9, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglUniformMatrix3fvARB(int var0, int var1, boolean var2, long var3, long var5);

   public static void glUniformMatrix4ARB(int var0, boolean var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformMatrix4fvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglUniformMatrix4fvARB(var0, var2.remaining() >> 4, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglUniformMatrix4fvARB(int var0, int var1, boolean var2, long var3, long var5);

   public static void glGetObjectParameterARB(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetObjectParameterfvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetObjectParameterfvARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetObjectParameterfvARB(int var0, int var1, long var2, long var4);

   public static float glGetObjectParameterfARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetObjectParameterfvARB;
      BufferChecks.checkFunctionAddress(var3);
      FloatBuffer var5 = APIUtil.getBufferFloat(var2);
      nglGetObjectParameterfvARB(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetObjectParameterARB(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetObjectParameterivARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetObjectParameterivARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetObjectParameterivARB(int var0, int var1, long var2, long var4);

   public static int glGetObjectParameteriARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetObjectParameterivARB;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetObjectParameterivARB(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetInfoLogARB(int var0, IntBuffer var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetInfoLogARB;
      BufferChecks.checkFunctionAddress(var4);
      if (var1 != null) {
         BufferChecks.checkBuffer((IntBuffer)var1, 1);
      }

      BufferChecks.checkDirect(var2);
      nglGetInfoLogARB(var0, var2.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetInfoLogARB(int var0, int var1, long var2, long var4, long var6);

   public static String glGetInfoLogARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetInfoLogARB;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getLengths(var2);
      ByteBuffer var6 = APIUtil.getBufferByte(var2, var1);
      nglGetInfoLogARB(var0, var1, MemoryUtil.getAddress0((Buffer)var5), MemoryUtil.getAddress(var6), var3);
      var6.limit(var5.get(0));
      return APIUtil.getString(var2, var6);
   }

   public static void glGetAttachedObjectsARB(int var0, IntBuffer var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetAttachedObjectsARB;
      BufferChecks.checkFunctionAddress(var4);
      if (var1 != null) {
         BufferChecks.checkBuffer((IntBuffer)var1, 1);
      }

      BufferChecks.checkDirect(var2);
      nglGetAttachedObjectsARB(var0, var2.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetAttachedObjectsARB(int var0, int var1, long var2, long var4, long var6);

   public static int glGetUniformLocationARB(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetUniformLocationARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkNullTerminated(var1);
      int var5 = nglGetUniformLocationARB(var0, MemoryUtil.getAddress(var1), var3);
      return var5;
   }

   static native int nglGetUniformLocationARB(int var0, long var1, long var3);

   public static int glGetUniformLocationARB(int var0, CharSequence var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetUniformLocationARB;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nglGetUniformLocationARB(var0, APIUtil.getBufferNT(var2, var1), var3);
      return var5;
   }

   public static void glGetActiveUniformARB(int var0, int var1, IntBuffer var2, IntBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetActiveUniformARB;
      BufferChecks.checkFunctionAddress(var7);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      BufferChecks.checkBuffer((IntBuffer)var4, 1);
      BufferChecks.checkDirect(var5);
      nglGetActiveUniformARB(var0, var1, var5.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   static native void nglGetActiveUniformARB(int var0, int var1, int var2, long var3, long var5, long var7, long var9, long var11);

   public static String glGetActiveUniformARB(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetActiveUniformARB;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 2);
      IntBuffer var7 = APIUtil.getLengths(var4);
      ByteBuffer var8 = APIUtil.getBufferByte(var4, var2);
      nglGetActiveUniformARB(var0, var1, var2, MemoryUtil.getAddress0((Buffer)var7), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var3, var3.position() + 1), MemoryUtil.getAddress(var8), var5);
      var8.limit(var7.get(0));
      return APIUtil.getString(var4, var8);
   }

   public static String glGetActiveUniformARB(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetActiveUniformARB;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getLengths(var3);
      ByteBuffer var7 = APIUtil.getBufferByte(var3, var2);
      nglGetActiveUniformARB(var0, var1, var2, MemoryUtil.getAddress0((Buffer)var6), MemoryUtil.getAddress0((Buffer)APIUtil.getBufferInt(var3)), MemoryUtil.getAddress((IntBuffer)APIUtil.getBufferInt(var3), 1), MemoryUtil.getAddress(var7), var4);
      var7.limit(var6.get(0));
      return APIUtil.getString(var3, var7);
   }

   public static int glGetActiveUniformSizeARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetActiveUniformARB;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetActiveUniformARB(var0, var1, 0, 0L, MemoryUtil.getAddress(var5), MemoryUtil.getAddress((IntBuffer)var5, 1), APIUtil.getBufferByte0(var2), var3);
      return var5.get(0);
   }

   public static int glGetActiveUniformTypeARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetActiveUniformARB;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetActiveUniformARB(var0, var1, 0, 0L, MemoryUtil.getAddress((IntBuffer)var5, 1), MemoryUtil.getAddress(var5), APIUtil.getBufferByte0(var2), var3);
      return var5.get(0);
   }

   public static void glGetUniformARB(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetUniformfvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetUniformfvARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetUniformfvARB(int var0, int var1, long var2, long var4);

   public static void glGetUniformARB(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetUniformivARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetUniformivARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetUniformivARB(int var0, int var1, long var2, long var4);

   public static void glGetShaderSourceARB(int var0, IntBuffer var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetShaderSourceARB;
      BufferChecks.checkFunctionAddress(var4);
      if (var1 != null) {
         BufferChecks.checkBuffer((IntBuffer)var1, 1);
      }

      BufferChecks.checkDirect(var2);
      nglGetShaderSourceARB(var0, var2.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetShaderSourceARB(int var0, int var1, long var2, long var4, long var6);

   public static String glGetShaderSourceARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetShaderSourceARB;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getLengths(var2);
      ByteBuffer var6 = APIUtil.getBufferByte(var2, var1);
      nglGetShaderSourceARB(var0, var1, MemoryUtil.getAddress0((Buffer)var5), MemoryUtil.getAddress(var6), var3);
      var6.limit(var5.get(0));
      return APIUtil.getString(var2, var6);
   }
}
