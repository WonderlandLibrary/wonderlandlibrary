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
    public static void renderMob(MobSpawnerBaseLogic mobSpawnerBaseLogic, double d, double d2, double d3, float f) {
        Entity entity = mobSpawnerBaseLogic.func_180612_a(mobSpawnerBaseLogic.getSpawnerWorld());
        if (entity != null) {
            float f2 = 0.4375f;
            GlStateManager.translate(0.0f, 0.4f, 0.0f);
            GlStateManager.rotate((float)(mobSpawnerBaseLogic.getPrevMobRotation() + (mobSpawnerBaseLogic.getMobRotation() - mobSpawnerBaseLogic.getPrevMobRotation()) * (double)f) * 10.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-30.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.4f, 0.0f);
            GlStateManager.scale(f2, f2, f2);
            entity.setLocationAndAngles(d, d2, d3, 0.0f, 0.0f);
            Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(entity, 0.0, 0.0, 0.0, 0.0f, f);
        }
    }

    @Override
    public void renderTileEntityAt(TileEntityMobSpawner tileEntityMobSpawner, double d, double d2, double d3, float f, int n) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)d + 0.5f, (float)d2, (float)d3 + 0.5f);
        TileEntityMobSpawnerRenderer.renderMob(tileEntityMobSpawner.getSpawnerBaseLogic(), d, d2, d3, f);
        GlStateManager.popMatrix();
    }
}

