package org.lwjgl.opengl;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengles.ContextAttribs;
import org.lwjgl.opengles.EGL;
import org.lwjgl.opengles.EGLConfig;
import org.lwjgl.opengles.EGLDisplay;
import org.lwjgl.opengles.EGLSurface;
import org.lwjgl.opengles.GLES20;
import org.lwjgl.opengles.PixelFormat;
import org.lwjgl.opengles.PowerManagementEventException;
import org.lwjgl.opengles.Util;

abstract class DrawableGLES implements DrawableLWJGL {
  protected PixelFormat pixel_format;
  
  protected EGLDisplay eglDisplay;
  
  protected EGLConfig eglConfig;
  
  protected EGLSurface eglSurface;
  
  protected ContextGLES context;
  
  protected Drawable shared_drawable;
  
  public void setPixelFormat(PixelFormatLWJGL pf) throws LWJGLException {
    synchronized (GlobalLock.lock) {
      this.pixel_format = (PixelFormat)pf;
    } 
  }
  
  public PixelFormatLWJGL getPixelFormat() {
    synchronized (GlobalLock.lock) {
      return (PixelFormatLWJGL)this.pixel_format;
    } 
  }
  
  public void initialize(long window, long display_id, int eglSurfaceType, PixelFormat pf) throws LWJGLException {
    synchronized (GlobalLock.lock) {
      if (this.eglSurface != null) {
        this.eglSurface.destroy();
        this.eglSurface = null;
      } 
      if (this.eglDisplay != null) {
        this.eglDisplay.terminate();
        this.eglDisplay = null;
      } 
      EGLDisplay eglDisplay = EGL.eglGetDisplay((int)display_id);
      int[] attribs = { 12329, 0, 12352, 4, 12333, 0 };
      EGLConfig[] configs = eglDisplay.chooseConfig(pf.getAttribBuffer(eglDisplay, eglSurfaceType, attribs), null, BufferUtils.createIntBuffer(1));
      if (configs.length == 0)
        throw new LWJGLException("No EGLConfigs found for the specified PixelFormat."); 
      EGLConfig eglConfig = pf.getBestMatch(configs);
      EGLSurface eglSurface = eglDisplay.createWindowSurface(eglConfig, window, null);
      pf.setSurfaceAttribs(eglSurface);
      this.eglDisplay = eglDisplay;
      this.eglConfig = eglConfig;
      this.eglSurface = eglSurface;
      if (this.context != null)
        this.context.getEGLContext().setDisplay(eglDisplay); 
    } 
  }
  
  public void createContext(ContextAttribs attribs, Drawable shared_drawable) throws LWJGLException {
    synchronized (GlobalLock.lock) {
      this.context = new ContextGLES(this, attribs, (shared_drawable != null) ? ((DrawableGLES)shared_drawable).getContext() : null);
      this.shared_drawable = shared_drawable;
    } 
  }
  
  Drawable getSharedDrawable() {
    synchronized (GlobalLock.lock) {
      return this.shared_drawable;
    } 
  }
  
  public EGLDisplay getEGLDisplay() {
    synchronized (GlobalLock.lock) {
      return this.eglDisplay;
    } 
  }
  
  public EGLConfig getEGLConfig() {
    synchronized (GlobalLock.lock) {
      return this.eglConfig;
    } 
  }
  
  public EGLSurface getEGLSurface() {
    synchronized (GlobalLock.lock) {
      return this.eglSurface;
    } 
  }
  
  public ContextGLES getContext() {
    synchronized (GlobalLock.lock) {
      return this.context;
    } 
  }
  
  public Context createSharedContext() throws LWJGLException {
    synchronized (GlobalLock.lock) {
      checkDestroyed();
      return new ContextGLES(this, this.context.getContextAttribs(), this.context);
    } 
  }
  
  public void checkGLError() {
    Util.checkGLError();
  }
  
  public void setSwapInterval(int swap_interval) {
    ContextGLES.setSwapInterval(swap_interval);
  }
  
  public void swapBuffers() throws LWJGLException {
    ContextGLES.swapBuffers();
  }
  
  public void initContext(float r, float g, float b) {
    GLES20.glClearColor(r, g, b, 0.0F);
    GLES20.glClear(16384);
  }
  
  public boolean isCurrent() throws LWJGLException {
    synchronized (GlobalLock.lock) {
      checkDestroyed();
      return this.context.isCurrent();
    } 
  }
  
  public void makeCurrent() throws LWJGLException, PowerManagementEventException {
    synchronized (GlobalLock.lock) {
      checkDestroyed();
      this.context.makeCurrent();
    } 
  }
  
  public void releaseContext() throws LWJGLException, PowerManagementEventException {
    synchronized (GlobalLock.lock) {
      checkDestroyed();
      if (this.context.isCurrent())
        this.context.releaseCurrent(); 
    } 
  }
  
  public void destroy() {
    synchronized (GlobalLock.lock) {
      try {
        if (this.context != null) {
          try {
            releaseContext();
          } catch (PowerManagementEventException e) {}
          this.context.forceDestroy();
          this.context = null;
        } 
        if (this.eglSurface != null) {
          this.eglSurface.destroy();
          this.eglSurface = null;
        } 
        if (this.eglDisplay != null) {
          this.eglDisplay.terminate();
          this.eglDisplay = null;
        } 
        this.pixel_format = null;
        this.shared_drawable = null;
      } catch (LWJGLException e) {
        LWJGLUtil.log("Exception occurred while destroying Drawable: " + e);
      } 
    } 
  }
  
  protected void checkDestroyed() {
    if (this.context == null)
      throw new IllegalStateException("The Drawable has no context available."); 
  }
  
  public void setCLSharingProperties(PointerBuffer properties) throws LWJGLException {
    throw new UnsupportedOperationException();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\DrawableGLES.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */