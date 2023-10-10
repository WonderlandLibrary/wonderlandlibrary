package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVTransformFeedback2 {
   public static final int GL_TRANSFORM_FEEDBACK_NV = 36386;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED_NV = 36387;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE_NV = 36388;
   public static final int GL_TRANSFORM_FEEDBACK_BINDING_NV = 36389;

   private NVTransformFeedback2() {
   }

   public static void glBindTransformFeedbackNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBindTransformFeedbackNV;
      BufferChecks.checkFunctionAddress(var3);
      nglBindTransformFeedbackNV(var0, var1, var3);
   }

   static native void nglBindTransformFeedbackNV(int var0, int var1, long var2);

   public static void glDeleteTransformFeedbacksNV(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteTransformFeedbacksNV;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteTransformFeedbacksNV(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteTransformFeedbacksNV(int var0, long var1, long var3);

   public static void glDeleteTransformFeedbacksNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteTransformFeedbacksNV;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteTransformFeedbacksNV(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glGenTransformFeedbacksNV(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenTransformFeedbacksNV;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenTransformFeedbacksNV(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenTransformFeedbacksNV(int var0, long var1, long var3);

   public static int glGenTransformFeedbacksNV() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenTransformFeedbacksNV;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenTransformFeedbacksNV(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static boolean glIsTransformFeedbackNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsTransformFeedbackNV;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsTransformFeedbackNV(var0, var2);
      return var4;
   }

   static native boolean nglIsTransformFeedbackNV(int var0, long var1);

   public static void glPauseTransformFeedbackNV() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glPauseTransformFeedbackNV;
      BufferChecks.checkFunctionAddress(var1);
      nglPauseTransformFeedbackNV(var1);
   }

   static native void nglPauseTransformFeedbackNV(long var0);

   public static void glResumeTransformFeedbackNV() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glResumeTransformFeedbackNV;
      BufferChecks.checkFunctionAddress(var1);
      nglResumeTransformFeedbackNV(var1);
   }

   static native void nglResumeTransformFeedbackNV(long var0);

   public static void glDrawTransformFeedbackNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDrawTransformFeedbackNV;
      BufferChecks.checkFunctionAddress(var3);
      nglDrawTransformFeedbackNV(var0, var1, var3);
   }

   static native void nglDrawTransformFeedbackNV(int var0, int var1, long var2);
}
