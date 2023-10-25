package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Toolkit;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

final class WindowsCanvasImplementation implements AWTCanvasImplementation {
  static {
    Toolkit.getDefaultToolkit();
    AccessController.doPrivileged(new PrivilegedAction() {
          public Object run() {
            try {
              System.loadLibrary("jawt");
            } catch (UnsatisfiedLinkError e) {
              LWJGLUtil.log("Failed to load jawt: " + e.getMessage());
            } 
            return null;
          }
        });
  }
  
  public PeerInfo createPeerInfo(Canvas component, PixelFormat pixel_format, ContextAttribs attribs) throws LWJGLException {
    return new WindowsAWTGLCanvasPeerInfo(component, pixel_format);
  }
  
  public GraphicsConfiguration findConfiguration(GraphicsDevice device, PixelFormat pixel_format) throws LWJGLException {
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\WindowsCanvasImplementation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */