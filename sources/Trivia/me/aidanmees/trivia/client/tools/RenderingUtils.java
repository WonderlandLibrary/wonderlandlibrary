package me.aidanmees.trivia.client.tools;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

public final class RenderingUtils
{
  public static final RenderItem RENDER_ITEM = new RenderItem(Minecraft.getMinecraft().renderEngine, Minecraft.getMinecraft().modelManager);
  private static ScaledResolution scaledResolution;
  private static int Y = 22;
  private static int pY = 0;
  private static float pY2 = 0.0F;
  private static int rectY = 32;
  
  public static void drawSearchBlock(Block block, BlockPos blockPos)
  {
    EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
    GlStateManager.pushMatrix();
    GL11.glLineWidth(1.0F);
    GlStateManager.disableDepth();
    disableLighting();
    double var8 = player.lastTickPosX + (player.posX - player.lastTickPosX) * Minecraft.getMinecraft().timer.renderPartialTicks;
    double var10 = player.lastTickPosY + (player.posY - player.lastTickPosY) *  Minecraft.getMinecraft().timer.renderPartialTicks;
    double var12 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) *  Minecraft.getMinecraft().timer.renderPartialTicks;
    Minecraft.getMinecraft();
    RenderGlobal.drawOutlinedBoundingBox(block.getSelectedBoundingBox(Minecraft.theWorld, blockPos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-var8, -var10, -var12), -1);
    GlStateManager.popMatrix();
  }
  
  public static void drawBoundingBox(AxisAlignedBB axisalignedbb)
  {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrender = Tessellator.getInstance().getWorldRenderer();
    worldrender.startDrawingQuads();
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
    worldrender.putPosition(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
    tessellator.draw();
  }
  
  public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineWidth, int hashcode)
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
  
  public static void drawOutlinedBoundingBox(AxisAlignedBB aa)
  {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldRenderer = tessellator.getWorldRenderer();
    worldRenderer.startDrawing(3);
    worldRenderer.putPosition(aa.minX, aa.minY, aa.minZ);
    worldRenderer.putPosition(aa.maxX, aa.minY, aa.minZ);
    worldRenderer.putPosition(aa.maxX, aa.minY, aa.maxZ);
    worldRenderer.putPosition(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.putPosition(aa.minX, aa.minY, aa.minZ);
    tessellator.draw();
    worldRenderer.startDrawing(3);
    worldRenderer.putPosition(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.putPosition(aa.maxX, aa.maxY, aa.minZ);
    worldRenderer.putPosition(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.putPosition(aa.minX, aa.maxY, aa.maxZ);
    worldRenderer.putPosition(aa.minX, aa.maxY, aa.minZ);
    tessellator.draw();
    worldRenderer.startDrawing(1);
    worldRenderer.putPosition(aa.minX, aa.minY, aa.minZ);
    worldRenderer.putPosition(aa.minX, aa.maxY, aa.minZ);
    worldRenderer.putPosition(aa.maxX, aa.minY, aa.minZ);
    worldRenderer.putPosition(aa.maxX, aa.maxY, aa.minZ);
    worldRenderer.putPosition(aa.maxX, aa.minY, aa.maxZ);
    worldRenderer.putPosition(aa.maxX, aa.maxY, aa.maxZ);
    worldRenderer.putPosition(aa.minX, aa.minY, aa.maxZ);
    worldRenderer.putPosition(aa.minX, aa.maxY, aa.maxZ);
    tessellator.draw();
  }
  
  public static void drawEsp(EntityLivingBase ent, float pTicks, int hexColor, int hexColorIn)
  {
    if (ent.isEntityAlive())
    {
      double x = getDiff(ent.lastTickPosX, ent.posX, pTicks, RenderManager.renderPosX);
      double y = getDiff(ent.lastTickPosY, ent.posY, pTicks, RenderManager.renderPosY);
      double z = getDiff(ent.lastTickPosZ, ent.posZ, pTicks, RenderManager.renderPosZ);
      boundingBox(ent, x, y, z, hexColor, hexColorIn);
    }
  }
  
  public static void boundingBox(Entity entity, double x, double y, double z, int color, int colorIn)
  {
    GlStateManager.pushMatrix();
    GL11.glLineWidth(1.0F);
    AxisAlignedBB var11 = entity.getEntityBoundingBox();
    AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - entity.posX + x, var11.minY - entity.posY + y, var11.minZ - entity.posZ + z, var11.maxX - entity.posX + x, var11.maxY - entity.posY + y, var11.maxZ - entity.posZ + z);
    if (color != 0)
    {
      GlStateManager.disableDepth();
      filledBox(var12, colorIn, true);
      disableLighting();
      RenderGlobal.drawOutlinedBoundingBox(var12, color);
    }
    GlStateManager.popMatrix();
  }
  
  public static void enableLighting()
  {
    GL11.glDisable(3042);
    GL11.glEnable(3553);
    GL11.glDisable(2848);
    GL11.glDisable(3042);
    OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GL11.glMatrixMode(5890);
    GL11.glLoadIdentity();
    float var3 = 0.0039063F;
    GL11.glScalef(var3, var3, var3);
    GL11.glTranslatef(8.0F, 8.0F, 8.0F);
    GL11.glMatrixMode(5888);
    GL11.glTexParameteri(3553, 10241, 9729);
    GL11.glTexParameteri(3553, 10240, 9729);
    GL11.glTexParameteri(3553, 10242, 10496);
    GL11.glTexParameteri(3553, 10243, 10496);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
  }
  
  public static void disableLighting()
  {
    OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GL11.glDisable(3553);
    OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(2848);
    GL11.glDisable(2896);
    GL11.glDisable(3553);
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
  
  public static void enableGL3D(float lineWidth)
  {
    GL11.glDisable(3008);
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    GL11.glDisable(3553);
    GL11.glDisable(2929);
    GL11.glDepthMask(false);
    GL11.glEnable(2884);
    Minecraft.getMinecraft().entityRenderer.disableLightmap();
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
  
  public static void drawBorderedRect(int x, int y, int x1, int y1, int insideC, int borderC)
  {
    enableGL2D();
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    drawVLine(x *= 2, y *= 2, y1 *= 2 - 1, borderC);
    drawVLine(x1 *= 2 - 1, y, y1, borderC);
    drawHLine(x, x1 - 1, y, borderC);
    drawHLine(x, x1 - 2, y1 - 1, borderC);
    drawRect(x + 1, y + 1, x1 - 1, y1 - 1, insideC);
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
  
  public static void drawBorderedRect(Rectangle rectangle, float width, int internalColor, int borderColor)
  {
    float x = rectangle.x;
    float y = rectangle.y;
    float x1 = rectangle.x + rectangle.width;
    float y1 = rectangle.y + rectangle.height;
    enableGL2D();
    glColor(internalColor);
    drawRect(x + width, y + width, x1 - width, y1 - width);
    glColor(borderColor);
    drawRect(x + 1.0F, y, x1 - 1.0F, y + width);
    drawRect(x, y, x + width, y1);
    drawRect(x1 - width, y, x1, y1);
    drawRect(x + 1.0F, y1 - width, x1 - 1.0F, y1);
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
    GL11.glPushMatrix();
    cx *= 2.0F;
    cy *= 2.0F;
    float f = (c >> 24 & 0xFF) / 255.0F;
    float f1 = (c >> 16 & 0xFF) / 255.0F;
    float f2 = (c >> 8 & 0xFF) / 255.0F;
    float f3 = (c & 0xFF) / 255.0F;
    float theta = (float)(6.2831852D / num_segments);
    float p = (float)Math.cos(theta);
    float s = (float)Math.sin(theta);
    float x = r *= 2.0F;
    float y = 0.0F;
    enableGL2D();
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    GL11.glColor4f(f1, f2, f3, f);
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
    GL11.glPopMatrix();
  }
  
  public static void drawFullCircle(int cx, int cy, double r, int c)
  {
    r *= 2.0D;
    cx *= 2;
    cy *= 2;
    float f = (c >> 24 & 0xFF) / 255.0F;
    float f1 = (c >> 16 & 0xFF) / 255.0F;
    float f2 = (c >> 8 & 0xFF) / 255.0F;
    float f3 = (c & 0xFF) / 255.0F;
    enableGL2D();
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    GL11.glColor4f(f1, f2, f3, f);
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
  
  public static void updateScaledResolution()
  {
    scaledResolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
  }
  
  public static ScaledResolution getScaledResolution()
  {
    return scaledResolution;
  }
  
  public static void prepareScissorBox(float x, float y, float x2, float y2)
  {
    updateScaledResolution();
    int factor = scaledResolution.getScaleFactor();
    GL11.glScissor((int)(x * factor), (int)((scaledResolution.getScaledHeight() - y2) * factor), (int)((x2 - x) * factor), (int)((y2 - y) * factor));
  }
  
  public static void drawOutlinedBox(AxisAlignedBB boundingBox)
  {
    if (boundingBox != null)
    {
      GL11.glBegin(3);
      GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glEnd();
      GL11.glBegin(3);
      GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glEnd();
      GL11.glBegin(1);
      GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.maxZ, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glVertex3d(boundingBox.minX, boundingBox.maxZ, boundingBox.maxZ);
      GL11.glEnd();
    }
  }
  
  public static void drawBox(RenderBox box)
  {
    GL11.glEnable(1537);
    if (box != null)
    {
      GL11.glBegin(7);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glEnd();
      GL11.glBegin(7);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glEnd();
      GL11.glBegin(7);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glEnd();
      GL11.glBegin(7);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glEnd();
      GL11.glBegin(7);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glEnd();
      GL11.glBegin(7);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glEnd();
      GL11.glBegin(7);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glEnd();
      GL11.glBegin(7);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glEnd();
      GL11.glBegin(7);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glEnd();
      GL11.glBegin(7);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glEnd();
      GL11.glBegin(7);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glEnd();
      GL11.glBegin(7);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.minX, box.maxZ, box.maxZ);
      GL11.glVertex3d(box.maxZ, box.maxZ, box.maxZ);
      GL11.glEnd();
    }
  }
  
  public static void filledBox(AxisAlignedBB aa, int color, boolean shouldColor)
  {
    GlStateManager.pushMatrix();
    float var11 = (color >> 24 & 0xFF) / 255.0F;
    float var6 = (color >> 16 & 0xFF) / 255.0F;
    float var7 = (color >> 8 & 0xFF) / 255.0F;
    float var8 = (color & 0xFF) / 255.0F;
    Tessellator var9 = Tessellator.getInstance();
    WorldRenderer t = var9.getWorldRenderer();
    if (shouldColor) {
      GlStateManager.color(var6, var7, var8, var11);
    }
    byte draw = 7;
    t.startDrawing(draw);
    t.putPosition(aa.minX, aa.minY, aa.minZ);
    t.putPosition(aa.minX, aa.maxY, aa.minZ);
    t.putPosition(aa.maxX, aa.minY, aa.minZ);
    t.putPosition(aa.maxX, aa.maxY, aa.minZ);
    t.putPosition(aa.maxX, aa.minY, aa.maxZ);
    t.putPosition(aa.maxX, aa.maxY, aa.maxZ);
    t.putPosition(aa.minX, aa.minY, aa.maxZ);
    t.putPosition(aa.minX, aa.maxY, aa.maxZ);
    var9.draw();
    t.startDrawing(draw);
    t.putPosition(aa.maxX, aa.maxY, aa.minZ);
    t.putPosition(aa.maxX, aa.minY, aa.minZ);
    t.putPosition(aa.minX, aa.maxY, aa.minZ);
    t.putPosition(aa.minX, aa.minY, aa.minZ);
    t.putPosition(aa.minX, aa.maxY, aa.maxZ);
    t.putPosition(aa.minX, aa.minY, aa.maxZ);
    t.putPosition(aa.maxX, aa.maxY, aa.maxZ);
    t.putPosition(aa.maxX, aa.minY, aa.maxZ);
    var9.draw();
    t.startDrawing(draw);
    t.putPosition(aa.minX, aa.maxY, aa.minZ);
    t.putPosition(aa.maxX, aa.maxY, aa.minZ);
    t.putPosition(aa.maxX, aa.maxY, aa.maxZ);
    t.putPosition(aa.minX, aa.maxY, aa.maxZ);
    t.putPosition(aa.minX, aa.maxY, aa.minZ);
    t.putPosition(aa.minX, aa.maxY, aa.maxZ);
    t.putPosition(aa.maxX, aa.maxY, aa.maxZ);
    t.putPosition(aa.maxX, aa.maxY, aa.minZ);
    var9.draw();
    t.startDrawing(draw);
    t.putPosition(aa.minX, aa.minY, aa.minZ);
    t.putPosition(aa.maxX, aa.minY, aa.minZ);
    t.putPosition(aa.maxX, aa.minY, aa.maxZ);
    t.putPosition(aa.minX, aa.minY, aa.maxZ);
    t.putPosition(aa.minX, aa.minY, aa.minZ);
    t.putPosition(aa.minX, aa.minY, aa.maxZ);
    t.putPosition(aa.maxX, aa.minY, aa.maxZ);
    t.putPosition(aa.maxX, aa.minY, aa.minZ);
    var9.draw();
    t.startDrawing(draw);
    t.putPosition(aa.minX, aa.minY, aa.minZ);
    t.putPosition(aa.minX, aa.maxY, aa.minZ);
    t.putPosition(aa.minX, aa.minY, aa.maxZ);
    t.putPosition(aa.minX, aa.maxY, aa.maxZ);
    t.putPosition(aa.maxX, aa.minY, aa.maxZ);
    t.putPosition(aa.maxX, aa.maxY, aa.maxZ);
    t.putPosition(aa.maxX, aa.minY, aa.minZ);
    t.putPosition(aa.maxX, aa.maxY, aa.minZ);
    var9.draw();
    t.startDrawing(draw);
    t.putPosition(aa.minX, aa.maxY, aa.maxZ);
    t.putPosition(aa.minX, aa.minY, aa.maxZ);
    t.putPosition(aa.minX, aa.maxY, aa.minZ);
    t.putPosition(aa.minX, aa.minY, aa.minZ);
    t.putPosition(aa.maxX, aa.maxY, aa.minZ);
    t.putPosition(aa.maxX, aa.minY, aa.minZ);
    t.putPosition(aa.maxX, aa.maxY, aa.maxZ);
    t.putPosition(aa.maxX, aa.minY, aa.maxZ);
    var9.draw();
    GlStateManager.depthMask(true);
    GlStateManager.popMatrix();
  }
  
  private static double getDiff(double lastI, double i, float ticks, double ownI)
  {
    return lastI + (i - lastI) * ticks - ownI;
  }
  
  public static void drawBeacon(BlockPos pos, int color, int colorIn, float partialTicks)
  {
    GlStateManager.pushMatrix();
    EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
    double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
    double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
    double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
    GL11.glLineWidth(1.0F);
    AxisAlignedBB var11 = new AxisAlignedBB(pos.getX() + 1, pos.getY(), pos.getZ() + 1, pos.getX(), pos.getY() + 200, pos.getZ());
    AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - x, var11.minY - y, var11.minZ - z, var11.maxX - x, var11.maxY - y, var11.maxZ - z);
    if (color != 0)
    {
      GlStateManager.disableDepth();
      filledBox(var12, colorIn, true);
      disableLighting();
      RenderGlobal.drawOutlinedBoundingBox(var12, color);
    }
    GlStateManager.popMatrix();
  }
  
  public static Color getRainbow(long offset, float fade)
  {
    float hue = (float)(System.nanoTime() + offset) / 5.0E9F % 1.0F;
    long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()), 16);
    Color c = new Color((int)color);
    return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade, c.getAlpha() / 255.0F);
  }
  
  public static void drawPotionStatus(ScaledResolution sr)
  {
    pY = 0;
    for (Iterator var2 = Minecraft.getMinecraft().thePlayer.getActivePotionEffects().iterator(); var2.hasNext(); pY -= 9)
    {
      PotionEffect effect = (PotionEffect)var2.next();
      Potion potion = Potion.potionTypes[effect.getPotionID()];
      String PType = I18n.format(potion.getName(), new Object[0]);
      if (effect.getAmplifier() == 1) {
        PType = PType + " II";
      } else if (effect.getAmplifier() == 2) {
        PType = PType + " III";
      } else if (effect.getAmplifier() == 3) {
        PType = PType + " IV";
      }
      if ((effect.getDuration() < 600) && (effect.getDuration() > 300)) {
        PType = PType + "§§7:§§6 " + Potion.getDurationString(effect);
      } else if (effect.getDuration() < 300) {
        PType = PType + "§§7:§§c " + Potion.getDurationString(effect);
      } else if (effect.getDuration() > 600) {
        PType = PType + "§§7:§§7 " + Potion.getDurationString(effect);
      }
      Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(PType, sr.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(PType), sr.getScaledHeight() - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + pY, potion.getLiquidColor());
    }
  }
  


  public static void drawStuffStatus(ScaledResolution scaledRes) {
      boolean currentItem = true;
      GL11.glPushMatrix();
      new net.minecraft.client.renderer.entity.RenderItem(Minecraft.getMinecraft().getTextureManager(), Minecraft.getMinecraft().modelManager);
      ArrayList<ItemStack> stuff = new ArrayList<ItemStack>();
      boolean onwater = Minecraft.getMinecraft().thePlayer.isEntityAlive() && Minecraft.getMinecraft().thePlayer.isInsideOfMaterial(Material.water);
      int split = -3;
      int errything2 = 3;
      while (errything2 >= 0) {
          ItemStack armer = Minecraft.getMinecraft().thePlayer.inventory.armorInventory[errything2];
          if (armer != null) {
              stuff.add(armer);
          }
          --errything2;
      }
      if (Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != null && currentItem) {
          stuff.add(Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem());
      }
      for (ItemStack var9 : stuff) {
          Minecraft.getMinecraft();
          if (Minecraft.theWorld != null) {
              RenderHelper.enableGUIStandardItemLighting();
              split += 16;
          }
          GlStateManager.pushMatrix();
          GlStateManager.disableAlpha();
          GlStateManager.clear(256);
          Minecraft.getMinecraft().getRenderItem().zLevel = -150.0F;
          Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(var9, split + scaledRes.getScaledWidth() / 2 - 4, scaledRes.getScaledHeight() - (onwater ? 65 : 55));
          RenderItem var10000 = Minecraft.getMinecraft().getRenderItem();
          Minecraft.getMinecraft();
          var10000.renderItemOverlays(Minecraft.fontRendererObj, var9, split + scaledRes.getScaledWidth() / 2 - 4, scaledRes.getScaledHeight() - (onwater ? 65 : 55));
          Minecraft.getMinecraft().getRenderItem().zLevel = 0.0F;
          GlStateManager.disableBlend();
          GlStateManager.scale(0.5D, 0.5D, 0.5D);
          GlStateManager.disableDepth();
          GlStateManager.disableLighting();
          renderEnchantText(var9, split + scaledRes.getScaledWidth() / 2 - 4, scaledRes.getScaledHeight() - (onwater ? 65 : 55));
          GlStateManager.enableDepth();
          GlStateManager.scale(2.0F, 2.0F, 2.0F);
          GlStateManager.enableAlpha();
          GlStateManager.popMatrix();
        }
        GL11.glPopMatrix();
      }

  
  public static void renderEnchantText(ItemStack stack, int x, int y)
  {
    int encY = y + 276;
    if ((stack.getItem() instanceof ItemArmor))
    {
      int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
      int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
      int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
      if (sLevel > 0)
      {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§§fp" + sLevel, x * 2, encY, 16777215);
        encY += 7;
      }
      if (kLevel > 0)
      {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§§ft" + kLevel, x * 2, encY, 16777215);
        encY += 7;
      }
      if (fLevel > 0)
      {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§§fu" + fLevel, x * 2, encY, 16777215);
        encY += 7;
      }
    }
    if ((stack.getItem() instanceof ItemBow))
    {
      int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
      int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
      int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
      int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
      if (sLevel > 0)
      {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§§fd" + sLevel, x * 2, encY, 16777215);
        encY += 7;
      }
      if (kLevel > 0)
      {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§§fk" + kLevel, x * 2, encY, 16777215);
        encY += 7;
      }
      if (fLevel > 0)
      {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§§ff" + fLevel, x * 2, encY, 16777215);
        encY += 7;
      }
      if (uLevel > 0)
      {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§§fu" + uLevel, x * 2, encY, 16777215);
        encY += 7;
      }
    }
    if ((stack.getItem() instanceof ItemSword))
    {
      int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
      int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
      int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
      int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
      if (sLevel > 0)
      {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§§fs" + sLevel, x * 2, encY, 16777215);
        encY += 7;
      }
      if (kLevel > 0)
      {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§§fk" + kLevel, x * 2, encY, 16777215);
        encY += 7;
      }
      if (fLevel > 0)
      {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§§ff" + fLevel, x * 2, encY, 16777215);
        encY += 7;
      }
      if (uLevel > 0) {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§§fu" + uLevel, x * 2, encY, 16777215);
      }
    }
  }
}
