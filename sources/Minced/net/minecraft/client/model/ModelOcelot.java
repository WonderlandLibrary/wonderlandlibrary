// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelOcelot extends ModelBase
{
    private final ModelRenderer ocelotBackLeftLeg;
    private final ModelRenderer ocelotBackRightLeg;
    private final ModelRenderer ocelotFrontLeftLeg;
    private final ModelRenderer ocelotFrontRightLeg;
    private final ModelRenderer ocelotTail;
    private final ModelRenderer ocelotTail2;
    private final ModelRenderer ocelotHead;
    private final ModelRenderer ocelotBody;
    private int state;
    
    public ModelOcelot() {
        this.state = 1;
        this.setTextureOffset("head.main", 0, 0);
        this.setTextureOffset("head.nose", 0, 24);
        this.setTextureOffset("head.ear1", 0, 10);
        this.setTextureOffset("head.ear2", 6, 10);
        (this.ocelotHead = new ModelRenderer(this, "head")).addBox("main", -2.5f, -2.0f, -3.0f, 5, 4, 5);
        this.ocelotHead.addBox("nose", -1.5f, 0.0f, -4.0f, 3, 2, 2);
        this.ocelotHead.addBox("ear1", -2.0f, -3.0f, 0.0f, 1, 1, 2);
        this.ocelotHead.addBox("ear2", 1.0f, -3.0f, 0.0f, 1, 1, 2);
        this.ocelotHead.setRotationPoint(0.0f, 15.0f, -9.0f);
        (this.ocelotBody = new ModelRenderer(this, 20, 0)).addBox(-2.0f, 3.0f, -8.0f, 4, 16, 6, 0.0f);
        this.ocelotBody.setRotationPoint(0.0f, 12.0f, -10.0f);
        (this.ocelotTail = new ModelRenderer(this, 0, 15)).addBox(-0.5f, 0.0f, 0.0f, 1, 8, 1);
        this.ocelotTail.rotateAngleX = 0.9f;
        this.ocelotTail.setRotationPoint(0.0f, 15.0f, 8.0f);
        (this.ocelotTail2 = new ModelRenderer(this, 4, 15)).addBox(-0.5f, 0.0f, 0.0f, 1, 8, 1);
        this.ocelotTail2.setRotationPoint(0.0f, 20.0f, 14.0f);
        (this.ocelotBackLeftLeg = new ModelRenderer(this, 8, 13)).addBox(-1.0f, 0.0f, 1.0f, 2, 6, 2);
        this.ocelotBackLeftLeg.setRotationPoint(1.1f, 18.0f, 5.0f);
        (this.ocelotBackRightLeg = new ModelRenderer(this, 8, 13)).addBox(-1.0f, 0.0f, 1.0f, 2, 6, 2);
        this.ocelotBackRightLeg.setRotationPoint(-1.1f, 18.0f, 5.0f);
        (this.ocelotFrontLeftLeg = new ModelRenderer(this, 40, 0)).addBox(-1.0f, 0.0f, 0.0f, 2, 10, 2);
        this.ocelotFrontLeftLeg.setRotationPoint(1.2f, 13.8f, -5.0f);
        (this.ocelotFrontRightLeg = new ModelRenderer(this, 40, 0)).addBox(-1.0f, 0.0f, 0.0f, 2, 10, 2);
        this.ocelotFrontRightLeg.setRotationPoint(-1.2f, 13.8f, -5.0f);
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        if (this.isChild) {
            final float f = 2.0f;
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.75f, 0.75f, 0.75f);
            GlStateManager.translate(0.0f, 10.0f * scale, 4.0f * scale);
            this.ocelotHead.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            GlStateManager.translate(0.0f, 24.0f * scale, 0.0f);
            this.ocelotBody.render(scale);
            this.ocelotBackLeftLeg.render(scale);
            this.ocelotBackRightLeg.render(scale);
            this.ocelotFrontLeftLeg.render(scale);
            this.ocelotFrontRightLeg.render(scale);
            this.ocelotTail.render(scale);
            this.ocelotTail2.render(scale);
            GlStateManager.popMatrix();
        }
        else {
            this.ocelotHead.render(scale);
            this.ocelotBody.render(scale);
            this.ocelotTail.render(scale);
            this.ocelotTail2.render(scale);
            this.ocelotBackLeftLeg.render(scale);
            this.ocelotBackRightLeg.render(scale);
            this.ocelotFrontLeftLeg.render(scale);
            this.ocelotFrontRightLeg.render(scale);
        }
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        this.ocelotHead.rotateAngleX = headPitch * 0.017453292f;
        this.ocelotHead.rotateAngleY = netHeadYaw * 0.017453292f;
        if (this.state != 3) {
            this.ocelotBody.rotateAngleX = 1.5707964f;
            if (this.state == 2) {
                this.ocelotBackLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * limbSwingAmount;
                this.ocelotBackRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 0.3f) * limbSwingAmount;
                this.ocelotFrontLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f + 0.3f) * limbSwingAmount;
                this.ocelotFrontRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * limbSwingAmount;
                this.ocelotTail2.rotateAngleX = 1.7278761f + 0.31415927f * MathHelper.cos(limbSwing) * limbSwingAmount;
            }
            else {
                this.ocelotBackLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * limbSwingAmount;
                this.ocelotBackRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * limbSwingAmount;
                this.ocelotFrontLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * limbSwingAmount;
                this.ocelotFrontRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * limbSwingAmount;
                if (this.state == 1) {
                    this.ocelotTail2.rotateAngleX = 1.7278761f + 0.7853982f * MathHelper.cos(limbSwing) * limbSwingAmount;
                }
                else {
                    this.ocelotTail2.rotateAngleX = 1.7278761f + 0.47123894f * MathHelper.cos(limbSwing) * limbSwingAmount;
                }
            }
        }
    }
    
    @Override
    public void setLivingAnimations(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTickTime) {
        final EntityOcelot entityocelot = (EntityOcelot)entitylivingbaseIn;
        this.ocelotBody.rotationPointY = 12.0f;
        this.ocelotBody.rotationPointZ = -10.0f;
        this.ocelotHead.rotationPointY = 15.0f;
        this.ocelotHead.rotationPointZ = -9.0f;
        this.ocelotTail.rotationPointY = 15.0f;
        this.ocelotTail.rotationPointZ = 8.0f;
        this.ocelotTail2.rotationPointY = 20.0f;
        this.ocelotTail2.rotationPointZ = 14.0f;
        this.ocelotFrontLeftLeg.rotationPointY = 13.8f;
        this.ocelotFrontLeftLeg.rotationPointZ = -5.0f;
        this.ocelotFrontRightLeg.rotationPointY = 13.8f;
        this.ocelotFrontRightLeg.rotationPointZ = -5.0f;
        this.ocelotBackLeftLeg.rotationPointY = 18.0f;
        this.ocelotBackLeftLeg.rotationPointZ = 5.0f;
        this.ocelotBackRightLeg.rotationPointY = 18.0f;
        this.ocelotBackRightLeg.rotationPointZ = 5.0f;
        this.ocelotTail.rotateAngleX = 0.9f;
        if (entityocelot.isSneaking()) {
            final ModelRenderer ocelotBody = this.ocelotBody;
            ++ocelotBody.rotationPointY;
            final ModelRenderer ocelotHead = this.ocelotHead;
            ocelotHead.rotationPointY += 2.0f;
            final ModelRenderer ocelotTail = this.ocelotTail;
            ++ocelotTail.rotationPointY;
            final ModelRenderer ocelotTail2 = this.ocelotTail2;
            ocelotTail2.rotationPointY -= 4.0f;
            final ModelRenderer ocelotTail3 = this.ocelotTail2;
            ocelotTail3.rotationPointZ += 2.0f;
            this.ocelotTail.rotateAngleX = 1.5707964f;
            this.ocelotTail2.rotateAngleX = 1.5707964f;
            this.state = 0;
        }
        else if (entityocelot.isSprinting()) {
            this.ocelotTail2.rotationPointY = this.ocelotTail.rotationPointY;
            final ModelRenderer ocelotTail4 = this.ocelotTail2;
            ocelotTail4.rotationPointZ += 2.0f;
            this.ocelotTail.rotateAngleX = 1.5707964f;
            this.ocelotTail2.rotateAngleX = 1.5707964f;
            this.state = 2;
        }
        else if (entityocelot.isSitting()) {
            this.ocelotBody.rotateAngleX = 0.7853982f;
            final ModelRenderer ocelotBody2 = this.ocelotBody;
            ocelotBody2.rotationPointY -= 4.0f;
            final ModelRenderer ocelotBody3 = this.ocelotBody;
            ocelotBody3.rotationPointZ += 5.0f;
            final ModelRenderer ocelotHead2 = this.ocelotHead;
            ocelotHead2.rotationPointY -= 3.3f;
            final ModelRenderer ocelotHead3 = this.ocelotHead;
            ++ocelotHead3.rotationPointZ;
            final ModelRenderer ocelotTail5 = this.ocelotTail;
            ocelotTail5.rotationPointY += 8.0f;
            final ModelRenderer ocelotTail6 = this.ocelotTail;
            ocelotTail6.rotationPointZ -= 2.0f;
            final ModelRenderer ocelotTail7 = this.ocelotTail2;
            ocelotTail7.rotationPointY += 2.0f;
            final ModelRenderer ocelotTail8 = this.ocelotTail2;
            ocelotTail8.rotationPointZ -= 0.8f;
            this.ocelotTail.rotateAngleX = 1.7278761f;
            this.ocelotTail2.rotateAngleX = 2.670354f;
            this.ocelotFrontLeftLeg.rotateAngleX = -0.15707964f;
            this.ocelotFrontLeftLeg.rotationPointY = 15.8f;
            this.ocelotFrontLeftLeg.rotationPointZ = -7.0f;
            this.ocelotFrontRightLeg.rotateAngleX = -0.15707964f;
            this.ocelotFrontRightLeg.rotationPointY = 15.8f;
            this.ocelotFrontRightLeg.rotationPointZ = -7.0f;
            this.ocelotBackLeftLeg.rotateAngleX = -1.5707964f;
            this.ocelotBackLeftLeg.rotationPointY = 21.0f;
            this.ocelotBackLeftLeg.rotationPointZ = 1.0f;
            this.ocelotBackRightLeg.rotateAngleX = -1.5707964f;
            this.ocelotBackRightLeg.rotationPointY = 21.0f;
            this.ocelotBackRightLeg.rotationPointZ = 1.0f;
            this.state = 3;
        }
        else {
            this.state = 1;
        }
    }
}
