// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.util.ResourceLocation;

public class RenderSkeleton extends RenderBiped
{
    private static final ResourceLocation skeletonTextures;
    private static final ResourceLocation witherSkeletonTextures;
    private static final String __OBFID = "CL_00001023";
    
    public RenderSkeleton(final RenderManager p_i46143_1_) {
        super(p_i46143_1_, new ModelSkeleton(), 0.5f);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBipedArmor(this) {
            private static final String __OBFID = "CL_00002431";
            
            @Override
            protected void func_177177_a() {
                this.field_177189_c = new ModelSkeleton(0.5f, true);
                this.field_177186_d = new ModelSkeleton(1.0f, true);
            }
        });
    }
    
    protected void preRenderCallback(final EntitySkeleton p_77041_1_, final float p_77041_2_) {
        if (p_77041_1_.getSkeletonType() == 1) {
            GlStateManager.scale(1.2f, 1.2f, 1.2f);
        }
    }
    
    @Override
    public void func_82422_c() {
        GlStateManager.translate(0.09375f, 0.1875f, 0.0f);
    }
    
    protected ResourceLocation func_180577_a(final EntitySkeleton p_180577_1_) {
        return (p_180577_1_.getSkeletonType() == 1) ? RenderSkeleton.witherSkeletonTextures : RenderSkeleton.skeletonTextures;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityLiving p_110775_1_) {
        return this.func_180577_a((EntitySkeleton)p_110775_1_);
    }
    
    @Override
    protected void preRenderCallback(final EntityLivingBase p_77041_1_, final float p_77041_2_) {
        this.preRenderCallback((EntitySkeleton)p_77041_1_, p_77041_2_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.func_180577_a((EntitySkeleton)p_110775_1_);
    }
    
    static {
        skeletonTextures = new ResourceLocation("textures/entity/skeleton/skeleton.png");
        witherSkeletonTextures = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");
    }
}
