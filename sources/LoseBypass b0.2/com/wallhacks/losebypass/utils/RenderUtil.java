/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.wallhacks.losebypass.utils;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.systems.module.modules.misc.FreeLook;
import com.wallhacks.losebypass.utils.ColorUtil;
import com.wallhacks.losebypass.utils.MC;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderUtil
implements MC {
    public static void bindTexture(int texture) {
        GL11.glBindTexture((int)3553, (int)texture);
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer != null && framebuffer.framebufferWidth == RenderUtil.mc.displayWidth) {
            if (framebuffer.framebufferHeight == RenderUtil.mc.displayHeight) return framebuffer;
        }
        if (framebuffer == null) return new Framebuffer(RenderUtil.mc.displayWidth, RenderUtil.mc.displayHeight, true);
        framebuffer.deleteFramebuffer();
        return new Framebuffer(RenderUtil.mc.displayWidth, RenderUtil.mc.displayHeight, true);
    }

    public static void draw3DText(String text, double x, double y, double z, double scale, float red, float green, float blue, float alpha) {
        RenderUtil.drawNametag(text, x, y, z, scale, new Color(red / 255.0f, green / 255.0f, blue / 255.0f, alpha / 255.0f).getRGB());
    }

    public static void drawNametag(String text, double x, double y, double z, double scale, int color) {
        int textWidth = LoseBypass.fontManager.getTextWidth(text) / 2;
        RenderUtil.prepare3D(x, y, z, scale);
        LoseBypass.fontManager.drawString(text, -textWidth, -LoseBypass.fontManager.getTextHeight(), color);
        RenderUtil.release3D();
    }

    public static Vec3 interpolateEntityByTicks(Entity entity, float renderPartialTicks) {
        return new Vec3(RenderUtil.calculateDistanceWithPartialTicks(entity.posX, entity.lastTickPosX, renderPartialTicks) - RenderUtil.mc.getRenderManager().renderPosX, RenderUtil.calculateDistanceWithPartialTicks(entity.posY, entity.lastTickPosY, renderPartialTicks) - RenderUtil.mc.getRenderManager().renderPosY, RenderUtil.calculateDistanceWithPartialTicks(entity.posZ, entity.lastTickPosZ, renderPartialTicks) - RenderUtil.mc.getRenderManager().renderPosZ);
    }

    public static double calculateDistanceWithPartialTicks(double originalPos, double finalPos, float renderPartialTicks) {
        return finalPos + (originalPos - finalPos) * (double)renderPartialTicks;
    }

    public static double[] interpolateEntity(Entity entity) {
        double x = RenderUtil.interpolateLastTickPos(entity.posX - 0.5, entity.lastTickPosX - 0.5);
        double y = RenderUtil.interpolateLastTickPos(entity.posY + 0.5, entity.lastTickPosY + 0.5);
        double z = RenderUtil.interpolateLastTickPos(entity.posZ - 0.5, entity.lastTickPosZ - 0.5);
        return new double[]{x, y, z};
    }

    public static double interpolateLastTickPos(double pos, double lastPos) {
        return lastPos + (pos - lastPos) * (double)RenderUtil.mc.timer.renderPartialTicks;
    }

    public static void draw3DText(String text, float x, float y, float z, double scale, float red, float green, float blue, float alpha) {
        double distance = mc.getRenderViewEntity().getDistance(x, y, z);
        RenderUtil.drawNametag(text, x, y, z, scale * (distance / 100.0), new Color(red / 255.0f, green / 255.0f, blue / 255.0f, alpha / 255.0f).getRGB());
    }

    public static void prepare3D(BlockPos pos, double scaling) {
        double distance = mc.getRenderViewEntity().getDistance(pos.getX(), pos.getY(), pos.getZ());
        RenderUtil.prepare3D(pos.getX(), pos.getY(), pos.getZ(), Math.max(scaling * 5.0, scaling * distance) / 100.0);
    }

    public static void prepare3D(double x, double y, double z, double scaling) {
        double renderX = RenderUtil.mc.getRenderManager().renderPosX;
        double renderY = RenderUtil.mc.getRenderManager().renderPosY;
        double renderZ = RenderUtil.mc.getRenderManager().renderPosZ;
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate(x + 0.5 - renderX, y + 0.5 - renderY, z + 0.5 - renderZ);
        GlStateManager.rotate(-RenderUtil.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(RenderUtil.mc.getRenderManager().playerViewX, FreeLook.getPerspective() == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scaling, -scaling, scaling);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
    }

    public static void release3D() {
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }

    public static void prepareScale(double scale) {
        GL11.glPushMatrix();
        GL11.glScaled((double)scale, (double)scale, (double)scale);
    }

    public static void releaseScale() {
        GL11.glPopMatrix();
    }

    public static void glBillboardDistanceScaled(double x, double y, double z, Entity entity, double scale) {
        RenderUtil.glBillboard(x, y, z);
        int distance = (int)entity.getDistance(x, y, z);
        double scaleDistance = (double)distance / 2.0 / (2.0 + (2.0 - scale));
        if (scaleDistance < 1.0) {
            scaleDistance = 1.0;
        }
        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }

    public static void glBillboard(double x, double y, double z) {
        float scale = 0.02666667f;
        GlStateManager.translate(x - RenderUtil.mc.getRenderManager().renderPosX, y - RenderUtil.mc.getRenderManager().renderPosY, z - RenderUtil.mc.getRenderManager().renderPosZ);
        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate(-RenderUtil.mc.getRenderViewEntity().rotationYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(RenderUtil.mc.getRenderViewEntity().rotationPitch, FreeLook.getPerspective() == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
    }

    public static void boundingESPBox(AxisAlignedBB box, Color c, float lineWidth) {
        double x = box.minX - RenderUtil.mc.getRenderManager().viewerPosX;
        double y = box.minY - RenderUtil.mc.getRenderManager().viewerPosY;
        double z = box.minZ - RenderUtil.mc.getRenderManager().viewerPosZ;
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)lineWidth);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4d((double)0.0, (double)1.0, (double)0.0, (double)0.15f);
        ColorUtil.glColor(c);
        AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x - box.minX + box.maxX, y - box.minY + box.maxY, z - box.minZ + box.maxZ);
        RenderGlobal.func_181561_a(bb);
        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void boundingESPBoxFilled(AxisAlignedBB box, Color c) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        int a = c.getAlpha();
        double x = box.minX - RenderUtil.mc.getRenderManager().viewerPosX;
        double y = box.minY - RenderUtil.mc.getRenderManager().viewerPosY;
        double z = box.minZ - RenderUtil.mc.getRenderManager().viewerPosZ;
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4d((double)(0.00390625f * (float)r), (double)(0.00390625f * (float)g), (double)(0.00390625f * (float)b), (double)(0.00390625f * (float)a));
        GL11.glBegin((int)7);
        AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x - box.minX + box.maxX, y - box.minY + box.maxY, z - box.minZ + box.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.minZ);
        GL11.glEnd();
        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void renderLineList(ArrayList<Vec3> list, Color color) {
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glBegin((int)1);
        GL11.glColor4f((float)(0.00390625f * (float)color.getRed()), (float)(0.00390625f * (float)color.getGreen()), (float)(0.00390625f * (float)color.getBlue()), (float)(0.00390625f * (float)color.getAlpha()));
        int i = 0;
        while (true) {
            if (i >= list.size() - 1) {
                GL11.glEnd();
                GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
                GL11.glEnable((int)3553);
                GL11.glEnable((int)2929);
                GL11.glDepthMask((boolean)true);
                GL11.glDisable((int)3042);
                return;
            }
            double x = list.get((int)i).xCoord - RenderUtil.mc.getRenderManager().renderPosX;
            double y = list.get((int)i).yCoord - RenderUtil.mc.getRenderManager().renderPosY;
            double z = list.get((int)i).zCoord - RenderUtil.mc.getRenderManager().renderPosZ;
            GL11.glVertex3d((double)x, (double)y, (double)z);
            x = list.get((int)(i + 1)).xCoord - RenderUtil.mc.getRenderManager().renderPosX;
            y = list.get((int)(i + 1)).yCoord - RenderUtil.mc.getRenderManager().renderPosY;
            z = list.get((int)(i + 1)).zCoord - RenderUtil.mc.getRenderManager().renderPosZ;
            GL11.glVertex3d((double)x, (double)y, (double)z);
            ++i;
        }
    }

    public static AxisAlignedBB getRenderBB(Entity entity) {
        double partialTicks = RenderUtil.mc.timer.renderPartialTicks;
        return new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 + (entity.posX - entity.lastTickPosX) * partialTicks - (entity.posX - entity.lastTickPosX), entity.getEntityBoundingBox().minY + (entity.posY - entity.lastTickPosY) * partialTicks - (entity.posY - entity.lastTickPosY), entity.getEntityBoundingBox().minZ - 0.05 + (entity.posZ - entity.lastTickPosZ) * partialTicks - (entity.posZ - entity.lastTickPosZ), entity.getEntityBoundingBox().maxX + 0.05 + (entity.posX - entity.lastTickPosX) * partialTicks - (entity.posX - entity.lastTickPosX), entity.getEntityBoundingBox().maxY + 0.1 + (entity.posY - entity.lastTickPosY) * partialTicks - (entity.posY - entity.lastTickPosY), entity.getEntityBoundingBox().maxZ + 0.05 + (entity.posZ - entity.lastTickPosZ) * partialTicks - (entity.posZ - entity.lastTickPosZ));
    }
}

