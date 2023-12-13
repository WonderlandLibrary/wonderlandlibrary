package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class GL32 {
   public static final int GL_CONTEXT_PROFILE_MASK = 37158;
   public static final int GL_CONTEXT_CORE_PROFILE_BIT = 1;
   public static final int GL_CONTEXT_COMPATIBILITY_PROFILE_BIT = 2;
   public static final int GL_MAX_VERTEX_OUTPUT_COMPONENTS = 37154;
   public static final int GL_MAX_GEOMETRY_INPUT_COMPONENTS = 37155;
   public static final int GL_MAX_GEOMETRY_OUTPUT_COMPONENTS = 37156;
   public static final int GL_MAX_FRAGMENT_INPUT_COMPONENTS = 37157;
   public static final int GL_FIRST_VERTEX_CONVENTION = 36429;
   public static final int GL_LAST_VERTEX_CONVENTION = 36430;
   public static final int GL_PROVOKING_VERTEX = 36431;
   public static final int GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION = 36428;
   public static final int GL_TEXTURE_CUBE_MAP_SEAMLESS = 34895;
   public static final int GL_SAMPLE_POSITION = 36432;
   public static final int GL_SAMPLE_MASK = 36433;
   public static final int GL_SAMPLE_MASK_VALUE = 36434;
   public static final int GL_TEXTURE_2D_MULTISAMPLE = 37120;
   public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE = 37121;
   public static final int GL_TEXTURE_2D_MULTISAMPLE_ARRAY = 37122;
   public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY = 37123;
   public static final int GL_MAX_SAMPLE_MASK_WORDS = 36441;
   public static final int GL_MAX_COLOR_TEXTURE_SAMPLES = 37134;
   public static final int GL_MAX_DEPTH_TEXTURE_SAMPLES = 37135;
   public static final int GL_MAX_INTEGER_SAMPLES = 37136;
   public static final int GL_TEXTURE_BINDING_2D_MULTISAMPLE = 37124;
   public static final int GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY = 37125;
   public static final int GL_TEXTURE_SAMPLES = 37126;
   public static final int GL_TEXTURE_FIXED_SAMPLE_LOCATIONS = 37127;
   public static final int GL_SAMPLER_2D_MULTISAMPLE = 37128;
   public static final int GL_INT_SAMPLER_2D_MULTISAMPLE = 37129;
   public static final int GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE = 37130;
   public static final int GL_SAMPLER_2D_MULTISAMPLE_ARRAY = 37131;
   public static final int GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 37132;
   public static final int GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 37133;
   public static final int GL_DEPTH_CLAMP = 34383;
   public static final int GL_GEOMETRY_SHADER = 36313;
   public static final int GL_GEOMETRY_VERTICES_OUT = 36314;
   public static final int GL_GEOMETRY_INPUT_TYPE = 36315;
   public static final int GL_GEOMETRY_OUTPUT_TYPE = 36316;
   public static final int GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS = 35881;
   public static final int GL_MAX_VARYING_COMPONENTS = 35659;
   public static final int GL_MAX_GEOMETRY_UNIFORM_COMPONENTS = 36319;
   public static final int GL_MAX_GEOMETRY_OUTPUT_VERTICES = 36320;
   public static final int GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS = 36321;
   public static final int GL_LINES_ADJACENCY = 10;
   public static final int GL_LINE_STRIP_ADJACENCY = 11;
   public static final int GL_TRIANGLES_ADJACENCY = 12;
   public static final int GL_TRIANGLE_STRIP_ADJACENCY = 13;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS = 36264;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_LAYERED = 36263;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER = 36052;
   public static final int GL_PROGRAM_POINT_SIZE = 34370;
   public static final int GL_MAX_SERVER_WAIT_TIMEOUT = 37137;
   public static final int GL_OBJECT_TYPE = 37138;
   public static final int GL_SYNC_CONDITION = 37139;
   public static final int GL_SYNC_STATUS = 37140;
   public static final int GL_SYNC_FLAGS = 37141;
   public static final int GL_SYNC_FENCE = 37142;
   public static final int GL_SYNC_GPU_COMMANDS_COMPLETE = 37143;
   public static final int GL_UNSIGNALED = 37144;
   public static final int GL_SIGNALED = 37145;
   public static final int GL_SYNC_FLUSH_COMMANDS_BIT = 1;
   public static final long GL_TIMEOUT_IGNORED = -1L;
   public static final int GL_ALREADY_SIGNALED = 37146;
   public static final int GL_TIMEOUT_EXPIRED = 37147;
   public static final int GL_CONDITION_SATISFIED = 37148;
   public static final int GL_WAIT_FAILED = 37149;

   private GL32() {
   }

   public static void glGetBufferParameter(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetBufferParameteri64v;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((LongBuffer)var2, 4);
      nglGetBufferParameteri64v(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetBufferParameteri64v(int var0, int var1, long var2, long var4);

   /** @deprecated */
   @Deprecated
   public static long glGetBufferParameter(int var0, int var1) {
      return glGetBufferParameteri64(var0, var1);
   }

   public static long glGetBufferParameteri64(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetBufferParameteri64v;
      BufferChecks.checkFunctionAddress(var3);
      LongBuffer var5 = APIUtil.getBufferLong(var2);
      nglGetBufferParameteri64v(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glDrawElementsBaseVertex(int var0, ByteBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glDrawElementsBaseVertex;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureElementVBOdisabled(var3);
      BufferChecks.checkDirect(var1);
      nglDrawElementsBaseVertex(var0, var1.remaining(), 5121, MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glDrawElementsBaseVertex(int var0, IntBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glDrawElementsBaseVertex;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureElementVBOdisabled(var3);
      BufferChecks.checkDirect(var1);
      nglDrawElementsBaseVertex(var0, var1.remaining(), 5125, MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glDrawElementsBaseVertex(int var0, ShortBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glDrawElementsBaseVertex;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureElementVBOdisabled(var3);
      BufferChecks.checkDirect(var1);
      nglDrawElementsBaseVertex(var0, var1.remaining(), 5123, MemoryUtil.getAddress(var1), var2, var4);
   }

   static native void nglDrawElementsBaseVertex(int var0, int var1, int var2, long var3, int var5, long var6);

   public static void glDrawElementsBaseVertex(int var0, int var1, int var2, long var3, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glDrawElementsBaseVertex;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureElementVBOenabled(var6);
      nglDrawElementsBaseVertexBO(var0, var1, var2, var3, var5, var7);
   }

   static native void nglDrawElementsBaseVertexBO(int var0, int var1, int var2, long var3, int var5, long var6);

   public static void glDrawRangeElementsBaseVertex(int var0, int var1, int var2, ByteBuffer var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDrawRangeElementsBaseVertex;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureElementVBOdisabled(var5);
      BufferChecks.checkDirect(var3);
      nglDrawRangeElementsBaseVertex(var0, var1, var2, var3.remaining(), 5121, MemoryUtil.getAddress(var3), var4, var6);
   }

   public static void glDrawRangeElementsBaseVertex(int var0, int var1, int var2, IntBuffer var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDrawRangeElementsBaseVertex;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureElementVBOdisabled(var5);
      BufferChecks.checkDirect(var3);
      nglDrawRangeElementsBaseVertex(var0, var1, var2, var3.remaining(), 5125, MemoryUtil.getAddress(var3), var4, var6);
   }

   public static void glDrawRangeElementsBaseVertex(int var0, int var1, int var2, ShortBuffer var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDrawRangeElementsBaseVertex;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureElementVBOdisabled(var5);
      BufferChecks.checkDirect(var3);
      nglDrawRangeElementsBaseVertex(var0, var1, var2, var3.remaining(), 5123, MemoryUtil.getAddress(var3), var4, var6);
   }

   static native void nglDrawRangeElementsBaseVertex(int var0, int var1, int var2, int var3, int var4, long var5, int var7, long var8);

   public static void glDrawRangeElementsBaseVertex(int var0, int var1, int var2, int var3, int var4, long var5, int var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glDrawRangeElementsBaseVertex;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureElementVBOenabled(var8);
      nglDrawRangeElementsBaseVertexBO(var0, var1, var2, var3, var4, var5, var7, var9);
   }

   static native void nglDrawRangeElementsBaseVertexBO(int var0, int var1, int var2, int var3, int var4, long var5, int var7, long var8);

   public static void glDrawElementsInstancedBaseVertex(int var0, ByteBuffer var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawElementsInstancedBaseVertex;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureElementVBOdisabled(var4);
      BufferChecks.checkDirect(var1);
      nglDrawElementsInstancedBaseVertex(var0, var1.remaining(), 5121, MemoryUtil.getAddress(var1), var2, var3, var5);
   }

   public static void glDrawElementsInstancedBaseVertex(int var0, IntBuffer var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawElementsInstancedBaseVertex;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureElementVBOdisabled(var4);
      BufferChecks.checkDirect(var1);
      nglDrawElementsInstancedBaseVertex(var0, var1.remaining(), 5125, MemoryUtil.getAddress(var1), var2, var3, var5);
   }

   public static void glDrawElementsInstancedBaseVertex(int var0, ShortBuffer var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawElementsInstancedBaseVertex;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureElementVBOdisabled(var4);
      BufferChecks.checkDirect(var1);
      nglDrawElementsInstancedBaseVertex(var0, var1.remaining(), 5123, MemoryUtil.getAddress(var1), var2, var3, var5);
   }

   static native void nglDrawElementsInstancedBaseVertex(int var0, int var1, int var2, long var3, int var5, int var6, long var7);

   public static void glDrawElementsInstancedBaseVertex(int var0, int var1, int var2, long var3, int var5, int var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glDrawElementsInstancedBaseVertex;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureElementVBOenabled(var7);
      nglDrawElementsInstancedBaseVertexBO(var0, var1, var2, var3, var5, var6, var8);
   }

   static native void nglDrawElementsInstancedBaseVertexBO(int var0, int var1, int var2, long var3, int var5, int var6, long var7);

   public static void glProvokingVertex(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glProvokingVertex;
      BufferChecks.checkFunctionAddress(var2);
      nglProvokingVertex(var0, var2);
   }

   static native void nglProvokingVertex(int var0, long var1);

   public static void glTexImage2DMultisample(int var0, int var1, int var2, int var3, int var4, boolean var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glTexImage2DMultisample;
      BufferChecks.checkFunctionAddress(var7);
      nglTexImage2DMultisample(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglTexImage2DMultisample(int var0, int var1, int var2, int var3, int var4, boolean var5, long var6);

   public static void glTexImage3DMultisample(int var0, int var1, int var2, int var3, int var4, int var5, boolean var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glTexImage3DMultisample;
      BufferChecks.checkFunctionAddress(var8);
      nglTexImage3DMultisample(var0, var1, var2, var3, var4, var5, var6, var8);
   }

   static native void nglTexImage3DMultisample(int var0, int var1, int var2, int var3, int var4, int var5, boolean var6, long var7);

   public static void glGetMultisample(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetMultisamplefv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 2);
      nglGetMultisamplefv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetMultisamplefv(int var0, int var1, long var2, long var4);

   public static void glSampleMaski(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glSampleMaski;
      BufferChecks.checkFunctionAddress(var3);
      nglSampleMaski(var0, var1, var3);
   }

   static native void nglSampleMaski(int var0, int var1, long var2);

   public static void glFramebufferTexture(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glFramebufferTexture;
      BufferChecks.checkFunctionAddress(var5);
      nglFramebufferTexture(var0, var1, var2, var3, var5);
   }

   static native void nglFramebufferTexture(int var0, int var1, int var2, int var3, long var4);

   public static GLSync glFenceSync(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glFenceSync;
      BufferChecks.checkFunctionAddress(var3);
      GLSync var5 = new GLSync(nglFenceSync(var0, var1, var3));
      return var5;
   }

   static native long nglFenceSync(int var0, int var1, long var2);

   public static boolean glIsSync(GLSync var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsSync;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsSync(var0.getPointer(), var2);
      return var4;
   }

   static native boolean nglIsSync(long var0, long var2);

   public static void glDeleteSync(GLSync var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteSync;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteSync(var0.getPointer(), var2);
   }

   static native void nglDeleteSync(long var0, long var2);

   public static int glClientWaitSync(GLSync var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glClientWaitSync;
      BufferChecks.checkFunctionAddress(var5);
      int var7 = nglClientWaitSync(var0.getPointer(), var1, var2, var5);
      return var7;
   }

   static native int nglClientWaitSync(long var0, int var2, long var3, long var5);

   public static void glWaitSync(GLSync var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glWaitSync;
      BufferChecks.checkFunctionAddress(var5);
      nglWaitSync(var0.getPointer(), var1, var2, var5);
   }

   static native void nglWaitSync(long var0, int var2, long var3, long var5);

   public static void glGetInteger64(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetInteger64v;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((LongBuffer)var1, 1);
      nglGetInteger64v(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetInteger64v(int var0, long var1, long var3);

   public static long glGetInteger64(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGetInteger64v;
      BufferChecks.checkFunctionAddress(var2);
      LongBuffer var4 = APIUtil.getBufferLong(var1);
      nglGetInteger64v(var0, MemoryUtil.getAddress(var4), var2);
      return var4.get(0);
   }

   public static void glGetInteger64(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetInteger64i_v;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((LongBuffer)var2, 4);
      nglGetInteger64i_v(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetInteger64i_v(int var0, int var1, long var2, long var4);

   public static long glGetInteger64(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetInteger64i_v;
      BufferChecks.checkFunctionAddress(var3);
      LongBuffer var5 = APIUtil.getBufferLong(var2);
      nglGetInteger64i_v(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetSync(GLSync var0, int var1, IntBuffer var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetSynciv;
      BufferChecks.checkFunctionAddress(var5);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      BufferChecks.checkDirect(var3);
      nglGetSynciv(var0.getPointer(), var1, var3.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetSynciv(long var0, int var2, int var3, long var4, long var6, long var8);

   /** @deprecated */
   @Deprecated
   public static int glGetSync(GLSync var0, int var1) {
      return glGetSynci(var0, var1);
   }

   public static int glGetSynci(GLSync var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetSynciv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetSynciv(var0.getPointer(), var1, 1, 0L, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }
}
