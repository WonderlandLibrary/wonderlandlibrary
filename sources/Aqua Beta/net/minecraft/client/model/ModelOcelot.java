package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.MathHelper;

public class ModelOcelot
extends ModelBase {
    ModelRenderer ocelotBackLeftLeg;
    ModelRenderer ocelotBackRightLeg;
    ModelRenderer ocelotFrontLeftLeg;
    ModelRenderer ocelotFrontRightLeg;
    ModelRenderer ocelotTail;
    ModelRenderer ocelotTail2;
    ModelRenderer ocelotHead;
    ModelRenderer ocelotBody;
    int field_78163_i = 1;

    public ModelOcelot() {
        this.setTextureOffset("head.main", 0, 0);
        this.setTextureOffset("head.nose", 0, 24);
        this.setTextureOffset("head.ear1", 0, 10);
        this.setTextureOffset("head.ear2", 6, 10);
        this.ocelotHead = new ModelRenderer((ModelBase)this, "head");
        this.ocelotHead.addBox("main", -2.5f, -2.0f, -3.0f, 5, 4, 5);
        this.ocelotHead.addBox("nose", -1.5f, 0.0f, -4.0f, 3, 2, 2);
        this.ocelotHead.addBox("ear1", -2.0f, -3.0f, 0.0f, 1, 1, 2);
        this.ocelotHead.addBox("ear2", 1.0f, -3.0f, 0.0f, 1, 1, 2);
        this.ocelotHead.setRotationPoint(0.0f, 15.0f, -9.0f);
        this.ocelotBody = new ModelRenderer((ModelBase)this, 20, 0);
        this.ocelotBody.addBox(-2.0f, 3.0f, -8.0f, 4, 16, 6, 0.0f);
        this.ocelotBody.setRotationPoint(0.0f, 12.0f, -10.0f);
        this.ocelotTail = new ModelRenderer((ModelBase)this, 0, 15);
        this.ocelotTail.addBox(-0.5f, 0.0f, 0.0f, 1, 8, 1);
        this.ocelotTail.rotateAngleX = 0.9f;
        this.ocelotTail.setRotationPoint(0.0f, 15.0f, 8.0f);
        this.ocelotTail2 = new ModelRenderer((ModelBase)this, 4, 15);
        this.ocelotTail2.addBox(-0.5f, 0.0f, 0.0f, 1, 8, 1);
        this.ocelotTail2.setRotationPoint(0.0f, 20.0f, 14.0f);
        this.ocelotBackLeftLeg = new ModelRenderer((ModelBase)this, 8, 13);
        this.ocelotBackLeftLeg.addBox(-1.0f, 0.0f, 1.0f, 2, 6, 2);
        this.ocelotBackLeftLeg.setRotationPoint(1.1f, 18.0f, 5.0f);
        this.ocelotBackRightLeg = new ModelRenderer((ModelBase)this, 8, 13);
        this.ocelotBackRightLeg.addBox(-1.0f, 0.0f, 1.0f, 2, 6, 2);
        this.ocelotBackRightLeg.setRotationPoint(-1.1f, 18.0f, 5.0f);
        this.ocelotFrontLeftLeg = new ModelRenderer((ModelBase)this, 40, 0);
        this.ocelotFrontLeftLeg.addBox(-1.0f, 0.0f, 0.0f, 2, 10, 2);
        this.ocelotFrontLeftLeg.setRotationPoint(1.2f, 13.8f, -5.0f);
        this.ocelotFrontRightLeg = new ModelRenderer((ModelBase)this, 40, 0);
        this.ocelotFrontRightLeg.addBox(-1.0f, 0.0f, 0.0f, 2, 10, 2);
        this.ocelotFrontRightLeg.setRotationPoint(-1.2f, 13.8f, -5.0f);
    }

    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        if (this.isChild) {
            float f = 2.0f;
            GlStateManager.pushMatrix();
            GlStateManager.scale((float)(1.5f / f), (float)(1.5f / f), (float)(1.5f / f));
            GlStateManager.translate((float)0.0f, (float)(10.0f * scale), (float)(4.0f * scale));
            this.ocelotHead.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale((float)(1.0f / f), (float)(1.0f / f), (float)(1.0f / f));
            GlStateManager.translate((float)0.0f, (float)(24.0f * scale), (float)0.0f);
            this.ocelotBody.render(scale);
            this.ocelotBackLeftLeg.render(scale);
            this.ocelotBackRightLeg.render(scale);
            this.ocelotFrontLeftLeg.render(scale);
            this.ocelotFrontRightLeg.render(scale);
            this.ocelotTail.render(scale);
            this.ocelotTail2.render(scale);
            GlStateManager.popMatrix();
        } else {
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

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        this.ocelotHead.rotateAngleX = headPitch / 57.295776f;
        this.ocelotHead.rotateAngleY = netHeadYaw / 57.295776f;
        if (this.field_78163_i != 3) {
            this.ocelotBody.rotateAngleX = 1.5707964f;
            if (this.field_78163_i == 2) {
                this.ocelotBackLeftLeg.rotateAngleX = MathHelper.cos((float)(limbSwing * 0.6662f)) * 1.0f * limbSwingAmount;
                this.ocelotBackRightLeg.rotateAngleX = MathHelper.cos((float)(limbSwing * 0.6662f + 0.3f)) * 1.0f * limbSwingAmount;
                this.ocelotFrontLeftLeg.rotateAngleX = MathHelper.cos((float)(limbSwing * 0.6662f + (float)Math.PI + 0.3f)) * 1.0f * limbSwingAmount;
                this.ocelotFrontRightLeg.rotateAngleX = MathHelper.cos((float)(limbSwing * 0.6662f + (float)Math.PI)) * 1.0f * limbSwingAmount;
                this.ocelotTail2.rotateAngleX = 1.7278761f + 0.31415927f * MathHelper.cos((float)limbSwing) * limbSwingAmount;
            } else {
                this.ocelotBackLeftLeg.rotateAngleX = MathHelper.cos((float)(limbSwing * 0.6662f)) * 1.0f * limbSwingAmount;
                this.ocelotBackRightLeg.rotateAngleX = MathHelper.cos((float)(limbSwing * 0.6662f + (float)Math.PI)) * 1.0f * limbSwingAmount;
                this.ocelotFrontLeftLeg.rotateAngleX = MathHelper.cos((float)(limbSwing * 0.6662f + (float)Math.PI)) * 1.0f * limbSwingAmount;
                this.ocelotFrontRightLeg.rotateAngleX = MathHelper.cos((float)(limbSwing * 0.6662f)) * 1.0f * limbSwingAmount;
                this.ocelotTail2.rotateAngleX = this.field_78163_i == 1 ? 1.7278761f + 0.7853982f * MathHelper.cos((float)limbSwing) * limbSwingAmount : 1.7278761f + 0.47123894f * MathHelper.cos((float)limbSwing) * limbSwingAmount;
            }
        }
    }

    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {
        EntityOcelot entityocelot = (EntityOcelot)entitylivingbaseIn;
        this.ocelotBody.rotationPointY = 12.0f;
        this.ocelotBody.rotationPointZ = -10.0f;
        this.ocelotHead.rotationPointY = 15.0f;
        this.ocelotHead.rotationPointZ = -9.0f;
        this.ocelotTail.rotationPointY = 15.0f;
        this.ocelotTail.rotationPointZ = 8.0f;
        this.ocelotTail2.rotationPointY = 20.0f;
        this.ocelotTail2.rotationPointZ = 14.0f;
        this.ocelotFrontRightLeg.rotationPointY = 13.8f;
        this.ocelotFrontLeftLeg.rotationPointY = 13.8f;
        this.ocelotFrontRightLeg.rotationPointZ = -5.0f;
        this.ocelotFrontLeftLeg.rotationPointZ = -5.0f;
        this.ocelotBackRightLeg.rotationPointY = 18.0f;
        this.ocelotBackLeftLeg.rotationPointY = 18.0f;
        this.ocelotBackRightLeg.rotationPointZ = 5.0f;
        this.ocelotBackLeftLeg.rotationPointZ = 5.0f;
        this.ocelotTail.rotateAngleX = 0.9f;
        if (entityocelot.isSneaking()) {
            this.ocelotBody.rotationPointY += 1.0f;
            this.ocelotHead.rotationPointY += 2.0f;
            this.ocelotTail.rotationPointY += 1.0f;
            this.ocelotTail2.rotationPointY += -4.0f;
            this.ocelotTail2.rotationPointZ += 2.0f;
            this.ocelotTail.rotateAngleX = 1.5707964f;
            this.ocelotTail2.rotateAngleX = 1.5707964f;
            this.field_78163_i = 0;
        } else if (entityocelot.isSprinting()) {
            this.ocelotTail2.rotationPointY = this.ocelotTail.rotationPointY;
            this.ocelotTail2.rotationPointZ += 2.0f;
            this.ocelotTail.rotateAngleX = 1.5707964f;
            this.ocelotTail2.rotateAngleX = 1.5707964f;
            this.field_78163_i = 2;
        } else if (entityocelot.isSitting()) {
            this.ocelotBody.rotateAngleX = 0.7853982f;
            this.ocelotBody.rotationPointY += -4.0f;
            this.ocelotBody.rotationPointZ += 5.0f;
            this.ocelotHead.rotationPointY += -3.3f;
            this.ocelotHead.rotationPointZ += 1.0f;
            this.ocelotTail.rotationPointY += 8.0f;
            this.ocelotTail.rotationPointZ += -2.0f;
            this.ocelotTail2.rotationPointY += 2.0f;
            this.ocelotTail2.rotationPointZ += -0.8f;
            this.ocelotTail.rotateAngleX = 1.7278761f;
            this.ocelotTail2.rotateAngleX = 2.670354f;
            this.ocelotFrontRightLeg.rotateAngleX = -0.15707964f;
            this.ocelotFrontLeftLeg.rotateAngleX = -0.15707964f;
            this.ocelotFrontRightLeg.rotationPointY = 15.8f;
            this.ocelotFrontLeftLeg.rotationPointY = 15.8f;
            this.ocelotFrontRightLeg.rotationPointZ = -7.0f;
            this.ocelotFrontLeftLeg.rotationPointZ = -7.0f;
            this.ocelotBackRightLeg.rotateAngleX = -1.5707964f;
            this.ocelotBackLeftLeg.rotateAngleX = -1.5707964f;
            this.ocelotBackRightLeg.rotationPointY = 21.0f;
            this.ocelotBackLeftLeg.rotationPointY = 21.0f;
            this.ocelotBackRightLeg.rotationPointZ = 1.0f;
            this.ocelotBackLeftLeg.rotationPointZ = 1.0f;
            this.field_78163_i = 3;
        } else {
            this.field_78163_i = 1;
        }
    }
}
