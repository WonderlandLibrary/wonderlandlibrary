/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.util.ResourceLocation;

public class RenderGhast
extends RenderLiving {
    private static final ResourceLocation ghastTextures = new ResourceLocation("textures/entity/ghast/ghast.png");
    private static final ResourceLocation ghastShootingTextures = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");
    private static final String __OBFID = "CL_00000997";

    public RenderGhast(RenderManager p_i46174_1_) {
        super(p_i46174_1_, new ModelGhast(), 0.5f);
    }

    protected ResourceLocation func_180576_a(EntityGhast p_180576_1_) {
        return p_180576_1_.func_110182_bF() ? ghastShootingTextures : ghastTextures;
    }

    protected void preRenderCallback(EntityGhast p_77041_1_, float p_77041_2_) {
        float var3 = 1.0f;
        float var4 = (8.0f + var3) / 2.0f;
        float var5 = (8.0f + 1.0f / var3) / 2.0f;
        GlStateManager.scale(var5, var4, var5);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {
        this.preRenderCallback((EntityGhast)p_77041_1_, p_77041_2_);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.func_180576_a((EntityGhast)p_110775_1_);
    }
}

