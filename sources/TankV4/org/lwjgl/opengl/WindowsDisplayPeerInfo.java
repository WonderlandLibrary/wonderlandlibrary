package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;

final class WindowsDisplayPeerInfo extends WindowsPeerInfo {
   final boolean egl;

   WindowsDisplayPeerInfo(boolean var1) throws LWJGLException {
      this.egl = var1;
      if (var1) {
         org.lwjgl.opengles.GLContext.loadOpenGLLibrary();
      } else {
         GLContext.loadOpenGLLibrary();
      }

   }

   void initDC(long var1, long var3) throws LWJGLException {
      nInitDC(this.getHandle(), var1, var3);
   }

   private static native void nInitDC(ByteBuffer var0, long var1, long var3);

   protected void doLockAndInitHandle() throws LWJGLException {
   }

   protected void doUnlock() throws LWJGLException {
   }

   public void destroy() {
      super.destroy();
      if (this.egl) {
         org.lwjgl.opengles.GLContext.unloadOpenGLLibrary();
      } else {
         GLContext.unloadOpenGLLibrary();
      }

   }
}
