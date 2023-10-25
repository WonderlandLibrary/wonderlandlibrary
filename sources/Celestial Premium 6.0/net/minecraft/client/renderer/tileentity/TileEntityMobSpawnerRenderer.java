/*
 * Decompiled with CFR 0.150.
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
    public void func_192841_a(TileEntityMobSpawner p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_192841_2_ + 0.5f, (float)p_192841_4_, (float)p_192841_6_ + 0.5f);
        TileEntityMobSpawnerRenderer.renderMob(p_192841_1_.getSpawnerBaseLogic(), p_192841_2_, p_192841_4_, p_192841_6_, p_192841_8_);
        GlStateManager.popMatrix();
    }

    public static void renderMob(MobSpawnerBaseLogic mobSpawnerLogic, double posX, double posY, double posZ, float partialTicks) {
        Entity entity = mobSpawnerLogic.getCachedEntity();
        if (entity != null) {
            float f = 0.53125f;
            float f1 = Math.max(entity.width, entity.height);
            if ((double)f1 > 1.0) {
                f /= f1;
            }
            GlStateManager.translate(0.0f, 0.4f, 0.0f);
            GlStateManager.rotate((float)(mobSpawnerLogic.getPrevMobRotation() + (mobSpawnerLogic.getMobRotation() - mobSpawnerLogic.getPrevMobRotation()) * (double)partialTicks) * 10.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.2f, 0.0f);
            GlStateManager.rotate(-30.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(f, f, f);
            entity.setLocationAndAngles(posX, posY, posZ, 0.0f, 0.0f);
            Minecraft.getMinecraft().getRenderManager().doRenderEntity(entity, 0.0, 0.0, 0.0, 0.0f, partialTicks, false);
        }
    }
}

