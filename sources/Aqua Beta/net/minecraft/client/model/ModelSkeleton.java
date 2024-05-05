package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;

public class ModelSkeleton
extends ModelZombie {
    public ModelSkeleton() {
        this(0.0f, false);
    }

    public ModelSkeleton(float p_i46303_1_, boolean p_i46303_2_) {
        super(p_i46303_1_, 0.0f, 64, 32);
        if (!p_i46303_2_) {
            this.bipedRightArm = new ModelRenderer((ModelBase)this, 40, 16);
            this.bipedRightArm.addBox(-1.0f, -2.0f, -1.0f, 2, 12, 2, p_i46303_1_);
            this.bipedRightArm.setRotationPoint(-5.0f, 2.0f, 0.0f);
            this.bipedLeftArm = new ModelRenderer((ModelBase)this, 40, 16);
            this.bipedLeftArm.mirror = true;
            this.bipedLeftArm.addBox(-1.0f, -2.0f, -1.0f, 2, 12, 2, p_i46303_1_);
            this.bipedLeftArm.setRotationPoint(5.0f, 2.0f, 0.0f);
            this.bipedRightLeg = new ModelRenderer((ModelBase)this, 0, 16);
            this.bipedRightLeg.addBox(-1.0f, 0.0f, -1.0f, 2, 12, 2, p_i46303_1_);
            this.bipedRightLeg.setRotationPoint(-2.0f, 12.0f, 0.0f);
            this.bipedLeftLeg = new ModelRenderer((ModelBase)this, 0, 16);
            this.bipedLeftLeg.mirror = true;
            this.bipedLeftLeg.addBox(-1.0f, 0.0f, -1.0f, 2, 12, 2, p_i46303_1_);
            this.bipedLeftLeg.setRotationPoint(2.0f, 12.0f, 0.0f);
        }
    }

    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {
        this.aimedBow = ((EntitySkeleton)entitylivingbaseIn).getSkeletonType() == 1;
        super.setLivingAnimations(entitylivingbaseIn, p_78086_2_, p_78086_3_, partialTickTime);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
    }
}
