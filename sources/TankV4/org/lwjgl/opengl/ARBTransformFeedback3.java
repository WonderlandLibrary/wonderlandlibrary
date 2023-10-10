package org.lwjgl.opengl;

import java.nio.IntBuffer;

public final class ARBTransformFeedback3 {
   public static final int GL_MAX_TRANSFORM_FEEDBACK_BUFFERS = 36464;
   public static final int GL_MAX_VERTEX_STREAMS = 36465;

   private ARBTransformFeedback3() {
   }

   public static void glDrawTransformFeedbackStream(int var0, int var1, int var2) {
      GL40.glDrawTransformFeedbackStream(var0, var1, var2);
   }

   public static void glBeginQueryIndexed(int var0, int var1, int var2) {
      GL40.glBeginQueryIndexed(var0, var1, var2);
   }

   public static void glEndQueryIndexed(int var0, int var1) {
      GL40.glEndQueryIndexed(var0, var1);
   }

   public static void glGetQueryIndexed(int var0, int var1, int var2, IntBuffer var3) {
      GL40.glGetQueryIndexed(var0, var1, var2, var3);
   }

   public static int glGetQueryIndexedi(int var0, int var1, int var2) {
      return GL40.glGetQueryIndexedi(var0, var1, var2);
   }
}
