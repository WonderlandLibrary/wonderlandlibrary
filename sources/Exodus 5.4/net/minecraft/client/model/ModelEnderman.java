/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelEnderman
extends ModelBiped {
    public boolean isAttacking;
    public boolean isCarrying;

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        super.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        this.bipedHead.showModel = true;
        float f7 = -14.0f;
        this.bipedBody.rotateAngleX = 0.0f;
        this.bipedBody.rotationPointY = f7;
        this.bipedBody.rotationPointZ = -0.0f;
        this.bipedRightLeg.rotateAngleX -= 0.0f;
        this.bipedLeftLeg.rotateAngleX -= 0.0f;
        this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX * 0.5);
        this.bipedLeftArm.rotateAngleX = (float)((double)this.bipedLeftArm.rotateAngleX * 0.5);
        this.bipedRightLeg.rotateAngleX = (float)((double)this.bipedRightLeg.rotateAngleX * 0.5);
        this.bipedLeftLeg.rotateAngleX = (float)((double)this.bipedLeftLeg.rotateAngleX * 0.5);
        float f8 = 0.4f;
        if (this.bipedRightArm.rotateAngleX > f8) {
            this.bipedRightArm.rotateAngleX = f8;
        }
        if (this.bipedLeftArm.rotateAngleX > f8) {
            this.bipedLeftArm.rotateAngleX = f8;
        }
        if (this.bipedRightArm.rotateAngleX < -f8) {
            this.bipedRightArm.rotateAngleX = -f8;
        }
        if (this.bipedLeftArm.rotateAngleX < -f8) {
            this.bipedLeftArm.rotateAngleX = -f8;
        }
        if (this.bipedRightLeg.rotateAngleX > f8) {
            this.bipedRightLeg.rotateAngleX = f8;
        }
        if (this.bipedLeftLeg.rotateAngleX > f8) {
            this.bipedLeftLeg.rotateAngleX = f8;
        }
        if (this.bipedRightLeg.rotateAngleX < -f8) {
            this.bipedRightLeg.rotateAngleX = -f8;
        }
        if (this.bipedLeftLeg.rotateAngleX < -f8) {
            this.bipedLeftLeg.rotateAngleX = -f8;
        }
        if (this.isCarrying) {
            this.bipedRightArm.rotateAngleX = -0.5f;
            this.bipedLeftArm.rotateAngleX = -0.5f;
            this.bipedRightArm.rotateAngleZ = 0.05f;
            this.bipedLeftArm.rotateAngleZ = -0.05f;
        }
        this.bipedRightArm.rotationPointZ = 0.0f;
        this.bipedLeftArm.rotationPointZ = 0.0f;
        this.bipedRightLeg.rotationPointZ = 0.0f;
        this.bipedLeftLeg.rotationPointZ = 0.0f;
        this.bipedRightLeg.rotationPointY = 9.0f + f7;
        this.bipedLeftLeg.rotationPointY = 9.0f + f7;
        this.bipedHead.rotationPointZ = -0.0f;
        this.bipedHead.rotationPointY = f7 + 1.0f;
        this.bipedHeadwear.rotationPointX = this.bipedHead.rotationPointX;
        this.bipedHeadwear.rotationPointY = this.bipedHead.rotationPointY;
        this.bipedHeadwear.rotationPointZ = this.bipedHead.rotationPointZ;
        this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
        this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
        this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ;
        if (this.isAttacking) {
            float f9 = 1.0f;
            this.bipedHead.rotationPointY -= f9 * 5.0f;
        }
    }

    public ModelEnderman(float f) {
        super(0.0f, -14.0f, 64, 32);
        float f2 = -14.0f;
        this.bipedHeadwear = new ModelRenderer(this, 0, 16);
        this.bipedHeadwear.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f - 0.5f);
        this.bipedHeadwear.setRotationPoint(0.0f, 0.0f + f2, 0.0f);
        this.bipedBody = new ModelRenderer(this, 32, 16);
        this.bipedBody.addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, f);
        this.bipedBody.setRotationPoint(0.0f, 0.0f + f2, 0.0f);
        this.bipedRightArm = new ModelRenderer(this, 56, 0);
        this.bipedRightArm.addBox(-1.0f, -2.0f, -1.0f, 2, 30, 2, f);
        this.bipedRightArm.setRotationPoint(-3.0f, 2.0f + f2, 0.0f);
        this.bipedLeftArm = new ModelRenderer(this, 56, 0);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0f, -2.0f, -1.0f, 2, 30, 2, f);
        this.bipedLeftArm.setRotationPoint(5.0f, 2.0f + f2, 0.0f);
        this.bipedRightLeg = new ModelRenderer(this, 56, 0);
        this.bipedRightLeg.addBox(-1.0f, 0.0f, -1.0f, 2, 30, 2, f);
        this.bipedRightLeg.setRotationPoint(-2.0f, 12.0f + f2, 0.0f);
        this.bipedLeftLeg = new ModelRenderer(this, 56, 0);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-1.0f, 0.0f, -1.0f, 2, 30, 2, f);
        this.bipedLeftLeg.setRotationPoint(2.0f, 12.0f + f2, 0.0f);
    }
}

