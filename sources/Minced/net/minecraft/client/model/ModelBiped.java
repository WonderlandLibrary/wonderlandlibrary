// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelBiped extends ModelBase
{
    public ModelRenderer bipedHead;
    public ModelRenderer bipedHeadwear;
    public ModelRenderer bipedBody;
    public ModelRenderer bipedRightArm;
    public ModelRenderer bipedLeftArm;
    public ModelRenderer bipedRightLeg;
    public ModelRenderer bipedLeftLeg;
    public ArmPose leftArmPose;
    public ArmPose rightArmPose;
    public boolean isSneak;
    
    public ModelBiped() {
        this(0.0f);
    }
    
    public ModelBiped(final float modelSize) {
        this(modelSize, 0.0f, 64, 32);
    }
    
    public ModelBiped(final float modelSize, final float p_i1149_2_, final int textureWidthIn, final int textureHeightIn) {
        this.leftArmPose = ArmPose.EMPTY;
        this.rightArmPose = ArmPose.EMPTY;
        this.textureWidth = textureWidthIn;
        this.textureHeight = textureHeightIn;
        (this.bipedHead = new ModelRenderer(this, 0, 0)).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, modelSize);
        this.bipedHead.setRotationPoint(0.0f, 0.0f + p_i1149_2_, 0.0f);
        (this.bipedHeadwear = new ModelRenderer(this, 32, 0)).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, modelSize + 0.5f);
        this.bipedHeadwear.setRotationPoint(0.0f, 0.0f + p_i1149_2_, 0.0f);
        (this.bipedBody = new ModelRenderer(this, 16, 16)).addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, modelSize);
        this.bipedBody.setRotationPoint(0.0f, 0.0f + p_i1149_2_, 0.0f);
        (this.bipedRightArm = new ModelRenderer(this, 40, 16)).addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, modelSize);
        this.bipedRightArm.setRotationPoint(-5.0f, 2.0f + p_i1149_2_, 0.0f);
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, modelSize);
        this.bipedLeftArm.setRotationPoint(5.0f, 2.0f + p_i1149_2_, 0.0f);
        (this.bipedRightLeg = new ModelRenderer(this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, modelSize);
        this.bipedRightLeg.setRotationPoint(-1.9f, 12.0f + p_i1149_2_, 0.0f);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, modelSize);
        this.bipedLeftLeg.setRotationPoint(1.9f, 12.0f + p_i1149_2_, 0.0f);
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        GlStateManager.pushMatrix();
        if (this.isChild) {
            final float f = 2.0f;
            GlStateManager.scale(0.75f, 0.75f, 0.75f);
            GlStateManager.translate(0.0f, 16.0f * scale, 0.0f);
            this.bipedHead.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            GlStateManager.translate(0.0f, 24.0f * scale, 0.0f);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);
        }
        else {
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
            }
            this.bipedHead.render(scale);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);
        }
        GlStateManager.popMatrix();
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        final boolean flag = entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getTicksElytraFlying() > 4;
        this.bipedHead.rotateAngleY = netHeadYaw * 0.017453292f;
        if (flag) {
            this.bipedHead.rotateAngleX = -0.7853982f;
        }
        else {
            this.bipedHead.rotateAngleX = headPitch * 0.017453292f;
        }
        this.bipedBody.rotateAngleY = 0.0f;
        this.bipedRightArm.rotationPointZ = 0.0f;
        this.bipedRightArm.rotationPointX = -5.0f;
        this.bipedLeftArm.rotationPointZ = 0.0f;
        this.bipedLeftArm.rotationPointX = 5.0f;
        float f = 1.0f;
        if (flag) {
            f = (float)(entityIn.motionX * entityIn.motionX + entityIn.motionY * entityIn.motionY + entityIn.motionZ * entityIn.motionZ);
            f /= 0.2f;
            f *= f * f;
        }
        if (f < 1.0f) {
            f = 1.0f;
        }
        this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 2.0f * limbSwingAmount * 0.5f / f;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 2.0f * limbSwingAmount * 0.5f / f;
        this.bipedRightArm.rotateAngleZ = 0.0f;
        this.bipedLeftArm.rotateAngleZ = 0.0f;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount / f;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 1.4f * limbSwingAmount / f;
        this.bipedRightLeg.rotateAngleY = 0.0f;
        this.bipedLeftLeg.rotateAngleY = 0.0f;
        this.bipedRightLeg.rotateAngleZ = 0.0f;
        this.bipedLeftLeg.rotateAngleZ = 0.0f;
        if (this.isRiding) {
            final ModelRenderer bipedRightArm = this.bipedRightArm;
            bipedRightArm.rotateAngleX -= 0.62831855f;
            final ModelRenderer bipedLeftArm = this.bipedLeftArm;
            bipedLeftArm.rotateAngleX -= 0.62831855f;
            this.bipedRightLeg.rotateAngleX = -1.4137167f;
            this.bipedRightLeg.rotateAngleY = 0.31415927f;
            this.bipedRightLeg.rotateAngleZ = 0.07853982f;
            this.bipedLeftLeg.rotateAngleX = -1.4137167f;
            this.bipedLeftLeg.rotateAngleY = -0.31415927f;
            this.bipedLeftLeg.rotateAngleZ = -0.07853982f;
        }
        this.bipedRightArm.rotateAngleY = 0.0f;
        this.bipedRightArm.rotateAngleZ = 0.0f;
        switch (this.leftArmPose) {
            case EMPTY: {
                this.bipedLeftArm.rotateAngleY = 0.0f;
                break;
            }
            case BLOCK: {
                this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5f - 0.9424779f;
                this.bipedLeftArm.rotateAngleY = 0.5235988f;
                break;
            }
            case ITEM: {
                this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5f - 0.31415927f;
                this.bipedLeftArm.rotateAngleY = 0.0f;
                break;
            }
        }
        switch (this.rightArmPose) {
            case EMPTY: {
                this.bipedRightArm.rotateAngleY = 0.0f;
                break;
            }
            case BLOCK: {
                this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5f - 0.9424779f;
                this.bipedRightArm.rotateAngleY = -0.5235988f;
                break;
            }
            case ITEM: {
                this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5f - 0.31415927f;
                this.bipedRightArm.rotateAngleY = 0.0f;
                break;
            }
        }
        if (this.swingProgress > 0.0f) {
            final EnumHandSide enumhandside = this.getMainHand(entityIn);
            final ModelRenderer modelrenderer = this.getArmForSide(enumhandside);
            float f2 = this.swingProgress;
            this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt(f2) * 6.2831855f) * 0.2f;
            if (enumhandside == EnumHandSide.LEFT) {
                final ModelRenderer bipedBody = this.bipedBody;
                bipedBody.rotateAngleY *= -1.0f;
            }
            this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0f;
            final ModelRenderer bipedRightArm2 = this.bipedRightArm;
            bipedRightArm2.rotateAngleY += this.bipedBody.rotateAngleY;
            final ModelRenderer bipedLeftArm2 = this.bipedLeftArm;
            bipedLeftArm2.rotateAngleY += this.bipedBody.rotateAngleY;
            final ModelRenderer bipedLeftArm3 = this.bipedLeftArm;
            bipedLeftArm3.rotateAngleX += this.bipedBody.rotateAngleY;
            f2 = 1.0f - this.swingProgress;
            f2 *= f2;
            f2 *= f2;
            f2 = 1.0f - f2;
            final float f3 = MathHelper.sin(f2 * 3.1415927f);
            final float f4 = MathHelper.sin(this.swingProgress * 3.1415927f) * -(this.bipedHead.rotateAngleX - 0.7f) * 0.75f;
            modelrenderer.rotateAngleX -= (float)(f3 * 1.2 + f4);
            final ModelRenderer modelRenderer = modelrenderer;
            modelRenderer.rotateAngleY += this.bipedBody.rotateAngleY * 2.0f;
            final ModelRenderer modelRenderer2 = modelrenderer;
            modelRenderer2.rotateAngleZ += MathHelper.sin(this.swingProgress * 3.1415927f) * -0.4f;
        }
        if (this.isSneak) {
            this.bipedBody.rotateAngleX = 0.5f;
            final ModelRenderer bipedRightArm3 = this.bipedRightArm;
            bipedRightArm3.rotateAngleX += 0.4f;
            final ModelRenderer bipedLeftArm4 = this.bipedLeftArm;
            bipedLeftArm4.rotateAngleX += 0.4f;
            this.bipedRightLeg.rotationPointZ = 4.0f;
            this.bipedLeftLeg.rotationPointZ = 4.0f;
            this.bipedRightLeg.rotationPointY = 9.0f;
            this.bipedLeftLeg.rotationPointY = 9.0f;
            this.bipedHead.rotationPointY = 1.0f;
        }
        else {
            this.bipedBody.rotateAngleX = 0.0f;
            this.bipedRightLeg.rotationPointZ = 0.1f;
            this.bipedLeftLeg.rotationPointZ = 0.1f;
            this.bipedRightLeg.rotationPointY = 12.0f;
            this.bipedLeftLeg.rotationPointY = 12.0f;
            this.bipedHead.rotationPointY = 0.0f;
        }
        final ModelRenderer bipedRightArm4 = this.bipedRightArm;
        bipedRightArm4.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
        final ModelRenderer bipedLeftArm5 = this.bipedLeftArm;
        bipedLeftArm5.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
        final ModelRenderer bipedRightArm5 = this.bipedRightArm;
        bipedRightArm5.rotateAngleX += MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
        final ModelRenderer bipedLeftArm6 = this.bipedLeftArm;
        bipedLeftArm6.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
        if (this.rightArmPose == ArmPose.BOW_AND_ARROW) {
            this.bipedRightArm.rotateAngleY = -0.1f + this.bipedHead.rotateAngleY;
            this.bipedLeftArm.rotateAngleY = 0.1f + this.bipedHead.rotateAngleY + 0.4f;
            this.bipedRightArm.rotateAngleX = -1.5707964f + this.bipedHead.rotateAngleX;
            this.bipedLeftArm.rotateAngleX = -1.5707964f + this.bipedHead.rotateAngleX;
        }
        else if (this.leftArmPose == ArmPose.BOW_AND_ARROW) {
            this.bipedRightArm.rotateAngleY = -0.1f + this.bipedHead.rotateAngleY - 0.4f;
            this.bipedLeftArm.rotateAngleY = 0.1f + this.bipedHead.rotateAngleY;
            this.bipedRightArm.rotateAngleX = -1.5707964f + this.bipedHead.rotateAngleX;
            this.bipedLeftArm.rotateAngleX = -1.5707964f + this.bipedHead.rotateAngleX;
        }
        ModelBase.copyModelAngles(this.bipedHead, this.bipedHeadwear);
    }
    
    @Override
    public void setModelAttributes(final ModelBase model) {
        super.setModelAttributes(model);
        if (model instanceof ModelBiped) {
            final ModelBiped modelbiped = (ModelBiped)model;
            this.leftArmPose = modelbiped.leftArmPose;
            this.rightArmPose = modelbiped.rightArmPose;
            this.isSneak = modelbiped.isSneak;
        }
    }
    
    public void setVisible(final boolean visible) {
        this.bipedHead.showModel = visible;
        this.bipedHeadwear.showModel = visible;
        this.bipedBody.showModel = visible;
        this.bipedRightArm.showModel = visible;
        this.bipedLeftArm.showModel = visible;
        this.bipedRightLeg.showModel = visible;
        this.bipedLeftLeg.showModel = visible;
    }
    
    public void postRenderArm(final float scale, final EnumHandSide side) {
        this.getArmForSide(side).postRender(scale);
    }
    
    protected ModelRenderer getArmForSide(final EnumHandSide side) {
        return (side == EnumHandSide.LEFT) ? this.bipedLeftArm : this.bipedRightArm;
    }
    
    protected EnumHandSide getMainHand(final Entity entityIn) {
        if (entityIn instanceof EntityLivingBase) {
            final EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
            final EnumHandSide enumhandside = entitylivingbase.getPrimaryHand();
            return (entitylivingbase.swingingHand == EnumHand.MAIN_HAND) ? enumhandside : enumhandside.opposite();
        }
        return EnumHandSide.RIGHT;
    }
    
    public enum ArmPose
    {
        EMPTY, 
        ITEM, 
        BLOCK, 
        BOW_AND_ARROW;
    }
}
