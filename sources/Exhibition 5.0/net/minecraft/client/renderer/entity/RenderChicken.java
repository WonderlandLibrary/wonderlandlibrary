// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

public class RenderChicken extends RenderLiving
{
    private static final ResourceLocation chickenTextures;
    private static final String __OBFID = "CL_00000983";
    
    public RenderChicken(final RenderManager p_i46188_1_, final ModelBase p_i46188_2_, final float p_i46188_3_) {
        super(p_i46188_1_, p_i46188_2_, p_i46188_3_);
    }
    
    protected ResourceLocation func_180568_a(final EntityChicken p_180568_1_) {
        return RenderChicken.chickenTextures;
    }
    
    protected float func_180569_a(final EntityChicken p_180569_1_, final float p_180569_2_) {
        final float var3 = p_180569_1_.field_70888_h + (p_180569_1_.field_70886_e - p_180569_1_.field_70888_h) * p_180569_2_;
        final float var4 = p_180569_1_.field_70884_g + (p_180569_1_.destPos - p_180569_1_.field_70884_g) * p_180569_2_;
        return (MathHelper.sin(var3) + 1.0f) * var4;
    }
    
    @Override
    protected float handleRotationFloat(final EntityLivingBase p_77044_1_, final float p_77044_2_) {
        return this.func_180569_a((EntityChicken)p_77044_1_, p_77044_2_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.func_180568_a((EntityChicken)p_110775_1_);
    }
    
    static {
        chickenTextures = new ResourceLocation("textures/entity/chicken.png");
    }
}
