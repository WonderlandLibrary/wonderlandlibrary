package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;

public class LayerVillagerArmor
extends LayerBipedArmor {
    public LayerVillagerArmor(RendererLivingEntity<?> rendererIn) {
        super(rendererIn);
    }

    protected void initArmor() {
        this.modelLeggings = new ModelZombieVillager(0.5f, 0.0f, true);
        this.modelArmor = new ModelZombieVillager(1.0f, 0.0f, true);
    }
}
