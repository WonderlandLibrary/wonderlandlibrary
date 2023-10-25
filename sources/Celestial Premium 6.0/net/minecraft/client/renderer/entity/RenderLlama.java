/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelLlama;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerLlamaDecor;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.util.ResourceLocation;

public class RenderLlama
extends RenderLiving<EntityLlama> {
    private static final ResourceLocation[] field_191350_a = new ResourceLocation[]{new ResourceLocation("textures/entity/llama/llama_creamy.png"), new ResourceLocation("textures/entity/llama/llama_white.png"), new ResourceLocation("textures/entity/llama/llama_brown.png"), new ResourceLocation("textures/entity/llama/llama_gray.png")};

    public RenderLlama(RenderManager p_i47203_1_) {
        super(p_i47203_1_, new ModelLlama(0.0f), 0.7f);
        this.addLayer(new LayerLlamaDecor(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityLlama entity) {
        return field_191350_a[entity.func_190719_dM()];
    }
}

