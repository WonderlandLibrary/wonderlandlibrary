/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelPlayer
extends ModelBiped {
    public ModelRenderer bipedRightLegwear;
    public ModelRenderer bipedBodyWear;
    private ModelRenderer bipedDeadmau5Head;
    public ModelRenderer bipedLeftArmwear;
    private ModelRenderer bipedCape;
    private boolean smallArms;
    public ModelRenderer bipedLeftLegwear;
    public ModelRenderer bipedRightArmwear;

    public void renderCape(float f) {
        this.bipedCape.render(f);
    }

    public void renderLeftArm() {
        this.bipedLeftArm.render(0.0625f);
        this.bipedLeftArmwear.render(0.0625f);
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        super.render(entity, f, f2, f3, f4, f5, f6);
        GlStateManager.pushMatrix();
        if (this.isChild) {
            float f7 = 2.0f;
            GlStateManager.scale(1.0f / f7, 1.0f / f7, 1.0f / f7);
            GlStateManager.translate(0.0f, 24.0f * f6, 0.0f);
            this.bipedLeftLegwear.render(f6);
            this.bipedRightLegwear.render(f6);
            this.bipedLeftArmwear.render(f6);
            this.bipedRightArmwear.render(f6);
            this.bipedBodyWear.render(f6);
        } else {
            if (entity.isSneaking()) {
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
            }
            this.bipedLeftLegwear.render(f6);
            this.bipedRightLegwear.render(f6);
            this.bipedLeftArmwear.render(f6);
            this.bipedRightArmwear.render(f6);
            this.bipedBodyWear.render(f6);
        }
        GlStateManager.popMatrix();
    }

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        super.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        ModelPlayer.copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
        ModelPlayer.copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
        ModelPlayer.copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
        ModelPlayer.copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
        ModelPlayer.copyModelAngles(this.bipedBody, this.bipedBodyWear);
        this.bipedCape.rotationPointY = entity.isSneaking() ? 2.0f : 0.0f;
    }

    public ModelPlayer(float f, boolean bl) {
        super(f, 0.0f, 64, 64);
        this.smallArms = bl;
        this.bipedDeadmau5Head = new ModelRenderer(this, 24, 0);
        this.bipedDeadmau5Head.addBox(-3.0f, -6.0f, -1.0f, 6, 6, 1, f);
        this.bipedCape = new ModelRenderer(this, 0, 0);
        this.bipedCape.setTextureSize(64, 32);
        this.bipedCape.addBox(-5.0f, 0.0f, -1.0f, 10, 16, 1, f);
        if (bl) {
            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
            this.bipedLeftArm.addBox(-1.0f, -2.0f, -2.0f, 3, 12, 4, f);
            this.bipedLeftArm.setRotationPoint(5.0f, 2.5f, 0.0f);
            this.bipedRightArm = new ModelRenderer(this, 40, 16);
            this.bipedRightArm.addBox(-2.0f, -2.0f, -2.0f, 3, 12, 4, f);
            this.bipedRightArm.setRotationPoint(-5.0f, 2.5f, 0.0f);
            this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
            this.bipedLeftArmwear.addBox(-1.0f, -2.0f, -2.0f, 3, 12, 4, f + 0.25f);
            this.bipedLeftArmwear.setRotationPoint(5.0f, 2.5f, 0.0f);
            this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
            this.bipedRightArmwear.addBox(-2.0f, -2.0f, -2.0f, 3, 12, 4, f + 0.25f);
            this.bipedRightArmwear.setRotationPoint(-5.0f, 2.5f, 10.0f);
        } else {
            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
            this.bipedLeftArm.addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, f);
            this.bipedLeftArm.setRotationPoint(5.0f, 2.0f, 0.0f);
            this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
            this.bipedLeftArmwear.addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, f + 0.25f);
            this.bipedLeftArmwear.setRotationPoint(5.0f, 2.0f, 0.0f);
            this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
            this.bipedRightArmwear.addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, f + 0.25f);
            this.bipedRightArmwear.setRotationPoint(-5.0f, 2.0f, 10.0f);
        }
        this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
        this.bipedLeftLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, f);
        this.bipedLeftLeg.setRotationPoint(1.9f, 12.0f, 0.0f);
        this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
        this.bipedLeftLegwear.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, f + 0.25f);
        this.bipedLeftLegwear.setRotationPoint(1.9f, 12.0f, 0.0f);
        this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
        this.bipedRightLegwear.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, f + 0.25f);
        this.bipedRightLegwear.setRotationPoint(-1.9f, 12.0f, 0.0f);
        this.bipedBodyWear = new ModelRenderer(this, 16, 32);
        this.bipedBodyWear.addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, f + 0.25f);
        this.bipedBodyWear.setRotationPoint(0.0f, 0.0f, 0.0f);
    }

    @Override
    public void postRenderArm(float f) {
        if (this.smallArms) {
            this.bipedRightArm.rotationPointX += 1.0f;
            this.bipedRightArm.postRender(f);
            this.bipedRightArm.rotationPointX -= 1.0f;
        } else {
            this.bipedRightArm.postRender(f);
        }
    }

    public void renderDeadmau5Head(float f) {
        ModelPlayer.copyModelAngles(this.bipedHead, this.bipedDeadmau5Head);
        this.bipedDeadmau5Head.rotationPointX = 0.0f;
        this.bipedDeadmau5Head.rotationPointY = 0.0f;
        this.bipedDeadmau5Head.render(f);
    }

    public void renderRightArm() {
        this.bipedRightArm.render(0.0625f);
        this.bipedRightArmwear.render(0.0625f);
    }

    @Override
    public void setInvisible(boolean bl) {
        super.setInvisible(bl);
        this.bipedLeftArmwear.showModel = bl;
        this.bipedRightArmwear.showModel = bl;
        this.bipedLeftLegwear.showModel = bl;
        this.bipedRightLegwear.showModel = bl;
        this.bipedBodyWear.showModel = bl;
        this.bipedCape.showModel = bl;
        this.bipedDeadmau5Head.showModel = bl;
    }
}

