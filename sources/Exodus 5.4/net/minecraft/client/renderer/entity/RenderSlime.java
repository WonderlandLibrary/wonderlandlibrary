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

    public RenderSlime(RenderManager renderManager, ModelBase modelBase, float f) {
        super(renderManager, modelBase, f);
        this.addLayer(new LayerSlimeGel(this));
    }

    @Override
    protected void preRenderCallback(EntitySlime entitySlime, float f) {
        float f2 = entitySlime.getSlimeSize();
        float f3 = (entitySlime.prevSquishFactor + (entitySlime.squishFactor - entitySlime.prevSquishFactor) * f) / (f2 * 0.5f + 1.0f);
        float f4 = 1.0f / (f3 + 1.0f);
        GlStateManager.scale(f4 * f2, 1.0f / f4 * f2, f4 * f2);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySlime entitySlime) {
        return slimeTextures;
    }

    @Override
    public void doRender(EntitySlime entitySlime, double d, double d2, double d3, float f, float f2) {
        this.shadowSize = 0.25f * (float)entitySlime.getSlimeSize();
        super.doRender(entitySlime, d, d2, d3, f, f2);
    }
}

