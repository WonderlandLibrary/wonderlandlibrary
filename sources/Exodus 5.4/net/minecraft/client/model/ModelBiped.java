/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBiped
extends ModelBase {
    public ModelRenderer bipedRightLeg;
    public ModelRenderer bipedBody;
    public ModelRenderer bipedLeftArm;
    public int heldItemLeft;
    public ModelRenderer bipedHead;
    public ModelRenderer bipedLeftLeg;
    public ModelRenderer bipedRightArm;
    public boolean aimedBow;
    public ModelRenderer bipedHeadwear;
    public boolean isSneak;
    public int heldItemRight;

    public ModelBiped(float f) {
        this(f, 0.0f, 64, 32);
    }

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        float f7;
        float f8;
        this.bipedHead.rotateAngleY = f4 / 57.295776f;
        this.bipedHead.rotateAngleX = f5 / 57.295776f;
        this.bipedRightArm.rotateAngleX = MathHelper.cos(f * 0.6662f + (float)Math.PI) * 2.0f * f2 * 0.5f;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(f * 0.6662f) * 2.0f * f2 * 0.5f;
        this.bipedRightArm.rotateAngleZ = 0.0f;
        this.bipedLeftArm.rotateAngleZ = 0.0f;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662f) * 1.4f * f2;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662f + (float)Math.PI) * 1.4f * f2;
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
            f8 = this.swingProgress;
            this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f8) * (float)Math.PI * 2.0f) * 0.2f;
            this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
            this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
            this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
            f8 = 1.0f - this.swingProgress;
            f8 *= f8;
            f8 *= f8;
            f8 = 1.0f - f8;
            f7 = MathHelper.sin(f8 * (float)Math.PI);
            float f9 = MathHelper.sin(this.swingProgress * (float)Math.PI) * -(this.bipedHead.rotateAngleX - 0.7f) * 0.75f;
            this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX - ((double)f7 * 1.2 + (double)f9));
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
        this.bipedRightArm.rotateAngleZ += MathHelper.cos(f3 * 0.09f) * 0.05f + 0.05f;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f3 * 0.09f) * 0.05f + 0.05f;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(f3 * 0.067f) * 0.05f;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f3 * 0.067f) * 0.05f;
        if (this.aimedBow) {
            f8 = 0.0f;
            f7 = 0.0f;
            this.bipedRightArm.rotateAngleZ = 0.0f;
            this.bipedLeftArm.rotateAngleZ = 0.0f;
            this.bipedRightArm.rotateAngleY = -(0.1f - f8 * 0.6f) + this.bipedHead.rotateAngleY;
            this.bipedLeftArm.rotateAngleY = 0.1f - f8 * 0.6f + this.bipedHead.rotateAngleY + 0.4f;
            this.bipedRightArm.rotateAngleX = -1.5707964f + this.bipedHead.rotateAngleX;
            this.bipedLeftArm.rotateAngleX = -1.5707964f + this.bipedHead.rotateAngleX;
            this.bipedRightArm.rotateAngleX -= f8 * 1.2f - f7 * 0.4f;
            this.bipedLeftArm.rotateAngleX -= f8 * 1.2f - f7 * 0.4f;
            this.bipedRightArm.rotateAngleZ += MathHelper.cos(f3 * 0.09f) * 0.05f + 0.05f;
            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f3 * 0.09f) * 0.05f + 0.05f;
            this.bipedRightArm.rotateAngleX += MathHelper.sin(f3 * 0.067f) * 0.05f;
            this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f3 * 0.067f) * 0.05f;
        }
        ModelBiped.copyModelAngles(this.bipedHead, this.bipedHeadwear);
    }

    public void postRenderArm(float f) {
        this.bipedRightArm.postRender(f);
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        GlStateManager.pushMatrix();
        if (this.isChild) {
            float f7 = 2.0f;
            GlStateManager.scale(1.5f / f7, 1.5f / f7, 1.5f / f7);
            GlStateManager.translate(0.0f, 16.0f * f6, 0.0f);
            this.bipedHead.render(f6);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f / f7, 1.0f / f7, 1.0f / f7);
            GlStateManager.translate(0.0f, 24.0f * f6, 0.0f);
            this.bipedBody.render(f6);
            this.bipedRightArm.render(f6);
            this.bipedLeftArm.render(f6);
            this.bipedRightLeg.render(f6);
            this.bipedLeftLeg.render(f6);
            this.bipedHeadwear.render(f6);
        } else {
            if (entity.isSneaking()) {
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
            }
            this.bipedHead.render(f6);
            this.bipedBody.render(f6);
            this.bipedRightArm.render(f6);
            this.bipedLeftArm.render(f6);
            this.bipedRightLeg.render(f6);
            this.bipedLeftLeg.render(f6);
            this.bipedHeadwear.render(f6);
        }
        GlStateManager.popMatrix();
    }

    public ModelBiped(float f, float f2, int n, int n2) {
        this.textureWidth = n;
        this.textureHeight = n2;
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f);
        this.bipedHead.setRotationPoint(0.0f, 0.0f + f2, 0.0f);
        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f + 0.5f);
        this.bipedHeadwear.setRotationPoint(0.0f, 0.0f + f2, 0.0f);
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, f);
        this.bipedBody.setRotationPoint(0.0f, 0.0f + f2, 0.0f);
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, f);
        this.bipedRightArm.setRotationPoint(-5.0f, 2.0f + f2, 0.0f);
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, f);
        this.bipedLeftArm.setRotationPoint(5.0f, 2.0f + f2, 0.0f);
        this.bipedRightLeg = new ModelRenderer(this, 0, 16);
        this.bipedRightLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, f);
        this.bipedRightLeg.setRotationPoint(-1.9f, 12.0f + f2, 0.0f);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, f);
        this.bipedLeftLeg.setRotationPoint(1.9f, 12.0f + f2, 0.0f);
    }

    public void setInvisible(boolean bl) {
        this.bipedHead.showModel = bl;
        this.bipedHeadwear.showModel = bl;
        this.bipedBody.showModel = bl;
        this.bipedRightArm.showModel = bl;
        this.bipedLeftArm.showModel = bl;
        this.bipedRightLeg.showModel = bl;
        this.bipedLeftLeg.showModel = bl;
    }

    public ModelBiped() {
        this(0.0f);
    }

    @Override
    public void setModelAttributes(ModelBase modelBase) {
        super.setModelAttributes(modelBase);
        if (modelBase instanceof ModelBiped) {
            ModelBiped modelBiped = (ModelBiped)modelBase;
            this.heldItemLeft = modelBiped.heldItemLeft;
            this.heldItemRight = modelBiped.heldItemRight;
            this.isSneak = modelBiped.isSneak;
            this.aimedBow = modelBiped.aimedBow;
        }
    }
}

