package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import org.lwjgl.LWJGLException;

final class MacOSXCanvasImplementation implements AWTCanvasImplementation {
  public PeerInfo createPeerInfo(Canvas component, PixelFormat pixel_format, ContextAttribs attribs) throws LWJGLException {
    try {
      return new MacOSXAWTGLCanvasPeerInfo(component, pixel_format, attribs, true);
    } catch (LWJGLException e) {
      return new MacOSXAWTGLCanvasPeerInfo(component, pixel_format, attribs, false);
    } 
  }
  
  public GraphicsConfiguration findConfiguration(GraphicsDevice device, PixelFormat pixel_format) throws LWJGLException {
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\MacOSXCanvasImplementation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */