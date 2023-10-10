package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

public final class ARBClearTexture {
   public static final int GL_CLEAR_TEXTURE = 37733;

   private ARBClearTexture() {
   }

   public static void glClearTexImage(int var0, int var1, int var2, int var3, ByteBuffer var4) {
      GL44.glClearTexImage(var0, var1, var2, var3, var4);
   }

   public static void glClearTexImage(int var0, int var1, int var2, int var3, DoubleBuffer var4) {
      GL44.glClearTexImage(var0, var1, var2, var3, var4);
   }

   public static void glClearTexImage(int var0, int var1, int var2, int var3, FloatBuffer var4) {
      GL44.glClearTexImage(var0, var1, var2, var3, var4);
   }

   public static void glClearTexImage(int var0, int var1, int var2, int var3, IntBuffer var4) {
      GL44.glClearTexImage(var0, var1, var2, var3, var4);
   }

   public static void glClearTexImage(int var0, int var1, int var2, int var3, ShortBuffer var4) {
      GL44.glClearTexImage(var0, var1, var2, var3, var4);
   }

   public static void glClearTexImage(int var0, int var1, int var2, int var3, LongBuffer var4) {
      GL44.glClearTexImage(var0, var1, var2, var3, var4);
   }

   public static void glClearTexSubImage(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, ByteBuffer var10) {
      GL44.glClearTexSubImage(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   public static void glClearTexSubImage(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, DoubleBuffer var10) {
      GL44.glClearTexSubImage(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   public static void glClearTexSubImage(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, FloatBuffer var10) {
      GL44.glClearTexSubImage(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   public static void glClearTexSubImage(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, IntBuffer var10) {
      GL44.glClearTexSubImage(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   public static void glClearTexSubImage(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, ShortBuffer var10) {
      GL44.glClearTexSubImage(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   public static void glClearTexSubImage(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, LongBuffer var10) {
      GL44.glClearTexSubImage(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }
}
