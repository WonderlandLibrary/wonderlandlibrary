// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.layers.LayerSaddle;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityPig;

public class RenderPig extends RenderLiving<EntityPig>
{
    private static final ResourceLocation PIG_TEXTURES;
    
    public RenderPig(final RenderManager p_i47198_1_) {
        super(p_i47198_1_, new ModelPig(), 0.7f);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerSaddle(this));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityPig entity) {
        return RenderPig.PIG_TEXTURES;
    }
    
    static {
        PIG_TEXTURES = new ResourceLocation("textures/entity/pig/pig.png");
    }
}
