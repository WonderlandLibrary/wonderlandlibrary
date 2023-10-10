package org.lwjgl.opengl;

import java.nio.IntBuffer;

public final class ARBTransformFeedback2 {
   public static final int GL_TRANSFORM_FEEDBACK = 36386;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED = 36387;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE = 36388;
   public static final int GL_TRANSFORM_FEEDBACK_BINDING = 36389;

   private ARBTransformFeedback2() {
   }

   public static void glBindTransformFeedback(int var0, int var1) {
      GL40.glBindTransformFeedback(var0, var1);
   }

   public static void glDeleteTransformFeedbacks(IntBuffer var0) {
      GL40.glDeleteTransformFeedbacks(var0);
   }

   public static void glDeleteTransformFeedbacks(int var0) {
      GL40.glDeleteTransformFeedbacks(var0);
   }

   public static void glGenTransformFeedbacks(IntBuffer var0) {
      GL40.glGenTransformFeedbacks(var0);
   }

   public static int glGenTransformFeedbacks() {
      return GL40.glGenTransformFeedbacks();
   }

   public static boolean glIsTransformFeedback(int var0) {
      return GL40.glIsTransformFeedback(var0);
   }

   public static void glPauseTransformFeedback() {
      GL40.glPauseTransformFeedback();
   }

   public static void glResumeTransformFeedback() {
      GL40.glResumeTransformFeedback();
   }

   public static void glDrawTransformFeedback(int var0, int var1) {
      GL40.glDrawTransformFeedback(var0, var1);
   }
}
