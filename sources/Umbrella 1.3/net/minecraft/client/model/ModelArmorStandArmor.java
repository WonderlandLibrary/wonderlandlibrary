/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelArmorStandArmor
extends ModelBiped {
    private static final String __OBFID = "CL_00002632";

    public ModelArmorStandArmor() {
        this(0.0f);
    }

    public ModelArmorStandArmor(float p_i46307_1_) {
        this(p_i46307_1_, 64, 32);
    }

    protected ModelArmorStandArmor(float p_i46308_1_, int p_i46308_2_, int p_i46308_3_) {
        super(p_i46308_1_, 0.0f, p_i46308_2_, p_i46308_3_);
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        if (p_78087_7_ instanceof EntityArmorStand) {
            EntityArmorStand var8 = (EntityArmorStand)p_78087_7_;
            this.bipedHead.rotateAngleX = (float)Math.PI / 180 * var8.getHeadRotation().func_179415_b();
            this.bipedHead.rotateAngleY = (float)Math.PI / 180 * var8.getHeadRotation().func_179416_c();
            this.bipedHead.rotateAngleZ = (float)Math.PI / 180 * var8.getHeadRotation().func_179413_d();
            this.bipedHead.setRotationPoint(0.0f, 1.0f, 0.0f);
            this.bipedBody.rotateAngleX = (float)Math.PI / 180 * var8.getBodyRotation().func_179415_b();
            this.bipedBody.rotateAngleY = (float)Math.PI / 180 * var8.getBodyRotation().func_179416_c();
            this.bipedBody.rotateAngleZ = (float)Math.PI / 180 * var8.getBodyRotation().func_179413_d();
            this.bipedLeftArm.rotateAngleX = (float)Math.PI / 180 * var8.getLeftArmRotation().func_179415_b();
            this.bipedLeftArm.rotateAngleY = (float)Math.PI / 180 * var8.getLeftArmRotation().func_179416_c();
            this.bipedLeftArm.rotateAngleZ = (float)Math.PI / 180 * var8.getLeftArmRotation().func_179413_d();
            this.bipedRightArm.rotateAngleX = (float)Math.PI / 180 * var8.getRightArmRotation().func_179415_b();
            this.bipedRightArm.rotateAngleY = (float)Math.PI / 180 * var8.getRightArmRotation().func_179416_c();
            this.bipedRightArm.rotateAngleZ = (float)Math.PI / 180 * var8.getRightArmRotation().func_179413_d();
            this.bipedLeftLeg.rotateAngleX = (float)Math.PI / 180 * var8.getLeftLegRotation().func_179415_b();
            this.bipedLeftLeg.rotateAngleY = (float)Math.PI / 180 * var8.getLeftLegRotation().func_179416_c();
            this.bipedLeftLeg.rotateAngleZ = (float)Math.PI / 180 * var8.getLeftLegRotation().func_179413_d();
            this.bipedLeftLeg.setRotationPoint(1.9f, 11.0f, 0.0f);
            this.bipedRightLeg.rotateAngleX = (float)Math.PI / 180 * var8.getRightLegRotation().func_179415_b();
            this.bipedRightLeg.rotateAngleY = (float)Math.PI / 180 * var8.getRightLegRotation().func_179416_c();
            this.bipedRightLeg.rotateAngleZ = (float)Math.PI / 180 * var8.getRightLegRotation().func_179413_d();
            this.bipedRightLeg.setRotationPoint(-1.9f, 11.0f, 0.0f);
            ModelArmorStandArmor.func_178685_a(this.bipedHead, this.bipedHeadwear);
        }
    }
}

