/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerSlimeGel;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;

public class RenderSlime
extends RenderLiving<EntitySlime> {
    private static final ResourceLocation slimeTextures = new ResourceLocation("textures/entity/slime/slime.png");

    public RenderSlime(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
        this.addLayer(new LayerSlimeGel(this));
    }

    @Override
    public void doRender(EntitySlime entity, double x2, double y2, double z2, float entityYaw, float partialTicks) {
        this.shadowSize = 0.25f * (float)entity.getSlimeSize();
        super.doRender(entity, x2, y2, z2, entityYaw, partialTicks);
    }

    @Override
    protected void preRenderCallback(EntitySlime entitylivingbaseIn, float partialTickTime) {
        float f2 = entitylivingbaseIn.getSlimeSize();
        float f1 = (entitylivingbaseIn.prevSquishFactor + (entitylivingbaseIn.squishFactor - entitylivingbaseIn.prevSquishFactor) * partialTickTime) / (f2 * 0.5f + 1.0f);
        float f22 = 1.0f / (f1 + 1.0f);
        GlStateManager.scale(f22 * f2, 1.0f / f22 * f2, f22 * f2);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySlime entity) {
        return slimeTextures;
    }
}

