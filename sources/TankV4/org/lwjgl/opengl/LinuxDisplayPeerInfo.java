package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;

final class LinuxDisplayPeerInfo extends LinuxPeerInfo {
   final boolean egl;

   LinuxDisplayPeerInfo() throws LWJGLException {
      this.egl = true;
      org.lwjgl.opengles.GLContext.loadOpenGLLibrary();
   }

   LinuxDisplayPeerInfo(PixelFormat var1) throws LWJGLException {
      this.egl = false;
      LinuxDisplay.lockAWT();
      GLContext.loadOpenGLLibrary();

      try {
         LinuxDisplay.incDisplay();

         try {
            initDefaultPeerInfo(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen(), this.getHandle(), var1);
         } catch (LWJGLException var4) {
            LinuxDisplay.decDisplay();
            throw var4;
         }
      } catch (LWJGLException var5) {
         GLContext.unloadOpenGLLibrary();
         throw var5;
      }

      LinuxDisplay.unlockAWT();
   }

   private static native void initDefaultPeerInfo(long var0, int var2, ByteBuffer var3, PixelFormat var4) throws LWJGLException;

   protected void doLockAndInitHandle() throws LWJGLException {
      LinuxDisplay.lockAWT();
      initDrawable(LinuxDisplay.getWindow(), this.getHandle());
      LinuxDisplay.unlockAWT();
   }

   private static native void initDrawable(long var0, ByteBuffer var2);

   protected void doUnlock() throws LWJGLException {
   }

   public void destroy() {
      super.destroy();
      if (this.egl) {
         org.lwjgl.opengles.GLContext.unloadOpenGLLibrary();
      } else {
         LinuxDisplay.lockAWT();
         LinuxDisplay.decDisplay();
         GLContext.unloadOpenGLLibrary();
         LinuxDisplay.unlockAWT();
      }

   }
}
