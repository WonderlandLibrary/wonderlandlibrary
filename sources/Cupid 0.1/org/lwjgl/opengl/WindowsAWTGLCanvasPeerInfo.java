package org.lwjgl.opengl;

import java.awt.Canvas;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;

final class WindowsAWTGLCanvasPeerInfo extends WindowsPeerInfo {
  private final Canvas component;
  
  private final AWTSurfaceLock awt_surface = new AWTSurfaceLock();
  
  private final PixelFormat pixel_format;
  
  private boolean has_pixel_format;
  
  WindowsAWTGLCanvasPeerInfo(Canvas component, PixelFormat pixel_format) {
    this.component = component;
    this.pixel_format = pixel_format;
  }
  
  protected void doLockAndInitHandle() throws LWJGLException {
    nInitHandle(this.awt_surface.lockAndGetHandle(this.component), getHandle());
    if (!this.has_pixel_format && this.pixel_format != null) {
      int format = choosePixelFormat(getHdc(), this.component.getX(), this.component.getY(), this.pixel_format, (IntBuffer)null, true, true, false, true);
      setPixelFormat(getHdc(), format);
      this.has_pixel_format = true;
    } 
  }
  
  private static native void nInitHandle(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2) throws LWJGLException;
  
  protected void doUnlock() throws LWJGLException {
    this.awt_surface.unlock();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\WindowsAWTGLCanvasPeerInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */