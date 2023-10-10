package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBTextureCompression {
   public static final int GL_COMPRESSED_ALPHA_ARB = 34025;
   public static final int GL_COMPRESSED_LUMINANCE_ARB = 34026;
   public static final int GL_COMPRESSED_LUMINANCE_ALPHA_ARB = 34027;
   public static final int GL_COMPRESSED_INTENSITY_ARB = 34028;
   public static final int GL_COMPRESSED_RGB_ARB = 34029;
   public static final int GL_COMPRESSED_RGBA_ARB = 34030;
   public static final int GL_TEXTURE_COMPRESSION_HINT_ARB = 34031;
   public static final int GL_TEXTURE_COMPRESSED_IMAGE_SIZE_ARB = 34464;
   public static final int GL_TEXTURE_COMPRESSED_ARB = 34465;
   public static final int GL_NUM_COMPRESSED_TEXTURE_FORMATS_ARB = 34466;
   public static final int GL_COMPRESSED_TEXTURE_FORMATS_ARB = 34467;

   private ARBTextureCompression() {
   }

   public static void glCompressedTexImage1DARB(int var0, int var1, int var2, int var3, int var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glCompressedTexImage1DARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureUnpackPBOdisabled(var6);
      BufferChecks.checkDirect(var5);
      nglCompressedTexImage1DARB(var0, var1, var2, var3, var4, var5.remaining(), MemoryUtil.getAddress(var5), var7);
   }

   static native void nglCompressedTexImage1DARB(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8);

   public static void glCompressedTexImage1DARB(int var0, int var1, int var2, int var3, int var4, int var5, long var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glCompressedTexImage1DARB;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOenabled(var8);
      nglCompressedTexImage1DARBBO(var0, var1, var2, var3, var4, var5, var6, var9);
   }

   static native void nglCompressedTexImage1DARBBO(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8);

   public static void glCompressedTexImage2DARB(int var0, int var1, int var2, int var3, int var4, int var5, ByteBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glCompressedTexImage2DARB;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOdisabled(var7);
      BufferChecks.checkDirect(var6);
      nglCompressedTexImage2DARB(var0, var1, var2, var3, var4, var5, var6.remaining(), MemoryUtil.getAddress(var6), var8);
   }

   static native void nglCompressedTexImage2DARB(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glCompressedTexImage2DARB(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glCompressedTexImage2DARB;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOenabled(var9);
      nglCompressedTexImage2DARBBO(var0, var1, var2, var3, var4, var5, var6, var7, var10);
   }

   static native void nglCompressedTexImage2DARBBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glCompressedTexImage3DARB(int var0, int var1, int var2, int var3, int var4, int var5, int var6, ByteBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glCompressedTexImage3DARB;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var7);
      nglCompressedTexImage3DARB(var0, var1, var2, var3, var4, var5, var6, var7.remaining(), MemoryUtil.getAddress(var7), var9);
   }

   static native void nglCompressedTexImage3DARB(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glCompressedTexImage3DARB(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glCompressedTexImage3DARB;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOenabled(var10);
      nglCompressedTexImage3DARBBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var11);
   }

   static native void nglCompressedTexImage3DARBBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glCompressedTexSubImage1DARB(int var0, int var1, int var2, int var3, int var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glCompressedTexSubImage1DARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureUnpackPBOdisabled(var6);
      BufferChecks.checkDirect(var5);
      nglCompressedTexSubImage1DARB(var0, var1, var2, var3, var4, var5.remaining(), MemoryUtil.getAddress(var5), var7);
   }

   static native void nglCompressedTexSubImage1DARB(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8);

   public static void glCompressedTexSubImage1DARB(int var0, int var1, int var2, int var3, int var4, int var5, long var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glCompressedTexSubImage1DARB;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOenabled(var8);
      nglCompressedTexSubImage1DARBBO(var0, var1, var2, var3, var4, var5, var6, var9);
   }

   static native void nglCompressedTexSubImage1DARBBO(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8);

   public static void glCompressedTexSubImage2DARB(int var0, int var1, int var2, int var3, int var4, int var5, int var6, ByteBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glCompressedTexSubImage2DARB;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var7);
      nglCompressedTexSubImage2DARB(var0, var1, var2, var3, var4, var5, var6, var7.remaining(), MemoryUtil.getAddress(var7), var9);
   }

   static native void nglCompressedTexSubImage2DARB(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glCompressedTexSubImage2DARB(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glCompressedTexSubImage2DARB;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOenabled(var10);
      nglCompressedTexSubImage2DARBBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var11);
   }

   static native void nglCompressedTexSubImage2DARBBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glCompressedTexSubImage3DARB(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, ByteBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glCompressedTexSubImage3DARB;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      BufferChecks.checkDirect(var9);
      nglCompressedTexSubImage3DARB(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9.remaining(), MemoryUtil.getAddress(var9), var11);
   }

   static native void nglCompressedTexSubImage3DARB(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10, long var12);

   public static void glCompressedTexSubImage3DARB(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glCompressedTexSubImage3DARB;
      BufferChecks.checkFunctionAddress(var13);
      GLChecks.ensureUnpackPBOenabled(var12);
      nglCompressedTexSubImage3DARBBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var13);
   }

   static native void nglCompressedTexSubImage3DARBBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10, long var12);

   public static void glGetCompressedTexImageARB(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetCompressedTexImageARB;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensurePackPBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      nglGetCompressedTexImageARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetCompressedTexImageARB(int var0, int var1, long var2, long var4);

   public static void glGetCompressedTexImageARB(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetCompressedTexImageARB;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOenabled(var4);
      nglGetCompressedTexImageARBBO(var0, var1, var2, var5);
   }

   static native void nglGetCompressedTexImageARBBO(int var0, int var1, long var2, long var4);
}
