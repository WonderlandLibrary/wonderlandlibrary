package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVCopyImage {
  public static void glCopyImageSubDataNV(int srcName, int srcTarget, int srcLevel, int srcX, int srcY, int srcZ, int dstName, int dstTarget, int dstLevel, int dstX, int dstY, int dstZ, int width, int height, int depth) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glCopyImageSubDataNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglCopyImageSubDataNV(srcName, srcTarget, srcLevel, srcX, srcY, srcZ, dstName, dstTarget, dstLevel, dstX, dstY, dstZ, width, height, depth, function_pointer);
  }
  
  static native void nglCopyImageSubDataNV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\NVCopyImage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */