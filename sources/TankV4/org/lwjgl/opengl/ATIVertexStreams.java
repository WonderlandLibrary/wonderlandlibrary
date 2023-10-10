package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ATIVertexStreams {
   public static final int GL_MAX_VERTEX_STREAMS_ATI = 34667;
   public static final int GL_VERTEX_SOURCE_ATI = 34668;
   public static final int GL_VERTEX_STREAM0_ATI = 34669;
   public static final int GL_VERTEX_STREAM1_ATI = 34670;
   public static final int GL_VERTEX_STREAM2_ATI = 34671;
   public static final int GL_VERTEX_STREAM3_ATI = 34672;
   public static final int GL_VERTEX_STREAM4_ATI = 34673;
   public static final int GL_VERTEX_STREAM5_ATI = 34674;
   public static final int GL_VERTEX_STREAM6_ATI = 34675;
   public static final int GL_VERTEX_STREAM7_ATI = 34676;

   private ATIVertexStreams() {
   }

   public static void glVertexStream2fATI(int var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexStream2fATI;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexStream2fATI(var0, var1, var2, var4);
   }

   static native void nglVertexStream2fATI(int var0, float var1, float var2, long var3);

   public static void glVertexStream2dATI(int var0, double var1, double var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexStream2dATI;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexStream2dATI(var0, var1, var3, var6);
   }

   static native void nglVertexStream2dATI(int var0, double var1, double var3, long var5);

   public static void glVertexStream2iATI(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexStream2iATI;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexStream2iATI(var0, var1, var2, var4);
   }

   static native void nglVertexStream2iATI(int var0, int var1, int var2, long var3);

   public static void glVertexStream2sATI(int var0, short var1, short var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexStream2sATI;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexStream2sATI(var0, var1, var2, var4);
   }

   static native void nglVertexStream2sATI(int var0, short var1, short var2, long var3);

   public static void glVertexStream3fATI(int var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexStream3fATI;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexStream3fATI(var0, var1, var2, var3, var5);
   }

   static native void nglVertexStream3fATI(int var0, float var1, float var2, float var3, long var4);

   public static void glVertexStream3dATI(int var0, double var1, double var3, double var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glVertexStream3dATI;
      BufferChecks.checkFunctionAddress(var8);
      nglVertexStream3dATI(var0, var1, var3, var5, var8);
   }

   static native void nglVertexStream3dATI(int var0, double var1, double var3, double var5, long var7);

   public static void glVertexStream3iATI(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexStream3iATI;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexStream3iATI(var0, var1, var2, var3, var5);
   }

   static native void nglVertexStream3iATI(int var0, int var1, int var2, int var3, long var4);

   public static void glVertexStream3sATI(int var0, short var1, short var2, short var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexStream3sATI;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexStream3sATI(var0, var1, var2, var3, var5);
   }

   static native void nglVertexStream3sATI(int var0, short var1, short var2, short var3, long var4);

   public static void glVertexStream4fATI(int var0, float var1, float var2, float var3, float var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexStream4fATI;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexStream4fATI(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexStream4fATI(int var0, float var1, float var2, float var3, float var4, long var5);

   public static void glVertexStream4dATI(int var0, double var1, double var3, double var5, double var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glVertexStream4dATI;
      BufferChecks.checkFunctionAddress(var10);
      nglVertexStream4dATI(var0, var1, var3, var5, var7, var10);
   }

   static native void nglVertexStream4dATI(int var0, double var1, double var3, double var5, double var7, long var9);

   public static void glVertexStream4iATI(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexStream4iATI;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexStream4iATI(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexStream4iATI(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glVertexStream4sATI(int var0, short var1, short var2, short var3, short var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexStream4sATI;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexStream4sATI(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexStream4sATI(int var0, short var1, short var2, short var3, short var4, long var5);

   public static void glNormalStream3bATI(int var0, byte var1, byte var2, byte var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNormalStream3bATI;
      BufferChecks.checkFunctionAddress(var5);
      nglNormalStream3bATI(var0, var1, var2, var3, var5);
   }

   static native void nglNormalStream3bATI(int var0, byte var1, byte var2, byte var3, long var4);

   public static void glNormalStream3fATI(int var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNormalStream3fATI;
      BufferChecks.checkFunctionAddress(var5);
      nglNormalStream3fATI(var0, var1, var2, var3, var5);
   }

   static native void nglNormalStream3fATI(int var0, float var1, float var2, float var3, long var4);

   public static void glNormalStream3dATI(int var0, double var1, double var3, double var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glNormalStream3dATI;
      BufferChecks.checkFunctionAddress(var8);
      nglNormalStream3dATI(var0, var1, var3, var5, var8);
   }

   static native void nglNormalStream3dATI(int var0, double var1, double var3, double var5, long var7);

   public static void glNormalStream3iATI(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNormalStream3iATI;
      BufferChecks.checkFunctionAddress(var5);
      nglNormalStream3iATI(var0, var1, var2, var3, var5);
   }

   static native void nglNormalStream3iATI(int var0, int var1, int var2, int var3, long var4);

   public static void glNormalStream3sATI(int var0, short var1, short var2, short var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNormalStream3sATI;
      BufferChecks.checkFunctionAddress(var5);
      nglNormalStream3sATI(var0, var1, var2, var3, var5);
   }

   static native void nglNormalStream3sATI(int var0, short var1, short var2, short var3, long var4);

   public static void glClientActiveVertexStreamATI(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glClientActiveVertexStreamATI;
      BufferChecks.checkFunctionAddress(var2);
      nglClientActiveVertexStreamATI(var0, var2);
   }

   static native void nglClientActiveVertexStreamATI(int var0, long var1);

   public static void glVertexBlendEnvfATI(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexBlendEnvfATI;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexBlendEnvfATI(var0, var1, var3);
   }

   static native void nglVertexBlendEnvfATI(int var0, float var1, long var2);

   public static void glVertexBlendEnviATI(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexBlendEnviATI;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexBlendEnviATI(var0, var1, var3);
   }

   static native void nglVertexBlendEnviATI(int var0, int var1, long var2);
}
