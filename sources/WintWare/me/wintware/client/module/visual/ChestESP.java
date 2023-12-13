/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.wintware.client.module.visual;

import java.awt.Color;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event3D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ChestESP
extends Module {
    public ChestESP() {
        super("ChestESP", Category.Visuals);
    }

    @EventTarget
    public void onEvent(Event3D e) {
        for (Object o : ChestESP.mc.world.loadedTileEntityList) {
            if (o instanceof TileEntityChest) {
                TileEntityLockable storage = (TileEntityLockable)o;
                this.drawESPOnStorage(storage, storage.getPos().getX(), storage.getPos().getY(), storage.getPos().getZ());
            }
            for (Object o1 : ChestESP.mc.world.loadedTileEntityList) {
                if (!(o instanceof TileEntityEnderChest)) continue;
                TileEntityEnderChest storage1 = (TileEntityEnderChest)o;
                this.drawChestEspOnStorage(storage1, storage1.getPos().getX(), storage1.getPos().getY(), storage1.getPos().getZ());
            }
        }
    }

    private void drawESPOnStorage(TileEntityLockable storage, double x, double y, double z) {
        if (!storage.isLocked()) {
            Vec3d vec2;
            Vec3d vec;
            TileEntityChest chest = (TileEntityChest)storage;
            if (chest.adjacentChestZNeg != null) {
                vec = new Vec3d(x + 0.0625, y, z - 0.9375);
                vec2 = new Vec3d(x + 0.9375, y + 0.875, z + 0.9375);
            } else if (chest.adjacentChestXNeg != null) {
                vec = new Vec3d(x + 0.9375, y, z + 0.0625);
                vec2 = new Vec3d(x - 0.9375, y + 0.875, z + 0.9375);
            } else {
                if (chest.adjacentChestXPos != null || chest.adjacentChestZPos != null) {
                    return;
                }
                vec = new Vec3d(x + 0.0625, y, z + 0.0625);
                vec2 = new Vec3d(x + 0.9375, y + 0.875, z + 0.9375);
            }
            GL11.glPushMatrix();
            GlStateManager.disableDepth();
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
            ChestESP.mc.entityRenderer.setupCameraTransform(ChestESP.mc.timer.renderPartialTicks, 2);
            double rainbowState = Math.ceil(System.currentTimeMillis() + 300L + 300L) / 15.0;
            Color chamsColor = Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), 0.4f, 1.0f);
            GL11.glColor4d((float)chamsColor.getRed() / 255.0f, (float)chamsColor.getGreen() / 255.0f, (float)chamsColor.getBlue() / 255.0f, (float)chamsColor.getAlpha() / 255.0f);
            mc.getRenderManager();
            mc.getRenderManager();
            mc.getRenderManager();
            mc.getRenderManager();
            mc.getRenderManager();
            mc.getRenderManager();
            RenderUtil.drawBoundingBox(new AxisAlignedBB(vec.xCoord - RenderManager.renderPosX, vec.yCoord - RenderManager.renderPosY, vec.zCoord - RenderManager.renderPosZ, vec2.xCoord - RenderManager.renderPosX, vec2.yCoord - RenderManager.renderPosY, vec2.zCoord - RenderManager.renderPosZ));
            GlStateManager.enableDepth();
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    private void drawChestEspOnStorage(TileEntityEnderChest storage, double x, double y, double z) {
        TileEntityEnderChest chest = storage;
        Vec3d vec = new Vec3d(x + 0.0625, y, z - 0.9375);
        Vec3d vec2 = new Vec3d(x + 0.9375, y + 0.875, z + 0.9375);
        vec = new Vec3d(x + 0.9375, y, z + 0.0625);
        vec2 = new Vec3d(x - 0.9375, y + 0.875, z + 0.9375);
        vec = new Vec3d(x + 0.0625, y, z + 0.0625);
        vec2 = new Vec3d(x + 0.9375, y + 0.875, z + 0.9375);
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        ChestESP.mc.entityRenderer.setupCameraTransform(ChestESP.mc.timer.renderPartialTicks, 2);
        double rainbowState = Math.ceil(System.currentTimeMillis() + 300L + 300L) / 15.0;
        rainbowState %= 360.0;
        Color chamsColor = new Color(203, 1, 208);
        GL11.glColor4d((float)chamsColor.getRed() / 255.0f, (float)chamsColor.getGreen() / 255.0f, (float)chamsColor.getBlue() / 255.0f, (float)chamsColor.getAlpha() / 255.0f);
        mc.getRenderManager();
        mc.getRenderManager();
        mc.getRenderManager();
        mc.getRenderManager();
        mc.getRenderManager();
        mc.getRenderManager();
        RenderUtil.drawBoundingBox(new AxisAlignedBB(vec.xCoord - RenderManager.renderPosX, vec.yCoord - RenderManager.renderPosY, vec.zCoord - RenderManager.renderPosZ, vec2.xCoord - RenderManager.renderPosX, vec2.yCoord - RenderManager.renderPosY, vec2.zCoord - RenderManager.renderPosZ));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
}

