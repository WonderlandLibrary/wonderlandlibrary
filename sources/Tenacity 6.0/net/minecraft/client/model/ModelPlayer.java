// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import dev.tenacity.utils.render.RenderUtil;
import net.minecraft.util.MathHelper;
import dev.tenacity.module.impl.render.CustomModel;
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
    private final ModelRenderer body;
    private ModelRenderer eye;
    private final ModelRenderer left_leg;
    private final ModelRenderer right_leg;
    private final ModelRenderer rabbitBone;
    private final ModelRenderer rabbitRleg;
    private final ModelRenderer rabbitLarm;
    public final ModelRenderer rabbitRarm;
    private final ModelRenderer rabbitLleg;
    private final ModelRenderer rabbitHead;
    
    public ModelPlayer(final float p_i46304_1_, final boolean p_i46304_2_) {
        super(p_i46304_1_, 0.0f, 64, 64);
        (this.rabbitBone = new ModelRenderer(this)).setRotationPoint(0.0f, 24.0f, 0.0f);
        this.rabbitBone.cubeList.add(new ModelBox(this.rabbitBone, 28, 45, -5.0f, -13.0f, -5.0f, 10, 11, 8, 0.0f, false));
        (this.rabbitRleg = new ModelRenderer(this)).setRotationPoint(-3.0f, -2.0f, -1.0f);
        this.rabbitBone.addChild(this.rabbitRleg);
        this.rabbitRleg.cubeList.add(new ModelBox(this.rabbitRleg, 0, 0, -2.0f, 0.0f, -2.0f, 4, 2, 4, 0.0f, false));
        (this.rabbitLarm = new ModelRenderer(this)).setRotationPoint(5.0f, -13.0f, -1.0f);
        this.setRotationAngle(this.rabbitLarm, 0.0f, 0.0f, -0.0873f);
        this.rabbitBone.addChild(this.rabbitLarm);
        this.rabbitLarm.cubeList.add(new ModelBox(this.rabbitLarm, 0, 0, 0.0f, 0.0f, -2.0f, 2, 8, 4, 0.0f, false));
        (this.rabbitRarm = new ModelRenderer(this)).setRotationPoint(-5.0f, -13.0f, -1.0f);
        this.setRotationAngle(this.rabbitRarm, 0.0f, 0.0f, 0.0873f);
        this.rabbitBone.addChild(this.rabbitRarm);
        this.rabbitRarm.cubeList.add(new ModelBox(this.rabbitRarm, 0, 0, -2.0f, 0.0f, -2.0f, 2, 8, 4, 0.0f, false));
        (this.rabbitLleg = new ModelRenderer(this)).setRotationPoint(3.0f, -2.0f, -1.0f);
        this.rabbitBone.addChild(this.rabbitLleg);
        this.rabbitLleg.cubeList.add(new ModelBox(this.rabbitLleg, 0, 0, -2.0f, 0.0f, -2.0f, 4, 2, 4, 0.0f, false));
        (this.rabbitHead = new ModelRenderer(this)).setRotationPoint(0.0f, -14.0f, -1.0f);
        this.rabbitBone.addChild(this.rabbitHead);
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 0, 0, -3.0f, 0.0f, -4.0f, 6, 1, 6, 0.0f, false));
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 56, 0, -5.0f, -9.0f, -5.0f, 2, 3, 2, 0.0f, false));
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 56, 0, 3.0f, -9.0f, -5.0f, 2, 3, 2, 0.0f, true));
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 0, 45, -4.0f, -11.0f, -4.0f, 8, 11, 8, 0.0f, false));
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 46, 0, 1.0f, -20.0f, 0.0f, 3, 9, 1, 0.0f, false));
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 46, 0, -4.0f, -20.0f, 0.0f, 3, 9, 1, 0.0f, false));
        this.body = new ModelRenderer(this);
        this.eye = new ModelRenderer(this);
        this.body.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.eye.setTextureOffset(0, 10).addBox(-3.0f, 7.0f, -4.0f, 6, 4, 1);
        this.body.setTextureOffset(34, 8).addBox(-4.0f, 6.0f, -3.0f, 8, 12, 6);
        this.body.setTextureOffset(15, 10).addBox(-3.0f, 9.0f, 3.0f, 6, 8, 3);
        this.body.setTextureOffset(26, 0).addBox(-3.0f, 5.0f, -3.0f, 6, 1, 6);
        (this.left_leg = new ModelRenderer(this)).setRotationPoint(-2.0f, 18.0f, 0.0f);
        this.left_leg.setTextureOffset(0, 0).addBox(2.9f, 0.0f, -1.5f, 3, 6, 3, 0.0f);
        (this.right_leg = new ModelRenderer(this)).setRotationPoint(2.0f, 18.0f, 0.0f);
        this.right_leg.setTextureOffset(13, 0).addBox(-5.9f, 0.0f, -1.5f, 3, 6, 3);
        this.smallArms = p_i46304_2_;
        (this.bipedDeadmau5Head = new ModelRenderer(this, 24, 0)).addBox(-3.0f, -6.0f, -1.0f, 6, 6, 1, p_i46304_1_);
        (this.bipedCape = new ModelRenderer(this, 0, 0)).setTextureSize(64, 32);
        this.bipedCape.addBox(-5.0f, 0.0f, -1.0f, 10, 16, 1, p_i46304_1_);
        if (p_i46304_2_) {
            (this.bipedLeftArm = new ModelRenderer(this, 32, 48)).addBox(-1.0f, -2.0f, -2.0f, 3, 12, 4, p_i46304_1_);
            this.bipedLeftArm.setRotationPoint(5.0f, 2.5f, 0.0f);
            (this.bipedRightArm = new ModelRenderer(this, 40, 16)).addBox(-2.0f, -2.0f, -2.0f, 3, 12, 4, p_i46304_1_);
            this.bipedRightArm.setRotationPoint(-5.0f, 2.5f, 0.0f);
            (this.bipedLeftArmwear = new ModelRenderer(this, 48, 48)).addBox(-1.0f, -2.0f, -2.0f, 3, 12, 4, p_i46304_1_ + 0.25f);
            this.bipedLeftArmwear.setRotationPoint(5.0f, 2.5f, 0.0f);
            (this.bipedRightArmwear = new ModelRenderer(this, 40, 32)).addBox(-2.0f, -2.0f, -2.0f, 3, 12, 4, p_i46304_1_ + 0.25f);
            this.bipedRightArmwear.setRotationPoint(-5.0f, 2.5f, 10.0f);
        }
        else {
            (this.bipedLeftArm = new ModelRenderer(this, 32, 48)).addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, p_i46304_1_);
            this.bipedLeftArm.setRotationPoint(5.0f, 2.0f, 0.0f);
            (this.bipedLeftArmwear = new ModelRenderer(this, 48, 48)).addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, p_i46304_1_ + 0.25f);
            this.bipedLeftArmwear.setRotationPoint(5.0f, 2.0f, 0.0f);
            (this.bipedRightArmwear = new ModelRenderer(this, 40, 32)).addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, p_i46304_1_ + 0.25f);
            this.bipedRightArmwear.setRotationPoint(-5.0f, 2.0f, 10.0f);
        }
        (this.bipedLeftLeg = new ModelRenderer(this, 16, 48)).addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i46304_1_);
        this.bipedLeftLeg.setRotationPoint(1.9f, 12.0f, 0.0f);
        (this.bipedLeftLegwear = new ModelRenderer(this, 0, 48)).addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i46304_1_ + 0.25f);
        this.bipedLeftLegwear.setRotationPoint(1.9f, 12.0f, 0.0f);
        (this.bipedRightLegwear = new ModelRenderer(this, 0, 32)).addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i46304_1_ + 0.25f);
        this.bipedRightLegwear.setRotationPoint(-1.9f, 12.0f, 0.0f);
        (this.bipedBodyWear = new ModelRenderer(this, 16, 32)).addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, p_i46304_1_ + 0.25f);
        this.bipedBodyWear.setRotationPoint(0.0f, 0.0f, 0.0f);
    }
    
    public void setRotationAngle(final ModelRenderer modelRenderer, final float x, final float y, final float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        GlStateManager.pushMatrix();
        if (CustomModel.enabled) {
            final String mode = CustomModel.model.getMode();
            switch (mode) {
                case "Among Us": {
                    this.bipedHead.rotateAngleY = netHeadYaw * 0.017453292f;
                    this.bipedHead.rotateAngleX = headPitch * 0.017453292f;
                    this.bipedBody.rotateAngleY = 0.0f;
                    this.bipedRightArm.rotationPointZ = 0.0f;
                    this.bipedRightArm.rotationPointX = -5.0f;
                    this.bipedLeftArm.rotationPointZ = 0.0f;
                    this.bipedLeftArm.rotationPointX = 5.0f;
                    final float f = 1.0f;
                    this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + MathHelper.PI) * 2.0f * limbSwingAmount * 0.5f / f;
                    this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 2.0f * limbSwingAmount * 0.5f / f;
                    this.bipedRightArm.rotateAngleZ = 0.0f;
                    this.bipedLeftArm.rotateAngleZ = 0.0f;
                    this.right_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount / f;
                    this.left_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + MathHelper.PI) * 1.4f * limbSwingAmount / f;
                    this.right_leg.rotateAngleY = 0.0f;
                    this.left_leg.rotateAngleY = 0.0f;
                    this.right_leg.rotateAngleZ = 0.0f;
                    this.left_leg.rotateAngleZ = 0.0f;
                    RenderUtil.color(CustomModel.getColor(entityIn).getRGB());
                    if (this.isChild) {
                        GlStateManager.scale(0.5, 0.5, 0.5);
                        GlStateManager.translate(0.0, 24.0 * scale, 0.0);
                        this.body.render(scale);
                        this.left_leg.render(scale);
                        this.right_leg.render(scale);
                    }
                    else {
                        GlStateManager.translate(0.0, -0.9, 0.0);
                        GlStateManager.scale(1.8, 1.6, 1.6);
                        GlStateManager.translate(0.0, 0.14, 0.0);
                        this.body.render(scale);
                        RenderUtil.color(-16711681);
                        this.eye.render(scale);
                        RenderUtil.color(CustomModel.getColor(entityIn).getRGB());
                        GlStateManager.translate(0.0, -0.15, 0.0);
                        this.left_leg.render(scale);
                        this.right_leg.render(scale);
                        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    }
                    GlStateManager.popMatrix();
                    break;
                }
                case "Rabbit": {
                    RenderUtil.color(-1);
                    GlStateManager.scale(1.25, 1.25, 1.25);
                    GlStateManager.translate(0.0, -0.3, 0.0);
                    this.rabbitHead.rotateAngleX = this.bipedHead.rotateAngleX;
                    this.rabbitHead.rotateAngleY = this.bipedHead.rotateAngleY;
                    this.rabbitHead.rotateAngleZ = this.bipedHead.rotateAngleZ;
                    this.rabbitLarm.rotateAngleX = this.bipedLeftArm.rotateAngleX;
                    this.rabbitLarm.rotateAngleY = this.bipedLeftArm.rotateAngleY;
                    this.rabbitLarm.rotateAngleZ = this.bipedLeftArm.rotateAngleZ;
                    this.rabbitRarm.rotateAngleX = this.bipedRightArm.rotateAngleX;
                    this.rabbitRarm.rotateAngleY = this.bipedRightArm.rotateAngleY;
                    this.rabbitRarm.rotateAngleZ = this.bipedRightArm.rotateAngleZ;
                    this.rabbitRleg.rotateAngleX = this.bipedRightLeg.rotateAngleX;
                    this.rabbitRleg.rotateAngleY = this.bipedRightLeg.rotateAngleY;
                    this.rabbitRleg.rotateAngleZ = this.bipedRightLeg.rotateAngleZ;
                    this.rabbitLleg.rotateAngleX = this.bipedLeftLeg.rotateAngleX;
                    this.rabbitLleg.rotateAngleY = this.bipedLeftLeg.rotateAngleY;
                    this.rabbitLleg.rotateAngleZ = this.bipedLeftLeg.rotateAngleZ;
                    this.rabbitBone.render(scale);
                    GlStateManager.popMatrix();
                    break;
                }
            }
        }
        else {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (this.isChild) {
                final float f2 = 2.0f;
                GlStateManager.scale(1.0f / f2, 1.0f / f2, 1.0f / f2);
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
    }
    
    public void renderDeadmau5Head(final float p_178727_1_) {
        ModelBase.copyModelAngles(this.bipedHead, this.bipedDeadmau5Head);
        this.bipedDeadmau5Head.rotationPointX = 0.0f;
        this.bipedDeadmau5Head.rotationPointY = 0.0f;
        this.bipedDeadmau5Head.render(p_178727_1_);
    }
    
    public void renderCape(final float p_178728_1_) {
        this.bipedCape.render(p_178728_1_);
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
    
    public void renderRightArm() {
        this.bipedRightArm.render(0.0625f);
        this.bipedRightArmwear.render(0.0625f);
    }
    
    public void renderLeftArm() {
        this.bipedLeftArm.render(0.0625f);
        this.bipedLeftArmwear.render(0.0625f);
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        super.setInvisible(invisible);
        this.bipedLeftArmwear.showModel = invisible;
        this.bipedRightArmwear.showModel = invisible;
        this.bipedLeftLegwear.showModel = invisible;
        this.bipedRightLegwear.showModel = invisible;
        this.bipedBodyWear.showModel = invisible;
        this.bipedCape.showModel = invisible;
        this.bipedDeadmau5Head.showModel = invisible;
    }
    
    @Override
    public void postRenderArm(final float scale) {
        if (this.smallArms) {
            final ModelRenderer bipedRightArm = this.bipedRightArm;
            ++bipedRightArm.rotationPointX;
            this.bipedRightArm.postRender(scale);
            final ModelRenderer bipedRightArm2 = this.bipedRightArm;
            --bipedRightArm2.rotationPointX;
        }
        else {
            this.bipedRightArm.postRender(scale);
        }
    }
}
