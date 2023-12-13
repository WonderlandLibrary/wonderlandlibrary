package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.Sys;

final class ContextGL implements Context {
   private static final ContextImplementation implementation;
   private static final ThreadLocal current_context_local = new ThreadLocal();
   private final ByteBuffer handle;
   private final PeerInfo peer_info;
   private final ContextAttribs contextAttribs;
   private final boolean forwardCompatible;
   private boolean destroyed;
   private boolean destroy_requested;
   private Thread thread;

   private static ContextImplementation createImplementation() {
      switch(LWJGLUtil.getPlatform()) {
      case 1:
         return new LinuxContextImplementation();
      case 2:
         return new MacOSXContextImplementation();
      case 3:
         return new WindowsContextImplementation();
      default:
         throw new IllegalStateException("Unsupported platform");
      }
   }

   PeerInfo getPeerInfo() {
      return this.peer_info;
   }

   ContextAttribs getContextAttribs() {
      return this.contextAttribs;
   }

   static ContextGL getCurrentContext() {
      return (ContextGL)current_context_local.get();
   }

   ContextGL(PeerInfo var1, ContextAttribs var2, ContextGL var3) throws LWJGLException {
      ContextGL var4 = var3 != null ? var3 : this;
      synchronized(var4){}
      if (var3 != null && var3.destroyed) {
         throw new IllegalArgumentException("Shared context is destroyed");
      } else {
         GLContext.loadOpenGLLibrary();

         try {
            this.peer_info = var1;
            this.contextAttribs = var2;
            IntBuffer var6;
            if (var2 != null) {
               var6 = var2.getAttribList();
               this.forwardCompatible = var2.isForwardCompatible();
            } else {
               var6 = null;
               this.forwardCompatible = false;
            }

            this.handle = implementation.create(var1, var6, var3 != null ? var3.handle : null);
         } catch (LWJGLException var8) {
            GLContext.unloadOpenGLLibrary();
            throw var8;
         }

      }
   }

   public void releaseCurrent() throws LWJGLException {
      ContextGL var1 = getCurrentContext();
      if (var1 != null) {
         implementation.releaseCurrentContext();
         GLContext.useContext((Object)null);
         current_context_local.set((Object)null);
         synchronized(var1){}
         var1.thread = null;
         var1.checkDestroy();
      }

   }

   public synchronized void releaseDrawable() throws LWJGLException {
      if (this.destroyed) {
         throw new IllegalStateException("Context is destroyed");
      } else {
         implementation.releaseDrawable(this.getHandle());
      }
   }

   public synchronized void update() {
      if (this.destroyed) {
         throw new IllegalStateException("Context is destroyed");
      } else {
         implementation.update(this.getHandle());
      }
   }

   public static void swapBuffers() throws LWJGLException {
      implementation.swapBuffers();
   }

   private void checkAccess() {
      if (this != null) {
         throw new IllegalStateException("From thread " + Thread.currentThread() + ": " + this.thread + " already has the context current");
      }
   }

   public synchronized void makeCurrent() throws LWJGLException {
      this.checkAccess();
      if (this.destroyed) {
         throw new IllegalStateException("Context is destroyed");
      } else {
         this.thread = Thread.currentThread();
         current_context_local.set(this);
         implementation.makeCurrent(this.peer_info, this.handle);
         GLContext.useContext(this, this.forwardCompatible);
      }
   }

   ByteBuffer getHandle() {
      return this.handle;
   }

   public synchronized boolean isCurrent() throws LWJGLException {
      if (this.destroyed) {
         throw new IllegalStateException("Context is destroyed");
      } else {
         return implementation.isCurrent(this.handle);
      }
   }

   private void checkDestroy() {
      if (!this.destroyed && this.destroy_requested) {
         try {
            this.releaseDrawable();
            implementation.destroy(this.peer_info, this.handle);
            CallbackUtil.unregisterCallbacks(this);
            this.destroyed = true;
            this.thread = null;
            GLContext.unloadOpenGLLibrary();
         } catch (LWJGLException var2) {
            LWJGLUtil.log("Exception occurred while destroying context: " + var2);
         }
      }

   }

   public static void setSwapInterval(int var0) {
      implementation.setSwapInterval(var0);
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
            if (GLContext.getCapabilities() != null && GLContext.getCapabilities().OpenGL11) {
               var2 = GL11.glGetError();
            }

            this.releaseCurrent();
         }

         this.checkDestroy();
         if (var1 && var2 != 0) {
            throw new OpenGLException(var2);
         }
      }
   }

   public synchronized void setCLSharingProperties(PointerBuffer var1) throws LWJGLException {
      ByteBuffer var2 = this.peer_info.lockAndGetHandle();
      switch(LWJGLUtil.getPlatform()) {
      case 1:
         LinuxContextImplementation var4 = (LinuxContextImplementation)implementation;
         var1.put(8200L).put(var4.getGLXContext(this.handle));
         var1.put(8202L).put(var4.getDisplay(var2));
         break;
      case 2:
         if (!LWJGLUtil.isMacOSXEqualsOrBetterThan(10, 6)) {
            throw new UnsupportedOperationException("CL/GL context sharing is not supported on this platform.");
         }

         MacOSXContextImplementation var5 = (MacOSXContextImplementation)implementation;
         long var6 = var5.getCGLShareGroup(this.handle);
         var1.put(268435456L).put(var6);
         break;
      case 3:
         WindowsContextImplementation var3 = (WindowsContextImplementation)implementation;
         var1.put(8200L).put(var3.getHGLRC(this.handle));
         var1.put(8203L).put(var3.getHDC(var2));
         break;
      default:
         throw new UnsupportedOperationException("CL/GL context sharing is not supported on this platform.");
      }

      this.peer_info.unlock();
   }

   static {
      Sys.initialize();
      implementation = createImplementation();
   }
}
