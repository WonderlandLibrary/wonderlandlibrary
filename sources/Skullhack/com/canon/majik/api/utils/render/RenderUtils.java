package com.canon.majik.api.utils.render;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.utils.Globals;
import com.canon.majik.impl.modules.impl.client.ClickGui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils implements Globals {

    public static float PI = 3.14159265358979323846F;

    public static void drawRectCircle(float x, float y, float width, float height, float value, int color) {
        float radius = Math.max(width / 2F, height / 2F);

        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, (color >> 24 & 0xFF) / 255.0F);
        GL11.glBegin(GL11.GL_POLYGON);
        for (int i = 0; i <= 360; i++) {
            float xRadius = MathHelper.sin(i * PI / 180F) * (radius * value);
            float yRadius = MathHelper.cos(i * PI / 180F) * (radius * value);
            if (0 >= xRadius) {
                xRadius = Math.max(xRadius, -(width / 2F));
            } else {
                xRadius = Math.min(xRadius, width / 2F);
            }

            if (0 >= yRadius) {
                yRadius = Math.max(yRadius, -(height / 2F));
            } else {
                yRadius = Math.min(yRadius, height / 2F);
            }

            GL11.glVertex2d(x + width / 2F + xRadius, y + height / 2F + yRadius);
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    public static void rect(float x, float y, float width, float height, int color) {
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(red, green, blue, alpha);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(x, y + height, 0.0D).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0D).endVertex();
        bufferbuilder.pos(x + width, y, 0.0D).endVertex();
        bufferbuilder.pos(x, y, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawOutlineRect(double left, double top, double right, double bottom, Color color, float lineWidth) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float) (color.getRGB() >> 24 & 255) / 255.0F;
        float f = (float) (color.getRGB() >> 16 & 255) / 255.0F;
        float f1 = (float) (color.getRGB() >> 8 & 255) / 255.0F;
        float f2 = (float) (color.getRGB() & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        GL11.glLineWidth(lineWidth);
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, top, 0.0D).endVertex();
        bufferbuilder.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
    }

    public static void invokeScissor(float x, float y, float width, float height) {
        scissor(x, y, width, height);
        glEnable(GL_SCISSOR_TEST);
    }

    public static void scissor(float x, float y, float width, float height) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        glScissor((int) (x * scaledResolution.getScaleFactor()), (int) ((scaledResolution.getScaledHeight() - height) * scaledResolution.getScaleFactor()), (int) ((width - x) * scaledResolution.getScaleFactor()), (int) ((height - y) * scaledResolution.getScaleFactor()));
    }

    public static void releaseScissor() {
        glDisable(GL_SCISSOR_TEST);
    }

    public static void circle(float x, float y, final float radius, Color color) {
        final double pi = Math.PI;
        x = x - radius / 2.0f;
        y = y - radius / 2.0f;

        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glDisable(GL_ALPHA_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);
        glColor(color);
        glDisable(GL_CULL_FACE);
        glBegin(GL_TRIANGLE_FAN);

        for (int i = 0; i < 360; i++) {
            glVertex2d(x + radius + Math.sin(i * pi / 180.0) * radius * -1.0, y + radius + Math.cos(i * pi / 180.0) * radius * -1.0);
        }

        glEnd();
        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }

    public static void circleOutline(float x, float y, final float radius, Color color) {
        final double pi = Math.PI;
        x = x - radius / 2.0f;
        y = y - radius / 2.0f;

        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glDisable(GL_ALPHA_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);
        glColor(color);
        glDisable(GL_CULL_FACE);
        glBegin(GL_LINE_STRIP);

        for (int i = 0; i < 360; i++) {
            glVertex2d(x + radius + Math.sin(i * pi / 180.0) * radius * -1.0, y + radius + Math.cos(i * pi / 180.0) * radius * -1.0);
        }

        glEnd();
        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }

    public static float scrollAnimate(float endPoint, float current, float speed) {
        boolean shouldContinueAnimation = endPoint > current;
        if (speed < 0.0f) {
            speed = 0.0f;
        } else if (speed > 1.0f) {
            speed = 1.0f;
        }

        float dif = Math.max(endPoint, current) - Math.min(endPoint, current);
        float factor = dif * speed;
        return current + (shouldContinueAnimation ? factor : -factor);
    }

    public static void glColor(Color color) {
        glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }


    public static void renderBox(AxisAlignedBB axisAlignedBB, boolean fill, boolean outline, Color color){
        if(fill){
            RenderUtils.renderBox(axisAlignedBB, color);
        }
        if(outline){
            RenderUtils.renderOutline(axisAlignedBB, color, 2.0f);
        }
    }

    public static void renderOutline(AxisAlignedBB bb, Color color, float lineWidth) {
        bb = new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ).offset(new Vec3d(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ));
        glPushMatrix();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glLineWidth(lineWidth);
        glColor(color);
        glBegin(GL_LINE_STRIP);
        glVertex3d(bb.minX, bb.minY, bb.minZ);
        glVertex3d(bb.minX, bb.minY, bb.maxZ);
        glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        glVertex3d(bb.maxX, bb.minY, bb.minZ);
        glVertex3d(bb.minX, bb.minY, bb.minZ);
        glVertex3d(bb.minX, bb.maxY, bb.minZ);
        glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        glVertex3d(bb.minX, bb.minY, bb.maxZ);
        glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        glVertex3d(bb.maxX, bb.minY, bb.minZ);
        glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        glVertex3d(bb.minX, bb.maxY, bb.minZ);
        glEnd();
        glColor(Color.WHITE);
        glLineWidth(1.0f);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        glPopMatrix();
    }

    public static void renderBox(AxisAlignedBB bb, Color color) {
        bb = new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ).offset(new Vec3d(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ));
        glPushMatrix();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        RenderGlobal.renderFilledBox(bb, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        glPopMatrix();
    }

    public static void drawString(final double scale, final String text,
                                  final float x, final float y, final int color) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        mc.fontRenderer.drawString(text, x, y, color, true);
        GlStateManager.popMatrix();
    }

    public static void drawHLine(int x, int y, int width, int colour) {
        rect(x, y, width, 1, colour);
    }

    public static void drawVLine(int x, int y, int height, int colour) {
        rect(x, y, 1, height, colour);
    }
}
