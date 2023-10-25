package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVTextureBarrier {
  public static void glTextureBarrierNV() {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTextureBarrierNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTextureBarrierNV(function_pointer);
  }
  
  static native void nglTextureBarrierNV(long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\NVTextureBarrier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */