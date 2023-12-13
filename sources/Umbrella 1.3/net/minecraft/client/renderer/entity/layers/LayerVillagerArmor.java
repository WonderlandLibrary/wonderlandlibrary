/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;

public class LayerVillagerArmor
extends LayerBipedArmor {
    private static final String __OBFID = "CL_00002409";

    public LayerVillagerArmor(RendererLivingEntity p_i46108_1_) {
        super(p_i46108_1_);
    }

    @Override
    protected void func_177177_a() {
        this.field_177189_c = new ModelZombieVillager(0.5f, 0.0f, true);
        this.field_177186_d = new ModelZombieVillager(1.0f, 0.0f, true);
    }
}

