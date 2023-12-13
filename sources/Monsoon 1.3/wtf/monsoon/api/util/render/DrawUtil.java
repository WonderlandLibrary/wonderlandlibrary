package wtf.monsoon.api.util.render;

import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiScreen;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

public class DrawUtil extends GuiScreen {
	
	public static DrawUtil instance = new DrawUtil();
  public static void drawRoundedRect(double x, double y, double x1, double y1, double radius, int color) {
    GL11.glPushAttrib(0);
    GL11.glScaled(0.5D, 0.5D, 0.5D);
    x *= 2.0D;
    y *= 2.0D;
    x1 *= 2.0D;
    y1 *= 2.0D;
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    setColor(color);
    GL11.glEnable(2848);
    GL11.glBegin(9);
    int i;
    for (i = 0; i <= 90; i += 3)
      GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D); 
    for (i = 90; i <= 180; i += 3)
      GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D); 
    for (i = 0; i <= 90; i += 3)
      GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius); 
    for (i = 90; i <= 180; i += 3)
      GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius); 
    GL11.glEnd();
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glDisable(2848);
    GL11.glDisable(3042);
    GL11.glEnable(3553);
    GL11.glScaled(2.0D, 2.0D, 2.0D);
    GL11.glPopAttrib();
  }
  
  public static void drawBorderedRoundedRect(float x, float y, float x1, float y1, float borderSize, int borderC, int insideC) {
    drawRoundedRect(x, y, x1, y1, borderSize, borderC);
    drawRoundedRect((x + 0.5F), (y + 0.5F), (x1 - 0.5F), (y1 - 0.5F), borderSize, insideC);
  }
  
  public static void drawBorderedRoundedRect(float x, float y, float x1, float y1, float radius, float borderSize, int borderC, int insideC) {
    drawRoundedRect(x, y, x1, y1, radius, borderC);
    drawRoundedRect((x + borderSize), (y + borderSize), (x1 - borderSize), (y1 - borderSize), radius, insideC);
  }
  
  public static void setColor(int color) {
    float a = (color >> 24 & 0xFF) / 255.0F;
    float r = (color >> 16 & 0xFF) / 255.0F;
    float g = (color >> 8 & 0xFF) / 255.0F;
    float b = (color & 0xFF) / 255.0F;
    GL11.glColor4f(r, g, b, a);
  }
  public static void drawHollowRect(int x, int y, int w, int h, int color) {

      Gui.drawHorizontalLine(x, x + w, y, color);
      Gui.drawHorizontalLine(x, x + w, y + h, color);

      Gui.drawVerticalLine(x, y + h, y, color);
      Gui.drawVerticalLine(x + w, y + h, y, color);

  }
  public static void drawHollowRect(float x, float y, float w, float h, int color) {

      Gui.drawHorizontalLine(x, w, y, color);
      Gui.drawHorizontalLine(x,  w, h, color);

      Gui.drawVerticalLine(x, h, y, color);
      Gui.drawVerticalLine(w, h, y, color);

  }

  public static void draw2DImage(ResourceLocation image, int x, int y, int width, int height, Color c) {
    glDisable(GL_DEPTH_TEST);
    glEnable(GL_BLEND);
    glDepthMask(false);
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha());
    Minecraft.getMinecraft().getTextureManager().bindTexture(image);
    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, width, height);
    glDepthMask(true);
    glDisable(GL_BLEND);
    glEnable(GL_DEPTH_TEST);
    glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  }

  public static void draw2DImage(ResourceLocation image, float x, float y, float width, float height, Color c) {
    glDisable(GL_DEPTH_TEST);
    glEnable(GL_BLEND);
    glDepthMask(false);
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha());
    Minecraft.getMinecraft().getTextureManager().bindTexture(image);
    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, width, height);
    glDepthMask(true);
    glDisable(GL_BLEND);
    glEnable(GL_DEPTH_TEST);
    glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  }
}
