package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

final class WindowsContextImplementation implements ContextImplementation {
   public ByteBuffer create(PeerInfo var1, IntBuffer var2, ByteBuffer var3) throws LWJGLException {
      ByteBuffer var4 = var1.lockAndGetHandle();
      ByteBuffer var5 = nCreate(var4, var2, var3);
      var1.unlock();
      return var5;
   }

   private static native ByteBuffer nCreate(ByteBuffer var0, IntBuffer var1, ByteBuffer var2) throws LWJGLException;

   native long getHGLRC(ByteBuffer var1);

   native long getHDC(ByteBuffer var1);

   public void swapBuffers() throws LWJGLException {
      ContextGL var1 = ContextGL.getCurrentContext();
      if (var1 == null) {
         throw new IllegalStateException("No context is current");
      } else {
         synchronized(var1){}
         PeerInfo var3 = var1.getPeerInfo();
         ByteBuffer var4 = var3.lockAndGetHandle();
         nSwapBuffers(var4);
         var3.unlock();
      }
   }

   private static native void nSwapBuffers(ByteBuffer var0) throws LWJGLException;

   public void releaseDrawable(ByteBuffer var1) throws LWJGLException {
   }

   public void update(ByteBuffer var1) {
   }

   public void releaseCurrentContext() throws LWJGLException {
      nReleaseCurrentContext();
   }

   private static native void nReleaseCurrentContext() throws LWJGLException;

   public void makeCurrent(PeerInfo var1, ByteBuffer var2) throws LWJGLException {
      ByteBuffer var3 = var1.lockAndGetHandle();
      nMakeCurrent(var3, var2);
      var1.unlock();
   }

   private static native void nMakeCurrent(ByteBuffer var0, ByteBuffer var1) throws LWJGLException;

   public boolean isCurrent(ByteBuffer var1) throws LWJGLException {
      boolean var2 = nIsCurrent(var1);
      return var2;
   }

   private static native boolean nIsCurrent(ByteBuffer var0) throws LWJGLException;

   public void setSwapInterval(int var1) {
      boolean var2 = nSetSwapInterval(var1);
      if (!var2) {
         LWJGLUtil.log("Failed to set swap interval");
      }

      Util.checkGLError();
   }

   private static native boolean nSetSwapInterval(int var0);

   public void destroy(PeerInfo var1, ByteBuffer var2) throws LWJGLException {
      nDestroy(var2);
   }

   private static native void nDestroy(ByteBuffer var0) throws LWJGLException;
}
