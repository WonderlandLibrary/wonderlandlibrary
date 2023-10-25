/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;

public class RenderSheep
extends RenderLiving<EntitySheep> {
    private static final ResourceLocation SHEARED_SHEEP_TEXTURES = new ResourceLocation("textures/entity/sheep/sheep.png");

    public RenderSheep(RenderManager p_i47195_1_) {
        super(p_i47195_1_, new ModelSheep2(), 0.7f);
        this.addLayer(new LayerSheepWool(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySheep entity) {
        return SHEARED_SHEEP_TEXTURES;
    }
}

