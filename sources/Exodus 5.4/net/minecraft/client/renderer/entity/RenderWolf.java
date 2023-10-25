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
    private static final ResourceLocation anrgyWolfTextures;
    private static final ResourceLocation wolfTextures;
    private static final ResourceLocation tamedWolfTextures;

    @Override
    protected float handleRotationFloat(EntityWolf entityWolf, float f) {
        return entityWolf.getTailRotation();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWolf entityWolf) {
        return entityWolf.isTamed() ? tamedWolfTextures : (entityWolf.isAngry() ? anrgyWolfTextures : wolfTextures);
    }

    static {
        wolfTextures = new ResourceLocation("textures/entity/wolf/wolf.png");
        tamedWolfTextures = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
        anrgyWolfTextures = new ResourceLocation("textures/entity/wolf/wolf_angry.png");
    }

    @Override
    public void doRender(EntityWolf entityWolf, double d, double d2, double d3, float f, float f2) {
        if (entityWolf.isWolfWet()) {
            float f3 = entityWolf.getBrightness(f2) * entityWolf.getShadingWhileWet(f2);
            GlStateManager.color(f3, f3, f3);
        }
        super.doRender(entityWolf, d, d2, d3, f, f2);
    }

    public RenderWolf(RenderManager renderManager, ModelBase modelBase, float f) {
        super(renderManager, modelBase, f);
        this.addLayer(new LayerWolfCollar(this));
    }
}

