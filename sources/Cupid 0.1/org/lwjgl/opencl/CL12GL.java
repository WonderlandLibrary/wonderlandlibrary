package org.lwjgl.opencl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class CL12GL {
  public static final int CL_GL_OBJECT_TEXTURE2D_ARRAY = 8206;
  
  public static final int CL_GL_OBJECT_TEXTURE1D = 8207;
  
  public static final int CL_GL_OBJECT_TEXTURE1D_ARRAY = 8208;
  
  public static final int CL_GL_OBJECT_TEXTURE_BUFFER = 8209;
  
  public static CLMem clCreateFromGLTexture(CLContext context, long flags, int target, int miplevel, int texture, IntBuffer errcode_ret) {
    long function_pointer = CLCapabilities.clCreateFromGLTexture;
    BufferChecks.checkFunctionAddress(function_pointer);
    if (errcode_ret != null)
      BufferChecks.checkBuffer(errcode_ret, 1); 
    CLMem __result = new CLMem(nclCreateFromGLTexture(context.getPointer(), flags, target, miplevel, texture, MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
    return __result;
  }
  
  static native long nclCreateFromGLTexture(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, long paramLong3, long paramLong4);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\CL12GL.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */