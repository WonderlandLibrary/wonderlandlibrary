package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.Sys;

public class AWTGLCanvas extends Canvas implements DrawableLWJGL, ComponentListener, HierarchyListener {
   private static final long serialVersionUID = 1L;
   private static final AWTCanvasImplementation implementation;
   private boolean update_context;
   private Object SYNC_LOCK;
   private final PixelFormat pixel_format;
   private final Drawable drawable;
   private final ContextAttribs attribs;
   private PeerInfo peer_info;
   private ContextGL context;
   private int reentry_count;
   private boolean first_run;

   static AWTCanvasImplementation createImplementation() {
      switch(LWJGLUtil.getPlatform()) {
      case 1:
         return new LinuxCanvasImplementation();
      case 2:
         return new MacOSXCanvasImplementation();
      case 3:
         return new WindowsCanvasImplementation();
      default:
         throw new IllegalStateException("Unsupported platform");
      }
   }

   private void setUpdate() {
      Object var1;
      synchronized(var1 = this.SYNC_LOCK){}
      this.update_context = true;
   }

   public void setPixelFormat(PixelFormatLWJGL var1) throws LWJGLException {
      throw new UnsupportedOperationException();
   }

   public void setPixelFormat(PixelFormatLWJGL var1, ContextAttribs var2) throws LWJGLException {
      throw new UnsupportedOperationException();
   }

   public PixelFormatLWJGL getPixelFormat() {
      return this.pixel_format;
   }

   public ContextGL getContext() {
      return this.context;
   }

   public ContextGL createSharedContext() throws LWJGLException {
      Object var1;
      synchronized(var1 = this.SYNC_LOCK){}
      if (this.context == null) {
         throw new IllegalStateException("Canvas not yet displayable");
      } else {
         return new ContextGL(this.peer_info, this.context.getContextAttribs(), this.context);
      }
   }

   public void checkGLError() {
      Util.checkGLError();
   }

   public void initContext(float var1, float var2, float var3) {
      GL11.glClearColor(var1, var2, var3, 0.0F);
      GL11.glClear(16384);
   }

   public AWTGLCanvas() throws LWJGLException {
      this(new PixelFormat());
   }

   public AWTGLCanvas(PixelFormat var1) throws LWJGLException {
      this(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(), var1);
   }

   public AWTGLCanvas(GraphicsDevice var1, PixelFormat var2) throws LWJGLException {
      this(var1, var2, (Drawable)null);
   }

   public AWTGLCanvas(GraphicsDevice var1, PixelFormat var2, Drawable var3) throws LWJGLException {
      this(var1, var2, var3, (ContextAttribs)null);
   }

   public AWTGLCanvas(GraphicsDevice var1, PixelFormat var2, Drawable var3, ContextAttribs var4) throws LWJGLException {
      super(implementation.findConfiguration(var1, var2));
      this.SYNC_LOCK = new Object();
      if (var2 == null) {
         throw new NullPointerException("Pixel format must be non-null");
      } else {
         this.addHierarchyListener(this);
         this.addComponentListener(this);
         this.drawable = var3;
         this.pixel_format = var2;
         this.attribs = var4;
      }
   }

   public void addNotify() {
      super.addNotify();
   }

   public void removeNotify() {
      Object var1;
      synchronized(var1 = this.SYNC_LOCK){}
      this.destroy();
      super.removeNotify();
   }

   public void setSwapInterval(int var1) {
      Object var2;
      synchronized(var2 = this.SYNC_LOCK){}
      if (this.context == null) {
         throw new IllegalStateException("Canvas not yet displayable");
      } else {
         ContextGL.setSwapInterval(var1);
      }
   }

   public void setVSyncEnabled(boolean var1) {
      this.setSwapInterval(var1 ? 1 : 0);
   }

   public void swapBuffers() throws LWJGLException {
      Object var1;
      synchronized(var1 = this.SYNC_LOCK){}
      if (this.context == null) {
         throw new IllegalStateException("Canvas not yet displayable");
      } else {
         ContextGL.swapBuffers();
      }
   }

   public boolean isCurrent() throws LWJGLException {
      Object var1;
      synchronized(var1 = this.SYNC_LOCK){}
      if (this.context == null) {
         throw new IllegalStateException("Canvas not yet displayable");
      } else {
         return this.context.isCurrent();
      }
   }

   public void makeCurrent() throws LWJGLException {
      Object var1;
      synchronized(var1 = this.SYNC_LOCK){}
      if (this.context == null) {
         throw new IllegalStateException("Canvas not yet displayable");
      } else {
         this.context.makeCurrent();
      }
   }

   public void releaseContext() throws LWJGLException {
      Object var1;
      synchronized(var1 = this.SYNC_LOCK){}
      if (this.context == null) {
         throw new IllegalStateException("Canvas not yet displayable");
      } else {
         if (this.context.isCurrent()) {
            this.context.releaseCurrent();
         }

      }
   }

   public final void destroy() {
      synchronized(this.SYNC_LOCK){}

      try {
         if (this.context != null) {
            this.context.forceDestroy();
            this.context = null;
            this.reentry_count = 0;
            this.peer_info.destroy();
            this.peer_info = null;
         }
      } catch (LWJGLException var4) {
         throw new RuntimeException(var4);
      }

   }

   public final void setCLSharingProperties(PointerBuffer var1) throws LWJGLException {
      Object var2;
      synchronized(var2 = this.SYNC_LOCK){}
      if (this.context == null) {
         throw new IllegalStateException("Canvas not yet displayable");
      } else {
         this.context.setCLSharingProperties(var1);
      }
   }

   protected void initGL() {
   }

   protected void paintGL() {
   }

   public final void paint(Graphics var1) {
      LWJGLException var2 = null;
      Object var3;
      synchronized(var3 = this.SYNC_LOCK){}
      if (this.isDisplayable()) {
         try {
            if (this.peer_info == null) {
               this.peer_info = implementation.createPeerInfo(this, this.pixel_format, this.attribs);
            }

            this.peer_info.lockAndGetHandle();
            if (this.context == null) {
               this.context = new ContextGL(this.peer_info, this.attribs, this.drawable != null ? (ContextGL)((DrawableLWJGL)this.drawable).getContext() : null);
               this.first_run = true;
            }

            if (this.reentry_count == 0) {
               this.context.makeCurrent();
            }

            ++this.reentry_count;
            if (this.update_context) {
               this.context.update();
               this.update_context = false;
            }

            if (this.first_run) {
               this.first_run = false;
               this.initGL();
            }

            this.paintGL();
            --this.reentry_count;
            if (this.reentry_count == 0) {
               this.context.releaseCurrent();
            }

            this.peer_info.unlock();
         } catch (LWJGLException var7) {
            var2 = var7;
         }

         if (var2 != null) {
            this.exceptionOccurred(var2);
         }

      }
   }

   protected void exceptionOccurred(LWJGLException var1) {
      LWJGLUtil.log("Unhandled exception occurred, skipping paint(): " + var1);
   }

   public void update(Graphics var1) {
      this.paint(var1);
   }

   public void componentShown(ComponentEvent var1) {
   }

   public void componentHidden(ComponentEvent var1) {
   }

   public void componentResized(ComponentEvent var1) {
      this.setUpdate();
   }

   public void componentMoved(ComponentEvent var1) {
      this.setUpdate();
   }

   public void setLocation(int var1, int var2) {
      super.setLocation(var1, var2);
      this.setUpdate();
   }

   public void setLocation(Point var1) {
      super.setLocation(var1);
      this.setUpdate();
   }

   public void setSize(Dimension var1) {
      super.setSize(var1);
      this.setUpdate();
   }

   public void setSize(int var1, int var2) {
      super.setSize(var1, var2);
      this.setUpdate();
   }

   public void setBounds(int var1, int var2, int var3, int var4) {
      super.setBounds(var1, var2, var3, var4);
      this.setUpdate();
   }

   public void hierarchyChanged(HierarchyEvent var1) {
      this.setUpdate();
   }

   public Context createSharedContext() throws LWJGLException {
      return this.createSharedContext();
   }

   public Context getContext() {
      return this.getContext();
   }

   static {
      Sys.initialize();
      implementation = createImplementation();
   }
}
