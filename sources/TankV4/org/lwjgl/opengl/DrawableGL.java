package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;

abstract class DrawableGL implements DrawableLWJGL {
   protected PixelFormat pixel_format;
   protected PeerInfo peer_info;
   protected ContextGL context;

   protected DrawableGL() {
   }

   public void setPixelFormat(PixelFormatLWJGL var1) throws LWJGLException {
      throw new UnsupportedOperationException();
   }

   public void setPixelFormat(PixelFormatLWJGL var1, ContextAttribs var2) throws LWJGLException {
      this.pixel_format = (PixelFormat)var1;
      this.peer_info = Display.getImplementation().createPeerInfo(this.pixel_format, var2);
   }

   public PixelFormatLWJGL getPixelFormat() {
      return this.pixel_format;
   }

   public ContextGL getContext() {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      return this.context;
   }

   public ContextGL createSharedContext() throws LWJGLException {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      this.checkDestroyed();
      return new ContextGL(this.peer_info, this.context.getContextAttribs(), this.context);
   }

   public void checkGLError() {
      Util.checkGLError();
   }

   public void setSwapInterval(int var1) {
      ContextGL.setSwapInterval(var1);
   }

   public void swapBuffers() throws LWJGLException {
      ContextGL.swapBuffers();
   }

   public void initContext(float var1, float var2, float var3) {
      GL11.glClearColor(var1, var2, var3, 0.0F);
      GL11.glClear(16384);
   }

   public boolean isCurrent() throws LWJGLException {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      this.checkDestroyed();
      return this.context.isCurrent();
   }

   public void makeCurrent() throws LWJGLException {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      this.checkDestroyed();
      this.context.makeCurrent();
   }

   public void releaseContext() throws LWJGLException {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      this.checkDestroyed();
      if (this.context.isCurrent()) {
         this.context.releaseCurrent();
      }

   }

   public void destroy() {
      Object var1;
      synchronized(var1 = GlobalLock.lock){}
      if (this.context != null) {
         try {
            this.releaseContext();
            this.context.forceDestroy();
            this.context = null;
            if (this.peer_info != null) {
               this.peer_info.destroy();
               this.peer_info = null;
            }
         } catch (LWJGLException var4) {
            LWJGLUtil.log("Exception occurred while destroying Drawable: " + var4);
         }

      }
   }

   public void setCLSharingProperties(PointerBuffer var1) throws LWJGLException {
      Object var2;
      synchronized(var2 = GlobalLock.lock){}
      this.checkDestroyed();
      this.context.setCLSharingProperties(var1);
   }

   protected final void checkDestroyed() {
      if (this.context == null) {
         throw new IllegalStateException("The Drawable has no context available.");
      }
   }

   public Context createSharedContext() throws LWJGLException {
      return this.createSharedContext();
   }

   public Context getContext() {
      return this.getContext();
   }
}
