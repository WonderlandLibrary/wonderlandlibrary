/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.util.ResourceLocation;

public class RenderGiantZombie
extends RenderLiving<EntityGiantZombie> {
    private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
    private final float scale;

    public RenderGiantZombie(RenderManager p_i47206_1_, float p_i47206_2_) {
        super(p_i47206_1_, new ModelZombie(), 0.5f * p_i47206_2_);
        this.scale = p_i47206_2_;
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBipedArmor(this){

            @Override
            protected void initArmor() {
                this.modelLeggings = new ModelZombie(0.5f, true);
                this.modelArmor = new ModelZombie(1.0f, true);
            }
        });
    }

    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0f, 0.1875f, 0.0f);
    }

    @Override
    protected void preRenderCallback(EntityGiantZombie entitylivingbaseIn, float partialTickTime) {
        GlStateManager.scale(this.scale, this.scale, this.scale);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityGiantZombie entity) {
        return ZOMBIE_TEXTURES;
    }
}

