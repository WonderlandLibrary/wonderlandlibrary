package net.minecraft.client.model;

import koks.event.AnimationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelPlayer extends ModelBiped {
    public ModelRenderer bipedLeftArmwear;
    public ModelRenderer bipedRightArmwear;
    public ModelRenderer bipedLeftLegwear;
    public ModelRenderer bipedRightLegwear;
    public ModelRenderer bipedBodyWear;
    private ModelRenderer bipedCape;
    private ModelRenderer bipedDeadmau5Head;
    private boolean smallArms;

    public ModelPlayer(float p_i46304_1_, boolean p_i46304_2_) {
        super(p_i46304_1_, 0.0F, 64, 64);
        this.smallArms = p_i46304_2_;
        this.bipedDeadmau5Head = new ModelRenderer(this, 24, 0);
        this.bipedDeadmau5Head.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, p_i46304_1_);
        this.bipedCape = new ModelRenderer(this, 0, 0);
        this.bipedCape.setTextureSize(64, 32);
        this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, p_i46304_1_);

        if (p_i46304_2_) {
            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
            this.bipedRightArm = new ModelRenderer(this, 40, 16);
            this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_);
            this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
            this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
            this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_ + 0.25F);
            this.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);
            this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
            this.bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_ + 0.25F);
            this.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
        } else {
            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, p_i46304_1_);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
            this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
            this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
            this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
            this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
            this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
            this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
        }

        this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i46304_1_);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
        this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
        this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
        this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
        this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bipedBodyWear = new ModelRenderer(this, 16, 32);
        this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, p_i46304_1_ + 0.25F);
        this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    @Override
    public void resetOffset() {
        bipedBodyWear.offsetX = 0;
        bipedBodyWear.offsetY = 0;
        bipedBodyWear.offsetZ = 0;

        bipedLeftArmwear.offsetX = 0;
        bipedLeftArmwear.offsetY = 0;
        bipedLeftArmwear.offsetZ = 0;

        bipedRightArmwear.offsetX = 0;
        bipedRightArmwear.offsetY = 0;
        bipedRightArmwear.offsetZ = 0;

        bipedLeftLegwear.offsetX = 0;
        bipedLeftLegwear.offsetY = 0;
        bipedLeftLegwear.offsetZ = 0;

        bipedRightLegwear.offsetX = 0;
        bipedRightLegwear.offsetY = 0;
        bipedRightLegwear.offsetZ = 0;

        bipedCape.offsetX = 0;
        bipedCape.offsetY = 0;
        bipedCape.offsetZ = 0;

        super.resetOffset();
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        super.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
        GlStateManager.pushMatrix();

        this.bipedHeadwear.offsetX = this.bipedHead.offsetX;
        this.bipedHeadwear.offsetY = this.bipedHead.offsetY;
        this.bipedHeadwear.offsetZ = this.bipedHead.offsetZ;
        this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
        this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;

        this.bipedBodyWear.offsetX = this.bipedBody.offsetX;
        this.bipedBodyWear.offsetY = this.bipedBody.offsetY;
        this.bipedBodyWear.offsetZ = this.bipedBody.offsetZ;
        this.bipedBodyWear.rotateAngleX = this.bipedBody.rotateAngleX;
        this.bipedBodyWear.rotateAngleY = this.bipedBody.rotateAngleY;
        this.bipedBodyWear.rotateAngleZ = this.bipedBody.rotateAngleZ;

        this.bipedLeftArmwear.offsetX = this.bipedLeftArm.offsetX;
        this.bipedLeftArmwear.offsetY = this.bipedLeftArm.offsetY;
        this.bipedLeftArmwear.offsetZ = this.bipedLeftArm.offsetZ;
        this.bipedLeftArmwear.rotateAngleX = this.bipedLeftArm.rotateAngleX;
        this.bipedLeftArmwear.rotateAngleY = this.bipedLeftArm.rotateAngleY;
        this.bipedLeftArmwear.rotateAngleZ = this.bipedLeftArm.rotateAngleZ;

        this.bipedRightArmwear.offsetX = this.bipedRightArm.offsetX;
        this.bipedRightArmwear.offsetY = this.bipedRightArm.offsetY;
        this.bipedRightArmwear.offsetZ = this.bipedRightArm.offsetZ;
        this.bipedRightArmwear.rotateAngleX = this.bipedRightArm.rotateAngleX;
        this.bipedRightArmwear.rotateAngleY = this.bipedRightArm.rotateAngleY;
        this.bipedRightArmwear.rotateAngleZ = this.bipedRightArm.rotateAngleZ;

        this.bipedLeftLegwear.offsetX = this.bipedLeftLeg.offsetX;
        this.bipedLeftLegwear.offsetY = this.bipedLeftLeg.offsetY;
        this.bipedLeftLegwear.offsetZ = this.bipedLeftLeg.offsetZ;
        this.bipedLeftLegwear.rotateAngleX = this.bipedLeftLeg.rotateAngleX;
        this.bipedLeftLegwear.rotateAngleY = this.bipedLeftLeg.rotateAngleY;
        this.bipedLeftLegwear.rotateAngleZ = this.bipedLeftLeg.rotateAngleZ;

        this.bipedRightLegwear.offsetX = this.bipedRightLeg.offsetX;
        this.bipedRightLegwear.offsetY = this.bipedRightLeg.offsetY;
        this.bipedRightLegwear.offsetZ = this.bipedRightLeg.offsetZ;
        this.bipedRightLegwear.rotateAngleX = this.bipedRightLeg.rotateAngleX;
        this.bipedRightLegwear.rotateAngleY = this.bipedRightLeg.rotateAngleY;
        this.bipedRightLegwear.rotateAngleZ = this.bipedRightLeg.rotateAngleZ;

        if(entityIn == Minecraft.getMinecraft().thePlayer) {
            if(this.bipedBody.offsetX != 0 || this.bipedBody.offsetY != 0 || this.bipedBody.offsetZ != 0)
                this.bipedCape.showModel = false;
            else
                this.bipedCape.showModel = true;
        }

        if (this.isChild) {
            float f = 2.0F;
            GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.bipedLeftLegwear.render(scale);
            this.bipedRightLegwear.render(scale);
            this.bipedLeftArmwear.render(scale);
            this.bipedRightArmwear.render(scale);
            this.bipedBodyWear.render(scale);
        } else {
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            this.bipedLeftLegwear.render(scale);
            this.bipedRightLegwear.render(scale);
            this.bipedLeftArmwear.render(scale);
            this.bipedRightArmwear.render(scale);
            this.bipedBodyWear.render(scale);
        }

        GlStateManager.popMatrix();
    }

    public void renderDeadmau5Head(float p_178727_1_) {
        copyModelAngles(this.bipedHead, this.bipedDeadmau5Head);
        this.bipedDeadmau5Head.rotationPointX = 0.0F;
        this.bipedDeadmau5Head.rotationPointY = 0.0F;
        this.bipedDeadmau5Head.render(p_178727_1_);
    }

    public void renderCape(float p_178728_1_) {
        this.bipedCape.render(p_178728_1_);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
        copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
        copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
        copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
        copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
        copyModelAngles(this.bipedBody, this.bipedBodyWear);
    }

    public void renderRightArm() {
        this.bipedRightArm.render(0.0625F);
        this.bipedRightArmwear.render(0.0625F);
    }

    public void renderLeftArm() {
        this.bipedLeftArm.render(0.0625F);
        this.bipedLeftArmwear.render(0.0625F);
    }

    public void setInvisible(boolean invisible) {
        super.setInvisible(invisible);
        this.bipedLeftArmwear.showModel = invisible;
        this.bipedRightArmwear.showModel = invisible;
        this.bipedLeftLegwear.showModel = invisible;
        this.bipedRightLegwear.showModel = invisible;
        this.bipedBodyWear.showModel = invisible;
        this.bipedCape.showModel = invisible;
        this.bipedDeadmau5Head.showModel = invisible;
    }

    public void postRenderArm(float scale) {
        if (this.smallArms) {
            ++this.bipedRightArm.rotationPointX;
            this.bipedRightArm.postRender(scale);
            --this.bipedRightArm.rotationPointX;
        } else {
            this.bipedRightArm.postRender(scale);
        }
    }
}
