/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.ResourceLocation;

public class RenderPigZombie
extends RenderBiped<EntityPigZombie> {
    private static final ResourceLocation ZOMBIE_PIGMAN_TEXTURE = new ResourceLocation("textures/entity/zombie_pigman.png");

    public RenderPigZombie(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelZombie(), 0.5f);
        this.addLayer(new LayerBipedArmor(this){

            @Override
            protected void initArmor() {
                this.modelLeggings = new ModelZombie(0.5f, true);
                this.modelArmor = new ModelZombie(1.0f, true);
            }
        });
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityPigZombie entity) {
        return ZOMBIE_PIGMAN_TEXTURE;
    }
}

