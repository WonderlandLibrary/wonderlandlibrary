package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public final class ARBDrawElementsBaseVertex {
   private ARBDrawElementsBaseVertex() {
   }

   public static void glDrawElementsBaseVertex(int var0, ByteBuffer var1, int var2) {
      GL32.glDrawElementsBaseVertex(var0, var1, var2);
   }

   public static void glDrawElementsBaseVertex(int var0, IntBuffer var1, int var2) {
      GL32.glDrawElementsBaseVertex(var0, var1, var2);
   }

   public static void glDrawElementsBaseVertex(int var0, ShortBuffer var1, int var2) {
      GL32.glDrawElementsBaseVertex(var0, var1, var2);
   }

   public static void glDrawElementsBaseVertex(int var0, int var1, int var2, long var3, int var5) {
      GL32.glDrawElementsBaseVertex(var0, var1, var2, var3, var5);
   }

   public static void glDrawRangeElementsBaseVertex(int var0, int var1, int var2, ByteBuffer var3, int var4) {
      GL32.glDrawRangeElementsBaseVertex(var0, var1, var2, var3, var4);
   }

   public static void glDrawRangeElementsBaseVertex(int var0, int var1, int var2, IntBuffer var3, int var4) {
      GL32.glDrawRangeElementsBaseVertex(var0, var1, var2, var3, var4);
   }

   public static void glDrawRangeElementsBaseVertex(int var0, int var1, int var2, ShortBuffer var3, int var4) {
      GL32.glDrawRangeElementsBaseVertex(var0, var1, var2, var3, var4);
   }

   public static void glDrawRangeElementsBaseVertex(int var0, int var1, int var2, int var3, int var4, long var5, int var7) {
      GL32.glDrawRangeElementsBaseVertex(var0, var1, var2, var3, var4, var5, var7);
   }

   public static void glDrawElementsInstancedBaseVertex(int var0, ByteBuffer var1, int var2, int var3) {
      GL32.glDrawElementsInstancedBaseVertex(var0, var1, var2, var3);
   }

   public static void glDrawElementsInstancedBaseVertex(int var0, IntBuffer var1, int var2, int var3) {
      GL32.glDrawElementsInstancedBaseVertex(var0, var1, var2, var3);
   }

   public static void glDrawElementsInstancedBaseVertex(int var0, ShortBuffer var1, int var2, int var3) {
      GL32.glDrawElementsInstancedBaseVertex(var0, var1, var2, var3);
   }

   public static void glDrawElementsInstancedBaseVertex(int var0, int var1, int var2, long var3, int var5, int var6) {
      GL32.glDrawElementsInstancedBaseVertex(var0, var1, var2, var3, var5, var6);
   }
}
