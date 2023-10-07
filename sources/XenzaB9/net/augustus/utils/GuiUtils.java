// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class GuiUtils
{
    public static void drawRect1(final double x, final double y, final double width, final double height, final int color) {
        final float f = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GL11.glColor4f(f2, f3, f4, f);
        Gui.drawRect((int)x, (int)y, (int)(x + width), (int)(y + height), color);
    }
    
    public static boolean isHovering(final int mouseX, final int mouseY, final int posX, final int posY, final int width, final int height) {
        return mouseX >= posX && mouseX < posX + width && mouseY >= posY && mouseY < posY + height;
    }
    
    public static void glResets() {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
    }
    
    public static void drawStraightLine(final int startPos, final int endPos, final int yPos, final int thickness, final int color) {
        Gui.drawRect(startPos, yPos + thickness, endPos, yPos, color);
    }
    
    public static void glStartFontRenderer() {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
    }
    
    public static void drawImage(final ResourceLocation resourceLocation, final float x, final float y, final float width, final float height, final int rgba) {
        final float a = (rgba >> 24 & 0xFF) / 255.0f;
        final float r = (rgba >> 16 & 0xFF) / 255.0f;
        final float g = (rgba >> 8 & 0xFF) / 255.0f;
        final float b = (rgba & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        GlStateManager.color(r, g, b, a);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        Gui.drawScaledCustomSizeModalRect((int)x, (int)y, 0.0f, 0.0f, (int)width, (int)height, (int)width, (int)height, width, height);
        GL11.glPopMatrix();
    }
    
    public static void drawRect(double left, double top, double right, double bottom, final float opacity) {
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
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(0.1f, 0.1f, 0.1f, opacity);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
