package org.lwjgl.opengl;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

public final class ARBSync {
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

   private ARBSync() {
   }

   public static GLSync glFenceSync(int var0, int var1) {
      return GL32.glFenceSync(var0, var1);
   }

   public static boolean glIsSync(GLSync var0) {
      return GL32.glIsSync(var0);
   }

   public static void glDeleteSync(GLSync var0) {
      GL32.glDeleteSync(var0);
   }

   public static int glClientWaitSync(GLSync var0, int var1, long var2) {
      return GL32.glClientWaitSync(var0, var1, var2);
   }

   public static void glWaitSync(GLSync var0, int var1, long var2) {
      GL32.glWaitSync(var0, var1, var2);
   }

   public static void glGetInteger64(int var0, LongBuffer var1) {
      GL32.glGetInteger64(var0, var1);
   }

   public static long glGetInteger64(int var0) {
      return GL32.glGetInteger64(var0);
   }

   public static void glGetSync(GLSync var0, int var1, IntBuffer var2, IntBuffer var3) {
      GL32.glGetSync(var0, var1, var2, var3);
   }

   /** @deprecated */
   @Deprecated
   public static int glGetSync(GLSync var0, int var1) {
      return GL32.glGetSynci(var0, var1);
   }

   public static int glGetSynci(GLSync var0, int var1) {
      return GL32.glGetSynci(var0, var1);
   }
}
