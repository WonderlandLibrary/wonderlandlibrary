/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelMagmaCube;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.util.ResourceLocation;

public class RenderMagmaCube
extends RenderLiving<EntityMagmaCube> {
    private static final ResourceLocation magmaCubeTextures = new ResourceLocation("textures/entity/slime/magmacube.png");

    @Override
    protected ResourceLocation getEntityTexture(EntityMagmaCube entityMagmaCube) {
        return magmaCubeTextures;
    }

    public RenderMagmaCube(RenderManager renderManager) {
        super(renderManager, new ModelMagmaCube(), 0.25f);
    }

    @Override
    protected void preRenderCallback(EntityMagmaCube entityMagmaCube, float f) {
        int n = entityMagmaCube.getSlimeSize();
        float f2 = (entityMagmaCube.prevSquishFactor + (entityMagmaCube.squishFactor - entityMagmaCube.prevSquishFactor) * f) / ((float)n * 0.5f + 1.0f);
        float f3 = 1.0f / (f2 + 1.0f);
        float f4 = n;
        GlStateManager.scale(f3 * f4, 1.0f / f3 * f4, f3 * f4);
    }
}

