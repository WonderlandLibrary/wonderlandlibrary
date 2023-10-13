/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelArmorStandArmor;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelArmorStand
extends ModelArmorStandArmor {
    public ModelRenderer standRightSide;
    public ModelRenderer standLeftSide;
    public ModelRenderer standWaist;
    public ModelRenderer standBase;
    private static final String __OBFID = "CL_00002631";

    public ModelArmorStand() {
        this(0.0f);
    }

    public ModelArmorStand(float p_i46306_1_) {
        super(p_i46306_1_, 64, 64);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-1.0f, -7.0f, -1.0f, 2, 7, 2, p_i46306_1_);
        this.bipedHead.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.bipedBody = new ModelRenderer(this, 0, 26);
        this.bipedBody.addBox(-6.0f, 0.0f, -1.5f, 12, 3, 3, p_i46306_1_);
        this.bipedBody.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.bipedRightArm = new ModelRenderer(this, 24, 0);
        this.bipedRightArm.addBox(-2.0f, -2.0f, -1.0f, 2, 12, 2, p_i46306_1_);
        this.bipedRightArm.setRotationPoint(-5.0f, 2.0f, 0.0f);
        this.bipedLeftArm = new ModelRenderer(this, 32, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(0.0f, -2.0f, -1.0f, 2, 12, 2, p_i46306_1_);
        this.bipedLeftArm.setRotationPoint(5.0f, 2.0f, 0.0f);
        this.bipedRightLeg = new ModelRenderer(this, 8, 0);
        this.bipedRightLeg.addBox(-1.0f, 0.0f, -1.0f, 2, 11, 2, p_i46306_1_);
        this.bipedRightLeg.setRotationPoint(-1.9f, 12.0f, 0.0f);
        this.bipedLeftLeg = new ModelRenderer(this, 40, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-1.0f, 0.0f, -1.0f, 2, 11, 2, p_i46306_1_);
        this.bipedLeftLeg.setRotationPoint(1.9f, 12.0f, 0.0f);
        this.standRightSide = new ModelRenderer(this, 16, 0);
        this.standRightSide.addBox(-3.0f, 3.0f, -1.0f, 2, 7, 2, p_i46306_1_);
        this.standRightSide.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.standRightSide.showModel = true;
        this.standLeftSide = new ModelRenderer(this, 48, 16);
        this.standLeftSide.addBox(1.0f, 3.0f, -1.0f, 2, 7, 2, p_i46306_1_);
        this.standLeftSide.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.standWaist = new ModelRenderer(this, 0, 48);
        this.standWaist.addBox(-4.0f, 10.0f, -1.0f, 8, 2, 2, p_i46306_1_);
        this.standWaist.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.standBase = new ModelRenderer(this, 0, 32);
        this.standBase.addBox(-6.0f, 11.0f, -6.0f, 12, 1, 12, p_i46306_1_);
        this.standBase.setRotationPoint(0.0f, 12.0f, 0.0f);
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        if (p_78087_7_ instanceof EntityArmorStand) {
            EntityArmorStand var8 = (EntityArmorStand)p_78087_7_;
            this.bipedLeftArm.showModel = var8.getShowArms();
            this.bipedRightArm.showModel = var8.getShowArms();
            this.standBase.showModel = !var8.hasNoBasePlate();
            this.bipedLeftLeg.setRotationPoint(1.9f, 12.0f, 0.0f);
            this.bipedRightLeg.setRotationPoint(-1.9f, 12.0f, 0.0f);
            this.standRightSide.rotateAngleX = (float)Math.PI / 180 * var8.getBodyRotation().func_179415_b();
            this.standRightSide.rotateAngleY = (float)Math.PI / 180 * var8.getBodyRotation().func_179416_c();
            this.standRightSide.rotateAngleZ = (float)Math.PI / 180 * var8.getBodyRotation().func_179413_d();
            this.standLeftSide.rotateAngleX = (float)Math.PI / 180 * var8.getBodyRotation().func_179415_b();
            this.standLeftSide.rotateAngleY = (float)Math.PI / 180 * var8.getBodyRotation().func_179416_c();
            this.standLeftSide.rotateAngleZ = (float)Math.PI / 180 * var8.getBodyRotation().func_179413_d();
            this.standWaist.rotateAngleX = (float)Math.PI / 180 * var8.getBodyRotation().func_179415_b();
            this.standWaist.rotateAngleY = (float)Math.PI / 180 * var8.getBodyRotation().func_179416_c();
            this.standWaist.rotateAngleZ = (float)Math.PI / 180 * var8.getBodyRotation().func_179413_d();
            float var9 = (var8.getLeftLegRotation().func_179415_b() + var8.getRightLegRotation().func_179415_b()) / 2.0f;
            float var10 = (var8.getLeftLegRotation().func_179416_c() + var8.getRightLegRotation().func_179416_c()) / 2.0f;
            float var11 = (var8.getLeftLegRotation().func_179413_d() + var8.getRightLegRotation().func_179413_d()) / 2.0f;
            this.standBase.rotateAngleX = 0.0f;
            this.standBase.rotateAngleY = (float)Math.PI / 180 * -p_78087_7_.rotationYaw;
            this.standBase.rotateAngleZ = 0.0f;
        }
    }

    @Override
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        super.render(p_78088_1_, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_);
        GlStateManager.pushMatrix();
        if (this.isChild) {
            float var8 = 2.0f;
            GlStateManager.scale(1.0f / var8, 1.0f / var8, 1.0f / var8);
            GlStateManager.translate(0.0f, 24.0f * p_78088_7_, 0.0f);
            this.standRightSide.render(p_78088_7_);
            this.standLeftSide.render(p_78088_7_);
            this.standWaist.render(p_78088_7_);
            this.standBase.render(p_78088_7_);
        } else {
            if (p_78088_1_.isSneaking()) {
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
            }
            this.standRightSide.render(p_78088_7_);
            this.standLeftSide.render(p_78088_7_);
            this.standWaist.render(p_78088_7_);
            this.standBase.render(p_78088_7_);
        }
        GlStateManager.popMatrix();
    }

    @Override
    public void postRenderHiddenArm(float p_178718_1_) {
        boolean var2 = this.bipedRightArm.showModel;
        this.bipedRightArm.showModel = true;
        super.postRenderHiddenArm(p_178718_1_);
        this.bipedRightArm.showModel = var2;
    }
}

