/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelZombie
extends ModelBiped {
    private static final String __OBFID = "CL_00000869";

    public ModelZombie() {
        this(0.0f, false);
    }

    protected ModelZombie(float p_i1167_1_, float p_i1167_2_, int p_i1167_3_, int p_i1167_4_) {
        super(p_i1167_1_, p_i1167_2_, p_i1167_3_, p_i1167_4_);
    }

    public ModelZombie(float p_i1168_1_, boolean p_i1168_2_) {
        super(p_i1168_1_, 0.0f, 64, p_i1168_2_ ? 32 : 64);
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        float var8 = MathHelper.sin(this.swingProgress * (float)Math.PI);
        float var9 = MathHelper.sin((1.0f - (1.0f - this.swingProgress) * (1.0f - this.swingProgress)) * (float)Math.PI);
        this.bipedRightArm.rotateAngleZ = 0.0f;
        this.bipedLeftArm.rotateAngleZ = 0.0f;
        this.bipedRightArm.rotateAngleY = -(0.1f - var8 * 0.6f);
        this.bipedLeftArm.rotateAngleY = 0.1f - var8 * 0.6f;
        this.bipedRightArm.rotateAngleX = -1.5707964f;
        this.bipedLeftArm.rotateAngleX = -1.5707964f;
        this.bipedRightArm.rotateAngleX -= var8 * 1.2f - var9 * 0.4f;
        this.bipedLeftArm.rotateAngleX -= var8 * 1.2f - var9 * 0.4f;
        this.bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09f) * 0.05f + 0.05f;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09f) * 0.05f + 0.05f;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067f) * 0.05f;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067f) * 0.05f;
    }
}

