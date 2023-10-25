/*
 * Decompiled with CFR 0.150.
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
    private static final ResourceLocation GHAST_TEXTURES = new ResourceLocation("textures/entity/ghast/ghast.png");
    private static final ResourceLocation GHAST_SHOOTING_TEXTURES = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");

    public RenderGhast(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelGhast(), 0.5f);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityGhast entity) {
        return entity.isAttacking() ? GHAST_SHOOTING_TEXTURES : GHAST_TEXTURES;
    }

    @Override
    protected void preRenderCallback(EntityGhast entitylivingbaseIn, float partialTickTime) {
        float f = 1.0f;
        float f1 = 4.5f;
        float f2 = 4.5f;
        GlStateManager.scale(4.5f, 4.5f, 4.5f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
}

