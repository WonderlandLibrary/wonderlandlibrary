package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;

final class MacOSXContextImplementation implements ContextImplementation {
   public ByteBuffer create(PeerInfo var1, IntBuffer var2, ByteBuffer var3) throws LWJGLException {
      ByteBuffer var4 = var1.lockAndGetHandle();
      ByteBuffer var5 = nCreate(var4, var3);
      var1.unlock();
      return var5;
   }

   private static native ByteBuffer nCreate(ByteBuffer var0, ByteBuffer var1) throws LWJGLException;

   public void swapBuffers() throws LWJGLException {
      ContextGL var1 = ContextGL.getCurrentContext();
      if (var1 == null) {
         throw new IllegalStateException("No context is current");
      } else {
         synchronized(var1){}
         nSwapBuffers(var1.getHandle());
      }
   }

   native long getCGLShareGroup(ByteBuffer var1);

   private static native void nSwapBuffers(ByteBuffer var0) throws LWJGLException;

   public void update(ByteBuffer var1) {
      nUpdate(var1);
   }

   private static native void nUpdate(ByteBuffer var0);

   public void releaseCurrentContext() throws LWJGLException {
      nReleaseCurrentContext();
   }

   private static native void nReleaseCurrentContext() throws LWJGLException;

   public void releaseDrawable(ByteBuffer var1) throws LWJGLException {
      clearDrawable(var1);
   }

   private static native void clearDrawable(ByteBuffer var0) throws LWJGLException;

   static void resetView(PeerInfo var0, ContextGL var1) throws LWJGLException {
      ByteBuffer var2 = var0.lockAndGetHandle();
      synchronized(var1){}
      clearDrawable(var1.getHandle());
      setView(var2, var1.getHandle());
      var0.unlock();
   }

   public void makeCurrent(PeerInfo var1, ByteBuffer var2) throws LWJGLException {
      ByteBuffer var3 = var1.lockAndGetHandle();
      setView(var3, var2);
      nMakeCurrent(var2);
      var1.unlock();
   }

   private static native void setView(ByteBuffer var0, ByteBuffer var1) throws LWJGLException;

   private static native void nMakeCurrent(ByteBuffer var0) throws LWJGLException;

   public boolean isCurrent(ByteBuffer var1) throws LWJGLException {
      boolean var2 = nIsCurrent(var1);
      return var2;
   }

   private static native boolean nIsCurrent(ByteBuffer var0) throws LWJGLException;

   public void setSwapInterval(int var1) {
      ContextGL var2 = ContextGL.getCurrentContext();
      synchronized(var2){}
      nSetSwapInterval(var2.getHandle(), var1);
   }

   private static native void nSetSwapInterval(ByteBuffer var0, int var1);

   public void destroy(PeerInfo var1, ByteBuffer var2) throws LWJGLException {
      nDestroy(var2);
   }

   private static native void nDestroy(ByteBuffer var0) throws LWJGLException;
}
