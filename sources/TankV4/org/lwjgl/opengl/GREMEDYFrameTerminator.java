package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class GREMEDYFrameTerminator {
   private GREMEDYFrameTerminator() {
   }

   public static void glFrameTerminatorGREMEDY() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glFrameTerminatorGREMEDY;
      BufferChecks.checkFunctionAddress(var1);
      nglFrameTerminatorGREMEDY(var1);
   }

   static native void nglFrameTerminatorGREMEDY(long var0);
}
