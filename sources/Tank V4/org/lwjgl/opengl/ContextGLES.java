package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.lwjgl.opengles.EGL;
import org.lwjgl.opengles.EGLContext;
import org.lwjgl.opengles.GLES20;
import org.lwjgl.opengles.PowerManagementEventException;

final class ContextGLES implements Context {
   private static final ThreadLocal current_context_local = new ThreadLocal();
   private final DrawableGLES drawable;
   private final EGLContext eglContext;
   private final org.lwjgl.opengles.ContextAttribs contextAttribs;
   private boolean destroyed;
   private boolean destroy_requested;
   private Thread thread;

   public EGLContext getEGLContext() {
      return this.eglContext;
   }

   org.lwjgl.opengles.ContextAttribs getContextAttribs() {
      return this.contextAttribs;
   }

   static ContextGLES getCurrentContext() {
      return (ContextGLES)current_context_local.get();
   }

   ContextGLES(DrawableGLES var1, org.lwjgl.opengles.ContextAttribs var2, ContextGLES var3) throws LWJGLException {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         ContextGLES var4 = var3 != null ? var3 : this;
         synchronized(var4){}
         if (var3 != null && var3.destroyed) {
            throw new IllegalArgumentException("Shared context is destroyed");
         } else {
            this.drawable = var1;
            this.contextAttribs = var2;
            this.eglContext = var1.getEGLDisplay().createContext(var1.getEGLConfig(), var3 == null ? null : var3.eglContext, var2 == null ? (new org.lwjgl.opengles.ContextAttribs(2)).getAttribList() : var2.getAttribList());
         }
      }
   }

   public void releaseCurrent() throws LWJGLException, PowerManagementEventException {
      EGL.eglReleaseCurrent(this.drawable.getEGLDisplay());
      org.lwjgl.opengles.GLContext.useContext((Object)null);
      current_context_local.set((Object)null);
      synchronized(this){}
      this.thread = null;
      this.checkDestroy();
   }

   public static void swapBuffers() throws LWJGLException, PowerManagementEventException {
      ContextGLES var0 = getCurrentContext();
      if (var0 != null) {
         var0.drawable.getEGLSurface().swapBuffers();
      }

   }

   private void checkAccess() {
      if (this != null) {
         throw new IllegalStateException("From thread " + Thread.currentThread() + ": " + this.thread + " already has the context current");
      }
   }

   public synchronized void makeCurrent() throws LWJGLException, PowerManagementEventException {
      this.checkAccess();
      if (this.destroyed) {
         throw new IllegalStateException("Context is destroyed");
      } else {
         this.thread = Thread.currentThread();
         current_context_local.set(this);
         this.eglContext.makeCurrent(this.drawable.getEGLSurface());
         org.lwjgl.opengles.GLContext.useContext(this);
      }
   }

   public synchronized boolean isCurrent() throws LWJGLException {
      if (this.destroyed) {
         throw new IllegalStateException("Context is destroyed");
      } else {
         return EGL.eglIsCurrentContext(this.eglContext);
      }
   }

   private void checkDestroy() {
      if (!this.destroyed && this.destroy_requested) {
         try {
            this.eglContext.destroy();
            this.destroyed = true;
            this.thread = null;
         } catch (LWJGLException var2) {
            LWJGLUtil.log("Exception occurred while destroying context: " + var2);
         }
      }

   }

   public static void setSwapInterval(int var0) {
      ContextGLES var1 = getCurrentContext();
      if (var1 != null) {
         try {
            var1.drawable.getEGLDisplay().setSwapInterval(var0);
         } catch (LWJGLException var3) {
            LWJGLUtil.log("Failed to set swap interval. Reason: " + var3.getMessage());
         }
      }

   }

   public synchronized void forceDestroy() throws LWJGLException {
      this.checkAccess();
      this.destroy();
   }

   public synchronized void destroy() throws LWJGLException {
      if (!this.destroyed) {
         this.destroy_requested = true;
         boolean var1 = this.isCurrent();
         int var2 = 0;
         if (var1) {
            if (org.lwjgl.opengles.GLContext.getCapabilities() != null && org.lwjgl.opengles.GLContext.getCapabilities().OpenGLES20) {
               var2 = GLES20.glGetError();
            }

            try {
               this.releaseCurrent();
            } catch (PowerManagementEventException var4) {
            }
         }

         this.checkDestroy();
         if (var1 && var2 != 0) {
            throw new OpenGLException(var2);
         }
      }
   }

   public void releaseDrawable() throws LWJGLException {
   }

   static {
      Sys.initialize();
   }
}
