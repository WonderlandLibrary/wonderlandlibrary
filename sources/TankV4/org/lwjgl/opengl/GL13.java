package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class GL13 {
   public static final int GL_TEXTURE0 = 33984;
   public static final int GL_TEXTURE1 = 33985;
   public static final int GL_TEXTURE2 = 33986;
   public static final int GL_TEXTURE3 = 33987;
   public static final int GL_TEXTURE4 = 33988;
   public static final int GL_TEXTURE5 = 33989;
   public static final int GL_TEXTURE6 = 33990;
   public static final int GL_TEXTURE7 = 33991;
   public static final int GL_TEXTURE8 = 33992;
   public static final int GL_TEXTURE9 = 33993;
   public static final int GL_TEXTURE10 = 33994;
   public static final int GL_TEXTURE11 = 33995;
   public static final int GL_TEXTURE12 = 33996;
   public static final int GL_TEXTURE13 = 33997;
   public static final int GL_TEXTURE14 = 33998;
   public static final int GL_TEXTURE15 = 33999;
   public static final int GL_TEXTURE16 = 34000;
   public static final int GL_TEXTURE17 = 34001;
   public static final int GL_TEXTURE18 = 34002;
   public static final int GL_TEXTURE19 = 34003;
   public static final int GL_TEXTURE20 = 34004;
   public static final int GL_TEXTURE21 = 34005;
   public static final int GL_TEXTURE22 = 34006;
   public static final int GL_TEXTURE23 = 34007;
   public static final int GL_TEXTURE24 = 34008;
   public static final int GL_TEXTURE25 = 34009;
   public static final int GL_TEXTURE26 = 34010;
   public static final int GL_TEXTURE27 = 34011;
   public static final int GL_TEXTURE28 = 34012;
   public static final int GL_TEXTURE29 = 34013;
   public static final int GL_TEXTURE30 = 34014;
   public static final int GL_TEXTURE31 = 34015;
   public static final int GL_ACTIVE_TEXTURE = 34016;
   public static final int GL_CLIENT_ACTIVE_TEXTURE = 34017;
   public static final int GL_MAX_TEXTURE_UNITS = 34018;
   public static final int GL_NORMAL_MAP = 34065;
   public static final int GL_REFLECTION_MAP = 34066;
   public static final int GL_TEXTURE_CUBE_MAP = 34067;
   public static final int GL_TEXTURE_BINDING_CUBE_MAP = 34068;
   public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_X = 34069;
   public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_X = 34070;
   public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Y = 34071;
   public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y = 34072;
   public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Z = 34073;
   public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z = 34074;
   public static final int GL_PROXY_TEXTURE_CUBE_MAP = 34075;
   public static final int GL_MAX_CUBE_MAP_TEXTURE_SIZE = 34076;
   public static final int GL_COMPRESSED_ALPHA = 34025;
   public static final int GL_COMPRESSED_LUMINANCE = 34026;
   public static final int GL_COMPRESSED_LUMINANCE_ALPHA = 34027;
   public static final int GL_COMPRESSED_INTENSITY = 34028;
   public static final int GL_COMPRESSED_RGB = 34029;
   public static final int GL_COMPRESSED_RGBA = 34030;
   public static final int GL_TEXTURE_COMPRESSION_HINT = 34031;
   public static final int GL_TEXTURE_COMPRESSED_IMAGE_SIZE = 34464;
   public static final int GL_TEXTURE_COMPRESSED = 34465;
   public static final int GL_NUM_COMPRESSED_TEXTURE_FORMATS = 34466;
   public static final int GL_COMPRESSED_TEXTURE_FORMATS = 34467;
   public static final int GL_MULTISAMPLE = 32925;
   public static final int GL_SAMPLE_ALPHA_TO_COVERAGE = 32926;
   public static final int GL_SAMPLE_ALPHA_TO_ONE = 32927;
   public static final int GL_SAMPLE_COVERAGE = 32928;
   public static final int GL_SAMPLE_BUFFERS = 32936;
   public static final int GL_SAMPLES = 32937;
   public static final int GL_SAMPLE_COVERAGE_VALUE = 32938;
   public static final int GL_SAMPLE_COVERAGE_INVERT = 32939;
   public static final int GL_MULTISAMPLE_BIT = 536870912;
   public static final int GL_TRANSPOSE_MODELVIEW_MATRIX = 34019;
   public static final int GL_TRANSPOSE_PROJECTION_MATRIX = 34020;
   public static final int GL_TRANSPOSE_TEXTURE_MATRIX = 34021;
   public static final int GL_TRANSPOSE_COLOR_MATRIX = 34022;
   public static final int GL_COMBINE = 34160;
   public static final int GL_COMBINE_RGB = 34161;
   public static final int GL_COMBINE_ALPHA = 34162;
   public static final int GL_SOURCE0_RGB = 34176;
   public static final int GL_SOURCE1_RGB = 34177;
   public static final int GL_SOURCE2_RGB = 34178;
   public static final int GL_SOURCE0_ALPHA = 34184;
   public static final int GL_SOURCE1_ALPHA = 34185;
   public static final int GL_SOURCE2_ALPHA = 34186;
   public static final int GL_OPERAND0_RGB = 34192;
   public static final int GL_OPERAND1_RGB = 34193;
   public static final int GL_OPERAND2_RGB = 34194;
   public static final int GL_OPERAND0_ALPHA = 34200;
   public static final int GL_OPERAND1_ALPHA = 34201;
   public static final int GL_OPERAND2_ALPHA = 34202;
   public static final int GL_RGB_SCALE = 34163;
   public static final int GL_ADD_SIGNED = 34164;
   public static final int GL_INTERPOLATE = 34165;
   public static final int GL_SUBTRACT = 34023;
   public static final int GL_CONSTANT = 34166;
   public static final int GL_PRIMARY_COLOR = 34167;
   public static final int GL_PREVIOUS = 34168;
   public static final int GL_DOT3_RGB = 34478;
   public static final int GL_DOT3_RGBA = 34479;
   public static final int GL_CLAMP_TO_BORDER = 33069;

   private GL13() {
   }

   public static void glActiveTexture(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glActiveTexture;
      BufferChecks.checkFunctionAddress(var2);
      nglActiveTexture(var0, var2);
   }

   static native void nglActiveTexture(int var0, long var1);

   public static void glClientActiveTexture(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glClientActiveTexture;
      BufferChecks.checkFunctionAddress(var2);
      StateTracker.getReferences(var1).glClientActiveTexture = var0 - '蓀';
      nglClientActiveTexture(var0, var2);
   }

   static native void nglClientActiveTexture(int var0, long var1);

   public static void glCompressedTexImage1D(int var0, int var1, int var2, int var3, int var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glCompressedTexImage1D;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureUnpackPBOdisabled(var6);
      BufferChecks.checkDirect(var5);
      nglCompressedTexImage1D(var0, var1, var2, var3, var4, var5.remaining(), MemoryUtil.getAddress(var5), var7);
   }

   static native void nglCompressedTexImage1D(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8);

   public static void glCompressedTexImage1D(int var0, int var1, int var2, int var3, int var4, int var5, long var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glCompressedTexImage1D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOenabled(var8);
      nglCompressedTexImage1DBO(var0, var1, var2, var3, var4, var5, var6, var9);
   }

   static native void nglCompressedTexImage1DBO(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8);

   public static void glCompressedTexImage2D(int var0, int var1, int var2, int var3, int var4, int var5, ByteBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glCompressedTexImage2D;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOdisabled(var7);
      BufferChecks.checkDirect(var6);
      nglCompressedTexImage2D(var0, var1, var2, var3, var4, var5, var6.remaining(), MemoryUtil.getAddress(var6), var8);
   }

   static native void nglCompressedTexImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glCompressedTexImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glCompressedTexImage2D;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOenabled(var9);
      nglCompressedTexImage2DBO(var0, var1, var2, var3, var4, var5, var6, var7, var10);
   }

   static native void nglCompressedTexImage2DBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glCompressedTexImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, ByteBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glCompressedTexImage3D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var7);
      nglCompressedTexImage3D(var0, var1, var2, var3, var4, var5, var6, var7.remaining(), MemoryUtil.getAddress(var7), var9);
   }

   static native void nglCompressedTexImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glCompressedTexImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glCompressedTexImage3D;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOenabled(var10);
      nglCompressedTexImage3DBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var11);
   }

   static native void nglCompressedTexImage3DBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glCompressedTexSubImage1D(int var0, int var1, int var2, int var3, int var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glCompressedTexSubImage1D;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureUnpackPBOdisabled(var6);
      BufferChecks.checkDirect(var5);
      nglCompressedTexSubImage1D(var0, var1, var2, var3, var4, var5.remaining(), MemoryUtil.getAddress(var5), var7);
   }

   static native void nglCompressedTexSubImage1D(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8);

   public static void glCompressedTexSubImage1D(int var0, int var1, int var2, int var3, int var4, int var5, long var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glCompressedTexSubImage1D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOenabled(var8);
      nglCompressedTexSubImage1DBO(var0, var1, var2, var3, var4, var5, var6, var9);
   }

   static native void nglCompressedTexSubImage1DBO(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8);

   public static void glCompressedTexSubImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, ByteBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glCompressedTexSubImage2D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var7);
      nglCompressedTexSubImage2D(var0, var1, var2, var3, var4, var5, var6, var7.remaining(), MemoryUtil.getAddress(var7), var9);
   }

   static native void nglCompressedTexSubImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glCompressedTexSubImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glCompressedTexSubImage2D;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOenabled(var10);
      nglCompressedTexSubImage2DBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var11);
   }

   static native void nglCompressedTexSubImage2DBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glCompressedTexSubImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, ByteBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glCompressedTexSubImage3D;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      BufferChecks.checkDirect(var9);
      nglCompressedTexSubImage3D(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9.remaining(), MemoryUtil.getAddress(var9), var11);
   }

   static native void nglCompressedTexSubImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10, long var12);

   public static void glCompressedTexSubImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glCompressedTexSubImage3D;
      BufferChecks.checkFunctionAddress(var13);
      GLChecks.ensureUnpackPBOenabled(var12);
      nglCompressedTexSubImage3DBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var13);
   }

   static native void nglCompressedTexSubImage3DBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10, long var12);

   public static void glGetCompressedTexImage(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetCompressedTexImage;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensurePackPBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      nglGetCompressedTexImage(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glGetCompressedTexImage(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetCompressedTexImage;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensurePackPBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      nglGetCompressedTexImage(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glGetCompressedTexImage(int var0, int var1, ShortBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetCompressedTexImage;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensurePackPBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      nglGetCompressedTexImage(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetCompressedTexImage(int var0, int var1, long var2, long var4);

   public static void glGetCompressedTexImage(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetCompressedTexImage;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOenabled(var4);
      nglGetCompressedTexImageBO(var0, var1, var2, var5);
   }

   static native void nglGetCompressedTexImageBO(int var0, int var1, long var2, long var4);

   public static void glMultiTexCoord1f(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMultiTexCoord1f;
      BufferChecks.checkFunctionAddress(var3);
      nglMultiTexCoord1f(var0, var1, var3);
   }

   static native void nglMultiTexCoord1f(int var0, float var1, long var2);

   public static void glMultiTexCoord1d(int var0, double var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiTexCoord1d;
      BufferChecks.checkFunctionAddress(var4);
      nglMultiTexCoord1d(var0, var1, var4);
   }

   static native void nglMultiTexCoord1d(int var0, double var1, long var3);

   public static void glMultiTexCoord2f(int var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiTexCoord2f;
      BufferChecks.checkFunctionAddress(var4);
      nglMultiTexCoord2f(var0, var1, var2, var4);
   }

   static native void nglMultiTexCoord2f(int var0, float var1, float var2, long var3);

   public static void glMultiTexCoord2d(int var0, double var1, double var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMultiTexCoord2d;
      BufferChecks.checkFunctionAddress(var6);
      nglMultiTexCoord2d(var0, var1, var3, var6);
   }

   static native void nglMultiTexCoord2d(int var0, double var1, double var3, long var5);

   public static void glMultiTexCoord3f(int var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexCoord3f;
      BufferChecks.checkFunctionAddress(var5);
      nglMultiTexCoord3f(var0, var1, var2, var3, var5);
   }

   static native void nglMultiTexCoord3f(int var0, float var1, float var2, float var3, long var4);

   public static void glMultiTexCoord3d(int var0, double var1, double var3, double var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glMultiTexCoord3d;
      BufferChecks.checkFunctionAddress(var8);
      nglMultiTexCoord3d(var0, var1, var3, var5, var8);
   }

   static native void nglMultiTexCoord3d(int var0, double var1, double var3, double var5, long var7);

   public static void glMultiTexCoord4f(int var0, float var1, float var2, float var3, float var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMultiTexCoord4f;
      BufferChecks.checkFunctionAddress(var6);
      nglMultiTexCoord4f(var0, var1, var2, var3, var4, var6);
   }

   static native void nglMultiTexCoord4f(int var0, float var1, float var2, float var3, float var4, long var5);

   public static void glMultiTexCoord4d(int var0, double var1, double var3, double var5, double var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glMultiTexCoord4d;
      BufferChecks.checkFunctionAddress(var10);
      nglMultiTexCoord4d(var0, var1, var3, var5, var7, var10);
   }

   static native void nglMultiTexCoord4d(int var0, double var1, double var3, double var5, double var7, long var9);

   public static void glLoadTransposeMatrix(FloatBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glLoadTransposeMatrixf;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkBuffer((FloatBuffer)var0, 16);
      nglLoadTransposeMatrixf(MemoryUtil.getAddress(var0), var2);
   }

   static native void nglLoadTransposeMatrixf(long var0, long var2);

   public static void glLoadTransposeMatrix(DoubleBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glLoadTransposeMatrixd;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkBuffer((DoubleBuffer)var0, 16);
      nglLoadTransposeMatrixd(MemoryUtil.getAddress(var0), var2);
   }

   static native void nglLoadTransposeMatrixd(long var0, long var2);

   public static void glMultTransposeMatrix(FloatBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glMultTransposeMatrixf;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkBuffer((FloatBuffer)var0, 16);
      nglMultTransposeMatrixf(MemoryUtil.getAddress(var0), var2);
   }

   static native void nglMultTransposeMatrixf(long var0, long var2);

   public static void glMultTransposeMatrix(DoubleBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glMultTransposeMatrixd;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkBuffer((DoubleBuffer)var0, 16);
      nglMultTransposeMatrixd(MemoryUtil.getAddress(var0), var2);
   }

   static native void nglMultTransposeMatrixd(long var0, long var2);

   public static void glSampleCoverage(float var0, boolean var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glSampleCoverage;
      BufferChecks.checkFunctionAddress(var3);
      nglSampleCoverage(var0, var1, var3);
   }

   static native void nglSampleCoverage(float var0, boolean var1, long var2);
}
