/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.client.renderer.entity.layers.LayerStrayClothing;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;

public class RenderStray
extends RenderSkeleton {
    private static final ResourceLocation STRAY_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/stray.png");

    public RenderStray(RenderManager p_i47191_1_) {
        super(p_i47191_1_);
        this.addLayer(new LayerStrayClothing(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(AbstractSkeleton entity) {
        return STRAY_SKELETON_TEXTURES;
    }
}

