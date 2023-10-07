// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.model;

import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelQuadruped extends ModelBase
{
    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer leg1;
    public ModelRenderer leg2;
    public ModelRenderer leg3;
    public ModelRenderer leg4;
    protected float childYOffset;
    protected float childZOffset;
    private static final String __OBFID = "CL_00000851";
    
    public ModelQuadruped(final int p_i1154_1_, final float p_i1154_2_) {
        this.head = new ModelRenderer(this, 0, 0);
        this.childYOffset = 8.0f;
        this.childZOffset = 4.0f;
        this.head.addBox(-4.0f, -4.0f, -8.0f, 8, 8, 8, p_i1154_2_);
        this.head.setRotationPoint(0.0f, 18 - p_i1154_1_, -6.0f);
        (this.body = new ModelRenderer(this, 28, 8)).addBox(-5.0f, -10.0f, -7.0f, 10, 16, 8, p_i1154_2_);
        this.body.setRotationPoint(0.0f, 17 - p_i1154_1_, 2.0f);
        (this.leg1 = new ModelRenderer(this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4, p_i1154_1_, 4, p_i1154_2_);
        this.leg1.setRotationPoint(-3.0f, 24 - p_i1154_1_, 7.0f);
        (this.leg2 = new ModelRenderer(this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4, p_i1154_1_, 4, p_i1154_2_);
        this.leg2.setRotationPoint(3.0f, 24 - p_i1154_1_, 7.0f);
        (this.leg3 = new ModelRenderer(this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4, p_i1154_1_, 4, p_i1154_2_);
        this.leg3.setRotationPoint(-3.0f, 24 - p_i1154_1_, -5.0f);
        (this.leg4 = new ModelRenderer(this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4, p_i1154_1_, 4, p_i1154_2_);
        this.leg4.setRotationPoint(3.0f, 24 - p_i1154_1_, -5.0f);
    }
    
    @Override
    public void render(final Entity p_78088_1_, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        if (this.isChild) {
            final float var8 = 2.0f;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, this.childYOffset * p_78088_7_, this.childZOffset * p_78088_7_);
            this.head.render(p_78088_7_);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f / var8, 1.0f / var8, 1.0f / var8);
            GlStateManager.translate(0.0f, 24.0f * p_78088_7_, 0.0f);
            this.body.render(p_78088_7_);
            this.leg1.render(p_78088_7_);
            this.leg2.render(p_78088_7_);
            this.leg3.render(p_78088_7_);
            this.leg4.render(p_78088_7_);
            GlStateManager.popMatrix();
        }
        else {
            this.head.render(p_78088_7_);
            this.body.render(p_78088_7_);
            this.leg1.render(p_78088_7_);
            this.leg2.render(p_78088_7_);
            this.leg3.render(p_78088_7_);
            this.leg4.render(p_78088_7_);
        }
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity p_78087_7_) {
        final float var8 = 57.295776f;
        this.head.rotateAngleX = p_78087_5_ / 57.295776f;
        this.head.rotateAngleY = p_78087_4_ / 57.295776f;
        this.body.rotateAngleX = 1.5707964f;
        this.leg1.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f) * 1.4f * p_78087_2_;
        this.leg2.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f + 3.1415927f) * 1.4f * p_78087_2_;
        this.leg3.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f + 3.1415927f) * 1.4f * p_78087_2_;
        this.leg4.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f) * 1.4f * p_78087_2_;
    }
}
