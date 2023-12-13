package me.aidanmees.trivia.client.tools;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class DarkRenderer
{
  static
  {
    Minecraft.getMinecraft();
  }
  
  private static FontRenderer fr = Minecraft.fontRendererObj;
  
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
  
  public static void drawSmallString(String text, int x, int y)
  {
    x *= 2;
    GL11.glScalef(0.8F, 0.8F, 0.8F);
    Minecraft.getMinecraft();
    Minecraft.fontRendererObj.drawString(text, x, y, 16777215);
    GL11.glScalef(1.25F, 1.25F, 1.25F);
  }
  
  public static void drawLargerString(String text, int x, int y)
  {
    x *= 2;
    GL11.glScalef(1.5F, 1.5F, 1.5F);
    Minecraft.getMinecraft();
    Minecraft.fontRendererObj.drawString(text, x, y, 16777215);
    GL11.glScalef(0.665F, 0.665F, 0.665F);
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
  
  public static void drawCentredStringWithShadow(String s, int x, int y, int colour)
  {
    x -= fr.getStringWidth(s) / 2;
    fr.drawString(s, x, y, colour);
  }
  
  public static void drawRect(int x, int y, int x2, int y2, int color)
  {
    Gui.drawRect(x, y, x2, y2, color);
  }
  
  public static void drawRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, int paramColor)
  {
    float alpha = (paramColor >> 24 & 0xFF) / 255.0F;
    float red = (paramColor >> 16 & 0xFF) / 255.0F;
    float green = (paramColor >> 8 & 0xFF) / 255.0F;
    float blue = (paramColor & 0xFF) / 255.0F;
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    GL11.glPushMatrix();
    GL11.glColor4f(red, green, blue, alpha);
    GL11.glBegin(7);
    GL11.glVertex2d(paramXEnd, paramYStart);
    GL11.glVertex2d(paramXStart, paramYStart);
    GL11.glVertex2d(paramXStart, paramYEnd);
    GL11.glVertex2d(paramXEnd, paramYEnd);
    GL11.glEnd();
    GL11.glPopMatrix();
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glDisable(2848);
  }
  
  public static void drawPoint(int x, int y, int color)
  {
    drawRect(x, y, x + 1, y + 1, color);
  }
  
  public static void drawVerticalLine(int x, int y, int height, int color)
  {
    drawRect(x, y, x + 1, height, color);
  }
  
  public static void drawHorizontalLine(int x, int y, int width, int color)
  {
    drawRect(x, y, width, y + 1, color);
  }
  
  public static void drawOutlinedBoundingBox(AxisAlignedBB aa)
  {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldRenderer = tessellator.getWorldRenderer();
    worldRenderer.startDrawing(3);
    worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
    worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
    tessellator.draw();
    worldRenderer.startDrawing(3);
    worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
    worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
    tessellator.draw();
    worldRenderer.startDrawing(1);
    worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
    worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
    worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
    tessellator.draw();
  }
  
  public static void drawBoundingBox(AxisAlignedBB aa)
  {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldRenderer = tessellator.getWorldRenderer();
    worldRenderer.startDrawingQuads();
    worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
    worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
    worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
    tessellator.draw();
    worldRenderer.startDrawingQuads();
    worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
    worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
    worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
    worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
    tessellator.draw();
    worldRenderer.startDrawingQuads();
    worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
    worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
    worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
    tessellator.draw();
    worldRenderer.startDrawingQuads();
    worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
    worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
    worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
    worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
    tessellator.draw();
    worldRenderer.startDrawingQuads();
    worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
    worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
    worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
    worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
    tessellator.draw();
    worldRenderer.startDrawingQuads();
    worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
    worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
    worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
    tessellator.draw();
  }
  
  public static void drawHat(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith)
  {
    GL11.glPushMatrix();
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    GL11.glDisable(3553);
    GL11.glEnable(2848);
    GL11.glDepthMask(false);
    GL11.glColor4f(red, green, blue, alpha);
    drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
    GL11.glLineWidth(lineWdith);
    GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
    drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
    GL11.glDisable(2848);
    GL11.glEnable(3553);
    GL11.glEnable(2929);
    GL11.glDepthMask(true);
    GL11.glDisable(3042);
    GL11.glPopMatrix();
  }
  
  public static void drawBorderedRect(int x, int y, int x1, int y1, int bord, int color)
  {
    drawRect(x + 1, y + 1, x1, y1, color);
    drawVerticalLine(x, y, y1, bord);
    drawVerticalLine(x1, y, y1, bord);
    drawHorizontalLine(x + 1, y, x1, bord);
    drawHorizontalLine(x, y1, x1 + 1, bord);
  }
  
  public static void drawFineBorderedRect(int x, int y, int x1, int y1, int bord, int color)
  {
    GL11.glScaled(0.5D, 0.5D, 0.5D);
    x *= 2;
    y *= 2;
    x1 *= 2;
    y1 *= 2;
    drawRect(x + 1, y + 1, x1, y1, color);
    drawVerticalLine(x, y, y1, bord);
    drawVerticalLine(x1, y, y1, bord);
    drawHorizontalLine(x + 1, y, x1, bord);
    drawHorizontalLine(x, y1, x1 + 1, bord);
    GL11.glScaled(2.0D, 2.0D, 2.0D);
  }
  
  public static void drawBorderRectNoCorners(int x, int y, int x2, int y2, int bord, int color)
  {
    x *= 2;
    y *= 2;
    x2 *= 2;
    y2 *= 2;
    GL11.glScaled(0.5D, 0.5D, 0.5D);
    drawRect(x + 1, y + 1, x2, y2, color);
    drawVerticalLine(x, y + 1, y2, bord);
    drawVerticalLine(x2, y + 1, y2, bord);
    drawHorizontalLine(x + 1, y, x2, bord);
    drawHorizontalLine(x + 1, y2, x2, bord);
    GL11.glScaled(2.0D, 2.0D, 2.0D);
  }
  
  public static void drawBorderedGradient(int x, int y, int x1, int y1, int bord, int gradTop, int gradBot)
  {
    GL11.glScaled(0.5D, 0.5D, 0.5D);
    x *= 2;
    y *= 2;
    x1 *= 2;
    y1 *= 2;
    float f = (gradTop >> 24 & 0xFF) / 255.0F;
    float f2 = (gradTop >> 16 & 0xFF) / 255.0F;
    float f3 = (gradTop >> 8 & 0xFF) / 255.0F;
    float f4 = (gradTop & 0xFF) / 255.0F;
    float f5 = (gradBot >> 24 & 0xFF) / 255.0F;
    float f6 = (gradBot >> 16 & 0xFF) / 255.0F;
    float f7 = (gradBot >> 8 & 0xFF) / 255.0F;
    float f8 = (gradBot & 0xFF) / 255.0F;
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    GL11.glShadeModel(7425);
    GL11.glPushMatrix();
    GL11.glBegin(7);
    GL11.glColor4f(f2, f3, f4, f);
    GL11.glVertex2d(x1, y + 1);
    GL11.glVertex2d(x + 1, y + 1);
    GL11.glColor4f(f6, f7, f8, f5);
    GL11.glVertex2d(x + 1, y1);
    GL11.glVertex2d(x1, y1);
    GL11.glEnd();
    GL11.glPopMatrix();
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glDisable(2848);
    GL11.glShadeModel(7424);
    drawVLine(x, y, y1 - 1, bord);
    drawVLine(x1 - 1, y, y1 - 1, bord);
    drawHLine(x, x1 - 1, y, bord);
    drawHLine(x, x1 - 1, y1 - 1, bord);
    GL11.glScaled(2.0D, 2.0D, 2.0D);
  }
  
  public static void draw2DCorner(EntityLivingBase entity, double posX, double posY, double posZ, int color)
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
    GuiUtils.drawRect(4.0F, -20.0F, 7.0F, -19.0F, color);
    GuiUtils.drawRect(-7.0F, -20.0F, -4.0F, -19.0F, color);
    GuiUtils.drawRect(6.0F, -20.0F, 7.0F, -17.5F, color);
    GuiUtils.drawRect(-7.0F, -20.0F, -6.0F, -17.5F, color);
    GuiUtils.drawRect(-7.0F, 2.0F, -4.0F, 3.0F, color);
    GuiUtils.drawRect(4.0F, 2.0F, 7.0F, 3.0F, color);
    GuiUtils.drawRect(-7.0F, 0.5F, -6.0F, 3.0F, color);
    GuiUtils.drawRect(6.0F, 0.5F, 7.0F, 3.0F, color);
    GuiUtils.drawRect(7.0F, -20.0F, 7.3F, -17.5F, -16777216);
    GuiUtils.drawRect(-7.3F, -20.0F, -7.0F, -17.5F, -16777216);
    GuiUtils.drawRect(4.0F, -20.3F, 7.3F, -20.0F, -16777216);
    GuiUtils.drawRect(-7.3F, -20.3F, -4.0F, -20.0F, -16777216);
    GuiUtils.drawRect(-7.0F, 3.0F, -4.0F, 3.3F, -16777216);
    GuiUtils.drawRect(4.0F, 3.0F, 7.0F, 3.3F, -16777216);
    GuiUtils.drawRect(-7.3F, 0.5F, -7.0F, 3.3F, -16777216);
    GuiUtils.drawRect(7.0F, 0.5F, 7.3F, 3.3F, -16777216);
    GL11.glDisable(3042);
    GL11.glEnable(2929);
    GL11.glEnable(2896);
    GlStateManager.popMatrix();
  }
  
  public static void drawRoundedRect(float x, float y, float x1, float y1, int insideC)
  {
    x *= 2.0F;
    y *= 2.0F;
    x1 *= 2.0F;
    y1 *= 2.0F;
    GL11.glPushMatrix();
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    drawVLine(x, y + 1.0F, y1 - 2.0F, insideC);
    drawVLine(x1 - 1.0F, y + 1.0F, y1 - 2.0F, insideC);
    drawHLine(x + 2.0F, x1 - 3.0F, y, insideC);
    drawHLine(x + 2.0F, x1 - 3.0F, y1 - 1.0F, insideC);
    drawHLine(x + 1.0F, x + 1.0F, y + 1.0F, insideC);
    drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, insideC);
    drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, insideC);
    drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, insideC);
    GL11.glPushMatrix();
    drawRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, insideC);
    GL11.glScalef(2.0F, 2.0F, 2.0F);
    GL11.glPopMatrix();
    GL11.glPopMatrix();
  }
  
  public static void drawRoundedBorderedRect(float x, float y, float x1, float y1, int borderC, int insideC)
  {
    RenderingUtils.enableGL2D();
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    RenderingUtils.drawVLine(x *= 2.0F, y *= 2.0F + 1.0F, y1 *= 2.0F - 2.0F, borderC);
    RenderingUtils.drawVLine(x1 *= 2.0F - 1.0F, y + 1.0F, y1 - 2.0F, borderC);
    RenderingUtils.drawHLine(x + 2.0F, x1 - 3.0F, y, borderC);
    RenderingUtils.drawHLine(x + 2.0F, x1 - 3.0F, y1 - 1.0F, borderC);
    RenderingUtils.drawHLine(x + 1.0F, x + 1.0F, y + 1.0F, borderC);
    RenderingUtils.drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, borderC);
    RenderingUtils.drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, borderC);
    RenderingUtils.drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, borderC);
    RenderingUtils.drawRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, insideC);
    GL11.glScalef(2.0F, 2.0F, 2.0F);
    RenderingUtils.disableGL2D();
  }
  
  public static void drawHLine(float par1, float par2, float par3, int par4)
  {
    if (par2 < par1)
    {
      float var5 = par1;
      par1 = par2;
      par2 = var5;
    }
    drawRect(par1, par3, par2 + 1.0F, par3 + 1.0F, par4);
  }
  
  public static void drawVLine(float par1, float par2, float par3, int par4)
  {
    if (par3 < par2)
    {
      float var5 = par2;
      par2 = par3;
      par3 = var5;
    }
    drawRect(par1, par2 + 1.0F, par1 + 1.0F, par3, par4);
  }
  
  public static void drawGradientBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2, int col3)
  {
    float f = (col1 >> 24 & 0xFF) / 255.0F;
    float f2 = (col1 >> 16 & 0xFF) / 255.0F;
    float f3 = (col1 >> 8 & 0xFF) / 255.0F;
    float f4 = (col1 & 0xFF) / 255.0F;
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    GL11.glDisable(3042);
    GL11.glPushMatrix();
    GL11.glColor4f(f2, f3, f4, f);
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
    GL11.glEnable(3042);
    GL11.glEnable(3553);
    GL11.glDisable(2848);
  }
  
  public static void drawGradientRect(double x, double y, double x2, double y2, int col1, int col2)
  {
    float f = (col1 >> 24 & 0xFF) / 255.0F;
    float f2 = (col1 >> 16 & 0xFF) / 255.0F;
    float f3 = (col1 >> 8 & 0xFF) / 255.0F;
    float f4 = (col1 & 0xFF) / 255.0F;
    float f5 = (col2 >> 24 & 0xFF) / 255.0F;
    float f6 = (col2 >> 16 & 0xFF) / 255.0F;
    float f7 = (col2 >> 8 & 0xFF) / 255.0F;
    float f8 = (col2 & 0xFF) / 255.0F;
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    GL11.glShadeModel(7425);
    GL11.glPushMatrix();
    GL11.glBegin(7);
    GL11.glColor4f(f2, f3, f4, f);
    GL11.glVertex2d(x2, y);
    GL11.glVertex2d(x, y);
    GL11.glColor4f(f6, f7, f8, f5);
    GL11.glVertex2d(x, y2);
    GL11.glVertex2d(x2, y2);
    GL11.glEnd();
    GL11.glPopMatrix();
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glDisable(2848);
    GL11.glShadeModel(7424);
  }
  
  public static void drawSidewaysGradientRect(double x, double y, double x2, double y2, int col1, int col2)
  {
    float f = (col1 >> 24 & 0xFF) / 255.0F;
    float f2 = (col1 >> 16 & 0xFF) / 255.0F;
    float f3 = (col1 >> 8 & 0xFF) / 255.0F;
    float f4 = (col1 & 0xFF) / 255.0F;
    float f5 = (col2 >> 24 & 0xFF) / 255.0F;
    float f6 = (col2 >> 16 & 0xFF) / 255.0F;
    float f7 = (col2 >> 8 & 0xFF) / 255.0F;
    float f8 = (col2 & 0xFF) / 255.0F;
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    GL11.glShadeModel(7425);
    GL11.glPushMatrix();
    GL11.glBegin(7);
    GL11.glColor4f(f2, f3, f4, f);
    GL11.glVertex2d(x, y);
    GL11.glVertex2d(x, y2);
    GL11.glColor4f(f6, f7, f8, f5);
    GL11.glVertex2d(x2, y2);
    GL11.glVertex2d(x2, y);
    GL11.glEnd();
    GL11.glPopMatrix();
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glDisable(2848);
    GL11.glShadeModel(7424);
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
  
  public static void drawFullCircle(int cx, int cy, double radius, int color)
  {
    radius *= 2.0D;
    cx *= 2;
    cy *= 2;
    float f = (color >> 24 & 0xFF) / 255.0F;
    float f2 = (color >> 16 & 0xFF) / 255.0F;
    float f3 = (color >> 8 & 0xFF) / 255.0F;
    float f4 = (color & 0xFF) / 255.0F;
    enableGL2D();
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    GL11.glColor4f(f2, f3, f4, f);
    GL11.glBegin(6);
    for (int i = 0; i <= 360; i++)
    {
      double x = Math.sin(i * 3.141592653589793D / 180.0D) * radius;
      double y = Math.cos(i * 3.141592653589793D / 180.0D) * radius;
      GL11.glVertex2d(cx + x, cy + y);
    }
    GL11.glEnd();
    GL11.glScalef(2.0F, 2.0F, 2.0F);
    disableGL2D();
  }
  
  public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineWidth)
  {
    GL11.glPushMatrix();
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    GL11.glDisable(3553);
    GL11.glEnable(2848);
    GL11.glDisable(2929);
    GL11.glDepthMask(false);
    GL11.glLineWidth(lineWidth);
    GL11.glColor4f(red, green, blue, alpha);
    drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
    GL11.glDisable(2848);
    GL11.glEnable(3553);
    GL11.glEnable(2929);
    GL11.glDepthMask(true);
    GL11.glDisable(3042);
    GL11.glPopMatrix();
  }
  
  public static void drawBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWidth)
  {
    GL11.glPushMatrix();
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    GL11.glDisable(3553);
    GL11.glEnable(2848);
    GL11.glDisable(2929);
    GL11.glDepthMask(false);
    GL11.glColor4f(red, green, blue, alpha);
    drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
    GL11.glLineWidth(lineWidth);
    GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
    drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
    GL11.glDisable(2848);
    GL11.glEnable(3553);
    GL11.glEnable(2929);
    GL11.glDepthMask(true);
    GL11.glDisable(3042);
    GL11.glPopMatrix();
  }
  
  public static double getAlphaFromHex(int color)
  {
    return (color >> 24 & 0xFF) / 255.0F;
  }
  
  public static double getRedFromHex(int color)
  {
    return (color >> 16 & 0xFF) / 255.0F;
  }
  
  public static double getGreenFromHex(int color)
  {
    return (color >> 8 & 0xFF) / 255.0F;
  }
  
  public static double getBlueFromHex(int color)
  {
    return (color & 0xFF) / 255.0F;
  }
  
  public static void drawCircle(double x, double y, double radius, int c)
  {
    float f = (c >> 24 & 0xFF) / 255.0F;
    float f2 = (c >> 16 & 0xFF) / 255.0F;
    float f3 = (c >> 8 & 0xFF) / 255.0F;
    float f4 = (c & 0xFF) / 255.0F;
    GlStateManager.alphaFunc(516, 0.001F);
    GlStateManager.color(f2, f3, f4, f);
    GlStateManager.enableAlpha();
    GlStateManager.enableBlend();
    GlStateManager.disableTexture2D();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    Tessellator tes = Tessellator.getInstance();
    for (double i = 0.0D; i < 360.0D; i += 1.0D)
    {
      double f1 = Math.sin(i * 3.141592653589793D / 180.0D) * radius;
      double f21 = Math.cos(i * 3.141592653589793D / 180.0D) * radius;
      GL11.glVertex2d(f3 + x, f4 + y);
    }
    GlStateManager.disableBlend();
    GlStateManager.disableAlpha();
    GlStateManager.enableTexture2D();
    GlStateManager.alphaFunc(516, 0.1F);
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
}

