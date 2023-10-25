package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;

abstract class DrawableGL implements DrawableLWJGL {
  protected PixelFormat pixel_format;
  
  protected PeerInfo peer_info;
  
  protected ContextGL context;
  
  public void setPixelFormat(PixelFormatLWJGL pf) throws LWJGLException {
    throw new UnsupportedOperationException();
  }
  
  public void setPixelFormat(PixelFormatLWJGL pf, ContextAttribs attribs) throws LWJGLException {
    this.pixel_format = (PixelFormat)pf;
    this.peer_info = Display.getImplementation().createPeerInfo(this.pixel_format, attribs);
  }
  
  public PixelFormatLWJGL getPixelFormat() {
    return this.pixel_format;
  }
  
  public ContextGL getContext() {
    synchronized (GlobalLock.lock) {
      return this.context;
    } 
  }
  
  public ContextGL createSharedContext() throws LWJGLException {
    synchronized (GlobalLock.lock) {
      checkDestroyed();
      return new ContextGL(this.peer_info, this.context.getContextAttribs(), this.context);
    } 
  }
  
  public void checkGLError() {
    Util.checkGLError();
  }
  
  public void setSwapInterval(int swap_interval) {
    ContextGL.setSwapInterval(swap_interval);
  }
  
  public void swapBuffers() throws LWJGLException {
    ContextGL.swapBuffers();
  }
  
  public void initContext(float r, float g, float b) {
    GL11.glClearColor(r, g, b, 0.0F);
    GL11.glClear(16384);
  }
  
  public boolean isCurrent() throws LWJGLException {
    synchronized (GlobalLock.lock) {
      checkDestroyed();
      return this.context.isCurrent();
    } 
  }
  
  public void makeCurrent() throws LWJGLException {
    synchronized (GlobalLock.lock) {
      checkDestroyed();
      this.context.makeCurrent();
    } 
  }
  
  public void releaseContext() throws LWJGLException {
    synchronized (GlobalLock.lock) {
      checkDestroyed();
      if (this.context.isCurrent())
        this.context.releaseCurrent(); 
    } 
  }
  
  public void destroy() {
    synchronized (GlobalLock.lock) {
      if (this.context == null)
        return; 
      try {
        releaseContext();
        this.context.forceDestroy();
        this.context = null;
        if (this.peer_info != null) {
          this.peer_info.destroy();
          this.peer_info = null;
        } 
      } catch (LWJGLException e) {
        LWJGLUtil.log("Exception occurred while destroying Drawable: " + e);
      } 
    } 
  }
  
  public void setCLSharingProperties(PointerBuffer properties) throws LWJGLException {
    synchronized (GlobalLock.lock) {
      checkDestroyed();
      this.context.setCLSharingProperties(properties);
    } 
  }
  
  protected final void checkDestroyed() {
    if (this.context == null)
      throw new IllegalStateException("The Drawable has no context available."); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\DrawableGL.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */