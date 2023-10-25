/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerSlimeGel;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;

public class RenderSlime
extends RenderLiving<EntitySlime> {
    private static final ResourceLocation SLIME_TEXTURES = new ResourceLocation("textures/entity/slime/slime.png");

    public RenderSlime(RenderManager p_i47193_1_) {
        super(p_i47193_1_, new ModelSlime(16), 0.25f);
        this.addLayer(new LayerSlimeGel(this));
    }

    @Override
    public void doRender(EntitySlime entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.shadowSize = 0.25f * (float)entity.getSlimeSize();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void preRenderCallback(EntitySlime entitylivingbaseIn, float partialTickTime) {
        float f = 0.999f;
        GlStateManager.scale(0.999f, 0.999f, 0.999f);
        float f1 = entitylivingbaseIn.getSlimeSize();
        float f2 = (entitylivingbaseIn.prevSquishFactor + (entitylivingbaseIn.squishFactor - entitylivingbaseIn.prevSquishFactor) * partialTickTime) / (f1 * 0.5f + 1.0f);
        float f3 = 1.0f / (f2 + 1.0f);
        GlStateManager.scale(f3 * f1, 1.0f / f3 * f1, f3 * f1);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySlime entity) {
        return SLIME_TEXTURES;
    }
}

