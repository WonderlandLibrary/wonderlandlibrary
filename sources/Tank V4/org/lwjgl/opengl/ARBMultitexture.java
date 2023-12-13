package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBMultitexture {
   public static final int GL_TEXTURE0_ARB = 33984;
   public static final int GL_TEXTURE1_ARB = 33985;
   public static final int GL_TEXTURE2_ARB = 33986;
   public static final int GL_TEXTURE3_ARB = 33987;
   public static final int GL_TEXTURE4_ARB = 33988;
   public static final int GL_TEXTURE5_ARB = 33989;
   public static final int GL_TEXTURE6_ARB = 33990;
   public static final int GL_TEXTURE7_ARB = 33991;
   public static final int GL_TEXTURE8_ARB = 33992;
   public static final int GL_TEXTURE9_ARB = 33993;
   public static final int GL_TEXTURE10_ARB = 33994;
   public static final int GL_TEXTURE11_ARB = 33995;
   public static final int GL_TEXTURE12_ARB = 33996;
   public static final int GL_TEXTURE13_ARB = 33997;
   public static final int GL_TEXTURE14_ARB = 33998;
   public static final int GL_TEXTURE15_ARB = 33999;
   public static final int GL_TEXTURE16_ARB = 34000;
   public static final int GL_TEXTURE17_ARB = 34001;
   public static final int GL_TEXTURE18_ARB = 34002;
   public static final int GL_TEXTURE19_ARB = 34003;
   public static final int GL_TEXTURE20_ARB = 34004;
   public static final int GL_TEXTURE21_ARB = 34005;
   public static final int GL_TEXTURE22_ARB = 34006;
   public static final int GL_TEXTURE23_ARB = 34007;
   public static final int GL_TEXTURE24_ARB = 34008;
   public static final int GL_TEXTURE25_ARB = 34009;
   public static final int GL_TEXTURE26_ARB = 34010;
   public static final int GL_TEXTURE27_ARB = 34011;
   public static final int GL_TEXTURE28_ARB = 34012;
   public static final int GL_TEXTURE29_ARB = 34013;
   public static final int GL_TEXTURE30_ARB = 34014;
   public static final int GL_TEXTURE31_ARB = 34015;
   public static final int GL_ACTIVE_TEXTURE_ARB = 34016;
   public static final int GL_CLIENT_ACTIVE_TEXTURE_ARB = 34017;
   public static final int GL_MAX_TEXTURE_UNITS_ARB = 34018;

   private ARBMultitexture() {
   }

   public static void glClientActiveTextureARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glClientActiveTextureARB;
      BufferChecks.checkFunctionAddress(var2);
      nglClientActiveTextureARB(var0, var2);
   }

   static native void nglClientActiveTextureARB(int var0, long var1);

   public static void glActiveTextureARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glActiveTextureARB;
      BufferChecks.checkFunctionAddress(var2);
      nglActiveTextureARB(var0, var2);
   }

   static native void nglActiveTextureARB(int var0, long var1);

   public static void glMultiTexCoord1fARB(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMultiTexCoord1fARB;
      BufferChecks.checkFunctionAddress(var3);
      nglMultiTexCoord1fARB(var0, var1, var3);
   }

   static native void nglMultiTexCoord1fARB(int var0, float var1, long var2);

   public static void glMultiTexCoord1dARB(int var0, double var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiTexCoord1dARB;
      BufferChecks.checkFunctionAddress(var4);
      nglMultiTexCoord1dARB(var0, var1, var4);
   }

   static native void nglMultiTexCoord1dARB(int var0, double var1, long var3);

   public static void glMultiTexCoord1iARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMultiTexCoord1iARB;
      BufferChecks.checkFunctionAddress(var3);
      nglMultiTexCoord1iARB(var0, var1, var3);
   }

   static native void nglMultiTexCoord1iARB(int var0, int var1, long var2);

   public static void glMultiTexCoord1sARB(int var0, short var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMultiTexCoord1sARB;
      BufferChecks.checkFunctionAddress(var3);
      nglMultiTexCoord1sARB(var0, var1, var3);
   }

   static native void nglMultiTexCoord1sARB(int var0, short var1, long var2);

   public static void glMultiTexCoord2fARB(int var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiTexCoord2fARB;
      BufferChecks.checkFunctionAddress(var4);
      nglMultiTexCoord2fARB(var0, var1, var2, var4);
   }

   static native void nglMultiTexCoord2fARB(int var0, float var1, float var2, long var3);

   public static void glMultiTexCoord2dARB(int var0, double var1, double var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMultiTexCoord2dARB;
      BufferChecks.checkFunctionAddress(var6);
      nglMultiTexCoord2dARB(var0, var1, var3, var6);
   }

   static native void nglMultiTexCoord2dARB(int var0, double var1, double var3, long var5);

   public static void glMultiTexCoord2iARB(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiTexCoord2iARB;
      BufferChecks.checkFunctionAddress(var4);
      nglMultiTexCoord2iARB(var0, var1, var2, var4);
   }

   static native void nglMultiTexCoord2iARB(int var0, int var1, int var2, long var3);

   public static void glMultiTexCoord2sARB(int var0, short var1, short var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiTexCoord2sARB;
      BufferChecks.checkFunctionAddress(var4);
      nglMultiTexCoord2sARB(var0, var1, var2, var4);
   }

   static native void nglMultiTexCoord2sARB(int var0, short var1, short var2, long var3);

   public static void glMultiTexCoord3fARB(int var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexCoord3fARB;
      BufferChecks.checkFunctionAddress(var5);
      nglMultiTexCoord3fARB(var0, var1, var2, var3, var5);
   }

   static native void nglMultiTexCoord3fARB(int var0, float var1, float var2, float var3, long var4);

   public static void glMultiTexCoord3dARB(int var0, double var1, double var3, double var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glMultiTexCoord3dARB;
      BufferChecks.checkFunctionAddress(var8);
      nglMultiTexCoord3dARB(var0, var1, var3, var5, var8);
   }

   static native void nglMultiTexCoord3dARB(int var0, double var1, double var3, double var5, long var7);

   public static void glMultiTexCoord3iARB(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexCoord3iARB;
      BufferChecks.checkFunctionAddress(var5);
      nglMultiTexCoord3iARB(var0, var1, var2, var3, var5);
   }

   static native void nglMultiTexCoord3iARB(int var0, int var1, int var2, int var3, long var4);

   public static void glMultiTexCoord3sARB(int var0, short var1, short var2, short var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexCoord3sARB;
      BufferChecks.checkFunctionAddress(var5);
      nglMultiTexCoord3sARB(var0, var1, var2, var3, var5);
   }

   static native void nglMultiTexCoord3sARB(int var0, short var1, short var2, short var3, long var4);

   public static void glMultiTexCoord4fARB(int var0, float var1, float var2, float var3, float var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMultiTexCoord4fARB;
      BufferChecks.checkFunctionAddress(var6);
      nglMultiTexCoord4fARB(var0, var1, var2, var3, var4, var6);
   }

   static native void nglMultiTexCoord4fARB(int var0, float var1, float var2, float var3, float var4, long var5);

   public static void glMultiTexCoord4dARB(int var0, double var1, double var3, double var5, double var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glMultiTexCoord4dARB;
      BufferChecks.checkFunctionAddress(var10);
      nglMultiTexCoord4dARB(var0, var1, var3, var5, var7, var10);
   }

   static native void nglMultiTexCoord4dARB(int var0, double var1, double var3, double var5, double var7, long var9);

   public static void glMultiTexCoord4iARB(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMultiTexCoord4iARB;
      BufferChecks.checkFunctionAddress(var6);
      nglMultiTexCoord4iARB(var0, var1, var2, var3, var4, var6);
   }

   static native void nglMultiTexCoord4iARB(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glMultiTexCoord4sARB(int var0, short var1, short var2, short var3, short var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMultiTexCoord4sARB;
      BufferChecks.checkFunctionAddress(var6);
      nglMultiTexCoord4sARB(var0, var1, var2, var3, var4, var6);
   }

   static native void nglMultiTexCoord4sARB(int var0, short var1, short var2, short var3, short var4, long var5);
}
