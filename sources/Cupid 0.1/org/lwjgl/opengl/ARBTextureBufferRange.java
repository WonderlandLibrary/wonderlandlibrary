package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBTextureBufferRange {
  public static final int GL_TEXTURE_BUFFER_OFFSET = 37277;
  
  public static final int GL_TEXTURE_BUFFER_SIZE = 37278;
  
  public static final int GL_TEXTURE_BUFFER_OFFSET_ALIGNMENT = 37279;
  
  public static void glTexBufferRange(int target, int internalformat, int buffer, long offset, long size) {
    GL43.glTexBufferRange(target, internalformat, buffer, offset, size);
  }
  
  public static void glTextureBufferRangeEXT(int texture, int target, int internalformat, int buffer, long offset, long size) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTextureBufferRangeEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTextureBufferRangeEXT(texture, target, internalformat, buffer, offset, size, function_pointer);
  }
  
  static native void nglTextureBufferRangeEXT(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2, long paramLong3);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBTextureBufferRange.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */