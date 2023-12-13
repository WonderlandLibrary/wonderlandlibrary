package me.aidanmees.trivia.client.tools;

import java.awt.Color;
import java.awt.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class GuiUtils
{
  public static final RenderItem RENDER_ITEM = new RenderItem(Minecraft.getMinecraft().renderEngine, Minecraft.getMinecraft().modelManager);
  private static ScaledResolution scaledResolution;
  
  public static void enableGL2D()
  {
    GL11.glDisable(2929);
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glDepthMask(true);
    GL11.glEnable(2848);
    GL11.glHint(3154, 4354);
    GL11.glHint(3155, 4354);
  }
  
  public static void disableGL2D()
  {
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glEnable(2929);
    GL11.glDisable(2848);
    GL11.glHint(3154, 4352);
    GL11.glHint(3155, 4352);
  }
  
  public static void drawRect(Rectangle rectangle, int color)
  {
    drawRect(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, color);
  }
  
  public static void drawRect(float x, float y, float x1, float y1, int color)
  {
    enableGL2D();
    glColor(color);
    drawRect(x, y, x1, y1);
    disableGL2D();
  }
  
  public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int internalColor, int borderColor)
  {
    enableGL2D();
    glColor(internalColor);
    drawRect(x + width, y + width, x1 - width, y1 - width);
    glColor(borderColor);
    drawRect(x + width, y, x1 - width, y + width);
    drawRect(x, y, x + width, y1);
    drawRect(x1 - width, y, x1, y1);
    drawRect(x + width, y1 - width, x1 - width, y1);
    disableGL2D();
  }
  
  public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC)
  {
    enableGL2D();
    x *= 2.0F;
    x1 *= 2.0F;
    y *= 2.0F;
    y1 *= 2.0F;
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    drawVLine(x, y, y1, borderC);
    drawVLine(x1 - 1.0F, y, y1, borderC);
    drawHLine(x, x1 - 1.0F, y, borderC);
    drawHLine(x, x1 - 2.0F, y1 - 1.0F, borderC);
    drawRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, insideC);
    GL11.glScalef(2.0F, 2.0F, 2.0F);
    disableGL2D();
  }
  
  public static void drawBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth, int inside, int border)
  {
    enableGL2D();
    drawRect(x, y, x1, y1, inside);
    glColor(border);
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glLineWidth(lineWidth);
    GL11.glBegin(3);
    GL11.glVertex2f(x, y);
    GL11.glVertex2f(x, y1);
    GL11.glVertex2f(x1, y1);
    GL11.glVertex2f(x1, y);
    GL11.glVertex2f(x, y);
    GL11.glEnd();
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    disableGL2D();
  }
  
  public static void drawGradientBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth, int border, int bottom, int top)
  {
    enableGL2D();
    drawGradientRect(x, y, x1, y1, top, bottom);
    glColor(border);
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glLineWidth(lineWidth);
    GL11.glBegin(3);
    GL11.glVertex2f(x, y);
    GL11.glVertex2f(x, y1);
    GL11.glVertex2f(x1, y1);
    GL11.glVertex2f(x1, y);
    GL11.glVertex2f(x, y);
    GL11.glEnd();
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    disableGL2D();
  }
  
  public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC)
  {
    enableGL2D();
    x *= 2.0F;
    y *= 2.0F;
    x1 *= 2.0F;
    y1 *= 2.0F;
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    drawVLine(x, y + 1.0F, y1 - 2.0F, borderC);
    drawVLine(x1 - 1.0F, y + 1.0F, y1 - 2.0F, borderC);
    drawHLine(x + 2.0F, x1 - 3.0F, y, borderC);
    drawHLine(x + 2.0F, x1 - 3.0F, y1 - 1.0F, borderC);
    drawHLine(x + 1.0F, x + 1.0F, y + 1.0F, borderC);
    drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, borderC);
    drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, borderC);
    drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, borderC);
    drawRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, insideC);
    GL11.glScalef(2.0F, 2.0F, 2.0F);
    disableGL2D();
  }
  
  public static void drawBorderedRect(Rectangle rectangle, float width, int internalColor, int borderColor)
  {
    float x = rectangle.x;
    float y = rectangle.y;
    float x2 = rectangle.x + rectangle.width;
    float y2 = rectangle.y + rectangle.height;
    enableGL2D();
    glColor(internalColor);
    drawRect(x + width, y + width, x2 - width, y2 - width);
    glColor(borderColor);
    drawRect(x + 1.0F, y, x2 - 1.0F, y + width);
    drawRect(x, y, x + width, y2);
    drawRect(x2 - width, y, x2, y2);
    drawRect(x + 1.0F, y2 - width, x2 - 1.0F, y2);
    disableGL2D();
  }
  
  public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor)
  {
    enableGL2D();
    GL11.glShadeModel(7425);
    GL11.glBegin(7);
    glColor(topColor);
    GL11.glVertex2f(x, y1);
    GL11.glVertex2f(x1, y1);
    glColor(bottomColor);
    GL11.glVertex2f(x1, y);
    GL11.glVertex2f(x, y);
    GL11.glEnd();
    GL11.glShadeModel(7424);
    disableGL2D();
  }
  
  public static void drawGradientHRect(float x, float y, float x1, float y1, int topColor, int bottomColor)
  {
    enableGL2D();
    GL11.glShadeModel(7425);
    GL11.glBegin(7);
    glColor(topColor);
    GL11.glVertex2f(x, y);
    GL11.glVertex2f(x, y1);
    glColor(bottomColor);
    GL11.glVertex2f(x1, y1);
    GL11.glVertex2f(x1, y);
    GL11.glEnd();
    GL11.glShadeModel(7424);
    disableGL2D();
  }
  
  public static void drawGradientRect(double x, double y, double x2, double y2, int col1, int col2)
  {
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    GL11.glShadeModel(7425);
    GL11.glPushMatrix();
    GL11.glBegin(7);
    glColor(col1);
    GL11.glVertex2d(x2, y);
    GL11.glVertex2d(x, y);
    glColor(col2);
    GL11.glVertex2d(x, y2);
    GL11.glVertex2d(x2, y2);
    GL11.glEnd();
    GL11.glPopMatrix();
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glDisable(2848);
    GL11.glShadeModel(7424);
  }
  
  public static void drawGradientBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2, int col3)
  {
    enableGL2D();
    GL11.glPushMatrix();
    glColor(col1);
    GL11.glLineWidth(1.0F);
    GL11.glBegin(1);
    GL11.glVertex2d(x, y);
    GL11.glVertex2d(x, y2);
    GL11.glVertex2d(x2, y2);
    GL11.glVertex2d(x2, y);
    GL11.glVertex2d(x, y);
    GL11.glVertex2d(x2, y);
    GL11.glVertex2d(x, y2);
    GL11.glVertex2d(x2, y2);
    GL11.glEnd();
    GL11.glPopMatrix();
    drawGradientRect(x, y, x2, y2, col2, col3);
    disableGL2D();
  }
  
  public static void glColor(Color color)
  {
    GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
  }
  
  public static void glColor(int hex)
  {
    float alpha = (hex >> 24 & 0xFF) / 255.0F;
    float red = (hex >> 16 & 0xFF) / 255.0F;
    float green = (hex >> 8 & 0xFF) / 255.0F;
    float blue = (hex & 0xFF) / 255.0F;
    GL11.glColor4f(red, green, blue, alpha);
  }
  
  public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB)
  {
    float red = 0.003921569F * redRGB;
    float green = 0.003921569F * greenRGB;
    float blue = 0.003921569F * blueRGB;
    GL11.glColor4f(red, green, blue, alpha);
  }
  
  public static void drawStrip(int x, int y, float width, double angle, float points, float radius, int color)
  {
    float f1 = (color >> 24 & 0xFF) / 255.0F;
    float f2 = (color >> 16 & 0xFF) / 255.0F;
    float f3 = (color >> 8 & 0xFF) / 255.0F;
    float f4 = (color & 0xFF) / 255.0F;
    GL11.glPushMatrix();
    GL11.glTranslated(x, y, 0.0D);
    GL11.glColor4f(f2, f3, f4, f1);
    GL11.glLineWidth(width);
    if (angle > 0.0D)
    {
      GL11.glBegin(3);
      for (int i = 0; i < angle; i++)
      {
        float a = (float)(i * (angle * 3.141592653589793D / points));
        float xc = (float)(Math.cos(a) * radius);
        float yc = (float)(Math.sin(a) * radius);
        GL11.glVertex2f(xc, yc);
      }
      GL11.glEnd();
    }
    if (angle < 0.0D)
    {
      GL11.glBegin(3);
      for (int i = 0; i > angle; i--)
      {
        float a = (float)(i * (angle * 3.141592653589793D / points));
        float xc = (float)(Math.cos(a) * -radius);
        float yc = (float)(Math.sin(a) * -radius);
        GL11.glVertex2f(xc, yc);
      }
      GL11.glEnd();
    }
    disableGL2D();
    GL11.glDisable(3479);
    GL11.glPopMatrix();
  }
  
  public static void drawHLine(float x, float y, float x1, int y1)
  {
    if (y < x)
    {
      float var5 = x;
      x = y;
      y = var5;
    }
    drawRect(x, x1, y + 1.0F, x1 + 1.0F, y1);
  }
  
  public static void drawVLine(float x, float y, float x1, int y1)
  {
    if (x1 < y)
    {
      float var5 = y;
      y = x1;
      x1 = var5;
    }
    drawRect(x, y + 1.0F, x + 1.0F, x1, y1);
  }
  
  public static void drawHLine(float x, float y, float x1, int y1, int y2)
  {
    if (y < x)
    {
      float var5 = x;
      x = y;
      y = var5;
    }
    drawGradientRect(x, x1, y + 1.0F, x1 + 1.0F, y1, y2);
  }
  
  public static void drawRect(float x, float y, float x1, float y1, float r, float g, float b, float a)
  {
    enableGL2D();
    GL11.glColor4f(r, g, b, a);
    drawRect(x, y, x1, y1);
    disableGL2D();
  }
  
  public static void drawRect(float x, float y, float x1, float y1)
  {
    GL11.glBegin(7);
    GL11.glVertex2f(x, y1);
    GL11.glVertex2f(x1, y1);
    GL11.glVertex2f(x1, y);
    GL11.glVertex2f(x, y);
    GL11.glEnd();
  }
  
  public static void drawCircle(float cx, float cy, float r, int num_segments, int c)
  {
    r *= 2.0F;
    cx *= 2.0F;
    cy *= 2.0F;
    float f = (c >> 24 & 0xFF) / 255.0F;
    float f2 = (c >> 16 & 0xFF) / 255.0F;
    float f3 = (c >> 8 & 0xFF) / 255.0F;
    float f4 = (c & 0xFF) / 255.0F;
    float theta = (float)(6.2831852D / num_segments);
    float p = (float)Math.cos(theta);
    float s = (float)Math.sin(theta);
    float x = r;
    float y = 0.0F;
    enableGL2D();
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    GL11.glColor4f(f2, f3, f4, f);
    GL11.glBegin(2);
    for (int ii = 0; ii < num_segments; ii++)
    {
      GL11.glVertex2f(x + cx, y + cy);
      float t = x;
      x = p * x - s * y;
      y = s * t + p * y;
    }
    GL11.glEnd();
    GL11.glScalef(2.0F, 2.0F, 2.0F);
    disableGL2D();
  }
  
  public static void drawFullCircle(int cx, int cy, double r, int c)
  {
    r *= 2.0D;
    cx *= 2;
    cy *= 2;
    float f = (c >> 24 & 0xFF) / 255.0F;
    float f2 = (c >> 16 & 0xFF) / 255.0F;
    float f3 = (c >> 8 & 0xFF) / 255.0F;
    float f4 = (c & 0xFF) / 255.0F;
    enableGL2D();
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    GL11.glColor4f(f2, f3, f4, f);
    GL11.glBegin(6);
    for (int i = 0; i <= 360; i++)
    {
      double x = Math.sin(i * 3.141592653589793D / 180.0D) * r;
      double y = Math.cos(i * 3.141592653589793D / 180.0D) * r;
      GL11.glVertex2d(cx + x, cy + y);
    }
    GL11.glEnd();
    GL11.glScalef(2.0F, 2.0F, 2.0F);
    disableGL2D();
  }
  
  public static void drawSmallString(String s, int x, int y, int color)
  {
    GL11.glPushMatrix();
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    Minecraft.getMinecraft();
    Minecraft.fontRendererObj.drawString(s, x * 2, y * 2, color);
    GL11.glPopMatrix();
  }
  
  public static void drawLargeString(String text, int x, int y, int color)
  {
    x *= 2;
    GL11.glPushMatrix();
    GL11.glScalef(1.5F, 1.5F, 1.5F);
    Minecraft.getMinecraft();
    Minecraft.fontRendererObj.drawString(text, x, y, color);
    GL11.glPopMatrix();
  }
  
  public static ScaledResolution getScaledResolution()
  {
    return scaledResolution;
  }
  
  public static void draw2D(Entity e, double posX, double posY, double posZ, float alpha, float red, float green, float blue)
  {
    GlStateManager.pushMatrix();
    GlStateManager.translate(posX, posY, posZ);
    GL11.glNormal3f(0.0F, 0.0F, 0.0F);
    GlStateManager.rotate(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
    GlStateManager.scale(-0.1D, -0.1D, 0.1D);
    GL11.glDisable(2896);
    GL11.glDisable(2929);
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    GlStateManager.bindCurrentTexture();
    GlStateManager.depthMask(true);
    drawOutlineRect(-6.2F, -19.2F, 6.2F, 2.2F, new Color(0, 0, 0, 80).getRGB());
    drawRect(-5.8F, -18.8F, 5.8F, 1.8F, new Color(0, 0, 0, 80).getRGB());
    drawOutlinedRect(-6.0F, -19.0F, 6.0F, 2.0F, alpha, red, green, blue);
    GL11.glDisable(3042);
    GL11.glEnable(2929);
    GL11.glEnable(2896);
    GlStateManager.popMatrix();
  }
  
  public static void drawRectColor(double d, double e, double f2, double f3, float alpha, float red, float green, float blue)
  {
    GlStateManager.enableBlend();
    enableGL2D();
    GlStateManager.checkBoundTexture();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GL11.glPushMatrix();
    GL11.glColor4f(alpha, red, green, blue);
    GL11.glBegin(7);
    GL11.glVertex2d(f2, e);
    GL11.glVertex2d(d, e);
    GL11.glVertex2d(d, f3);
    GL11.glVertex2d(f2, f3);
    GL11.glEnd();
    disableGL2D();
    GlStateManager.bindCurrentTexture();
    GlStateManager.disableBlend();
    GL11.glPopMatrix();
  }
  
  public static void drawOutlineRect(float drawX, float drawY, float drawWidth, float drawHeight, int color)
  {
    drawRect(drawX, drawY, drawWidth, drawY + 0.5F, color);
    drawRect(drawX, drawY + 0.5F, drawX + 0.5F, drawHeight, color);
    drawRect(drawWidth - 0.5F, drawY + 0.5F, drawWidth, drawHeight - 0.5F, color);
    drawRect(drawX + 0.5F, drawHeight - 0.5F, drawWidth, drawHeight, color);
  }
  
  public static void drawOutlinedRect(float drawX, float drawY, float drawWidth, float drawHeight, float alpha, float red, float green, float blue)
  {
    drawRectColor(drawX, drawY, drawWidth, drawY + 0.5F, alpha, red, green, blue);
    drawRectColor(drawX, drawY + 0.5F, drawX + 0.5F, drawHeight, alpha, red, green, blue);
    drawRectColor(drawWidth - 0.5F, drawY + 0.5F, drawWidth, drawHeight - 0.5F, alpha, red, green, blue);
    drawRectColor(drawX + 0.5F, drawHeight - 0.5F, drawWidth, drawHeight, alpha, red, green, blue);
  }
}

