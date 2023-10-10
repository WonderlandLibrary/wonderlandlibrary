package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class EXTTransformFeedback {
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_EXT = 35982;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_START_EXT = 35972;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_SIZE_EXT = 35973;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_BINDING_EXT = 35983;
   public static final int GL_INTERLEAVED_ATTRIBS_EXT = 35980;
   public static final int GL_SEPARATE_ATTRIBS_EXT = 35981;
   public static final int GL_PRIMITIVES_GENERATED_EXT = 35975;
   public static final int GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN_EXT = 35976;
   public static final int GL_RASTERIZER_DISCARD_EXT = 35977;
   public static final int GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS_EXT = 35978;
   public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS_EXT = 35979;
   public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS_EXT = 35968;
   public static final int GL_TRANSFORM_FEEDBACK_VARYINGS_EXT = 35971;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_MODE_EXT = 35967;
   public static final int GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH_EXT = 35958;

   private EXTTransformFeedback() {
   }

   public static void glBindBufferRangeEXT(int var0, int var1, int var2, long var3, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glBindBufferRangeEXT;
      BufferChecks.checkFunctionAddress(var8);
      nglBindBufferRangeEXT(var0, var1, var2, var3, var5, var8);
   }

   static native void nglBindBufferRangeEXT(int var0, int var1, int var2, long var3, long var5, long var7);

   public static void glBindBufferOffsetEXT(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glBindBufferOffsetEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglBindBufferOffsetEXT(var0, var1, var2, var3, var6);
   }

   static native void nglBindBufferOffsetEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glBindBufferBaseEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBindBufferBaseEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglBindBufferBaseEXT(var0, var1, var2, var4);
   }

   static native void nglBindBufferBaseEXT(int var0, int var1, int var2, long var3);

   public static void glBeginTransformFeedbackEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glBeginTransformFeedbackEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglBeginTransformFeedbackEXT(var0, var2);
   }

   static native void nglBeginTransformFeedbackEXT(int var0, long var1);

   public static void glEndTransformFeedbackEXT() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glEndTransformFeedbackEXT;
      BufferChecks.checkFunctionAddress(var1);
      nglEndTransformFeedbackEXT(var1);
   }

   static native void nglEndTransformFeedbackEXT(long var0);

   public static void glTransformFeedbackVaryingsEXT(int var0, int var1, ByteBuffer var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTransformFeedbackVaryingsEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2, var1);
      nglTransformFeedbackVaryingsEXT(var0, var1, MemoryUtil.getAddress(var2), var3, var5);
   }

   static native void nglTransformFeedbackVaryingsEXT(int var0, int var1, long var2, int var4, long var5);

   public static void glTransformFeedbackVaryingsEXT(int var0, CharSequence[] var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTransformFeedbackVaryingsEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkArray(var1);
      nglTransformFeedbackVaryingsEXT(var0, var1.length, APIUtil.getBufferNT(var3, var1), var2, var4);
   }

   public static void glGetTransformFeedbackVaryingEXT(int var0, int var1, IntBuffer var2, IntBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetTransformFeedbackVaryingEXT;
      BufferChecks.checkFunctionAddress(var7);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      BufferChecks.checkBuffer((IntBuffer)var4, 1);
      BufferChecks.checkDirect(var5);
      nglGetTransformFeedbackVaryingEXT(var0, var1, var5.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   static native void nglGetTransformFeedbackVaryingEXT(int var0, int var1, int var2, long var3, long var5, long var7, long var9, long var11);

   public static String glGetTransformFeedbackVaryingEXT(int var0, int var1, int var2, IntBuffer var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetTransformFeedbackVaryingEXT;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      BufferChecks.checkBuffer((IntBuffer)var4, 1);
      IntBuffer var8 = APIUtil.getLengths(var5);
      ByteBuffer var9 = APIUtil.getBufferByte(var5, var2);
      nglGetTransformFeedbackVaryingEXT(var0, var1, var2, MemoryUtil.getAddress0((Buffer)var8), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var9), var6);
      var9.limit(var8.get(0));
      return APIUtil.getString(var5, var9);
   }
}
