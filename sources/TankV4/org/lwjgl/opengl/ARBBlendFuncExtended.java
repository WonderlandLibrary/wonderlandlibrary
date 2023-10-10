package org.lwjgl.opengl;

import java.nio.ByteBuffer;

public final class ARBBlendFuncExtended {
   public static final int GL_SRC1_COLOR = 35065;
   public static final int GL_SRC1_ALPHA = 34185;
   public static final int GL_ONE_MINUS_SRC1_COLOR = 35066;
   public static final int GL_ONE_MINUS_SRC1_ALPHA = 35067;
   public static final int GL_MAX_DUAL_SOURCE_DRAW_BUFFERS = 35068;

   private ARBBlendFuncExtended() {
   }

   public static void glBindFragDataLocationIndexed(int var0, int var1, int var2, ByteBuffer var3) {
      GL33.glBindFragDataLocationIndexed(var0, var1, var2, var3);
   }

   public static void glBindFragDataLocationIndexed(int var0, int var1, int var2, CharSequence var3) {
      GL33.glBindFragDataLocationIndexed(var0, var1, var2, var3);
   }

   public static int glGetFragDataIndex(int var0, ByteBuffer var1) {
      return GL33.glGetFragDataIndex(var0, var1);
   }

   public static int glGetFragDataIndex(int var0, CharSequence var1) {
      return GL33.glGetFragDataIndex(var0, var1);
   }
}
