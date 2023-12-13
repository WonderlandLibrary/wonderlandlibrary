package slavikcodd3r.rainbow.utils;

import java.awt.Color;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;

import org.jibble.pircbot.Colors;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;

public class RenderUtil
{

    public static void drawFancy(final double d, final double e, final double f2, final double f3, final int paramColor) {
        final float alpha = (paramColor >> 24 & 0xFF) / 255.0f;
        final float red = (paramColor >> 16 & 0xFF) / 255.0f;
        final float green = (paramColor >> 8 & 0xFF) / 255.0f;
        final float blue = (paramColor & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glPushMatrix();
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(7);
        GL11.glVertex2d(f2 + 1.300000011920929, e);
        GL11.glVertex2d(d + 1.0, e);
        GL11.glVertex2d(d - 1.300000011920929, f3);
        GL11.glVertex2d(f2 - 1.0, f3);
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GL11.glDisable(2832);
        GL11.glDisable(3042);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }
    
    public static void drawGradient(final double x, final double y2, final double x2, final double y22, final int col1, final int col2) {
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
        final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
        final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
        final float f8 = (col2 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x, y2);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(x, y22);
        GL11.glVertex2d(x2, y22);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }
    
    public static void drawGradientSideways(final double left, final double top, final double right, final double bottom, final int col1, final int col2) {
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
        final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
        final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
        final float f8 = (col2 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }
    
    public static void rectangle(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            final double var5 = top;
            top = bottom;
            bottom = var5;
        }
        final float var6 = (color >> 24 & 0xFF) / 255.0f;
        final float var7 = (color >> 16 & 0xFF) / 255.0f;
        final float var8 = (color >> 8 & 0xFF) / 255.0f;
        final float var9 = (color & 0xFF) / 255.0f;
        final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var7, var8, var9, var6);
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(left, bottom, 0.0);
        worldRenderer.addVertex(right, bottom, 0.0);
        worldRenderer.addVertex(right, top, 0.0);
        worldRenderer.addVertex(left, top, 0.0);
        Tessellator.getInstance().draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void rectangleBordered(final double x, final double y2, final double x1, final double y1, final double width, final int internalColor, final int borderColor) {
        rectangle(x + width, y2 + width, x1 - width, y1 - width, internalColor);
        rectangle(x + width, y2, x1 - width, y2 + width, borderColor);
        rectangle(x, y2, x + width, y1, borderColor);
        rectangle(x1 - width, y2, x1, y1, borderColor);
        rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
    }
    
    public static void filledBox(final AxisAlignedBB boundingBox, final int color, final boolean shouldColor) {
        GlStateManager.pushMatrix();
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        if (shouldColor) {
            GlStateManager.color(var12, var13, var14, var11);
        }
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        Tessellator.getInstance().draw();
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldRenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        Tessellator.getInstance().draw();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }
    
    public static void drawOutlinedBoundingBox(final AxisAlignedBB boundingBox) {
        final Tessellator var1 = Tessellator.getInstance();
        final WorldRenderer var2 = var1.getWorldRenderer();
        var2.startDrawing(3);
        var2.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        var2.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        var2.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        var2.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        var2.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        var1.draw();
        var2.startDrawing(3);
        var2.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        var2.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        var2.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        var2.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        var2.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        var1.draw();
        var2.startDrawing(1);
        var2.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        var2.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        var2.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        var2.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        var2.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        var2.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        var2.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        var2.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        var1.draw();
    }
    
    public static void drawLines(final AxisAlignedBB boundingBox) {
        GL11.glPushMatrix();
        GL11.glBegin(2);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glEnd();
        GL11.glPopMatrix();
    }
    
    public static void drawBoundingBox(final AxisAlignedBB axisalignedbb) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrender = Tessellator.getInstance().getWorldRenderer();
        worldrender.startDrawingQuads();
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        worldrender.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.draw();
    }
    
    public static void drawLine3D(final float x, final float y2, final float z, final float x1, final float y1, final float z1, final int color) {
        pre3D();
        GL11.glLoadIdentity();
        Minecraft.getMinecraft().entityRenderer.orientCamera(Minecraft.getMinecraft().timer.renderPartialTicks);
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        GL11.glColor4f(var12, var13, var14, var11);
        GL11.glLineWidth(0.5f);
        GL11.glBegin(3);
        GL11.glVertex3d((double)x, (double)y2, (double)z);
        GL11.glVertex3d((double)x1, (double)y1, (double)z1);
        GL11.glEnd();
        post3D();
    }
    
    public static void draw3DLine(final float x, final float y2, final float z, final int color) {
        pre3D();
        GL11.glLoadIdentity();
        Minecraft.getMinecraft().entityRenderer.orientCamera(Minecraft.getMinecraft().timer.renderPartialTicks);
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        GL11.glColor4f(var12, var13, var14, var11);
        GL11.glLineWidth(0.5f);
        GL11.glBegin(3);
        GL11.glVertex3d(0.0, (double)Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0);
        GL11.glVertex3d((double)x, (double)y2, (double)z);
        GL11.glEnd();
        post3D();
    }
    
    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glHint(3154, 4354);
    }
    
    public static void post3D() {
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void glColor(final float alpha, final int redRGB, final int greenRGB, final int blueRGB) {
        final float red = 0.003921569f * redRGB;
        final float green = 0.003921569f * greenRGB;
        final float blue = 0.003921569f * blueRGB;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void drawRect(final float x, final float y2, final float x1, final float y1) {
        GL11.glBegin(7);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y2);
        GL11.glVertex2f(x, y2);
        GL11.glEnd();
    }
    
    public static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void drawRect(final float x, final float y2, final float x1, final float y1, final int color) {
        enableGL2D();
        glColor(color);
        drawRect(x, y2, x1, y1);
        disableGL2D();
    }
    
    public static void drawRoundedRect(final float x, final float y2, final float x1, final float y1, final int borderC, final int insideC) {
        drawRect(x + 0.5f, y2, x1 - 0.5f, y2 + 0.5f, insideC);
        drawRect(x + 0.5f, y1 - 0.5f, x1 - 0.5f, y1, insideC);
        drawRect(x, y2 + 0.5f, x1, y1 - 0.5f, insideC);
    }
    
    public static void drawHLine(float x, float y2, final float x1, final int y1) {
        if (y2 < x) {
            final float var5 = x;
            x = y2;
            y2 = var5;
        }
        drawRect(x, x1, y2 + 1.0f, x1 + 1.0f, y1);
    }
    
    public static void drawVLine(final float x, final float y2, final float x1, final float y1, final float width, final int color) {
        if (width <= 0.0f) {
            return;
        }
        GL11.glPushMatrix();
        pre3D();
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        final int shade = GL11.glGetInteger(2900);
        GlStateManager.shadeModel(7425);
        GL11.glColor4f(var12, var13, var14, var11);
        final float line = GL11.glGetFloat(2849);
        GL11.glLineWidth(width);
        GL11.glBegin(3);
        GL11.glVertex3d((double)x, (double)y2, 0.0);
        GL11.glVertex3d((double)x1, (double)y1, 0.0);
        GL11.glEnd();
        GlStateManager.shadeModel(shade);
        GL11.glLineWidth(line);
        post3D();
        GL11.glPopMatrix();
    }
    
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static void drawCircle(final float cx, final float cy, final float r, final int num_segments, final int c2) {
        final float f = (c2 >> 24 & 0xFF) / 255.0f;
        final float f2 = (c2 >> 16 & 0xFF) / 255.0f;
        final float f3 = (c2 >> 8 & 0xFF) / 255.0f;
        final float f4 = (c2 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        GL11.glVertex2f(cx, cy);
        for (int i = num_segments; i >= 0; --i) {
            final double theta = i * 6.283185307179586 / num_segments;
            GL11.glVertex2d(cx + r * Math.cos(theta), cy + r * Math.sin(theta));
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public static void drawBorderedCircle(final float circleX, final float circleY, final double radius, final double width, final int borderColor, final int innerColor) {
        enableGL2D();
        GlStateManager.enableBlend();
        GL11.glEnable(2881);
        drawCircle(circleX, circleY, (float)(radius - 0.5 + width), 72, borderColor);
        drawFullCircle(circleX, circleY, (float)radius, innerColor);
        GlStateManager.disableBlend();
        GL11.glDisable(2881);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        disableGL2D();
    }
    
    public static void drawCircleNew(final float x, final float y2, final float radius, final int numberOfSides) {
    }
    
    public static void drawFullCircle(float cx, float cy, float r, final int c2) {
        cx *= 2.0f;
        cy *= 2.0f;
        final float p2 = (float)Math.cos(0.19634953141212463);
        final float s = (float)Math.sin(0.19634953141212463);
        float x = r *= 2.0f;
        float y2 = 0.0f;
        enableGL2D();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glEnable(3024);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        glColor(c2);
        GL11.glBegin(9);
        for (int ii = 0; ii < 32; ++ii) {
            GL11.glVertex2f(x + cx, y2 + cy);
            final float t = x;
            x = p2 * x - s * y2;
            y2 = s * t + p2 * y2;
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        disableGL2D();
    }
    
    public static void glColor(final Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public static void drawCircle(final int x, final int y2, final double r, final int c2) {
        final float f = (c2 >> 24 & 0xFF) / 255.0f;
        final float f2 = (c2 >> 16 & 0xFF) / 255.0f;
        final float f3 = (c2 >> 8 & 0xFF) / 255.0f;
        final float f4 = (c2 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(2);
        for (int i = 0; i <= 360; ++i) {
            final double x2 = Math.sin(i * 3.141592653589793 / 180.0) * r;
            final double y3 = Math.cos(i * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(x + x2, y2 + y3);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public static void drawTracerLine(final double x, final double y2, final double z, final Color color, final float alpha, final float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(lineWdith);
        glColor(color);
        GL11.glBegin(2);
        Minecraft.getMinecraft();
        GL11.glVertex3d(0.0, 0.0 + Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0);
        GL11.glVertex3d(x, y2, z);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
}
