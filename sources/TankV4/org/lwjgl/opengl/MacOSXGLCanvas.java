package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.Graphics;

final class MacOSXGLCanvas extends Canvas {
   private static final long serialVersionUID = 6916664741667434870L;
   private boolean canvas_painted;
   private boolean dirty;

   public void update(Graphics var1) {
      this.paint(var1);
   }

   public void paint(Graphics var1) {
      synchronized(this){}
      this.dirty = true;
      this.canvas_painted = true;
   }

   public boolean syncCanvasPainted() {
      synchronized(this){}
      boolean var1 = this.canvas_painted;
      this.canvas_painted = false;
      return var1;
   }

   public boolean syncIsDirty() {
      synchronized(this){}
      boolean var1 = this.dirty;
      this.dirty = false;
      return var1;
   }
}
