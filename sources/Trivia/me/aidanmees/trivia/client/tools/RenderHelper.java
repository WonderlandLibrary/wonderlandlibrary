package me.aidanmees.trivia.client.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

public class RenderHelper
{
  public static CFontRenderer font = new CFontRenderer(new Font("Roboto", 0, 18), true, true);
  public static CFontRenderer comfortaalight = new CFontRenderer(new Font("COMFORTAA-LIGHT", 0, 16), true, true);
  public static CFontRenderer adam = new CFontRenderer(new Font("Adam", 0, 20), true, true);
  public static CFontRenderer adam_60 = new CFontRenderer(new Font("Adam", 0, 60), true, true);
  public static CFontRenderer comfortaalight_60 = new CFontRenderer(new Font("COMFORTAA-LIGHT", 0, 60), true, true);
  private static Map<Integer, Boolean> glCapMap = new HashMap();
  
  public void init()
  {
    URL fontUrlRoboto = null;
    Font fontRoboto = null;
    URL fontUrlComfortaa = null;
    Font fontComfortaa = null;
    URL fontUrlAdam = null;
    Font fontAdam = null;
    try
    {
      fontUrlRoboto = new URL("http://download1593.mediafire.com/km93z40p1h1g/sg6nw8q9audzcj4/roboto.ttf");
      fontRoboto = Font.createFont(0, fontUrlRoboto.openStream());
      fontUrlComfortaa = new URL("http://download1509.mediafire.com/6y40e4c7o3zg/h4jnkbmxlrc022r/COMFORTAA-LIGHT.TTF");
      fontComfortaa = Font.createFont(0, fontUrlComfortaa.openStream());
      fontUrlAdam = new URL("http://download1511.mediafire.com/gxvx539cm4dg/0f5o1bzn5zyaquc/Adam.otf");
      fontAdam = Font.createFont(0, fontUrlAdam.openStream());
    }
    catch (FontFormatException localFontFormatException) {}catch (IOException localIOException) {}
    font = new CFontRenderer(new Font("Roboto", 0, 18), true, true);
    comfortaalight = new CFontRenderer(new Font("Comfortaa", 0, 16), true, true);
    comfortaalight_60 = new CFontRenderer(new Font("Comfortaa", 0, 60), true, true);
    adam = new CFontRenderer(new Font("Adam", 0, 20), true, true);
    adam_60 = new CFontRenderer(new Font("Adam", 0, 60), true, true);
  }
  
  public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2)
  {
    drawRect(x, y, x2, y2, col2);
    float f = (col1 >> 24 & 0xFF) / 255.0F;
    float f2 = (col1 >> 16 & 0xFF) / 255.0F;
    float f3 = (col1 >> 8 & 0xFF) / 255.0F;
    float f4 = (col1 & 0xFF) / 255.0F;
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    GL11.glPushMatrix();
    GL11.glColor4f(f2, f3, f4, f);
    GL11.glLineWidth(l1);
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
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glDisable(2848);
  }
  
  public static void drawRect(float g, float h, float i, float j, int col1)
  {
    float f = (col1 >> 24 & 0xFF) / 255.0F;
    float f2 = (col1 >> 16 & 0xFF) / 255.0F;
    float f3 = (col1 >> 8 & 0xFF) / 255.0F;
    float f4 = (col1 & 0xFF) / 255.0F;
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    GL11.glPushMatrix();
    GL11.glColor4f(f2, f3, f4, f);
    GL11.glBegin(7);
    GL11.glVertex2d(i, h);
    GL11.glVertex2d(g, h);
    GL11.glVertex2d(g, j);
    GL11.glVertex2d(i, j);
    GL11.glEnd();
    GL11.glPopMatrix();
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glDisable(2848);
  }
  public static void drawRectRGB(float g, float h, float i, float j, int RED,int GREEN,int BLUE , int ALPHA)
  {
   
    float f2 = RED;
    float f3 = GREEN;
    float f4 = BLUE;
    float f = ALPHA;
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    GL11.glPushMatrix();
    GL11.glColor4f(f2, f3, f4, f);
    GL11.glBegin(7);
    GL11.glVertex2d(i, h);
    GL11.glVertex2d(g, h);
    GL11.glVertex2d(g, j);
    GL11.glVertex2d(i, j);
    GL11.glEnd();
    GL11.glPopMatrix();
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glDisable(2848);
  }
  
  public static void setColor(Color c)
  {
    GL11.glColor4f(c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F, c.getAlpha() / 255.0F);
  }
  
  public static void drawIcon(float x, float y, ResourceLocation resourceLocation)
  {
    GL11.glPushMatrix();
    Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
    GlStateManager.enableBlend();
    GL11.glEnable(2848);
    GlStateManager.enableRescaleNormal();
    GlStateManager.enableAlpha();
    GlStateManager.alphaFunc(516, 0.1F);
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(770, 771);
    GL11.glTranslatef(x, y, 0.0F);
    drawScaledRect(0, 0, 0.0F, 0.0F, 12, 12, 12, 12, 12.0F, 12.0F);
    GlStateManager.disableAlpha();
    GlStateManager.disableRescaleNormal();
    GlStateManager.disableLighting();
    GlStateManager.disableRescaleNormal();
    GL11.glDisable(2848);
    GlStateManager.disableBlend();
    GL11.glPopMatrix();
  }
  
  public static void drawIcon(float x, float y, int size, ResourceLocation resourceLocation)
  {
    GL11.glPushMatrix();
    Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    GlStateManager.enableRescaleNormal();
    GlStateManager.enableAlpha();
    GlStateManager.alphaFunc(516, 0.1F);
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(770, 771);
    GL11.glTranslatef(x, y, 0.0F);
    drawScaledRect(0, 0, 0.0F, 0.0F, size, size, size, size, size, size);
    GlStateManager.disableAlpha();
    GlStateManager.disableRescaleNormal();
    GlStateManager.disableLighting();
    GlStateManager.disableRescaleNormal();
    GL11.glDisable(2848);
    GlStateManager.disableBlend();
    GL11.glPopMatrix();
  }
  
  
  public static void drawIcon(float x, float y, int sizex, int sizey, ResourceLocation resourceLocation)
  {
    GL11.glPushMatrix();
    Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    GlStateManager.enableRescaleNormal();
    GlStateManager.enableAlpha();
    GlStateManager.alphaFunc(516, 0.1F);
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(770, 771);
    GL11.glTranslatef(x, y, 0.0F);
    drawScaledRect(0, 0, 0.0F, 0.0F, sizex, sizey, sizex, sizey, sizex, sizey);
    GlStateManager.disableAlpha();
    GlStateManager.disableRescaleNormal();
    GlStateManager.disableLighting();
    GlStateManager.disableRescaleNormal();
    GL11.glDisable(2848);
    GlStateManager.disableBlend();
    GL11.glPopMatrix();
  }
  
  public static void drawScaledRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight)
  {
    Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
  }
  
  public static int getStringWidth(String text)
  {
    Minecraft.getMinecraft();return Minecraft.fontRendererObj.getStringWidth(text);
  }
  
  public static Color rainbow(long offset, float fade)
  {
    float hue = (float)(System.nanoTime() + offset) / 1.0E10F % 1.0F;
    long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()), 16);
    Color c = new Color((int)color);
    return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade, c.getAlpha() / 255.0F);
  }
  
  public static void draw(float x, float y, float w, float h)
  {
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    GL11.glPushMatrix();
    int i = 1;
    for (float x2 = 0.0F; x2 <= w; x2 += 0.1F)
    {
      i *= 500;
      setColor(rainbow(i, 1.0F));
      GL11.glBegin(0);
      GL11.glVertex2d(x + x2, y);
      GL11.glEnd();
      GL11.glPopMatrix();
    }
    for (float y2 = h; y2 >= 0.0F; y2 -= 0.1F)
    {
      i *= 500;
      setColor(rainbow(i, 1.0F));
      GL11.glBegin(0);
      GL11.glVertex2d(x + w, y + y2);
      GL11.glEnd();
      GL11.glPopMatrix();
    }
    for (float x2 = 0.0F; x2 <= w; x2 += 0.1F)
    {
      i *= 500;
      setColor(rainbow(i, 1.0F));
      GL11.glBegin(0);
      GL11.glVertex2d(x + x2, y + h);
      GL11.glEnd();
      GL11.glPopMatrix();
    }
    for (float y2 = h; y2 >= 0.0F; y2 -= 0.1F)
    {
      i *= 500;
      setColor(rainbow(i, 1.0F));
      GL11.glBegin(0);
      GL11.glVertex2d(x, y + y2);
      GL11.glEnd();
      GL11.glPopMatrix();
    }
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glDisable(2848);
  }
  
  public static void drawItemStack(ItemStack stack, int x, int y)
  {
    GL11.glDepthMask(false);
    Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(stack, x, y);
    GL11.glDepthMask(true);
  }
  
  public static int createShader(String shaderCode, int shaderType)
    throws Exception
  {
    int shader = 0;
    try
    {
      shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
      if (shader == 0) {
        return 0;
      }
      ARBShaderObjects.glShaderSourceARB(shader, shaderCode);
      ARBShaderObjects.glCompileShaderARB(shader);
      if (ARBShaderObjects.glGetObjectParameteriARB(shader, 35713) == 0) {
        throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
      }
      return shader;
    }
    catch (Exception exc)
    {
      ARBShaderObjects.glDeleteObjectARB(shader);
      throw exc;
    }
  }
  
  public static String getLogInfo(int obj)
  {
    return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, 35716));
  }
  
  public static void setGLCap(int cap, boolean flag)
  {
    glCapMap.put(Integer.valueOf(cap), Boolean.valueOf(GL11.glGetBoolean(cap)));
    if (flag) {
      GL11.glEnable(cap);
    } else {
      GL11.glDisable(cap);
    }
  }
  
  public static void revertGLCap(int cap)
  {
    Boolean origCap = (Boolean)glCapMap.get(Integer.valueOf(cap));
    if (origCap != null) {
      if (origCap.booleanValue()) {
        GL11.glEnable(cap);
      } else {
        GL11.glDisable(cap);
      }
    }
  }
  
  public static void revertAllCaps()
  {
    for (Iterator localIterator = glCapMap.keySet().iterator(); localIterator.hasNext();)
    {
      int cap = ((Integer)localIterator.next()).intValue();
      revertGLCap(cap);
    }
  }
  
  public static void drawFullCircle(int cx, double cy, double r, int segments, float lineWidth, int part, int c)
  {
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    r *= 2.0D;
    cx *= 2;
    cy *= 2;
    float f = (c >> 24 & 0xFF) / 255.0F;
    float f2 = (c >> 16 & 0xFF) / 255.0F;
    float f3 = (c >> 8 & 0xFF) / 255.0F;
    float f4 = (c & 0xFF) / 255.0F;
    GL11.glEnable(3042);
    GL11.glLineWidth(lineWidth);
    GL11.glDisable(3553);
    GL11.glEnable(2848);
    GL11.glBlendFunc(770, 771);
    GL11.glColor4f(f2, f3, f4, f);
    GL11.glBegin(3);
    for (int i = segments - part; i <= segments; i++)
    {
      double x = Math.sin(i * 3.141592653589793D / 180.0D) * r;
      double y = Math.cos(i * 3.141592653589793D / 180.0D) * r;
      GL11.glVertex2d(cx + x, cy + y);
    }
    GL11.glEnd();
    GL11.glDisable(2848);
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glScalef(2.0F, 2.0F, 2.0F);
  }
  public static void drawFullCircleRGB(int cx, double cy, double r, int segments, float lineWidth, int part, float red, float green, float blue, float alpha)
  {
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    r *= 2.0D;
    cx *= 2;
    cy *= 2;
    float f = alpha / 255;
    float f2 = red / 255;
    float f3 = green / 255;
    float f4 = blue / 255;
    GL11.glEnable(3042);
    GL11.glLineWidth(lineWidth);
    GL11.glDisable(3553);
    GL11.glEnable(2848);
    GL11.glBlendFunc(770, 771);
    GL11.glColor4f(f2, f3, f4, f);
    GL11.glBegin(3);
    for (int i = segments - part; i <= segments; i++)
    {
      double x = Math.sin(i * 3.141592653589793D / 180.0D) * r;
      double y = Math.cos(i * 3.141592653589793D / 180.0D) * r;
      GL11.glVertex2d(cx + x, cy + y);
    }
    GL11.glEnd();
    GL11.glDisable(2848);
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glScalef(2.0F, 2.0F, 2.0F);
  }
  
  public static void drawHLine(float par1, float par2, float par3, int par4)
  {
    if (par2 < par1)
    {
      float var5 = par1;
      par1 = par2;
      par2 = var5;
    }
    drawRect(par1, par3, par2 + 3.0F, par3 + 3.0F, par4);
  }
  public static void drawHLineRGB(float par1, float par2, float par3, int RED,int GREEN,int BLUE, int ALPHA)
  {
    if (par2 < par1)
    {
      float var5 = par1;
      par1 = par2;
      par2 = var5;
    }
    drawRectRGB(par1, par3, par2 + 1.0F, par3 + 1.0F, RED,GREEN,BLUE,ALPHA);
  }
}
