package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

final class MacOSXCanvasListener implements ComponentListener, HierarchyListener {
   private final Canvas canvas;
   private int width;
   private int height;
   private boolean context_update;
   private boolean resized;

   MacOSXCanvasListener(Canvas var1) {
      this.canvas = var1;
      var1.addComponentListener(this);
      var1.addHierarchyListener(this);
      this.setUpdate();
   }

   public void disableListeners() {
      java.awt.EventQueue.invokeLater(new Runnable(this) {
         final MacOSXCanvasListener this$0;

         {
            this.this$0 = var1;
         }

         public void run() {
            MacOSXCanvasListener.access$000(this.this$0).removeComponentListener(this.this$0);
            MacOSXCanvasListener.access$000(this.this$0).removeHierarchyListener(this.this$0);
         }
      });
   }

   public boolean syncShouldUpdateContext() {
      synchronized(this){}
      boolean var1 = this.context_update;
      this.context_update = false;
      return var1;
   }

   private synchronized void setUpdate() {
      synchronized(this){}
      this.width = this.canvas.getWidth();
      this.height = this.canvas.getHeight();
      this.context_update = true;
   }

   public int syncGetWidth() {
      synchronized(this){}
      return this.width;
   }

   public int syncGetHeight() {
      synchronized(this){}
      return this.height;
   }

   public void componentShown(ComponentEvent var1) {
   }

   public void componentHidden(ComponentEvent var1) {
   }

   public void componentResized(ComponentEvent var1) {
      this.setUpdate();
      this.resized = true;
   }

   public void componentMoved(ComponentEvent var1) {
      this.setUpdate();
   }

   public void hierarchyChanged(HierarchyEvent var1) {
      this.setUpdate();
   }

   public boolean wasResized() {
      if (this.resized) {
         this.resized = false;
         return true;
      } else {
         return false;
      }
   }

   static Canvas access$000(MacOSXCanvasListener var0) {
      return var0.canvas;
   }
}
