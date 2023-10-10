package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public final class ARBBaseInstance {
   private ARBBaseInstance() {
   }

   public static void glDrawArraysInstancedBaseInstance(int var0, int var1, int var2, int var3, int var4) {
      GL42.glDrawArraysInstancedBaseInstance(var0, var1, var2, var3, var4);
   }

   public static void glDrawElementsInstancedBaseInstance(int var0, ByteBuffer var1, int var2, int var3) {
      GL42.glDrawElementsInstancedBaseInstance(var0, var1, var2, var3);
   }

   public static void glDrawElementsInstancedBaseInstance(int var0, IntBuffer var1, int var2, int var3) {
      GL42.glDrawElementsInstancedBaseInstance(var0, var1, var2, var3);
   }

   public static void glDrawElementsInstancedBaseInstance(int var0, ShortBuffer var1, int var2, int var3) {
      GL42.glDrawElementsInstancedBaseInstance(var0, var1, var2, var3);
   }

   public static void glDrawElementsInstancedBaseInstance(int var0, int var1, int var2, long var3, int var5, int var6) {
      GL42.glDrawElementsInstancedBaseInstance(var0, var1, var2, var3, var5, var6);
   }

   public static void glDrawElementsInstancedBaseVertexBaseInstance(int var0, ByteBuffer var1, int var2, int var3, int var4) {
      GL42.glDrawElementsInstancedBaseVertexBaseInstance(var0, var1, var2, var3, var4);
   }

   public static void glDrawElementsInstancedBaseVertexBaseInstance(int var0, IntBuffer var1, int var2, int var3, int var4) {
      GL42.glDrawElementsInstancedBaseVertexBaseInstance(var0, var1, var2, var3, var4);
   }

   public static void glDrawElementsInstancedBaseVertexBaseInstance(int var0, ShortBuffer var1, int var2, int var3, int var4) {
      GL42.glDrawElementsInstancedBaseVertexBaseInstance(var0, var1, var2, var3, var4);
   }

   public static void glDrawElementsInstancedBaseVertexBaseInstance(int var0, int var1, int var2, long var3, int var5, int var6, int var7) {
      GL42.glDrawElementsInstancedBaseVertexBaseInstance(var0, var1, var2, var3, var5, var6, var7);
   }
}
