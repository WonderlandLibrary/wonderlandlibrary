package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

public final class GL44 {
   public static final int GL_MAX_VERTEX_ATTRIB_STRIDE = 33509;
   public static final int GL_MAP_PERSISTENT_BIT = 64;
   public static final int GL_MAP_COHERENT_BIT = 128;
   public static final int GL_DYNAMIC_STORAGE_BIT = 256;
   public static final int GL_CLIENT_STORAGE_BIT = 512;
   public static final int GL_BUFFER_IMMUTABLE_STORAGE = 33311;
   public static final int GL_BUFFER_STORAGE_FLAGS = 33312;
   public static final int GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT = 16384;
   public static final int GL_CLEAR_TEXTURE = 37733;
   public static final int GL_LOCATION_COMPONENT = 37706;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_INDEX = 37707;
   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_STRIDE = 37708;
   public static final int GL_QUERY_RESULT_NO_WAIT = 37268;
   public static final int GL_QUERY_BUFFER = 37266;
   public static final int GL_QUERY_BUFFER_BINDING = 37267;
   public static final int GL_QUERY_BUFFER_BARRIER_BIT = 32768;
   public static final int GL_MIRROR_CLAMP_TO_EDGE = 34627;

   private GL44() {
   }

   public static void glBufferStorage(int var0, ByteBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBufferStorage;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglBufferStorage(var0, (long)var1.remaining(), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glBufferStorage(int var0, DoubleBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBufferStorage;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglBufferStorage(var0, (long)(var1.remaining() << 3), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glBufferStorage(int var0, FloatBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBufferStorage;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglBufferStorage(var0, (long)(var1.remaining() << 2), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glBufferStorage(int var0, IntBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBufferStorage;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglBufferStorage(var0, (long)(var1.remaining() << 2), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glBufferStorage(int var0, ShortBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBufferStorage;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglBufferStorage(var0, (long)(var1.remaining() << 1), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glBufferStorage(int var0, LongBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBufferStorage;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglBufferStorage(var0, (long)(var1.remaining() << 3), MemoryUtil.getAddress(var1), var2, var4);
   }

   static native void nglBufferStorage(int var0, long var1, long var3, int var5, long var6);

   public static void glBufferStorage(int var0, long var1, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBufferStorage;
      BufferChecks.checkFunctionAddress(var5);
      nglBufferStorage(var0, var1, 0L, var3, var5);
   }

   public static void glClearTexImage(int var0, int var1, int var2, int var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glClearTexImage;
      BufferChecks.checkFunctionAddress(var6);
      if (var4 != null) {
         BufferChecks.checkBuffer((ByteBuffer)var4, 1);
      }

      nglClearTexImage(var0, var1, var2, var3, MemoryUtil.getAddressSafe(var4), var6);
   }

   public static void glClearTexImage(int var0, int var1, int var2, int var3, DoubleBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glClearTexImage;
      BufferChecks.checkFunctionAddress(var6);
      if (var4 != null) {
         BufferChecks.checkBuffer((DoubleBuffer)var4, 1);
      }

      nglClearTexImage(var0, var1, var2, var3, MemoryUtil.getAddressSafe(var4), var6);
   }

   public static void glClearTexImage(int var0, int var1, int var2, int var3, FloatBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glClearTexImage;
      BufferChecks.checkFunctionAddress(var6);
      if (var4 != null) {
         BufferChecks.checkBuffer((FloatBuffer)var4, 1);
      }

      nglClearTexImage(var0, var1, var2, var3, MemoryUtil.getAddressSafe(var4), var6);
   }

   public static void glClearTexImage(int var0, int var1, int var2, int var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glClearTexImage;
      BufferChecks.checkFunctionAddress(var6);
      if (var4 != null) {
         BufferChecks.checkBuffer((IntBuffer)var4, 1);
      }

      nglClearTexImage(var0, var1, var2, var3, MemoryUtil.getAddressSafe(var4), var6);
   }

   public static void glClearTexImage(int var0, int var1, int var2, int var3, ShortBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glClearTexImage;
      BufferChecks.checkFunctionAddress(var6);
      if (var4 != null) {
         BufferChecks.checkBuffer((ShortBuffer)var4, 1);
      }

      nglClearTexImage(var0, var1, var2, var3, MemoryUtil.getAddressSafe(var4), var6);
   }

   public static void glClearTexImage(int var0, int var1, int var2, int var3, LongBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glClearTexImage;
      BufferChecks.checkFunctionAddress(var6);
      if (var4 != null) {
         BufferChecks.checkBuffer((LongBuffer)var4, 1);
      }

      nglClearTexImage(var0, var1, var2, var3, MemoryUtil.getAddressSafe(var4), var6);
   }

   static native void nglClearTexImage(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glClearTexSubImage(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, ByteBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glClearTexSubImage;
      BufferChecks.checkFunctionAddress(var12);
      if (var10 != null) {
         BufferChecks.checkBuffer((ByteBuffer)var10, 1);
      }

      nglClearTexSubImage(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddressSafe(var10), var12);
   }

   public static void glClearTexSubImage(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, DoubleBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glClearTexSubImage;
      BufferChecks.checkFunctionAddress(var12);
      if (var10 != null) {
         BufferChecks.checkBuffer((DoubleBuffer)var10, 1);
      }

      nglClearTexSubImage(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddressSafe(var10), var12);
   }

   public static void glClearTexSubImage(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, FloatBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glClearTexSubImage;
      BufferChecks.checkFunctionAddress(var12);
      if (var10 != null) {
         BufferChecks.checkBuffer((FloatBuffer)var10, 1);
      }

      nglClearTexSubImage(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddressSafe(var10), var12);
   }

   public static void glClearTexSubImage(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, IntBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glClearTexSubImage;
      BufferChecks.checkFunctionAddress(var12);
      if (var10 != null) {
         BufferChecks.checkBuffer((IntBuffer)var10, 1);
      }

      nglClearTexSubImage(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddressSafe(var10), var12);
   }

   public static void glClearTexSubImage(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, ShortBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glClearTexSubImage;
      BufferChecks.checkFunctionAddress(var12);
      if (var10 != null) {
         BufferChecks.checkBuffer((ShortBuffer)var10, 1);
      }

      nglClearTexSubImage(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddressSafe(var10), var12);
   }

   public static void glClearTexSubImage(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, LongBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glClearTexSubImage;
      BufferChecks.checkFunctionAddress(var12);
      if (var10 != null) {
         BufferChecks.checkBuffer((LongBuffer)var10, 1);
      }

      nglClearTexSubImage(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddressSafe(var10), var12);
   }

   static native void nglClearTexSubImage(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10, long var12);

   public static void glBindBuffersBase(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBindBuffersBase;
      BufferChecks.checkFunctionAddress(var5);
      if (var3 != null) {
         BufferChecks.checkBuffer(var3, var2);
      }

      nglBindBuffersBase(var0, var1, var2, MemoryUtil.getAddressSafe(var3), var5);
   }

   static native void nglBindBuffersBase(int var0, int var1, int var2, long var3, long var5);

   public static void glBindBuffersRange(int var0, int var1, int var2, IntBuffer var3, PointerBuffer var4, PointerBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glBindBuffersRange;
      BufferChecks.checkFunctionAddress(var7);
      if (var3 != null) {
         BufferChecks.checkBuffer(var3, var2);
      }

      if (var4 != null) {
         BufferChecks.checkBuffer(var4, var2);
      }

      if (var5 != null) {
         BufferChecks.checkBuffer(var5, var2);
      }

      nglBindBuffersRange(var0, var1, var2, MemoryUtil.getAddressSafe(var3), MemoryUtil.getAddressSafe(var4), MemoryUtil.getAddressSafe(var5), var7);
   }

   static native void nglBindBuffersRange(int var0, int var1, int var2, long var3, long var5, long var7, long var9);

   public static void glBindTextures(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBindTextures;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkBuffer(var2, var1);
      }

      nglBindTextures(var0, var1, MemoryUtil.getAddressSafe(var2), var4);
   }

   static native void nglBindTextures(int var0, int var1, long var2, long var4);

   public static void glBindSamplers(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBindSamplers;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkBuffer(var2, var1);
      }

      nglBindSamplers(var0, var1, MemoryUtil.getAddressSafe(var2), var4);
   }

   static native void nglBindSamplers(int var0, int var1, long var2, long var4);

   public static void glBindImageTextures(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBindImageTextures;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkBuffer(var2, var1);
      }

      nglBindImageTextures(var0, var1, MemoryUtil.getAddressSafe(var2), var4);
   }

   static native void nglBindImageTextures(int var0, int var1, long var2, long var4);

   public static void glBindVertexBuffers(int var0, int var1, IntBuffer var2, PointerBuffer var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glBindVertexBuffers;
      BufferChecks.checkFunctionAddress(var6);
      if (var2 != null) {
         BufferChecks.checkBuffer(var2, var1);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer(var3, var1);
      }

      if (var4 != null) {
         BufferChecks.checkBuffer(var4, var1);
      }

      nglBindVertexBuffers(var0, var1, MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), MemoryUtil.getAddressSafe(var4), var6);
   }

   static native void nglBindVertexBuffers(int var0, int var1, long var2, long var4, long var6, long var8);
}
