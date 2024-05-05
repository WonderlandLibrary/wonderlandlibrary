package space.lunaclient.luna.util.particle.utils;

import org.lwjgl.opengl.GL11;

public class ParticleRenderUtils
{
  public ParticleRenderUtils() {}
  
  public static void drawBorderedCircle(int x, int y, float radius, int outsideC, int insideC)
  {
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    GL11.glPushMatrix();
    float scale = 0.1F;
    GL11.glScalef(scale, scale, scale);
    x = (int)(x * (1.0F / scale));
    y = (int)(y * (1.0F / scale));
    radius *= 1.0F / scale;
    drawCircle(x, y, radius, insideC);
    drawUnfilledCircle(x, y, radius, 1.0F, outsideC);
    GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);
    GL11.glPopMatrix();
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glDisable(2848);
  }
  
  public static void drawUnfilledCircle(int x, int y, float radius, float lineWidth, int color)
  {
    float alpha = (color >> 24 & 0xFF) / 255.0F;
    float red = (color >> 16 & 0xFF) / 255.0F;
    float green = (color >> 8 & 0xFF) / 255.0F;
    float blue = (color & 0xFF) / 255.0F;
    GL11.glColor4f(red, green, blue, alpha);
    GL11.glLineWidth(lineWidth);
    GL11.glEnable(2848);
    GL11.glBegin(2);
    for (int i = 0; i <= 360; i++) {
      GL11.glVertex2d(x + Math.sin(i * 3.141526D / 180.0D) * radius, y + Math.cos(i * 3.141526D / 180.0D) * radius);
    }
    GL11.glEnd();
    GL11.glDisable(2848);
  }
  
  public static void drawCircle(int x, int y, float radius, int color)
  {
    float alpha = (color >> 24 & 0xFF) / 255.0F;
    float red = (color >> 16 & 0xFF) / 255.0F;
    float green = (color >> 8 & 0xFF) / 255.0F;
    float blue = (color & 0xFF) / 255.0F;
    GL11.glColor4f(red, green, blue, alpha);
    GL11.glLineWidth(0.25F);
    GL11.glBegin(9);
    for (int i = 0; i <= 360; i++) {
      GL11.glVertex2d(x + Math.sin(i * 3.141526D / 180.0D) * radius, y + Math.cos(i * 3.141526D / 180.0D) * radius);
    }
    GL11.glEnd();
  }
}
