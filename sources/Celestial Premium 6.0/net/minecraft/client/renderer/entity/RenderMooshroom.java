/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelCow;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.util.ResourceLocation;

public class RenderMooshroom
extends RenderLiving<EntityMooshroom> {
    private static final ResourceLocation MOOSHROOM_TEXTURES = new ResourceLocation("textures/entity/cow/mooshroom.png");

    public RenderMooshroom(RenderManager p_i47200_1_) {
        super(p_i47200_1_, new ModelCow(), 0.7f);
        this.addLayer(new LayerMooshroomMushroom(this));
    }

    @Override
    public ModelCow getMainModel() {
        return (ModelCow)super.getMainModel();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMooshroom entity) {
        return MOOSHROOM_TEXTURES;
    }
}

