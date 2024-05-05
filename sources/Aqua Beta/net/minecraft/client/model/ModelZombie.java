package net.minecraft.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelZombie
extends ModelBiped {
    public ModelZombie() {
        this(0.0f, false);
    }

    protected ModelZombie(float modelSize, float p_i1167_2_, int textureWidthIn, int textureHeightIn) {
        super(modelSize, p_i1167_2_, textureWidthIn, textureHeightIn);
    }

    public ModelZombie(float modelSize, boolean p_i1168_2_) {
        super(modelSize, 0.0f, 64, p_i1168_2_ ? 32 : 64);
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
