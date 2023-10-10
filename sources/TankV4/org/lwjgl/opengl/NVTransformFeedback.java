package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVTransformFeedback {
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_NV = 35982;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_START_NV = 35972;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_SIZE_NV = 35973;
   public static final int GL_TRANSFORM_FEEDBACK_RECORD_NV = 35974;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_BINDING_NV = 35983;
   public static final int GL_INTERLEAVED_ATTRIBS_NV = 35980;
   public static final int GL_SEPARATE_ATTRIBS_NV = 35981;
   public static final int GL_PRIMITIVES_GENERATED_NV = 35975;
   public static final int GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN_NV = 35976;
   public static final int GL_RASTERIZER_DISCARD_NV = 35977;
   public static final int GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS_NV = 35978;
   public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS_NV = 35979;
   public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS_NV = 35968;
   public static final int GL_TRANSFORM_FEEDBACK_ATTRIBS_NV = 35966;
   public static final int GL_ACTIVE_VARYINGS_NV = 35969;
   public static final int GL_ACTIVE_VARYING_MAX_LENGTH_NV = 35970;
   public static final int GL_TRANSFORM_FEEDBACK_VARYINGS_NV = 35971;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_MODE_NV = 35967;
   public static final int GL_BACK_PRIMARY_COLOR_NV = 35959;
   public static final int GL_BACK_SECONDARY_COLOR_NV = 35960;
   public static final int GL_TEXTURE_COORD_NV = 35961;
   public static final int GL_CLIP_DISTANCE_NV = 35962;
   public static final int GL_VERTEX_ID_NV = 35963;
   public static final int GL_PRIMITIVE_ID_NV = 35964;
   public static final int GL_GENERIC_ATTRIB_NV = 35965;
   public static final int GL_LAYER_NV = 36266;

   private NVTransformFeedback() {
   }

   public static void glBindBufferRangeNV(int var0, int var1, int var2, long var3, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glBindBufferRangeNV;
      BufferChecks.checkFunctionAddress(var8);
      nglBindBufferRangeNV(var0, var1, var2, var3, var5, var8);
   }

   static native void nglBindBufferRangeNV(int var0, int var1, int var2, long var3, long var5, long var7);

   public static void glBindBufferOffsetNV(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glBindBufferOffsetNV;
      BufferChecks.checkFunctionAddress(var6);
      nglBindBufferOffsetNV(var0, var1, var2, var3, var6);
   }

   static native void nglBindBufferOffsetNV(int var0, int var1, int var2, long var3, long var5);

   public static void glBindBufferBaseNV(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBindBufferBaseNV;
      BufferChecks.checkFunctionAddress(var4);
      nglBindBufferBaseNV(var0, var1, var2, var4);
   }

   static native void nglBindBufferBaseNV(int var0, int var1, int var2, long var3);

   public static void glTransformFeedbackAttribsNV(IntBuffer var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glTransformFeedbackAttribsNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var0, 3);
      nglTransformFeedbackAttribsNV(var0.remaining() / 3, MemoryUtil.getAddress(var0), var1, var3);
   }

   static native void nglTransformFeedbackAttribsNV(int var0, long var1, int var3, long var4);

   public static void glTransformFeedbackVaryingsNV(int var0, IntBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTransformFeedbackVaryingsNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglTransformFeedbackVaryingsNV(var0, var1.remaining(), MemoryUtil.getAddress(var1), var2, var4);
   }

   static native void nglTransformFeedbackVaryingsNV(int var0, int var1, long var2, int var4, long var5);

   public static void glBeginTransformFeedbackNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glBeginTransformFeedbackNV;
      BufferChecks.checkFunctionAddress(var2);
      nglBeginTransformFeedbackNV(var0, var2);
   }

   static native void nglBeginTransformFeedbackNV(int var0, long var1);

   public static void glEndTransformFeedbackNV() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glEndTransformFeedbackNV;
      BufferChecks.checkFunctionAddress(var1);
      nglEndTransformFeedbackNV(var1);
   }

   static native void nglEndTransformFeedbackNV(long var0);

   public static int glGetVaryingLocationNV(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetVaryingLocationNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkNullTerminated(var1);
      int var5 = nglGetVaryingLocationNV(var0, MemoryUtil.getAddress(var1), var3);
      return var5;
   }

   static native int nglGetVaryingLocationNV(int var0, long var1, long var3);

   public static int glGetVaryingLocationNV(int var0, CharSequence var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetVaryingLocationNV;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nglGetVaryingLocationNV(var0, APIUtil.getBufferNT(var2, var1), var3);
      return var5;
   }

   public static void glGetActiveVaryingNV(int var0, int var1, IntBuffer var2, IntBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetActiveVaryingNV;
      BufferChecks.checkFunctionAddress(var7);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      BufferChecks.checkBuffer((IntBuffer)var4, 1);
      BufferChecks.checkDirect(var5);
      nglGetActiveVaryingNV(var0, var1, var5.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   static native void nglGetActiveVaryingNV(int var0, int var1, int var2, long var3, long var5, long var7, long var9, long var11);

   public static String glGetActiveVaryingNV(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetActiveVaryingNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 2);
      IntBuffer var7 = APIUtil.getLengths(var4);
      ByteBuffer var8 = APIUtil.getBufferByte(var4, var2);
      nglGetActiveVaryingNV(var0, var1, var2, MemoryUtil.getAddress0((Buffer)var7), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var3, var3.position() + 1), MemoryUtil.getAddress(var8), var5);
      var8.limit(var7.get(0));
      return APIUtil.getString(var4, var8);
   }

   public static String glGetActiveVaryingNV(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetActiveVaryingNV;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getLengths(var3);
      ByteBuffer var7 = APIUtil.getBufferByte(var3, var2);
      nglGetActiveVaryingNV(var0, var1, var2, MemoryUtil.getAddress0((Buffer)var6), MemoryUtil.getAddress0((Buffer)APIUtil.getBufferInt(var3)), MemoryUtil.getAddress((IntBuffer)APIUtil.getBufferInt(var3), 1), MemoryUtil.getAddress(var7), var4);
      var7.limit(var6.get(0));
      return APIUtil.getString(var3, var7);
   }

   public static int glGetActiveVaryingSizeNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetActiveVaryingNV;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetActiveVaryingNV(var0, var1, 0, 0L, MemoryUtil.getAddress(var5), MemoryUtil.getAddress((IntBuffer)var5, 1), APIUtil.getBufferByte0(var2), var3);
      return var5.get(0);
   }

   public static int glGetActiveVaryingTypeNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetActiveVaryingNV;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetActiveVaryingNV(var0, var1, 0, 0L, MemoryUtil.getAddress((IntBuffer)var5, 1), MemoryUtil.getAddress(var5), APIUtil.getBufferByte0(var2), var3);
      return var5.get(0);
   }

   public static void glActiveVaryingNV(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glActiveVaryingNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkNullTerminated(var1);
      nglActiveVaryingNV(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglActiveVaryingNV(int var0, long var1, long var3);

   public static void glActiveVaryingNV(int var0, CharSequence var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glActiveVaryingNV;
      BufferChecks.checkFunctionAddress(var3);
      nglActiveVaryingNV(var0, APIUtil.getBufferNT(var2, var1), var3);
   }

   public static void glGetTransformFeedbackVaryingNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTransformFeedbackVaryingNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetTransformFeedbackVaryingNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetTransformFeedbackVaryingNV(int var0, int var1, long var2, long var4);

   public static int glGetTransformFeedbackVaryingNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetTransformFeedbackVaryingNV;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetTransformFeedbackVaryingNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }
}
