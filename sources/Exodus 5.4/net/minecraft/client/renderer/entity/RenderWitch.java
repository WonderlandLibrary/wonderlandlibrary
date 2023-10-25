/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItemWitch;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.util.ResourceLocation;

public class RenderWitch
extends RenderLiving<EntityWitch> {
    private static final ResourceLocation witchTextures = new ResourceLocation("textures/entity/witch.png");

    @Override
    protected ResourceLocation getEntityTexture(EntityWitch entityWitch) {
        return witchTextures;
    }

    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0f, 0.1875f, 0.0f);
    }

    public RenderWitch(RenderManager renderManager) {
        super(renderManager, new ModelWitch(0.0f), 0.5f);
        this.addLayer(new LayerHeldItemWitch(this));
    }

    @Override
    public void doRender(EntityWitch entityWitch, double d, double d2, double d3, float f, float f2) {
        ((ModelWitch)this.mainModel).field_82900_g = entityWitch.getHeldItem() != null;
        super.doRender(entityWitch, d, d2, d3, f, f2);
    }

    @Override
    protected void preRenderCallback(EntityWitch entityWitch, float f) {
        float f2 = 0.9375f;
        GlStateManager.scale(f2, f2, f2);
    }
}

