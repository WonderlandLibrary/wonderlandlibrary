package org.lwjgl.opencl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class CL12GL {
   public static final int CL_GL_OBJECT_TEXTURE2D_ARRAY = 8206;
   public static final int CL_GL_OBJECT_TEXTURE1D = 8207;
   public static final int CL_GL_OBJECT_TEXTURE1D_ARRAY = 8208;
   public static final int CL_GL_OBJECT_TEXTURE_BUFFER = 8209;

   private CL12GL() {
   }

   public static CLMem clCreateFromGLTexture(CLContext var0, long var1, int var3, int var4, int var5, IntBuffer var6) {
      long var7 = CLCapabilities.clCreateFromGLTexture;
      BufferChecks.checkFunctionAddress(var7);
      if (var6 != null) {
         BufferChecks.checkBuffer((IntBuffer)var6, 1);
      }

      CLMem var9 = new CLMem(nclCreateFromGLTexture(var0.getPointer(), var1, var3, var4, var5, MemoryUtil.getAddressSafe(var6), var7), var0);
      return var9;
   }

   static native long nclCreateFromGLTexture(long var0, long var2, int var4, int var5, int var6, long var7, long var9);
}
