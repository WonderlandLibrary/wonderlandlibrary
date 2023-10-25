package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.EventQueue;
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
  
  MacOSXCanvasListener(Canvas canvas) {
    this.canvas = canvas;
    canvas.addComponentListener(this);
    canvas.addHierarchyListener(this);
    setUpdate();
  }
  
  public void disableListeners() {
    EventQueue.invokeLater(new Runnable() {
          public void run() {
            MacOSXCanvasListener.this.canvas.removeComponentListener(MacOSXCanvasListener.this);
            MacOSXCanvasListener.this.canvas.removeHierarchyListener(MacOSXCanvasListener.this);
          }
        });
  }
  
  public boolean syncShouldUpdateContext() {
    boolean should_update;
    synchronized (this) {
      should_update = this.context_update;
      this.context_update = false;
    } 
    return should_update;
  }
  
  private synchronized void setUpdate() {
    synchronized (this) {
      this.width = this.canvas.getWidth();
      this.height = this.canvas.getHeight();
      this.context_update = true;
    } 
  }
  
  public int syncGetWidth() {
    synchronized (this) {
      return this.width;
    } 
  }
  
  public int syncGetHeight() {
    synchronized (this) {
      return this.height;
    } 
  }
  
  public void componentShown(ComponentEvent e) {}
  
  public void componentHidden(ComponentEvent e) {}
  
  public void componentResized(ComponentEvent e) {
    setUpdate();
    this.resized = true;
  }
  
  public void componentMoved(ComponentEvent e) {
    setUpdate();
  }
  
  public void hierarchyChanged(HierarchyEvent e) {
    setUpdate();
  }
  
  public boolean wasResized() {
    if (this.resized) {
      this.resized = false;
      return true;
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\MacOSXCanvasListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */