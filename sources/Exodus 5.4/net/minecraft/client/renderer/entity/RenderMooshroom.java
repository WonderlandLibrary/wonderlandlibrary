/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.util.ResourceLocation;

public class RenderMooshroom
extends RenderLiving<EntityMooshroom> {
    private static final ResourceLocation mooshroomTextures = new ResourceLocation("textures/entity/cow/mooshroom.png");

    @Override
    protected ResourceLocation getEntityTexture(EntityMooshroom entityMooshroom) {
        return mooshroomTextures;
    }

    public RenderMooshroom(RenderManager renderManager, ModelBase modelBase, float f) {
        super(renderManager, modelBase, f);
        this.addLayer(new LayerMooshroomMushroom(this));
    }
}

