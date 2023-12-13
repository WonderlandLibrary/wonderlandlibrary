// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerWolfCollar;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

public class RenderWolf extends RenderLiving
{
    private static final ResourceLocation wolfTextures;
    private static final ResourceLocation tamedWolfTextures;
    private static final ResourceLocation anrgyWolfTextures;
    private static final String __OBFID = "CL_00001036";
    
    public RenderWolf(final RenderManager p_i46128_1_, final ModelBase p_i46128_2_, final float p_i46128_3_) {
        super(p_i46128_1_, p_i46128_2_, p_i46128_3_);
        this.addLayer(new LayerWolfCollar(this));
    }
    
    protected float func_180593_a(final EntityWolf p_180593_1_, final float p_180593_2_) {
        return p_180593_1_.getTailRotation();
    }
    
    public void func_177135_a(final EntityWolf p_177135_1_, final double p_177135_2_, final double p_177135_4_, final double p_177135_6_, final float p_177135_8_, final float p_177135_9_) {
        if (p_177135_1_.isWolfWet()) {
            final float var10 = p_177135_1_.getBrightness(p_177135_9_) * p_177135_1_.getShadingWhileWet(p_177135_9_);
            GlStateManager.color(var10, var10, var10);
        }
        super.doRender(p_177135_1_, p_177135_2_, p_177135_4_, p_177135_6_, p_177135_8_, p_177135_9_);
    }
    
    protected ResourceLocation getEntityTexture(final EntityWolf p_110775_1_) {
        return p_110775_1_.isTamed() ? RenderWolf.tamedWolfTextures : (p_110775_1_.isAngry() ? RenderWolf.anrgyWolfTextures : RenderWolf.wolfTextures);
    }
    
    @Override
    public void doRender(final EntityLiving p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_177135_a((EntityWolf)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    protected float handleRotationFloat(final EntityLivingBase p_77044_1_, final float p_77044_2_) {
        return this.func_180593_a((EntityWolf)p_77044_1_, p_77044_2_);
    }
    
    @Override
    public void doRender(final EntityLivingBase p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_177135_a((EntityWolf)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntityWolf)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_177135_a((EntityWolf)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    static {
        wolfTextures = new ResourceLocation("textures/entity/wolf/wolf.png");
        tamedWolfTextures = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
        anrgyWolfTextures = new ResourceLocation("textures/entity/wolf/wolf_angry.png");
    }
}
