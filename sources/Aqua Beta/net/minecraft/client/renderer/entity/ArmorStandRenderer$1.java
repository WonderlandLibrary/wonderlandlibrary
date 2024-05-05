package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelArmorStandArmor;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;

class ArmorStandRenderer.1
extends LayerBipedArmor {
    ArmorStandRenderer.1(RendererLivingEntity rendererIn) {
        super(rendererIn);
    }

    protected void initArmor() {
        this.modelLeggings = new ModelArmorStandArmor(0.5f);
        this.modelArmor = new ModelArmorStandArmor(1.0f);
    }
}
