package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelZombieVillager
extends ModelBiped {
    public ModelZombieVillager() {
        this(0.0f, 0.0f, false);
    }

    public ModelZombieVillager(float p_i1165_1_, float p_i1165_2_, boolean p_i1165_3_) {
        super(p_i1165_1_, 0.0f, 64, p_i1165_3_ ? 32 : 64);
        if (p_i1165_3_) {
            this.bipedHead = new ModelRenderer((ModelBase)this, 0, 0);
            this.bipedHead.addBox(-4.0f, -10.0f, -4.0f, 8, 8, 8, p_i1165_1_);
            this.bipedHead.setRotationPoint(0.0f, 0.0f + p_i1165_2_, 0.0f);
        } else {
            this.bipedHead = new ModelRenderer((ModelBase)this);
            this.bipedHead.setRotationPoint(0.0f, 0.0f + p_i1165_2_, 0.0f);
            this.bipedHead.setTextureOffset(0, 32).addBox(-4.0f, -10.0f, -4.0f, 8, 10, 8, p_i1165_1_);
            this.bipedHead.setTextureOffset(24, 32).addBox(-1.0f, -3.0f, -6.0f, 2, 4, 2, p_i1165_1_);
        }
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        float f = MathHelper.sin((float)(this.swingProgress * (float)Math.PI));
        float f1 = MathHelper.sin((float)((1.0f - (1.0f - this.swingProgress) * (1.0f - this.swingProgress)) * (float)Math.PI));
        this.bipedRightArm.rotateAngleZ = 0.0f;
        this.bipedLeftArm.rotateAngleZ = 0.0f;
        this.bipedRightArm.rotateAngleY = -(0.1f - f * 0.6f);
        this.bipedLeftArm.rotateAngleY = 0.1f - f * 0.6f;
        this.bipedRightArm.rotateAngleX = -1.5707964f;
        this.bipedLeftArm.rotateAngleX = -1.5707964f;
        this.bipedRightArm.rotateAngleX -= f * 1.2f - f1 * 0.4f;
        this.bipedLeftArm.rotateAngleX -= f * 1.2f - f1 * 0.4f;
        this.bipedRightArm.rotateAngleZ += MathHelper.cos((float)(ageInTicks * 0.09f)) * 0.05f + 0.05f;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos((float)(ageInTicks * 0.09f)) * 0.05f + 0.05f;
        this.bipedRightArm.rotateAngleX += MathHelper.sin((float)(ageInTicks * 0.067f)) * 0.05f;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin((float)(ageInTicks * 0.067f)) * 0.05f;
    }
}
