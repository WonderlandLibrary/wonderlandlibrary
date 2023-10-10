package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

public final class CL10GL {
   public static final int CL_GL_OBJECT_BUFFER = 8192;
   public static final int CL_GL_OBJECT_TEXTURE2D = 8193;
   public static final int CL_GL_OBJECT_TEXTURE3D = 8194;
   public static final int CL_GL_OBJECT_RENDERBUFFER = 8195;
   public static final int CL_GL_TEXTURE_TARGET = 8196;
   public static final int CL_GL_MIPMAP_LEVEL = 8197;

   private CL10GL() {
   }

   public static CLMem clCreateFromGLBuffer(CLContext var0, long var1, int var3, IntBuffer var4) {
      long var5 = CLCapabilities.clCreateFromGLBuffer;
      BufferChecks.checkFunctionAddress(var5);
      if (var4 != null) {
         BufferChecks.checkBuffer((IntBuffer)var4, 1);
      }

      CLMem var7 = new CLMem(nclCreateFromGLBuffer(var0.getPointer(), var1, var3, MemoryUtil.getAddressSafe(var4), var5), var0);
      return var7;
   }

   static native long nclCreateFromGLBuffer(long var0, long var2, int var4, long var5, long var7);

   public static CLMem clCreateFromGLTexture2D(CLContext var0, long var1, int var3, int var4, int var5, IntBuffer var6) {
      long var7 = CLCapabilities.clCreateFromGLTexture2D;
      BufferChecks.checkFunctionAddress(var7);
      if (var6 != null) {
         BufferChecks.checkBuffer((IntBuffer)var6, 1);
      }

      CLMem var9 = new CLMem(nclCreateFromGLTexture2D(var0.getPointer(), var1, var3, var4, var5, MemoryUtil.getAddressSafe(var6), var7), var0);
      return var9;
   }

   static native long nclCreateFromGLTexture2D(long var0, long var2, int var4, int var5, int var6, long var7, long var9);

   public static CLMem clCreateFromGLTexture3D(CLContext var0, long var1, int var3, int var4, int var5, IntBuffer var6) {
      long var7 = CLCapabilities.clCreateFromGLTexture3D;
      BufferChecks.checkFunctionAddress(var7);
      if (var6 != null) {
         BufferChecks.checkBuffer((IntBuffer)var6, 1);
      }

      CLMem var9 = new CLMem(nclCreateFromGLTexture3D(var0.getPointer(), var1, var3, var4, var5, MemoryUtil.getAddressSafe(var6), var7), var0);
      return var9;
   }

   static native long nclCreateFromGLTexture3D(long var0, long var2, int var4, int var5, int var6, long var7, long var9);

   public static CLMem clCreateFromGLRenderbuffer(CLContext var0, long var1, int var3, IntBuffer var4) {
      long var5 = CLCapabilities.clCreateFromGLRenderbuffer;
      BufferChecks.checkFunctionAddress(var5);
      if (var4 != null) {
         BufferChecks.checkBuffer((IntBuffer)var4, 1);
      }

      CLMem var7 = new CLMem(nclCreateFromGLRenderbuffer(var0.getPointer(), var1, var3, MemoryUtil.getAddressSafe(var4), var5), var0);
      return var7;
   }

   static native long nclCreateFromGLRenderbuffer(long var0, long var2, int var4, long var5, long var7);

   public static int clGetGLObjectInfo(CLMem var0, IntBuffer var1, IntBuffer var2) {
      long var3 = CLCapabilities.clGetGLObjectInfo;
      BufferChecks.checkFunctionAddress(var3);
      if (var1 != null) {
         BufferChecks.checkBuffer((IntBuffer)var1, 1);
      }

      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      int var5 = nclGetGLObjectInfo(var0.getPointer(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddressSafe(var2), var3);
      return var5;
   }

   static native int nclGetGLObjectInfo(long var0, long var2, long var4, long var6);

   public static int clGetGLTextureInfo(CLMem var0, int var1, ByteBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clGetGLTextureInfo;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      int var6 = nclGetGLTextureInfo(var0.getPointer(), var1, (long)(var2 == null ? 0 : var2.remaining()), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      return var6;
   }

   static native int nclGetGLTextureInfo(long var0, int var2, long var3, long var5, long var7, long var9);

   public static int clEnqueueAcquireGLObjects(CLCommandQueue var0, PointerBuffer var1, PointerBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clEnqueueAcquireGLObjects;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((PointerBuffer)var1, 1);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      int var6 = nclEnqueueAcquireGLObjects(var0.getPointer(), var1.remaining(), MemoryUtil.getAddress(var1), var2 == null ? 0 : var2.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      if (var6 == 0) {
         var0.registerCLEvent(var3);
      }

      return var6;
   }

   static native int nclEnqueueAcquireGLObjects(long var0, int var2, long var3, int var5, long var6, long var8, long var10);

   public static int clEnqueueAcquireGLObjects(CLCommandQueue var0, CLMem var1, PointerBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clEnqueueAcquireGLObjects;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      int var6 = nclEnqueueAcquireGLObjects(var0.getPointer(), 1, APIUtil.getPointer(var1), var2 == null ? 0 : var2.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      if (var6 == 0) {
         var0.registerCLEvent(var3);
      }

      return var6;
   }

   public static int clEnqueueReleaseGLObjects(CLCommandQueue var0, PointerBuffer var1, PointerBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clEnqueueReleaseGLObjects;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((PointerBuffer)var1, 1);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      int var6 = nclEnqueueReleaseGLObjects(var0.getPointer(), var1.remaining(), MemoryUtil.getAddress(var1), var2 == null ? 0 : var2.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      if (var6 == 0) {
         var0.registerCLEvent(var3);
      }

      return var6;
   }

   static native int nclEnqueueReleaseGLObjects(long var0, int var2, long var3, int var5, long var6, long var8, long var10);

   public static int clEnqueueReleaseGLObjects(CLCommandQueue var0, CLMem var1, PointerBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clEnqueueReleaseGLObjects;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      int var6 = nclEnqueueReleaseGLObjects(var0.getPointer(), 1, APIUtil.getPointer(var1), var2 == null ? 0 : var2.remaining(), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      if (var6 == 0) {
         var0.registerCLEvent(var3);
      }

      return var6;
   }
}
