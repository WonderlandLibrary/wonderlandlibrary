package net.minecraft.client.model;

import koks.api.registry.module.ModuleRegistry;
import koks.event.AnimationEvent;
import koks.module.visual.Animations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

import java.util.List;

public class ModelBiped extends ModelBase {
    public ModelRenderer bipedHead;

    /**
     * The Biped's Headwear. Used for the outer layer of player skins.
     */
    public ModelRenderer bipedHeadwear;
    public ModelRenderer bipedBody;

    /**
     * The Biped's Right Arm
     */
    public ModelRenderer bipedRightArm;

    /**
     * The Biped's Left Arm
     */
    public ModelRenderer bipedLeftArm;

    /**
     * The Biped's Right Leg
     */
    public ModelRenderer bipedRightLeg;

    /**
     * The Biped's Left Leg
     */
    public ModelRenderer bipedLeftLeg;

    public List<ModelRenderer> bipedList;

    /**
     * Records whether the model should be rendered holding an item in the left hand, and if that item is a block.
     */
    public int heldItemLeft;

    /**
     * Records whether the model should be rendered holding an item in the right hand, and if that item is a block.
     */
    public int heldItemRight;
    public boolean isSneak;

    /**
     * Records whether the model should be rendered aiming a bow.
     */
    public boolean aimedBow;

    public ModelBiped() {
        this(0.0F);
    }

    public ModelBiped(float modelSize) {
        this(modelSize, 0.0F, 64, 32);
    }

    public ModelBiped(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn) {
        this.textureWidth = textureWidthIn;
        this.textureHeight = textureHeightIn;
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
        this.bipedHead.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.5F);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize);
        this.bipedBody.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + p_i1149_2_, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + p_i1149_2_, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 16);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + p_i1149_2_, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + p_i1149_2_, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        GlStateManager.pushMatrix();

        if(!Minecraft.getMinecraft().developerMode) {
            resetOffset();

            if (entityIn.equals(Minecraft.getMinecraft().thePlayer) && ModuleRegistry.getModule(Animations.class).isToggled()) {
                final AnimationEvent animationEvent = new AnimationEvent(
                        new float[]{bipedHead.offsetX, bipedHead.offsetY, bipedHead.offsetZ, bipedHead.rotateAngleX, bipedHead.rotateAngleY, bipedHead.rotateAngleZ},
                        new float[]{bipedBody.offsetX, bipedBody.offsetY, bipedBody.offsetZ, bipedBody.rotateAngleX, bipedBody.rotateAngleY, bipedBody.rotateAngleZ},
                        new float[]{bipedLeftArm.offsetX, bipedLeftArm.offsetY, bipedLeftArm.offsetZ, bipedLeftArm.rotateAngleX, bipedLeftArm.rotateAngleY, bipedLeftArm.rotateAngleZ},
                        new float[]{bipedRightArm.offsetX, bipedRightArm.offsetY, bipedRightArm.offsetZ, bipedRightArm.rotateAngleX, bipedRightArm.rotateAngleY, bipedRightArm.rotateAngleZ},
                        new float[]{bipedLeftLeg.offsetX, bipedLeftLeg.offsetY, bipedLeftLeg.offsetZ, bipedLeftLeg.rotateAngleX, bipedLeftLeg.rotateAngleY, bipedLeftLeg.rotateAngleZ},
                        new float[]{bipedRightLeg.offsetX, bipedRightLeg.offsetY, bipedRightLeg.offsetZ, bipedRightLeg.rotateAngleX, bipedRightLeg.rotateAngleY, bipedRightLeg.rotateAngleZ}).onFire();

                this.bipedHead.offsetX = animationEvent.getBipedHead()[0];
                this.bipedHead.offsetY = animationEvent.getBipedHead()[1];
                this.bipedHead.offsetZ = animationEvent.getBipedHead()[2];
                this.bipedHead.rotateAngleX = animationEvent.getBipedHead()[3];
                this.bipedHead.rotateAngleY = animationEvent.getBipedHead()[4];

                this.bipedHeadwear.offsetX = this.bipedHead.offsetX;
                this.bipedHeadwear.offsetY = this.bipedHead.offsetY;
                this.bipedHeadwear.offsetZ = this.bipedHead.offsetZ;
                this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
                this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;

                this.bipedBody.offsetX = animationEvent.getBipedBody()[0];
                this.bipedBody.offsetY = animationEvent.getBipedBody()[1];
                this.bipedBody.offsetZ = animationEvent.getBipedBody()[2];
                this.bipedBody.rotateAngleX = animationEvent.getBipedBody()[3];
                this.bipedBody.rotateAngleY = animationEvent.getBipedBody()[4];
                this.bipedBody.rotateAngleZ = animationEvent.getBipedBody()[5];

                this.bipedLeftArm.offsetX = animationEvent.getBipedLeftArm()[0];
                this.bipedLeftArm.offsetY = animationEvent.getBipedLeftArm()[1];
                this.bipedLeftArm.offsetZ = animationEvent.getBipedLeftArm()[2];
                this.bipedLeftArm.rotateAngleX = animationEvent.getBipedLeftArm()[3];
                this.bipedLeftArm.rotateAngleY = animationEvent.getBipedLeftArm()[4];
                this.bipedLeftArm.rotateAngleZ = animationEvent.getBipedLeftArm()[5];

                this.bipedRightArm.offsetX = animationEvent.getBipedRightArm()[0];
                this.bipedRightArm.offsetY = animationEvent.getBipedRightArm()[1];
                this.bipedRightArm.offsetZ = animationEvent.getBipedRightArm()[2];
                this.bipedRightArm.rotateAngleX = animationEvent.getBipedRightArm()[3];
                this.bipedRightArm.rotateAngleY = animationEvent.getBipedRightArm()[4];
                this.bipedRightArm.rotateAngleZ = animationEvent.getBipedRightArm()[5];

                this.bipedLeftLeg.offsetX = animationEvent.getBipedLeftLeg()[0];
                this.bipedLeftLeg.offsetY = animationEvent.getBipedLeftLeg()[1];
                this.bipedLeftLeg.offsetZ = animationEvent.getBipedLeftLeg()[2];
                this.bipedLeftLeg.rotateAngleX = animationEvent.getBipedLeftLeg()[3];
                this.bipedLeftLeg.rotateAngleY = animationEvent.getBipedLeftLeg()[4];
                this.bipedLeftLeg.rotateAngleZ = animationEvent.getBipedLeftLeg()[5];

                this.bipedRightLeg.offsetX = animationEvent.getBipedRightLeg()[0];
                this.bipedRightLeg.offsetY = animationEvent.getBipedRightLeg()[1];
                this.bipedRightLeg.offsetZ = animationEvent.getBipedRightLeg()[2];
                this.bipedRightLeg.rotateAngleX = animationEvent.getBipedRightLeg()[3];
                this.bipedRightLeg.rotateAngleY = animationEvent.getBipedRightLeg()[4];
                this.bipedRightLeg.rotateAngleZ = animationEvent.getBipedRightLeg()[5];
            }
        }

        if (this.isChild) {
            float f = 2.0F;
            GlStateManager.scale(1.5F / f, 1.5F / f, 1.5F / f);
            GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            this.bipedHead.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);
        } else {
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
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

    public void resetOffset() {
        bipedHeadwear.offsetX = 0;
        bipedHeadwear.offsetY = 0;
        bipedHeadwear.offsetZ = 0;

        bipedHead.offsetX = 0;
        bipedHead.offsetY = 0;
        bipedHead.offsetZ = 0;

        bipedBody.offsetX = 0;
        bipedBody.offsetY = 0;
        bipedBody.offsetZ = 0;

        bipedLeftArm.offsetX = 0;
        bipedLeftArm.offsetY = 0;
        bipedLeftArm.offsetZ = 0;

        bipedRightArm.offsetX = 0;
        bipedRightArm.offsetY = 0;
        bipedRightArm.offsetZ = 0;

        bipedLeftLeg.offsetX = 0;
        bipedLeftLeg.offsetY = 0;
        bipedLeftLeg.offsetZ = 0;

        bipedRightLeg.offsetX = 0;
        bipedRightLeg.offsetY = 0;
        bipedRightLeg.offsetZ = 0;
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn) {
        this.bipedHead.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
        this.bipedHead.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
        this.bipedRightArm.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float) Math.PI) * 2.0F * p_78087_2_ * 0.5F;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 2.0F * p_78087_2_ * 0.5F;
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float) Math.PI) * 1.4F * p_78087_2_;
        this.bipedRightLeg.rotateAngleY = 0.0F;
        this.bipedLeftLeg.rotateAngleY = 0.0F;

        if (this.isRiding) {
            this.bipedRightArm.rotateAngleX += -((float) Math.PI / 5F);
            this.bipedLeftArm.rotateAngleX += -((float) Math.PI / 5F);
            this.bipedRightLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
            this.bipedLeftLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
            this.bipedRightLeg.rotateAngleY = ((float) Math.PI / 10F);
            this.bipedLeftLeg.rotateAngleY = -((float) Math.PI / 10F);
        }

        if (this.heldItemLeft != 0) {
            this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F) * (float) this.heldItemLeft;
        }

        this.bipedRightArm.rotateAngleY = 0.0F;
        this.bipedRightArm.rotateAngleZ = 0.0F;

        switch (this.heldItemRight) {
            case 0:
            case 2:
            default:
                break;

            case 1:
                this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F) * (float) this.heldItemRight;
                break;

            case 3:
                this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F) * (float) this.heldItemRight;
                this.bipedRightArm.rotateAngleY = -0.5235988F;
        }

        this.bipedLeftArm.rotateAngleY = 0.0F;

        if (this.swingProgress > -9990.0F) {
            float f = this.swingProgress;
            this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f) * (float) Math.PI * 2.0F) * 0.2F;
            this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
            this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
            this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
            f = 1.0F - this.swingProgress;
            f = f * f;
            f = f * f;
            f = 1.0F - f;
            float f1 = MathHelper.sin(f * (float) Math.PI);
            float f2 = MathHelper.sin(this.swingProgress * (float) Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
            this.bipedRightArm.rotateAngleX = (float) ((double) this.bipedRightArm.rotateAngleX - ((double) f1 * 1.2D + (double) f2));
            this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
            this.bipedRightArm.rotateAngleZ += MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
        }

        if (this.isSneak) {
            this.bipedBody.rotateAngleX = 0.5F;
            this.bipedRightArm.rotateAngleX += 0.4F;
            this.bipedLeftArm.rotateAngleX += 0.4F;
            this.bipedRightLeg.rotationPointZ = 4.0F;
            this.bipedLeftLeg.rotationPointZ = 4.0F;
            this.bipedRightLeg.rotationPointY = 9.0F;
            this.bipedLeftLeg.rotationPointY = 9.0F;
            this.bipedHead.rotationPointY = 1.0F;
        } else {
            this.bipedBody.rotateAngleX = 0.0F;
            this.bipedRightLeg.rotationPointZ = 0.1F;
            this.bipedLeftLeg.rotationPointZ = 0.1F;
            this.bipedRightLeg.rotationPointY = 12.0F;
            this.bipedLeftLeg.rotationPointY = 12.0F;
            this.bipedHead.rotationPointY = 0.0F;
        }

        this.bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;

        if (this.aimedBow) {
            float f3 = 0.0F;
            float f4 = 0.0F;
            this.bipedRightArm.rotateAngleZ = 0.0F;
            this.bipedLeftArm.rotateAngleZ = 0.0F;
            this.bipedRightArm.rotateAngleY = -(0.1F - f3 * 0.6F) + this.bipedHead.rotateAngleY;
            this.bipedLeftArm.rotateAngleY = 0.1F - f3 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
            this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
            this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
            this.bipedRightArm.rotateAngleX -= f3 * 1.2F - f4 * 0.4F;
            this.bipedLeftArm.rotateAngleX -= f3 * 1.2F - f4 * 0.4F;
            this.bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
            this.bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
            this.bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
        }

        copyModelAngles(this.bipedHead, this.bipedHeadwear);
    }

    public void setModelAttributes(ModelBase model) {
        super.setModelAttributes(model);

        if (model instanceof ModelBiped) {
            ModelBiped modelbiped = (ModelBiped) model;
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
