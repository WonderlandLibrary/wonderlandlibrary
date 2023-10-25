/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.util.ResourceLocation;

public class RenderGhast
extends RenderLiving<EntityGhast> {
    private static final ResourceLocation ghastShootingTextures;
    private static final ResourceLocation ghastTextures;

    @Override
    protected ResourceLocation getEntityTexture(EntityGhast entityGhast) {
        return entityGhast.isAttacking() ? ghastShootingTextures : ghastTextures;
    }

    public RenderGhast(RenderManager renderManager) {
        super(renderManager, new ModelGhast(), 0.5f);
    }

    static {
        ghastTextures = new ResourceLocation("textures/entity/ghast/ghast.png");
        ghastShootingTextures = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");
    }

    @Override
    protected void preRenderCallback(EntityGhast entityGhast, float f) {
        float f2 = 1.0f;
        float f3 = (8.0f + f2) / 2.0f;
        float f4 = (8.0f + 1.0f / f2) / 2.0f;
        GlStateManager.scale(f4, f3, f4);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
}

