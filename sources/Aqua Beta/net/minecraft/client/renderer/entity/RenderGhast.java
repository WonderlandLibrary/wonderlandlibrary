package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.util.ResourceLocation;

public class RenderGhast
extends RenderLiving<EntityGhast> {
    private static final ResourceLocation ghastTextures = new ResourceLocation("textures/entity/ghast/ghast.png");
    private static final ResourceLocation ghastShootingTextures = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");

    public RenderGhast(RenderManager renderManagerIn) {
        super(renderManagerIn, (ModelBase)new ModelGhast(), 0.5f);
    }

    protected ResourceLocation getEntityTexture(EntityGhast entity) {
        return entity.isAttacking() ? ghastShootingTextures : ghastTextures;
    }

    protected void preRenderCallback(EntityGhast entitylivingbaseIn, float partialTickTime) {
        float f = 1.0f;
        float f1 = (8.0f + f) / 2.0f;
        float f2 = (8.0f + 1.0f / f) / 2.0f;
        GlStateManager.scale((float)f2, (float)f1, (float)f2);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }
}
