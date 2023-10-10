package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.PointerBuffer;

public final class ARBMultiBind {
   private ARBMultiBind() {
   }

   public static void glBindBuffersBase(int var0, int var1, int var2, IntBuffer var3) {
      GL44.glBindBuffersBase(var0, var1, var2, var3);
   }

   public static void glBindBuffersRange(int var0, int var1, int var2, IntBuffer var3, PointerBuffer var4, PointerBuffer var5) {
      GL44.glBindBuffersRange(var0, var1, var2, var3, var4, var5);
   }

   public static void glBindTextures(int var0, int var1, IntBuffer var2) {
      GL44.glBindTextures(var0, var1, var2);
   }

   public static void glBindSamplers(int var0, int var1, IntBuffer var2) {
      GL44.glBindSamplers(var0, var1, var2);
   }

   public static void glBindImageTextures(int var0, int var1, IntBuffer var2) {
      GL44.glBindImageTextures(var0, var1, var2);
   }

   public static void glBindVertexBuffers(int var0, int var1, IntBuffer var2, PointerBuffer var3, IntBuffer var4) {
      GL44.glBindVertexBuffers(var0, var1, var2, var3, var4);
   }
}
