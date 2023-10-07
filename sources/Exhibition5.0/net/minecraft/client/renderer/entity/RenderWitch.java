// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerHeldItemWitch;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelWitch;
import net.minecraft.util.ResourceLocation;

public class RenderWitch extends RenderLiving
{
    private static final ResourceLocation witchTextures;
    private static final String __OBFID = "CL_00001033";
    
    public RenderWitch(final RenderManager p_i46131_1_) {
        super(p_i46131_1_, new ModelWitch(0.0f), 0.5f);
        this.addLayer(new LayerHeldItemWitch(this));
    }
    
    public void func_180590_a(final EntityWitch p_180590_1_, final double p_180590_2_, final double p_180590_4_, final double p_180590_6_, final float p_180590_8_, final float p_180590_9_) {
        ((ModelWitch)this.mainModel).field_82900_g = (p_180590_1_.getHeldItem() != null);
        super.doRender(p_180590_1_, p_180590_2_, p_180590_4_, p_180590_6_, p_180590_8_, p_180590_9_);
    }
    
    protected ResourceLocation func_180589_a(final EntityWitch p_180589_1_) {
        return RenderWitch.witchTextures;
    }
    
    @Override
    public void func_82422_c() {
        GlStateManager.translate(0.0f, 0.1875f, 0.0f);
    }
    
    protected void preRenderCallback(final EntityWitch p_77041_1_, final float p_77041_2_) {
        final float var3 = 0.9375f;
        GlStateManager.scale(var3, var3, var3);
    }
    
    @Override
    public void doRender(final EntityLiving p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_180590_a((EntityWitch)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    protected void preRenderCallback(final EntityLivingBase p_77041_1_, final float p_77041_2_) {
        this.preRenderCallback((EntityWitch)p_77041_1_, p_77041_2_);
    }
    
    @Override
    public void doRender(final EntityLivingBase p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_180590_a((EntityWitch)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.func_180589_a((EntityWitch)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_180590_a((EntityWitch)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    static {
        witchTextures = new ResourceLocation("textures/entity/witch.png");
    }
}
