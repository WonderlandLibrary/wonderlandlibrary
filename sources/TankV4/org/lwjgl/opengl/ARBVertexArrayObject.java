package org.lwjgl.opengl;

import java.nio.IntBuffer;

public final class ARBVertexArrayObject {
   public static final int GL_VERTEX_ARRAY_BINDING = 34229;

   private ARBVertexArrayObject() {
   }

   public static void glBindVertexArray(int var0) {
      GL30.glBindVertexArray(var0);
   }

   public static void glDeleteVertexArrays(IntBuffer var0) {
      GL30.glDeleteVertexArrays(var0);
   }

   public static void glDeleteVertexArrays(int var0) {
      GL30.glDeleteVertexArrays(var0);
   }

   public static void glGenVertexArrays(IntBuffer var0) {
      GL30.glGenVertexArrays(var0);
   }

   public static int glGenVertexArrays() {
      return GL30.glGenVertexArrays();
   }

   public static boolean glIsVertexArray(int var0) {
      return GL30.glIsVertexArray(var0);
   }
}
