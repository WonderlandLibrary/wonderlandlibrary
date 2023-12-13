/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.arithmo.module.impl.render;

import java.util.List;
import me.arithmo.Client;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventRender3D;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.impl.other.ChestStealer;
import me.arithmo.util.RenderingUtil;
import me.arithmo.util.render.GLUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class ChestESP
extends Module {
    public ChestESP(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventRender3D.class})
    public void onEvent(Event event) {
        EventRender3D e = (EventRender3D)event;
        for (Object o : ChestESP.mc.theWorld.loadedTileEntityList) {
            if (!(o instanceof TileEntityChest)) continue;
            TileEntityLockable storage = (TileEntityLockable)o;
            this.drawESPOnStorage(storage, storage.getPos().getX(), storage.getPos().getY(), storage.getPos().getZ());
        }
    }

    public void drawESPOnStorage(TileEntityLockable storage, double x, double y, double z) {
        assert (!storage.isLocked());
        TileEntityChest chest = (TileEntityChest)storage;
        Vec3 vec = new Vec3(0.0, 0.0, 0.0);
        Vec3 vec2 = new Vec3(0.0, 0.0, 0.0);
        if (chest.adjacentChestZNeg != null) {
            vec = new Vec3(x + 0.0625, y, z - 0.9375);
            vec2 = new Vec3(x + 0.9375, y + 0.875, z + 0.9375);
        } else if (chest.adjacentChestXNeg != null) {
            vec = new Vec3(x + 0.9375, y, z + 0.0625);
            vec2 = new Vec3(x - 0.9375, y + 0.875, z + 0.9375);
        } else if (chest.adjacentChestZNeg == null && chest.adjacentChestXNeg == null && chest.adjacentChestXPos == null && chest.adjacentChestZPos == null) {
            vec = new Vec3(x + 0.0625, y, z + 0.0625);
            vec2 = new Vec3(x + 0.9375, y + 0.875, z + 0.9375);
        } else {
            return;
        }
        GL11.glPushMatrix();
        RenderingUtil.pre3D();
        ChestESP.mc.entityRenderer.setupCameraTransform(ChestESP.mc.timer.renderPartialTicks, 2);
        if (chest.getChestType() == 1) {
            GL11.glColor4d((double)0.7, (double)0.1, (double)0.1, (double)0.5);
        } else if (chest.isEmpty && Client.getModuleManager().isEnabled(ChestStealer.class)) {
            GL11.glColor4d((double)0.4, (double)0.2, (double)0.2, (double)0.5);
        } else {
            GL11.glColor4d((double)0.7, (double)0.4, (double)0.0, (double)0.5);
        }
        RenderingUtil.drawBoundingBox(new AxisAlignedBB(vec.xCoord - RenderManager.renderPosX, vec.yCoord - RenderManager.renderPosY, vec.zCoord - RenderManager.renderPosZ, vec2.xCoord - RenderManager.renderPosX, vec2.yCoord - RenderManager.renderPosY, vec2.zCoord - RenderManager.renderPosZ));
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        RenderingUtil.post3D();
        GL11.glPopMatrix();
    }

    public void drawESP(double x, double y, double z, double r, double g, double b) {
        GL11.glPushMatrix();
        GLUtil.setGLCap(3042, true);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtil.setGLCap(2896, false);
        GLUtil.setGLCap(3553, false);
        GLUtil.setGLCap(2848, true);
        GLUtil.setGLCap(2929, false);
        GL11.glDepthMask((boolean)false);
        AxisAlignedBB boundingBox = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        GL11.glColor4d((double)r, (double)g, (double)b, (double)0.14000000059604645);
        RenderingUtil.drawBoundingBox(boundingBox.contract(0.025, 0.025, 0.025));
        GL11.glColor4d((double)r, (double)g, (double)b, (double)0.33000001311302185);
        GL11.glLineWidth((float)1.0f);
        RenderingUtil.drawOutlinedBoundingBox(boundingBox);
        GL11.glLineWidth((float)1.4f);
        RenderingUtil.drawLines(boundingBox);
        GL11.glLineWidth((float)2.0f);
        GLUtil.revertAllCaps();
        GL11.glDepthMask((boolean)true);
        GL11.glPopMatrix();
    }
}

