package org.lwjgl.opengl;

import java.nio.IntBuffer;

public final class ARBInvalidateSubdata {
   private ARBInvalidateSubdata() {
   }

   public static void glInvalidateTexSubImage(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      GL43.glInvalidateTexSubImage(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   public static void glInvalidateTexImage(int var0, int var1) {
      GL43.glInvalidateTexImage(var0, var1);
   }

   public static void glInvalidateBufferSubData(int var0, long var1, long var3) {
      GL43.glInvalidateBufferSubData(var0, var1, var3);
   }

   public static void glInvalidateBufferData(int var0) {
      GL43.glInvalidateBufferData(var0);
   }

   public static void glInvalidateFramebuffer(int var0, IntBuffer var1) {
      GL43.glInvalidateFramebuffer(var0, var1);
   }

   public static void glInvalidateSubFramebuffer(int var0, IntBuffer var1, int var2, int var3, int var4, int var5) {
      GL43.glInvalidateSubFramebuffer(var0, var1, var2, var3, var4, var5);
   }
}
