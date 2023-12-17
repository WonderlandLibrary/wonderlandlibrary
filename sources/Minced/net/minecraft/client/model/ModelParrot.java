// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;

public class ModelParrot extends ModelBase
{
    ModelRenderer body;
    ModelRenderer tail;
    ModelRenderer wingLeft;
    ModelRenderer wingRight;
    ModelRenderer head;
    ModelRenderer head2;
    ModelRenderer beak1;
    ModelRenderer beak2;
    ModelRenderer feather;
    ModelRenderer legLeft;
    ModelRenderer legRight;
    private State state;
    
    public ModelParrot() {
        this.state = State.STANDING;
        this.textureWidth = 32;
        this.textureHeight = 32;
        (this.body = new ModelRenderer(this, 2, 8)).addBox(-1.5f, 0.0f, -1.5f, 3, 6, 3);
        this.body.setRotationPoint(0.0f, 16.5f, -3.0f);
        (this.tail = new ModelRenderer(this, 22, 1)).addBox(-1.5f, -1.0f, -1.0f, 3, 4, 1);
        this.tail.setRotationPoint(0.0f, 21.07f, 1.16f);
        (this.wingLeft = new ModelRenderer(this, 19, 8)).addBox(-0.5f, 0.0f, -1.5f, 1, 5, 3);
        this.wingLeft.setRotationPoint(1.5f, 16.94f, -2.76f);
        (this.wingRight = new ModelRenderer(this, 19, 8)).addBox(-0.5f, 0.0f, -1.5f, 1, 5, 3);
        this.wingRight.setRotationPoint(-1.5f, 16.94f, -2.76f);
        (this.head = new ModelRenderer(this, 2, 2)).addBox(-1.0f, -1.5f, -1.0f, 2, 3, 2);
        this.head.setRotationPoint(0.0f, 15.69f, -2.76f);
        (this.head2 = new ModelRenderer(this, 10, 0)).addBox(-1.0f, -0.5f, -2.0f, 2, 1, 4);
        this.head2.setRotationPoint(0.0f, -2.0f, -1.0f);
        this.head.addChild(this.head2);
        (this.beak1 = new ModelRenderer(this, 11, 7)).addBox(-0.5f, -1.0f, -0.5f, 1, 2, 1);
        this.beak1.setRotationPoint(0.0f, -0.5f, -1.5f);
        this.head.addChild(this.beak1);
        (this.beak2 = new ModelRenderer(this, 16, 7)).addBox(-0.5f, 0.0f, -0.5f, 1, 2, 1);
        this.beak2.setRotationPoint(0.0f, -1.75f, -2.45f);
        this.head.addChild(this.beak2);
        (this.feather = new ModelRenderer(this, 2, 18)).addBox(0.0f, -4.0f, -2.0f, 0, 5, 4);
        this.feather.setRotationPoint(0.0f, -2.15f, 0.15f);
        this.head.addChild(this.feather);
        (this.legLeft = new ModelRenderer(this, 14, 18)).addBox(-0.5f, 0.0f, -0.5f, 1, 2, 1);
        this.legLeft.setRotationPoint(1.0f, 22.0f, -1.05f);
        (this.legRight = new ModelRenderer(this, 14, 18)).addBox(-0.5f, 0.0f, -0.5f, 1, 2, 1);
        this.legRight.setRotationPoint(-1.0f, 22.0f, -1.05f);
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.body.render(scale);
        this.wingLeft.render(scale);
        this.wingRight.render(scale);
        this.tail.render(scale);
        this.head.render(scale);
        this.legLeft.render(scale);
        this.legRight.render(scale);
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        final float f = ageInTicks * 0.3f;
        this.head.rotateAngleX = headPitch * 0.017453292f;
        this.head.rotateAngleY = netHeadYaw * 0.017453292f;
        this.head.rotateAngleZ = 0.0f;
        this.head.rotationPointX = 0.0f;
        this.body.rotationPointX = 0.0f;
        this.tail.rotationPointX = 0.0f;
        this.wingRight.rotationPointX = -1.5f;
        this.wingLeft.rotationPointX = 1.5f;
        if (this.state != State.FLYING) {
            if (this.state == State.SITTING) {
                return;
            }
            if (this.state == State.PARTY) {
                final float f2 = MathHelper.cos((float)entityIn.ticksExisted);
                final float f3 = MathHelper.sin((float)entityIn.ticksExisted);
                this.head.rotationPointX = f2;
                this.head.rotationPointY = 15.69f + f3;
                this.head.rotateAngleX = 0.0f;
                this.head.rotateAngleY = 0.0f;
                this.head.rotateAngleZ = MathHelper.sin((float)entityIn.ticksExisted) * 0.4f;
                this.body.rotationPointX = f2;
                this.body.rotationPointY = 16.5f + f3;
                this.wingLeft.rotateAngleZ = -0.0873f - ageInTicks;
                this.wingLeft.rotationPointX = 1.5f + f2;
                this.wingLeft.rotationPointY = 16.94f + f3;
                this.wingRight.rotateAngleZ = 0.0873f + ageInTicks;
                this.wingRight.rotationPointX = -1.5f + f2;
                this.wingRight.rotationPointY = 16.94f + f3;
                this.tail.rotationPointX = f2;
                this.tail.rotationPointY = 21.07f + f3;
                return;
            }
            final ModelRenderer legLeft = this.legLeft;
            legLeft.rotateAngleX += MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
            final ModelRenderer legRight = this.legRight;
            legRight.rotateAngleX += MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 1.4f * limbSwingAmount;
        }
        this.head.rotationPointY = 15.69f + f;
        this.tail.rotateAngleX = 1.015f + MathHelper.cos(limbSwing * 0.6662f) * 0.3f * limbSwingAmount;
        this.tail.rotationPointY = 21.07f + f;
        this.body.rotationPointY = 16.5f + f;
        this.wingLeft.rotateAngleZ = -0.0873f - ageInTicks;
        this.wingLeft.rotationPointY = 16.94f + f;
        this.wingRight.rotateAngleZ = 0.0873f + ageInTicks;
        this.wingRight.rotationPointY = 16.94f + f;
        this.legLeft.rotationPointY = 22.0f + f;
        this.legRight.rotationPointY = 22.0f + f;
    }
    
    @Override
    public void setLivingAnimations(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTickTime) {
        this.feather.rotateAngleX = -0.2214f;
        this.body.rotateAngleX = 0.4937f;
        this.wingLeft.rotateAngleX = -0.69813174f;
        this.wingLeft.rotateAngleY = -3.1415927f;
        this.wingRight.rotateAngleX = -0.69813174f;
        this.wingRight.rotateAngleY = -3.1415927f;
        this.legLeft.rotateAngleX = -0.0299f;
        this.legRight.rotateAngleX = -0.0299f;
        this.legLeft.rotationPointY = 22.0f;
        this.legRight.rotationPointY = 22.0f;
        if (entitylivingbaseIn instanceof EntityParrot) {
            final EntityParrot entityparrot = (EntityParrot)entitylivingbaseIn;
            if (entityparrot.isPartying()) {
                this.legLeft.rotateAngleZ = -0.34906584f;
                this.legRight.rotateAngleZ = 0.34906584f;
                this.state = State.PARTY;
                return;
            }
            if (entityparrot.isSitting()) {
                final float f = 1.9f;
                this.head.rotationPointY = 17.59f;
                this.tail.rotateAngleX = 1.5388988f;
                this.tail.rotationPointY = 22.97f;
                this.body.rotationPointY = 18.4f;
                this.wingLeft.rotateAngleZ = -0.0873f;
                this.wingLeft.rotationPointY = 18.84f;
                this.wingRight.rotateAngleZ = 0.0873f;
                this.wingRight.rotationPointY = 18.84f;
                final ModelRenderer legLeft = this.legLeft;
                ++legLeft.rotationPointY;
                final ModelRenderer legRight = this.legRight;
                ++legRight.rotationPointY;
                final ModelRenderer legLeft2 = this.legLeft;
                ++legLeft2.rotateAngleX;
                final ModelRenderer legRight2 = this.legRight;
                ++legRight2.rotateAngleX;
                this.state = State.SITTING;
            }
            else if (entityparrot.isFlying()) {
                final ModelRenderer legLeft3 = this.legLeft;
                legLeft3.rotateAngleX += 0.69813174f;
                final ModelRenderer legRight3 = this.legRight;
                legRight3.rotateAngleX += 0.69813174f;
                this.state = State.FLYING;
            }
            else {
                this.state = State.STANDING;
            }
            this.legLeft.rotateAngleZ = 0.0f;
            this.legRight.rotateAngleZ = 0.0f;
        }
    }
    
    enum State
    {
        FLYING, 
        STANDING, 
        SITTING, 
        PARTY;
    }
}
