package org.lwjgl.opengl;

public final class ARBCopyBuffer {
   public static final int GL_COPY_READ_BUFFER = 36662;
   public static final int GL_COPY_WRITE_BUFFER = 36663;

   private ARBCopyBuffer() {
   }

   public static void glCopyBufferSubData(int var0, int var1, long var2, long var4, long var6) {
      GL31.glCopyBufferSubData(var0, var1, var2, var4, var6);
   }
}
