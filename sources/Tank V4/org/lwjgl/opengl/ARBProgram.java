package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public class ARBProgram {
   public static final int GL_PROGRAM_FORMAT_ASCII_ARB = 34933;
   public static final int GL_PROGRAM_LENGTH_ARB = 34343;
   public static final int GL_PROGRAM_FORMAT_ARB = 34934;
   public static final int GL_PROGRAM_BINDING_ARB = 34423;
   public static final int GL_PROGRAM_INSTRUCTIONS_ARB = 34976;
   public static final int GL_MAX_PROGRAM_INSTRUCTIONS_ARB = 34977;
   public static final int GL_PROGRAM_NATIVE_INSTRUCTIONS_ARB = 34978;
   public static final int GL_MAX_PROGRAM_NATIVE_INSTRUCTIONS_ARB = 34979;
   public static final int GL_PROGRAM_TEMPORARIES_ARB = 34980;
   public static final int GL_MAX_PROGRAM_TEMPORARIES_ARB = 34981;
   public static final int GL_PROGRAM_NATIVE_TEMPORARIES_ARB = 34982;
   public static final int GL_MAX_PROGRAM_NATIVE_TEMPORARIES_ARB = 34983;
   public static final int GL_PROGRAM_PARAMETERS_ARB = 34984;
   public static final int GL_MAX_PROGRAM_PARAMETERS_ARB = 34985;
   public static final int GL_PROGRAM_NATIVE_PARAMETERS_ARB = 34986;
   public static final int GL_MAX_PROGRAM_NATIVE_PARAMETERS_ARB = 34987;
   public static final int GL_PROGRAM_ATTRIBS_ARB = 34988;
   public static final int GL_MAX_PROGRAM_ATTRIBS_ARB = 34989;
   public static final int GL_PROGRAM_NATIVE_ATTRIBS_ARB = 34990;
   public static final int GL_MAX_PROGRAM_NATIVE_ATTRIBS_ARB = 34991;
   public static final int GL_MAX_PROGRAM_LOCAL_PARAMETERS_ARB = 34996;
   public static final int GL_MAX_PROGRAM_ENV_PARAMETERS_ARB = 34997;
   public static final int GL_PROGRAM_UNDER_NATIVE_LIMITS_ARB = 34998;
   public static final int GL_PROGRAM_STRING_ARB = 34344;
   public static final int GL_PROGRAM_ERROR_POSITION_ARB = 34379;
   public static final int GL_CURRENT_MATRIX_ARB = 34369;
   public static final int GL_TRANSPOSE_CURRENT_MATRIX_ARB = 34999;
   public static final int GL_CURRENT_MATRIX_STACK_DEPTH_ARB = 34368;
   public static final int GL_MAX_PROGRAM_MATRICES_ARB = 34351;
   public static final int GL_MAX_PROGRAM_MATRIX_STACK_DEPTH_ARB = 34350;
   public static final int GL_PROGRAM_ERROR_STRING_ARB = 34932;
   public static final int GL_MATRIX0_ARB = 35008;
   public static final int GL_MATRIX1_ARB = 35009;
   public static final int GL_MATRIX2_ARB = 35010;
   public static final int GL_MATRIX3_ARB = 35011;
   public static final int GL_MATRIX4_ARB = 35012;
   public static final int GL_MATRIX5_ARB = 35013;
   public static final int GL_MATRIX6_ARB = 35014;
   public static final int GL_MATRIX7_ARB = 35015;
   public static final int GL_MATRIX8_ARB = 35016;
   public static final int GL_MATRIX9_ARB = 35017;
   public static final int GL_MATRIX10_ARB = 35018;
   public static final int GL_MATRIX11_ARB = 35019;
   public static final int GL_MATRIX12_ARB = 35020;
   public static final int GL_MATRIX13_ARB = 35021;
   public static final int GL_MATRIX14_ARB = 35022;
   public static final int GL_MATRIX15_ARB = 35023;
   public static final int GL_MATRIX16_ARB = 35024;
   public static final int GL_MATRIX17_ARB = 35025;
   public static final int GL_MATRIX18_ARB = 35026;
   public static final int GL_MATRIX19_ARB = 35027;
   public static final int GL_MATRIX20_ARB = 35028;
   public static final int GL_MATRIX21_ARB = 35029;
   public static final int GL_MATRIX22_ARB = 35030;
   public static final int GL_MATRIX23_ARB = 35031;
   public static final int GL_MATRIX24_ARB = 35032;
   public static final int GL_MATRIX25_ARB = 35033;
   public static final int GL_MATRIX26_ARB = 35034;
   public static final int GL_MATRIX27_ARB = 35035;
   public static final int GL_MATRIX28_ARB = 35036;
   public static final int GL_MATRIX29_ARB = 35037;
   public static final int GL_MATRIX30_ARB = 35038;
   public static final int GL_MATRIX31_ARB = 35039;

   public static void glProgramStringARB(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramStringARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramStringARB(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramStringARB(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramStringARB(int var0, int var1, CharSequence var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramStringARB;
      BufferChecks.checkFunctionAddress(var4);
      nglProgramStringARB(var0, var1, var2.length(), APIUtil.getBuffer(var3, var2), var4);
   }

   public static void glBindProgramARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBindProgramARB;
      BufferChecks.checkFunctionAddress(var3);
      nglBindProgramARB(var0, var1, var3);
   }

   static native void nglBindProgramARB(int var0, int var1, long var2);

   public static void glDeleteProgramsARB(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteProgramsARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteProgramsARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteProgramsARB(int var0, long var1, long var3);

   public static void glDeleteProgramsARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteProgramsARB;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteProgramsARB(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glGenProgramsARB(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenProgramsARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenProgramsARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenProgramsARB(int var0, long var1, long var3);

   public static int glGenProgramsARB() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenProgramsARB;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenProgramsARB(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static void glProgramEnvParameter4fARB(int var0, int var1, float var2, float var3, float var4, float var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramEnvParameter4fARB;
      BufferChecks.checkFunctionAddress(var7);
      nglProgramEnvParameter4fARB(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglProgramEnvParameter4fARB(int var0, int var1, float var2, float var3, float var4, float var5, long var6);

   public static void glProgramEnvParameter4dARB(int var0, int var1, double var2, double var4, double var6, double var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glProgramEnvParameter4dARB;
      BufferChecks.checkFunctionAddress(var11);
      nglProgramEnvParameter4dARB(var0, var1, var2, var4, var6, var8, var11);
   }

   static native void nglProgramEnvParameter4dARB(int var0, int var1, double var2, double var4, double var6, double var8, long var10);

   public static void glProgramEnvParameter4ARB(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramEnvParameter4fvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglProgramEnvParameter4fvARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramEnvParameter4fvARB(int var0, int var1, long var2, long var4);

   public static void glProgramEnvParameter4ARB(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramEnvParameter4dvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((DoubleBuffer)var2, 4);
      nglProgramEnvParameter4dvARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramEnvParameter4dvARB(int var0, int var1, long var2, long var4);

   public static void glProgramLocalParameter4fARB(int var0, int var1, float var2, float var3, float var4, float var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramLocalParameter4fARB;
      BufferChecks.checkFunctionAddress(var7);
      nglProgramLocalParameter4fARB(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglProgramLocalParameter4fARB(int var0, int var1, float var2, float var3, float var4, float var5, long var6);

   public static void glProgramLocalParameter4dARB(int var0, int var1, double var2, double var4, double var6, double var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glProgramLocalParameter4dARB;
      BufferChecks.checkFunctionAddress(var11);
      nglProgramLocalParameter4dARB(var0, var1, var2, var4, var6, var8, var11);
   }

   static native void nglProgramLocalParameter4dARB(int var0, int var1, double var2, double var4, double var6, double var8, long var10);

   public static void glProgramLocalParameter4ARB(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramLocalParameter4fvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglProgramLocalParameter4fvARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramLocalParameter4fvARB(int var0, int var1, long var2, long var4);

   public static void glProgramLocalParameter4ARB(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramLocalParameter4dvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((DoubleBuffer)var2, 4);
      nglProgramLocalParameter4dvARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramLocalParameter4dvARB(int var0, int var1, long var2, long var4);

   public static void glGetProgramEnvParameterARB(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramEnvParameterfvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetProgramEnvParameterfvARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramEnvParameterfvARB(int var0, int var1, long var2, long var4);

   public static void glGetProgramEnvParameterARB(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramEnvParameterdvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((DoubleBuffer)var2, 4);
      nglGetProgramEnvParameterdvARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramEnvParameterdvARB(int var0, int var1, long var2, long var4);

   public static void glGetProgramLocalParameterARB(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramLocalParameterfvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetProgramLocalParameterfvARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramLocalParameterfvARB(int var0, int var1, long var2, long var4);

   public static void glGetProgramLocalParameterARB(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramLocalParameterdvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((DoubleBuffer)var2, 4);
      nglGetProgramLocalParameterdvARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramLocalParameterdvARB(int var0, int var1, long var2, long var4);

   public static void glGetProgramARB(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramivARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetProgramivARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramivARB(int var0, int var1, long var2, long var4);

   /** @deprecated */
   @Deprecated
   public static int glGetProgramARB(int var0, int var1) {
      return glGetProgramiARB(var0, var1);
   }

   public static int glGetProgramiARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetProgramivARB;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetProgramivARB(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetProgramStringARB(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramStringARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetProgramStringARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramStringARB(int var0, int var1, long var2, long var4);

   public static String glGetProgramStringARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetProgramStringARB;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = glGetProgramiARB(var0, 34343);
      ByteBuffer var6 = APIUtil.getBufferByte(var2, var5);
      nglGetProgramStringARB(var0, var1, MemoryUtil.getAddress(var6), var3);
      var6.limit(var5);
      return APIUtil.getString(var2, var6);
   }

   public static boolean glIsProgramARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsProgramARB;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsProgramARB(var0, var2);
      return var4;
   }

   static native boolean nglIsProgramARB(int var0, long var1);
}
