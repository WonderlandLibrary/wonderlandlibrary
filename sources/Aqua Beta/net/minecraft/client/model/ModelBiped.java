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

    public ModelBiped() {
        this(0.0f);
    }

    public ModelBiped(float modelSize) {
        this(modelSize, 0.0f, 64, 32);
    }

    public ModelBiped(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn) {
        this.textureWidth = textureWidthIn;
        this.textureHeight = textureHeightIn;
        this.bipedHead = new ModelRenderer((ModelBase)this, 0, 0);
        this.bipedHead.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, modelSize);
        this.bipedHead.setRotationPoint(0.0f, 0.0f + p_i1149_2_, 0.0f);
        this.bipedHeadwear = new ModelRenderer((ModelBase)this, 32, 0);
        this.bipedHeadwear.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, modelSize + 0.5f);
        this.bipedHeadwear.setRotationPoint(0.0f, 0.0f + p_i1149_2_, 0.0f);
        this.bipedBody = new ModelRenderer((ModelBase)this, 16, 16);
        this.bipedBody.addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, modelSize);
        this.bipedBody.setRotationPoint(0.0f, 0.0f + p_i1149_2_, 0.0f);
        this.bipedRightArm = new ModelRenderer((ModelBase)this, 40, 16);
        this.bipedRightArm.addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, modelSize);
        this.bipedRightArm.setRotationPoint(-5.0f, 2.0f + p_i1149_2_, 0.0f);
        this.bipedLeftArm = new ModelRenderer((ModelBase)this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, modelSize);
        this.bipedLeftArm.setRotationPoint(5.0f, 2.0f + p_i1149_2_, 0.0f);
        this.bipedRightLeg = new ModelRenderer((ModelBase)this, 0, 16);
        this.bipedRightLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, modelSize);
        this.bipedRightLeg.setRotationPoint(-1.9f, 12.0f + p_i1149_2_, 0.0f);
        this.bipedLeftLeg = new ModelRenderer((ModelBase)this, 0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, modelSize);
        this.bipedLeftLeg.setRotationPoint(1.9f, 12.0f + p_i1149_2_, 0.0f);
    }

    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        GlStateManager.pushMatrix();
        if (this.isChild) {
            float f = 2.0f;
            GlStateManager.scale((float)(1.5f / f), (float)(1.5f / f), (float)(1.5f / f));
            GlStateManager.translate((float)0.0f, (float)(16.0f * scale), (float)0.0f);
            this.bipedHead.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale((float)(1.0f / f), (float)(1.0f / f), (float)(1.0f / f));
            GlStateManager.translate((float)0.0f, (float)(24.0f * scale), (float)0.0f);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);
        } else {
            if (entityIn.isSneaking()) {
                GlStateManager.translate((float)0.0f, (float)0.2f, (float)0.0f);
            }
            this.bipedHead.render(scale);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);
        }
        GlStateManager.popMatrix();
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        this.bipedHead.rotateAngleY = netHeadYaw / 57.295776f;
        this.bipedHead.rotateAngleX = headPitch / 57.295776f;
        this.bipedRightArm.rotateAngleX = MathHelper.cos((float)(limbSwing * 0.6662f + (float)Math.PI)) * 2.0f * limbSwingAmount * 0.5f;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos((float)(limbSwing * 0.6662f)) * 2.0f * limbSwingAmount * 0.5f;
        this.bipedRightArm.rotateAngleZ = 0.0f;
        this.bipedLeftArm.rotateAngleZ = 0.0f;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos((float)(limbSwing * 0.6662f)) * 1.4f * limbSwingAmount;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos((float)(limbSwing * 0.6662f + (float)Math.PI)) * 1.4f * limbSwingAmount;
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
            float f = this.swingProgress;
            this.bipedBody.rotateAngleY = MathHelper.sin((float)(MathHelper.sqrt_float((float)f) * (float)Math.PI * 2.0f)) * 0.2f;
            this.bipedRightArm.rotationPointZ = MathHelper.sin((float)this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedRightArm.rotationPointX = -MathHelper.cos((float)this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedLeftArm.rotationPointZ = -MathHelper.sin((float)this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedLeftArm.rotationPointX = MathHelper.cos((float)this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
            this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
            this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
            f = 1.0f - this.swingProgress;
            f *= f;
            f *= f;
            f = 1.0f - f;
            float f1 = MathHelper.sin((float)(f * (float)Math.PI));
            float f2 = MathHelper.sin((float)(this.swingProgress * (float)Math.PI)) * -(this.bipedHead.rotateAngleX - 0.7f) * 0.75f;
            this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX - ((double)f1 * 1.2 + (double)f2));
            this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0f;
            this.bipedRightArm.rotateAngleZ += MathHelper.sin((float)(this.swingProgress * (float)Math.PI)) * -0.4f;
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
        this.bipedRightArm.rotateAngleZ += MathHelper.cos((float)(ageInTicks * 0.09f)) * 0.05f + 0.05f;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos((float)(ageInTicks * 0.09f)) * 0.05f + 0.05f;
        this.bipedRightArm.rotateAngleX += MathHelper.sin((float)(ageInTicks * 0.067f)) * 0.05f;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin((float)(ageInTicks * 0.067f)) * 0.05f;
        if (this.aimedBow) {
            float f3 = 0.0f;
            float f4 = 0.0f;
            this.bipedRightArm.rotateAngleZ = 0.0f;
            this.bipedLeftArm.rotateAngleZ = 0.0f;
            this.bipedRightArm.rotateAngleY = -(0.1f - f3 * 0.6f) + this.bipedHead.rotateAngleY;
            this.bipedLeftArm.rotateAngleY = 0.1f - f3 * 0.6f + this.bipedHead.rotateAngleY + 0.4f;
            this.bipedRightArm.rotateAngleX = -1.5707964f + this.bipedHead.rotateAngleX;
            this.bipedLeftArm.rotateAngleX = -1.5707964f + this.bipedHead.rotateAngleX;
            this.bipedRightArm.rotateAngleX -= f3 * 1.2f - f4 * 0.4f;
            this.bipedLeftArm.rotateAngleX -= f3 * 1.2f - f4 * 0.4f;
            this.bipedRightArm.rotateAngleZ += MathHelper.cos((float)(ageInTicks * 0.09f)) * 0.05f + 0.05f;
            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos((float)(ageInTicks * 0.09f)) * 0.05f + 0.05f;
            this.bipedRightArm.rotateAngleX += MathHelper.sin((float)(ageInTicks * 0.067f)) * 0.05f;
            this.bipedLeftArm.rotateAngleX -= MathHelper.sin((float)(ageInTicks * 0.067f)) * 0.05f;
        }
        ModelBiped.copyModelAngles((ModelRenderer)this.bipedHead, (ModelRenderer)this.bipedHeadwear);
    }

    public void setModelAttributes(ModelBase model) {
        super.setModelAttributes(model);
        if (model instanceof ModelBiped) {
            ModelBiped modelbiped = (ModelBiped)model;
            this.heldItemLeft = modelbiped.heldItemLeft;
            this.heldItemRight = modelbiped.heldItemRight;
            this.isSneak = modelbiped.isSneak;
            this.aimedBow = modelbiped.aimedBow;
        }
    }

    public void setInvisible(boolean invisible) {
        this.bipedHead.showModel = invisible;
        this.bipedHeadwear.showModel = invisible;
        this.bipedBody.showModel = invisible;
        this.bipedRightArm.showModel = invisible;
        this.bipedLeftArm.showModel = invisible;
        this.bipedRightLeg.showModel = invisible;
        this.bipedLeftLeg.showModel = invisible;
    }

    public void postRenderArm(float scale) {
        this.bipedRightArm.postRender(scale);
    }
}
