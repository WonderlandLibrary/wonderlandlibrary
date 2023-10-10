package org.lwjgl.opengl;

import java.awt.Canvas;
import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

final class AWTSurfaceLock {
   private static final int WAIT_DELAY_MILLIS = 100;
   private final ByteBuffer lock_buffer = createHandle();
   private boolean firstLockSucceeded;

   private static native ByteBuffer createHandle();

   public ByteBuffer lockAndGetHandle(Canvas var1) throws LWJGLException {
      while(var1 != false) {
         LWJGLUtil.log("Could not get drawing surface info, retrying...");

         try {
            Thread.sleep(100L);
         } catch (InterruptedException var3) {
            LWJGLUtil.log("Interrupted while retrying: " + var3);
         }
      }

      return this.lock_buffer;
   }

   private static native boolean lockAndInitHandle(ByteBuffer var0, Canvas var1) throws LWJGLException;

   void unlock() throws LWJGLException {
      nUnlock(this.lock_buffer);
   }

   private static native void nUnlock(ByteBuffer var0) throws LWJGLException;

   static ByteBuffer access$000(AWTSurfaceLock var0) {
      return var0.lock_buffer;
   }

   static boolean access$100(ByteBuffer var0, Canvas var1) throws LWJGLException {
      return lockAndInitHandle(var0, var1);
   }
}
