package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class ARBMultiDrawIndirect {
   private ARBMultiDrawIndirect() {
   }

   public static void glMultiDrawArraysIndirect(int var0, ByteBuffer var1, int var2, int var3) {
      GL43.glMultiDrawArraysIndirect(var0, var1, var2, var3);
   }

   public static void glMultiDrawArraysIndirect(int var0, long var1, int var3, int var4) {
      GL43.glMultiDrawArraysIndirect(var0, var1, var3, var4);
   }

   public static void glMultiDrawArraysIndirect(int var0, IntBuffer var1, int var2, int var3) {
      GL43.glMultiDrawArraysIndirect(var0, var1, var2, var3);
   }

   public static void glMultiDrawElementsIndirect(int var0, int var1, ByteBuffer var2, int var3, int var4) {
      GL43.glMultiDrawElementsIndirect(var0, var1, var2, var3, var4);
   }

   public static void glMultiDrawElementsIndirect(int var0, int var1, long var2, int var4, int var5) {
      GL43.glMultiDrawElementsIndirect(var0, var1, var2, var4, var5);
   }

   public static void glMultiDrawElementsIndirect(int var0, int var1, IntBuffer var2, int var3, int var4) {
      GL43.glMultiDrawElementsIndirect(var0, var1, var2, var3, var4);
   }
}
