// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.model;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;

public class ModelSpider extends ModelBase
{
    public ModelRenderer spiderHead;
    public ModelRenderer spiderNeck;
    public ModelRenderer spiderBody;
    public ModelRenderer spiderLeg1;
    public ModelRenderer spiderLeg2;
    public ModelRenderer spiderLeg3;
    public ModelRenderer spiderLeg4;
    public ModelRenderer spiderLeg5;
    public ModelRenderer spiderLeg6;
    public ModelRenderer spiderLeg7;
    public ModelRenderer spiderLeg8;
    private static final String __OBFID = "CL_00000860";
    
    public ModelSpider() {
        final float var1 = 0.0f;
        final byte var2 = 15;
        (this.spiderHead = new ModelRenderer(this, 32, 4)).addBox(-4.0f, -4.0f, -8.0f, 8, 8, 8, var1);
        this.spiderHead.setRotationPoint(0.0f, var2, -3.0f);
        (this.spiderNeck = new ModelRenderer(this, 0, 0)).addBox(-3.0f, -3.0f, -3.0f, 6, 6, 6, var1);
        this.spiderNeck.setRotationPoint(0.0f, var2, 0.0f);
        (this.spiderBody = new ModelRenderer(this, 0, 12)).addBox(-5.0f, -4.0f, -6.0f, 10, 8, 12, var1);
        this.spiderBody.setRotationPoint(0.0f, var2, 9.0f);
        (this.spiderLeg1 = new ModelRenderer(this, 18, 0)).addBox(-15.0f, -1.0f, -1.0f, 16, 2, 2, var1);
        this.spiderLeg1.setRotationPoint(-4.0f, var2, 2.0f);
        (this.spiderLeg2 = new ModelRenderer(this, 18, 0)).addBox(-1.0f, -1.0f, -1.0f, 16, 2, 2, var1);
        this.spiderLeg2.setRotationPoint(4.0f, var2, 2.0f);
        (this.spiderLeg3 = new ModelRenderer(this, 18, 0)).addBox(-15.0f, -1.0f, -1.0f, 16, 2, 2, var1);
        this.spiderLeg3.setRotationPoint(-4.0f, var2, 1.0f);
        (this.spiderLeg4 = new ModelRenderer(this, 18, 0)).addBox(-1.0f, -1.0f, -1.0f, 16, 2, 2, var1);
        this.spiderLeg4.setRotationPoint(4.0f, var2, 1.0f);
        (this.spiderLeg5 = new ModelRenderer(this, 18, 0)).addBox(-15.0f, -1.0f, -1.0f, 16, 2, 2, var1);
        this.spiderLeg5.setRotationPoint(-4.0f, var2, 0.0f);
        (this.spiderLeg6 = new ModelRenderer(this, 18, 0)).addBox(-1.0f, -1.0f, -1.0f, 16, 2, 2, var1);
        this.spiderLeg6.setRotationPoint(4.0f, var2, 0.0f);
        (this.spiderLeg7 = new ModelRenderer(this, 18, 0)).addBox(-15.0f, -1.0f, -1.0f, 16, 2, 2, var1);
        this.spiderLeg7.setRotationPoint(-4.0f, var2, -1.0f);
        (this.spiderLeg8 = new ModelRenderer(this, 18, 0)).addBox(-1.0f, -1.0f, -1.0f, 16, 2, 2, var1);
        this.spiderLeg8.setRotationPoint(4.0f, var2, -1.0f);
    }
    
    @Override
    public void render(final Entity p_78088_1_, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.spiderHead.render(p_78088_7_);
        this.spiderNeck.render(p_78088_7_);
        this.spiderBody.render(p_78088_7_);
        this.spiderLeg1.render(p_78088_7_);
        this.spiderLeg2.render(p_78088_7_);
        this.spiderLeg3.render(p_78088_7_);
        this.spiderLeg4.render(p_78088_7_);
        this.spiderLeg5.render(p_78088_7_);
        this.spiderLeg6.render(p_78088_7_);
        this.spiderLeg7.render(p_78088_7_);
        this.spiderLeg8.render(p_78088_7_);
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity p_78087_7_) {
        this.spiderHead.rotateAngleY = p_78087_4_ / 57.295776f;
        this.spiderHead.rotateAngleX = p_78087_5_ / 57.295776f;
        final float var8 = 0.7853982f;
        this.spiderLeg1.rotateAngleZ = -var8;
        this.spiderLeg2.rotateAngleZ = var8;
        this.spiderLeg3.rotateAngleZ = -var8 * 0.74f;
        this.spiderLeg4.rotateAngleZ = var8 * 0.74f;
        this.spiderLeg5.rotateAngleZ = -var8 * 0.74f;
        this.spiderLeg6.rotateAngleZ = var8 * 0.74f;
        this.spiderLeg7.rotateAngleZ = -var8;
        this.spiderLeg8.rotateAngleZ = var8;
        final float var9 = -0.0f;
        final float var10 = 0.3926991f;
        this.spiderLeg1.rotateAngleY = var10 * 2.0f + var9;
        this.spiderLeg2.rotateAngleY = -var10 * 2.0f - var9;
        this.spiderLeg3.rotateAngleY = var10 * 1.0f + var9;
        this.spiderLeg4.rotateAngleY = -var10 * 1.0f - var9;
        this.spiderLeg5.rotateAngleY = -var10 * 1.0f + var9;
        this.spiderLeg6.rotateAngleY = var10 * 1.0f - var9;
        this.spiderLeg7.rotateAngleY = -var10 * 2.0f + var9;
        this.spiderLeg8.rotateAngleY = var10 * 2.0f - var9;
        final float var11 = -(MathHelper.cos(p_78087_1_ * 0.6662f * 2.0f + 0.0f) * 0.4f) * p_78087_2_;
        final float var12 = -(MathHelper.cos(p_78087_1_ * 0.6662f * 2.0f + 3.1415927f) * 0.4f) * p_78087_2_;
        final float var13 = -(MathHelper.cos(p_78087_1_ * 0.6662f * 2.0f + 1.5707964f) * 0.4f) * p_78087_2_;
        final float var14 = -(MathHelper.cos(p_78087_1_ * 0.6662f * 2.0f + 4.712389f) * 0.4f) * p_78087_2_;
        final float var15 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662f + 0.0f) * 0.4f) * p_78087_2_;
        final float var16 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662f + 3.1415927f) * 0.4f) * p_78087_2_;
        final float var17 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662f + 1.5707964f) * 0.4f) * p_78087_2_;
        final float var18 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662f + 4.712389f) * 0.4f) * p_78087_2_;
        final ModelRenderer spiderLeg1 = this.spiderLeg1;
        spiderLeg1.rotateAngleY += var11;
        final ModelRenderer spiderLeg2 = this.spiderLeg2;
        spiderLeg2.rotateAngleY += -var11;
        final ModelRenderer spiderLeg3 = this.spiderLeg3;
        spiderLeg3.rotateAngleY += var12;
        final ModelRenderer spiderLeg4 = this.spiderLeg4;
        spiderLeg4.rotateAngleY += -var12;
        final ModelRenderer spiderLeg5 = this.spiderLeg5;
        spiderLeg5.rotateAngleY += var13;
        final ModelRenderer spiderLeg6 = this.spiderLeg6;
        spiderLeg6.rotateAngleY += -var13;
        final ModelRenderer spiderLeg7 = this.spiderLeg7;
        spiderLeg7.rotateAngleY += var14;
        final ModelRenderer spiderLeg8 = this.spiderLeg8;
        spiderLeg8.rotateAngleY += -var14;
        final ModelRenderer spiderLeg9 = this.spiderLeg1;
        spiderLeg9.rotateAngleZ += var15;
        final ModelRenderer spiderLeg10 = this.spiderLeg2;
        spiderLeg10.rotateAngleZ += -var15;
        final ModelRenderer spiderLeg11 = this.spiderLeg3;
        spiderLeg11.rotateAngleZ += var16;
        final ModelRenderer spiderLeg12 = this.spiderLeg4;
        spiderLeg12.rotateAngleZ += -var16;
        final ModelRenderer spiderLeg13 = this.spiderLeg5;
        spiderLeg13.rotateAngleZ += var17;
        final ModelRenderer spiderLeg14 = this.spiderLeg6;
        spiderLeg14.rotateAngleZ += -var17;
        final ModelRenderer spiderLeg15 = this.spiderLeg7;
        spiderLeg15.rotateAngleZ += var18;
        final ModelRenderer spiderLeg16 = this.spiderLeg8;
        spiderLeg16.rotateAngleZ += -var18;
    }
}
