package org.lwjgl.opengl;

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.nio.ByteBuffer;
import javax.swing.SwingUtilities;
import org.lwjgl.LWJGLException;

abstract class MacOSXCanvasPeerInfo extends MacOSXPeerInfo {
   private final AWTSurfaceLock awt_surface = new AWTSurfaceLock();
   public ByteBuffer window_handle;

   protected MacOSXCanvasPeerInfo(PixelFormat var1, ContextAttribs var2, boolean var3) throws LWJGLException {
      super(var1, var2, true, true, var3, true);
   }

   protected void initHandle(Canvas var1) throws LWJGLException {
      boolean var2 = true;
      boolean var3 = true;
      String var4 = System.getProperty("java.version");
      if (!var4.startsWith("1.5") && !var4.startsWith("1.6")) {
         if (var4.startsWith("1.7")) {
            var3 = false;
         }
      } else {
         var2 = false;
      }

      Insets var5 = this.getInsets(var1);
      int var6 = var5 != null ? var5.top : 0;
      int var7 = var5 != null ? var5.left : 0;
      this.window_handle = nInitHandle(this.awt_surface.lockAndGetHandle(var1), this.getHandle(), this.window_handle, var2, var3, var1.getX() - var7, var1.getY() - var6);
      if (var4.startsWith("1.7")) {
         this.addComponentListener(var1);
         if (SwingUtilities.getWindowAncestor(var1.getParent()) != null) {
            Point var8 = SwingUtilities.convertPoint(var1, var1.getLocation(), (Component)null);
            Point var9 = SwingUtilities.convertPoint(var1.getParent(), var1.getLocation(), (Component)null);
            if (var8.getX() == var9.getX() && var8.getY() == var9.getY()) {
               var5 = this.getWindowInsets(var1);
               var6 = var5 != null ? var5.top : 0;
               var7 = var5 != null ? var5.left : 0;
               int var10 = (int)var8.getX() - var7;
               int var11 = (int)(-var8.getY()) + var6 - var1.getHeight();
               int var12 = var1.getWidth();
               int var13 = var1.getHeight();
               nSetLayerBounds(this.getHandle(), var10, var11, var12, var13);
            }
         }
      }

   }

   private void addComponentListener(Canvas var1) {
      ComponentListener[] var2 = var1.getComponentListeners();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         ComponentListener var4 = var2[var3];
         if (var4.toString() == "CanvasPeerInfoListener") {
            return;
         }
      }

      ComponentListener var5 = new ComponentListener(this, var1) {
         final Canvas val$component;
         final MacOSXCanvasPeerInfo this$0;

         {
            this.this$0 = var1;
            this.val$component = var2;
         }

         public void componentHidden(ComponentEvent var1) {
         }

         public void componentMoved(ComponentEvent var1) {
            if (SwingUtilities.getWindowAncestor(this.val$component.getParent()) != null) {
               Point var2 = SwingUtilities.convertPoint(this.val$component, this.val$component.getLocation(), (Component)null);
               Point var3 = SwingUtilities.convertPoint(this.val$component.getParent(), this.val$component.getLocation(), (Component)null);
               if (var2.getX() == var3.getX() && var2.getY() == var3.getY()) {
                  Insets var9 = MacOSXCanvasPeerInfo.access$000(this.this$0, this.val$component);
                  int var5 = var9 != null ? var9.top : 0;
                  int var6 = var9 != null ? var9.left : 0;
                  MacOSXCanvasPeerInfo.access$100(this.this$0.getHandle(), (int)var2.getX() - var6, (int)var2.getY() - var5, this.val$component.getWidth(), this.val$component.getHeight());
                  return;
               }
            }

            Insets var7 = MacOSXCanvasPeerInfo.access$200(this.this$0, this.val$component);
            int var8 = var7 != null ? var7.top : 0;
            int var4 = var7 != null ? var7.left : 0;
            MacOSXCanvasPeerInfo.access$100(this.this$0.getHandle(), this.val$component.getX() - var4, this.val$component.getY() - var8, this.val$component.getWidth(), this.val$component.getHeight());
         }

         public void componentResized(ComponentEvent var1) {
            if (SwingUtilities.getWindowAncestor(this.val$component.getParent()) != null) {
               Point var2 = SwingUtilities.convertPoint(this.val$component, this.val$component.getLocation(), (Component)null);
               Point var3 = SwingUtilities.convertPoint(this.val$component.getParent(), this.val$component.getLocation(), (Component)null);
               if (var2.getX() == var3.getX() && var2.getY() == var3.getY()) {
                  Insets var9 = MacOSXCanvasPeerInfo.access$000(this.this$0, this.val$component);
                  int var5 = var9 != null ? var9.top : 0;
                  int var6 = var9 != null ? var9.left : 0;
                  MacOSXCanvasPeerInfo.access$100(this.this$0.getHandle(), (int)var2.getX() - var6, (int)var2.getY() - var5, this.val$component.getWidth(), this.val$component.getHeight());
                  return;
               }
            }

            Insets var7 = MacOSXCanvasPeerInfo.access$200(this.this$0, this.val$component);
            int var8 = var7 != null ? var7.top : 0;
            int var4 = var7 != null ? var7.left : 0;
            MacOSXCanvasPeerInfo.access$100(this.this$0.getHandle(), this.val$component.getX() - var4, this.val$component.getY() - var8, this.val$component.getWidth(), this.val$component.getHeight());
         }

         public void componentShown(ComponentEvent var1) {
         }

         public String toString() {
            return "CanvasPeerInfoListener";
         }
      };
      var1.addComponentListener(var5);
   }

   private static native ByteBuffer nInitHandle(ByteBuffer var0, ByteBuffer var1, ByteBuffer var2, boolean var3, boolean var4, int var5, int var6) throws LWJGLException;

   private static native void nSetLayerPosition(ByteBuffer var0, int var1, int var2);

   private static native void nSetLayerBounds(ByteBuffer var0, int var1, int var2, int var3, int var4);

   protected void doUnlock() throws LWJGLException {
      this.awt_surface.unlock();
   }

   private Insets getWindowInsets(Canvas var1) {
      for(Container var2 = var1.getParent(); var2 != null; var2 = var2.getParent()) {
         if (var2 instanceof Window || var2 instanceof Applet) {
            return var2.getInsets();
         }
      }

      return null;
   }

   private Insets getInsets(Canvas var1) {
      for(Container var2 = var1.getParent(); var2 != null; var2 = var2.getParent()) {
         if (var2 instanceof Container) {
            return ((Container)var2).getInsets();
         }
      }

      return null;
   }

   static Insets access$000(MacOSXCanvasPeerInfo var0, Canvas var1) {
      return var0.getWindowInsets(var1);
   }

   static void access$100(ByteBuffer var0, int var1, int var2, int var3, int var4) {
      nSetLayerBounds(var0, var1, var2, var3, var4);
   }

   static Insets access$200(MacOSXCanvasPeerInfo var0, Canvas var1) {
      return var0.getInsets(var1);
   }
}
