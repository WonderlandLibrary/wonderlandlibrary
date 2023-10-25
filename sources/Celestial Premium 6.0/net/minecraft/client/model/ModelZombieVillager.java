/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.MathHelper;

public class ModelZombieVillager
extends ModelBiped {
    public ModelZombieVillager() {
        this(0.0f, 0.0f, false);
    }

    public ModelZombieVillager(float p_i1165_1_, float p_i1165_2_, boolean p_i1165_3_) {
        super(p_i1165_1_, 0.0f, 64, p_i1165_3_ ? 32 : 64);
        if (p_i1165_3_) {
            this.bipedHead = new ModelRenderer(this, 0, 0);
            this.bipedHead.addBox(-4.0f, -10.0f, -4.0f, 8, 8, 8, p_i1165_1_);
            this.bipedHead.setRotationPoint(0.0f, 0.0f + p_i1165_2_, 0.0f);
            this.bipedBody = new ModelRenderer(this, 16, 16);
            this.bipedBody.setRotationPoint(0.0f, 0.0f + p_i1165_2_, 0.0f);
            this.bipedBody.addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, p_i1165_1_ + 0.1f);
            this.bipedRightLeg = new ModelRenderer(this, 0, 16);
            this.bipedRightLeg.setRotationPoint(-2.0f, 12.0f + p_i1165_2_, 0.0f);
            this.bipedRightLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i1165_1_ + 0.1f);
            this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
            this.bipedLeftLeg.mirror = true;
            this.bipedLeftLeg.setRotationPoint(2.0f, 12.0f + p_i1165_2_, 0.0f);
            this.bipedLeftLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i1165_1_ + 0.1f);
        } else {
            this.bipedHead = new ModelRenderer(this, 0, 0);
            this.bipedHead.setRotationPoint(0.0f, p_i1165_2_, 0.0f);
            this.bipedHead.setTextureOffset(0, 0).addBox(-4.0f, -10.0f, -4.0f, 8, 10, 8, p_i1165_1_);
            this.bipedHead.setTextureOffset(24, 0).addBox(-1.0f, -3.0f, -6.0f, 2, 4, 2, p_i1165_1_);
            this.bipedBody = new ModelRenderer(this, 16, 20);
            this.bipedBody.setRotationPoint(0.0f, 0.0f + p_i1165_2_, 0.0f);
            this.bipedBody.addBox(-4.0f, 0.0f, -3.0f, 8, 12, 6, p_i1165_1_);
            this.bipedBody.setTextureOffset(0, 38).addBox(-4.0f, 0.0f, -3.0f, 8, 18, 6, p_i1165_1_ + 0.05f);
            this.bipedRightArm = new ModelRenderer(this, 44, 38);
            this.bipedRightArm.addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, p_i1165_1_);
            this.bipedRightArm.setRotationPoint(-5.0f, 2.0f + p_i1165_2_, 0.0f);
            this.bipedLeftArm = new ModelRenderer(this, 44, 38);
            this.bipedLeftArm.mirror = true;
            this.bipedLeftArm.addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, p_i1165_1_);
            this.bipedLeftArm.setRotationPoint(5.0f, 2.0f + p_i1165_2_, 0.0f);
            this.bipedRightLeg = new ModelRenderer(this, 0, 22);
            this.bipedRightLeg.setRotationPoint(-2.0f, 12.0f + p_i1165_2_, 0.0f);
            this.bipedRightLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i1165_1_);
            this.bipedLeftLeg = new ModelRenderer(this, 0, 22);
            this.bipedLeftLeg.mirror = true;
            this.bipedLeftLeg.setRotationPoint(2.0f, 12.0f + p_i1165_2_, 0.0f);
            this.bipedLeftLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i1165_1_);
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        float f2;
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        EntityZombie entityzombie = (EntityZombie)entityIn;
        float f = MathHelper.sin(this.swingProgress * (float)Math.PI);
        float f1 = MathHelper.sin((1.0f - (1.0f - this.swingProgress) * (1.0f - this.swingProgress)) * (float)Math.PI);
        this.bipedRightArm.rotateAngleZ = 0.0f;
        this.bipedLeftArm.rotateAngleZ = 0.0f;
        this.bipedRightArm.rotateAngleY = -(0.1f - f * 0.6f);
        this.bipedLeftArm.rotateAngleY = 0.1f - f * 0.6f;
        this.bipedRightArm.rotateAngleX = f2 = (float)(-Math.PI) / (entityzombie.isArmsRaised() ? 1.5f : 2.25f);
        this.bipedLeftArm.rotateAngleX = f2;
        this.bipedRightArm.rotateAngleX += f * 1.2f - f1 * 0.4f;
        this.bipedLeftArm.rotateAngleX += f * 1.2f - f1 * 0.4f;
        this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
    }
}

