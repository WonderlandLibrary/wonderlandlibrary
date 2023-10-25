package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.Graphics;

final class MacOSXGLCanvas extends Canvas {
  private static final long serialVersionUID = 6916664741667434870L;
  
  private boolean canvas_painted;
  
  private boolean dirty;
  
  public void update(Graphics g) {
    paint(g);
  }
  
  public void paint(Graphics g) {
    synchronized (this) {
      this.dirty = true;
      this.canvas_painted = true;
    } 
  }
  
  public boolean syncCanvasPainted() {
    boolean result;
    synchronized (this) {
      result = this.canvas_painted;
      this.canvas_painted = false;
    } 
    return result;
  }
  
  public boolean syncIsDirty() {
    boolean result;
    synchronized (this) {
      result = this.dirty;
      this.dirty = false;
    } 
    return result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\MacOSXGLCanvas.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */