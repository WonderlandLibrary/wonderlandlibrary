package me.theresa.fontRenderer.font.opengl.renderer;

public interface LineStripRenderer {
   boolean applyGLLineFixes();

   void start();

   void end();

   void vertex(float var1, float var2);

   void color(float var1, float var2, float var3, float var4);

   void setWidth(float var1);

   void setAntiAlias(boolean var1);

   void setLineCaps(boolean var1);
}
