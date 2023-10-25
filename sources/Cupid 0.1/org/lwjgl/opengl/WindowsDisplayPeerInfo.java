package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengles.GLContext;

final class WindowsDisplayPeerInfo extends WindowsPeerInfo {
  final boolean egl;
  
  WindowsDisplayPeerInfo(boolean egl) throws LWJGLException {
    this.egl = egl;
    if (egl) {
      GLContext.loadOpenGLLibrary();
    } else {
      GLContext.loadOpenGLLibrary();
    } 
  }
  
  void initDC(long hwnd, long hdc) throws LWJGLException {
    nInitDC(getHandle(), hwnd, hdc);
  }
  
  private static native void nInitDC(ByteBuffer paramByteBuffer, long paramLong1, long paramLong2);
  
  protected void doLockAndInitHandle() throws LWJGLException {}
  
  protected void doUnlock() throws LWJGLException {}
  
  public void destroy() {
    super.destroy();
    if (this.egl) {
      GLContext.unloadOpenGLLibrary();
    } else {
      GLContext.unloadOpenGLLibrary();
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\WindowsDisplayPeerInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */