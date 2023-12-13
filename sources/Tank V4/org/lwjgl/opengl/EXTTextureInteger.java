package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class EXTTextureInteger {
   public static final int GL_RGBA_INTEGER_MODE_EXT = 36254;
   public static final int GL_RGBA32UI_EXT = 36208;
   public static final int GL_RGB32UI_EXT = 36209;
   public static final int GL_ALPHA32UI_EXT = 36210;
   public static final int GL_INTENSITY32UI_EXT = 36211;
   public static final int GL_LUMINANCE32UI_EXT = 36212;
   public static final int GL_LUMINANCE_ALPHA32UI_EXT = 36213;
   public static final int GL_RGBA16UI_EXT = 36214;
   public static final int GL_RGB16UI_EXT = 36215;
   public static final int GL_ALPHA16UI_EXT = 36216;
   public static final int GL_INTENSITY16UI_EXT = 36217;
   public static final int GL_LUMINANCE16UI_EXT = 36218;
   public static final int GL_LUMINANCE_ALPHA16UI_EXT = 36219;
   public static final int GL_RGBA8UI_EXT = 36220;
   public static final int GL_RGB8UI_EXT = 36221;
   public static final int GL_ALPHA8UI_EXT = 36222;
   public static final int GL_INTENSITY8UI_EXT = 36223;
   public static final int GL_LUMINANCE8UI_EXT = 36224;
   public static final int GL_LUMINANCE_ALPHA8UI_EXT = 36225;
   public static final int GL_RGBA32I_EXT = 36226;
   public static final int GL_RGB32I_EXT = 36227;
   public static final int GL_ALPHA32I_EXT = 36228;
   public static final int GL_INTENSITY32I_EXT = 36229;
   public static final int GL_LUMINANCE32I_EXT = 36230;
   public static final int GL_LUMINANCE_ALPHA32I_EXT = 36231;
   public static final int GL_RGBA16I_EXT = 36232;
   public static final int GL_RGB16I_EXT = 36233;
   public static final int GL_ALPHA16I_EXT = 36234;
   public static final int GL_INTENSITY16I_EXT = 36235;
   public static final int GL_LUMINANCE16I_EXT = 36236;
   public static final int GL_LUMINANCE_ALPHA16I_EXT = 36237;
   public static final int GL_RGBA8I_EXT = 36238;
   public static final int GL_RGB8I_EXT = 36239;
   public static final int GL_ALPHA8I_EXT = 36240;
   public static final int GL_INTENSITY8I_EXT = 36241;
   public static final int GL_LUMINANCE8I_EXT = 36242;
   public static final int GL_LUMINANCE_ALPHA8I_EXT = 36243;
   public static final int GL_RED_INTEGER_EXT = 36244;
   public static final int GL_GREEN_INTEGER_EXT = 36245;
   public static final int GL_BLUE_INTEGER_EXT = 36246;
   public static final int GL_ALPHA_INTEGER_EXT = 36247;
   public static final int GL_RGB_INTEGER_EXT = 36248;
   public static final int GL_RGBA_INTEGER_EXT = 36249;
   public static final int GL_BGR_INTEGER_EXT = 36250;
   public static final int GL_BGRA_INTEGER_EXT = 36251;
   public static final int GL_LUMINANCE_INTEGER_EXT = 36252;
   public static final int GL_LUMINANCE_ALPHA_INTEGER_EXT = 36253;

   private EXTTextureInteger() {
   }

   public static void glClearColorIiEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glClearColorIiEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglClearColorIiEXT(var0, var1, var2, var3, var5);
   }

   static native void nglClearColorIiEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glClearColorIuiEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glClearColorIuiEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglClearColorIuiEXT(var0, var1, var2, var3, var5);
   }

   static native void nglClearColorIuiEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glTexParameterIEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexParameterIivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglTexParameterIivEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglTexParameterIivEXT(int var0, int var1, long var2, long var4);

   public static void glTexParameterIiEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexParameterIivEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglTexParameterIivEXT(var0, var1, APIUtil.getInt(var3, var2), var4);
   }

   public static void glTexParameterIuEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexParameterIuivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglTexParameterIuivEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglTexParameterIuivEXT(int var0, int var1, long var2, long var4);

   public static void glTexParameterIuiEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexParameterIuivEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglTexParameterIuivEXT(var0, var1, APIUtil.getInt(var3, var2), var4);
   }

   public static void glGetTexParameterIEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTexParameterIivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetTexParameterIivEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetTexParameterIivEXT(int var0, int var1, long var2, long var4);

   public static int glGetTexParameterIiEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetTexParameterIivEXT;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetTexParameterIivEXT(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetTexParameterIuEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTexParameterIuivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetTexParameterIuivEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetTexParameterIuivEXT(int var0, int var1, long var2, long var4);

   public static int glGetTexParameterIuiEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetTexParameterIuivEXT;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetTexParameterIuivEXT(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }
}
