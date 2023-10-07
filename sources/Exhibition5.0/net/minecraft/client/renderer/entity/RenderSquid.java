// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

public class RenderSquid extends RenderLiving
{
    private static final ResourceLocation squidTextures;
    private static final String __OBFID = "CL_00001028";
    
    public RenderSquid(final RenderManager p_i46138_1_, final ModelBase p_i46138_2_, final float p_i46138_3_) {
        super(p_i46138_1_, p_i46138_2_, p_i46138_3_);
    }
    
    protected ResourceLocation getEntityTexture(final EntitySquid p_110775_1_) {
        return RenderSquid.squidTextures;
    }
    
    protected void rotateCorpse(final EntitySquid p_77043_1_, final float p_77043_2_, final float p_77043_3_, final float p_77043_4_) {
        final float var5 = p_77043_1_.prevSquidPitch + (p_77043_1_.squidPitch - p_77043_1_.prevSquidPitch) * p_77043_4_;
        final float var6 = p_77043_1_.prevSquidYaw + (p_77043_1_.squidYaw - p_77043_1_.prevSquidYaw) * p_77043_4_;
        GlStateManager.translate(0.0f, 0.5f, 0.0f);
        GlStateManager.rotate(180.0f - p_77043_3_, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(var5, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(var6, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, -1.2f, 0.0f);
    }
    
    protected float handleRotationFloat(final EntitySquid p_77044_1_, final float p_77044_2_) {
        return p_77044_1_.lastTentacleAngle + (p_77044_1_.tentacleAngle - p_77044_1_.lastTentacleAngle) * p_77044_2_;
    }
    
    @Override
    protected float handleRotationFloat(final EntityLivingBase p_77044_1_, final float p_77044_2_) {
        return this.handleRotationFloat((EntitySquid)p_77044_1_, p_77044_2_);
    }
    
    @Override
    protected void rotateCorpse(final EntityLivingBase p_77043_1_, final float p_77043_2_, final float p_77043_3_, final float p_77043_4_) {
        this.rotateCorpse((EntitySquid)p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntitySquid)p_110775_1_);
    }
    
    static {
        squidTextures = new ResourceLocation("textures/entity/squid.png");
    }
}
