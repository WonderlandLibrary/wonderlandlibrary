package space.lunaclient.luna.util;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import space.lunaclient.luna.api.element.Element;

public class RenderUtils
{
  public RenderUtils() {}
  
  public static void drawOutlinedBoundingBox(AxisAlignedBB aa)
  {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldRenderer = tessellator.getWorldRenderer();
    worldRenderer.startDrawing(3);
    worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
    worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
    tessellator.draw();
    worldRenderer.startDrawing(3);
    worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
    worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
    tessellator.draw();
    worldRenderer.startDrawing(1);
    worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
    worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
    worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
    tessellator.draw();
  }
  
  public static void drawBox(Box box)
  {
    if (box == null) {
      return;
    }
    GL11.glBegin(7);
    GL11.glVertex3d(box.minX, box.minY, box.maxZ);
    GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
    GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
    GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
    GL11.glEnd();
    GL11.glBegin(7);
    GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
    GL11.glVertex3d(box.minX, box.minY, box.maxZ);
    GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
    GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
    GL11.glEnd();
    
    GL11.glBegin(7);
    GL11.glVertex3d(box.minX, box.minY, box.minZ);
    GL11.glVertex3d(box.minX, box.minY, box.maxZ);
    GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
    GL11.glVertex3d(box.minX, box.maxY, box.minZ);
    GL11.glEnd();
    GL11.glBegin(7);
    GL11.glVertex3d(box.minX, box.minY, box.maxZ);
    GL11.glVertex3d(box.minX, box.minY, box.minZ);
    GL11.glVertex3d(box.minX, box.maxY, box.minZ);
    GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
    GL11.glEnd();
    
    GL11.glBegin(7);
    GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
    GL11.glVertex3d(box.maxX, box.minY, box.minZ);
    GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
    GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
    GL11.glEnd();
    GL11.glBegin(7);
    GL11.glVertex3d(box.maxX, box.minY, box.minZ);
    GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
    GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
    GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
    GL11.glEnd();
    
    GL11.glBegin(7);
    GL11.glVertex3d(box.minX, box.minY, box.minZ);
    GL11.glVertex3d(box.maxX, box.minY, box.minZ);
    GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
    GL11.glVertex3d(box.minX, box.maxY, box.minZ);
    GL11.glEnd();
    GL11.glBegin(7);
    GL11.glVertex3d(box.maxX, box.minY, box.minZ);
    GL11.glVertex3d(box.minX, box.minY, box.minZ);
    GL11.glVertex3d(box.minX, box.maxY, box.minZ);
    GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
    GL11.glEnd();
    
    GL11.glBegin(7);
    GL11.glVertex3d(box.minX, box.maxY, box.minZ);
    GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
    GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
    GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
    GL11.glEnd();
    
    GL11.glBegin(7);
    GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
    GL11.glVertex3d(box.minX, box.maxY, box.minZ);
    GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
    GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
    GL11.glEnd();
    
    GL11.glBegin(7);
    GL11.glVertex3d(box.minX, box.minY, box.minZ);
    GL11.glVertex3d(box.maxX, box.minY, box.minZ);
    GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
    GL11.glVertex3d(box.minX, box.minY, box.maxZ);
    GL11.glEnd();
    
    GL11.glBegin(7);
    GL11.glVertex3d(box.maxX, box.minY, box.minZ);
    GL11.glVertex3d(box.minX, box.minY, box.minZ);
    GL11.glVertex3d(box.minX, box.minY, box.maxZ);
    GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
    GL11.glEnd();
  }
  
  public static void drawBorderedRectNameTag(float x, float y, float x2, float y2, float l1, int col1, int col2)
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
  
  public static ScaledResolution getScaledRes()
  {
    return new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
  }
  
  public static void drawRect(float g, float h, float i, float j, int col1)
  {
    float f = (col1 >> 24 & 0xFF) / 255.0F;
    float f1 = (col1 >> 16 & 0xFF) / 255.0F;
    float f2 = (col1 >> 8 & 0xFF) / 255.0F;
    float f3 = (col1 & 0xFF) / 255.0F;
    
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    
    GL11.glPushMatrix();
    GL11.glColor4f(f1, f2, f3, f);
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
  
  public static void enableGL3D(float lineWidth)
  {
    GL11.glDisable(3008);
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    GL11.glDisable(3553);
    GL11.glDisable(2929);
    GL11.glDepthMask(false);
    GL11.glEnable(2884);
    GL11.glEnable(2848);
    GL11.glHint(3154, 4354);
    GL11.glHint(3155, 4354);
    GL11.glLineWidth(lineWidth);
  }
  
  public static void disableGL3D()
  {
    GL11.glEnable(3553);
    GL11.glEnable(2929);
    GL11.glDisable(3042);
    GL11.glEnable(3008);
    GL11.glDepthMask(true);
    GL11.glCullFace(1029);
    GL11.glDisable(2848);
    GL11.glHint(3154, 4352);
    GL11.glHint(3155, 4352);
  }
  
  public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int internalColor, int borderColor)
  {
    enableGL2D();
    glColor(internalColor);
    drawRectZZ(x + width, y + width, x1 - width, y1 - width);
    glColor(borderColor);
    drawRectZZ(x + width, y, x1 - width, y + width);
    drawRectZZ(x, y, x + width, y1);
    drawRectZZ(x1 - width, y, x1, y1);
    drawRectZZ(x + width, y1 - width, x1 - width, y1);
    disableGL2D();
  }
  
  private static void enableGL2D()
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
  
  private static void disableGL2D()
  {
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glEnable(2929);
    GL11.glDisable(2848);
    GL11.glHint(3154, 4352);
    GL11.glHint(3155, 4352);
  }
  
  public static void glColor(int hex)
  {
    float alpha = (hex >> 24 & 0xFF) / 255.0F;
    float red = (hex >> 16 & 0xFF) / 255.0F;
    float green = (hex >> 8 & 0xFF) / 255.0F;
    float blue = (hex & 0xFF) / 255.0F;
    GL11.glColor4f(red, green, blue, alpha);
  }
  
  public static void drawBorderRect(int left, int top, int right, int bottom, int bcolor, int icolor, int bwidth)
  {
    drawRect(left + bwidth, top + bwidth, right - bwidth, bottom - bwidth, icolor);
    drawRect(left, top + 1, left + bwidth, bottom - 1, bcolor);
    drawRect(left + bwidth, top, right, top + bwidth, bcolor);
    drawRect(left + bwidth, bottom - bwidth, right, bottom, bcolor);
    drawRect(right - bwidth, top + bwidth, right, bottom - bwidth, bcolor);
  }
  
  private static void drawRectZZ(float x, float y, float x1, float y1)
  {
    GL11.glBegin(7);
    GL11.glVertex2f(x, y1);
    GL11.glVertex2f(x1, y1);
    GL11.glVertex2f(x1, y);
    GL11.glVertex2f(x, y);
    GL11.glEnd();
  }
  
  public void drawRect(double x, double y, double x1, double y1, int color)
  {
    Gui.drawRect(x, y, x1, y1, color);
  }
  
  public static void drawRect(int x, int y, int x1, int y1, int color)
  {
    GL11.glDisable(2929);
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glDepthMask(true);
    GL11.glEnable(2848);
    GL11.glHint(3154, 4354);
    GL11.glHint(3155, 4354);
    Gui.drawRect(x, y, x1, y1, color);
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glEnable(2929);
    GL11.glDisable(2848);
    GL11.glHint(3154, 4352);
    GL11.glHint(3155, 4352);
  }
  
  private static void drawBoundingBox(AxisAlignedBB aa)
  {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldRenderer = tessellator.getWorldRenderer();
    worldRenderer.startDrawingQuads();
    worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
    worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
    worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
    tessellator.draw();
    worldRenderer.startDrawingQuads();
    worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
    worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
    worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
    worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
    tessellator.draw();
    worldRenderer.startDrawingQuads();
    worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
    worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
    worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
    tessellator.draw();
    worldRenderer.startDrawingQuads();
    worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
    worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
    worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
    worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
    tessellator.draw();
    worldRenderer.startDrawingQuads();
    worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
    worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
    worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
    worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
    tessellator.draw();
    worldRenderer.startDrawingQuads();
    worldRenderer.addVertex(aa.minX, aa.maxY, aa.maxZ);
    worldRenderer.addVertex(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.addVertex(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.addVertex(aa.minX, aa.minY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.maxY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.minY, aa.minZ);
    worldRenderer.addVertex(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.addVertex(aa.maxX, aa.minY, aa.maxZ);
    tessellator.draw();
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
  
  public static void drawTracerLine(double x, double y, double z, Color color, float alpha, float lineWdith)
  {
    GL11.glPushMatrix();
    GL11.glEnable(3042);
    GL11.glEnable(2848);
    GL11.glDisable(2929);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glLineWidth(lineWdith);
    glColor(color);
    
    GL11.glLoadIdentity();
    boolean bobbing = Element.mc.gameSettings.viewBobbing;
    Element.mc.gameSettings.viewBobbing = false;
    Element.mc.entityRenderer.orientCamera(Element.mc.timer.renderPartialTicks);
    GL11.glBegin(3);
    GL11.glVertex3d(0.0D, Minecraft.thePlayer.getEyeHeight(), 0.0D);
    GL11.glVertex3d(x, y, z);
    GL11.glVertex3d(x, y + Minecraft.thePlayer.getEyeHeight(), z);
    GL11.glEnd();
    Element.mc.gameSettings.viewBobbing = bobbing;
    
    GL11.glEnable(3553);
    GL11.glEnable(2929);
    GL11.glDisable(2848);
    GL11.glDisable(3042);
    GL11.glPopMatrix();
  }
  
  private static void glColor(Color color)
  {
    GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
  }
  
  public static void drawCircle(double d, double e, double r, int c)
  {
    float f = (c >> 24 & 0xFF) / 255.0F;
    float f1 = (c >> 16 & 0xFF) / 255.0F;
    float f2 = (c >> 8 & 0xFF) / 255.0F;
    float f3 = (c & 0xFF) / 255.0F;
    GL11.glPushMatrix();
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glEnable(2848);
    GL11.glBlendFunc(770, 771);
    GL11.glColor4f(f1, f2, f3, f);
    GL11.glBegin(2);
    for (int i = 0; i <= 360; i++)
    {
      double x2 = Math.sin(i * 3.141592653589793D / 180.0D) * r;
      double y2 = Math.cos(i * 3.141592653589793D / 180.0D) * r;
      GL11.glVertex2d(d + x2, e + y2);
    }
    GL11.glEnd();
    GL11.glDisable(2848);
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glPopMatrix();
  }
  
  public static void drawFilledCircle(double x, double y, double r, int c)
  {
    float f = (c >> 24 & 0xFF) / 255.0F;
    float f1 = (c >> 16 & 0xFF) / 255.0F;
    float f2 = (c >> 8 & 0xFF) / 255.0F;
    float f3 = (c & 0xFF) / 255.0F;
    GL11.glPushMatrix();
    GL11.glLineWidth(1.0F);
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glEnable(2848);
    GL11.glBlendFunc(770, 771);
    GL11.glColor4f(f1, f2, f3, f);
    GL11.glBegin(6);
    for (int i = 0; i <= 360; i++)
    {
      double x2 = Math.sin(i * 3.141592653589793D / 180.0D) * r;
      double y2 = Math.cos(i * 3.141592653589793D / 180.0D) * r;
      GL11.glVertex2d(x + x2, y + y2);
    }
    GL11.glEnd();
    GL11.glDisable(2848);
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glPopMatrix();
  }
  
  public static int getRainbow(int speed, int offset)
  {
    float hue = (float)((System.currentTimeMillis() + offset) % speed);
    hue /= speed;
    return Color.getHSBColor(hue, 1.0F, 1.0F).getRGB();
  }
  
  public static void DrawArc(double cx, double cy, double r, double start_angle, double arc_angle, int num_segments, float lw, int c)
  {
    float f = (c >> 24 & 0xFF) / 255.0F;
    float f1 = (c >> 16 & 0xFF) / 255.0F;
    float f2 = (c >> 8 & 0xFF) / 255.0F;
    float f3 = (c & 0xFF) / 255.0F;
    GL11.glColor4f(f1, f2, f3, f);
    double theta = arc_angle / (num_segments - 1);
    
    double tangetial_factor = Math.tan(theta);
    double radial_factor = Math.cos(theta);
    
    double x = r * Math.cos(start_angle);
    double y = r * Math.sin(start_angle);
    
    GL11.glLineWidth(lw);
    GL11.glBegin(3);
    for (int ii = 0; ii < num_segments; ii++)
    {
      GL11.glVertex2d(x + cx, y + cy);
      
      double tx = -y;
      double ty = x;
      
      x += tx * tangetial_factor;
      y += ty * tangetial_factor;
      
      x *= radial_factor;
      y *= radial_factor;
    }
    GL11.glEnd();
  }
  
  public static void drawBlockESP2(double x, double y, double z, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWidth)
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
  
  public static void drawBorderedCircle(int i, int y, float size, int j, int k) {}
  
  public static class ColorUtils
  {
    public ColorUtils() {}
    
    public Color getRainbow(long offset, float fade)
    {
      float hue = (float)(System.nanoTime() + offset) / 5.0E9F % 1.0F;
      long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0F, 1.0F)), 16);
      Color c = new Color((int)color);
      return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade, c.getAlpha() / 255.0F);
    }
    
    public int darker(int hex)
    {
      Color meme = Color.getColor(null, hex);
      return meme.darker().getRGB();
    }
    
    public static int darker(Color color, double fraction)
    {
      int red = (int)Math.round(color.getRed() * (1.0D - fraction));
      int green = (int)Math.round(color.getGreen() * (1.0D - fraction));
      int blue = (int)Math.round(color.getBlue() * (1.0D - fraction));
      if (red < 0) {
        red = 0;
      } else if (red > 255) {
        red = 255;
      }
      if (green < 0) {
        green = 0;
      } else if (green > 255) {
        green = 255;
      }
      if (blue < 0) {
        blue = 0;
      } else if (blue > 255) {
        blue = 255;
      }
      int alpha = color.getAlpha();
      return new Color(red, green, blue, alpha).getRGB();
    }
    
    public static Color blend(Color color1, Color color2, double ratio)
    {
      float r = (float)ratio;
      float ir = 1.0F - r;
      float[] rgb1 = new float[3];
      float[] rgb2 = new float[3];
      color1.getColorComponents(rgb1);
      color2.getColorComponents(rgb2);
      Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
      return color3;
    }
    
    public static String hexFromInt(int color)
    {
      return hexFromInt(new Color(color));
    }
    
    static String hexFromInt(Color color)
    {
      return Integer.toHexString(color.getRGB()).substring(2);
    }
  }
  
  public static class Camera
  {
    private final Minecraft mc;
    private Timer timer;
    private double posX;
    private double posY;
    private double posZ;
    private float rotationYaw;
    private float rotationPitch;
    
    public Camera(Entity entity)
    {
      this.mc = Minecraft.getMinecraft();
      if (this.timer == null) {
        this.timer = this.mc.timer;
      }
      this.posX = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * this.timer.renderPartialTicks);
      this.posY = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * this.timer.renderPartialTicks);
      this.posZ = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * this.timer.renderPartialTicks);
      setRotationYaw(entity.rotationYaw);
      setRotationPitch(entity.rotationPitch);
      if (((entity instanceof EntityPlayer)) && (Minecraft.getMinecraft().gameSettings.viewBobbing) && (entity == Minecraft.thePlayer))
      {
        EntityPlayer living1 = (EntityPlayer)entity;
        setRotationYaw(getRotationYaw() + living1.prevCameraYaw + (living1.cameraYaw - living1.prevCameraYaw) * this.timer.renderPartialTicks);
        setRotationPitch(getRotationPitch() + living1.prevCameraPitch + (living1.cameraPitch - living1.prevCameraPitch) * this.timer.renderPartialTicks);
      }
      else if ((entity instanceof EntityLivingBase))
      {
        EntityLivingBase living2 = (EntityLivingBase)entity;
        setRotationYaw(living2.rotationYawHead);
      }
    }
    
    public Camera(Entity entity, double offsetX, double offsetY, double offsetZ, double offsetRotationYaw, double offsetRotationPitch)
    {
      this.mc = Minecraft.getMinecraft();
      this.posX = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * this.timer.renderPartialTicks);
      this.posY = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * this.timer.renderPartialTicks);
      this.posZ = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * this.timer.renderPartialTicks);
      setRotationYaw(entity.rotationYaw);
      setRotationPitch(entity.rotationPitch);
      if (((entity instanceof EntityPlayer)) && (Minecraft.getMinecraft().gameSettings.viewBobbing) && (entity == Minecraft.thePlayer))
      {
        EntityPlayer player = (EntityPlayer)entity;
        setRotationYaw(getRotationYaw() + player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * this.timer.renderPartialTicks);
        setRotationPitch(getRotationPitch() + player.prevCameraPitch + (player.cameraPitch - player.prevCameraPitch) * this.timer.renderPartialTicks);
      }
      this.posX += offsetX;
      this.posY += offsetY;
      this.posZ += offsetZ;
      this.rotationYaw += (float)offsetRotationYaw;
      this.rotationPitch += (float)offsetRotationPitch;
    }
    
    public Camera(double posX, double posY, double posZ, float rotationYaw, float rotationPitch)
    {
      this.mc = Minecraft.getMinecraft();
      setPosX(posX);
      this.posY = posY;
      this.posZ = posZ;
      setRotationYaw(rotationYaw);
      setRotationPitch(rotationPitch);
    }
    
    public double getPosX()
    {
      return this.posX;
    }
    
    public void setPosX(double posX)
    {
      this.posX = posX;
    }
    
    public double getPosY()
    {
      return this.posY;
    }
    
    public void setPosY(double posY)
    {
      this.posY = posY;
    }
    
    public double getPosZ()
    {
      return this.posZ;
    }
    
    public void setPosZ(double posZ)
    {
      this.posZ = posZ;
    }
    
    public float getRotationYaw()
    {
      return this.rotationYaw;
    }
    
    public void setRotationYaw(float rotationYaw)
    {
      this.rotationYaw = rotationYaw;
    }
    
    public float getRotationPitch()
    {
      return this.rotationPitch;
    }
    
    public void setRotationPitch(float rotationPitch)
    {
      this.rotationPitch = rotationPitch;
    }
  }
  
  public static class R3DUtils
  {
    public R3DUtils() {}
    
    public static void startDrawing()
    {
      GL11.glEnable(3042);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      Element.mc.entityRenderer.setupCameraTransform(Element.mc.timer.renderPartialTicks, 0);
    }
    
    public static void drawTracerLine(Entity entity, Color color)
    {
      Minecraft.getMinecraft().getRenderManager();
      double x = entity.posX - RenderManager.renderPosX;
      Minecraft.getMinecraft().getRenderManager();
      double y = entity.posY + entity.height / 2.0F - RenderManager.renderPosY;
      Minecraft.getMinecraft().getRenderManager();
      double z = entity.posZ - RenderManager.renderPosZ;
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3042);
      GL11.glLineWidth(2.0F);
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      glColor(color);
      Vec3 eyes = new Vec3(0.0D, 0.0D, 1.0D).rotatePitch(-(float)Math.toRadians(Minecraft.thePlayer.rotationPitch)).rotateYaw(-(float)Math.toRadians(Minecraft.thePlayer.rotationYaw));
      GL11.glBegin(1);
      GL11.glVertex3d(eyes.xCoord, Minecraft.thePlayer.getEyeHeight() + eyes.yCoord, eyes.zCoord);
      GL11.glVertex3d(x, y, z);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
    }
    
    static void glColor(Color color)
    {
      GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
    }
  }
  
  public static class R2DUtils
  {
    public R2DUtils() {}
    
    public static void drawRect(float x, float y, float x1, float y1, int color)
    {
      enableGL2D();
      glColor(color);
      drawRect(x, y, x1, y1);
      disableGL2D();
    }
    
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
    
    public static void glColor(int hex)
    {
      float alpha = (hex >> 24 & 0xFF) / 255.0F;
      float red = (hex >> 16 & 0xFF) / 255.0F;
      float green = (hex >> 8 & 0xFF) / 255.0F;
      float blue = (hex & 0xFF) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha);
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
    
    public static void rectangle(double left, double top, double right, double bottom, Color color)
    {
      if (left < right)
      {
        double var5 = left;
        left = right;
        right = var5;
      }
      if (top < bottom)
      {
        double var5 = top;
        top = bottom;
        bottom = var5;
      }
      float var11 = (color.getRGB() >> 24 & 0xFF) / 255.0F;
      float var6 = (color.getRGB() >> 16 & 0xFF) / 255.0F;
      float var7 = (color.getRGB() >> 8 & 0xFF) / 255.0F;
      float var8 = (color.getRGB() & 0xFF) / 255.0F;
      WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(var6, var7, var8, var11);
      worldRenderer.startDrawingQuads();
      worldRenderer.addVertex(left, bottom, 0.0D);
      worldRenderer.addVertex(right, bottom, 0.0D);
      worldRenderer.addVertex(right, top, 0.0D);
      worldRenderer.addVertex(left, top, 0.0D);
      Tessellator.getInstance().draw();
      GlStateManager.enableTexture();
      GlStateManager.disableBlend();
    }
  }
}
