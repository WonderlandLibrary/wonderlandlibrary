// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.Entity;

public class ModelZombieVillager extends ModelBiped
{
    public ModelZombieVillager() {
        this(0.0f, 0.0f, false);
    }
    
    public ModelZombieVillager(final float p_i1165_1_, final float p_i1165_2_, final boolean p_i1165_3_) {
        super(p_i1165_1_, 0.0f, 64, p_i1165_3_ ? 32 : 64);
        if (p_i1165_3_) {
            (this.bipedHead = new ModelRenderer(this, 0, 0)).addBox(-4.0f, -10.0f, -4.0f, 8, 8, 8, p_i1165_1_);
            this.bipedHead.setRotationPoint(0.0f, 0.0f + p_i1165_2_, 0.0f);
            (this.bipedBody = new ModelRenderer(this, 16, 16)).setRotationPoint(0.0f, 0.0f + p_i1165_2_, 0.0f);
            this.bipedBody.addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, p_i1165_1_ + 0.1f);
            (this.bipedRightLeg = new ModelRenderer(this, 0, 16)).setRotationPoint(-2.0f, 12.0f + p_i1165_2_, 0.0f);
            this.bipedRightLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i1165_1_ + 0.1f);
            this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
            this.bipedLeftLeg.mirror = true;
            this.bipedLeftLeg.setRotationPoint(2.0f, 12.0f + p_i1165_2_, 0.0f);
            this.bipedLeftLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i1165_1_ + 0.1f);
        }
        else {
            (this.bipedHead = new ModelRenderer(this, 0, 0)).setRotationPoint(0.0f, p_i1165_2_, 0.0f);
            this.bipedHead.setTextureOffset(0, 0).addBox(-4.0f, -10.0f, -4.0f, 8, 10, 8, p_i1165_1_);
            this.bipedHead.setTextureOffset(24, 0).addBox(-1.0f, -3.0f, -6.0f, 2, 4, 2, p_i1165_1_);
            (this.bipedBody = new ModelRenderer(this, 16, 20)).setRotationPoint(0.0f, 0.0f + p_i1165_2_, 0.0f);
            this.bipedBody.addBox(-4.0f, 0.0f, -3.0f, 8, 12, 6, p_i1165_1_);
            this.bipedBody.setTextureOffset(0, 38).addBox(-4.0f, 0.0f, -3.0f, 8, 18, 6, p_i1165_1_ + 0.05f);
            (this.bipedRightArm = new ModelRenderer(this, 44, 38)).addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, p_i1165_1_);
            this.bipedRightArm.setRotationPoint(-5.0f, 2.0f + p_i1165_2_, 0.0f);
            this.bipedLeftArm = new ModelRenderer(this, 44, 38);
            this.bipedLeftArm.mirror = true;
            this.bipedLeftArm.addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, p_i1165_1_);
            this.bipedLeftArm.setRotationPoint(5.0f, 2.0f + p_i1165_2_, 0.0f);
            (this.bipedRightLeg = new ModelRenderer(this, 0, 22)).setRotationPoint(-2.0f, 12.0f + p_i1165_2_, 0.0f);
            this.bipedRightLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i1165_1_);
            this.bipedLeftLeg = new ModelRenderer(this, 0, 22);
            this.bipedLeftLeg.mirror = true;
            this.bipedLeftLeg.setRotationPoint(2.0f, 12.0f + p_i1165_2_, 0.0f);
            this.bipedLeftLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i1165_1_);
        }
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        final EntityZombie entityzombie = (EntityZombie)entityIn;
        final float f = MathHelper.sin(this.swingProgress * 3.1415927f);
        final float f2 = MathHelper.sin((1.0f - (1.0f - this.swingProgress) * (1.0f - this.swingProgress)) * 3.1415927f);
        this.bipedRightArm.rotateAngleZ = 0.0f;
        this.bipedLeftArm.rotateAngleZ = 0.0f;
        this.bipedRightArm.rotateAngleY = -(0.1f - f * 0.6f);
        this.bipedLeftArm.rotateAngleY = 0.1f - f * 0.6f;
        final float f3 = -3.1415927f / (entityzombie.isArmsRaised() ? 1.5f : 2.25f);
        this.bipedRightArm.rotateAngleX = f3;
        this.bipedLeftArm.rotateAngleX = f3;
        final ModelRenderer bipedRightArm = this.bipedRightArm;
        bipedRightArm.rotateAngleX += f * 1.2f - f2 * 0.4f;
        final ModelRenderer bipedLeftArm = this.bipedLeftArm;
        bipedLeftArm.rotateAngleX += f * 1.2f - f2 * 0.4f;
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
