// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.layers.LayerLlamaDecor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelLlama;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityLlama;

public class RenderLlama extends RenderLiving<EntityLlama>
{
    private static final ResourceLocation[] LLAMA_TEXTURES;
    
    public RenderLlama(final RenderManager p_i47203_1_) {
        super(p_i47203_1_, new ModelLlama(0.0f), 0.7f);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerLlamaDecor(this));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityLlama entity) {
        return RenderLlama.LLAMA_TEXTURES[entity.getVariant()];
    }
    
    static {
        LLAMA_TEXTURES = new ResourceLocation[] { new ResourceLocation("textures/entity/llama/llama_creamy.png"), new ResourceLocation("textures/entity/llama/llama_white.png"), new ResourceLocation("textures/entity/llama/llama_brown.png"), new ResourceLocation("textures/entity/llama/llama_gray.png") };
    }
}
