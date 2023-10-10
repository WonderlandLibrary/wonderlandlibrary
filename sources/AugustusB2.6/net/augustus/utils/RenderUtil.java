// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.augustus.utils.interfaces.MC;

public class RenderUtil implements MC
{
    public static void drawRect(int left, int top, int right, int bottom, final int color) {
        if (left < right) {
            final int i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final int j = top;
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
    
    public static void drawRect(double left, double top, double right, double bottom, final int color) {
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
    
    public static void drawFloatRect(float left, float top, float right, float bottom, final int color) {
        if (left < right) {
            final float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final float j = top;
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
    
    public static void drawCircle(final double x, final double y, final double radius, final int color) {
        GL11.glPushMatrix();
        color(color);
        GL11.glBegin(9);
        for (int i = 0; i < 360; ++i) {
            GL11.glVertex2d(x + Math.sin(Math.toRadians(i)) * radius, y + Math.cos(Math.toRadians(i)) * radius);
        }
        GL11.glEnd();
        GL11.glPopMatrix();
    }
    
    public static void drawColoredCircle(final double x, final double y, final double radius, final float brightness) {
        GL11.glPushMatrix();
        GL11.glLineWidth(3.5f);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glBegin(3);
        for (int i = 0; i < 360; ++i) {
            color(Color.HSBtoRGB(1.0f, 0.0f, brightness));
            GL11.glVertex2d(x, y);
            color(Color.HSBtoRGB(i / 360.0f, 1.0f, brightness));
            GL11.glVertex2d(x + Math.sin(Math.toRadians(i)) * radius, y + Math.cos(Math.toRadians(i)) * radius);
        }
        GL11.glEnd();
        GL11.glShadeModel(7424);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    
    public static void color(final int argb) {
        final float alpha = (argb >> 24 & 0xFF) / 255.0f;
        final float red = (argb >> 16 & 0xFF) / 255.0f;
        final float green = (argb >> 8 & 0xFF) / 255.0f;
        final float blue = (argb & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void drawEntityServerESP(final Entity entity, final float red, final float green, final float blue, final float alpha, final float lineAlpha, final float lineWidth) {
        double d0 = entity.serverPosX / 32.0;
        double d2 = entity.serverPosY / 32.0;
        double d3 = entity.serverPosZ / 32.0;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase livingBase = (EntityLivingBase)entity;
            d0 = livingBase.realPosX / 32.0;
            d2 = livingBase.realPosY / 32.0;
            d3 = livingBase.realPosZ / 32.0;
        }
        final float x = (float)(d0 - RenderUtil.mc.getRenderManager().getRenderPosX());
        final float y = (float)(d2 - RenderUtil.mc.getRenderManager().getRenderPosY());
        final float z = (float)(d3 - RenderUtil.mc.getRenderManager().getRenderPosZ());
        GL11.glColor4f(red, green, blue, alpha);
        otherDrawBoundingBox(entity, x, y, z, entity.width - 0.2f, entity.height + 0.1f);
        if (lineWidth > 0.0f) {
            GL11.glLineWidth(lineWidth);
            GL11.glColor4f(red, green, blue, lineAlpha);
            otherDrawOutlinedBoundingBox(entity, x, y, z, entity.width - 0.2f, entity.height + 0.1f);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawEntityESP(final Entity entity, final float red, final float green, final float blue, final float alpha, final float lineAlpha, final float lineWidth) {
        final float x = (float)(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * RenderUtil.mc.getTimer().renderPartialTicks - RenderUtil.mc.getRenderManager().getRenderPosX());
        final float y = (float)(entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * RenderUtil.mc.getTimer().renderPartialTicks - RenderUtil.mc.getRenderManager().getRenderPosY());
        final float z = (float)(entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * RenderUtil.mc.getTimer().renderPartialTicks - RenderUtil.mc.getRenderManager().getRenderPosZ());
        GL11.glColor4f(red, green, blue, alpha);
        otherDrawBoundingBox(entity, x, y, z, entity.width - 0.2f, entity.height + 0.1f);
        if (lineWidth > 0.0f) {
            GL11.glLineWidth(lineWidth);
            GL11.glColor4f(red, green, blue, lineAlpha);
            otherDrawOutlinedBoundingBox(entity, x, y, z, entity.width - 0.2f, entity.height + 0.1f);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawBlockESP(final TileEntity tileEntity, final float red, final float green, final float blue, final float alpha, final float lineAlpha, final float lineWidth, final boolean other) {
        GlStateManager.color(red, green, blue, alpha);
        AxisAlignedBB axisAlignedBB = tileEntity.getBlockType().getSelectedBoundingBox(RenderUtil.mc.theWorld, tileEntity.getPos());
        if (tileEntity instanceof TileEntityChest) {
            final TileEntityChest tileEntityChest = (TileEntityChest)tileEntity;
            if (tileEntityChest.adjacentChestXNeg != null) {
                axisAlignedBB = axisAlignedBB.union(tileEntityChest.adjacentChestXNeg.getBlockType().getSelectedBoundingBox(RenderUtil.mc.theWorld, tileEntityChest.adjacentChestXNeg.getPos()));
            }
            else if (tileEntityChest.adjacentChestZNeg != null) {
                axisAlignedBB = axisAlignedBB.union(tileEntityChest.adjacentChestZNeg.getBlockType().getSelectedBoundingBox(RenderUtil.mc.theWorld, tileEntityChest.adjacentChestZNeg.getPos()));
            }
            else if (tileEntityChest.adjacentChestXPos != null) {
                axisAlignedBB = axisAlignedBB.union(tileEntityChest.adjacentChestXPos.getBlockType().getSelectedBoundingBox(RenderUtil.mc.theWorld, tileEntityChest.adjacentChestXPos.getPos()));
            }
            else if (tileEntityChest.adjacentChestZPos != null) {
                axisAlignedBB = axisAlignedBB.union(tileEntityChest.adjacentChestZPos.getBlockType().getSelectedBoundingBox(RenderUtil.mc.theWorld, tileEntityChest.adjacentChestZPos.getPos()));
            }
        }
        axisAlignedBB = axisAlignedBB.offset(-RenderUtil.mc.getRenderManager().getRenderPosX(), -RenderUtil.mc.getRenderManager().getRenderPosY(), -RenderUtil.mc.getRenderManager().getRenderPosZ());
        if (other) {
            drawBoundingBox(axisAlignedBB);
        }
        else {
            drawBoundingBox(axisAlignedBB.expand(0.05, 0.05, 0.05));
        }
        if (lineWidth > 0.0f) {
            GL11.glLineWidth(lineWidth);
            GlStateManager.color(red, green, blue, lineAlpha);
            if (other) {
                drawOutlinedBoundingBox(axisAlignedBB);
            }
            else {
                drawOutlinedBoundingBox(axisAlignedBB.expand(0.05, 0.05, 0.05));
            }
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawBlockESP(final BlockPos blockPos, final float red, final float green, final float blue, final float alpha, final float lineAlpha, final float lineWidth) {
        GlStateManager.color(red, green, blue, alpha);
        final float x = (float)(blockPos.getX() - RenderUtil.mc.getRenderManager().getRenderPosX());
        final float y = (float)(blockPos.getY() - RenderUtil.mc.getRenderManager().getRenderPosY());
        final float z = (float)(blockPos.getZ() - RenderUtil.mc.getRenderManager().getRenderPosZ());
        final Block block = RenderUtil.mc.theWorld.getBlockState(blockPos).getBlock();
        drawBoundingBox(new AxisAlignedBB(x, y, z, x + block.getBlockBoundsMaxX(), y + block.getBlockBoundsMaxY(), z + block.getBlockBoundsMaxZ()));
        if (lineWidth > 0.0f) {
            GL11.glLineWidth(lineWidth);
            GlStateManager.color(red, green, blue, lineAlpha);
            drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + block.getBlockBoundsMaxX(), y + block.getBlockBoundsMaxY(), z + block.getBlockBoundsMaxZ()));
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawTracers(final EntityLivingBase entity, final float red, final float green, final float blue, final float alpha, final float lineWidth, final String s) {
        final float x = (float)(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * RenderUtil.mc.getTimer().renderPartialTicks - RenderUtil.mc.getRenderManager().getRenderPosX());
        final float y = (float)(entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * RenderUtil.mc.getTimer().renderPartialTicks - RenderUtil.mc.getRenderManager().getRenderPosY());
        final float z = (float)(entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * RenderUtil.mc.getTimer().renderPartialTicks - RenderUtil.mc.getRenderManager().getRenderPosZ());
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(red, green, blue, alpha);
        drawTracerLine(x, y, z, s);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawItemBoxESP(final EntityItem entityItem, final float red, final float green, final float blue, final float alpha) {
        final float x = (float)(entityItem.lastTickPosX + (entityItem.posX - entityItem.lastTickPosX) * RenderUtil.mc.getTimer().renderPartialTicks - RenderUtil.mc.getRenderManager().getRenderPosX());
        final float y = (float)(entityItem.lastTickPosY + (entityItem.posY - entityItem.lastTickPosY) * RenderUtil.mc.getTimer().renderPartialTicks - RenderUtil.mc.getRenderManager().getRenderPosY());
        final float z = (float)(entityItem.lastTickPosZ + (entityItem.posZ - entityItem.lastTickPosZ) * RenderUtil.mc.getTimer().renderPartialTicks - RenderUtil.mc.getRenderManager().getRenderPosZ());
        GL11.glColor4f(red, green, blue, alpha);
        drawItemBock(new AxisAlignedBB(x - 0.25, y + 0.05, z - 0.25, x + 0.25, y + 0.65, z + 0.253));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawItemBock(final AxisAlignedBB a) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((float)a.minX, (float)a.minY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.minY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.maxY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.maxY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.minY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.minY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.maxY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.maxY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.minY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.minY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.maxY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.maxY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.minY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.minY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.maxY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.maxY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.minY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.minY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.minY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.minY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.maxY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.maxY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.maxY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.maxY, (float)a.minZ).endVertex();
        worldrenderer.endVertex();
        tessellator.draw();
    }
    
    private static void drawTracerLine(final float x, final float y, final float z, final String s) {
        final float py = s.equals("Feet") ? 0.0f : RenderUtil.mc.thePlayer.getEyeHeight();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(1, DefaultVertexFormats.POSITION);
        worldrenderer.pos(0.0, py, 0.0).endVertex();
        worldrenderer.pos(x, y, z).endVertex();
        worldrenderer.endVertex();
        tessellator.draw();
    }
    
    private static void drawOutlinedBoundingBoxGL11(final AxisAlignedBB a) {
        GL11.glBegin(1);
        GL11.glVertex3d(a.minX, a.minY, a.minZ);
        GL11.glVertex3d(a.minX, a.minY, a.maxZ);
        GL11.glVertex3d(a.minX, a.minY, a.maxZ);
        GL11.glVertex3d(a.maxX, a.minY, a.maxZ);
        GL11.glVertex3d(a.maxX, a.minY, a.maxZ);
        GL11.glVertex3d(a.maxX, a.minY, a.minZ);
        GL11.glVertex3d(a.maxX, a.minY, a.minZ);
        GL11.glVertex3d(a.minX, a.minY, a.minZ);
        GL11.glVertex3d(a.minX, a.maxY, a.minZ);
        GL11.glVertex3d(a.minX, a.maxY, a.maxZ);
        GL11.glVertex3d(a.minX, a.maxY, a.maxZ);
        GL11.glVertex3d(a.maxX, a.maxY, a.maxZ);
        GL11.glVertex3d(a.maxX, a.maxY, a.maxZ);
        GL11.glVertex3d(a.maxX, a.maxY, a.minZ);
        GL11.glVertex3d(a.maxX, a.maxY, a.minZ);
        GL11.glVertex3d(a.minX, a.maxY, a.minZ);
        GL11.glVertex3d(a.minX, a.minY, a.minZ);
        GL11.glVertex3d(a.minX, a.maxY, a.minZ);
        GL11.glVertex3d(a.maxX, a.minY, a.minZ);
        GL11.glVertex3d(a.maxX, a.maxY, a.minZ);
        GL11.glVertex3d(a.maxX, a.minY, a.maxZ);
        GL11.glVertex3d(a.maxX, a.maxY, a.maxZ);
        GL11.glVertex3d(a.minX, a.minY, a.maxZ);
        GL11.glVertex3d(a.minX, a.maxY, a.maxZ);
        GL11.glEnd();
    }
    
    private static void drawBoundingBoxGL11(final AxisAlignedBB a) {
        GL11.glBegin(7);
        GL11.glVertex3d(a.minX, a.minY, a.minZ);
        GL11.glVertex3d(a.minX, a.minY, a.maxZ);
        GL11.glVertex3d(a.minX, a.maxY, a.maxZ);
        GL11.glVertex3d(a.minX, a.maxY, a.minZ);
        GL11.glVertex3d(a.minX, a.minY, a.maxZ);
        GL11.glVertex3d(a.maxX, a.minY, a.maxZ);
        GL11.glVertex3d(a.maxX, a.maxY, a.maxZ);
        GL11.glVertex3d(a.minX, a.maxY, a.maxZ);
        GL11.glVertex3d(a.maxX, a.minY, a.maxZ);
        GL11.glVertex3d(a.maxX, a.minY, a.minZ);
        GL11.glVertex3d(a.maxX, a.maxY, a.minZ);
        GL11.glVertex3d(a.maxX, a.maxY, a.maxZ);
        GL11.glVertex3d(a.maxX, a.minY, a.minZ);
        GL11.glVertex3d(a.minX, a.minY, a.minZ);
        GL11.glVertex3d(a.minX, a.maxY, a.minZ);
        GL11.glVertex3d(a.maxX, a.maxY, a.minZ);
        GL11.glVertex3d(a.minX, a.minY, a.minZ);
        GL11.glVertex3d(a.minX, a.minY, a.maxZ);
        GL11.glVertex3d(a.maxX, a.minY, a.maxZ);
        GL11.glVertex3d(a.maxX, a.minY, a.minZ);
        GL11.glVertex3d(a.minX, a.maxY, a.minZ);
        GL11.glVertex3d(a.minX, a.maxY, a.maxZ);
        GL11.glVertex3d(a.maxX, a.maxY, a.maxZ);
        GL11.glVertex3d(a.maxX, a.maxY, a.minZ);
        GL11.glEnd();
    }
    
    private static void otherDrawOutlinedBoundingBoxGL11(final Entity entity, final float x, final float y, final float z, double width, final double height) {
        width *= 1.5;
        final float yaw1 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 45.0f;
        float newYaw1;
        if (yaw1 < 0.0f) {
            newYaw1 = 0.0f;
            newYaw1 += 360.0f - Math.abs(yaw1);
        }
        else {
            newYaw1 = yaw1;
        }
        newYaw1 *= -1.0f;
        newYaw1 *= (float)0.017453292519943295;
        final float yaw2 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 135.0f;
        float newYaw2;
        if (yaw2 < 0.0f) {
            newYaw2 = 0.0f;
            newYaw2 += 360.0f - Math.abs(yaw2);
        }
        else {
            newYaw2 = yaw2;
        }
        newYaw2 *= -1.0f;
        newYaw2 *= (float)0.017453292519943295;
        final float yaw3 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 225.0f;
        float newYaw3;
        if (yaw3 < 0.0f) {
            newYaw3 = 0.0f;
            newYaw3 += 360.0f - Math.abs(yaw3);
        }
        else {
            newYaw3 = yaw3;
        }
        newYaw3 *= -1.0f;
        newYaw3 *= (float)0.017453292519943295;
        final float yaw4 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 315.0f;
        float newYaw4;
        if (yaw4 < 0.0f) {
            newYaw4 = 0.0f;
            newYaw4 += 360.0f - Math.abs(yaw4);
        }
        else {
            newYaw4 = yaw4;
        }
        newYaw4 *= -1.0f;
        newYaw4 *= (float)0.017453292519943295;
        final double x2 = Math.sin(newYaw1) * width + x;
        final double z2 = Math.cos(newYaw1) * width + z;
        final double x3 = Math.sin(newYaw2) * width + x;
        final double z3 = Math.cos(newYaw2) * width + z;
        final double x4 = Math.sin(newYaw3) * width + x;
        final double z4 = Math.cos(newYaw3) * width + z;
        final double x5 = Math.sin(newYaw4) * width + x;
        final double z5 = Math.cos(newYaw4) * width + z;
        final double y2 = y + height;
        GL11.glBegin(1);
        GL11.glVertex3d(x2, y, z2);
        GL11.glVertex3d(x3, y, z3);
        GL11.glVertex3d(x3, y, z3);
        GL11.glVertex3d(x4, y, z4);
        GL11.glVertex3d(x4, y, z4);
        GL11.glVertex3d(x5, y, z5);
        GL11.glVertex3d(x5, y, z5);
        GL11.glVertex3d(x2, y, z2);
        GL11.glVertex3d(x2, y2, z2);
        GL11.glVertex3d(x3, y2, z3);
        GL11.glVertex3d(x3, y2, z3);
        GL11.glVertex3d(x4, y2, z4);
        GL11.glVertex3d(x4, y2, z4);
        GL11.glVertex3d(x5, y2, z5);
        GL11.glVertex3d(x5, y2, z5);
        GL11.glVertex3d(x2, y2, z2);
        GL11.glVertex3d(x2, y, z2);
        GL11.glVertex3d(x2, y2, z2);
        GL11.glVertex3d(x3, y, z3);
        GL11.glVertex3d(x3, y2, z3);
        GL11.glVertex3d(x4, y, z4);
        GL11.glVertex3d(x4, y2, z4);
        GL11.glVertex3d(x5, y, z5);
        GL11.glVertex3d(x5, y2, z5);
        GL11.glEnd();
    }
    
    private static void otherDrawBoundingBoxGL11(final Entity entity, final float x, final float y, final float z, double width, final double height) {
        width *= 1.5;
        final float yaw1 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 45.0f;
        float newYaw1;
        if (yaw1 < 0.0f) {
            newYaw1 = 0.0f;
            newYaw1 += 360.0f - Math.abs(yaw1);
        }
        else {
            newYaw1 = yaw1;
        }
        newYaw1 *= -1.0f;
        newYaw1 *= (float)0.017453292519943295;
        final float yaw2 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 135.0f;
        float newYaw2;
        if (yaw2 < 0.0f) {
            newYaw2 = 0.0f;
            newYaw2 += 360.0f - Math.abs(yaw2);
        }
        else {
            newYaw2 = yaw2;
        }
        newYaw2 *= -1.0f;
        newYaw2 *= (float)0.017453292519943295;
        final float yaw3 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 225.0f;
        float newYaw3;
        if (yaw3 < 0.0f) {
            newYaw3 = 0.0f;
            newYaw3 += 360.0f - Math.abs(yaw3);
        }
        else {
            newYaw3 = yaw3;
        }
        newYaw3 *= -1.0f;
        newYaw3 *= (float)0.017453292519943295;
        final float yaw4 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 315.0f;
        float newYaw4;
        if (yaw4 < 0.0f) {
            newYaw4 = 0.0f;
            newYaw4 += 360.0f - Math.abs(yaw4);
        }
        else {
            newYaw4 = yaw4;
        }
        newYaw4 *= -1.0f;
        newYaw4 *= (float)0.017453292519943295;
        final double x2 = Math.sin(newYaw1) * width + x;
        final double z2 = Math.cos(newYaw1) * width + z;
        final double x3 = Math.sin(newYaw2) * width + x;
        final double z3 = Math.cos(newYaw2) * width + z;
        final double x4 = Math.sin(newYaw3) * width + x;
        final double z4 = Math.cos(newYaw3) * width + z;
        final double x5 = Math.sin(newYaw4) * width + x;
        final double z5 = Math.cos(newYaw4) * width + z;
        final double y2 = y + height;
        GL11.glBegin(7);
        GL11.glVertex3d(x2, y, z2);
        GL11.glVertex3d(x2, y2, z2);
        GL11.glVertex3d(x3, y2, z3);
        GL11.glVertex3d(x3, y, z3);
        GL11.glVertex3d(x3, y, z3);
        GL11.glVertex3d(x3, y2, z3);
        GL11.glVertex3d(x4, y2, z4);
        GL11.glVertex3d(x4, y, z4);
        GL11.glVertex3d(x4, y, z4);
        GL11.glVertex3d(x4, y2, z4);
        GL11.glVertex3d(x5, y2, z5);
        GL11.glVertex3d(x5, y, z5);
        GL11.glVertex3d(x5, y, z5);
        GL11.glVertex3d(x5, y2, z5);
        GL11.glVertex3d(x2, y2, z2);
        GL11.glVertex3d(x2, y, z2);
        GL11.glVertex3d(x2, y, z2);
        GL11.glVertex3d(x3, y, z3);
        GL11.glVertex3d(x4, y, z4);
        GL11.glVertex3d(x5, y, z5);
        GL11.glVertex3d(x2, y2, z2);
        GL11.glVertex3d(x3, y2, z3);
        GL11.glVertex3d(x4, y2, z4);
        GL11.glVertex3d(x5, y2, z5);
        GL11.glEnd();
    }
    
    private static void drawOutlinedBoundingBox(final AxisAlignedBB a) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos((float)a.minX, (float)a.minY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.minY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.maxY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.maxY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.minY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.minY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.maxY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.maxY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.minY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.minY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.minY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.minY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.maxY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.maxY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.maxY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.maxY, (float)a.minZ).endVertex();
        worldrenderer.endVertex();
        tessellator.draw();
    }
    
    public static void drawBoundingBox(final AxisAlignedBB a) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((float)a.minX, (float)a.minY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.minY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.maxY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.maxY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.minY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.minY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.maxY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.maxY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.minY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.minY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.maxY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.maxY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.minY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.minY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.maxY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.maxY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.minY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.minY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.minY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.minY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.maxY, (float)a.minZ).endVertex();
        worldrenderer.pos((float)a.minX, (float)a.maxY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.maxY, (float)a.maxZ).endVertex();
        worldrenderer.pos((float)a.maxX, (float)a.maxY, (float)a.minZ).endVertex();
        worldrenderer.endVertex();
        tessellator.draw();
    }
    
    public static void otherDrawOutlinedBoundingBox(final Entity entity, final float x, final float y, final float z, double width, final double height) {
        width *= 1.5;
        final float yaw1 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 45.0f;
        float newYaw1;
        if (yaw1 < 0.0f) {
            newYaw1 = 0.0f;
            newYaw1 += 360.0f - Math.abs(yaw1);
        }
        else {
            newYaw1 = yaw1;
        }
        newYaw1 *= -1.0f;
        newYaw1 *= (float)0.017453292519943295;
        final float yaw2 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 135.0f;
        float newYaw2;
        if (yaw2 < 0.0f) {
            newYaw2 = 0.0f;
            newYaw2 += 360.0f - Math.abs(yaw2);
        }
        else {
            newYaw2 = yaw2;
        }
        newYaw2 *= -1.0f;
        newYaw2 *= (float)0.017453292519943295;
        final float yaw3 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 225.0f;
        float newYaw3;
        if (yaw3 < 0.0f) {
            newYaw3 = 0.0f;
            newYaw3 += 360.0f - Math.abs(yaw3);
        }
        else {
            newYaw3 = yaw3;
        }
        newYaw3 *= -1.0f;
        newYaw3 *= (float)0.017453292519943295;
        final float yaw4 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 315.0f;
        float newYaw4;
        if (yaw4 < 0.0f) {
            newYaw4 = 0.0f;
            newYaw4 += 360.0f - Math.abs(yaw4);
        }
        else {
            newYaw4 = yaw4;
        }
        newYaw4 *= -1.0f;
        newYaw4 *= (float)0.017453292519943295;
        final float x2 = (float)(Math.sin(newYaw1) * width + x);
        final float z2 = (float)(Math.cos(newYaw1) * width + z);
        final float x3 = (float)(Math.sin(newYaw2) * width + x);
        final float z3 = (float)(Math.cos(newYaw2) * width + z);
        final float x4 = (float)(Math.sin(newYaw3) * width + x);
        final float z4 = (float)(Math.cos(newYaw3) * width + z);
        final float x5 = (float)(Math.sin(newYaw4) * width + x);
        final float z5 = (float)(Math.cos(newYaw4) * width + z);
        final float y2 = (float)(y + height);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x2, y, z2).endVertex();
        worldrenderer.pos(x2, y2, z2).endVertex();
        worldrenderer.pos(x3, y2, z3).endVertex();
        worldrenderer.pos(x3, y, z3).endVertex();
        worldrenderer.pos(x2, y, z2).endVertex();
        worldrenderer.pos(x5, y, z5).endVertex();
        worldrenderer.pos(x4, y, z4).endVertex();
        worldrenderer.pos(x4, y2, z4).endVertex();
        worldrenderer.pos(x5, y2, z5).endVertex();
        worldrenderer.pos(x5, y, z5).endVertex();
        worldrenderer.pos(x5, y2, z5).endVertex();
        worldrenderer.pos(x4, y2, z4).endVertex();
        worldrenderer.pos(x3, y2, z3).endVertex();
        worldrenderer.pos(x3, y, z3).endVertex();
        worldrenderer.pos(x4, y, z4).endVertex();
        worldrenderer.pos(x5, y, z5).endVertex();
        worldrenderer.pos(x5, y2, z5).endVertex();
        worldrenderer.pos(x2, y2, z2).endVertex();
        worldrenderer.pos(x2, y, z2).endVertex();
        worldrenderer.endVertex();
        tessellator.draw();
    }
    
    public static void otherDrawBoundingBox(final Entity entity, final float x, final float y, final float z, double width, final double height) {
        width *= 1.5;
        final float yaw1 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 45.0f;
        float newYaw1;
        if (yaw1 < 0.0f) {
            newYaw1 = 0.0f;
            newYaw1 += 360.0f - Math.abs(yaw1);
        }
        else {
            newYaw1 = yaw1;
        }
        newYaw1 *= -1.0f;
        newYaw1 *= (float)0.017453292519943295;
        final float yaw2 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 135.0f;
        float newYaw2;
        if (yaw2 < 0.0f) {
            newYaw2 = 0.0f;
            newYaw2 += 360.0f - Math.abs(yaw2);
        }
        else {
            newYaw2 = yaw2;
        }
        newYaw2 *= -1.0f;
        newYaw2 *= (float)0.017453292519943295;
        final float yaw3 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 225.0f;
        float newYaw3;
        if (yaw3 < 0.0f) {
            newYaw3 = 0.0f;
            newYaw3 += 360.0f - Math.abs(yaw3);
        }
        else {
            newYaw3 = yaw3;
        }
        newYaw3 *= -1.0f;
        newYaw3 *= (float)0.017453292519943295;
        final float yaw4 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 315.0f;
        float newYaw4;
        if (yaw4 < 0.0f) {
            newYaw4 = 0.0f;
            newYaw4 += 360.0f - Math.abs(yaw4);
        }
        else {
            newYaw4 = yaw4;
        }
        newYaw4 *= -1.0f;
        newYaw4 *= (float)0.017453292519943295;
        final float x2 = (float)(Math.sin(newYaw1) * width + x);
        final float z2 = (float)(Math.cos(newYaw1) * width + z);
        final float x3 = (float)(Math.sin(newYaw2) * width + x);
        final float z3 = (float)(Math.cos(newYaw2) * width + z);
        final float x4 = (float)(Math.sin(newYaw3) * width + x);
        final float z4 = (float)(Math.cos(newYaw3) * width + z);
        final float x5 = (float)(Math.sin(newYaw4) * width + x);
        final float z5 = (float)(Math.cos(newYaw4) * width + z);
        final float y2 = (float)(y + height);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x2, y, z2).endVertex();
        worldrenderer.pos(x2, y2, z2).endVertex();
        worldrenderer.pos(x3, y2, z3).endVertex();
        worldrenderer.pos(x3, y, z3).endVertex();
        worldrenderer.pos(x3, y, z3).endVertex();
        worldrenderer.pos(x3, y2, z3).endVertex();
        worldrenderer.pos(x4, y2, z4).endVertex();
        worldrenderer.pos(x4, y, z4).endVertex();
        worldrenderer.pos(x4, y, z4).endVertex();
        worldrenderer.pos(x4, y2, z4).endVertex();
        worldrenderer.pos(x5, y2, z5).endVertex();
        worldrenderer.pos(x5, y, z5).endVertex();
        worldrenderer.pos(x5, y, z5).endVertex();
        worldrenderer.pos(x5, y2, z5).endVertex();
        worldrenderer.pos(x2, y2, z2).endVertex();
        worldrenderer.pos(x2, y, z2).endVertex();
        worldrenderer.pos(x2, y, z2).endVertex();
        worldrenderer.pos(x3, y, z3).endVertex();
        worldrenderer.pos(x4, y, z4).endVertex();
        worldrenderer.pos(x5, y, z5).endVertex();
        worldrenderer.pos(x2, y2, z2).endVertex();
        worldrenderer.pos(x3, y2, z3).endVertex();
        worldrenderer.pos(x4, y2, z4).endVertex();
        worldrenderer.pos(x5, y2, z5).endVertex();
        worldrenderer.endVertex();
        tessellator.draw();
    }
    
    public static void drawCornerESP(final EntityLivingBase entity, final float red, final float green, final float blue) {
        final float x = (float)(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * RenderUtil.mc.getTimer().renderPartialTicks - RenderUtil.mc.getRenderManager().getRenderPosX());
        final float y = (float)(entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * RenderUtil.mc.getTimer().renderPartialTicks - RenderUtil.mc.getRenderManager().getRenderPosY());
        final float z = (float)(entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * RenderUtil.mc.getTimer().renderPartialTicks - RenderUtil.mc.getRenderManager().getRenderPosZ());
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + entity.height / 2.0f, z);
        GlStateManager.rotate(-RenderUtil.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-0.098, -0.098, 0.098);
        final float width = (float)(26.6 * entity.width / 2.0);
        final float height = (entity instanceof EntityPlayer) ? 12.0f : ((float)(11.98 * (entity.height / 2.0f)));
        GlStateManager.color(red, green, blue);
        draw3DRect(width, height - 1.0f, width - 4.0f, height);
        draw3DRect(-width, height - 1.0f, -width + 4.0f, height);
        draw3DRect(-width, height, -width + 1.0f, height - 4.0f);
        draw3DRect(width, height, width - 1.0f, height - 4.0f);
        draw3DRect(width, -height, width - 4.0f, -height + 1.0f);
        draw3DRect(-width, -height, -width + 4.0f, -height + 1.0f);
        draw3DRect(-width, -height + 1.0f, -width + 1.0f, -height + 4.0f);
        draw3DRect(width, -height + 1.0f, width - 1.0f, -height + 4.0f);
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        draw3DRect(width, height, width - 4.0f, height + 0.2f);
        draw3DRect(-width, height, -width + 4.0f, height + 0.2f);
        draw3DRect(-width - 0.2f, height + 0.2f, -width, height - 4.0f);
        draw3DRect(width + 0.2f, height + 0.2f, width, height - 4.0f);
        draw3DRect(width + 0.2f, -height, width - 4.0f, -height - 0.2f);
        draw3DRect(-width - 0.2f, -height, -width + 4.0f, -height - 0.2f);
        draw3DRect(-width - 0.2f, -height, -width, -height + 4.0f);
        draw3DRect(width + 0.2f, -height, width, -height + 4.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }
    
    public static void drawFake2DESP(final EntityLivingBase entity, final float red, final float green, final float blue) {
        final float x = (float)(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * RenderUtil.mc.getTimer().renderPartialTicks - RenderUtil.mc.getRenderManager().getRenderPosX());
        final float y = (float)(entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * RenderUtil.mc.getTimer().renderPartialTicks - RenderUtil.mc.getRenderManager().getRenderPosY());
        final float z = (float)(entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * RenderUtil.mc.getTimer().renderPartialTicks - RenderUtil.mc.getRenderManager().getRenderPosZ());
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + entity.height / 2.0f, z);
        GlStateManager.rotate(-RenderUtil.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-0.1, -0.1, 0.1);
        GlStateManager.color(red, green, blue);
        final float width = (float)(23.3 * entity.width / 2.0);
        final float height = (entity instanceof EntityPlayer) ? 12.0f : ((float)(11.98 * (entity.height / 2.0f)));
        draw3DRect(width, height, -width, height + 0.4f);
        draw3DRect(width, -height, -width, -height + 0.4f);
        draw3DRect(width, -height + 0.4f, width - 0.4f, height + 0.4f);
        draw3DRect(-width, -height + 0.4f, -width + 0.4f, height + 0.4f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }
    
    public static void drawCornerESP(final TileEntity tileEntity, final float red, final float green, final float blue) {
        double tx = 0.0;
        double tz = 0.0;
        if (tileEntity instanceof TileEntityChest) {
            final TileEntityChest tileEntityChest = (TileEntityChest)tileEntity;
            if (tileEntityChest.adjacentChestXNeg != null) {
                tx -= 0.5;
            }
            else if (tileEntityChest.adjacentChestZNeg != null) {
                tz -= 0.5;
            }
        }
        final float x = (float)(tileEntity.getPos().getX() + tx - RenderUtil.mc.getRenderManager().getRenderPosX());
        final float y = (float)(tileEntity.getPos().getY() - RenderUtil.mc.getRenderManager().getRenderPosY());
        final float z = (float)(tileEntity.getPos().getZ() + tz - RenderUtil.mc.getRenderManager().getRenderPosZ());
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 0.47, z + 0.5);
        GlStateManager.rotate(-RenderUtil.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(RenderUtil.mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-0.098, -0.098, 0.098);
        final float width = (tx != 0.0 || tz != 0.0) ? 14.0f : 8.0f;
        final float height = 7.5f;
        GlStateManager.color(red, green, blue);
        draw3DRect(width, height - 1.0f, width - 4.0f, height);
        draw3DRect(-width, height - 1.0f, -width + 4.0f, height);
        draw3DRect(-width, height, -width + 1.0f, height - 4.0f);
        draw3DRect(width, height, width - 1.0f, height - 4.0f);
        draw3DRect(width, -height, width - 4.0f, -height + 1.0f);
        draw3DRect(-width, -height, -width + 4.0f, -height + 1.0f);
        draw3DRect(-width, -height + 1.0f, -width + 1.0f, -height + 4.0f);
        draw3DRect(width, -height + 1.0f, width - 1.0f, -height + 4.0f);
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        draw3DRect(width, height, width - 4.0f, height + 0.2f);
        draw3DRect(-width, height, -width + 4.0f, height + 0.2f);
        draw3DRect(-width - 0.2f, height + 0.2f, -width, height - 4.0f);
        draw3DRect(width + 0.2f, height + 0.2f, width, height - 4.0f);
        draw3DRect(width + 0.2f, -height, width - 4.0f, -height - 0.2f);
        draw3DRect(-width - 0.2f, -height, -width + 4.0f, -height - 0.2f);
        draw3DRect(-width - 0.2f, -height, -width, -height + 4.0f);
        draw3DRect(width + 0.2f, -height, width, -height + 4.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }
    
    public static void drawFake2DESP(final TileEntity tileEntity, final float red, final float green, final float blue) {
        double tx = 0.0;
        double tz = 0.0;
        if (tileEntity instanceof TileEntityChest) {
            final TileEntityChest tileEntityChest = (TileEntityChest)tileEntity;
            if (tileEntityChest.adjacentChestXNeg != null) {
                tx -= 0.5;
            }
            else if (tileEntityChest.adjacentChestZNeg != null) {
                tz -= 0.5;
            }
        }
        final float x = (float)(tileEntity.getPos().getX() + tx - RenderUtil.mc.getRenderManager().getRenderPosX());
        final float y = (float)(tileEntity.getPos().getY() - RenderUtil.mc.getRenderManager().getRenderPosY());
        final float z = (float)(tileEntity.getPos().getZ() + tz - RenderUtil.mc.getRenderManager().getRenderPosZ());
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 0.47, z + 0.5);
        GlStateManager.rotate(-RenderUtil.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(RenderUtil.mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-0.1, -0.1, 0.1);
        GlStateManager.color(red, green, blue);
        final float width = (tx != 0.0 || tz != 0.0) ? 14.0f : 8.0f;
        final float height = 7.5f;
        draw3DRect(width, height, -width, height + 0.4f);
        draw3DRect(width, -height, -width, -height + 0.4f);
        draw3DRect(width, -height + 0.4f, width - 0.4f, height + 0.4f);
        draw3DRect(-width, -height + 0.4f, -width + 0.4f, height + 0.4f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }
    
    public static void draw3DRect(final float x1, final float y1, final float x2, final float y2) {
        GL11.glBegin(7);
        GL11.glVertex2d(x2, y1);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x1, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
    }
    
    public static void setScissorBox(final float x1, final float y1, final float x2, final float y2) {
        final ScaledResolution sr = new ScaledResolution(RenderUtil.mc);
        final float scaleFactor = (float)sr.getScaleFactor();
        GL11.glScissor((int)(x1 * scaleFactor), (int)((sr.getScaledHeight_double() - y2) * scaleFactor), (int)((x2 - x1) * scaleFactor), (int)((y2 - y1) * scaleFactor));
    }
}
