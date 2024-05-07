package com.masterof13fps.utils.render;

import com.masterof13fps.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {

    private static final Vec3 field_82884_b = new Vec3(0.20000000298023224D, 1.0D, -0.699999988079071D).normalize();
    private static final Vec3 field_82885_c = new Vec3(-0.20000000298023224D, 1.0D, 0.699999988079071D).normalize();
    private static final String __OBFID = "CL_00000629";
    private static final ScaledResolution sr = new ScaledResolution(Minecraft.mc());
    public static int enemy = 0;
    public static int friend = 1;
    public static int other = 2;
    public static int target = 3;
    public static int team = 4;
    private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);

    public static void scissor(int x, int y, int x2, int y2) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.mc());
        double factor = scaledResolution.getScaleFactor();
        GL11.glScissor((int) (x * factor), (int) ((scaledResolution.height() - y2) * factor), (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
    }

    /**
     * Draws rect with rounded corners, how it's made:
     * https://vinii.de/github/LWJGLUtil/roundedRect.png
     */
    public static void drawRoundedRect(final float x, final float y, final float width, final float height,
                                       final float radius, final int color) {
        float x2 = x + ((radius / 2f) + 0.5f);
        float y2 = y + ((radius / 2f) + 0.5f);
        float calcWidth = (width - ((radius / 2f) + 0.5f));
        float calcHeight = (height - ((radius / 2f) + 0.5f));
        // top (pink)
        relativeRect(x2 + radius / 2f, y2 - radius / 2f - 0.5f, x2 + calcWidth - radius / 2f, y + calcHeight - radius / 2f,
                color);
        // bottom (yellow)
        relativeRect(x2 + radius / 2f, y2, x2 + calcWidth - radius / 2f, y2 + calcHeight + radius / 2f + 0.5f, color);
        // left (red)
        relativeRect((x2 - radius / 2f - 0.5f), y2 + radius / 2f, x2 + calcWidth, y2 + calcHeight - radius / 2f, color);
        // right (green)
        relativeRect(x2, y2 + radius / 2f + 0.5f, x2 + calcWidth + radius / 2f + 0.5f, y2 + calcHeight - radius / 2f,
                color);

        // left top circle
        polygonCircle(x, y - 0.15, radius * 2, 360, color);
        // right top circle
        polygonCircle(x + calcWidth - radius + 1.0, y - 0.15, radius * 2, 360, color);
        // left bottom circle
        polygonCircle(x, y + calcHeight - radius + 1, radius * 2, 360, color);
        // right bottom circle
        polygonCircle(x + calcWidth - radius + 1, y + calcHeight - radius + 1, radius * 2, 360, color);
    }

    /**
     * Draws a rect, as in {@link Gui}#drawRect.
     */
    public static void relativeRect(final float left, final float top, final float right, final float bottom,
                                    final int color) {

        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glColor(color);

        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, bottom, 0).endVertex();
        worldRenderer.pos(right, bottom, 0).endVertex();
        worldRenderer.pos(right, top, 0).endVertex();
        worldRenderer.pos(left, top, 0).endVertex();

        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    /**
     * Draws a polygon circle
     */
    public static final void polygonCircle(final double x, final double y, double sideLength, final double degree,
                                           final int color) {
        sideLength *= 0.5;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GlStateManager.disableAlpha();

        glColor(color);

        GL11.glLineWidth(1);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        // since its filled, otherwise GL_LINE_STRIP
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        for (double i = 0; i <= degree; i++) {
            final double angle = i * (Math.PI * 2) / degree;

            GL11.glVertex2d(x + (sideLength * Math.cos(angle)) + sideLength,
                    y + (sideLength * Math.sin(angle)) + sideLength);
        }

        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);

        GlStateManager.enableAlpha();

        //GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    /**
     * Draws a horizontal gradient rect
     */
    public static void drawHorizontalGradient(final float x, final float y, final float width, final float height,
                                              final int leftColor, final int rightColor) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);

        GL11.glPushMatrix();
        GL11.glBegin(7);

        glColor(leftColor);

        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y + height);

        glColor(rightColor);

        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);

        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    /**
     * Draws a vertical gradient rect
     */
    public static void drawVerticalGradient(final float x, final float y, final float width, final float height,
                                            final int topColor, final int bottomColor) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);

        GL11.glPushMatrix();
        GL11.glBegin(7);

        glColor(topColor);

        GL11.glVertex2f(x, y + height);
        GL11.glVertex2f(x + width, y + height);

        glColor(bottomColor);

        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x, y);

        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    public static void color(final int color) {
        final float f = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GL11.glColor4f(f2, f3, f4, f);
    }

    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = 0.003921569F * (float) c.getRed();
        float g = 0.003921569F * (float) c.getGreen();
        float b = 0.003921569F * (float) c.getBlue();
        return (new Color(r, g, b, alpha)).getRGB();
    }

    public static final ScaledResolution getScaledRes() {
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.mc());
        return scaledRes;
    }

    public static void enableStandardItemLighting() {
        GlStateManager.enableLighting();
        GlStateManager.enableLight(0);
        GlStateManager.enableLight(1);
        GlStateManager.enableColorMaterial();
        GlStateManager.colorMaterial(1032, 5634);
        float var0 = 0.4F;
        float var1 = 0.6F;
        float var2 = 0.0F;
        GL11.glLight(16384, 4611, setColorBuffer(field_82884_b.xCoord, field_82884_b.yCoord, field_82884_b.zCoord, 0.0D));
        GL11.glLight(16384, 4609, setColorBuffer(var1, var1, var1, 1.0D));
        GL11.glLight(16384, 4608, setColorBuffer(0.0D, 0.0D, 0.0D, 1.0D));
        GL11.glLight(16384, 4610, setColorBuffer(var2, var2, var2, 1.0D));
        GL11.glLight(16385, 4611, setColorBuffer(field_82885_c.xCoord, field_82885_c.yCoord, field_82885_c.zCoord, 0.0D));
        GL11.glLight(16385, 4609, setColorBuffer(var1, var1, var1, 1.0D));
        GL11.glLight(16385, 4608, setColorBuffer(0.0D, 0.0D, 0.0D, 1.0D));
        GL11.glLight(16385, 4610, setColorBuffer(var2, var2, var2, 1.0D));
        GlStateManager.shadeModel(7424);
        GL11.glLightModel(2899, setColorBuffer(var0, var0, var0, 1.0D));
    }

    private static FloatBuffer setColorBuffer(double p_74517_0_, double p_74517_2_, double p_74517_4_, double p_74517_6_) {
        return setColorBuffer((float) p_74517_0_, (float) p_74517_2_, (float) p_74517_4_, (float) p_74517_6_);
    }

    public static void drawTexturedRect(int x, int y, int width, int height, GuiIngame screen, ResourceLocation texture) {
        GlStateManager.pushMatrix();

        GlStateManager.enableBlend();

        Minecraft.mc().getTextureManager().bindTexture(texture);

        screen.drawTexturedModalRect(x, y, 0, 0, width, height);

        GlStateManager.disableBlend();

        GlStateManager.popMatrix();
    }

    public static void drawHollowRect(float posX, float posY, float posX2, float posY2, float width, int color, boolean center) {
        float corners = width / 2.0F;
        float side = width / 2.0F;
        if (center) {
            drawRect(posX - side, posY - corners, posX + side, posY2 + corners, color);
            drawRect(posX2 - side, posY - corners, posX2 + side, posY2 + corners, color);
            drawRect(posX - corners, posY - side, posX2 + corners, posY + side, color);
            drawRect(posX - corners, posY2 - side, posX2 + corners, posY2 + side, color);
        } else {
            drawRect(posX - width, posY - corners, posX, posY2 + corners, color);
            drawRect(posX2, posY - corners, posX2 + width, posY2 + corners, color);
            drawRect(posX - corners, posY - width, posX2 + corners, posY, color);
            drawRect(posX - corners, posY2, posX2 + corners, posY2 + width, color);
        }
    }

    public static void drawGradientBorderedRect(float posX, float posY, float posX2, float posY2, float width, int color, int startColor, int endColor, boolean center) {
        drawGradientRect(posX, posY, posX2, posY2, startColor, endColor);
        drawHollowRect(posX, posY, posX2, posY2, width, color, center);
    }

    public static void drawCoolLines(AxisAlignedBB mask) {
        GL11.glPushMatrix();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.minX, mask.minY,
                mask.minZ);
        GL11.glVertex3d(mask.minX, mask.maxY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.minY,
                mask.minZ);
        GL11.glVertex3d(mask.maxX, mask.maxY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.minY,
                mask.maxZ);
        GL11.glVertex3d(mask.minX, mask.maxY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.minY,
                mask.minZ);
        GL11.glVertex3d(mask.minX, mask.maxY,
                mask.minZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.minY,
                mask.minZ);
        GL11.glVertex3d(mask.minX, mask.minY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.maxY,
                mask.minZ);
        GL11.glVertex3d(mask.minX, mask.maxY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
        drawRect(x, y, x2, y2, col2);

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

    public static void drawBorderedCorneredRect(float x, float y, float x2, float y2, float lineWidth, int lineColor, int bgColor) {
        drawRect(x, y, x2, y2, bgColor);

        float f = (lineColor >> 24 & 0xFF) / 255.0F;
        float f1 = (lineColor >> 16 & 0xFF) / 255.0F;
        float f2 = (lineColor >> 8 & 0xFF) / 255.0F;
        float f3 = (lineColor & 0xFF) / 255.0F;

        GL11.glEnable(3042);
        GL11.glEnable(3553);
        drawRect(x - 1.0F, y, x2 + 1.0F, y - lineWidth, lineColor);
        drawRect(x, y, x - lineWidth, y2, lineColor);
        drawRect(x - 1.0F, y2, x2 + 1.0F, y2 + lineWidth, lineColor);
        drawRect(x2, y, x2 + lineWidth, y2, lineColor);
        GL11.glDisable(3553);
        GL11.glDisable(3042);
    }

    public static double interp(double from, double to, double pct) {
        return from + (to - from) * pct;
    }

  /*public static void drawFilledBox(AxisAlignedBB mask)
  {
    WorldRenderer worldRenderer = Tessellator.instance.getWorldRenderer();
    Tessellator tessellator = Tessellator.instance;
    worldRenderer.startDrawingQuads();
    worldRenderer.addVertex(mask.minX, mask.minY,
      mask.minZ);
    worldRenderer.addVertex(mask.minX, mask.maxY,
      mask.minZ);
    worldRenderer.addVertex(mask.maxX, mask.minY,
      mask.minZ);
    worldRenderer.addVertex(mask.maxX, mask.maxY,
      mask.minZ);
    worldRenderer.addVertex(mask.maxX, mask.minY,
      mask.maxZ);
    worldRenderer.addVertex(mask.maxX, mask.maxY,
      mask.maxZ);
    worldRenderer.addVertex(mask.minX, mask.minY,
      mask.maxZ);
    worldRenderer.addVertex(mask.minX, mask.maxY,
      mask.maxZ);
    tessellator.draw();
    worldRenderer.startDrawingQuads();
    worldRenderer.addVertex(mask.maxX, mask.maxY,
      mask.minZ);
    worldRenderer.addVertex(mask.maxX, mask.minY,
      mask.minZ);
    worldRenderer.addVertex(mask.minX, mask.maxY,
      mask.minZ);
    worldRenderer.addVertex(mask.minX, mask.minY,
      mask.minZ);
    worldRenderer.addVertex(mask.minX, mask.maxY,
      mask.maxZ);
    worldRenderer.addVertex(mask.minX, mask.minY,
      mask.maxZ);
    worldRenderer.addVertex(mask.maxX, mask.maxY,
      mask.maxZ);
    worldRenderer.addVertex(mask.maxX, mask.minY,
      mask.maxZ);
    tessellator.draw();
    worldRenderer.startDrawingQuads();
    worldRenderer.addVertex(mask.minX, mask.maxY,
      mask.minZ);
    worldRenderer.addVertex(mask.maxX, mask.maxY,
      mask.minZ);
    worldRenderer.addVertex(mask.maxX, mask.maxY,
      mask.maxZ);
    worldRenderer.addVertex(mask.minX, mask.maxY,
      mask.maxZ);
    worldRenderer.addVertex(mask.minX, mask.maxY,
      mask.minZ);
    worldRenderer.addVertex(mask.minX, mask.maxY,
      mask.maxZ);
    worldRenderer.addVertex(mask.maxX, mask.maxY,
      mask.maxZ);
    worldRenderer.addVertex(mask.maxX, mask.maxY,
      mask.minZ);
    tessellator.draw();
    worldRenderer.startDrawingQuads();
    worldRenderer.addVertex(mask.minX, mask.minY,
      mask.minZ);
    worldRenderer.addVertex(mask.maxX, mask.minY,
      mask.minZ);
    worldRenderer.addVertex(mask.maxX, mask.minY,
      mask.maxZ);
    worldRenderer.addVertex(mask.minX, mask.minY,
      mask.maxZ);
    worldRenderer.addVertex(mask.minX, mask.minY,
      mask.minZ);
    worldRenderer.addVertex(mask.minX, mask.minY,
      mask.maxZ);
    worldRenderer.addVertex(mask.maxX, mask.minY,
      mask.maxZ);
    worldRenderer.addVertex(mask.maxX, mask.minY,
      mask.minZ);
    tessellator.draw();
    worldRenderer.startDrawingQuads();
    worldRenderer.addVertex(mask.minX, mask.minY,
      mask.minZ);
    worldRenderer.addVertex(mask.minX, mask.maxY,
      mask.minZ);
    worldRenderer.addVertex(mask.minX, mask.minY,
      mask.maxZ);
    worldRenderer.addVertex(mask.minX, mask.maxY,
      mask.maxZ);
    worldRenderer.addVertex(mask.maxX, mask.minY,
      mask.maxZ);
    worldRenderer.addVertex(mask.maxX, mask.maxY,
      mask.maxZ);
    worldRenderer.addVertex(mask.maxX, mask.minY,
      mask.minZ);
    worldRenderer.addVertex(mask.maxX, mask.maxY,
      mask.minZ);
    tessellator.draw();
    worldRenderer.startDrawingQuads();
    worldRenderer.addVertex(mask.minX, mask.maxY,
      mask.maxZ);
    worldRenderer.addVertex(mask.minX, mask.minY,
      mask.maxZ);
    worldRenderer.addVertex(mask.minX, mask.maxY,
      mask.minZ);
    worldRenderer.addVertex(mask.minX, mask.minY,
      mask.minZ);
    worldRenderer.addVertex(mask.maxX, mask.maxY,
      mask.minZ);
    worldRenderer.addVertex(mask.maxX, mask.minY,
      mask.minZ);
    worldRenderer.addVertex(mask.maxX, mask.maxY,
      mask.maxZ);
    worldRenderer.addVertex(mask.maxX, mask.minY,
      mask.maxZ);
    tessellator.draw();
  }*/

    public static void glColor(Color color) {
        GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
    }

    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
        GL11.glEnable(1536);
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
        GL11.glDisable(1536);
    }

    public static void drawLines(AxisAlignedBB mask) {
        GL11.glPushMatrix();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.minX, mask.minY,
                mask.minZ);
        GL11.glVertex3d(mask.minX, mask.maxY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.minX, mask.maxY,
                mask.minZ);
        GL11.glVertex3d(mask.minX, mask.minY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.minY,
                mask.minZ);
        GL11.glVertex3d(mask.maxX, mask.maxY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.maxY,
                mask.minZ);
        GL11.glVertex3d(mask.maxX, mask.minY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.minY,
                mask.maxZ);
        GL11.glVertex3d(mask.minX, mask.maxY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.maxY,
                mask.maxZ);
        GL11.glVertex3d(mask.minX, mask.minY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.minY,
                mask.minZ);
        GL11.glVertex3d(mask.minX, mask.maxY,
                mask.minZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.maxY,
                mask.minZ);
        GL11.glVertex3d(mask.minX, mask.minY,
                mask.minZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.minX, mask.maxY,
                mask.minZ);
        GL11.glVertex3d(mask.maxX, mask.maxY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.maxY,
                mask.minZ);
        GL11.glVertex3d(mask.minX, mask.maxY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.minX, mask.minY,
                mask.minZ);
        GL11.glVertex3d(mask.maxX, mask.minY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.minY,
                mask.minZ);
        GL11.glVertex3d(mask.minX, mask.minY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

  /*public static void drawOutlinedBoundingBox(AxisAlignedBB mask)
  {
    WorldRenderer var2 = Tessellator.instance.getWorldRenderer();
    Tessellator var1 = Tessellator.instance;
    var2.startDrawing(3);
    var2.addVertex(mask.minX, mask.minY,
      mask.minZ);
    var2.addVertex(mask.maxX, mask.minY,
      mask.minZ);
    var2.addVertex(mask.maxX, mask.minY,
      mask.maxZ);
    var2.addVertex(mask.minX, mask.minY,
      mask.maxZ);
    var2.addVertex(mask.minX, mask.minY,
      mask.minZ);
    var1.draw();
    var2.startDrawing(3);
    var2.addVertex(mask.minX, mask.maxY,
      mask.minZ);
    var2.addVertex(mask.maxX, mask.maxY,
      mask.minZ);
    var2.addVertex(mask.maxX, mask.maxY,
      mask.maxZ);
    var2.addVertex(mask.minX, mask.maxY,
      mask.maxZ);
    var2.addVertex(mask.minX, mask.maxY,
      mask.minZ);
    var1.draw();
    var2.startDrawing(1);
    var2.addVertex(mask.minX, mask.minY,
      mask.minZ);
    var2.addVertex(mask.minX, mask.maxY,
      mask.minZ);
    var2.addVertex(mask.maxX, mask.minY,
      mask.minZ);
    var2.addVertex(mask.maxX, mask.maxY,
      mask.minZ);
    var2.addVertex(mask.maxX, mask.minY,
      mask.maxZ);
    var2.addVertex(mask.maxX, mask.maxY,
      mask.maxZ);
    var2.addVertex(mask.minX, mask.minY,
      mask.maxZ);
    var2.addVertex(mask.minX, mask.maxY,
      mask.maxZ);
    var1.draw();
  }*/

    public static void drawRect(float g, float h, float i, float j, int col1) {
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

    public static void drawRect(float g, float h, float i, float j, Color col2) {
        ColorUtil.setColor(col2);

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);

        GL11.glPushMatrix();
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

    public static void drawButtonBorderedRect(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, Color innerColor, Color outerColor, float lineWidhth) {
        drawButtonRect(x1, y1, x2, y2, x3, y3, x4, y4, innerColor);

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);

        GL11.glPushMatrix();
        ColorUtil.setColor(outerColor);
        GL11.glLineWidth(lineWidhth);
        GL11.glBegin(2);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x3, y3);
        GL11.glVertex2d(x4, y4);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
    }

    public static void drawButtonRect(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, Color col) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);

        GL11.glPushMatrix();
        ColorUtil.setColor(col);
        GL11.glBegin(7);
        GL11.glVertex2d(x4, y4);
        GL11.glVertex2d(x3, y3);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();

        GL11.glBegin(7);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x3, y3);
        GL11.glVertex2d(x4, y4);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void disableStandardItemLighting() {
        GlStateManager.disableLighting();
        GlStateManager.disableLight(0);
        GlStateManager.disableLight(1);
        GlStateManager.disableColorMaterial();
    }

    public static void drawOutlinedBox(AxisAlignedBB boundingBox, int color) {
        if (boundingBox != null) {
            GL11.glBegin(3);
            GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
            GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
            GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
            GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
            GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
            GL11.glEnd();
            GL11.glBegin(3);
            GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
            GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
            GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
            GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
            GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
            GL11.glEnd();
            GL11.glBegin(1);
            GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
            GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
            GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
            GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
            GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
            GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
            GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
            GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
            GL11.glEnd();
        }
    }

    public static void drawRect(double x, double y, double x2, double y2, int color) {
        float red = (color >> 24 & 0xFF) / 255.0F;
        float green = (color >> 16 & 0xFF) / 255.0F;
        float blue = (color >> 8 & 0xFF) / 255.0F;
        float alpha = (color & 0xFF) / 255.0F;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        GL11.glPushMatrix();
        glColor4f(green, blue, alpha, red);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);

        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public static void drawGradientRect(double left, double top, double right, double bottom, int startColor,
                                        int endColor) {
        float var7 = (float) (startColor >> 24 & 255) / 255.0F;
        float var8 = (float) (startColor >> 16 & 255) / 255.0F;
        float var9 = (float) (startColor >> 8 & 255) / 255.0F;
        float var10 = (float) (startColor & 255) / 255.0F;
        float var11 = (float) (endColor >> 24 & 255) / 255.0F;
        float var12 = (float) (endColor >> 16 & 255) / 255.0F;
        float var13 = (float) (endColor >> 8 & 255) / 255.0F;
        float var14 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator var15 = Tessellator.getInstance();
        WorldRenderer worldRenderer = var15.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);//
        worldRenderer.color(var8, var9, var10, var7);
        worldRenderer.pos(right, top, 0);
        worldRenderer.pos(left, top, 0);
        worldRenderer.color(var12, var13, var14, var11);
        worldRenderer.pos(left, bottom, 0);
        worldRenderer.pos(right, bottom, 0);
        var15.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    private static void circle(double d, double e, float radius, int fill) {
        arc(d, e, 0.0F, 360.0F, radius, fill);
    }

    private static void arc(double d, double e, float start, float end, float radius, int color) {
        arcEllipse(d, e, start, end, radius, radius, color);
    }

    private static void arcEllipse(double d, double e, float start, float end, float w, float h, int color) {
        GlStateManager.color(0.0F, 0.0F, 0.0F);
        glColor4f(0.0F, 0.0F, 0.0F, 0.0F);

        float temp = 0.0F;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }

        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = (color >> 24 & 0xFF) / 255.0F;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.color(red, green, blue, alpha);
        if (alpha > 0.5F) {
            glEnable(GL_LINE_SMOOTH);
            glLineWidth(2.0F);
            glBegin(3);
            for (float i = end; i >= start; i -= 4.0F) {
                float ldx = (float) Math.cos(i * 3.141592653589793D / 180.0D) * (w * 1.001F);
                float ldy = (float) Math.sin(i * 3.141592653589793D / 180.0D) * (h * 1.001F);
                glVertex2d(d + ldx, e + ldy);
            }
            glEnd();
            glDisable(GL_LINE_SMOOTH);
        }
        glBegin(GL_TRIANGLE_FAN);
        for (float i = end; i >= start; i -= 4.0F) {
            float ldx = (float) Math.cos(i * 3.141592653589793D / 180.0D) * w;
            float ldy = (float) Math.sin(i * 3.141592653589793D / 180.0D) * h;
            glVertex2d(d + ldx, e + ldy);
        }
        glEnd();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawImage(final String image, final int x, final int y, final int width, final int height) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Wrapper.mc.getTextureManager().bindTexture(new ResourceLocation(image));
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void drawBorderedRect(double left, double top, double right, double bottom, float borderWidth,
                                        int borderColor, int color) {
        float alpha = (borderColor >> 24 & 0xFF) / 255.0f;
        float red = (borderColor >> 16 & 0xFF) / 255.0f;
        float green = (borderColor >> 8 & 0xFF) / 255.0f;
        float blue = (borderColor & 0xFF) / 255.0f;
        GlStateManager.pushMatrix();
        drawRect(left, top, right, bottom, color);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);

        if (borderWidth == 1.0F) {
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
        }

        GL11.glLineWidth(borderWidth);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);//
        worldRenderer.pos(left, top, 0.0F);
        worldRenderer.pos(left, bottom, 0.0F);
        worldRenderer.pos(right, bottom, 0.0F);
        worldRenderer.pos(right, top, 0.0F);
        worldRenderer.pos(left, top, 0.0F);
        worldRenderer.pos(right, top, 0.0F);
        worldRenderer.pos(left, bottom, 0.0F);
        worldRenderer.pos(right, bottom, 0.0F);
        tessellator.draw();
        GL11.glLineWidth(2.0F);

        if (borderWidth == 1.0F) {
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void box(double x, double y, double z, double x2, double y2, double z2, Color color) {
        x = x - RenderManager.renderPosX;
        y = y - RenderManager.renderPosY;
        z = z - RenderManager.renderPosZ;
        x2 = x2 - RenderManager.renderPosX;
        y2 = y2 - RenderManager.renderPosY;
        z2 = z2 - RenderManager.renderPosZ;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL_BLEND);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        drawColorBox(new AxisAlignedBB(x, y, z, x2, y2, z2));
        GL11.glColor4d(0, 0, 0, 0.5F);
        RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x2, y2, z2));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL_BLEND);
    }

    /**
     * Renders a frame with any size and any color.
     *
     * @param x
     * @param y
     * @param z
     * @param x2
     * @param y2
     * @param z2
     * @param color
     */
    public static void frame(double x, double y, double z, double x2, double y2, double z2, Color color) {
        x = x - RenderManager.renderPosX;
        y = y - RenderManager.renderPosY;
        z = z - RenderManager.renderPosZ;
        x2 = x2 - RenderManager.renderPosX;
        y2 = y2 - RenderManager.renderPosY;
        z2 = z2 - RenderManager.renderPosZ;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL_BLEND);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x2, y2, z2));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL_BLEND);
    }

    public static void framelessBlockESP(BlockPos blockPos, Color color) {
        double x = blockPos.getX() - RenderManager.renderPosX;
        double y = blockPos.getY() - RenderManager.renderPosY;
        double z = blockPos.getZ() - RenderManager.renderPosZ;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL_BLEND);
        GL11.glLineWidth(2.0F);
        GL11.glColor4d(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255, 0.15);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL_BLEND);
    }

    public static void drawColorBox(AxisAlignedBB axisalignedbb) {
        Tessellator ts = Tessellator.getInstance();
        WorldRenderer wr = ts.getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);// Starts X.
        wr.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        ts.draw();
        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);//
        wr.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        ts.draw();// Ends X.
        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);//// Starts Y.
        wr.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        ts.draw();
        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);//
        wr.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        ts.draw();// Ends Y.
        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);//// Starts Z.
        wr.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        ts.draw();
        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);//
        wr.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        wr.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        ts.draw();// Ends Z.
    }

    public static void tracerLine(Entity entity, int mode) {
        double x = entity.posX - RenderManager.renderPosX;
        double y = entity.posY + entity.height / 2 - RenderManager.renderPosY;
        double z = entity.posZ - RenderManager.renderPosZ;
        glBlendFunc(770, 771);
        glEnable(GL_BLEND);
        glLineWidth(2.0F);
        glDisable(GL11.GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        if (mode == 0)// Enemy
            GL11.glColor4d(1 - Minecraft.mc().thePlayer.getDistanceToEntity(entity) / 40,
                    Minecraft.mc().thePlayer.getDistanceToEntity(entity) / 40, 0, 0.5F);
        else if (mode == 1)// Friend
            GL11.glColor4d(0, 0, 1, 0.5F);
        else if (mode == 2)// Other
            GL11.glColor4d(1, 1, 0, 0.5F);
        else if (mode == 3)// Target
            GL11.glColor4d(1, 0, 0, 0.5F);
        else if (mode == 4)// Team
            GL11.glColor4d(0, 1, 0, 0.5F);
        glBegin(GL_LINES);
        {
            glVertex3d(0, Minecraft.mc().thePlayer.getEyeHeight(), 0);
            glVertex3d(x, y, z);
        }
        glEnd();
        glEnable(GL11.GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);
    }

    public static void tracerLine(Entity entity, Color color) {
        double x = entity.posX - RenderManager.renderPosX;
        double y = entity.posY + entity.height / 2 - RenderManager.renderPosY;
        double z = entity.posZ - RenderManager.renderPosZ;
        glBlendFunc(770, 771);
        glEnable(GL_BLEND);
        glLineWidth(2.0F);
        glDisable(GL11.GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glBegin(GL_LINES);
        {
            glVertex3d(0, Minecraft.mc().thePlayer.getEyeHeight(), 0);
            glVertex3d(x, y, z);
        }
        glEnd();
        glEnable(GL11.GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);
    }

    public static void tracerLine(int x, int y, int z, Color color) {
        x += 0.5 - RenderManager.renderPosX;
        y += 0.5 - RenderManager.renderPosY;
        z += 0.5 - RenderManager.renderPosZ;
        glBlendFunc(770, 771);
        glEnable(GL_BLEND);
        glLineWidth(2.0F);
        glDisable(GL11.GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glBegin(GL_LINES);
        {
            glVertex3d(0, Minecraft.mc().thePlayer.getEyeHeight(), 0);
            glVertex3d(x, y, z);
        }
        glEnd();
        glEnable(GL11.GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);//
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);//
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);//
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

    public static String removeColorCode(String text) {
        String finalText = text;
        if (finalText.contains("§")) {
            for (int i = 0; i < finalText.length(); i++) {
                if (Character.toString(finalText.charAt(i)).equals("�")) {
                    try {
                        String part1 = finalText.substring(0, i);
                        String part2 = finalText.substring(Math.min(i + 2, finalText.length()));
                        finalText = part1 + part2;
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return finalText;
    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);//
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);//
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);//
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);//
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);//
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ);
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ);
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ);
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ);
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);//
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

    public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue,
                                            float alpha, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        // GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glLineWidth(lineWidth);
        glColor4f(red, green, blue, alpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawBlockESP(double x, double y, double z, float red, float green, float blue, float alpha,
                                    float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        // GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
        GL11.glLineWidth(lineWidth);
        glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawSolidBlockESP(double x, double y, double z, float red, float green, float blue,
                                         float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        // GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawOutlinedEntityESP(double x, double y, double z, double width, double height, float red,
                                             float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        // GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        glColor4f(red, green, blue, alpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawSolidEntityESP(double x, double y, double z, double width, double height, float red,
                                          float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        // GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green,
                                     float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        // GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glLineWidth(lineWdith);
        glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawTracerLine(double x, double y, double z, float red, float green, float blue, float alpha,
                                      float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        // GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(lineWdith);
        glColor4f(red, green, blue, alpha);
        GL11.glBegin(2);
        GL11.glVertex3d(0.0D, 0.0D + Minecraft.mc().thePlayer.getEyeHeight(), 0.0D);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        // GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    public static void drawCircle(int x, int y, double r, int c) {
        float f = ((c >> 24) & 0xff) / 255F;
        float f1 = ((c >> 16) & 0xff) / 255F;
        float f2 = ((c >> 8) & 0xff) / 255F;
        float f3 = (c & 0xff) / 255F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_LINE_LOOP);

        for (int i = 0; i <= 360; i++) {
            double x2 = Math.sin(((i * Math.PI) / 180)) * r;
            double y2 = Math.cos(((i * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawFilledCircle(int x, int y, double r, int c) {
        float f = ((c >> 24) & 0xff) / 255F;
        float f1 = ((c >> 16) & 0xff) / 255F;
        float f2 = ((c >> 8) & 0xff) / 255F;
        float f3 = (c & 0xff) / 255F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360; i++) {
            double x2 = Math.sin(((i * Math.PI) / 180)) * r;
            double y2 = Math.cos(((i * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void dr(double i, double j, double k, double l, int i1) {
        if (i < k) {
            double j1 = i;
            i = k;
            k = j1;
        }

        if (j < l) {
            double k1 = j;
            j = l;
            l = k1;
        }

        float f = ((i1 >> 24) & 0xff) / 255F;
        float f1 = ((i1 >> 16) & 0xff) / 255F;
        float f2 = ((i1 >> 8) & 0xff) / 255F;
        float f3 = (i1 & 0xff) / 255F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        glColor4f(f1, f2, f3, f);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);//
        worldRenderer.pos(i, l, 0.0D);
        worldRenderer.pos(k, l, 0.0D);
        worldRenderer.pos(k, j, 0.0D);
        worldRenderer.pos(i, j, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void drawCircle(final int x, final int y, final float radius, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        final boolean blend = GL11.glIsEnabled(3042);
        final boolean line = GL11.glIsEnabled(2848);
        final boolean texture = GL11.glIsEnabled(3553);
        if (!blend) {
            GL11.glEnable(3042);
        }
        if (!line) {
            GL11.glEnable(2848);
        }
        if (texture) {
            GL11.glDisable(3553);
        }
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d(x + Math.sin(i * 3.141526 / 180.0) * radius, y + Math.cos(i * 3.141526 / 180.0) * radius);
        }
        GL11.glEnd();
        if (texture) {
            GL11.glEnable(3553);
        }
        if (!line) {
            GL11.glDisable(2848);
        }
        if (!blend) {
            GL11.glDisable(3042);
        }
    }

}
