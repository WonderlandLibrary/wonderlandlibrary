/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;

public class TileEntityMobSpawnerRenderer
extends TileEntitySpecialRenderer<TileEntityMobSpawner> {
    @Override
    public void renderTileEntityAt(TileEntityMobSpawner te, double x2, double y2, double z2, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x2 + 0.5f, (float)y2, (float)z2 + 0.5f);
        TileEntityMobSpawnerRenderer.renderMob(te.getSpawnerBaseLogic(), x2, y2, z2, partialTicks);
        GlStateManager.popMatrix();
    }

    public static void renderMob(MobSpawnerBaseLogic mobSpawnerLogic, double posX, double posY, double posZ, float partialTicks) {
        Entity entity = mobSpawnerLogic.func_180612_a(mobSpawnerLogic.getSpawnerWorld());
        if (entity != null) {
            float f2 = 0.4375f;
            GlStateManager.translate(0.0f, 0.4f, 0.0f);
            GlStateManager.rotate((float)(mobSpawnerLogic.getPrevMobRotation() + (mobSpawnerLogic.getMobRotation() - mobSpawnerLogic.getPrevMobRotation()) * (double)partialTicks) * 10.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-30.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.4f, 0.0f);
            GlStateManager.scale(f2, f2, f2);
            entity.setLocationAndAngles(posX, posY, posZ, 0.0f, 0.0f);
            Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(entity, 0.0, 0.0, 0.0, 0.0f, partialTicks);
        }
    }
}

