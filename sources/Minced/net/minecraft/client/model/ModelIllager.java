// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.EnumHandSide;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.monster.AbstractIllager;
import net.minecraft.entity.Entity;

public class ModelIllager extends ModelBase
{
    public ModelRenderer head;
    public ModelRenderer hat;
    public ModelRenderer body;
    public ModelRenderer arms;
    public ModelRenderer leg0;
    public ModelRenderer leg1;
    public ModelRenderer nose;
    public ModelRenderer rightArm;
    public ModelRenderer leftArm;
    
    public ModelIllager(final float scaleFactor, final float p_i47227_2_, final int textureWidthIn, final int textureHeightIn) {
        (this.head = new ModelRenderer(this).setTextureSize(textureWidthIn, textureHeightIn)).setRotationPoint(0.0f, 0.0f + p_i47227_2_, 0.0f);
        this.head.setTextureOffset(0, 0).addBox(-4.0f, -10.0f, -4.0f, 8, 10, 8, scaleFactor);
        (this.hat = new ModelRenderer(this, 32, 0).setTextureSize(textureWidthIn, textureHeightIn)).addBox(-4.0f, -10.0f, -4.0f, 8, 12, 8, scaleFactor + 0.45f);
        this.head.addChild(this.hat);
        this.hat.showModel = false;
        (this.nose = new ModelRenderer(this).setTextureSize(textureWidthIn, textureHeightIn)).setRotationPoint(0.0f, p_i47227_2_ - 2.0f, 0.0f);
        this.nose.setTextureOffset(24, 0).addBox(-1.0f, -1.0f, -6.0f, 2, 4, 2, scaleFactor);
        this.head.addChild(this.nose);
        (this.body = new ModelRenderer(this).setTextureSize(textureWidthIn, textureHeightIn)).setRotationPoint(0.0f, 0.0f + p_i47227_2_, 0.0f);
        this.body.setTextureOffset(16, 20).addBox(-4.0f, 0.0f, -3.0f, 8, 12, 6, scaleFactor);
        this.body.setTextureOffset(0, 38).addBox(-4.0f, 0.0f, -3.0f, 8, 18, 6, scaleFactor + 0.5f);
        (this.arms = new ModelRenderer(this).setTextureSize(textureWidthIn, textureHeightIn)).setRotationPoint(0.0f, 0.0f + p_i47227_2_ + 2.0f, 0.0f);
        this.arms.setTextureOffset(44, 22).addBox(-8.0f, -2.0f, -2.0f, 4, 8, 4, scaleFactor);
        final ModelRenderer modelrenderer = new ModelRenderer(this, 44, 22).setTextureSize(textureWidthIn, textureHeightIn);
        modelrenderer.mirror = true;
        modelrenderer.addBox(4.0f, -2.0f, -2.0f, 4, 8, 4, scaleFactor);
        this.arms.addChild(modelrenderer);
        this.arms.setTextureOffset(40, 38).addBox(-4.0f, 2.0f, -2.0f, 8, 4, 4, scaleFactor);
        (this.leg0 = new ModelRenderer(this, 0, 22).setTextureSize(textureWidthIn, textureHeightIn)).setRotationPoint(-2.0f, 12.0f + p_i47227_2_, 0.0f);
        this.leg0.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, scaleFactor);
        this.leg1 = new ModelRenderer(this, 0, 22).setTextureSize(textureWidthIn, textureHeightIn);
        this.leg1.mirror = true;
        this.leg1.setRotationPoint(2.0f, 12.0f + p_i47227_2_, 0.0f);
        this.leg1.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, scaleFactor);
        (this.rightArm = new ModelRenderer(this, 40, 46).setTextureSize(textureWidthIn, textureHeightIn)).addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, scaleFactor);
        this.rightArm.setRotationPoint(-5.0f, 2.0f + p_i47227_2_, 0.0f);
        this.leftArm = new ModelRenderer(this, 40, 46).setTextureSize(textureWidthIn, textureHeightIn);
        this.leftArm.mirror = true;
        this.leftArm.addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, scaleFactor);
        this.leftArm.setRotationPoint(5.0f, 2.0f + p_i47227_2_, 0.0f);
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.head.render(scale);
        this.body.render(scale);
        this.leg0.render(scale);
        this.leg1.render(scale);
        final AbstractIllager abstractillager = (AbstractIllager)entityIn;
        if (abstractillager.getArmPose() == AbstractIllager.IllagerArmPose.CROSSED) {
            this.arms.render(scale);
        }
        else {
            this.rightArm.render(scale);
            this.leftArm.render(scale);
        }
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        this.head.rotateAngleY = netHeadYaw * 0.017453292f;
        this.head.rotateAngleX = headPitch * 0.017453292f;
        this.arms.rotationPointY = 3.0f;
        this.arms.rotationPointZ = -1.0f;
        this.arms.rotateAngleX = -0.75f;
        this.leg0.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount * 0.5f;
        this.leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 1.4f * limbSwingAmount * 0.5f;
        this.leg0.rotateAngleY = 0.0f;
        this.leg1.rotateAngleY = 0.0f;
        final AbstractIllager.IllagerArmPose abstractillager$illagerarmpose = ((AbstractIllager)entityIn).getArmPose();
        if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.ATTACKING) {
            final float f = MathHelper.sin(this.swingProgress * 3.1415927f);
            final float f2 = MathHelper.sin((1.0f - (1.0f - this.swingProgress) * (1.0f - this.swingProgress)) * 3.1415927f);
            this.rightArm.rotateAngleZ = 0.0f;
            this.leftArm.rotateAngleZ = 0.0f;
            this.rightArm.rotateAngleY = 0.15707964f;
            this.leftArm.rotateAngleY = -0.15707964f;
            if (((EntityLivingBase)entityIn).getPrimaryHand() == EnumHandSide.RIGHT) {
                this.rightArm.rotateAngleX = -1.8849558f + MathHelper.cos(ageInTicks * 0.09f) * 0.15f;
                this.leftArm.rotateAngleX = -0.0f + MathHelper.cos(ageInTicks * 0.19f) * 0.5f;
                final ModelRenderer rightArm = this.rightArm;
                rightArm.rotateAngleX += f * 2.2f - f2 * 0.4f;
                final ModelRenderer leftArm = this.leftArm;
                leftArm.rotateAngleX += f * 1.2f - f2 * 0.4f;
            }
            else {
                this.rightArm.rotateAngleX = -0.0f + MathHelper.cos(ageInTicks * 0.19f) * 0.5f;
                this.leftArm.rotateAngleX = -1.8849558f + MathHelper.cos(ageInTicks * 0.09f) * 0.15f;
                final ModelRenderer rightArm2 = this.rightArm;
                rightArm2.rotateAngleX += f * 1.2f - f2 * 0.4f;
                final ModelRenderer leftArm2 = this.leftArm;
                leftArm2.rotateAngleX += f * 2.2f - f2 * 0.4f;
            }
            final ModelRenderer rightArm3 = this.rightArm;
            rightArm3.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
            final ModelRenderer leftArm3 = this.leftArm;
            leftArm3.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
            final ModelRenderer rightArm4 = this.rightArm;
            rightArm4.rotateAngleX += MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
            final ModelRenderer leftArm4 = this.leftArm;
            leftArm4.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
        }
        else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.SPELLCASTING) {
            this.rightArm.rotationPointZ = 0.0f;
            this.rightArm.rotationPointX = -5.0f;
            this.leftArm.rotationPointZ = 0.0f;
            this.leftArm.rotationPointX = 5.0f;
            this.rightArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662f) * 0.25f;
            this.leftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662f) * 0.25f;
            this.rightArm.rotateAngleZ = 2.3561945f;
            this.leftArm.rotateAngleZ = -2.3561945f;
            this.rightArm.rotateAngleY = 0.0f;
            this.leftArm.rotateAngleY = 0.0f;
        }
        else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.BOW_AND_ARROW) {
            this.rightArm.rotateAngleY = -0.1f + this.head.rotateAngleY;
            this.rightArm.rotateAngleX = -1.5707964f + this.head.rotateAngleX;
            this.leftArm.rotateAngleX = -0.9424779f + this.head.rotateAngleX;
            this.leftArm.rotateAngleY = this.head.rotateAngleY - 0.4f;
            this.leftArm.rotateAngleZ = 1.5707964f;
        }
    }
    
    public ModelRenderer getArm(final EnumHandSide p_191216_1_) {
        return (p_191216_1_ == EnumHandSide.LEFT) ? this.leftArm : this.rightArm;
    }
}
