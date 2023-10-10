package org.lwjgl.opengl;

import java.awt.Canvas;
import org.lwjgl.LWJGLException;

final class MacOSXDisplayPeerInfo extends MacOSXCanvasPeerInfo {
   private boolean locked;

   MacOSXDisplayPeerInfo(PixelFormat var1, ContextAttribs var2, boolean var3) throws LWJGLException {
      super(var1, var2, var3);
   }

   protected void doLockAndInitHandle() throws LWJGLException {
      if (this.locked) {
         throw new RuntimeException("Already locked");
      } else {
         Canvas var1 = ((MacOSXDisplay)Display.getImplementation()).getCanvas();
         if (var1 != null) {
            this.initHandle(var1);
            this.locked = true;
         }

      }
   }

   protected void doUnlock() throws LWJGLException {
      if (this.locked) {
         super.doUnlock();
         this.locked = false;
      }

   }
}
