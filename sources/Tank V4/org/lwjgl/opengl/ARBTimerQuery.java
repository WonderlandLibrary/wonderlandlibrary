package org.lwjgl.opengl;

import java.nio.LongBuffer;

public final class ARBTimerQuery {
   public static final int GL_TIME_ELAPSED = 35007;
   public static final int GL_TIMESTAMP = 36392;

   private ARBTimerQuery() {
   }

   public static void glQueryCounter(int var0, int var1) {
      GL33.glQueryCounter(var0, var1);
   }

   public static void glGetQueryObject(int var0, int var1, LongBuffer var2) {
      GL33.glGetQueryObject(var0, var1, var2);
   }

   public static long glGetQueryObjecti64(int var0, int var1) {
      return GL33.glGetQueryObjecti64(var0, var1);
   }

   public static void glGetQueryObjectu(int var0, int var1, LongBuffer var2) {
      GL33.glGetQueryObjectu(var0, var1, var2);
   }

   public static long glGetQueryObjectui64(int var0, int var1) {
      return GL33.glGetQueryObjectui64(var0, var1);
   }
}
