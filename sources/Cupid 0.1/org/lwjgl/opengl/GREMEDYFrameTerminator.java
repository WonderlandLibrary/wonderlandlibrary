package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class GREMEDYFrameTerminator {
  public static void glFrameTerminatorGREMEDY() {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glFrameTerminatorGREMEDY;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglFrameTerminatorGREMEDY(function_pointer);
  }
  
  static native void nglFrameTerminatorGREMEDY(long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\GREMEDYFrameTerminator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */