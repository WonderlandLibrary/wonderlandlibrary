/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.MathHelper;

public class ModelWolf
extends ModelBase {
    public ModelRenderer wolfHeadMain;
    public ModelRenderer wolfBody;
    public ModelRenderer wolfLeg1;
    public ModelRenderer wolfLeg2;
    public ModelRenderer wolfLeg3;
    public ModelRenderer wolfLeg4;
    ModelRenderer wolfTail;
    ModelRenderer wolfMane;
    private static final String __OBFID = "CL_00000868";

    public ModelWolf() {
        float var1 = 0.0f;
        float var2 = 13.5f;
        this.wolfHeadMain = new ModelRenderer(this, 0, 0);
        this.wolfHeadMain.addBox(-3.0f, -3.0f, -2.0f, 6, 6, 4, var1);
        this.wolfHeadMain.setRotationPoint(-1.0f, var2, -7.0f);
        this.wolfBody = new ModelRenderer(this, 18, 14);
        this.wolfBody.addBox(-4.0f, -2.0f, -3.0f, 6, 9, 6, var1);
        this.wolfBody.setRotationPoint(0.0f, 14.0f, 2.0f);
        this.wolfMane = new ModelRenderer(this, 21, 0);
        this.wolfMane.addBox(-4.0f, -3.0f, -3.0f, 8, 6, 7, var1);
        this.wolfMane.setRotationPoint(-1.0f, 14.0f, 2.0f);
        this.wolfLeg1 = new ModelRenderer(this, 0, 18);
        this.wolfLeg1.addBox(-1.0f, 0.0f, -1.0f, 2, 8, 2, var1);
        this.wolfLeg1.setRotationPoint(-2.5f, 16.0f, 7.0f);
        this.wolfLeg2 = new ModelRenderer(this, 0, 18);
        this.wolfLeg2.addBox(-1.0f, 0.0f, -1.0f, 2, 8, 2, var1);
        this.wolfLeg2.setRotationPoint(0.5f, 16.0f, 7.0f);
        this.wolfLeg3 = new ModelRenderer(this, 0, 18);
        this.wolfLeg3.addBox(-1.0f, 0.0f, -1.0f, 2, 8, 2, var1);
        this.wolfLeg3.setRotationPoint(-2.5f, 16.0f, -4.0f);
        this.wolfLeg4 = new ModelRenderer(this, 0, 18);
        this.wolfLeg4.addBox(-1.0f, 0.0f, -1.0f, 2, 8, 2, var1);
        this.wolfLeg4.setRotationPoint(0.5f, 16.0f, -4.0f);
        this.wolfTail = new ModelRenderer(this, 9, 18);
        this.wolfTail.addBox(-1.0f, 0.0f, -1.0f, 2, 8, 2, var1);
        this.wolfTail.setRotationPoint(-1.0f, 12.0f, 8.0f);
        this.wolfHeadMain.setTextureOffset(16, 14).addBox(-3.0f, -5.0f, 0.0f, 2, 2, 1, var1);
        this.wolfHeadMain.setTextureOffset(16, 14).addBox(1.0f, -5.0f, 0.0f, 2, 2, 1, var1);
        this.wolfHeadMain.setTextureOffset(0, 10).addBox(-1.5f, 0.0f, -5.0f, 3, 3, 4, var1);
    }

    @Override
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        super.render(p_78088_1_, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_);
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        if (this.isChild) {
            float var8 = 2.0f;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 5.0f * p_78088_7_, 2.0f * p_78088_7_);
            this.wolfHeadMain.renderWithRotation(p_78088_7_);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f / var8, 1.0f / var8, 1.0f / var8);
            GlStateManager.translate(0.0f, 24.0f * p_78088_7_, 0.0f);
            this.wolfBody.render(p_78088_7_);
            this.wolfLeg1.render(p_78088_7_);
            this.wolfLeg2.render(p_78088_7_);
            this.wolfLeg3.render(p_78088_7_);
            this.wolfLeg4.render(p_78088_7_);
            this.wolfTail.renderWithRotation(p_78088_7_);
            this.wolfMane.render(p_78088_7_);
            GlStateManager.popMatrix();
        } else {
            this.wolfHeadMain.renderWithRotation(p_78088_7_);
            this.wolfBody.render(p_78088_7_);
            this.wolfLeg1.render(p_78088_7_);
            this.wolfLeg2.render(p_78088_7_);
            this.wolfLeg3.render(p_78088_7_);
            this.wolfLeg4.render(p_78088_7_);
            this.wolfTail.renderWithRotation(p_78088_7_);
            this.wolfMane.render(p_78088_7_);
        }
    }

    @Override
    public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
        EntityWolf var5 = (EntityWolf)p_78086_1_;
        this.wolfTail.rotateAngleY = var5.isAngry() ? 0.0f : MathHelper.cos(p_78086_2_ * 0.6662f) * 1.4f * p_78086_3_;
        if (var5.isSitting()) {
            this.wolfMane.setRotationPoint(-1.0f, 16.0f, -3.0f);
            this.wolfMane.rotateAngleX = 1.2566371f;
            this.wolfMane.rotateAngleY = 0.0f;
            this.wolfBody.setRotationPoint(0.0f, 18.0f, 0.0f);
            this.wolfBody.rotateAngleX = 0.7853982f;
            this.wolfTail.setRotationPoint(-1.0f, 21.0f, 6.0f);
            this.wolfLeg1.setRotationPoint(-2.5f, 22.0f, 2.0f);
            this.wolfLeg1.rotateAngleX = 4.712389f;
            this.wolfLeg2.setRotationPoint(0.5f, 22.0f, 2.0f);
            this.wolfLeg2.rotateAngleX = 4.712389f;
            this.wolfLeg3.rotateAngleX = 5.811947f;
            this.wolfLeg3.setRotationPoint(-2.49f, 17.0f, -4.0f);
            this.wolfLeg4.rotateAngleX = 5.811947f;
            this.wolfLeg4.setRotationPoint(0.51f, 17.0f, -4.0f);
        } else {
            this.wolfBody.setRotationPoint(0.0f, 14.0f, 2.0f);
            this.wolfBody.rotateAngleX = 1.5707964f;
            this.wolfMane.setRotationPoint(-1.0f, 14.0f, -3.0f);
            this.wolfMane.rotateAngleX = this.wolfBody.rotateAngleX;
            this.wolfTail.setRotationPoint(-1.0f, 12.0f, 8.0f);
            this.wolfLeg1.setRotationPoint(-2.5f, 16.0f, 7.0f);
            this.wolfLeg2.setRotationPoint(0.5f, 16.0f, 7.0f);
            this.wolfLeg3.setRotationPoint(-2.5f, 16.0f, -4.0f);
            this.wolfLeg4.setRotationPoint(0.5f, 16.0f, -4.0f);
            this.wolfLeg1.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662f) * 1.4f * p_78086_3_;
            this.wolfLeg2.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662f + (float)Math.PI) * 1.4f * p_78086_3_;
            this.wolfLeg3.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662f + (float)Math.PI) * 1.4f * p_78086_3_;
            this.wolfLeg4.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662f) * 1.4f * p_78086_3_;
        }
        this.wolfHeadMain.rotateAngleZ = var5.getInterestedAngle(p_78086_4_) + var5.getShakeAngle(p_78086_4_, 0.0f);
        this.wolfMane.rotateAngleZ = var5.getShakeAngle(p_78086_4_, -0.08f);
        this.wolfBody.rotateAngleZ = var5.getShakeAngle(p_78086_4_, -0.16f);
        this.wolfTail.rotateAngleZ = var5.getShakeAngle(p_78086_4_, -0.2f);
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        this.wolfHeadMain.rotateAngleX = p_78087_5_ / 57.295776f;
        this.wolfHeadMain.rotateAngleY = p_78087_4_ / 57.295776f;
        this.wolfTail.rotateAngleX = p_78087_3_;
    }
}

