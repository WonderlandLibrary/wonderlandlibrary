// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerSlimeGel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

public class RenderSlime extends RenderLiving
{
    private static final ResourceLocation slimeTextures;
    private static final String __OBFID = "CL_00001024";
    
    public RenderSlime(final RenderManager p_i46141_1_, final ModelBase p_i46141_2_, final float p_i46141_3_) {
        super(p_i46141_1_, p_i46141_2_, p_i46141_3_);
        this.addLayer(new LayerSlimeGel(this));
    }
    
    public void doRender(final EntitySlime p_177124_1_, final double p_177124_2_, final double p_177124_4_, final double p_177124_6_, final float p_177124_8_, final float p_177124_9_) {
        this.shadowSize = 0.25f * p_177124_1_.getSlimeSize();
        super.doRender(p_177124_1_, p_177124_2_, p_177124_4_, p_177124_6_, p_177124_8_, p_177124_9_);
    }
    
    protected void preRenderCallback(final EntitySlime p_77041_1_, final float p_77041_2_) {
        final float var3 = p_77041_1_.getSlimeSize();
        final float var4 = (p_77041_1_.prevSquishFactor + (p_77041_1_.squishFactor - p_77041_1_.prevSquishFactor) * p_77041_2_) / (var3 * 0.5f + 1.0f);
        final float var5 = 1.0f / (var4 + 1.0f);
        GlStateManager.scale(var5 * var3, 1.0f / var5 * var3, var5 * var3);
    }
    
    protected ResourceLocation getEntityTexture(final EntitySlime p_110775_1_) {
        return RenderSlime.slimeTextures;
    }
    
    @Override
    public void doRender(final EntityLiving p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntitySlime)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    protected void preRenderCallback(final EntityLivingBase p_77041_1_, final float p_77041_2_) {
        this.preRenderCallback((EntitySlime)p_77041_1_, p_77041_2_);
    }
    
    @Override
    public void doRender(final EntityLivingBase p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntitySlime)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntitySlime)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntitySlime)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    static {
        slimeTextures = new ResourceLocation("textures/entity/slime/slime.png");
    }
}
