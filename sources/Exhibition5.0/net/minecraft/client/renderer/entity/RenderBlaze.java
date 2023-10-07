// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBlaze;
import net.minecraft.util.ResourceLocation;

public class RenderBlaze extends RenderLiving
{
    private static final ResourceLocation blazeTextures;
    private static final String __OBFID = "CL_00000980";
    
    public RenderBlaze(final RenderManager p_i46191_1_) {
        super(p_i46191_1_, new ModelBlaze(), 0.5f);
    }
    
    protected ResourceLocation getEntityTexture(final EntityBlaze p_110775_1_) {
        return RenderBlaze.blazeTextures;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntityBlaze)p_110775_1_);
    }
    
    static {
        blazeTextures = new ResourceLocation("textures/entity/blaze.png");
    }
}
