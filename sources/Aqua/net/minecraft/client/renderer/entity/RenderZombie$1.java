package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;

class RenderZombie.1
extends LayerBipedArmor {
    RenderZombie.1(RendererLivingEntity rendererIn) {
        super(rendererIn);
    }

    protected void initArmor() {
        this.modelLeggings = new ModelZombie(0.5f, true);
        this.modelArmor = new ModelZombie(1.0f, true);
    }
}
