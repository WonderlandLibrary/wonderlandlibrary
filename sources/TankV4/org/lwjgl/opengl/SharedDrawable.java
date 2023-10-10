package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.PointerBuffer;

public final class SharedDrawable extends DrawableGL {
   public SharedDrawable(Drawable var1) throws LWJGLException {
      this.context = (ContextGL)((DrawableLWJGL)var1).createSharedContext();
   }

   public ContextGL createSharedContext() {
      throw new UnsupportedOperationException();
   }

   public void setCLSharingProperties(PointerBuffer var1) throws LWJGLException {
      super.setCLSharingProperties(var1);
   }

   public void destroy() {
      super.destroy();
   }

   public void releaseContext() throws LWJGLException {
      super.releaseContext();
   }

   public void makeCurrent() throws LWJGLException {
      super.makeCurrent();
   }

   public boolean isCurrent() throws LWJGLException {
      return super.isCurrent();
   }

   public void initContext(float var1, float var2, float var3) {
      super.initContext(var1, var2, var3);
   }

   public void swapBuffers() throws LWJGLException {
      super.swapBuffers();
   }

   public void setSwapInterval(int var1) {
      super.setSwapInterval(var1);
   }

   public void checkGLError() {
      super.checkGLError();
   }

   public ContextGL getContext() {
      return super.getContext();
   }

   public PixelFormatLWJGL getPixelFormat() {
      return super.getPixelFormat();
   }

   public void setPixelFormat(PixelFormatLWJGL var1, ContextAttribs var2) throws LWJGLException {
      super.setPixelFormat(var1, var2);
   }

   public void setPixelFormat(PixelFormatLWJGL var1) throws LWJGLException {
      super.setPixelFormat(var1);
   }

   public Context createSharedContext() throws LWJGLException {
      return this.createSharedContext();
   }
}
