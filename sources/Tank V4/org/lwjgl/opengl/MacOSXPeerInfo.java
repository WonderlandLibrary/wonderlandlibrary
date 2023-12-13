package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

abstract class MacOSXPeerInfo extends PeerInfo {
   MacOSXPeerInfo(PixelFormat var1, ContextAttribs var2, boolean var3, boolean var4, boolean var5, boolean var6) throws LWJGLException {
      super(createHandle());
      boolean var7 = var2 != null && var2.getMajorVersion() == 3 && var2.getMinorVersion() == 2 && var2.isProfileCore();
      if (var7 && !LWJGLUtil.isMacOSXEqualsOrBetterThan(10, 7)) {
         throw new LWJGLException("OpenGL 3.2 requested, but it requires MacOS X 10.7 or newer");
      } else {
         this.choosePixelFormat(var1, var7, var3, var4, var5, var6);
      }
   }

   private static native ByteBuffer createHandle();

   private void choosePixelFormat(PixelFormat var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) throws LWJGLException {
      nChoosePixelFormat(this.getHandle(), var1, var2, var3, var4, var5, var6);
   }

   private static native void nChoosePixelFormat(ByteBuffer var0, PixelFormat var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) throws LWJGLException;

   public void destroy() {
      nDestroy(this.getHandle());
   }

   private static native void nDestroy(ByteBuffer var0);
}
