package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class EXTDirectStateAccess {
   public static final int GL_PROGRAM_MATRIX_EXT = 36397;
   public static final int GL_TRANSPOSE_PROGRAM_MATRIX_EXT = 36398;
   public static final int GL_PROGRAM_MATRIX_STACK_DEPTH_EXT = 36399;

   private EXTDirectStateAccess() {
   }

   public static void glClientAttribDefaultEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glClientAttribDefaultEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglClientAttribDefaultEXT(var0, var2);
   }

   static native void nglClientAttribDefaultEXT(int var0, long var1);

   public static void glPushClientAttribDefaultEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glPushClientAttribDefaultEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglPushClientAttribDefaultEXT(var0, var2);
   }

   static native void nglPushClientAttribDefaultEXT(int var0, long var1);

   public static void glMatrixLoadEXT(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMatrixLoadfEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 16);
      nglMatrixLoadfEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglMatrixLoadfEXT(int var0, long var1, long var3);

   public static void glMatrixLoadEXT(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMatrixLoaddEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 16);
      nglMatrixLoaddEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglMatrixLoaddEXT(int var0, long var1, long var3);

   public static void glMatrixMultEXT(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMatrixMultfEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 16);
      nglMatrixMultfEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglMatrixMultfEXT(int var0, long var1, long var3);

   public static void glMatrixMultEXT(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMatrixMultdEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 16);
      nglMatrixMultdEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglMatrixMultdEXT(int var0, long var1, long var3);

   public static void glMatrixLoadIdentityEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glMatrixLoadIdentityEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglMatrixLoadIdentityEXT(var0, var2);
   }

   static native void nglMatrixLoadIdentityEXT(int var0, long var1);

   public static void glMatrixRotatefEXT(int var0, float var1, float var2, float var3, float var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMatrixRotatefEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglMatrixRotatefEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglMatrixRotatefEXT(int var0, float var1, float var2, float var3, float var4, long var5);

   public static void glMatrixRotatedEXT(int var0, double var1, double var3, double var5, double var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glMatrixRotatedEXT;
      BufferChecks.checkFunctionAddress(var10);
      nglMatrixRotatedEXT(var0, var1, var3, var5, var7, var10);
   }

   static native void nglMatrixRotatedEXT(int var0, double var1, double var3, double var5, double var7, long var9);

   public static void glMatrixScalefEXT(int var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMatrixScalefEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglMatrixScalefEXT(var0, var1, var2, var3, var5);
   }

   static native void nglMatrixScalefEXT(int var0, float var1, float var2, float var3, long var4);

   public static void glMatrixScaledEXT(int var0, double var1, double var3, double var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glMatrixScaledEXT;
      BufferChecks.checkFunctionAddress(var8);
      nglMatrixScaledEXT(var0, var1, var3, var5, var8);
   }

   static native void nglMatrixScaledEXT(int var0, double var1, double var3, double var5, long var7);

   public static void glMatrixTranslatefEXT(int var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMatrixTranslatefEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglMatrixTranslatefEXT(var0, var1, var2, var3, var5);
   }

   static native void nglMatrixTranslatefEXT(int var0, float var1, float var2, float var3, long var4);

   public static void glMatrixTranslatedEXT(int var0, double var1, double var3, double var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glMatrixTranslatedEXT;
      BufferChecks.checkFunctionAddress(var8);
      nglMatrixTranslatedEXT(var0, var1, var3, var5, var8);
   }

   static native void nglMatrixTranslatedEXT(int var0, double var1, double var3, double var5, long var7);

   public static void glMatrixOrthoEXT(int var0, double var1, double var3, double var5, double var7, double var9, double var11) {
      ContextCapabilities var13 = GLContext.getCapabilities();
      long var14 = var13.glMatrixOrthoEXT;
      BufferChecks.checkFunctionAddress(var14);
      nglMatrixOrthoEXT(var0, var1, var3, var5, var7, var9, var11, var14);
   }

   static native void nglMatrixOrthoEXT(int var0, double var1, double var3, double var5, double var7, double var9, double var11, long var13);

   public static void glMatrixFrustumEXT(int var0, double var1, double var3, double var5, double var7, double var9, double var11) {
      ContextCapabilities var13 = GLContext.getCapabilities();
      long var14 = var13.glMatrixFrustumEXT;
      BufferChecks.checkFunctionAddress(var14);
      nglMatrixFrustumEXT(var0, var1, var3, var5, var7, var9, var11, var14);
   }

   static native void nglMatrixFrustumEXT(int var0, double var1, double var3, double var5, double var7, double var9, double var11, long var13);

   public static void glMatrixPushEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glMatrixPushEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglMatrixPushEXT(var0, var2);
   }

   static native void nglMatrixPushEXT(int var0, long var1);

   public static void glMatrixPopEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glMatrixPopEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglMatrixPopEXT(var0, var2);
   }

   static native void nglMatrixPopEXT(int var0, long var1);

   public static void glTextureParameteriEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTextureParameteriEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglTextureParameteriEXT(var0, var1, var2, var3, var5);
   }

   static native void nglTextureParameteriEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glTextureParameterEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTextureParameterivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglTextureParameterivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglTextureParameterivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glTextureParameterfEXT(int var0, int var1, int var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTextureParameterfEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglTextureParameterfEXT(var0, var1, var2, var3, var5);
   }

   static native void nglTextureParameterfEXT(int var0, int var1, int var2, float var3, long var4);

   public static void glTextureParameterEXT(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTextureParameterfvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((FloatBuffer)var3, 4);
      nglTextureParameterfvEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglTextureParameterfvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glTextureImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, ByteBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTextureImage1DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      if (var8 != null) {
         BufferChecks.checkBuffer(var8, GLChecks.calculateTexImage1DStorage(var8, var6, var7, var4));
      }

      nglTextureImage1DEXT(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddressSafe(var8), var10);
   }

   public static void glTextureImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, DoubleBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTextureImage1DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      if (var8 != null) {
         BufferChecks.checkBuffer(var8, GLChecks.calculateTexImage1DStorage(var8, var6, var7, var4));
      }

      nglTextureImage1DEXT(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddressSafe(var8), var10);
   }

   public static void glTextureImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, FloatBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTextureImage1DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      if (var8 != null) {
         BufferChecks.checkBuffer(var8, GLChecks.calculateTexImage1DStorage(var8, var6, var7, var4));
      }

      nglTextureImage1DEXT(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddressSafe(var8), var10);
   }

   public static void glTextureImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, IntBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTextureImage1DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      if (var8 != null) {
         BufferChecks.checkBuffer(var8, GLChecks.calculateTexImage1DStorage(var8, var6, var7, var4));
      }

      nglTextureImage1DEXT(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddressSafe(var8), var10);
   }

   public static void glTextureImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, ShortBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTextureImage1DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      if (var8 != null) {
         BufferChecks.checkBuffer(var8, GLChecks.calculateTexImage1DStorage(var8, var6, var7, var4));
      }

      nglTextureImage1DEXT(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddressSafe(var8), var10);
   }

   static native void nglTextureImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glTextureImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTextureImage1DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOenabled(var10);
      nglTextureImage1DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var11);
   }

   static native void nglTextureImage1DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glTextureImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, ByteBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTextureImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      if (var9 != null) {
         BufferChecks.checkBuffer(var9, GLChecks.calculateTexImage2DStorage(var9, var7, var8, var4, var5));
      }

      nglTextureImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddressSafe(var9), var11);
   }

   public static void glTextureImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, DoubleBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTextureImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      if (var9 != null) {
         BufferChecks.checkBuffer(var9, GLChecks.calculateTexImage2DStorage(var9, var7, var8, var4, var5));
      }

      nglTextureImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddressSafe(var9), var11);
   }

   public static void glTextureImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, FloatBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTextureImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      if (var9 != null) {
         BufferChecks.checkBuffer(var9, GLChecks.calculateTexImage2DStorage(var9, var7, var8, var4, var5));
      }

      nglTextureImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddressSafe(var9), var11);
   }

   public static void glTextureImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, IntBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTextureImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      if (var9 != null) {
         BufferChecks.checkBuffer(var9, GLChecks.calculateTexImage2DStorage(var9, var7, var8, var4, var5));
      }

      nglTextureImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddressSafe(var9), var11);
   }

   public static void glTextureImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, ShortBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTextureImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      if (var9 != null) {
         BufferChecks.checkBuffer(var9, GLChecks.calculateTexImage2DStorage(var9, var7, var8, var4, var5));
      }

      nglTextureImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddressSafe(var9), var11);
   }

   static native void nglTextureImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glTextureImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glTextureImage2DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOenabled(var11);
      nglTextureImage2DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var12);
   }

   static native void nglTextureImage2DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glTextureSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, ByteBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTextureSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkBuffer(var7, GLChecks.calculateImageStorage(var7, var5, var6, var4, 1, 1));
      nglTextureSubImage1DEXT(var0, var1, var2, var3, var4, var5, var6, MemoryUtil.getAddress(var7), var9);
   }

   public static void glTextureSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, DoubleBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTextureSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkBuffer(var7, GLChecks.calculateImageStorage(var7, var5, var6, var4, 1, 1));
      nglTextureSubImage1DEXT(var0, var1, var2, var3, var4, var5, var6, MemoryUtil.getAddress(var7), var9);
   }

   public static void glTextureSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, FloatBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTextureSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkBuffer(var7, GLChecks.calculateImageStorage(var7, var5, var6, var4, 1, 1));
      nglTextureSubImage1DEXT(var0, var1, var2, var3, var4, var5, var6, MemoryUtil.getAddress(var7), var9);
   }

   public static void glTextureSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, IntBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTextureSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkBuffer(var7, GLChecks.calculateImageStorage(var7, var5, var6, var4, 1, 1));
      nglTextureSubImage1DEXT(var0, var1, var2, var3, var4, var5, var6, MemoryUtil.getAddress(var7), var9);
   }

   public static void glTextureSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, ShortBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTextureSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkBuffer(var7, GLChecks.calculateImageStorage(var7, var5, var6, var4, 1, 1));
      nglTextureSubImage1DEXT(var0, var1, var2, var3, var4, var5, var6, MemoryUtil.getAddress(var7), var9);
   }

   static native void nglTextureSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glTextureSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTextureSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOenabled(var9);
      nglTextureSubImage1DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var10);
   }

   static native void nglTextureSubImage1DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glTextureSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, ByteBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTextureSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      BufferChecks.checkBuffer(var9, GLChecks.calculateImageStorage(var9, var7, var8, var5, var6, 1));
      nglTextureSubImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddress(var9), var11);
   }

   public static void glTextureSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, DoubleBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTextureSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      BufferChecks.checkBuffer(var9, GLChecks.calculateImageStorage(var9, var7, var8, var5, var6, 1));
      nglTextureSubImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddress(var9), var11);
   }

   public static void glTextureSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, FloatBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTextureSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      BufferChecks.checkBuffer(var9, GLChecks.calculateImageStorage(var9, var7, var8, var5, var6, 1));
      nglTextureSubImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddress(var9), var11);
   }

   public static void glTextureSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, IntBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTextureSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      BufferChecks.checkBuffer(var9, GLChecks.calculateImageStorage(var9, var7, var8, var5, var6, 1));
      nglTextureSubImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddress(var9), var11);
   }

   public static void glTextureSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, ShortBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTextureSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      BufferChecks.checkBuffer(var9, GLChecks.calculateImageStorage(var9, var7, var8, var5, var6, 1));
      nglTextureSubImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddress(var9), var11);
   }

   static native void nglTextureSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glTextureSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glTextureSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOenabled(var11);
      nglTextureSubImage2DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var12);
   }

   static native void nglTextureSubImage2DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glCopyTextureImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glCopyTextureImage1DEXT;
      BufferChecks.checkFunctionAddress(var9);
      nglCopyTextureImage1DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var9);
   }

   static native void nglCopyTextureImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8);

   public static void glCopyTextureImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glCopyTextureImage2DEXT;
      BufferChecks.checkFunctionAddress(var10);
      nglCopyTextureImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var10);
   }

   static native void nglCopyTextureImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9);

   public static void glCopyTextureSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glCopyTextureSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var8);
      nglCopyTextureSubImage1DEXT(var0, var1, var2, var3, var4, var5, var6, var8);
   }

   static native void nglCopyTextureSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7);

   public static void glCopyTextureSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glCopyTextureSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var10);
      nglCopyTextureSubImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var10);
   }

   static native void nglCopyTextureSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9);

   public static void glGetTextureImageEXT(int var0, int var1, int var2, int var3, int var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetTextureImageEXT;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, 1, 1, 1));
      nglGetTextureImageEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetTextureImageEXT(int var0, int var1, int var2, int var3, int var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetTextureImageEXT;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, 1, 1, 1));
      nglGetTextureImageEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetTextureImageEXT(int var0, int var1, int var2, int var3, int var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetTextureImageEXT;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, 1, 1, 1));
      nglGetTextureImageEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetTextureImageEXT(int var0, int var1, int var2, int var3, int var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetTextureImageEXT;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, 1, 1, 1));
      nglGetTextureImageEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetTextureImageEXT(int var0, int var1, int var2, int var3, int var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetTextureImageEXT;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, 1, 1, 1));
      nglGetTextureImageEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   static native void nglGetTextureImageEXT(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glGetTextureImageEXT(int var0, int var1, int var2, int var3, int var4, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glGetTextureImageEXT;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensurePackPBOenabled(var7);
      nglGetTextureImageEXTBO(var0, var1, var2, var3, var4, var5, var8);
   }

   static native void nglGetTextureImageEXTBO(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glGetTextureParameterEXT(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetTextureParameterfvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((FloatBuffer)var3, 4);
      nglGetTextureParameterfvEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetTextureParameterfvEXT(int var0, int var1, int var2, long var3, long var5);

   public static float glGetTextureParameterfEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTextureParameterfvEXT;
      BufferChecks.checkFunctionAddress(var4);
      FloatBuffer var6 = APIUtil.getBufferFloat(var3);
      nglGetTextureParameterfvEXT(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGetTextureParameterEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetTextureParameterivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetTextureParameterivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetTextureParameterivEXT(int var0, int var1, int var2, long var3, long var5);

   public static int glGetTextureParameteriEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTextureParameterivEXT;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetTextureParameterivEXT(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGetTextureLevelParameterEXT(int var0, int var1, int var2, int var3, FloatBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetTextureLevelParameterfvEXT;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkBuffer((FloatBuffer)var4, 4);
      nglGetTextureLevelParameterfvEXT(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglGetTextureLevelParameterfvEXT(int var0, int var1, int var2, int var3, long var4, long var6);

   public static float glGetTextureLevelParameterfEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetTextureLevelParameterfvEXT;
      BufferChecks.checkFunctionAddress(var5);
      FloatBuffer var7 = APIUtil.getBufferFloat(var4);
      nglGetTextureLevelParameterfvEXT(var0, var1, var2, var3, MemoryUtil.getAddress(var7), var5);
      return var7.get(0);
   }

   public static void glGetTextureLevelParameterEXT(int var0, int var1, int var2, int var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetTextureLevelParameterivEXT;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkBuffer((IntBuffer)var4, 4);
      nglGetTextureLevelParameterivEXT(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglGetTextureLevelParameterivEXT(int var0, int var1, int var2, int var3, long var4, long var6);

   public static int glGetTextureLevelParameteriEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetTextureLevelParameterivEXT;
      BufferChecks.checkFunctionAddress(var5);
      IntBuffer var7 = APIUtil.getBufferInt(var4);
      nglGetTextureLevelParameterivEXT(var0, var1, var2, var3, MemoryUtil.getAddress(var7), var5);
      return var7.get(0);
   }

   public static void glTextureImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, ByteBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glTextureImage3DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOdisabled(var11);
      if (var10 != null) {
         BufferChecks.checkBuffer(var10, GLChecks.calculateTexImage3DStorage(var10, var8, var9, var4, var5, var6));
      }

      nglTextureImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddressSafe(var10), var12);
   }

   public static void glTextureImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, DoubleBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glTextureImage3DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOdisabled(var11);
      if (var10 != null) {
         BufferChecks.checkBuffer(var10, GLChecks.calculateTexImage3DStorage(var10, var8, var9, var4, var5, var6));
      }

      nglTextureImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddressSafe(var10), var12);
   }

   public static void glTextureImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, FloatBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glTextureImage3DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOdisabled(var11);
      if (var10 != null) {
         BufferChecks.checkBuffer(var10, GLChecks.calculateTexImage3DStorage(var10, var8, var9, var4, var5, var6));
      }

      nglTextureImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddressSafe(var10), var12);
   }

   public static void glTextureImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, IntBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glTextureImage3DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOdisabled(var11);
      if (var10 != null) {
         BufferChecks.checkBuffer(var10, GLChecks.calculateTexImage3DStorage(var10, var8, var9, var4, var5, var6));
      }

      nglTextureImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddressSafe(var10), var12);
   }

   public static void glTextureImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, ShortBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glTextureImage3DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOdisabled(var11);
      if (var10 != null) {
         BufferChecks.checkBuffer(var10, GLChecks.calculateTexImage3DStorage(var10, var8, var9, var4, var5, var6));
      }

      nglTextureImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddressSafe(var10), var12);
   }

   static native void nglTextureImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10, long var12);

   public static void glTextureImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glTextureImage3DEXT;
      BufferChecks.checkFunctionAddress(var13);
      GLChecks.ensureUnpackPBOenabled(var12);
      nglTextureImage3DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var13);
   }

   static native void nglTextureImage3DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10, long var12);

   public static void glTextureSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, ByteBuffer var11) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glTextureSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var13);
      GLChecks.ensureUnpackPBOdisabled(var12);
      BufferChecks.checkBuffer(var11, GLChecks.calculateImageStorage(var11, var9, var10, var6, var7, var8));
      nglTextureSubImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, MemoryUtil.getAddress(var11), var13);
   }

   public static void glTextureSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, DoubleBuffer var11) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glTextureSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var13);
      GLChecks.ensureUnpackPBOdisabled(var12);
      BufferChecks.checkBuffer(var11, GLChecks.calculateImageStorage(var11, var9, var10, var6, var7, var8));
      nglTextureSubImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, MemoryUtil.getAddress(var11), var13);
   }

   public static void glTextureSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, FloatBuffer var11) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glTextureSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var13);
      GLChecks.ensureUnpackPBOdisabled(var12);
      BufferChecks.checkBuffer(var11, GLChecks.calculateImageStorage(var11, var9, var10, var6, var7, var8));
      nglTextureSubImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, MemoryUtil.getAddress(var11), var13);
   }

   public static void glTextureSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, IntBuffer var11) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glTextureSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var13);
      GLChecks.ensureUnpackPBOdisabled(var12);
      BufferChecks.checkBuffer(var11, GLChecks.calculateImageStorage(var11, var9, var10, var6, var7, var8));
      nglTextureSubImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, MemoryUtil.getAddress(var11), var13);
   }

   public static void glTextureSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, ShortBuffer var11) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glTextureSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var13);
      GLChecks.ensureUnpackPBOdisabled(var12);
      BufferChecks.checkBuffer(var11, GLChecks.calculateImageStorage(var11, var9, var10, var6, var7, var8));
      nglTextureSubImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, MemoryUtil.getAddress(var11), var13);
   }

   static native void nglTextureSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, long var11, long var13);

   public static void glTextureSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, long var11) {
      ContextCapabilities var13 = GLContext.getCapabilities();
      long var14 = var13.glTextureSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var14);
      GLChecks.ensureUnpackPBOenabled(var13);
      nglTextureSubImage3DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var14);
   }

   static native void nglTextureSubImage3DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, long var11, long var13);

   public static void glCopyTextureSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glCopyTextureSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var11);
      nglCopyTextureSubImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var11);
   }

   static native void nglCopyTextureSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10);

   public static void glBindMultiTextureEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBindMultiTextureEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglBindMultiTextureEXT(var0, var1, var2, var4);
   }

   static native void nglBindMultiTextureEXT(int var0, int var1, int var2, long var3);

   public static void glMultiTexCoordPointerEXT(int var0, int var1, int var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexCoordPointerEXT;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglMultiTexCoordPointerEXT(var0, var1, 5130, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glMultiTexCoordPointerEXT(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexCoordPointerEXT;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglMultiTexCoordPointerEXT(var0, var1, 5126, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglMultiTexCoordPointerEXT(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glMultiTexCoordPointerEXT(int var0, int var1, int var2, int var3, long var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glMultiTexCoordPointerEXT;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureArrayVBOenabled(var6);
      nglMultiTexCoordPointerEXTBO(var0, var1, var2, var3, var4, var7);
   }

   static native void nglMultiTexCoordPointerEXTBO(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glMultiTexEnvfEXT(int var0, int var1, int var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexEnvfEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglMultiTexEnvfEXT(var0, var1, var2, var3, var5);
   }

   static native void nglMultiTexEnvfEXT(int var0, int var1, int var2, float var3, long var4);

   public static void glMultiTexEnvEXT(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexEnvfvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((FloatBuffer)var3, 4);
      nglMultiTexEnvfvEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglMultiTexEnvfvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glMultiTexEnviEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexEnviEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglMultiTexEnviEXT(var0, var1, var2, var3, var5);
   }

   static native void nglMultiTexEnviEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glMultiTexEnvEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexEnvivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglMultiTexEnvivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglMultiTexEnvivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glMultiTexGendEXT(int var0, int var1, int var2, double var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMultiTexGendEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglMultiTexGendEXT(var0, var1, var2, var3, var6);
   }

   static native void nglMultiTexGendEXT(int var0, int var1, int var2, double var3, long var5);

   public static void glMultiTexGenEXT(int var0, int var1, int var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexGendvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((DoubleBuffer)var3, 4);
      nglMultiTexGendvEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglMultiTexGendvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glMultiTexGenfEXT(int var0, int var1, int var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexGenfEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglMultiTexGenfEXT(var0, var1, var2, var3, var5);
   }

   static native void nglMultiTexGenfEXT(int var0, int var1, int var2, float var3, long var4);

   public static void glMultiTexGenEXT(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexGenfvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((FloatBuffer)var3, 4);
      nglMultiTexGenfvEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglMultiTexGenfvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glMultiTexGeniEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexGeniEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglMultiTexGeniEXT(var0, var1, var2, var3, var5);
   }

   static native void nglMultiTexGeniEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glMultiTexGenEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexGenivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglMultiTexGenivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglMultiTexGenivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glGetMultiTexEnvEXT(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetMultiTexEnvfvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((FloatBuffer)var3, 4);
      nglGetMultiTexEnvfvEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetMultiTexEnvfvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glGetMultiTexEnvEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetMultiTexEnvivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetMultiTexEnvivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetMultiTexEnvivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glGetMultiTexGenEXT(int var0, int var1, int var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetMultiTexGendvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((DoubleBuffer)var3, 4);
      nglGetMultiTexGendvEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetMultiTexGendvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glGetMultiTexGenEXT(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetMultiTexGenfvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((FloatBuffer)var3, 4);
      nglGetMultiTexGenfvEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetMultiTexGenfvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glGetMultiTexGenEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetMultiTexGenivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetMultiTexGenivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetMultiTexGenivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glMultiTexParameteriEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexParameteriEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglMultiTexParameteriEXT(var0, var1, var2, var3, var5);
   }

   static native void nglMultiTexParameteriEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glMultiTexParameterEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexParameterivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglMultiTexParameterivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglMultiTexParameterivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glMultiTexParameterfEXT(int var0, int var1, int var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexParameterfEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglMultiTexParameterfEXT(var0, var1, var2, var3, var5);
   }

   static native void nglMultiTexParameterfEXT(int var0, int var1, int var2, float var3, long var4);

   public static void glMultiTexParameterEXT(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexParameterfvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((FloatBuffer)var3, 4);
      nglMultiTexParameterfvEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglMultiTexParameterfvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glMultiTexImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, ByteBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glMultiTexImage1DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      if (var8 != null) {
         BufferChecks.checkBuffer(var8, GLChecks.calculateTexImage1DStorage(var8, var6, var7, var4));
      }

      nglMultiTexImage1DEXT(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddressSafe(var8), var10);
   }

   public static void glMultiTexImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, DoubleBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glMultiTexImage1DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      if (var8 != null) {
         BufferChecks.checkBuffer(var8, GLChecks.calculateTexImage1DStorage(var8, var6, var7, var4));
      }

      nglMultiTexImage1DEXT(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddressSafe(var8), var10);
   }

   public static void glMultiTexImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, FloatBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glMultiTexImage1DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      if (var8 != null) {
         BufferChecks.checkBuffer(var8, GLChecks.calculateTexImage1DStorage(var8, var6, var7, var4));
      }

      nglMultiTexImage1DEXT(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddressSafe(var8), var10);
   }

   public static void glMultiTexImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, IntBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glMultiTexImage1DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      if (var8 != null) {
         BufferChecks.checkBuffer(var8, GLChecks.calculateTexImage1DStorage(var8, var6, var7, var4));
      }

      nglMultiTexImage1DEXT(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddressSafe(var8), var10);
   }

   public static void glMultiTexImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, ShortBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glMultiTexImage1DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      if (var8 != null) {
         BufferChecks.checkBuffer(var8, GLChecks.calculateTexImage1DStorage(var8, var6, var7, var4));
      }

      nglMultiTexImage1DEXT(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddressSafe(var8), var10);
   }

   static native void nglMultiTexImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glMultiTexImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glMultiTexImage1DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOenabled(var10);
      nglMultiTexImage1DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var11);
   }

   static native void nglMultiTexImage1DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glMultiTexImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, ByteBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glMultiTexImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      if (var9 != null) {
         BufferChecks.checkBuffer(var9, GLChecks.calculateTexImage2DStorage(var9, var7, var8, var4, var5));
      }

      nglMultiTexImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddressSafe(var9), var11);
   }

   public static void glMultiTexImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, DoubleBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glMultiTexImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      if (var9 != null) {
         BufferChecks.checkBuffer(var9, GLChecks.calculateTexImage2DStorage(var9, var7, var8, var4, var5));
      }

      nglMultiTexImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddressSafe(var9), var11);
   }

   public static void glMultiTexImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, FloatBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glMultiTexImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      if (var9 != null) {
         BufferChecks.checkBuffer(var9, GLChecks.calculateTexImage2DStorage(var9, var7, var8, var4, var5));
      }

      nglMultiTexImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddressSafe(var9), var11);
   }

   public static void glMultiTexImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, IntBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glMultiTexImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      if (var9 != null) {
         BufferChecks.checkBuffer(var9, GLChecks.calculateTexImage2DStorage(var9, var7, var8, var4, var5));
      }

      nglMultiTexImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddressSafe(var9), var11);
   }

   public static void glMultiTexImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, ShortBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glMultiTexImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      if (var9 != null) {
         BufferChecks.checkBuffer(var9, GLChecks.calculateTexImage2DStorage(var9, var7, var8, var4, var5));
      }

      nglMultiTexImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddressSafe(var9), var11);
   }

   static native void nglMultiTexImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glMultiTexImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glMultiTexImage2DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOenabled(var11);
      nglMultiTexImage2DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var12);
   }

   static native void nglMultiTexImage2DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glMultiTexSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, ByteBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glMultiTexSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkBuffer(var7, GLChecks.calculateImageStorage(var7, var5, var6, var4, 1, 1));
      nglMultiTexSubImage1DEXT(var0, var1, var2, var3, var4, var5, var6, MemoryUtil.getAddress(var7), var9);
   }

   public static void glMultiTexSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, DoubleBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glMultiTexSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkBuffer(var7, GLChecks.calculateImageStorage(var7, var5, var6, var4, 1, 1));
      nglMultiTexSubImage1DEXT(var0, var1, var2, var3, var4, var5, var6, MemoryUtil.getAddress(var7), var9);
   }

   public static void glMultiTexSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, FloatBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glMultiTexSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkBuffer(var7, GLChecks.calculateImageStorage(var7, var5, var6, var4, 1, 1));
      nglMultiTexSubImage1DEXT(var0, var1, var2, var3, var4, var5, var6, MemoryUtil.getAddress(var7), var9);
   }

   public static void glMultiTexSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, IntBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glMultiTexSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkBuffer(var7, GLChecks.calculateImageStorage(var7, var5, var6, var4, 1, 1));
      nglMultiTexSubImage1DEXT(var0, var1, var2, var3, var4, var5, var6, MemoryUtil.getAddress(var7), var9);
   }

   public static void glMultiTexSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, ShortBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glMultiTexSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkBuffer(var7, GLChecks.calculateImageStorage(var7, var5, var6, var4, 1, 1));
      nglMultiTexSubImage1DEXT(var0, var1, var2, var3, var4, var5, var6, MemoryUtil.getAddress(var7), var9);
   }

   static native void nglMultiTexSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glMultiTexSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glMultiTexSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOenabled(var9);
      nglMultiTexSubImage1DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var10);
   }

   static native void nglMultiTexSubImage1DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glMultiTexSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, ByteBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glMultiTexSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      BufferChecks.checkBuffer(var9, GLChecks.calculateImageStorage(var9, var7, var8, var5, var6, 1));
      nglMultiTexSubImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddress(var9), var11);
   }

   public static void glMultiTexSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, DoubleBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glMultiTexSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      BufferChecks.checkBuffer(var9, GLChecks.calculateImageStorage(var9, var7, var8, var5, var6, 1));
      nglMultiTexSubImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddress(var9), var11);
   }

   public static void glMultiTexSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, FloatBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glMultiTexSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      BufferChecks.checkBuffer(var9, GLChecks.calculateImageStorage(var9, var7, var8, var5, var6, 1));
      nglMultiTexSubImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddress(var9), var11);
   }

   public static void glMultiTexSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, IntBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glMultiTexSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      BufferChecks.checkBuffer(var9, GLChecks.calculateImageStorage(var9, var7, var8, var5, var6, 1));
      nglMultiTexSubImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddress(var9), var11);
   }

   public static void glMultiTexSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, ShortBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glMultiTexSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      BufferChecks.checkBuffer(var9, GLChecks.calculateImageStorage(var9, var7, var8, var5, var6, 1));
      nglMultiTexSubImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddress(var9), var11);
   }

   static native void nglMultiTexSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glMultiTexSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glMultiTexSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOenabled(var11);
      nglMultiTexSubImage2DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var12);
   }

   static native void nglMultiTexSubImage2DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glCopyMultiTexImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glCopyMultiTexImage1DEXT;
      BufferChecks.checkFunctionAddress(var9);
      nglCopyMultiTexImage1DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var9);
   }

   static native void nglCopyMultiTexImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8);

   public static void glCopyMultiTexImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glCopyMultiTexImage2DEXT;
      BufferChecks.checkFunctionAddress(var10);
      nglCopyMultiTexImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var10);
   }

   static native void nglCopyMultiTexImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9);

   public static void glCopyMultiTexSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glCopyMultiTexSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var8);
      nglCopyMultiTexSubImage1DEXT(var0, var1, var2, var3, var4, var5, var6, var8);
   }

   static native void nglCopyMultiTexSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7);

   public static void glCopyMultiTexSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glCopyMultiTexSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var10);
      nglCopyMultiTexSubImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var10);
   }

   static native void nglCopyMultiTexSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9);

   public static void glGetMultiTexImageEXT(int var0, int var1, int var2, int var3, int var4, ByteBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetMultiTexImageEXT;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, 1, 1, 1));
      nglGetMultiTexImageEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetMultiTexImageEXT(int var0, int var1, int var2, int var3, int var4, DoubleBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetMultiTexImageEXT;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, 1, 1, 1));
      nglGetMultiTexImageEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetMultiTexImageEXT(int var0, int var1, int var2, int var3, int var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetMultiTexImageEXT;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, 1, 1, 1));
      nglGetMultiTexImageEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetMultiTexImageEXT(int var0, int var1, int var2, int var3, int var4, IntBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetMultiTexImageEXT;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, 1, 1, 1));
      nglGetMultiTexImageEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   public static void glGetMultiTexImageEXT(int var0, int var1, int var2, int var3, int var4, ShortBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetMultiTexImageEXT;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOdisabled(var6);
      BufferChecks.checkBuffer(var5, GLChecks.calculateImageStorage(var5, var3, var4, 1, 1, 1));
      nglGetMultiTexImageEXT(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   static native void nglGetMultiTexImageEXT(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glGetMultiTexImageEXT(int var0, int var1, int var2, int var3, int var4, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glGetMultiTexImageEXT;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensurePackPBOenabled(var7);
      nglGetMultiTexImageEXTBO(var0, var1, var2, var3, var4, var5, var8);
   }

   static native void nglGetMultiTexImageEXTBO(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glGetMultiTexParameterEXT(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetMultiTexParameterfvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((FloatBuffer)var3, 4);
      nglGetMultiTexParameterfvEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetMultiTexParameterfvEXT(int var0, int var1, int var2, long var3, long var5);

   public static float glGetMultiTexParameterfEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetMultiTexParameterfvEXT;
      BufferChecks.checkFunctionAddress(var4);
      FloatBuffer var6 = APIUtil.getBufferFloat(var3);
      nglGetMultiTexParameterfvEXT(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGetMultiTexParameterEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetMultiTexParameterivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetMultiTexParameterivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetMultiTexParameterivEXT(int var0, int var1, int var2, long var3, long var5);

   public static int glGetMultiTexParameteriEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetMultiTexParameterivEXT;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetMultiTexParameterivEXT(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGetMultiTexLevelParameterEXT(int var0, int var1, int var2, int var3, FloatBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetMultiTexLevelParameterfvEXT;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkBuffer((FloatBuffer)var4, 4);
      nglGetMultiTexLevelParameterfvEXT(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglGetMultiTexLevelParameterfvEXT(int var0, int var1, int var2, int var3, long var4, long var6);

   public static float glGetMultiTexLevelParameterfEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetMultiTexLevelParameterfvEXT;
      BufferChecks.checkFunctionAddress(var5);
      FloatBuffer var7 = APIUtil.getBufferFloat(var4);
      nglGetMultiTexLevelParameterfvEXT(var0, var1, var2, var3, MemoryUtil.getAddress(var7), var5);
      return var7.get(0);
   }

   public static void glGetMultiTexLevelParameterEXT(int var0, int var1, int var2, int var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetMultiTexLevelParameterivEXT;
      BufferChecks.checkFunctionAddress(var6);
      BufferChecks.checkBuffer((IntBuffer)var4, 4);
      nglGetMultiTexLevelParameterivEXT(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglGetMultiTexLevelParameterivEXT(int var0, int var1, int var2, int var3, long var4, long var6);

   public static int glGetMultiTexLevelParameteriEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetMultiTexLevelParameterivEXT;
      BufferChecks.checkFunctionAddress(var5);
      IntBuffer var7 = APIUtil.getBufferInt(var4);
      nglGetMultiTexLevelParameterivEXT(var0, var1, var2, var3, MemoryUtil.getAddress(var7), var5);
      return var7.get(0);
   }

   public static void glMultiTexImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, ByteBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glMultiTexImage3DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOdisabled(var11);
      if (var10 != null) {
         BufferChecks.checkBuffer(var10, GLChecks.calculateTexImage3DStorage(var10, var8, var9, var4, var5, var6));
      }

      nglMultiTexImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddressSafe(var10), var12);
   }

   public static void glMultiTexImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, DoubleBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glMultiTexImage3DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOdisabled(var11);
      if (var10 != null) {
         BufferChecks.checkBuffer(var10, GLChecks.calculateTexImage3DStorage(var10, var8, var9, var4, var5, var6));
      }

      nglMultiTexImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddressSafe(var10), var12);
   }

   public static void glMultiTexImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, FloatBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glMultiTexImage3DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOdisabled(var11);
      if (var10 != null) {
         BufferChecks.checkBuffer(var10, GLChecks.calculateTexImage3DStorage(var10, var8, var9, var4, var5, var6));
      }

      nglMultiTexImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddressSafe(var10), var12);
   }

   public static void glMultiTexImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, IntBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glMultiTexImage3DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOdisabled(var11);
      if (var10 != null) {
         BufferChecks.checkBuffer(var10, GLChecks.calculateTexImage3DStorage(var10, var8, var9, var4, var5, var6));
      }

      nglMultiTexImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddressSafe(var10), var12);
   }

   public static void glMultiTexImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, ShortBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glMultiTexImage3DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOdisabled(var11);
      if (var10 != null) {
         BufferChecks.checkBuffer(var10, GLChecks.calculateTexImage3DStorage(var10, var8, var9, var4, var5, var6));
      }

      nglMultiTexImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddressSafe(var10), var12);
   }

   static native void nglMultiTexImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10, long var12);

   public static void glMultiTexImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glMultiTexImage3DEXT;
      BufferChecks.checkFunctionAddress(var13);
      GLChecks.ensureUnpackPBOenabled(var12);
      nglMultiTexImage3DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var13);
   }

   static native void nglMultiTexImage3DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10, long var12);

   public static void glMultiTexSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, ByteBuffer var11) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glMultiTexSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var13);
      GLChecks.ensureUnpackPBOdisabled(var12);
      BufferChecks.checkBuffer(var11, GLChecks.calculateImageStorage(var11, var9, var10, var6, var7, var8));
      nglMultiTexSubImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, MemoryUtil.getAddress(var11), var13);
   }

   public static void glMultiTexSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, DoubleBuffer var11) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glMultiTexSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var13);
      GLChecks.ensureUnpackPBOdisabled(var12);
      BufferChecks.checkBuffer(var11, GLChecks.calculateImageStorage(var11, var9, var10, var6, var7, var8));
      nglMultiTexSubImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, MemoryUtil.getAddress(var11), var13);
   }

   public static void glMultiTexSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, FloatBuffer var11) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glMultiTexSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var13);
      GLChecks.ensureUnpackPBOdisabled(var12);
      BufferChecks.checkBuffer(var11, GLChecks.calculateImageStorage(var11, var9, var10, var6, var7, var8));
      nglMultiTexSubImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, MemoryUtil.getAddress(var11), var13);
   }

   public static void glMultiTexSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, IntBuffer var11) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glMultiTexSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var13);
      GLChecks.ensureUnpackPBOdisabled(var12);
      BufferChecks.checkBuffer(var11, GLChecks.calculateImageStorage(var11, var9, var10, var6, var7, var8));
      nglMultiTexSubImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, MemoryUtil.getAddress(var11), var13);
   }

   public static void glMultiTexSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, ShortBuffer var11) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glMultiTexSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var13);
      GLChecks.ensureUnpackPBOdisabled(var12);
      BufferChecks.checkBuffer(var11, GLChecks.calculateImageStorage(var11, var9, var10, var6, var7, var8));
      nglMultiTexSubImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, MemoryUtil.getAddress(var11), var13);
   }

   static native void nglMultiTexSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, long var11, long var13);

   public static void glMultiTexSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, long var11) {
      ContextCapabilities var13 = GLContext.getCapabilities();
      long var14 = var13.glMultiTexSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var14);
      GLChecks.ensureUnpackPBOenabled(var13);
      nglMultiTexSubImage3DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var14);
   }

   static native void nglMultiTexSubImage3DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, long var11, long var13);

   public static void glCopyMultiTexSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glCopyMultiTexSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var11);
      nglCopyMultiTexSubImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var11);
   }

   static native void nglCopyMultiTexSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10);

   public static void glEnableClientStateIndexedEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glEnableClientStateIndexedEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglEnableClientStateIndexedEXT(var0, var1, var3);
   }

   static native void nglEnableClientStateIndexedEXT(int var0, int var1, long var2);

   public static void glDisableClientStateIndexedEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDisableClientStateIndexedEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglDisableClientStateIndexedEXT(var0, var1, var3);
   }

   static native void nglDisableClientStateIndexedEXT(int var0, int var1, long var2);

   public static void glEnableClientStateiEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glEnableClientStateiEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglEnableClientStateiEXT(var0, var1, var3);
   }

   static native void nglEnableClientStateiEXT(int var0, int var1, long var2);

   public static void glDisableClientStateiEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDisableClientStateiEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglDisableClientStateiEXT(var0, var1, var3);
   }

   static native void nglDisableClientStateiEXT(int var0, int var1, long var2);

   public static void glGetFloatIndexedEXT(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetFloatIndexedvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 16);
      nglGetFloatIndexedvEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetFloatIndexedvEXT(int var0, int var1, long var2, long var4);

   public static float glGetFloatIndexedEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetFloatIndexedvEXT;
      BufferChecks.checkFunctionAddress(var3);
      FloatBuffer var5 = APIUtil.getBufferFloat(var2);
      nglGetFloatIndexedvEXT(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetDoubleIndexedEXT(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetDoubleIndexedvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((DoubleBuffer)var2, 16);
      nglGetDoubleIndexedvEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetDoubleIndexedvEXT(int var0, int var1, long var2, long var4);

   public static double glGetDoubleIndexedEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetDoubleIndexedvEXT;
      BufferChecks.checkFunctionAddress(var3);
      DoubleBuffer var5 = APIUtil.getBufferDouble(var2);
      nglGetDoubleIndexedvEXT(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static ByteBuffer glGetPointerIndexedEXT(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetPointerIndexedvEXT;
      BufferChecks.checkFunctionAddress(var5);
      ByteBuffer var7 = nglGetPointerIndexedvEXT(var0, var1, var2, var5);
      return LWJGLUtil.CHECKS && var7 == null ? null : var7.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglGetPointerIndexedvEXT(int var0, int var1, long var2, long var4);

   public static void glGetFloatEXT(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetFloati_vEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 16);
      nglGetFloati_vEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetFloati_vEXT(int var0, int var1, long var2, long var4);

   public static float glGetFloatEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetFloati_vEXT;
      BufferChecks.checkFunctionAddress(var3);
      FloatBuffer var5 = APIUtil.getBufferFloat(var2);
      nglGetFloati_vEXT(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetDoubleEXT(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetDoublei_vEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((DoubleBuffer)var2, 16);
      nglGetDoublei_vEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetDoublei_vEXT(int var0, int var1, long var2, long var4);

   public static double glGetDoubleEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetDoublei_vEXT;
      BufferChecks.checkFunctionAddress(var3);
      DoubleBuffer var5 = APIUtil.getBufferDouble(var2);
      nglGetDoublei_vEXT(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static ByteBuffer glGetPointerEXT(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetPointeri_vEXT;
      BufferChecks.checkFunctionAddress(var5);
      ByteBuffer var7 = nglGetPointeri_vEXT(var0, var1, var2, var5);
      return LWJGLUtil.CHECKS && var7 == null ? null : var7.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglGetPointeri_vEXT(int var0, int var1, long var2, long var4);

   public static void glEnableIndexedEXT(int var0, int var1) {
      EXTDrawBuffers2.glEnableIndexedEXT(var0, var1);
   }

   public static void glDisableIndexedEXT(int var0, int var1) {
      EXTDrawBuffers2.glDisableIndexedEXT(var0, var1);
   }

   public static boolean glIsEnabledIndexedEXT(int var0, int var1) {
      return EXTDrawBuffers2.glIsEnabledIndexedEXT(var0, var1);
   }

   public static void glGetIntegerIndexedEXT(int var0, int var1, IntBuffer var2) {
      EXTDrawBuffers2.glGetIntegerIndexedEXT(var0, var1, var2);
   }

   public static int glGetIntegerIndexedEXT(int var0, int var1) {
      return EXTDrawBuffers2.glGetIntegerIndexedEXT(var0, var1);
   }

   public static void glGetBooleanIndexedEXT(int var0, int var1, ByteBuffer var2) {
      EXTDrawBuffers2.glGetBooleanIndexedEXT(var0, var1, var2);
   }

   public static boolean glGetBooleanIndexedEXT(int var0, int var1) {
      return EXTDrawBuffers2.glGetBooleanIndexedEXT(var0, var1);
   }

   public static void glNamedProgramStringEXT(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedProgramStringEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglNamedProgramStringEXT(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var5);
   }

   static native void nglNamedProgramStringEXT(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glNamedProgramStringEXT(int var0, int var1, int var2, CharSequence var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedProgramStringEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglNamedProgramStringEXT(var0, var1, var2, var3.length(), APIUtil.getBuffer(var4, var3), var5);
   }

   public static void glNamedProgramLocalParameter4dEXT(int var0, int var1, int var2, double var3, double var5, double var7, double var9) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glNamedProgramLocalParameter4dEXT;
      BufferChecks.checkFunctionAddress(var12);
      nglNamedProgramLocalParameter4dEXT(var0, var1, var2, var3, var5, var7, var9, var12);
   }

   static native void nglNamedProgramLocalParameter4dEXT(int var0, int var1, int var2, double var3, double var5, double var7, double var9, long var11);

   public static void glNamedProgramLocalParameter4EXT(int var0, int var1, int var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedProgramLocalParameter4dvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((DoubleBuffer)var3, 4);
      nglNamedProgramLocalParameter4dvEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglNamedProgramLocalParameter4dvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glNamedProgramLocalParameter4fEXT(int var0, int var1, int var2, float var3, float var4, float var5, float var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glNamedProgramLocalParameter4fEXT;
      BufferChecks.checkFunctionAddress(var8);
      nglNamedProgramLocalParameter4fEXT(var0, var1, var2, var3, var4, var5, var6, var8);
   }

   static native void nglNamedProgramLocalParameter4fEXT(int var0, int var1, int var2, float var3, float var4, float var5, float var6, long var7);

   public static void glNamedProgramLocalParameter4EXT(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedProgramLocalParameter4fvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((FloatBuffer)var3, 4);
      nglNamedProgramLocalParameter4fvEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglNamedProgramLocalParameter4fvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glGetNamedProgramLocalParameterEXT(int var0, int var1, int var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetNamedProgramLocalParameterdvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((DoubleBuffer)var3, 4);
      nglGetNamedProgramLocalParameterdvEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetNamedProgramLocalParameterdvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glGetNamedProgramLocalParameterEXT(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetNamedProgramLocalParameterfvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((FloatBuffer)var3, 4);
      nglGetNamedProgramLocalParameterfvEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetNamedProgramLocalParameterfvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glGetNamedProgramEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetNamedProgramivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetNamedProgramivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetNamedProgramivEXT(int var0, int var1, int var2, long var3, long var5);

   public static int glGetNamedProgramEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetNamedProgramivEXT;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetNamedProgramivEXT(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGetNamedProgramStringEXT(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetNamedProgramStringEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetNamedProgramStringEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetNamedProgramStringEXT(int var0, int var1, int var2, long var3, long var5);

   public static String glGetNamedProgramStringEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetNamedProgramStringEXT;
      BufferChecks.checkFunctionAddress(var4);
      int var6 = glGetNamedProgramEXT(var0, var1, 34343);
      ByteBuffer var7 = APIUtil.getBufferByte(var3, var6);
      nglGetNamedProgramStringEXT(var0, var1, var2, MemoryUtil.getAddress(var7), var4);
      var7.limit(var6);
      return APIUtil.getString(var3, var7);
   }

   public static void glCompressedTextureImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, ByteBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glCompressedTextureImage3DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      BufferChecks.checkDirect(var8);
      nglCompressedTextureImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8.remaining(), MemoryUtil.getAddress(var8), var10);
   }

   static native void nglCompressedTextureImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glCompressedTextureImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glCompressedTextureImage3DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOenabled(var11);
      nglCompressedTextureImage3DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var12);
   }

   static native void nglCompressedTextureImage3DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glCompressedTextureImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, ByteBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glCompressedTextureImage2DEXT;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var7);
      nglCompressedTextureImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7.remaining(), MemoryUtil.getAddress(var7), var9);
   }

   static native void nglCompressedTextureImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glCompressedTextureImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glCompressedTextureImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOenabled(var10);
      nglCompressedTextureImage2DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var11);
   }

   static native void nglCompressedTextureImage2DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glCompressedTextureImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, ByteBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glCompressedTextureImage1DEXT;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOdisabled(var7);
      BufferChecks.checkDirect(var6);
      nglCompressedTextureImage1DEXT(var0, var1, var2, var3, var4, var5, var6.remaining(), MemoryUtil.getAddress(var6), var8);
   }

   static native void nglCompressedTextureImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glCompressedTextureImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glCompressedTextureImage1DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOenabled(var9);
      nglCompressedTextureImage1DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var10);
   }

   static native void nglCompressedTextureImage1DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glCompressedTextureSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, ByteBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glCompressedTextureSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOdisabled(var11);
      BufferChecks.checkDirect(var10);
      nglCompressedTextureSubImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10.remaining(), MemoryUtil.getAddress(var10), var12);
   }

   static native void nglCompressedTextureSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, long var11, long var13);

   public static void glCompressedTextureSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, long var11) {
      ContextCapabilities var13 = GLContext.getCapabilities();
      long var14 = var13.glCompressedTextureSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var14);
      GLChecks.ensureUnpackPBOenabled(var13);
      nglCompressedTextureSubImage3DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var14);
   }

   static native void nglCompressedTextureSubImage3DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, long var11, long var13);

   public static void glCompressedTextureSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, ByteBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glCompressedTextureSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      BufferChecks.checkDirect(var8);
      nglCompressedTextureSubImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8.remaining(), MemoryUtil.getAddress(var8), var10);
   }

   static native void nglCompressedTextureSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glCompressedTextureSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glCompressedTextureSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOenabled(var11);
      nglCompressedTextureSubImage2DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var12);
   }

   static native void nglCompressedTextureSubImage2DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glCompressedTextureSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, ByteBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glCompressedTextureSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOdisabled(var7);
      BufferChecks.checkDirect(var6);
      nglCompressedTextureSubImage1DEXT(var0, var1, var2, var3, var4, var5, var6.remaining(), MemoryUtil.getAddress(var6), var8);
   }

   static native void nglCompressedTextureSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glCompressedTextureSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glCompressedTextureSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOenabled(var9);
      nglCompressedTextureSubImage1DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var10);
   }

   static native void nglCompressedTextureSubImage1DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glGetCompressedTextureImageEXT(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetCompressedTextureImageEXT;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglGetCompressedTextureImageEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetCompressedTextureImageEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetCompressedTextureImageEXT;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglGetCompressedTextureImageEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetCompressedTextureImageEXT(int var0, int var1, int var2, ShortBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetCompressedTextureImageEXT;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglGetCompressedTextureImageEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetCompressedTextureImageEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glGetCompressedTextureImageEXT(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetCompressedTextureImageEXT;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOenabled(var5);
      nglGetCompressedTextureImageEXTBO(var0, var1, var2, var3, var6);
   }

   static native void nglGetCompressedTextureImageEXTBO(int var0, int var1, int var2, long var3, long var5);

   public static void glCompressedMultiTexImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, ByteBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glCompressedMultiTexImage3DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      BufferChecks.checkDirect(var8);
      nglCompressedMultiTexImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8.remaining(), MemoryUtil.getAddress(var8), var10);
   }

   static native void nglCompressedMultiTexImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glCompressedMultiTexImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glCompressedMultiTexImage3DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOenabled(var11);
      nglCompressedMultiTexImage3DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var12);
   }

   static native void nglCompressedMultiTexImage3DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glCompressedMultiTexImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, ByteBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glCompressedMultiTexImage2DEXT;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      BufferChecks.checkDirect(var7);
      nglCompressedMultiTexImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7.remaining(), MemoryUtil.getAddress(var7), var9);
   }

   static native void nglCompressedMultiTexImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glCompressedMultiTexImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glCompressedMultiTexImage2DEXT;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOenabled(var10);
      nglCompressedMultiTexImage2DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var11);
   }

   static native void nglCompressedMultiTexImage2DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glCompressedMultiTexImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, ByteBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glCompressedMultiTexImage1DEXT;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOdisabled(var7);
      BufferChecks.checkDirect(var6);
      nglCompressedMultiTexImage1DEXT(var0, var1, var2, var3, var4, var5, var6.remaining(), MemoryUtil.getAddress(var6), var8);
   }

   static native void nglCompressedMultiTexImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glCompressedMultiTexImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glCompressedMultiTexImage1DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOenabled(var9);
      nglCompressedMultiTexImage1DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var10);
   }

   static native void nglCompressedMultiTexImage1DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glCompressedMultiTexSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, ByteBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glCompressedMultiTexSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOdisabled(var11);
      BufferChecks.checkDirect(var10);
      nglCompressedMultiTexSubImage3DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10.remaining(), MemoryUtil.getAddress(var10), var12);
   }

   static native void nglCompressedMultiTexSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, long var11, long var13);

   public static void glCompressedMultiTexSubImage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, long var11) {
      ContextCapabilities var13 = GLContext.getCapabilities();
      long var14 = var13.glCompressedMultiTexSubImage3DEXT;
      BufferChecks.checkFunctionAddress(var14);
      GLChecks.ensureUnpackPBOenabled(var13);
      nglCompressedMultiTexSubImage3DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var14);
   }

   static native void nglCompressedMultiTexSubImage3DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, long var11, long var13);

   public static void glCompressedMultiTexSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, ByteBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glCompressedMultiTexSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      BufferChecks.checkDirect(var8);
      nglCompressedMultiTexSubImage2DEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8.remaining(), MemoryUtil.getAddress(var8), var10);
   }

   static native void nglCompressedMultiTexSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glCompressedMultiTexSubImage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glCompressedMultiTexSubImage2DEXT;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOenabled(var11);
      nglCompressedMultiTexSubImage2DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var12);
   }

   static native void nglCompressedMultiTexSubImage2DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glCompressedMultiTexSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, ByteBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glCompressedMultiTexSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOdisabled(var7);
      BufferChecks.checkDirect(var6);
      nglCompressedMultiTexSubImage1DEXT(var0, var1, var2, var3, var4, var5, var6.remaining(), MemoryUtil.getAddress(var6), var8);
   }

   static native void nglCompressedMultiTexSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glCompressedMultiTexSubImage1DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glCompressedMultiTexSubImage1DEXT;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOenabled(var9);
      nglCompressedMultiTexSubImage1DEXTBO(var0, var1, var2, var3, var4, var5, var6, var7, var10);
   }

   static native void nglCompressedMultiTexSubImage1DEXTBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glGetCompressedMultiTexImageEXT(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetCompressedMultiTexImageEXT;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglGetCompressedMultiTexImageEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetCompressedMultiTexImageEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetCompressedMultiTexImageEXT;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglGetCompressedMultiTexImageEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetCompressedMultiTexImageEXT(int var0, int var1, int var2, ShortBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetCompressedMultiTexImageEXT;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensurePackPBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglGetCompressedMultiTexImageEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetCompressedMultiTexImageEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glGetCompressedMultiTexImageEXT(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetCompressedMultiTexImageEXT;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOenabled(var5);
      nglGetCompressedMultiTexImageEXTBO(var0, var1, var2, var3, var6);
   }

   static native void nglGetCompressedMultiTexImageEXTBO(int var0, int var1, int var2, long var3, long var5);

   public static void glMatrixLoadTransposeEXT(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMatrixLoadTransposefEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 16);
      nglMatrixLoadTransposefEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglMatrixLoadTransposefEXT(int var0, long var1, long var3);

   public static void glMatrixLoadTransposeEXT(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMatrixLoadTransposedEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 16);
      nglMatrixLoadTransposedEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglMatrixLoadTransposedEXT(int var0, long var1, long var3);

   public static void glMatrixMultTransposeEXT(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMatrixMultTransposefEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 16);
      nglMatrixMultTransposefEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglMatrixMultTransposefEXT(int var0, long var1, long var3);

   public static void glMatrixMultTransposeEXT(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMatrixMultTransposedEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 16);
      nglMatrixMultTransposedEXT(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglMatrixMultTransposedEXT(int var0, long var1, long var3);

   public static void glNamedBufferDataEXT(int var0, long var1, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedBufferDataEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglNamedBufferDataEXT(var0, var1, 0L, var3, var5);
   }

   public static void glNamedBufferDataEXT(int var0, ByteBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNamedBufferDataEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglNamedBufferDataEXT(var0, (long)var1.remaining(), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glNamedBufferDataEXT(int var0, DoubleBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNamedBufferDataEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglNamedBufferDataEXT(var0, (long)(var1.remaining() << 3), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glNamedBufferDataEXT(int var0, FloatBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNamedBufferDataEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglNamedBufferDataEXT(var0, (long)(var1.remaining() << 2), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glNamedBufferDataEXT(int var0, IntBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNamedBufferDataEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglNamedBufferDataEXT(var0, (long)(var1.remaining() << 2), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glNamedBufferDataEXT(int var0, ShortBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNamedBufferDataEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglNamedBufferDataEXT(var0, (long)(var1.remaining() << 1), MemoryUtil.getAddress(var1), var2, var4);
   }

   static native void nglNamedBufferDataEXT(int var0, long var1, long var3, int var5, long var6);

   public static void glNamedBufferSubDataEXT(int var0, long var1, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedBufferSubDataEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglNamedBufferSubDataEXT(var0, var1, (long)var3.remaining(), MemoryUtil.getAddress(var3), var5);
   }

   public static void glNamedBufferSubDataEXT(int var0, long var1, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedBufferSubDataEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglNamedBufferSubDataEXT(var0, var1, (long)(var3.remaining() << 3), MemoryUtil.getAddress(var3), var5);
   }

   public static void glNamedBufferSubDataEXT(int var0, long var1, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedBufferSubDataEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglNamedBufferSubDataEXT(var0, var1, (long)(var3.remaining() << 2), MemoryUtil.getAddress(var3), var5);
   }

   public static void glNamedBufferSubDataEXT(int var0, long var1, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedBufferSubDataEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglNamedBufferSubDataEXT(var0, var1, (long)(var3.remaining() << 2), MemoryUtil.getAddress(var3), var5);
   }

   public static void glNamedBufferSubDataEXT(int var0, long var1, ShortBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedBufferSubDataEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglNamedBufferSubDataEXT(var0, var1, (long)(var3.remaining() << 1), MemoryUtil.getAddress(var3), var5);
   }

   static native void nglNamedBufferSubDataEXT(int var0, long var1, long var3, long var5, long var7);

   public static ByteBuffer glMapNamedBufferEXT(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMapNamedBufferEXT;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      ByteBuffer var6 = nglMapNamedBufferEXT(var0, var1, (long)GLChecks.getNamedBufferObjectSize(var3, var0), var2, var4);
      return LWJGLUtil.CHECKS && var6 == null ? null : var6.order(ByteOrder.nativeOrder());
   }

   public static ByteBuffer glMapNamedBufferEXT(int var0, int var1, long var2, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMapNamedBufferEXT;
      BufferChecks.checkFunctionAddress(var6);
      if (var4 != null) {
         BufferChecks.checkDirect(var4);
      }

      ByteBuffer var8 = nglMapNamedBufferEXT(var0, var1, var2, var4, var6);
      return LWJGLUtil.CHECKS && var8 == null ? null : var8.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglMapNamedBufferEXT(int var0, int var1, long var2, ByteBuffer var4, long var5);

   public static boolean glUnmapNamedBufferEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glUnmapNamedBufferEXT;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglUnmapNamedBufferEXT(var0, var2);
      return var4;
   }

   static native boolean nglUnmapNamedBufferEXT(int var0, long var1);

   public static void glGetNamedBufferParameterEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetNamedBufferParameterivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetNamedBufferParameterivEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetNamedBufferParameterivEXT(int var0, int var1, long var2, long var4);

   public static int glGetNamedBufferParameterEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetNamedBufferParameterivEXT;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetNamedBufferParameterivEXT(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static ByteBuffer glGetNamedBufferPointerEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetNamedBufferPointervEXT;
      BufferChecks.checkFunctionAddress(var3);
      ByteBuffer var5 = nglGetNamedBufferPointervEXT(var0, var1, (long)GLChecks.getNamedBufferObjectSize(var2, var0), var3);
      return LWJGLUtil.CHECKS && var5 == null ? null : var5.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglGetNamedBufferPointervEXT(int var0, int var1, long var2, long var4);

   public static void glGetNamedBufferSubDataEXT(int var0, long var1, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetNamedBufferSubDataEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetNamedBufferSubDataEXT(var0, var1, (long)var3.remaining(), MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetNamedBufferSubDataEXT(int var0, long var1, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetNamedBufferSubDataEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetNamedBufferSubDataEXT(var0, var1, (long)(var3.remaining() << 3), MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetNamedBufferSubDataEXT(int var0, long var1, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetNamedBufferSubDataEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetNamedBufferSubDataEXT(var0, var1, (long)(var3.remaining() << 2), MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetNamedBufferSubDataEXT(int var0, long var1, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetNamedBufferSubDataEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetNamedBufferSubDataEXT(var0, var1, (long)(var3.remaining() << 2), MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetNamedBufferSubDataEXT(int var0, long var1, ShortBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetNamedBufferSubDataEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetNamedBufferSubDataEXT(var0, var1, (long)(var3.remaining() << 1), MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetNamedBufferSubDataEXT(int var0, long var1, long var3, long var5, long var7);

   public static void glProgramUniform1fEXT(int var0, int var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform1fEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglProgramUniform1fEXT(var0, var1, var2, var4);
   }

   static native void nglProgramUniform1fEXT(int var0, int var1, float var2, long var3);

   public static void glProgramUniform2fEXT(int var0, int var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniform2fEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglProgramUniform2fEXT(var0, var1, var2, var3, var5);
   }

   static native void nglProgramUniform2fEXT(int var0, int var1, float var2, float var3, long var4);

   public static void glProgramUniform3fEXT(int var0, int var1, float var2, float var3, float var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glProgramUniform3fEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglProgramUniform3fEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglProgramUniform3fEXT(int var0, int var1, float var2, float var3, float var4, long var5);

   public static void glProgramUniform4fEXT(int var0, int var1, float var2, float var3, float var4, float var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramUniform4fEXT;
      BufferChecks.checkFunctionAddress(var7);
      nglProgramUniform4fEXT(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglProgramUniform4fEXT(int var0, int var1, float var2, float var3, float var4, float var5, long var6);

   public static void glProgramUniform1iEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform1iEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglProgramUniform1iEXT(var0, var1, var2, var4);
   }

   static native void nglProgramUniform1iEXT(int var0, int var1, int var2, long var3);

   public static void glProgramUniform2iEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniform2iEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglProgramUniform2iEXT(var0, var1, var2, var3, var5);
   }

   static native void nglProgramUniform2iEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glProgramUniform3iEXT(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glProgramUniform3iEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglProgramUniform3iEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglProgramUniform3iEXT(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glProgramUniform4iEXT(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramUniform4iEXT;
      BufferChecks.checkFunctionAddress(var7);
      nglProgramUniform4iEXT(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglProgramUniform4iEXT(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glProgramUniform1EXT(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform1fvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform1fvEXT(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform1fvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform2EXT(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform2fvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform2fvEXT(var0, var1, var2.remaining() >> 1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform2fvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform3EXT(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform3fvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform3fvEXT(var0, var1, var2.remaining() / 3, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform3fvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform4EXT(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform4fvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform4fvEXT(var0, var1, var2.remaining() >> 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform4fvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform1EXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform1ivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform1ivEXT(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform1ivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform2EXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform2ivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform2ivEXT(var0, var1, var2.remaining() >> 1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform2ivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform3EXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform3ivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform3ivEXT(var0, var1, var2.remaining() / 3, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform3ivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform4EXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform4ivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform4ivEXT(var0, var1, var2.remaining() >> 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform4ivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniformMatrix2EXT(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix2fvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix2fvEXT(var0, var1, var3.remaining() >> 2, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix2fvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix3EXT(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix3fvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix3fvEXT(var0, var1, var3.remaining() / 9, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix3fvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix4EXT(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix4fvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix4fvEXT(var0, var1, var3.remaining() >> 4, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix4fvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix2x3EXT(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix2x3fvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix2x3fvEXT(var0, var1, var3.remaining() / 6, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix2x3fvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix3x2EXT(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix3x2fvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix3x2fvEXT(var0, var1, var3.remaining() / 6, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix3x2fvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix2x4EXT(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix2x4fvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix2x4fvEXT(var0, var1, var3.remaining() >> 3, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix2x4fvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix4x2EXT(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix4x2fvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix4x2fvEXT(var0, var1, var3.remaining() >> 3, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix4x2fvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix3x4EXT(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix3x4fvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix3x4fvEXT(var0, var1, var3.remaining() / 12, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix3x4fvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix4x3EXT(int var0, int var1, boolean var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix4x3fvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix4x3fvEXT(var0, var1, var3.remaining() / 12, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix4x3fvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glTextureBufferEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTextureBufferEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglTextureBufferEXT(var0, var1, var2, var3, var5);
   }

   static native void nglTextureBufferEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glMultiTexBufferEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexBufferEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglMultiTexBufferEXT(var0, var1, var2, var3, var5);
   }

   static native void nglMultiTexBufferEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glTextureParameterIEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTextureParameterIivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglTextureParameterIivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglTextureParameterIivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glTextureParameterIEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTextureParameterIivEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglTextureParameterIivEXT(var0, var1, var2, APIUtil.getInt(var4, var3), var5);
   }

   public static void glTextureParameterIuEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTextureParameterIuivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglTextureParameterIuivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglTextureParameterIuivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glTextureParameterIuEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTextureParameterIuivEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglTextureParameterIuivEXT(var0, var1, var2, APIUtil.getInt(var4, var3), var5);
   }

   public static void glGetTextureParameterIEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetTextureParameterIivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetTextureParameterIivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetTextureParameterIivEXT(int var0, int var1, int var2, long var3, long var5);

   public static int glGetTextureParameterIiEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTextureParameterIivEXT;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetTextureParameterIivEXT(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGetTextureParameterIuEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetTextureParameterIuivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetTextureParameterIuivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetTextureParameterIuivEXT(int var0, int var1, int var2, long var3, long var5);

   public static int glGetTextureParameterIuiEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTextureParameterIuivEXT;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetTextureParameterIuivEXT(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glMultiTexParameterIEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexParameterIivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglMultiTexParameterIivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglMultiTexParameterIivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glMultiTexParameterIEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexParameterIivEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglMultiTexParameterIivEXT(var0, var1, var2, APIUtil.getInt(var4, var3), var5);
   }

   public static void glMultiTexParameterIuEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexParameterIuivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglMultiTexParameterIuivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglMultiTexParameterIuivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glMultiTexParameterIuEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glMultiTexParameterIuivEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglMultiTexParameterIuivEXT(var0, var1, var2, APIUtil.getInt(var4, var3), var5);
   }

   public static void glGetMultiTexParameterIEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetMultiTexParameterIivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetMultiTexParameterIivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetMultiTexParameterIivEXT(int var0, int var1, int var2, long var3, long var5);

   public static int glGetMultiTexParameterIiEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetMultiTexParameterIivEXT;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetMultiTexParameterIivEXT(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGetMultiTexParameterIuEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetMultiTexParameterIuivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetMultiTexParameterIuivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetMultiTexParameterIuivEXT(int var0, int var1, int var2, long var3, long var5);

   public static int glGetMultiTexParameterIuiEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetMultiTexParameterIuivEXT;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetMultiTexParameterIuivEXT(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glProgramUniform1uiEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform1uiEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglProgramUniform1uiEXT(var0, var1, var2, var4);
   }

   static native void nglProgramUniform1uiEXT(int var0, int var1, int var2, long var3);

   public static void glProgramUniform2uiEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniform2uiEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglProgramUniform2uiEXT(var0, var1, var2, var3, var5);
   }

   static native void nglProgramUniform2uiEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glProgramUniform3uiEXT(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glProgramUniform3uiEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglProgramUniform3uiEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglProgramUniform3uiEXT(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glProgramUniform4uiEXT(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramUniform4uiEXT;
      BufferChecks.checkFunctionAddress(var7);
      nglProgramUniform4uiEXT(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglProgramUniform4uiEXT(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glProgramUniform1uEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform1uivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform1uivEXT(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform1uivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform2uEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform2uivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform2uivEXT(var0, var1, var2.remaining() >> 1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform2uivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform3uEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform3uivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform3uivEXT(var0, var1, var2.remaining() / 3, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform3uivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform4uEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform4uivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform4uivEXT(var0, var1, var2.remaining() >> 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform4uivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glNamedProgramLocalParameters4EXT(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedProgramLocalParameters4fvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglNamedProgramLocalParameters4fvEXT(var0, var1, var2, var3.remaining() >> 2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglNamedProgramLocalParameters4fvEXT(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glNamedProgramLocalParameterI4iEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glNamedProgramLocalParameterI4iEXT;
      BufferChecks.checkFunctionAddress(var8);
      nglNamedProgramLocalParameterI4iEXT(var0, var1, var2, var3, var4, var5, var6, var8);
   }

   static native void nglNamedProgramLocalParameterI4iEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7);

   public static void glNamedProgramLocalParameterI4EXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedProgramLocalParameterI4ivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglNamedProgramLocalParameterI4ivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglNamedProgramLocalParameterI4ivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glNamedProgramLocalParametersI4EXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedProgramLocalParametersI4ivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglNamedProgramLocalParametersI4ivEXT(var0, var1, var2, var3.remaining() >> 2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglNamedProgramLocalParametersI4ivEXT(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glNamedProgramLocalParameterI4uiEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glNamedProgramLocalParameterI4uiEXT;
      BufferChecks.checkFunctionAddress(var8);
      nglNamedProgramLocalParameterI4uiEXT(var0, var1, var2, var3, var4, var5, var6, var8);
   }

   static native void nglNamedProgramLocalParameterI4uiEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7);

   public static void glNamedProgramLocalParameterI4uEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedProgramLocalParameterI4uivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglNamedProgramLocalParameterI4uivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglNamedProgramLocalParameterI4uivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glNamedProgramLocalParametersI4uEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedProgramLocalParametersI4uivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglNamedProgramLocalParametersI4uivEXT(var0, var1, var2, var3.remaining() >> 2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglNamedProgramLocalParametersI4uivEXT(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glGetNamedProgramLocalParameterIEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetNamedProgramLocalParameterIivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetNamedProgramLocalParameterIivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetNamedProgramLocalParameterIivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glGetNamedProgramLocalParameterIuEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetNamedProgramLocalParameterIuivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetNamedProgramLocalParameterIuivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetNamedProgramLocalParameterIuivEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glNamedRenderbufferStorageEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedRenderbufferStorageEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglNamedRenderbufferStorageEXT(var0, var1, var2, var3, var5);
   }

   static native void nglNamedRenderbufferStorageEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glGetNamedRenderbufferParameterEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetNamedRenderbufferParameterivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetNamedRenderbufferParameterivEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetNamedRenderbufferParameterivEXT(int var0, int var1, long var2, long var4);

   public static int glGetNamedRenderbufferParameterEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetNamedRenderbufferParameterivEXT;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetNamedRenderbufferParameterivEXT(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glNamedRenderbufferStorageMultisampleEXT(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glNamedRenderbufferStorageMultisampleEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglNamedRenderbufferStorageMultisampleEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglNamedRenderbufferStorageMultisampleEXT(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glNamedRenderbufferStorageMultisampleCoverageEXT(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glNamedRenderbufferStorageMultisampleCoverageEXT;
      BufferChecks.checkFunctionAddress(var7);
      nglNamedRenderbufferStorageMultisampleCoverageEXT(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglNamedRenderbufferStorageMultisampleCoverageEXT(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static int glCheckNamedFramebufferStatusEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glCheckNamedFramebufferStatusEXT;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nglCheckNamedFramebufferStatusEXT(var0, var1, var3);
      return var5;
   }

   static native int nglCheckNamedFramebufferStatusEXT(int var0, int var1, long var2);

   public static void glNamedFramebufferTexture1DEXT(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glNamedFramebufferTexture1DEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglNamedFramebufferTexture1DEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglNamedFramebufferTexture1DEXT(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glNamedFramebufferTexture2DEXT(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glNamedFramebufferTexture2DEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglNamedFramebufferTexture2DEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglNamedFramebufferTexture2DEXT(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glNamedFramebufferTexture3DEXT(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glNamedFramebufferTexture3DEXT;
      BufferChecks.checkFunctionAddress(var7);
      nglNamedFramebufferTexture3DEXT(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglNamedFramebufferTexture3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glNamedFramebufferRenderbufferEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedFramebufferRenderbufferEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglNamedFramebufferRenderbufferEXT(var0, var1, var2, var3, var5);
   }

   static native void nglNamedFramebufferRenderbufferEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glGetNamedFramebufferAttachmentParameterEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetNamedFramebufferAttachmentParameterivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetNamedFramebufferAttachmentParameterivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetNamedFramebufferAttachmentParameterivEXT(int var0, int var1, int var2, long var3, long var5);

   public static int glGetNamedFramebufferAttachmentParameterEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetNamedFramebufferAttachmentParameterivEXT;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetNamedFramebufferAttachmentParameterivEXT(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGenerateTextureMipmapEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGenerateTextureMipmapEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglGenerateTextureMipmapEXT(var0, var1, var3);
   }

   static native void nglGenerateTextureMipmapEXT(int var0, int var1, long var2);

   public static void glGenerateMultiTexMipmapEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGenerateMultiTexMipmapEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglGenerateMultiTexMipmapEXT(var0, var1, var3);
   }

   static native void nglGenerateMultiTexMipmapEXT(int var0, int var1, long var2);

   public static void glFramebufferDrawBufferEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glFramebufferDrawBufferEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglFramebufferDrawBufferEXT(var0, var1, var3);
   }

   static native void nglFramebufferDrawBufferEXT(int var0, int var1, long var2);

   public static void glFramebufferDrawBuffersEXT(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glFramebufferDrawBuffersEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglFramebufferDrawBuffersEXT(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglFramebufferDrawBuffersEXT(int var0, int var1, long var2, long var4);

   public static void glFramebufferReadBufferEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glFramebufferReadBufferEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglFramebufferReadBufferEXT(var0, var1, var3);
   }

   static native void nglFramebufferReadBufferEXT(int var0, int var1, long var2);

   public static void glGetFramebufferParameterEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetFramebufferParameterivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetFramebufferParameterivEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetFramebufferParameterivEXT(int var0, int var1, long var2, long var4);

   public static int glGetFramebufferParameterEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetFramebufferParameterivEXT;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetFramebufferParameterivEXT(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glNamedCopyBufferSubDataEXT(int var0, int var1, long var2, long var4, long var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glNamedCopyBufferSubDataEXT;
      BufferChecks.checkFunctionAddress(var9);
      nglNamedCopyBufferSubDataEXT(var0, var1, var2, var4, var6, var9);
   }

   static native void nglNamedCopyBufferSubDataEXT(int var0, int var1, long var2, long var4, long var6, long var8);

   public static void glNamedFramebufferTextureEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedFramebufferTextureEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglNamedFramebufferTextureEXT(var0, var1, var2, var3, var5);
   }

   static native void nglNamedFramebufferTextureEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glNamedFramebufferTextureLayerEXT(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glNamedFramebufferTextureLayerEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglNamedFramebufferTextureLayerEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglNamedFramebufferTextureLayerEXT(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glNamedFramebufferTextureFaceEXT(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glNamedFramebufferTextureFaceEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglNamedFramebufferTextureFaceEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglNamedFramebufferTextureFaceEXT(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glTextureRenderbufferEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTextureRenderbufferEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglTextureRenderbufferEXT(var0, var1, var2, var4);
   }

   static native void nglTextureRenderbufferEXT(int var0, int var1, int var2, long var3);

   public static void glMultiTexRenderbufferEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMultiTexRenderbufferEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglMultiTexRenderbufferEXT(var0, var1, var2, var4);
   }

   static native void nglMultiTexRenderbufferEXT(int var0, int var1, int var2, long var3);

   public static void glVertexArrayVertexOffsetEXT(int var0, int var1, int var2, int var3, int var4, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glVertexArrayVertexOffsetEXT;
      BufferChecks.checkFunctionAddress(var8);
      nglVertexArrayVertexOffsetEXT(var0, var1, var2, var3, var4, var5, var8);
   }

   static native void nglVertexArrayVertexOffsetEXT(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glVertexArrayColorOffsetEXT(int var0, int var1, int var2, int var3, int var4, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glVertexArrayColorOffsetEXT;
      BufferChecks.checkFunctionAddress(var8);
      nglVertexArrayColorOffsetEXT(var0, var1, var2, var3, var4, var5, var8);
   }

   static native void nglVertexArrayColorOffsetEXT(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glVertexArrayEdgeFlagOffsetEXT(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexArrayEdgeFlagOffsetEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexArrayEdgeFlagOffsetEXT(var0, var1, var2, var3, var6);
   }

   static native void nglVertexArrayEdgeFlagOffsetEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glVertexArrayIndexOffsetEXT(int var0, int var1, int var2, int var3, long var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glVertexArrayIndexOffsetEXT;
      BufferChecks.checkFunctionAddress(var7);
      nglVertexArrayIndexOffsetEXT(var0, var1, var2, var3, var4, var7);
   }

   static native void nglVertexArrayIndexOffsetEXT(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glVertexArrayNormalOffsetEXT(int var0, int var1, int var2, int var3, long var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glVertexArrayNormalOffsetEXT;
      BufferChecks.checkFunctionAddress(var7);
      nglVertexArrayNormalOffsetEXT(var0, var1, var2, var3, var4, var7);
   }

   static native void nglVertexArrayNormalOffsetEXT(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glVertexArrayTexCoordOffsetEXT(int var0, int var1, int var2, int var3, int var4, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glVertexArrayTexCoordOffsetEXT;
      BufferChecks.checkFunctionAddress(var8);
      nglVertexArrayTexCoordOffsetEXT(var0, var1, var2, var3, var4, var5, var8);
   }

   static native void nglVertexArrayTexCoordOffsetEXT(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glVertexArrayMultiTexCoordOffsetEXT(int var0, int var1, int var2, int var3, int var4, int var5, long var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glVertexArrayMultiTexCoordOffsetEXT;
      BufferChecks.checkFunctionAddress(var9);
      nglVertexArrayMultiTexCoordOffsetEXT(var0, var1, var2, var3, var4, var5, var6, var9);
   }

   static native void nglVertexArrayMultiTexCoordOffsetEXT(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8);

   public static void glVertexArrayFogCoordOffsetEXT(int var0, int var1, int var2, int var3, long var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glVertexArrayFogCoordOffsetEXT;
      BufferChecks.checkFunctionAddress(var7);
      nglVertexArrayFogCoordOffsetEXT(var0, var1, var2, var3, var4, var7);
   }

   static native void nglVertexArrayFogCoordOffsetEXT(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glVertexArraySecondaryColorOffsetEXT(int var0, int var1, int var2, int var3, int var4, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glVertexArraySecondaryColorOffsetEXT;
      BufferChecks.checkFunctionAddress(var8);
      nglVertexArraySecondaryColorOffsetEXT(var0, var1, var2, var3, var4, var5, var8);
   }

   static native void nglVertexArraySecondaryColorOffsetEXT(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glVertexArrayVertexAttribOffsetEXT(int var0, int var1, int var2, int var3, int var4, boolean var5, int var6, long var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glVertexArrayVertexAttribOffsetEXT;
      BufferChecks.checkFunctionAddress(var10);
      nglVertexArrayVertexAttribOffsetEXT(var0, var1, var2, var3, var4, var5, var6, var7, var10);
   }

   static native void nglVertexArrayVertexAttribOffsetEXT(int var0, int var1, int var2, int var3, int var4, boolean var5, int var6, long var7, long var9);

   public static void glVertexArrayVertexAttribIOffsetEXT(int var0, int var1, int var2, int var3, int var4, int var5, long var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glVertexArrayVertexAttribIOffsetEXT;
      BufferChecks.checkFunctionAddress(var9);
      nglVertexArrayVertexAttribIOffsetEXT(var0, var1, var2, var3, var4, var5, var6, var9);
   }

   static native void nglVertexArrayVertexAttribIOffsetEXT(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8);

   public static void glEnableVertexArrayEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glEnableVertexArrayEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglEnableVertexArrayEXT(var0, var1, var3);
   }

   static native void nglEnableVertexArrayEXT(int var0, int var1, long var2);

   public static void glDisableVertexArrayEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDisableVertexArrayEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglDisableVertexArrayEXT(var0, var1, var3);
   }

   static native void nglDisableVertexArrayEXT(int var0, int var1, long var2);

   public static void glEnableVertexArrayAttribEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glEnableVertexArrayAttribEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglEnableVertexArrayAttribEXT(var0, var1, var3);
   }

   static native void nglEnableVertexArrayAttribEXT(int var0, int var1, long var2);

   public static void glDisableVertexArrayAttribEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDisableVertexArrayAttribEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglDisableVertexArrayAttribEXT(var0, var1, var3);
   }

   static native void nglDisableVertexArrayAttribEXT(int var0, int var1, long var2);

   public static void glGetVertexArrayIntegerEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexArrayIntegervEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 16);
      nglGetVertexArrayIntegervEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexArrayIntegervEXT(int var0, int var1, long var2, long var4);

   public static int glGetVertexArrayIntegerEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetVertexArrayIntegervEXT;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetVertexArrayIntegervEXT(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static ByteBuffer glGetVertexArrayPointerEXT(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetVertexArrayPointervEXT;
      BufferChecks.checkFunctionAddress(var5);
      ByteBuffer var7 = nglGetVertexArrayPointervEXT(var0, var1, var2, var5);
      return LWJGLUtil.CHECKS && var7 == null ? null : var7.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglGetVertexArrayPointervEXT(int var0, int var1, long var2, long var4);

   public static void glGetVertexArrayIntegerEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetVertexArrayIntegeri_vEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 16);
      nglGetVertexArrayIntegeri_vEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetVertexArrayIntegeri_vEXT(int var0, int var1, int var2, long var3, long var5);

   public static int glGetVertexArrayIntegeriEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexArrayIntegeri_vEXT;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetVertexArrayIntegeri_vEXT(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static ByteBuffer glGetVertexArrayPointeri_EXT(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetVertexArrayPointeri_vEXT;
      BufferChecks.checkFunctionAddress(var6);
      ByteBuffer var8 = nglGetVertexArrayPointeri_vEXT(var0, var1, var2, var3, var6);
      return LWJGLUtil.CHECKS && var8 == null ? null : var8.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglGetVertexArrayPointeri_vEXT(int var0, int var1, int var2, long var3, long var5);

   public static ByteBuffer glMapNamedBufferRangeEXT(int var0, long var1, long var3, int var5, ByteBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glMapNamedBufferRangeEXT;
      BufferChecks.checkFunctionAddress(var8);
      if (var6 != null) {
         BufferChecks.checkDirect(var6);
      }

      ByteBuffer var10 = nglMapNamedBufferRangeEXT(var0, var1, var3, var5, var6, var8);
      return LWJGLUtil.CHECKS && var10 == null ? null : var10.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglMapNamedBufferRangeEXT(int var0, long var1, long var3, int var5, ByteBuffer var6, long var7);

   public static void glFlushMappedNamedBufferRangeEXT(int var0, long var1, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glFlushMappedNamedBufferRangeEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglFlushMappedNamedBufferRangeEXT(var0, var1, var3, var6);
   }

   static native void nglFlushMappedNamedBufferRangeEXT(int var0, long var1, long var3, long var5);
}
