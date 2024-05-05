package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBook
extends ModelBase {
    public ModelRenderer coverRight = new ModelRenderer((ModelBase)this).setTextureOffset(0, 0).addBox(-6.0f, -5.0f, 0.0f, 6, 10, 0);
    public ModelRenderer coverLeft = new ModelRenderer((ModelBase)this).setTextureOffset(16, 0).addBox(0.0f, -5.0f, 0.0f, 6, 10, 0);
    public ModelRenderer pagesRight = new ModelRenderer((ModelBase)this).setTextureOffset(0, 10).addBox(0.0f, -4.0f, -0.99f, 5, 8, 1);
    public ModelRenderer pagesLeft = new ModelRenderer((ModelBase)this).setTextureOffset(12, 10).addBox(0.0f, -4.0f, -0.01f, 5, 8, 1);
    public ModelRenderer flippingPageRight = new ModelRenderer((ModelBase)this).setTextureOffset(24, 10).addBox(0.0f, -4.0f, 0.0f, 5, 8, 0);
    public ModelRenderer flippingPageLeft = new ModelRenderer((ModelBase)this).setTextureOffset(24, 10).addBox(0.0f, -4.0f, 0.0f, 5, 8, 0);
    public ModelRenderer bookSpine = new ModelRenderer((ModelBase)this).setTextureOffset(12, 0).addBox(-1.0f, -5.0f, 0.0f, 2, 10, 0);

    public ModelBook() {
        this.coverRight.setRotationPoint(0.0f, 0.0f, -1.0f);
        this.coverLeft.setRotationPoint(0.0f, 0.0f, 1.0f);
        this.bookSpine.rotateAngleY = 1.5707964f;
    }

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

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        float f = (MathHelper.sin((float)(limbSwing * 0.02f)) * 0.1f + 1.25f) * netHeadYaw;
        this.coverRight.rotateAngleY = (float)Math.PI + f;
        this.coverLeft.rotateAngleY = -f;
        this.pagesRight.rotateAngleY = f;
        this.pagesLeft.rotateAngleY = -f;
        this.flippingPageRight.rotateAngleY = f - f * 2.0f * limbSwingAmount;
        this.flippingPageLeft.rotateAngleY = f - f * 2.0f * ageInTicks;
        this.pagesRight.rotationPointX = MathHelper.sin((float)f);
        this.pagesLeft.rotationPointX = MathHelper.sin((float)f);
        this.flippingPageRight.rotationPointX = MathHelper.sin((float)f);
        this.flippingPageLeft.rotationPointX = MathHelper.sin((float)f);
    }
}
