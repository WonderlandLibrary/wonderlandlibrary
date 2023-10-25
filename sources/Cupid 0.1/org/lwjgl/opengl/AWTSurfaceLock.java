package org.lwjgl.opengl;

import java.awt.Canvas;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

final class AWTSurfaceLock {
  private static final int WAIT_DELAY_MILLIS = 100;
  
  private final ByteBuffer lock_buffer = createHandle();
  
  private boolean firstLockSucceeded;
  
  public ByteBuffer lockAndGetHandle(Canvas component) throws LWJGLException {
    while (!privilegedLockAndInitHandle(component)) {
      LWJGLUtil.log("Could not get drawing surface info, retrying...");
      try {
        Thread.sleep(100L);
      } catch (InterruptedException e) {
        LWJGLUtil.log("Interrupted while retrying: " + e);
      } 
    } 
    return this.lock_buffer;
  }
  
  private boolean privilegedLockAndInitHandle(final Canvas component) throws LWJGLException {
    if (this.firstLockSucceeded)
      return lockAndInitHandle(this.lock_buffer, component); 
    try {
      this.firstLockSucceeded = ((Boolean)AccessController.<Boolean>doPrivileged(new PrivilegedExceptionAction<Boolean>() {
            public Boolean run() throws LWJGLException {
              return Boolean.valueOf(AWTSurfaceLock.lockAndInitHandle(AWTSurfaceLock.this.lock_buffer, component));
            }
          })).booleanValue();
      return this.firstLockSucceeded;
    } catch (PrivilegedActionException e) {
      throw (LWJGLException)e.getException();
    } 
  }
  
  void unlock() throws LWJGLException {
    nUnlock(this.lock_buffer);
  }
  
  private static native ByteBuffer createHandle();
  
  private static native boolean lockAndInitHandle(ByteBuffer paramByteBuffer, Canvas paramCanvas) throws LWJGLException;
  
  private static native void nUnlock(ByteBuffer paramByteBuffer) throws LWJGLException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\AWTSurfaceLock.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */