package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class GL33 {
   public static final int GL_SRC1_COLOR = 35065;
   public static final int GL_SRC1_ALPHA = 34185;
   public static final int GL_ONE_MINUS_SRC1_COLOR = 35066;
   public static final int GL_ONE_MINUS_SRC1_ALPHA = 35067;
   public static final int GL_MAX_DUAL_SOURCE_DRAW_BUFFERS = 35068;
   public static final int GL_ANY_SAMPLES_PASSED = 35887;
   public static final int GL_SAMPLER_BINDING = 35097;
   public static final int GL_RGB10_A2UI = 36975;
   public static final int GL_TEXTURE_SWIZZLE_R = 36418;
   public static final int GL_TEXTURE_SWIZZLE_G = 36419;
   public static final int GL_TEXTURE_SWIZZLE_B = 36420;
   public static final int GL_TEXTURE_SWIZZLE_A = 36421;
   public static final int GL_TEXTURE_SWIZZLE_RGBA = 36422;
   public static final int GL_TIME_ELAPSED = 35007;
   public static final int GL_TIMESTAMP = 36392;
   public static final int GL_VERTEX_ATTRIB_ARRAY_DIVISOR = 35070;
   public static final int GL_INT_2_10_10_10_REV = 36255;

   private GL33() {
   }

   public static void glBindFragDataLocationIndexed(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBindFragDataLocationIndexed;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkNullTerminated(var3);
      nglBindFragDataLocationIndexed(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglBindFragDataLocationIndexed(int var0, int var1, int var2, long var3, long var5);

   public static void glBindFragDataLocationIndexed(int var0, int var1, int var2, CharSequence var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBindFragDataLocationIndexed;
      BufferChecks.checkFunctionAddress(var5);
      nglBindFragDataLocationIndexed(var0, var1, var2, APIUtil.getBufferNT(var4, var3), var5);
   }

   public static int glGetFragDataIndex(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetFragDataIndex;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkNullTerminated(var1);
      int var5 = nglGetFragDataIndex(var0, MemoryUtil.getAddress(var1), var3);
      return var5;
   }

   static native int nglGetFragDataIndex(int var0, long var1, long var3);

   public static int glGetFragDataIndex(int var0, CharSequence var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetFragDataIndex;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nglGetFragDataIndex(var0, APIUtil.getBufferNT(var2, var1), var3);
      return var5;
   }

   public static void glGenSamplers(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenSamplers;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenSamplers(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenSamplers(int var0, long var1, long var3);

   public static int glGenSamplers() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenSamplers;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenSamplers(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static void glDeleteSamplers(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteSamplers;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteSamplers(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteSamplers(int var0, long var1, long var3);

   public static void glDeleteSamplers(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteSamplers;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteSamplers(1, APIUtil.getInt(var1, var0), var2);
   }

   public static boolean glIsSampler(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsSampler;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsSampler(var0, var2);
      return var4;
   }

   static native boolean nglIsSampler(int var0, long var1);

   public static void glBindSampler(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBindSampler;
      BufferChecks.checkFunctionAddress(var3);
      nglBindSampler(var0, var1, var3);
   }

   static native void nglBindSampler(int var0, int var1, long var2);

   public static void glSamplerParameteri(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSamplerParameteri;
      BufferChecks.checkFunctionAddress(var4);
      nglSamplerParameteri(var0, var1, var2, var4);
   }

   static native void nglSamplerParameteri(int var0, int var1, int var2, long var3);

   public static void glSamplerParameterf(int var0, int var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSamplerParameterf;
      BufferChecks.checkFunctionAddress(var4);
      nglSamplerParameterf(var0, var1, var2, var4);
   }

   static native void nglSamplerParameterf(int var0, int var1, float var2, long var3);

   public static void glSamplerParameter(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSamplerParameteriv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglSamplerParameteriv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglSamplerParameteriv(int var0, int var1, long var2, long var4);

   public static void glSamplerParameter(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSamplerParameterfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglSamplerParameterfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglSamplerParameterfv(int var0, int var1, long var2, long var4);

   public static void glSamplerParameterI(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSamplerParameterIiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglSamplerParameterIiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglSamplerParameterIiv(int var0, int var1, long var2, long var4);

   public static void glSamplerParameterIu(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSamplerParameterIuiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglSamplerParameterIuiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglSamplerParameterIuiv(int var0, int var1, long var2, long var4);

   public static void glGetSamplerParameter(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetSamplerParameteriv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetSamplerParameteriv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetSamplerParameteriv(int var0, int var1, long var2, long var4);

   public static int glGetSamplerParameteri(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetSamplerParameteriv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetSamplerParameteriv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetSamplerParameter(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetSamplerParameterfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetSamplerParameterfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetSamplerParameterfv(int var0, int var1, long var2, long var4);

   public static float glGetSamplerParameterf(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetSamplerParameterfv;
      BufferChecks.checkFunctionAddress(var3);
      FloatBuffer var5 = APIUtil.getBufferFloat(var2);
      nglGetSamplerParameterfv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetSamplerParameterI(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetSamplerParameterIiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetSamplerParameterIiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetSamplerParameterIiv(int var0, int var1, long var2, long var4);

   public static int glGetSamplerParameterIi(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetSamplerParameterIiv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetSamplerParameterIiv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetSamplerParameterIu(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetSamplerParameterIuiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetSamplerParameterIuiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetSamplerParameterIuiv(int var0, int var1, long var2, long var4);

   public static int glGetSamplerParameterIui(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetSamplerParameterIuiv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetSamplerParameterIuiv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glQueryCounter(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glQueryCounter;
      BufferChecks.checkFunctionAddress(var3);
      nglQueryCounter(var0, var1, var3);
   }

   static native void nglQueryCounter(int var0, int var1, long var2);

   public static void glGetQueryObject(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetQueryObjecti64v;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((LongBuffer)var2, 1);
      nglGetQueryObjecti64v(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetQueryObjecti64v(int var0, int var1, long var2, long var4);

   /** @deprecated */
   @Deprecated
   public static long glGetQueryObject(int var0, int var1) {
      return glGetQueryObjecti64(var0, var1);
   }

   public static long glGetQueryObjecti64(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetQueryObjecti64v;
      BufferChecks.checkFunctionAddress(var3);
      LongBuffer var5 = APIUtil.getBufferLong(var2);
      nglGetQueryObjecti64v(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetQueryObjectu(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetQueryObjectui64v;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((LongBuffer)var2, 1);
      nglGetQueryObjectui64v(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetQueryObjectui64v(int var0, int var1, long var2, long var4);

   /** @deprecated */
   @Deprecated
   public static long glGetQueryObjectu(int var0, int var1) {
      return glGetQueryObjectui64(var0, var1);
   }

   public static long glGetQueryObjectui64(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetQueryObjectui64v;
      BufferChecks.checkFunctionAddress(var3);
      LongBuffer var5 = APIUtil.getBufferLong(var2);
      nglGetQueryObjectui64v(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glVertexAttribDivisor(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribDivisor;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexAttribDivisor(var0, var1, var3);
   }

   static native void nglVertexAttribDivisor(int var0, int var1, long var2);

   public static void glVertexP2ui(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexP2ui;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexP2ui(var0, var1, var3);
   }

   static native void nglVertexP2ui(int var0, int var1, long var2);

   public static void glVertexP3ui(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexP3ui;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexP3ui(var0, var1, var3);
   }

   static native void nglVertexP3ui(int var0, int var1, long var2);

   public static void glVertexP4ui(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexP4ui;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexP4ui(var0, var1, var3);
   }

   static native void nglVertexP4ui(int var0, int var1, long var2);

   public static void glVertexP2u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexP2uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 2);
      nglVertexP2uiv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexP2uiv(int var0, long var1, long var3);

   public static void glVertexP3u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexP3uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 3);
      nglVertexP3uiv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexP3uiv(int var0, long var1, long var3);

   public static void glVertexP4u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexP4uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglVertexP4uiv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexP4uiv(int var0, long var1, long var3);

   public static void glTexCoordP1ui(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glTexCoordP1ui;
      BufferChecks.checkFunctionAddress(var3);
      nglTexCoordP1ui(var0, var1, var3);
   }

   static native void nglTexCoordP1ui(int var0, int var1, long var2);

   public static void glTexCoordP2ui(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glTexCoordP2ui;
      BufferChecks.checkFunctionAddress(var3);
      nglTexCoordP2ui(var0, var1, var3);
   }

   static native void nglTexCoordP2ui(int var0, int var1, long var2);

   public static void glTexCoordP3ui(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glTexCoordP3ui;
      BufferChecks.checkFunctionAddress(var3);
      nglTexCoordP3ui(var0, var1, var3);
   }

   static native void nglTexCoordP3ui(int var0, int var1, long var2);

   public static void glTexCoordP4ui(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glTexCoordP4ui;
      BufferChecks.checkFunctionAddress(var3);
      nglTexCoordP4ui(var0, var1, var3);
   }

   static native void nglTexCoordP4ui(int var0, int var1, long var2);

   public static void glTexCoordP1u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glTexCoordP1uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 1);
      nglTexCoordP1uiv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglTexCoordP1uiv(int var0, long var1, long var3);

   public static void glTexCoordP2u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glTexCoordP2uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 2);
      nglTexCoordP2uiv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglTexCoordP2uiv(int var0, long var1, long var3);

   public static void glTexCoordP3u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glTexCoordP3uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 3);
      nglTexCoordP3uiv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglTexCoordP3uiv(int var0, long var1, long var3);

   public static void glTexCoordP4u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glTexCoordP4uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglTexCoordP4uiv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglTexCoordP4uiv(int var0, long var1, long var3);

   public static void glMultiTexCoordP1ui(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiTexCoordP1ui;
      BufferChecks.checkFunctionAddress(var4);
      nglMultiTexCoordP1ui(var0, var1, var2, var4);
   }

   static native void nglMultiTexCoordP1ui(int var0, int var1, int var2, long var3);

   public static void glMultiTexCoordP2ui(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiTexCoordP2ui;
      BufferChecks.checkFunctionAddress(var4);
      nglMultiTexCoordP2ui(var0, var1, var2, var4);
   }

   static native void nglMultiTexCoordP2ui(int var0, int var1, int var2, long var3);

   public static void glMultiTexCoordP3ui(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiTexCoordP3ui;
      BufferChecks.checkFunctionAddress(var4);
      nglMultiTexCoordP3ui(var0, var1, var2, var4);
   }

   static native void nglMultiTexCoordP3ui(int var0, int var1, int var2, long var3);

   public static void glMultiTexCoordP4ui(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiTexCoordP4ui;
      BufferChecks.checkFunctionAddress(var4);
      nglMultiTexCoordP4ui(var0, var1, var2, var4);
   }

   static native void nglMultiTexCoordP4ui(int var0, int var1, int var2, long var3);

   public static void glMultiTexCoordP1u(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiTexCoordP1uiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglMultiTexCoordP1uiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglMultiTexCoordP1uiv(int var0, int var1, long var2, long var4);

   public static void glMultiTexCoordP2u(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiTexCoordP2uiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 2);
      nglMultiTexCoordP2uiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglMultiTexCoordP2uiv(int var0, int var1, long var2, long var4);

   public static void glMultiTexCoordP3u(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiTexCoordP3uiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 3);
      nglMultiTexCoordP3uiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglMultiTexCoordP3uiv(int var0, int var1, long var2, long var4);

   public static void glMultiTexCoordP4u(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiTexCoordP4uiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglMultiTexCoordP4uiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglMultiTexCoordP4uiv(int var0, int var1, long var2, long var4);

   public static void glNormalP3ui(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glNormalP3ui;
      BufferChecks.checkFunctionAddress(var3);
      nglNormalP3ui(var0, var1, var3);
   }

   static native void nglNormalP3ui(int var0, int var1, long var2);

   public static void glNormalP3u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glNormalP3uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 3);
      nglNormalP3uiv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglNormalP3uiv(int var0, long var1, long var3);

   public static void glColorP3ui(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glColorP3ui;
      BufferChecks.checkFunctionAddress(var3);
      nglColorP3ui(var0, var1, var3);
   }

   static native void nglColorP3ui(int var0, int var1, long var2);

   public static void glColorP4ui(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glColorP4ui;
      BufferChecks.checkFunctionAddress(var3);
      nglColorP4ui(var0, var1, var3);
   }

   static native void nglColorP4ui(int var0, int var1, long var2);

   public static void glColorP3u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glColorP3uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 3);
      nglColorP3uiv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglColorP3uiv(int var0, long var1, long var3);

   public static void glColorP4u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glColorP4uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglColorP4uiv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglColorP4uiv(int var0, long var1, long var3);

   public static void glSecondaryColorP3ui(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glSecondaryColorP3ui;
      BufferChecks.checkFunctionAddress(var3);
      nglSecondaryColorP3ui(var0, var1, var3);
   }

   static native void nglSecondaryColorP3ui(int var0, int var1, long var2);

   public static void glSecondaryColorP3u(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glSecondaryColorP3uiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 3);
      nglSecondaryColorP3uiv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglSecondaryColorP3uiv(int var0, long var1, long var3);

   public static void glVertexAttribP1ui(int var0, int var1, boolean var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribP1ui;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttribP1ui(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttribP1ui(int var0, int var1, boolean var2, int var3, long var4);

   public static void glVertexAttribP2ui(int var0, int var1, boolean var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribP2ui;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttribP2ui(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttribP2ui(int var0, int var1, boolean var2, int var3, long var4);

   public static void glVertexAttribP3ui(int var0, int var1, boolean var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribP3ui;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttribP3ui(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttribP3ui(int var0, int var1, boolean var2, int var3, long var4);

   public static void glVertexAttribP4ui(int var0, int var1, boolean var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribP4ui;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttribP4ui(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttribP4ui(int var0, int var1, boolean var2, int var3, long var4);

   public static void glVertexAttribP1u(int var0, int var1, boolean var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribP1uiv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      nglVertexAttribP1uiv(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglVertexAttribP1uiv(int var0, int var1, boolean var2, long var3, long var5);

   public static void glVertexAttribP2u(int var0, int var1, boolean var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribP2uiv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 2);
      nglVertexAttribP2uiv(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglVertexAttribP2uiv(int var0, int var1, boolean var2, long var3, long var5);

   public static void glVertexAttribP3u(int var0, int var1, boolean var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribP3uiv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 3);
      nglVertexAttribP3uiv(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglVertexAttribP3uiv(int var0, int var1, boolean var2, long var3, long var5);

   public static void glVertexAttribP4u(int var0, int var1, boolean var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribP4uiv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglVertexAttribP4uiv(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglVertexAttribP4uiv(int var0, int var1, boolean var2, long var3, long var5);
}
