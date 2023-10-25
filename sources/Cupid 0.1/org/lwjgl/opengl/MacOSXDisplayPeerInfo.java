package org.lwjgl.opengl;

import java.awt.Canvas;
import org.lwjgl.LWJGLException;

final class MacOSXDisplayPeerInfo extends MacOSXCanvasPeerInfo {
  private boolean locked;
  
  MacOSXDisplayPeerInfo(PixelFormat pixel_format, ContextAttribs attribs, boolean support_pbuffer) throws LWJGLException {
    super(pixel_format, attribs, support_pbuffer);
  }
  
  protected void doLockAndInitHandle() throws LWJGLException {
    if (this.locked)
      throw new RuntimeException("Already locked"); 
    Canvas canvas = ((MacOSXDisplay)Display.getImplementation()).getCanvas();
    if (canvas != null) {
      initHandle(canvas);
      this.locked = true;
    } 
  }
  
  protected void doUnlock() throws LWJGLException {
    if (this.locked) {
      super.doUnlock();
      this.locked = false;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\MacOSXDisplayPeerInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */