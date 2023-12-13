/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerWolfCollar;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;

public class RenderWolf
extends RenderLiving<EntityWolf> {
    private static final ResourceLocation wolfTextures = new ResourceLocation("textures/entity/wolf/wolf.png");
    private static final ResourceLocation tamedWolfTextures = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
    private static final ResourceLocation anrgyWolfTextures = new ResourceLocation("textures/entity/wolf/wolf_angry.png");

    public RenderWolf(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
        this.addLayer(new LayerWolfCollar(this));
    }

    @Override
    protected float handleRotationFloat(EntityWolf livingBase, float partialTicks) {
        return livingBase.getTailRotation();
    }

    @Override
    public void doRender(EntityWolf entity, double x2, double y2, double z2, float entityYaw, float partialTicks) {
        if (entity.isWolfWet()) {
            float f2 = entity.getBrightness(partialTicks) * entity.getShadingWhileWet(partialTicks);
            GlStateManager.color(f2, f2, f2);
        }
        super.doRender(entity, x2, y2, z2, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWolf entity) {
        return entity.isTamed() ? tamedWolfTextures : (entity.isAngry() ? anrgyWolfTextures : wolfTextures);
    }
}

