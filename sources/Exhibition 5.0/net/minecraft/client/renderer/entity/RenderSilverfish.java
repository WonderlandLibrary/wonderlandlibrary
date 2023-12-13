// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.util.ResourceLocation;

public class RenderSilverfish extends RenderLiving
{
    private static final ResourceLocation silverfishTextures;
    private static final String __OBFID = "CL_00001022";
    
    public RenderSilverfish(final RenderManager p_i46144_1_) {
        super(p_i46144_1_, new ModelSilverfish(), 0.3f);
    }
    
    protected float func_180584_a(final EntitySilverfish p_180584_1_) {
        return 180.0f;
    }
    
    protected ResourceLocation getEntityTexture(final EntitySilverfish p_110775_1_) {
        return RenderSilverfish.silverfishTextures;
    }
    
    @Override
    protected float getDeathMaxRotation(final EntityLivingBase p_77037_1_) {
        return this.func_180584_a((EntitySilverfish)p_77037_1_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntitySilverfish)p_110775_1_);
    }
    
    static {
        silverfishTextures = new ResourceLocation("textures/entity/silverfish.png");
    }
}
