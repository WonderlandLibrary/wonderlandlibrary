// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.model;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;

public class ModelBook extends ModelBase
{
    public ModelRenderer coverRight;
    public ModelRenderer coverLeft;
    public ModelRenderer pagesRight;
    public ModelRenderer pagesLeft;
    public ModelRenderer flippingPageRight;
    public ModelRenderer flippingPageLeft;
    public ModelRenderer bookSpine;
    private static final String __OBFID = "CL_00000833";
    
    public ModelBook() {
        this.coverRight = new ModelRenderer(this).setTextureOffset(0, 0).addBox(-6.0f, -5.0f, 0.0f, 6, 10, 0);
        this.coverLeft = new ModelRenderer(this).setTextureOffset(16, 0).addBox(0.0f, -5.0f, 0.0f, 6, 10, 0);
        this.pagesRight = new ModelRenderer(this).setTextureOffset(0, 10).addBox(0.0f, -4.0f, -0.99f, 5, 8, 1);
        this.pagesLeft = new ModelRenderer(this).setTextureOffset(12, 10).addBox(0.0f, -4.0f, -0.01f, 5, 8, 1);
        this.flippingPageRight = new ModelRenderer(this).setTextureOffset(24, 10).addBox(0.0f, -4.0f, 0.0f, 5, 8, 0);
        this.flippingPageLeft = new ModelRenderer(this).setTextureOffset(24, 10).addBox(0.0f, -4.0f, 0.0f, 5, 8, 0);
        this.bookSpine = new ModelRenderer(this).setTextureOffset(12, 0).addBox(-1.0f, -5.0f, 0.0f, 2, 10, 0);
        this.coverRight.setRotationPoint(0.0f, 0.0f, -1.0f);
        this.coverLeft.setRotationPoint(0.0f, 0.0f, 1.0f);
        this.bookSpine.rotateAngleY = 1.5707964f;
    }
    
    @Override
    public void render(final Entity p_78088_1_, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.coverRight.render(p_78088_7_);
        this.coverLeft.render(p_78088_7_);
        this.bookSpine.render(p_78088_7_);
        this.pagesRight.render(p_78088_7_);
        this.pagesLeft.render(p_78088_7_);
        this.flippingPageRight.render(p_78088_7_);
        this.flippingPageLeft.render(p_78088_7_);
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity p_78087_7_) {
        final float var8 = (MathHelper.sin(p_78087_1_ * 0.02f) * 0.1f + 1.25f) * p_78087_4_;
        this.coverRight.rotateAngleY = 3.1415927f + var8;
        this.coverLeft.rotateAngleY = -var8;
        this.pagesRight.rotateAngleY = var8;
        this.pagesLeft.rotateAngleY = -var8;
        this.flippingPageRight.rotateAngleY = var8 - var8 * 2.0f * p_78087_2_;
        this.flippingPageLeft.rotateAngleY = var8 - var8 * 2.0f * p_78087_3_;
        this.pagesRight.rotationPointX = MathHelper.sin(var8);
        this.pagesLeft.rotationPointX = MathHelper.sin(var8);
        this.flippingPageRight.rotationPointX = MathHelper.sin(var8);
        this.flippingPageLeft.rotationPointX = MathHelper.sin(var8);
    }
}
