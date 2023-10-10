package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBRobustness {
   public static final int GL_NO_ERROR = 0;
   public static final int GL_GUILTY_CONTEXT_RESET_ARB = 33363;
   public static final int GL_INNOCENT_CONTEXT_RESET_ARB = 33364;
   public static final int GL_UNKNOWN_CONTEXT_RESET_ARB = 33365;
   public static final int GL_RESET_NOTIFICATION_STRATEGY_ARB = 33366;
   public static final int GL_LOSE_CONTEXT_ON_RESET_ARB = 33362;
   public static final int GL_NO_RESET_NOTIFICATION_ARB = 33377;
   public static final int GL_CONTEXT_FLAG_ROBUST_ACCESS_BIT_ARB = 4;

   private ARBRobustness() {
   }

   public static int glGetGraphicsResetStatusARB() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGetGraphicsResetStatusARB;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nglGetGraphicsResetStatusARB(var1);
      return var3;
   }

   static native int nglGetGraphicsResetStatusARB(long var0);

   public static void glGetnMapdvARB(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetnMapdvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetnMapdvARB(var0, var1, var2.remaining() << 3, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetnMapdvARB(int var0, int var1, int var2, long var3, long var5);

   public static void glGetnMapfvARB(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetnMapfvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetnMapfvARB(var0, var1, var2.remaining() << 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetnMapfvARB(int var0, int var1, int var2, long var3, long var5);

   public static void glGetnMapivARB(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetnMapivARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetnMapivARB(var0, var1, var2.remaining() << 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetnMapivARB(int var0, int var1, int var2, long var3, long var5);

   public static void glGetnPixelMapfvARB(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetnPixelMapfvARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglGetnPixelMapfvARB(var0, var1.remaining() << 2, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetnPixelMapfvARB(int var0, int var1, long var2, long var4);

   public static void glGetnPixelMapuivARB(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetnPixelMapuivARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglGetnPixelMapuivARB(var0, var1.remaining() << 2, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetnPixelMapuivARB(int var0, int var1, long var2, long var4);

   public static void glGetnPixelMapusvARB(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetnPixelMapusvARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglGetnPixelMapusvARB(var0, var1.remaining() << 1, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetnPixelMapusvARB(int var0, int var1, long var2, long var4);

   public static void glGetnPolygonStippleARB(ByteBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGetnPolygonStippleARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGetnPolygonStippleARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGetnPolygonStippleARB(int var0, long var1, long var3);

   public static void glGetnTexImageARB(int var0, int var1, int var2, int var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetnTexImageARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      nglGetnTexImageARB(var0, var1, var2, var3, var4.remaining(), MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetnTexImageARB(int var0, int var1, int var2, int var3, DoubleBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetnTexImageARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      nglGetnTexImageARB(var0, var1, var2, var3, var4.remaining() << 3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetnTexImageARB(int var0, int var1, int var2, int var3, FloatBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetnTexImageARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      nglGetnTexImageARB(var0, var1, var2, var3, var4.remaining() << 2, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetnTexImageARB(int var0, int var1, int var2, int var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetnTexImageARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      nglGetnTexImageARB(var0, var1, var2, var3, var4.remaining() << 2, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetnTexImageARB(int var0, int var1, int var2, int var3, ShortBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetnTexImageARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      nglGetnTexImageARB(var0, var1, var2, var3, var4.remaining() << 1, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglGetnTexImageARB(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glGetnTexImageARB(int var0, int var1, int var2, int var3, int var4, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glGetnTexImageARB;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensurePackPBOenabled(var7);
      nglGetnTexImageARBBO(var0, var1, var2, var3, var4, var5, var8);
   }

   static native void nglGetnTexImageARBBO(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glReadnPixelsARB(int var0, int var1, int var2, int var3, int var4, int var5, ByteBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glReadnPixelsARB;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensurePackPBOdisabled(var7);
      BufferChecks.checkDirect(var6);
      nglReadnPixelsARB(var0, var1, var2, var3, var4, var5, var6.remaining(), MemoryUtil.getAddress(var6), var8);
   }

   public static void glReadnPixelsARB(int var0, int var1, int var2, int var3, int var4, int var5, DoubleBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glReadnPixelsARB;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensurePackPBOdisabled(var7);
      BufferChecks.checkDirect(var6);
      nglReadnPixelsARB(var0, var1, var2, var3, var4, var5, var6.remaining() << 3, MemoryUtil.getAddress(var6), var8);
   }

   public static void glReadnPixelsARB(int var0, int var1, int var2, int var3, int var4, int var5, FloatBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glReadnPixelsARB;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensurePackPBOdisabled(var7);
      BufferChecks.checkDirect(var6);
      nglReadnPixelsARB(var0, var1, var2, var3, var4, var5, var6.remaining() << 2, MemoryUtil.getAddress(var6), var8);
   }

   public static void glReadnPixelsARB(int var0, int var1, int var2, int var3, int var4, int var5, IntBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glReadnPixelsARB;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensurePackPBOdisabled(var7);
      BufferChecks.checkDirect(var6);
      nglReadnPixelsARB(var0, var1, var2, var3, var4, var5, var6.remaining() << 2, MemoryUtil.getAddress(var6), var8);
   }

   public static void glReadnPixelsARB(int var0, int var1, int var2, int var3, int var4, int var5, ShortBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glReadnPixelsARB;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensurePackPBOdisabled(var7);
      BufferChecks.checkDirect(var6);
      nglReadnPixelsARB(var0, var1, var2, var3, var4, var5, var6.remaining() << 1, MemoryUtil.getAddress(var6), var8);
   }

   static native void nglReadnPixelsARB(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glReadnPixelsARB(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glReadnPixelsARB;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensurePackPBOenabled(var9);
      nglReadnPixelsARBBO(var0, var1, var2, var3, var4, var5, var6, var7, var10);
   }

   static native void nglReadnPixelsARBBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glGetnColorTableARB(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetnColorTableARB;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetnColorTableARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetnColorTableARB(int var0, int var1, int var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetnColorTableARB;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetnColorTableARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetnColorTableARB(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetnColorTableARB;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetnColorTableARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetnColorTableARB(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glGetnConvolutionFilterARB(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetnConvolutionFilterARB;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglGetnConvolutionFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetnConvolutionFilterARB(int var0, int var1, int var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetnConvolutionFilterARB;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglGetnConvolutionFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetnConvolutionFilterARB(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetnConvolutionFilterARB;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglGetnConvolutionFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetnConvolutionFilterARB(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetnConvolutionFilterARB;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglGetnConvolutionFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetnConvolutionFilterARB(int var0, int var1, int var2, ShortBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetnConvolutionFilterARB;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglGetnConvolutionFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetnConvolutionFilterARB(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glGetnConvolutionFilterARB(int var0, int var1, int var2, int var3, long var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnConvolutionFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOenabled(var6);
      nglGetnConvolutionFilterARBBO(var0, var1, var2, var3, var4, var7);
   }

   static native void nglGetnConvolutionFilterARBBO(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, ByteBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, ByteBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, ByteBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, ByteBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, ByteBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, DoubleBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, DoubleBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, DoubleBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, DoubleBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, DoubleBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, FloatBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, FloatBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, FloatBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, FloatBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, FloatBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, IntBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, IntBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, IntBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, IntBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, ShortBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, ShortBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, ShortBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, ShortBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ByteBuffer var3, ShortBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, ByteBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, ByteBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, ByteBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, ByteBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, ByteBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, DoubleBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, DoubleBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, DoubleBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, DoubleBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, DoubleBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, FloatBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, FloatBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, FloatBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, FloatBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, FloatBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, IntBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, IntBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, IntBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, IntBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, ShortBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, ShortBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, ShortBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, ShortBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, DoubleBuffer var3, ShortBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 3, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, ByteBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, ByteBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, ByteBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, ByteBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, ByteBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, DoubleBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, DoubleBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, DoubleBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, DoubleBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, DoubleBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, FloatBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, FloatBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, FloatBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, FloatBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, FloatBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, IntBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, IntBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, IntBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, IntBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, ShortBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, ShortBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, ShortBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, ShortBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, FloatBuffer var3, ShortBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, ByteBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, ByteBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, ByteBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, ByteBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, ByteBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, DoubleBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, DoubleBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, DoubleBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, DoubleBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, DoubleBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, FloatBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, FloatBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, FloatBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, FloatBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, FloatBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, IntBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, IntBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, IntBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, IntBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, ShortBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, ShortBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, ShortBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, ShortBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, IntBuffer var3, ShortBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 2, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, ByteBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, ByteBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, ByteBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, ByteBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, ByteBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining(), MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, DoubleBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, DoubleBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, DoubleBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, DoubleBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, DoubleBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 3, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, FloatBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, FloatBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, FloatBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, FloatBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, FloatBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, IntBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, IntBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, IntBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, IntBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, IntBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 2, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, ShortBuffer var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, ShortBuffer var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, ShortBuffer var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, ShortBuffer var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, ShortBuffer var3, ShortBuffer var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkDirect(var3);
      BufferChecks.checkDirect(var4);
      BufferChecks.checkDirect(var5);
      nglGetnSeparableFilterARB(var0, var1, var2, var3.remaining() << 1, MemoryUtil.getAddress(var3), var4.remaining() << 1, MemoryUtil.getAddress(var4), MemoryUtil.getAddress(var5), var7);
   }

   static native void nglGetnSeparableFilterARB(int var0, int var1, int var2, int var3, long var4, int var6, long var7, long var9, long var11);

   public static void glGetnSeparableFilterARB(int var0, int var1, int var2, int var3, long var4, int var6, long var7, long var9) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glGetnSeparableFilterARB;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensurePackPBOenabled(var11);
      nglGetnSeparableFilterARBBO(var0, var1, var2, var3, var4, var6, var7, var9, var12);
   }

   static native void nglGetnSeparableFilterARBBO(int var0, int var1, int var2, int var3, long var4, int var6, long var7, long var9, long var11);

   public static void glGetnHistogramARB(int var0, boolean var1, int var2, int var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetnHistogramARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      nglGetnHistogramARB(var0, var1, var2, var3, var4.remaining(), MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetnHistogramARB(int var0, boolean var1, int var2, int var3, DoubleBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetnHistogramARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      nglGetnHistogramARB(var0, var1, var2, var3, var4.remaining() << 3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetnHistogramARB(int var0, boolean var1, int var2, int var3, FloatBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetnHistogramARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      nglGetnHistogramARB(var0, var1, var2, var3, var4.remaining() << 2, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetnHistogramARB(int var0, boolean var1, int var2, int var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetnHistogramARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      nglGetnHistogramARB(var0, var1, var2, var3, var4.remaining() << 2, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetnHistogramARB(int var0, boolean var1, int var2, int var3, ShortBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetnHistogramARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      nglGetnHistogramARB(var0, var1, var2, var3, var4.remaining() << 1, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglGetnHistogramARB(int var0, boolean var1, int var2, int var3, int var4, long var5, long var7);

   public static void glGetnHistogramARB(int var0, boolean var1, int var2, int var3, int var4, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glGetnHistogramARB;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensurePackPBOenabled(var7);
      nglGetnHistogramARBBO(var0, var1, var2, var3, var4, var5, var8);
   }

   static native void nglGetnHistogramARBBO(int var0, boolean var1, int var2, int var3, int var4, long var5, long var7);

   public static void glGetnMinmaxARB(int var0, boolean var1, int var2, int var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetnMinmaxARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      nglGetnMinmaxARB(var0, var1, var2, var3, var4.remaining(), MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetnMinmaxARB(int var0, boolean var1, int var2, int var3, DoubleBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetnMinmaxARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      nglGetnMinmaxARB(var0, var1, var2, var3, var4.remaining() << 3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetnMinmaxARB(int var0, boolean var1, int var2, int var3, FloatBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetnMinmaxARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      nglGetnMinmaxARB(var0, var1, var2, var3, var4.remaining() << 2, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetnMinmaxARB(int var0, boolean var1, int var2, int var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetnMinmaxARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      nglGetnMinmaxARB(var0, var1, var2, var3, var4.remaining() << 2, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetnMinmaxARB(int var0, boolean var1, int var2, int var3, ShortBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetnMinmaxARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkDirect(var4);
      nglGetnMinmaxARB(var0, var1, var2, var3, var4.remaining() << 1, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglGetnMinmaxARB(int var0, boolean var1, int var2, int var3, int var4, long var5, long var7);

   public static void glGetnMinmaxARB(int var0, boolean var1, int var2, int var3, int var4, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glGetnMinmaxARB;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensurePackPBOenabled(var7);
      nglGetnMinmaxARBBO(var0, var1, var2, var3, var4, var5, var8);
   }

   static native void nglGetnMinmaxARBBO(int var0, boolean var1, int var2, int var3, int var4, long var5, long var7);

   public static void glGetnCompressedTexImageARB(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetnCompressedTexImageARB;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensurePackPBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      nglGetnCompressedTexImageARB(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   public static void glGetnCompressedTexImageARB(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetnCompressedTexImageARB;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensurePackPBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      nglGetnCompressedTexImageARB(var0, var1, var2.remaining() << 2, MemoryUtil.getAddress(var2), var4);
   }

   public static void glGetnCompressedTexImageARB(int var0, int var1, ShortBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetnCompressedTexImageARB;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensurePackPBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      nglGetnCompressedTexImageARB(var0, var1, var2.remaining() << 1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetnCompressedTexImageARB(int var0, int var1, int var2, long var3, long var5);

   public static void glGetnCompressedTexImageARB(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetnCompressedTexImageARB;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOenabled(var5);
      nglGetnCompressedTexImageARBBO(var0, var1, var2, var3, var6);
   }

   static native void nglGetnCompressedTexImageARBBO(int var0, int var1, int var2, long var3, long var5);

   public static void glGetnUniformfvARB(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetnUniformfvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetnUniformfvARB(var0, var1, var2.remaining() << 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetnUniformfvARB(int var0, int var1, int var2, long var3, long var5);

   public static void glGetnUniformivARB(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetnUniformivARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetnUniformivARB(var0, var1, var2.remaining() << 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetnUniformivARB(int var0, int var1, int var2, long var3, long var5);

   public static void glGetnUniformuivARB(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetnUniformuivARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetnUniformuivARB(var0, var1, var2.remaining() << 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetnUniformuivARB(int var0, int var1, int var2, long var3, long var5);

   public static void glGetnUniformdvARB(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetnUniformdvARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetnUniformdvARB(var0, var1, var2.remaining() << 3, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetnUniformdvARB(int var0, int var1, int var2, long var3, long var5);
}
