package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;

abstract class WindowsPeerInfo extends PeerInfo {
   protected WindowsPeerInfo() {
      super(createHandle());
   }

   private static native ByteBuffer createHandle();

   protected static int choosePixelFormat(long var0, int var2, int var3, PixelFormat var4, IntBuffer var5, boolean var6, boolean var7, boolean var8, boolean var9) throws LWJGLException {
      return nChoosePixelFormat(var0, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   private static native int nChoosePixelFormat(long var0, int var2, int var3, PixelFormat var4, IntBuffer var5, boolean var6, boolean var7, boolean var8, boolean var9) throws LWJGLException;

   protected static native void setPixelFormat(long var0, int var2) throws LWJGLException;

   public final long getHdc() {
      return nGetHdc(this.getHandle());
   }

   private static native long nGetHdc(ByteBuffer var0);

   public final long getHwnd() {
      return nGetHwnd(this.getHandle());
   }

   private static native long nGetHwnd(ByteBuffer var0);
}
