package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class ARBDrawIndirect {
   public static final int GL_DRAW_INDIRECT_BUFFER = 36671;
   public static final int GL_DRAW_INDIRECT_BUFFER_BINDING = 36675;

   private ARBDrawIndirect() {
   }

   public static void glDrawArraysIndirect(int var0, ByteBuffer var1) {
      GL40.glDrawArraysIndirect(var0, var1);
   }

   public static void glDrawArraysIndirect(int var0, long var1) {
      GL40.glDrawArraysIndirect(var0, var1);
   }

   public static void glDrawArraysIndirect(int var0, IntBuffer var1) {
      GL40.glDrawArraysIndirect(var0, var1);
   }

   public static void glDrawElementsIndirect(int var0, int var1, ByteBuffer var2) {
      GL40.glDrawElementsIndirect(var0, var1, var2);
   }

   public static void glDrawElementsIndirect(int var0, int var1, long var2) {
      GL40.glDrawElementsIndirect(var0, var1, var2);
   }

   public static void glDrawElementsIndirect(int var0, int var1, IntBuffer var2) {
      GL40.glDrawElementsIndirect(var0, var1, var2);
   }
}
