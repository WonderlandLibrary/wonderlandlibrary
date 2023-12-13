package org.newdawn.slick.opengl.renderer;

public class DefaultLineStripRenderer implements LineStripRenderer {
   private static SGL GL = Renderer.get();

   public void end() {
      GL.glEnd();
   }

   public void setAntiAlias(boolean var1) {
      if (var1) {
         GL.glEnable(2848);
      } else {
         GL.glDisable(2848);
      }

   }

   public void setWidth(float var1) {
      GL.glLineWidth(var1);
   }

   public void start() {
      GL.glBegin(3);
   }

   public void vertex(float var1, float var2) {
      GL.glVertex2f(var1, var2);
   }

   public void color(float var1, float var2, float var3, float var4) {
      GL.glColor4f(var1, var2, var3, var4);
   }

   public void setLineCaps(boolean var1) {
   }

   public boolean applyGLLineFixes() {
      return true;
   }
}
