package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengles.EGL;
import org.lwjgl.opengles.EGLConfig;
import org.lwjgl.opengles.EGLDisplay;
import org.lwjgl.opengles.EGLSurface;
import org.lwjgl.opengles.GLES20;
import org.lwjgl.opengles.PowerManagementEventException;

abstract class DrawableGLES implements DrawableLWJGL {
   protected org.lwjgl.opengles.PixelFormat pixel_format;
   protected EGLDisplay eglDisplay;
   protected EGLConfig eglConfig;
   protected EGLSurface eglSurface;
   protected ContextGLES context;
   protected Drawable shared_drawable;

   protected DrawableGLES() {
   }

   public void setPixelFormat(PixelFormatLWJGL var1) throws LWJGLException {
      Object var2;
      synchronized(var2 = GlobalLock.lock){}
      this.pixel_format = (org.lwjgl.opengles.PixelFormat)var1;
   }

   public PixelFormatLWJGL getPixelFormat() {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      return this.pixel_format;
   }

   public void initialize(long var1, long var3, int var5, org.lwjgl.opengles.PixelFormat var6) throws LWJGLException {
      Object var7;
      synchronized(var7 = GlobalLock.lock){}
      if (this.eglSurface != null) {
         this.eglSurface.destroy();
         this.eglSurface = null;
      }

      if (this.eglDisplay != null) {
         this.eglDisplay.terminate();
         this.eglDisplay = null;
      }

      EGLDisplay var8 = EGL.eglGetDisplay((long)((int)var3));
      int[] var9 = new int[]{12329, 0, 12352, 4, 12333, 0};
      EGLConfig[] var10 = var8.chooseConfig(var6.getAttribBuffer(var8, var5, var9), (EGLConfig[])null, BufferUtils.createIntBuffer(1));
      if (var10.length == 0) {
         throw new LWJGLException("No EGLConfigs found for the specified PixelFormat.");
      } else {
         EGLConfig var11 = var6.getBestMatch(var10);
         EGLSurface var12 = var8.createWindowSurface(var11, var1, (IntBuffer)null);
         var6.setSurfaceAttribs(var12);
         this.eglDisplay = var8;
         this.eglConfig = var11;
         this.eglSurface = var12;
         if (this.context != null) {
            this.context.getEGLContext().setDisplay(var8);
         }

      }
   }

   public void createContext(org.lwjgl.opengles.ContextAttribs var1, Drawable var2) throws LWJGLException {
      Object var3;
      synchronized(var3 = GlobalLock.lock){}
      this.context = new ContextGLES(this, var1, var2 != null ? ((DrawableGLES)var2).getContext() : null);
      this.shared_drawable = var2;
   }

   Drawable getSharedDrawable() {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      return this.shared_drawable;
   }

   public EGLDisplay getEGLDisplay() {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      return this.eglDisplay;
   }

   public EGLConfig getEGLConfig() {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      return this.eglConfig;
   }

   public EGLSurface getEGLSurface() {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      return this.eglSurface;
   }

   public ContextGLES getContext() {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      return this.context;
   }

   public Context createSharedContext() throws LWJGLException {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      this.checkDestroyed();
      return new ContextGLES(this, this.context.getContextAttribs(), this.context);
   }

   public void checkGLError() {
      org.lwjgl.opengles.Util.checkGLError();
   }

   public void setSwapInterval(int var1) {
      ContextGLES.setSwapInterval(var1);
   }

   public void swapBuffers() throws LWJGLException {
      ContextGLES.swapBuffers();
   }

   public void initContext(float var1, float var2, float var3) {
      GLES20.glClearColor(var1, var2, var3, 0.0F);
      GLES20.glClear(16384);
   }

   public boolean isCurrent() throws LWJGLException {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      this.checkDestroyed();
      return this.context.isCurrent();
   }

   public void makeCurrent() throws LWJGLException, PowerManagementEventException {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      this.checkDestroyed();
      this.context.makeCurrent();
   }

   public void releaseContext() throws LWJGLException, PowerManagementEventException {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      this.checkDestroyed();
      if (this.context.isCurrent()) {
         this.context.releaseCurrent();
      }

   }

   public void destroy() {
      synchronized(GlobalLock.lock){}

      try {
         if (this.context != null) {
            try {
               this.releaseContext();
            } catch (PowerManagementEventException var4) {
            }

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
      } catch (LWJGLException var5) {
         LWJGLUtil.log("Exception occurred while destroying Drawable: " + var5);
      }

   }

   protected void checkDestroyed() {
      if (this.context == null) {
         throw new IllegalStateException("The Drawable has no context available.");
      }
   }

   public void setCLSharingProperties(PointerBuffer var1) throws LWJGLException {
      throw new UnsupportedOperationException();
   }

   public Context getContext() {
      return this.getContext();
   }
}
