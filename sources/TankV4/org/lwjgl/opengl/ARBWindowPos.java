package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBWindowPos {
   private ARBWindowPos() {
   }

   public static void glWindowPos2fARB(float var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glWindowPos2fARB;
      BufferChecks.checkFunctionAddress(var3);
      nglWindowPos2fARB(var0, var1, var3);
   }

   static native void nglWindowPos2fARB(float var0, float var1, long var2);

   public static void glWindowPos2dARB(double var0, double var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glWindowPos2dARB;
      BufferChecks.checkFunctionAddress(var5);
      nglWindowPos2dARB(var0, var2, var5);
   }

   static native void nglWindowPos2dARB(double var0, double var2, long var4);

   public static void glWindowPos2iARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glWindowPos2iARB;
      BufferChecks.checkFunctionAddress(var3);
      nglWindowPos2iARB(var0, var1, var3);
   }

   static native void nglWindowPos2iARB(int var0, int var1, long var2);

   public static void glWindowPos2sARB(short var0, short var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glWindowPos2sARB;
      BufferChecks.checkFunctionAddress(var3);
      nglWindowPos2sARB(var0, var1, var3);
   }

   static native void nglWindowPos2sARB(short var0, short var1, long var2);

   public static void glWindowPos3fARB(float var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glWindowPos3fARB;
      BufferChecks.checkFunctionAddress(var4);
      nglWindowPos3fARB(var0, var1, var2, var4);
   }

   static native void nglWindowPos3fARB(float var0, float var1, float var2, long var3);

   public static void glWindowPos3dARB(double var0, double var2, double var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glWindowPos3dARB;
      BufferChecks.checkFunctionAddress(var7);
      nglWindowPos3dARB(var0, var2, var4, var7);
   }

   static native void nglWindowPos3dARB(double var0, double var2, double var4, long var6);

   public static void glWindowPos3iARB(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glWindowPos3iARB;
      BufferChecks.checkFunctionAddress(var4);
      nglWindowPos3iARB(var0, var1, var2, var4);
   }

   static native void nglWindowPos3iARB(int var0, int var1, int var2, long var3);

   public static void glWindowPos3sARB(short var0, short var1, short var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glWindowPos3sARB;
      BufferChecks.checkFunctionAddress(var4);
      nglWindowPos3sARB(var0, var1, var2, var4);
   }

   static native void nglWindowPos3sARB(short var0, short var1, short var2, long var3);
}
