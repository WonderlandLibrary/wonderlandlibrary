package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.ResourceLocation;

public class RenderPigZombie
extends RenderBiped<EntityPigZombie> {
    private static final ResourceLocation ZOMBIE_PIGMAN_TEXTURE = new ResourceLocation("textures/entity/zombie_pigman.png");

    public RenderPigZombie(RenderManager renderManagerIn) {
        super(renderManagerIn, (ModelBiped)new ModelZombie(), 0.5f, 1.0f);
        this.addLayer((LayerRenderer)new LayerHeldItem((RendererLivingEntity)this));
        this.addLayer((LayerRenderer)new /* Unavailable Anonymous Inner Class!! */);
    }

    protected ResourceLocation getEntityTexture(EntityPigZombie entity) {
        return ZOMBIE_PIGMAN_TEXTURE;
    }
}
