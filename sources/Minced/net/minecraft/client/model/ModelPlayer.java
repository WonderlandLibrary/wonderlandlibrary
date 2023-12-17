// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.EnumHandSide;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelPlayer extends ModelBiped
{
    public ModelRenderer bipedLeftArmwear;
    public ModelRenderer bipedRightArmwear;
    public ModelRenderer bipedLeftLegwear;
    public ModelRenderer bipedRightLegwear;
    public ModelRenderer bipedBodyWear;
    private final ModelRenderer bipedCape;
    private final ModelRenderer bipedDeadmau5Head;
    private final boolean smallArms;
    
    public ModelPlayer(final float modelSize, final boolean smallArmsIn) {
        super(modelSize, 0.0f, 64, 64);
        this.smallArms = smallArmsIn;
        (this.bipedDeadmau5Head = new ModelRenderer(this, 24, 0)).addBox(-3.0f, -6.0f, -1.0f, 6, 6, 1, modelSize);
        (this.bipedCape = new ModelRenderer(this, 0, 0)).setTextureSize(64, 32);
        this.bipedCape.addBox(-5.0f, 0.0f, -1.0f, 10, 16, 1, modelSize);
        if (smallArmsIn) {
            (this.bipedLeftArm = new ModelRenderer(this, 32, 48)).addBox(-1.0f, -2.0f, -2.0f, 3, 12, 4, modelSize);
            this.bipedLeftArm.setRotationPoint(5.0f, 2.5f, 0.0f);
            (this.bipedRightArm = new ModelRenderer(this, 40, 16)).addBox(-2.0f, -2.0f, -2.0f, 3, 12, 4, modelSize);
            this.bipedRightArm.setRotationPoint(-5.0f, 2.5f, 0.0f);
            (this.bipedLeftArmwear = new ModelRenderer(this, 48, 48)).addBox(-1.0f, -2.0f, -2.0f, 3, 12, 4, modelSize + 0.25f);
            this.bipedLeftArmwear.setRotationPoint(5.0f, 2.5f, 0.0f);
            (this.bipedRightArmwear = new ModelRenderer(this, 40, 32)).addBox(-2.0f, -2.0f, -2.0f, 3, 12, 4, modelSize + 0.25f);
            this.bipedRightArmwear.setRotationPoint(-5.0f, 2.5f, 10.0f);
        }
        else {
            (this.bipedLeftArm = new ModelRenderer(this, 32, 48)).addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, modelSize);
            this.bipedLeftArm.setRotationPoint(5.0f, 2.0f, 0.0f);
            (this.bipedLeftArmwear = new ModelRenderer(this, 48, 48)).addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, modelSize + 0.25f);
            this.bipedLeftArmwear.setRotationPoint(5.0f, 2.0f, 0.0f);
            (this.bipedRightArmwear = new ModelRenderer(this, 40, 32)).addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, modelSize + 0.25f);
            this.bipedRightArmwear.setRotationPoint(-5.0f, 2.0f, 10.0f);
        }
        (this.bipedLeftLeg = new ModelRenderer(this, 16, 48)).addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, modelSize);
        this.bipedLeftLeg.setRotationPoint(1.9f, 12.0f, 0.0f);
        (this.bipedLeftLegwear = new ModelRenderer(this, 0, 48)).addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, modelSize + 0.25f);
        this.bipedLeftLegwear.setRotationPoint(1.9f, 12.0f, 0.0f);
        (this.bipedRightLegwear = new ModelRenderer(this, 0, 32)).addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, modelSize + 0.25f);
        this.bipedRightLegwear.setRotationPoint(-1.9f, 12.0f, 0.0f);
        (this.bipedBodyWear = new ModelRenderer(this, 16, 32)).addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, modelSize + 0.25f);
        this.bipedBodyWear.setRotationPoint(0.0f, 0.0f, 0.0f);
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.pushMatrix();
        if (this.isChild) {
            final float f = 2.0f;
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            GlStateManager.translate(0.0f, 24.0f * scale, 0.0f);
            this.bipedLeftLegwear.render(scale);
            this.bipedRightLegwear.render(scale);
            this.bipedLeftArmwear.render(scale);
            this.bipedRightArmwear.render(scale);
            this.bipedBodyWear.render(scale);
        }
        else {
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
            }
            this.bipedLeftLegwear.render(scale);
            this.bipedRightLegwear.render(scale);
            this.bipedLeftArmwear.render(scale);
            this.bipedRightArmwear.render(scale);
            this.bipedBodyWear.render(scale);
        }
        GlStateManager.popMatrix();
    }
    
    public void renderDeadmau5Head(final float scale) {
        ModelBase.copyModelAngles(this.bipedHead, this.bipedDeadmau5Head);
        this.bipedDeadmau5Head.rotationPointX = 0.0f;
        this.bipedDeadmau5Head.rotationPointY = 0.0f;
        this.bipedDeadmau5Head.render(scale);
    }
    
    public void renderCape(final float scale) {
        this.bipedCape.render(scale);
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        ModelBase.copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
        ModelBase.copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
        ModelBase.copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
        ModelBase.copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
        ModelBase.copyModelAngles(this.bipedBody, this.bipedBodyWear);
    }
    
    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);
        this.bipedLeftArmwear.showModel = visible;
        this.bipedRightArmwear.showModel = visible;
        this.bipedLeftLegwear.showModel = visible;
        this.bipedRightLegwear.showModel = visible;
        this.bipedBodyWear.showModel = visible;
        this.bipedCape.showModel = visible;
        this.bipedDeadmau5Head.showModel = visible;
    }
    
    @Override
    public void postRenderArm(final float scale, final EnumHandSide side) {
        final ModelRenderer modelrenderer = this.getArmForSide(side);
        if (this.smallArms) {
            final float f = 0.5f * ((side == EnumHandSide.RIGHT) ? 1 : -1);
            final ModelRenderer modelRenderer = modelrenderer;
            modelRenderer.rotationPointX += f;
            modelrenderer.postRender(scale);
            final ModelRenderer modelRenderer2 = modelrenderer;
            modelRenderer2.rotationPointX -= f;
        }
        else {
            modelrenderer.postRender(scale);
        }
    }
}
