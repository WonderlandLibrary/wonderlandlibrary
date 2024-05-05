// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.render;

import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class Draw
{
    public static void drawRoundedRect(final double x, final double y, final double width, final double height, final float cornerRadius) {
        final int slices = 10;
        drawFillRectangle(x + cornerRadius, y, width - 2.0f * cornerRadius, height);
        drawFillRectangle(x, y + cornerRadius, cornerRadius, height - 2.0f * cornerRadius);
        drawFillRectangle(x + width - cornerRadius, y + cornerRadius, cornerRadius, height - 2.0f * cornerRadius);
        drawCirclePart(x + cornerRadius, y + cornerRadius, -3.1415927f, -1.5707964f, cornerRadius, 10);
        drawCirclePart(x + cornerRadius, y + height - cornerRadius, -1.5707964f, 0.0f, cornerRadius, 10);
        drawCirclePart(x + width - cornerRadius, y + cornerRadius, 1.5707964f, 3.1415927f, cornerRadius, 10);
        drawCirclePart(x + width - cornerRadius, y + height - cornerRadius, 0.0f, 1.5707964f, cornerRadius, 10);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GlStateManager.disableBlend();
    }
    
    public static void drawHollowRect(final float x, final float y, final float w, final float h, final int color) {
        Gui.drawHorizontalLine((int)x, (int)w, (int)y, color);
        Gui.drawHorizontalLine((int)x, (int)w, (int)h, color);
        Gui.drawVerticalLine((int)x, (int)h, (int)y, color);
        Gui.drawVerticalLine((int)w, (int)h, (int)y, color);
    }
    
    public static void drawBorderedRectangle(final double left, final double top, final double right, final double bottom, final double borderWidth, final int insideColor, final int borderColor, final boolean borderIncludedInBounds) {
        drawRectangle(left - (borderIncludedInBounds ? 0.0 : borderWidth), top - (borderIncludedInBounds ? 0.0 : borderWidth), right + (borderIncludedInBounds ? 0.0 : borderWidth), bottom + (borderIncludedInBounds ? 0.0 : borderWidth), borderColor);
        drawRectangle(left + (borderIncludedInBounds ? borderWidth : 0.0), top + (borderIncludedInBounds ? borderWidth : 0.0), right - (borderIncludedInBounds ? borderWidth : 0.0), bottom - (borderIncludedInBounds ? borderWidth : 0.0), insideColor);
    }
    
    public static void drawRectangle(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f4, f5, f6, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawFillRectangle(final double x, final double y, final double width, final double height) {
        GlStateManager.enableBlend();
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glBegin(7);
        GL11.glVertex2d(x, y + height);
        GL11.glVertex2d(x + width, y + height);
        GL11.glVertex2d(x + width, y);
        GL11.glVertex2d(x, y);
        GL11.glEnd();
    }
    
    public static void drawCirclePart(final double x, final double y, final float fromAngle, final float toAngle, final float radius, final int slices) {
        GlStateManager.enableBlend();
        GL11.glBegin(6);
        GL11.glVertex2d(x, y);
        final float increment = (toAngle - fromAngle) / slices;
        for (int i = 0; i <= slices; ++i) {
            final float angle = fromAngle + i * increment;
            final float dX = MathHelper.sin(angle);
            final float dY = MathHelper.cos(angle);
            GL11.glVertex2d(x + dX * radius, y + dY * radius);
        }
        GL11.glEnd();
    }
    
    public static void color(final int color) {
        final float red = (color & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color >> 16 & 0xFF) / 255.0f;
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }
    
    public static void colorRGBA(final int color) {
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        GlStateManager.color(r, g, b, a);
    }
}
