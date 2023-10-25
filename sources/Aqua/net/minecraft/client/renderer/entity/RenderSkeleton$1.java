package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;

class RenderSkeleton.1
extends LayerBipedArmor {
    RenderSkeleton.1(RendererLivingEntity rendererIn) {
        super(rendererIn);
    }

    protected void initArmor() {
        this.modelLeggings = new ModelSkeleton(0.5f, true);
        this.modelArmor = new ModelSkeleton(1.0f, true);
    }
}
