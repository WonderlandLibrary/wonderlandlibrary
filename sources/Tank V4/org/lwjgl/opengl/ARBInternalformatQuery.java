package org.lwjgl.opengl;

import java.nio.IntBuffer;

public final class ARBInternalformatQuery {
   public static final int GL_NUM_SAMPLE_COUNTS = 37760;

   private ARBInternalformatQuery() {
   }

   public static void glGetInternalformat(int var0, int var1, int var2, IntBuffer var3) {
      GL42.glGetInternalformat(var0, var1, var2, var3);
   }

   public static int glGetInternalformat(int var0, int var1, int var2) {
      return GL42.glGetInternalformat(var0, var1, var2);
   }
}
