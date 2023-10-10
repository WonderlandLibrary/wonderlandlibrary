package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class EXTPalettedTexture {
   public static final int GL_COLOR_INDEX1_EXT = 32994;
   public static final int GL_COLOR_INDEX2_EXT = 32995;
   public static final int GL_COLOR_INDEX4_EXT = 32996;
   public static final int GL_COLOR_INDEX8_EXT = 32997;
   public static final int GL_COLOR_INDEX12_EXT = 32998;
   public static final int GL_COLOR_INDEX16_EXT = 32999;
   public static final int GL_COLOR_TABLE_FORMAT_EXT = 32984;
   public static final int GL_COLOR_TABLE_WIDTH_EXT = 32985;
   public static final int GL_COLOR_TABLE_RED_SIZE_EXT = 32986;
   public static final int GL_COLOR_TABLE_GREEN_SIZE_EXT = 32987;
   public static final int GL_COLOR_TABLE_BLUE_SIZE_EXT = 32988;
   public static final int GL_COLOR_TABLE_ALPHA_SIZE_EXT = 32989;
   public static final int GL_COLOR_TABLE_LUMINANCE_SIZE_EXT = 32990;
   public static final int GL_COLOR_TABLE_INTENSITY_SIZE_EXT = 32991;
   public static final int GL_TEXTURE_INDEX_SIZE_EXT = 33005;

   private EXTPalettedTexture() {
   }

   public static void glColorTableEXT(int var0, int var1, int var2, int var3, int var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glColorTableEXT;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, var2, 1, 1));
      nglColorTableEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glColorTableEXT(int var0, int var1, int var2, int var3, int var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glColorTableEXT;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, var2, 1, 1));
      nglColorTableEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glColorTableEXT(int var0, int var1, int var2, int var3, int var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glColorTableEXT;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, var2, 1, 1));
      nglColorTableEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glColorTableEXT(int var0, int var1, int var2, int var3, int var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glColorTableEXT;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, var2, 1, 1));
      nglColorTableEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glColorTableEXT(int var0, int var1, int var2, int var3, int var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glColorTableEXT;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, var2, 1, 1));
      nglColorTableEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   static native void nglColorTableEXT(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glColorSubTableEXT(int var0, int var1, int var2, int var3, int var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glColorSubTableEXT;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, var2, 1, 1));
      nglColorSubTableEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glColorSubTableEXT(int var0, int var1, int var2, int var3, int var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glColorSubTableEXT;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, var2, 1, 1));
      nglColorSubTableEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glColorSubTableEXT(int var0, int var1, int var2, int var3, int var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glColorSubTableEXT;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, var2, 1, 1));
      nglColorSubTableEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glColorSubTableEXT(int var0, int var1, int var2, int var3, int var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glColorSubTableEXT;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, var2, 1, 1));
      nglColorSubTableEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glColorSubTableEXT(int var0, int var1, int var2, int var3, int var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glColorSubTableEXT;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, var2, 1, 1));
      nglColorSubTableEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   static native void nglColorSubTableEXT(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glGetColorTableEXT(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetColorTableEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetColorTableEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetColorTableEXT(int var0, int var1, int var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetColorTableEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetColorTableEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetColorTableEXT(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetColorTableEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetColorTableEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetColorTableEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetColorTableEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetColorTableEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetColorTableEXT(int var0, int var1, int var2, ShortBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetColorTableEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetColorTableEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetColorTableEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glGetColorTableParameterEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetColorTableParameterivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetColorTableParameterivEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetColorTableParameterivEXT(int var0, int var1, long var2, long var4);

   public static void glGetColorTableParameterEXT(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetColorTableParameterfvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetColorTableParameterfvEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetColorTableParameterfvEXT(int var0, int var1, long var2, long var4);
}
