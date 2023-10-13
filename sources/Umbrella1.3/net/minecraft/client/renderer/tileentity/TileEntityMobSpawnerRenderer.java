/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;

public class TileEntityMobSpawnerRenderer
extends TileEntitySpecialRenderer {
    private static final String __OBFID = "CL_00000968";

    public void func_180539_a(TileEntityMobSpawner p_180539_1_, double p_180539_2_, double p_180539_4_, double p_180539_6_, float p_180539_8_, int p_180539_9_) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_180539_2_ + 0.5f, (float)p_180539_4_, (float)p_180539_6_ + 0.5f);
        TileEntityMobSpawnerRenderer.func_147517_a(p_180539_1_.getSpawnerBaseLogic(), p_180539_2_, p_180539_4_, p_180539_6_, p_180539_8_);
        GlStateManager.popMatrix();
    }

    public static void func_147517_a(MobSpawnerBaseLogic p_147517_0_, double p_147517_1_, double p_147517_3_, double p_147517_5_, float p_147517_7_) {
        Entity var8 = p_147517_0_.func_180612_a(p_147517_0_.getSpawnerWorld());
        if (var8 != null) {
            float var9 = 0.4375f;
            GlStateManager.translate(0.0f, 0.4f, 0.0f);
            GlStateManager.rotate((float)(p_147517_0_.func_177223_e() + (p_147517_0_.func_177222_d() - p_147517_0_.func_177223_e()) * (double)p_147517_7_) * 10.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-30.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.4f, 0.0f);
            GlStateManager.scale(var9, var9, var9);
            var8.setLocationAndAngles(p_147517_1_, p_147517_3_, p_147517_5_, 0.0f, 0.0f);
            Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(var8, 0.0, 0.0, 0.0, 0.0f, p_147517_7_);
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity p_180535_1_, double p_180535_2_, double p_180535_4_, double p_180535_6_, float p_180535_8_, int p_180535_9_) {
        this.func_180539_a((TileEntityMobSpawner)p_180535_1_, p_180535_2_, p_180535_4_, p_180535_6_, p_180535_8_, p_180535_9_);
    }
}

