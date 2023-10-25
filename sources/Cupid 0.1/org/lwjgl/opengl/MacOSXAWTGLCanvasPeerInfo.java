package org.lwjgl.opengl;

import java.awt.Canvas;
import org.lwjgl.LWJGLException;

final class MacOSXAWTGLCanvasPeerInfo extends MacOSXCanvasPeerInfo {
  private final Canvas component;
  
  MacOSXAWTGLCanvasPeerInfo(Canvas component, PixelFormat pixel_format, ContextAttribs attribs, boolean support_pbuffer) throws LWJGLException {
    super(pixel_format, attribs, support_pbuffer);
    this.component = component;
  }
  
  protected void doLockAndInitHandle() throws LWJGLException {
    initHandle(this.component);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\MacOSXAWTGLCanvasPeerInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */