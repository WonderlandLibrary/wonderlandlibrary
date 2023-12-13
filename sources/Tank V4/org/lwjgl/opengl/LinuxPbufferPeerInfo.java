package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;

final class LinuxPbufferPeerInfo extends LinuxPeerInfo {
   LinuxPbufferPeerInfo(int var1, int var2, PixelFormat var3) throws LWJGLException {
      LinuxDisplay.lockAWT();
      GLContext.loadOpenGLLibrary();

      try {
         LinuxDisplay.incDisplay();

         try {
            nInitHandle(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen(), this.getHandle(), var1, var2, var3);
         } catch (LWJGLException var6) {
            LinuxDisplay.decDisplay();
            throw var6;
         }
      } catch (LWJGLException var7) {
         GLContext.unloadOpenGLLibrary();
         throw var7;
      }

      LinuxDisplay.unlockAWT();
   }

   private static native void nInitHandle(long var0, int var2, ByteBuffer var3, int var4, int var5, PixelFormat var6) throws LWJGLException;

   public void destroy() {
      LinuxDisplay.lockAWT();
      nDestroy(this.getHandle());
      LinuxDisplay.decDisplay();
      GLContext.unloadOpenGLLibrary();
      LinuxDisplay.unlockAWT();
   }

   private static native void nDestroy(ByteBuffer var0);

   protected void doLockAndInitHandle() throws LWJGLException {
   }

   protected void doUnlock() throws LWJGLException {
   }
}
