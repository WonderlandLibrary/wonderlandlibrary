package me.nyan.flush.utils.render;


import me.nyan.flush.Flush;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void glColor(int red, int green, int blue, int alpha) {
        GlStateManager.color(red / 255F, green / 255F, blue / 255F, alpha / 255F);
    }

    public static void glColor(int color) {
        glColor(color >> 16 & 255, color >> 8 & 255, color & 255, color >> 24 & 255);
    }

    public static void drawLoadingCircle(float x, float y, float radius, int color, int lineWidth) {
        int i = (int) ((System.currentTimeMillis() / 3) % 360);
        drawCircle(x, y, radius, i - 200, i, color, lineWidth);
    }

    public static void drawCircle(float x, float y, float radius, int color, float lineWidth) {
        drawCircle(x, y, radius, -180, 180, color, lineWidth);
    }

    public static void drawCircle(float x, float y, float radius, int start, int end, int color, float lineWidth) {
        glLineWidth(lineWidth);
        glColor(color);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        glEnable(GL_LINE_SMOOTH);

        glBegin(GL_LINE_STRIP);

        float a = (end - start) / Math.max(radius, 20);
        for (float i = start; i <= end; i += a) {
            float radians = MathUtils.toRadians(i);
            glVertex2f(x + MathHelper.cos(radians) * radius, y + MathHelper.sin(radians) * radius);
        }

        float endR = MathUtils.toRadians(end);
        glVertex2f(x + MathHelper.cos(endR) * (radius - 0.5F), y + MathHelper.sin(endR) * (radius - 0.5F));

        glEnd();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        glColor(-1);
    }

    public static void fillCircle(float x, float y, float radius, int start, int end, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        glColor(color);
        glBegin(GL_TRIANGLE_FAN);
        glVertex2f(x, y);
        float a = (end - start) / Math.max(radius, 20);
        for (float i = start; i <= end; i += a) {
            float radians = i / 180F * MathHelper.PI;
            glVertex2f(x + MathHelper.cos(radians) * (radius - 0.5F), y + MathHelper.sin(radians) * (radius - 0.5F));
        }

        float endR = MathUtils.toRadians(end);
        glVertex2f(x + MathHelper.cos(endR) * (radius - 0.5F), y + MathHelper.sin(endR) * (radius - 0.5F));

        glEnd();
        glColor(-1);
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

        if ((color >> 24 & 255) == 255) {
            drawCircle(x, y, radius - 0.5F, start, end, color, 1);
        }
    }

    public static void fillCircle(float x, float y, float radius, int color) {
        fillCircle(x, y, radius, -180, 180, color);
    }

    public static void drawRectangle(float x, float y, float width, float height, int color, float lineWidth) {
        glLineWidth(lineWidth);
        glDisable(GL_LINE_SMOOTH);

        GlStateManager.enableBlend();
        glColor(color);

        glBegin(GL_LINE_STRIP);

        glVertex2f(x, y);
        glVertex2f(x + width, y);
        glVertex2f(x + width, y + height);
        glVertex2f(x, y + height);
        glVertex2f(x, y);

        glEnd();

        glColor(-1);
    }

    public static void fillRectangle(float x, float y, float width, float height, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        glColor(color);

        glBegin(GL_TRIANGLE_FAN);

        glVertex2f(x, y + height);
        glVertex2f(x + width, y + height);
        glVertex2f(x + width, y);
        glVertex2f(x, y);

        glEnd();

        GlStateManager.enableTexture2D();
        glColor(-1);
    }

    public static void fillRoundRect(float x, float y, float width, float height, float cornerRadius, int color) {
        cornerRadius = Math.min(Math.min(cornerRadius, width / 2F), height / 2F);

        GlStateManager.disableAlpha();

        Gui.drawRect(x + cornerRadius, y, x + width - cornerRadius, y + height, color);
        Gui.drawRect(x, y + cornerRadius, x + cornerRadius, y + height - cornerRadius, color);
        Gui.drawRect(x + width - cornerRadius, y + cornerRadius, x + width, y + height - cornerRadius, color);

        fillCircle(x + cornerRadius, y + cornerRadius, cornerRadius, -180, -90, color);
        fillCircle(x + cornerRadius, y + height - cornerRadius, cornerRadius, 90, 180, color);

        fillCircle(x + width - cornerRadius, y + cornerRadius, cornerRadius, -90, 0, color);
        fillCircle(x + width - cornerRadius, y + height - cornerRadius, cornerRadius, 0, 90, color);

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRoundRect(float x, float y, float width, float height, float cornerRadius, int color, float lineWidth) {
        cornerRadius = Math.min(Math.min(cornerRadius, width / 2F), height / 2F);
        GlStateManager.disableAlpha();

        glLineWidth(lineWidth);
        drawLine(x + cornerRadius, y, x + width - cornerRadius, y, color);
        drawLine(x + cornerRadius, y + height, x + width - cornerRadius, y + height, color);
        drawLine(x, y + cornerRadius, x, y + height - cornerRadius, color);
        drawLine(x + width, y + cornerRadius, x + width, y + height - cornerRadius, color);

        drawCircle(x + cornerRadius, y + cornerRadius, cornerRadius, -180, -90, color, lineWidth);
        drawCircle(x + cornerRadius, y + height - cornerRadius, cornerRadius, 90, 180, color, lineWidth);

        drawCircle(x + width - cornerRadius, y + cornerRadius, cornerRadius, -90, 0, color, lineWidth);
        drawCircle(x + width - cornerRadius, y + height - cornerRadius, cornerRadius, 0, 90, color, lineWidth);

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawLine(float x, float y, float x2, float y2, int color) {
        glColor(color);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        //glEnable(GL_LINE_SMOOTH);

        glBegin(GL_LINES);
        glVertex2f(x, y);
        glVertex2f(x2, y2);
        glEnd();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        glColor(-1);
    }

    public static void drawCross(float x, float y, float width, float height, int color) {
        GL11.glLineWidth(1F);
        drawLine(x, y, x + width, y + height, color);
        drawLine(x + width, y, x, y + height, color);
    }

    public static void drawHashtag(float x, float y, float width, float height, int color) {
        //horizontal lines
        drawLine(x + 1, y + 4, x + width, y + 4, color);
        drawLine(x, y + height - 4, x + width - 1, y + height - 4, color);

        //vertical lines
        drawLine(x + width / 2f + 3.5f, y, x + width / 2f + 1, y + height, color);

        drawLine(x + width / 2f - 1, y, x + width / 2f - 3.5f, y + height, color);
    }

    public static void drawEntityOnScreen(EntityLivingBase e, int posX, int posY, int scale, float mouseX, float mouseY) {
        glColor(-1);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.enableDepth();
        GlStateManager.translate((float) posX, (float) posY, 50.0F);
        GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = e.renderYawOffset;
        float f1 = e.rotationYaw;
        float f2 = e.rotationPitch;
        float f3 = e.prevRotationYawHead;
        float f4 = e.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float) Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
        e.renderYawOffset = (float) Math.atan(mouseX / 40.0F) * 20.0F;
        e.rotationYaw = (float) Math.atan(mouseX / 40.0F) * 40.0F;
        e.rotationPitch = -((float) Math.atan(mouseY / 40.0F)) * 20.0F;
        e.rotationYawHead = e.rotationYaw;
        e.prevRotationYawHead = e.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(e, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);
        e.renderYawOffset = f;
        e.rotationYaw = f1;
        e.rotationPitch = f2;
        e.prevRotationYawHead = f3;
        e.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static void drawFace(AbstractClientPlayer e, int x, int y, int width, int height) {
        ResourceLocation skin = e.getLocationSkin();
        GlStateManager.pushMatrix();
        mc.getTextureManager().bindTexture(skin);
        GlStateManager.enableBlend();
        GlStateManager.color(1, 1, 1);
        Gui.drawScaledCustomSizeModalRect(x, y, 8.0f, 8.0f, 8, 8, width, height, 64.0f, 64.0f);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBoundingBoxOutline(AxisAlignedBB boundingBox, int color) {
        glColor(color);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION);

        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();

        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();

        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();

        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();

        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();

        tessellator.draw();
        glColor(-1);
    }

    public static void fillBoundingBox(AxisAlignedBB axisAlignedBB, int color) {
        glColor(color);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        worldRenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION);

        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();

        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();

        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();

        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();

        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();

        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
        glColor(-1);
    }

    public static void drawBlockOutline(BlockPos blockPos, float partialTicks, int color) {
        Block block = blockPos.getBlock();
        block.setBlockBoundsBasedOnState(mc.theWorld, blockPos);

        double x = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * (double) partialTicks;
        double y = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * (double) partialTicks;
        double z = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * (double) partialTicks;

        drawBoundingBoxOutline(block.getSelectedBoundingBox(mc.theWorld, blockPos).offset(-x, -y, -z), color);
    }

    public static void drawBlockBox(BlockPos blockPos, float partialTicks, int color) {
        Block block = blockPos.getBlock();
        block.setBlockBoundsBasedOnState(mc.theWorld, blockPos);

        double x = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * (double) partialTicks;
        double y = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * (double) partialTicks;
        double z = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * (double) partialTicks;

        fillBoundingBox(block.getSelectedBoundingBox(mc.theWorld, blockPos).offset(-x, -y, -z), color);
    }

    public static void glScissor(double x, double y, double x2, double y2) {
        ScaledResolution scale = new ScaledResolution(mc);
        float factor = scale.getScaleFactor();
        GL11.glScissor((int) (x * factor), (int) (((float) scale.getScaledHeight() - y2) * factor),
                (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
    }

    public static void drawImage(ResourceLocation image, double x, double y, double width, double height) {
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(image);
        double f = 1.0F / width;
        double f1 = 1.0F / height;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0D).tex((0 * f), ((0 + (float) height) * f1)).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).tex(((0 + (float) width) * f), ((0 + (float) height) * f1)).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).tex(((0 + (float) width) * f), (0 * f1)).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex((0 * f), (0 * f1)).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
    }

    public static void drawImageCentered(ResourceLocation image, double x, double y, double width, double height) {
        drawImage(image, x - width / 2.0, y - height / 2.0, width, height);
    }

    public static void drawImage(DynamicTexture image, double x, double y, double width, double height, ResourceLocation id) {
        mc.getTextureManager().loadTexture(id, image);
        drawImage(id, x, y, width, height);
    }

    public static float calculateSpeed(float speed) {
        return speed / 20F * Flush.getFrameTime();
    }

    public static void drawGradientRect(double left, double top, double right, double bottom, int startColor, int endColor) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.shadeModel(GL_SMOOTH);
        glBegin(GL_TRIANGLE_FAN);

        glColor(startColor);
        glVertex2d(right, top);
        glVertex2d(left, top);
        glColor(endColor);
        glVertex2d(left, bottom);
        glVertex2d(right, bottom);

        glEnd();
        glColor(-1);
        GlStateManager.shadeModel(GL_FLAT);
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static float getFPS() {
        return 1000F / Flush.getFrameTime();
    }

    public static int drawString(Object fontRenderer, String text, float x, float y, int color, boolean shadow) {
        if (fontRenderer instanceof FontRenderer) {
            return ((FontRenderer) fontRenderer).drawString(text, x, y, color, shadow);
        }
        if (fontRenderer instanceof GlyphPageFontRenderer) {
            return ((GlyphPageFontRenderer) fontRenderer).drawString(text, x, y, color, shadow);
        }
        return 0;
    }

    public static int getStringWidth(Object fontRenderer, String text) {
        if (fontRenderer instanceof FontRenderer) {
            return ((FontRenderer) fontRenderer).getStringWidth(text);
        }
        if (fontRenderer instanceof GlyphPageFontRenderer) {
            return ((GlyphPageFontRenderer) fontRenderer).getStringWidth(text);
        }
        return 0;
    }

    public static int getFontHeight(Object fontRenderer) {
        if (fontRenderer instanceof FontRenderer) {
            return ((FontRenderer) fontRenderer).FONT_HEIGHT;
        }
        if (fontRenderer instanceof GlyphPageFontRenderer) {
            return ((GlyphPageFontRenderer) fontRenderer).getFontHeight();
        }
        return 0;
    }
}