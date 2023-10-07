// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerSaddle;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

public class RenderPig extends RenderLiving
{
    private static final ResourceLocation pigTextures;
    private static final String __OBFID = "CL_00001019";
    
    public RenderPig(final RenderManager p_i46149_1_, final ModelBase p_i46149_2_, final float p_i46149_3_) {
        super(p_i46149_1_, p_i46149_2_, p_i46149_3_);
        this.addLayer(new LayerSaddle(this));
    }
    
    protected ResourceLocation func_180583_a(final EntityPig p_180583_1_) {
        return RenderPig.pigTextures;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.func_180583_a((EntityPig)p_110775_1_);
    }
    
    static {
        pigTextures = new ResourceLocation("textures/entity/pig/pig.png");
    }
}
