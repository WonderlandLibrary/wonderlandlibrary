/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;

public class ModelIronGolem
extends ModelBase {
    public ModelRenderer ironGolemRightArm;
    public ModelRenderer ironGolemRightLeg;
    public ModelRenderer ironGolemLeftLeg;
    public ModelRenderer ironGolemBody;
    public ModelRenderer ironGolemLeftArm;
    public ModelRenderer ironGolemHead;

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        this.ironGolemHead.rotateAngleY = f4 / 57.295776f;
        this.ironGolemHead.rotateAngleX = f5 / 57.295776f;
        this.ironGolemLeftLeg.rotateAngleX = -1.5f * this.func_78172_a(f, 13.0f) * f2;
        this.ironGolemRightLeg.rotateAngleX = 1.5f * this.func_78172_a(f, 13.0f) * f2;
        this.ironGolemLeftLeg.rotateAngleY = 0.0f;
        this.ironGolemRightLeg.rotateAngleY = 0.0f;
    }

    public ModelIronGolem(float f) {
        this(f, -7.0f);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entityLivingBase, float f, float f2, float f3) {
        EntityIronGolem entityIronGolem = (EntityIronGolem)entityLivingBase;
        int n = entityIronGolem.getAttackTimer();
        if (n > 0) {
            this.ironGolemRightArm.rotateAngleX = -2.0f + 1.5f * this.func_78172_a((float)n - f3, 10.0f);
            this.ironGolemLeftArm.rotateAngleX = -2.0f + 1.5f * this.func_78172_a((float)n - f3, 10.0f);
        } else {
            int n2 = entityIronGolem.getHoldRoseTick();
            if (n2 > 0) {
                this.ironGolemRightArm.rotateAngleX = -0.8f + 0.025f * this.func_78172_a(n2, 70.0f);
                this.ironGolemLeftArm.rotateAngleX = 0.0f;
            } else {
                this.ironGolemRightArm.rotateAngleX = (-0.2f + 1.5f * this.func_78172_a(f, 13.0f)) * f2;
                this.ironGolemLeftArm.rotateAngleX = (-0.2f - 1.5f * this.func_78172_a(f, 13.0f)) * f2;
            }
        }
    }

    public ModelIronGolem(float f, float f2) {
        int n = 128;
        int n2 = 128;
        this.ironGolemHead = new ModelRenderer(this).setTextureSize(n, n2);
        this.ironGolemHead.setRotationPoint(0.0f, 0.0f + f2, -2.0f);
        this.ironGolemHead.setTextureOffset(0, 0).addBox(-4.0f, -12.0f, -5.5f, 8, 10, 8, f);
        this.ironGolemHead.setTextureOffset(24, 0).addBox(-1.0f, -5.0f, -7.5f, 2, 4, 2, f);
        this.ironGolemBody = new ModelRenderer(this).setTextureSize(n, n2);
        this.ironGolemBody.setRotationPoint(0.0f, 0.0f + f2, 0.0f);
        this.ironGolemBody.setTextureOffset(0, 40).addBox(-9.0f, -2.0f, -6.0f, 18, 12, 11, f);
        this.ironGolemBody.setTextureOffset(0, 70).addBox(-4.5f, 10.0f, -3.0f, 9, 5, 6, f + 0.5f);
        this.ironGolemRightArm = new ModelRenderer(this).setTextureSize(n, n2);
        this.ironGolemRightArm.setRotationPoint(0.0f, -7.0f, 0.0f);
        this.ironGolemRightArm.setTextureOffset(60, 21).addBox(-13.0f, -2.5f, -3.0f, 4, 30, 6, f);
        this.ironGolemLeftArm = new ModelRenderer(this).setTextureSize(n, n2);
        this.ironGolemLeftArm.setRotationPoint(0.0f, -7.0f, 0.0f);
        this.ironGolemLeftArm.setTextureOffset(60, 58).addBox(9.0f, -2.5f, -3.0f, 4, 30, 6, f);
        this.ironGolemLeftLeg = new ModelRenderer(this, 0, 22).setTextureSize(n, n2);
        this.ironGolemLeftLeg.setRotationPoint(-4.0f, 18.0f + f2, 0.0f);
        this.ironGolemLeftLeg.setTextureOffset(37, 0).addBox(-3.5f, -3.0f, -3.0f, 6, 16, 5, f);
        this.ironGolemRightLeg = new ModelRenderer(this, 0, 22).setTextureSize(n, n2);
        this.ironGolemRightLeg.mirror = true;
        this.ironGolemRightLeg.setTextureOffset(60, 0).setRotationPoint(5.0f, 18.0f + f2, 0.0f);
        this.ironGolemRightLeg.addBox(-3.5f, -3.0f, -3.0f, 6, 16, 5, f);
    }

    private float func_78172_a(float f, float f2) {
        return (Math.abs(f % f2 - f2 * 0.5f) - f2 * 0.25f) / (f2 * 0.25f);
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        this.ironGolemHead.render(f6);
        this.ironGolemBody.render(f6);
        this.ironGolemLeftLeg.render(f6);
        this.ironGolemRightLeg.render(f6);
        this.ironGolemRightArm.render(f6);
        this.ironGolemLeftArm.render(f6);
    }

    public ModelIronGolem() {
        this(0.0f);
    }
}

