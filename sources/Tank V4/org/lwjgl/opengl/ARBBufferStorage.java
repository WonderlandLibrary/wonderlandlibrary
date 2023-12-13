package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBBufferStorage {
   public static final int GL_MAP_PERSISTENT_BIT = 64;
   public static final int GL_MAP_COHERENT_BIT = 128;
   public static final int GL_DYNAMIC_STORAGE_BIT = 256;
   public static final int GL_CLIENT_STORAGE_BIT = 512;
   public static final int GL_BUFFER_IMMUTABLE_STORAGE = 33311;
   public static final int GL_BUFFER_STORAGE_FLAGS = 33312;
   public static final int GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT = 16384;

   private ARBBufferStorage() {
   }

   public static void glBufferStorage(int var0, ByteBuffer var1, int var2) {
      GL44.glBufferStorage(var0, var1, var2);
   }

   public static void glBufferStorage(int var0, DoubleBuffer var1, int var2) {
      GL44.glBufferStorage(var0, var1, var2);
   }

   public static void glBufferStorage(int var0, FloatBuffer var1, int var2) {
      GL44.glBufferStorage(var0, var1, var2);
   }

   public static void glBufferStorage(int var0, IntBuffer var1, int var2) {
      GL44.glBufferStorage(var0, var1, var2);
   }

   public static void glBufferStorage(int var0, ShortBuffer var1, int var2) {
      GL44.glBufferStorage(var0, var1, var2);
   }

   public static void glBufferStorage(int var0, LongBuffer var1, int var2) {
      GL44.glBufferStorage(var0, var1, var2);
   }

   public static void glBufferStorage(int var0, long var1, int var3) {
      GL44.glBufferStorage(var0, var1, var3);
   }

   public static void glNamedBufferStorageEXT(int var0, ByteBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNamedBufferStorageEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglNamedBufferStorageEXT(var0, (long)var1.remaining(), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glNamedBufferStorageEXT(int var0, DoubleBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNamedBufferStorageEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglNamedBufferStorageEXT(var0, (long)(var1.remaining() << 3), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glNamedBufferStorageEXT(int var0, FloatBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNamedBufferStorageEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglNamedBufferStorageEXT(var0, (long)(var1.remaining() << 2), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glNamedBufferStorageEXT(int var0, IntBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNamedBufferStorageEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglNamedBufferStorageEXT(var0, (long)(var1.remaining() << 2), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glNamedBufferStorageEXT(int var0, ShortBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNamedBufferStorageEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglNamedBufferStorageEXT(var0, (long)(var1.remaining() << 1), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glNamedBufferStorageEXT(int var0, LongBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNamedBufferStorageEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglNamedBufferStorageEXT(var0, (long)(var1.remaining() << 3), MemoryUtil.getAddress(var1), var2, var4);
   }

   static native void nglNamedBufferStorageEXT(int var0, long var1, long var3, int var5, long var6);

   public static void glNamedBufferStorageEXT(int var0, long var1, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNamedBufferStorageEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglNamedBufferStorageEXT(var0, var1, 0L, var3, var5);
   }
}
