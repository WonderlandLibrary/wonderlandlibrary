package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

final class LinuxCanvasImplementation implements AWTCanvasImplementation {
  static int getScreenFromDevice(final GraphicsDevice device) throws LWJGLException {
    try {
      Method getScreen_method = AccessController.<Method>doPrivileged(new PrivilegedExceptionAction<Method>() {
            public Method run() throws Exception {
              return device.getClass().getMethod("getScreen", new Class[0]);
            }
          });
      Integer screen = (Integer)getScreen_method.invoke(device, new Object[0]);
      return screen.intValue();
    } catch (Exception e) {
      throw new LWJGLException(e);
    } 
  }
  
  private static int getVisualIDFromConfiguration(final GraphicsConfiguration configuration) throws LWJGLException {
    try {
      Method getVisual_method = AccessController.<Method>doPrivileged(new PrivilegedExceptionAction<Method>() {
            public Method run() throws Exception {
              return configuration.getClass().getMethod("getVisual", new Class[0]);
            }
          });
      Integer visual = (Integer)getVisual_method.invoke(configuration, new Object[0]);
      return visual.intValue();
    } catch (Exception e) {
      throw new LWJGLException(e);
    } 
  }
  
  public PeerInfo createPeerInfo(Canvas component, PixelFormat pixel_format, ContextAttribs attribs) throws LWJGLException {
    return new LinuxAWTGLCanvasPeerInfo(component);
  }
  
  public GraphicsConfiguration findConfiguration(GraphicsDevice device, PixelFormat pixel_format) throws LWJGLException {
    try {
      int screen = getScreenFromDevice(device);
      int visual_id_matching_format = findVisualIDFromFormat(screen, pixel_format);
      GraphicsConfiguration[] configurations = device.getConfigurations();
      for (GraphicsConfiguration configuration : configurations) {
        int visual_id = getVisualIDFromConfiguration(configuration);
        if (visual_id == visual_id_matching_format)
          return configuration; 
      } 
    } catch (LWJGLException e) {
      LWJGLUtil.log("Got exception while trying to determine configuration: " + e);
    } 
    return null;
  }
  
  private static int findVisualIDFromFormat(int screen, PixelFormat pixel_format) throws LWJGLException {
    try {
      LinuxDisplay.lockAWT();
    } finally {
      LinuxDisplay.unlockAWT();
    } 
  }
  
  private static native int nFindVisualIDFromFormat(long paramLong, int paramInt, PixelFormat paramPixelFormat) throws LWJGLException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\LinuxCanvasImplementation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */