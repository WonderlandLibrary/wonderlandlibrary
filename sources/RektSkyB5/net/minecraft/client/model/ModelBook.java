/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBook
extends ModelBase {
    public ModelRenderer coverRight = new ModelRenderer(this).setTextureOffset(0, 0).addBox(-6.0f, -5.0f, 0.0f, 6, 10, 0);
    public ModelRenderer coverLeft = new ModelRenderer(this).setTextureOffset(16, 0).addBox(0.0f, -5.0f, 0.0f, 6, 10, 0);
    public ModelRenderer pagesRight = new ModelRenderer(this).setTextureOffset(0, 10).addBox(0.0f, -4.0f, -0.99f, 5, 8, 1);
    public ModelRenderer pagesLeft = new ModelRenderer(this).setTextureOffset(12, 10).addBox(0.0f, -4.0f, -0.01f, 5, 8, 1);
    public ModelRenderer flippingPageRight = new ModelRenderer(this).setTextureOffset(24, 10).addBox(0.0f, -4.0f, 0.0f, 5, 8, 0);
    public ModelRenderer flippingPageLeft = new ModelRenderer(this).setTextureOffset(24, 10).addBox(0.0f, -4.0f, 0.0f, 5, 8, 0);
    public ModelRenderer bookSpine = new ModelRenderer(this).setTextureOffset(12, 0).addBox(-1.0f, -5.0f, 0.0f, 2, 10, 0);

    public ModelBook() {
        this.coverRight.setRotationPoint(0.0f, 0.0f, -1.0f);
        this.coverLeft.setRotationPoint(0.0f, 0.0f, 1.0f);
        this.bookSpine.rotateAngleY = 1.5707964f;
    }

    @Override
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        this.coverRight.render(scale);
        this.coverLeft.render(scale);
        this.bookSpine.render(scale);
        this.pagesRight.render(scale);
        this.pagesLeft.render(scale);
        this.flippingPageRight.render(scale);
        this.flippingPageLeft.render(scale);
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn) {
        float f2 = (MathHelper.sin(p_78087_1_ * 0.02f) * 0.1f + 1.25f) * p_78087_4_;
        this.coverRight.rotateAngleY = (float)Math.PI + f2;
        this.coverLeft.rotateAngleY = -f2;
        this.pagesRight.rotateAngleY = f2;
        this.pagesLeft.rotateAngleY = -f2;
        this.flippingPageRight.rotateAngleY = f2 - f2 * 2.0f * p_78087_2_;
        this.flippingPageLeft.rotateAngleY = f2 - f2 * 2.0f * p_78087_3_;
        this.pagesRight.rotationPointX = MathHelper.sin(f2);
        this.pagesLeft.rotationPointX = MathHelper.sin(f2);
        this.flippingPageRight.rotationPointX = MathHelper.sin(f2);
        this.flippingPageLeft.rotationPointX = MathHelper.sin(f2);
    }
}

