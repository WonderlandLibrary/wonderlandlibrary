package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.nio.ByteBuffer;
import javax.swing.SwingUtilities;
import org.lwjgl.LWJGLException;

abstract class MacOSXCanvasPeerInfo extends MacOSXPeerInfo {
  private final AWTSurfaceLock awt_surface = new AWTSurfaceLock();
  
  public ByteBuffer window_handle;
  
  protected MacOSXCanvasPeerInfo(PixelFormat pixel_format, ContextAttribs attribs, boolean support_pbuffer) throws LWJGLException {
    super(pixel_format, attribs, true, true, support_pbuffer, true);
  }
  
  protected void initHandle(Canvas component) throws LWJGLException {
    boolean forceCALayer = true;
    boolean autoResizable = true;
    String javaVersion = System.getProperty("java.version");
    if (javaVersion.startsWith("1.5") || javaVersion.startsWith("1.6")) {
      forceCALayer = false;
    } else if (javaVersion.startsWith("1.7")) {
      autoResizable = false;
    } 
    Insets insets = getInsets(component);
    int top = (insets != null) ? insets.top : 0;
    int left = (insets != null) ? insets.left : 0;
    this.window_handle = nInitHandle(this.awt_surface.lockAndGetHandle(component), getHandle(), this.window_handle, forceCALayer, autoResizable, component.getX() - left, component.getY() - top);
    if (javaVersion.startsWith("1.7")) {
      addComponentListener(component);
      reSetLayerBounds(component, getHandle());
    } 
  }
  
  private void addComponentListener(final Canvas component) {
    ComponentListener[] components = component.getComponentListeners();
    for (int i = 0; i < components.length; i++) {
      ComponentListener c = components[i];
      if (c.toString() == "CanvasPeerInfoListener")
        return; 
    } 
    ComponentListener comp = new ComponentListener() {
        public void componentHidden(ComponentEvent e) {}
        
        public void componentMoved(ComponentEvent e) {
          MacOSXCanvasPeerInfo.reSetLayerBounds(component, MacOSXCanvasPeerInfo.this.getHandle());
        }
        
        public void componentResized(ComponentEvent e) {
          MacOSXCanvasPeerInfo.reSetLayerBounds(component, MacOSXCanvasPeerInfo.this.getHandle());
        }
        
        public void componentShown(ComponentEvent e) {}
        
        public String toString() {
          return "CanvasPeerInfoListener";
        }
      };
    component.addComponentListener(comp);
  }
  
  private static void reSetLayerBounds(Canvas component, ByteBuffer peer_info_handle) {
    Component peer = SwingUtilities.getRoot(component);
    Point rtLoc = SwingUtilities.convertPoint(component.getParent(), component.getLocation(), peer);
    int x = (int)rtLoc.getX(), y = (int)rtLoc.getY();
    Insets insets = getInsets(component);
    x -= (insets != null) ? insets.left : 0;
    y -= (insets != null) ? insets.top : 0;
    y = peer.getHeight() - y - component.getHeight();
    nSetLayerBounds(peer_info_handle, x, y, component.getWidth(), component.getHeight());
  }
  
  protected void doUnlock() throws LWJGLException {
    this.awt_surface.unlock();
  }
  
  private static Insets getInsets(Canvas component) {
    Container c = SwingUtilities.getRootPane(component);
    if (c != null)
      return c.getInsets(); 
    return new Insets(0, 0, 0, 0);
  }
  
  private static native ByteBuffer nInitHandle(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2, ByteBuffer paramByteBuffer3, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2) throws LWJGLException;
  
  private static native void nSetLayerPosition(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2);
  
  private static native void nSetLayerBounds(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\MacOSXCanvasPeerInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */