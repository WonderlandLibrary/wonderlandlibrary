package me.theresa.fontRenderer.font.opengl.renderer;

public class DefaultLineStripRenderer implements LineStripRenderer {
   private final SGL GL = Renderer.get();

   public void end() {
      this.GL.glEnd();
   }

   public void setAntiAlias(boolean antialias) {
      if (antialias) {
         this.GL.glEnable(2848);
      } else {
         this.GL.glDisable(2848);
      }

   }

   public void setWidth(float width) {
      this.GL.glLineWidth(width);
   }

   public void start() {
      this.GL.glBegin(3);
   }

   public void vertex(float x, float y) {
      this.GL.glVertex2f(x, y);
   }

   public void color(float r, float g, float b, float a) {
      this.GL.glColor4f(r, g, b, a);
   }

   public void setLineCaps(boolean caps) {
   }

   public boolean applyGLLineFixes() {
      return true;
   }
}
