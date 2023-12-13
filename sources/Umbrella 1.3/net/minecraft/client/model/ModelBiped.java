/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBiped
extends ModelBase {
    public ModelRenderer bipedHead;
    public ModelRenderer bipedHeadwear;
    public ModelRenderer bipedBody;
    public ModelRenderer bipedRightArm;
    public ModelRenderer bipedLeftArm;
    public ModelRenderer bipedRightLeg;
    public ModelRenderer bipedLeftLeg;
    public int heldItemLeft;
    public int heldItemRight;
    public boolean isSneak;
    public boolean aimedBow;
    private static final String __OBFID = "CL_00000840";

    public ModelBiped() {
        this(0.0f);
    }

    public ModelBiped(float p_i1148_1_) {
        this(p_i1148_1_, 0.0f, 64, 32);
    }

    public ModelBiped(float p_i1149_1_, float p_i1149_2_, int p_i1149_3_, int p_i1149_4_) {
        this.textureWidth = p_i1149_3_;
        this.textureHeight = p_i1149_4_;
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, p_i1149_1_);
        this.bipedHead.setRotationPoint(0.0f, 0.0f + p_i1149_2_, 0.0f);
        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, p_i1149_1_ + 0.5f);
        this.bipedHeadwear.setRotationPoint(0.0f, 0.0f + p_i1149_2_, 0.0f);
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, p_i1149_1_);
        this.bipedBody.setRotationPoint(0.0f, 0.0f + p_i1149_2_, 0.0f);
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, p_i1149_1_);
        this.bipedRightArm.setRotationPoint(-5.0f, 2.0f + p_i1149_2_, 0.0f);
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, p_i1149_1_);
        this.bipedLeftArm.setRotationPoint(5.0f, 2.0f + p_i1149_2_, 0.0f);
        this.bipedRightLeg = new ModelRenderer(this, 0, 16);
        this.bipedRightLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i1149_1_);
        this.bipedRightLeg.setRotationPoint(-1.9f, 12.0f + p_i1149_2_, 0.0f);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i1149_1_);
        this.bipedLeftLeg.setRotationPoint(1.9f, 12.0f + p_i1149_2_, 0.0f);
    }

    @Override
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        GlStateManager.pushMatrix();
        if (this.isChild) {
            float var8 = 2.0f;
            GlStateManager.scale(1.5f / var8, 1.5f / var8, 1.5f / var8);
            GlStateManager.translate(0.0f, 16.0f * p_78088_7_, 0.0f);
            this.bipedHead.render(p_78088_7_);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f / var8, 1.0f / var8, 1.0f / var8);
            GlStateManager.translate(0.0f, 24.0f * p_78088_7_, 0.0f);
            this.bipedBody.render(p_78088_7_);
            this.bipedRightArm.render(p_78088_7_);
            this.bipedLeftArm.render(p_78088_7_);
            this.bipedRightLeg.render(p_78088_7_);
            this.bipedLeftLeg.render(p_78088_7_);
            this.bipedHeadwear.render(p_78088_7_);
        } else {
            if (p_78088_1_.isSneaking()) {
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
            }
            this.bipedHead.render(p_78088_7_);
            this.bipedBody.render(p_78088_7_);
            this.bipedRightArm.render(p_78088_7_);
            this.bipedLeftArm.render(p_78088_7_);
            this.bipedRightLeg.render(p_78088_7_);
            this.bipedLeftLeg.render(p_78088_7_);
            this.bipedHeadwear.render(p_78088_7_);
        }
        GlStateManager.popMatrix();
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        float var9;
        float var8;
        this.bipedHead.rotateAngleY = p_78087_4_ / 57.295776f;
        this.bipedHead.rotateAngleX = p_78087_5_ / 57.295776f;
        this.bipedRightArm.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f + (float)Math.PI) * 2.0f * p_78087_2_ * 0.5f;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f) * 2.0f * p_78087_2_ * 0.5f;
        this.bipedRightArm.rotateAngleZ = 0.0f;
        this.bipedLeftArm.rotateAngleZ = 0.0f;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f) * 1.4f * p_78087_2_;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f + (float)Math.PI) * 1.4f * p_78087_2_;
        this.bipedRightLeg.rotateAngleY = 0.0f;
        this.bipedLeftLeg.rotateAngleY = 0.0f;
        if (this.isRiding) {
            this.bipedRightArm.rotateAngleX += -0.62831855f;
            this.bipedLeftArm.rotateAngleX += -0.62831855f;
            this.bipedRightLeg.rotateAngleX = -1.2566371f;
            this.bipedLeftLeg.rotateAngleX = -1.2566371f;
            this.bipedRightLeg.rotateAngleY = 0.31415927f;
            this.bipedLeftLeg.rotateAngleY = -0.31415927f;
        }
        if (this.heldItemLeft != 0) {
            this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5f - 0.31415927f * (float)this.heldItemLeft;
        }
        this.bipedRightArm.rotateAngleY = 0.0f;
        this.bipedRightArm.rotateAngleZ = 0.0f;
        switch (this.heldItemRight) {
            default: {
                break;
            }
            case 1: {
                this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5f - 0.31415927f * (float)this.heldItemRight;
                break;
            }
            case 3: {
                this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5f - 0.31415927f * (float)this.heldItemRight;
                this.bipedRightArm.rotateAngleY = -0.5235988f;
            }
        }
        this.bipedLeftArm.rotateAngleY = 0.0f;
        if (this.swingProgress > -9990.0f) {
            var8 = this.swingProgress;
            this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(var8) * (float)Math.PI * 2.0f) * 0.2f;
            this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
            this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
            this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
            var8 = 1.0f - this.swingProgress;
            var8 *= var8;
            var8 *= var8;
            var8 = 1.0f - var8;
            var9 = MathHelper.sin(var8 * (float)Math.PI);
            float var10 = MathHelper.sin(this.swingProgress * (float)Math.PI) * -(this.bipedHead.rotateAngleX - 0.7f) * 0.75f;
            this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX - ((double)var9 * 1.2 + (double)var10));
            this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0f;
            this.bipedRightArm.rotateAngleZ += MathHelper.sin(this.swingProgress * (float)Math.PI) * -0.4f;
        }
        if (this.isSneak) {
            this.bipedBody.rotateAngleX = 0.5f;
            this.bipedRightArm.rotateAngleX += 0.4f;
            this.bipedLeftArm.rotateAngleX += 0.4f;
            this.bipedRightLeg.rotationPointZ = 4.0f;
            this.bipedLeftLeg.rotationPointZ = 4.0f;
            this.bipedRightLeg.rotationPointY = 9.0f;
            this.bipedLeftLeg.rotationPointY = 9.0f;
            this.bipedHead.rotationPointY = 1.0f;
        } else {
            this.bipedBody.rotateAngleX = 0.0f;
            this.bipedRightLeg.rotationPointZ = 0.1f;
            this.bipedLeftLeg.rotationPointZ = 0.1f;
            this.bipedRightLeg.rotationPointY = 12.0f;
            this.bipedLeftLeg.rotationPointY = 12.0f;
            this.bipedHead.rotationPointY = 0.0f;
        }
        this.bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09f) * 0.05f + 0.05f;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09f) * 0.05f + 0.05f;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067f) * 0.05f;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067f) * 0.05f;
        if (this.aimedBow) {
            var8 = 0.0f;
            var9 = 0.0f;
            this.bipedRightArm.rotateAngleZ = 0.0f;
            this.bipedLeftArm.rotateAngleZ = 0.0f;
            this.bipedRightArm.rotateAngleY = -(0.1f - var8 * 0.6f) + this.bipedHead.rotateAngleY;
            this.bipedLeftArm.rotateAngleY = 0.1f - var8 * 0.6f + this.bipedHead.rotateAngleY + 0.4f;
            this.bipedRightArm.rotateAngleX = -1.5707964f + this.bipedHead.rotateAngleX;
            this.bipedLeftArm.rotateAngleX = -1.5707964f + this.bipedHead.rotateAngleX;
            this.bipedRightArm.rotateAngleX -= var8 * 1.2f - var9 * 0.4f;
            this.bipedLeftArm.rotateAngleX -= var8 * 1.2f - var9 * 0.4f;
            this.bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09f) * 0.05f + 0.05f;
            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09f) * 0.05f + 0.05f;
            this.bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067f) * 0.05f;
            this.bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067f) * 0.05f;
        }
        ModelBiped.func_178685_a(this.bipedHead, this.bipedHeadwear);
    }

    @Override
    public void setModelAttributes(ModelBase p_178686_1_) {
        super.setModelAttributes(p_178686_1_);
        if (p_178686_1_ instanceof ModelBiped) {
            ModelBiped var2 = (ModelBiped)p_178686_1_;
            this.heldItemLeft = var2.heldItemLeft;
            this.heldItemRight = var2.heldItemRight;
            this.isSneak = var2.isSneak;
            this.aimedBow = var2.aimedBow;
        }
    }

    public void func_178719_a(boolean p_178719_1_) {
        this.bipedHead.showModel = p_178719_1_;
        this.bipedHeadwear.showModel = p_178719_1_;
        this.bipedBody.showModel = p_178719_1_;
        this.bipedRightArm.showModel = p_178719_1_;
        this.bipedLeftArm.showModel = p_178719_1_;
        this.bipedRightLeg.showModel = p_178719_1_;
        this.bipedLeftLeg.showModel = p_178719_1_;
    }

    public void postRenderHiddenArm(float p_178718_1_) {
        this.bipedRightArm.postRender(p_178718_1_);
    }
}

