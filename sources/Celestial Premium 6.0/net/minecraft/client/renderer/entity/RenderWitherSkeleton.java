/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;

public class RenderWitherSkeleton
extends RenderSkeleton {
    private static final ResourceLocation WITHER_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");

    public RenderWitherSkeleton(RenderManager p_i47188_1_) {
        super(p_i47188_1_);
    }

    @Override
    protected ResourceLocation getEntityTexture(AbstractSkeleton entity) {
        return WITHER_SKELETON_TEXTURES;
    }

    @Override
    protected void preRenderCallback(AbstractSkeleton entitylivingbaseIn, float partialTickTime) {
        GlStateManager.scale(1.2f, 1.2f, 1.2f);
    }
}

