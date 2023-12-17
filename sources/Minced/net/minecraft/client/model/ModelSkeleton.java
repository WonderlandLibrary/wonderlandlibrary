// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.EntityLivingBase;

public class ModelSkeleton extends ModelBiped
{
    public ModelSkeleton() {
        this(0.0f, false);
    }
    
    public ModelSkeleton(final float modelSize, final boolean p_i46303_2_) {
        super(modelSize, 0.0f, 64, 32);
        if (!p_i46303_2_) {
            (this.bipedRightArm = new ModelRenderer(this, 40, 16)).addBox(-1.0f, -2.0f, -1.0f, 2, 12, 2, modelSize);
            this.bipedRightArm.setRotationPoint(-5.0f, 2.0f, 0.0f);
            this.bipedLeftArm = new ModelRenderer(this, 40, 16);
            this.bipedLeftArm.mirror = true;
            this.bipedLeftArm.addBox(-1.0f, -2.0f, -1.0f, 2, 12, 2, modelSize);
            this.bipedLeftArm.setRotationPoint(5.0f, 2.0f, 0.0f);
            (this.bipedRightLeg = new ModelRenderer(this, 0, 16)).addBox(-1.0f, 0.0f, -1.0f, 2, 12, 2, modelSize);
            this.bipedRightLeg.setRotationPoint(-2.0f, 12.0f, 0.0f);
            this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
            this.bipedLeftLeg.mirror = true;
            this.bipedLeftLeg.addBox(-1.0f, 0.0f, -1.0f, 2, 12, 2, modelSize);
            this.bipedLeftLeg.setRotationPoint(2.0f, 12.0f, 0.0f);
        }
    }
    
    @Override
    public void setLivingAnimations(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTickTime) {
        this.rightArmPose = ArmPose.EMPTY;
        this.leftArmPose = ArmPose.EMPTY;
        final ItemStack itemstack = entitylivingbaseIn.getHeldItem(EnumHand.MAIN_HAND);
        if (itemstack.getItem() == Items.BOW && ((AbstractSkeleton)entitylivingbaseIn).isSwingingArms()) {
            if (entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT) {
                this.rightArmPose = ArmPose.BOW_AND_ARROW;
            }
            else {
                this.leftArmPose = ArmPose.BOW_AND_ARROW;
            }
        }
        super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        final ItemStack itemstack = ((EntityLivingBase)entityIn).getHeldItemMainhand();
        final AbstractSkeleton abstractskeleton = (AbstractSkeleton)entityIn;
        if (abstractskeleton.isSwingingArms() && (itemstack.isEmpty() || itemstack.getItem() != Items.BOW)) {
            final float f = MathHelper.sin(this.swingProgress * 3.1415927f);
            final float f2 = MathHelper.sin((1.0f - (1.0f - this.swingProgress) * (1.0f - this.swingProgress)) * 3.1415927f);
            this.bipedRightArm.rotateAngleZ = 0.0f;
            this.bipedLeftArm.rotateAngleZ = 0.0f;
            this.bipedRightArm.rotateAngleY = -(0.1f - f * 0.6f);
            this.bipedLeftArm.rotateAngleY = 0.1f - f * 0.6f;
            this.bipedRightArm.rotateAngleX = -1.5707964f;
            this.bipedLeftArm.rotateAngleX = -1.5707964f;
            final ModelRenderer bipedRightArm = this.bipedRightArm;
            bipedRightArm.rotateAngleX -= f * 1.2f - f2 * 0.4f;
            final ModelRenderer bipedLeftArm = this.bipedLeftArm;
            bipedLeftArm.rotateAngleX -= f * 1.2f - f2 * 0.4f;
            final ModelRenderer bipedRightArm2 = this.bipedRightArm;
            bipedRightArm2.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
            final ModelRenderer bipedLeftArm2 = this.bipedLeftArm;
            bipedLeftArm2.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
            final ModelRenderer bipedRightArm3 = this.bipedRightArm;
            bipedRightArm3.rotateAngleX += MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
            final ModelRenderer bipedLeftArm3 = this.bipedLeftArm;
            bipedLeftArm3.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
        }
    }
    
    @Override
    public void postRenderArm(final float scale, final EnumHandSide side) {
        final float f = (side == EnumHandSide.RIGHT) ? 1.0f : -1.0f;
        final ModelRenderer armForSide;
        final ModelRenderer modelrenderer = armForSide = this.getArmForSide(side);
        armForSide.rotationPointX += f;
        modelrenderer.postRender(scale);
        final ModelRenderer modelRenderer = modelrenderer;
        modelRenderer.rotationPointX -= f;
    }
}
