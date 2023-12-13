package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;

final class WindowsPbufferPeerInfo extends WindowsPeerInfo {
   WindowsPbufferPeerInfo(int var1, int var2, PixelFormat var3, IntBuffer var4, IntBuffer var5) throws LWJGLException {
      nCreate(this.getHandle(), var1, var2, var3, var4, var5);
   }

   private static native void nCreate(ByteBuffer var0, int var1, int var2, PixelFormat var3, IntBuffer var4, IntBuffer var5) throws LWJGLException;

   public boolean isBufferLost() {
      return nIsBufferLost(this.getHandle());
   }

   private static native boolean nIsBufferLost(ByteBuffer var0);

   public void setPbufferAttrib(int var1, int var2) {
      nSetPbufferAttrib(this.getHandle(), var1, var2);
   }

   private static native void nSetPbufferAttrib(ByteBuffer var0, int var1, int var2);

   public void bindTexImageToPbuffer(int var1) {
      nBindTexImageToPbuffer(this.getHandle(), var1);
   }

   private static native void nBindTexImageToPbuffer(ByteBuffer var0, int var1);

   public void releaseTexImageFromPbuffer(int var1) {
      nReleaseTexImageFromPbuffer(this.getHandle(), var1);
   }

   private static native void nReleaseTexImageFromPbuffer(ByteBuffer var0, int var1);

   public void destroy() {
      nDestroy(this.getHandle());
   }

   private static native void nDestroy(ByteBuffer var0);

   protected void doLockAndInitHandle() throws LWJGLException {
   }

   protected void doUnlock() throws LWJGLException {
   }
}
