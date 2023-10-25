/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelArmorStandArmor
extends ModelBiped {
    protected ModelArmorStandArmor(float f, int n, int n2) {
        super(f, 0.0f, n, n2);
    }

    public ModelArmorStandArmor(float f) {
        this(f, 64, 32);
    }

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        if (entity instanceof EntityArmorStand) {
            EntityArmorStand entityArmorStand = (EntityArmorStand)entity;
            this.bipedHead.rotateAngleX = (float)Math.PI / 180 * entityArmorStand.getHeadRotation().getX();
            this.bipedHead.rotateAngleY = (float)Math.PI / 180 * entityArmorStand.getHeadRotation().getY();
            this.bipedHead.rotateAngleZ = (float)Math.PI / 180 * entityArmorStand.getHeadRotation().getZ();
            this.bipedHead.setRotationPoint(0.0f, 1.0f, 0.0f);
            this.bipedBody.rotateAngleX = (float)Math.PI / 180 * entityArmorStand.getBodyRotation().getX();
            this.bipedBody.rotateAngleY = (float)Math.PI / 180 * entityArmorStand.getBodyRotation().getY();
            this.bipedBody.rotateAngleZ = (float)Math.PI / 180 * entityArmorStand.getBodyRotation().getZ();
            this.bipedLeftArm.rotateAngleX = (float)Math.PI / 180 * entityArmorStand.getLeftArmRotation().getX();
            this.bipedLeftArm.rotateAngleY = (float)Math.PI / 180 * entityArmorStand.getLeftArmRotation().getY();
            this.bipedLeftArm.rotateAngleZ = (float)Math.PI / 180 * entityArmorStand.getLeftArmRotation().getZ();
            this.bipedRightArm.rotateAngleX = (float)Math.PI / 180 * entityArmorStand.getRightArmRotation().getX();
            this.bipedRightArm.rotateAngleY = (float)Math.PI / 180 * entityArmorStand.getRightArmRotation().getY();
            this.bipedRightArm.rotateAngleZ = (float)Math.PI / 180 * entityArmorStand.getRightArmRotation().getZ();
            this.bipedLeftLeg.rotateAngleX = (float)Math.PI / 180 * entityArmorStand.getLeftLegRotation().getX();
            this.bipedLeftLeg.rotateAngleY = (float)Math.PI / 180 * entityArmorStand.getLeftLegRotation().getY();
            this.bipedLeftLeg.rotateAngleZ = (float)Math.PI / 180 * entityArmorStand.getLeftLegRotation().getZ();
            this.bipedLeftLeg.setRotationPoint(1.9f, 11.0f, 0.0f);
            this.bipedRightLeg.rotateAngleX = (float)Math.PI / 180 * entityArmorStand.getRightLegRotation().getX();
            this.bipedRightLeg.rotateAngleY = (float)Math.PI / 180 * entityArmorStand.getRightLegRotation().getY();
            this.bipedRightLeg.rotateAngleZ = (float)Math.PI / 180 * entityArmorStand.getRightLegRotation().getZ();
            this.bipedRightLeg.setRotationPoint(-1.9f, 11.0f, 0.0f);
            ModelArmorStandArmor.copyModelAngles(this.bipedHead, this.bipedHeadwear);
        }
    }

    public ModelArmorStandArmor() {
        this(0.0f);
    }
}

