package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;

final class MacOSXPbufferPeerInfo extends MacOSXPeerInfo {
   MacOSXPbufferPeerInfo(int var1, int var2, PixelFormat var3, ContextAttribs var4) throws LWJGLException {
      super(var3, var4, false, false, true, false);
      nCreate(this.getHandle(), var1, var2);
   }

   private static native void nCreate(ByteBuffer var0, int var1, int var2) throws LWJGLException;

   public void destroy() {
      nDestroy(this.getHandle());
   }

   private static native void nDestroy(ByteBuffer var0);

   protected void doLockAndInitHandle() throws LWJGLException {
   }

   protected void doUnlock() throws LWJGLException {
   }
}
