package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengles.GLContext;

final class LinuxDisplayPeerInfo extends LinuxPeerInfo {
  final boolean egl;
  
  LinuxDisplayPeerInfo() throws LWJGLException {
    this.egl = true;
    GLContext.loadOpenGLLibrary();
  }
  
  LinuxDisplayPeerInfo(PixelFormat pixel_format) throws LWJGLException {
    this.egl = false;
    LinuxDisplay.lockAWT();
    try {
      GLContext.loadOpenGLLibrary();
      try {
        LinuxDisplay.incDisplay();
        try {
          initDefaultPeerInfo(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen(), getHandle(), pixel_format);
        } catch (LWJGLException e) {
          LinuxDisplay.decDisplay();
          throw e;
        } 
      } catch (LWJGLException e) {
        GLContext.unloadOpenGLLibrary();
        throw e;
      } 
    } finally {
      LinuxDisplay.unlockAWT();
    } 
  }
  
  private static native void initDefaultPeerInfo(long paramLong, int paramInt, ByteBuffer paramByteBuffer, PixelFormat paramPixelFormat) throws LWJGLException;
  
  protected void doLockAndInitHandle() throws LWJGLException {
    LinuxDisplay.lockAWT();
    try {
      initDrawable(LinuxDisplay.getWindow(), getHandle());
    } finally {
      LinuxDisplay.unlockAWT();
    } 
  }
  
  private static native void initDrawable(long paramLong, ByteBuffer paramByteBuffer);
  
  protected void doUnlock() throws LWJGLException {}
  
  public void destroy() {
    super.destroy();
    if (this.egl) {
      GLContext.unloadOpenGLLibrary();
    } else {
      LinuxDisplay.lockAWT();
      LinuxDisplay.decDisplay();
      GLContext.unloadOpenGLLibrary();
      LinuxDisplay.unlockAWT();
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\LinuxDisplayPeerInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */