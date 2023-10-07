// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerWitherAura;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelWither;
import net.minecraft.util.ResourceLocation;

public class RenderWither extends RenderLiving
{
    private static final ResourceLocation invulnerableWitherTextures;
    private static final ResourceLocation witherTextures;
    private static final String __OBFID = "CL_00001034";
    
    public RenderWither(final RenderManager p_i46130_1_) {
        super(p_i46130_1_, new ModelWither(0.0f), 1.0f);
        this.addLayer(new LayerWitherAura(this));
    }
    
    public void func_180591_a(final EntityWither p_180591_1_, final double p_180591_2_, final double p_180591_4_, final double p_180591_6_, final float p_180591_8_, final float p_180591_9_) {
        BossStatus.setBossStatus(p_180591_1_, true);
        super.doRender(p_180591_1_, p_180591_2_, p_180591_4_, p_180591_6_, p_180591_8_, p_180591_9_);
    }
    
    protected ResourceLocation getEntityTexture(final EntityWither p_110775_1_) {
        final int var2 = p_110775_1_.getInvulTime();
        return (var2 > 0 && (var2 > 80 || var2 / 5 % 2 != 1)) ? RenderWither.invulnerableWitherTextures : RenderWither.witherTextures;
    }
    
    protected void func_180592_a(final EntityWither p_180592_1_, final float p_180592_2_) {
        float var3 = 2.0f;
        final int var4 = p_180592_1_.getInvulTime();
        if (var4 > 0) {
            var3 -= (var4 - p_180592_2_) / 220.0f * 0.5f;
        }
        GlStateManager.scale(var3, var3, var3);
    }
    
    @Override
    public void doRender(final EntityLiving p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_180591_a((EntityWither)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    protected void preRenderCallback(final EntityLivingBase p_77041_1_, final float p_77041_2_) {
        this.func_180592_a((EntityWither)p_77041_1_, p_77041_2_);
    }
    
    @Override
    public void doRender(final EntityLivingBase p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_180591_a((EntityWither)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntityWither)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_180591_a((EntityWither)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    static {
        invulnerableWitherTextures = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
        witherTextures = new ResourceLocation("textures/entity/wither/wither.png");
    }
}
