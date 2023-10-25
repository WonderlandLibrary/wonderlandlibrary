/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;

public class RenderHusk
extends RenderZombie {
    private static final ResourceLocation HUSK_ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/husk.png");

    public RenderHusk(RenderManager p_i47204_1_) {
        super(p_i47204_1_);
    }

    @Override
    protected void preRenderCallback(EntityZombie entitylivingbaseIn, float partialTickTime) {
        float f = 1.0625f;
        GlStateManager.scale(1.0625f, 1.0625f, 1.0625f);
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityZombie entity) {
        return HUSK_ZOMBIE_TEXTURES;
    }
}

