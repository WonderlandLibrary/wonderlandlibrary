/*
 * Decompiled with CFR 0.152.
 */
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
    ModelRenderer ocelotFrontRightLeg;
    ModelRenderer ocelotTail;
    int field_78163_i = 1;
    ModelRenderer ocelotHead;
    ModelRenderer ocelotFrontLeftLeg;
    ModelRenderer ocelotBody;
    ModelRenderer ocelotTail2;
    ModelRenderer ocelotBackRightLeg;
    ModelRenderer ocelotBackLeftLeg;

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        if (this.isChild) {
            float f7 = 2.0f;
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.5f / f7, 1.5f / f7, 1.5f / f7);
            GlStateManager.translate(0.0f, 10.0f * f6, 4.0f * f6);
            this.ocelotHead.render(f6);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f / f7, 1.0f / f7, 1.0f / f7);
            GlStateManager.translate(0.0f, 24.0f * f6, 0.0f);
            this.ocelotBody.render(f6);
            this.ocelotBackLeftLeg.render(f6);
            this.ocelotBackRightLeg.render(f6);
            this.ocelotFrontLeftLeg.render(f6);
            this.ocelotFrontRightLeg.render(f6);
            this.ocelotTail.render(f6);
            this.ocelotTail2.render(f6);
            GlStateManager.popMatrix();
        } else {
            this.ocelotHead.render(f6);
            this.ocelotBody.render(f6);
            this.ocelotTail.render(f6);
            this.ocelotTail2.render(f6);
            this.ocelotBackLeftLeg.render(f6);
            this.ocelotBackRightLeg.render(f6);
            this.ocelotFrontLeftLeg.render(f6);
            this.ocelotFrontRightLeg.render(f6);
        }
    }

    public ModelOcelot() {
        this.setTextureOffset("head.main", 0, 0);
        this.setTextureOffset("head.nose", 0, 24);
        this.setTextureOffset("head.ear1", 0, 10);
        this.setTextureOffset("head.ear2", 6, 10);
        this.ocelotHead = new ModelRenderer(this, "head");
        this.ocelotHead.addBox("main", -2.5f, -2.0f, -3.0f, 5, 4, 5);
        this.ocelotHead.addBox("nose", -1.5f, 0.0f, -4.0f, 3, 2, 2);
        this.ocelotHead.addBox("ear1", -2.0f, -3.0f, 0.0f, 1, 1, 2);
        this.ocelotHead.addBox("ear2", 1.0f, -3.0f, 0.0f, 1, 1, 2);
        this.ocelotHead.setRotationPoint(0.0f, 15.0f, -9.0f);
        this.ocelotBody = new ModelRenderer(this, 20, 0);
        this.ocelotBody.addBox(-2.0f, 3.0f, -8.0f, 4, 16, 6, 0.0f);
        this.ocelotBody.setRotationPoint(0.0f, 12.0f, -10.0f);
        this.ocelotTail = new ModelRenderer(this, 0, 15);
        this.ocelotTail.addBox(-0.5f, 0.0f, 0.0f, 1, 8, 1);
        this.ocelotTail.rotateAngleX = 0.9f;
        this.ocelotTail.setRotationPoint(0.0f, 15.0f, 8.0f);
        this.ocelotTail2 = new ModelRenderer(this, 4, 15);
        this.ocelotTail2.addBox(-0.5f, 0.0f, 0.0f, 1, 8, 1);
        this.ocelotTail2.setRotationPoint(0.0f, 20.0f, 14.0f);
        this.ocelotBackLeftLeg = new ModelRenderer(this, 8, 13);
        this.ocelotBackLeftLeg.addBox(-1.0f, 0.0f, 1.0f, 2, 6, 2);
        this.ocelotBackLeftLeg.setRotationPoint(1.1f, 18.0f, 5.0f);
        this.ocelotBackRightLeg = new ModelRenderer(this, 8, 13);
        this.ocelotBackRightLeg.addBox(-1.0f, 0.0f, 1.0f, 2, 6, 2);
        this.ocelotBackRightLeg.setRotationPoint(-1.1f, 18.0f, 5.0f);
        this.ocelotFrontLeftLeg = new ModelRenderer(this, 40, 0);
        this.ocelotFrontLeftLeg.addBox(-1.0f, 0.0f, 0.0f, 2, 10, 2);
        this.ocelotFrontLeftLeg.setRotationPoint(1.2f, 13.8f, -5.0f);
        this.ocelotFrontRightLeg = new ModelRenderer(this, 40, 0);
        this.ocelotFrontRightLeg.addBox(-1.0f, 0.0f, 0.0f, 2, 10, 2);
        this.ocelotFrontRightLeg.setRotationPoint(-1.2f, 13.8f, -5.0f);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entityLivingBase, float f, float f2, float f3) {
        EntityOcelot entityOcelot = (EntityOcelot)entityLivingBase;
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
        if (entityOcelot.isSneaking()) {
            this.ocelotBody.rotationPointY += 1.0f;
            this.ocelotHead.rotationPointY += 2.0f;
            this.ocelotTail.rotationPointY += 1.0f;
            this.ocelotTail2.rotationPointY += -4.0f;
            this.ocelotTail2.rotationPointZ += 2.0f;
            this.ocelotTail.rotateAngleX = 1.5707964f;
            this.ocelotTail2.rotateAngleX = 1.5707964f;
            this.field_78163_i = 0;
        } else if (entityOcelot.isSprinting()) {
            this.ocelotTail2.rotationPointY = this.ocelotTail.rotationPointY;
            this.ocelotTail2.rotationPointZ += 2.0f;
            this.ocelotTail.rotateAngleX = 1.5707964f;
            this.ocelotTail2.rotateAngleX = 1.5707964f;
            this.field_78163_i = 2;
        } else if (entityOcelot.isSitting()) {
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

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        this.ocelotHead.rotateAngleX = f5 / 57.295776f;
        this.ocelotHead.rotateAngleY = f4 / 57.295776f;
        if (this.field_78163_i != 3) {
            this.ocelotBody.rotateAngleX = 1.5707964f;
            if (this.field_78163_i == 2) {
                this.ocelotBackLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662f) * 1.0f * f2;
                this.ocelotBackRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662f + 0.3f) * 1.0f * f2;
                this.ocelotFrontLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662f + (float)Math.PI + 0.3f) * 1.0f * f2;
                this.ocelotFrontRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662f + (float)Math.PI) * 1.0f * f2;
                this.ocelotTail2.rotateAngleX = 1.7278761f + 0.31415927f * MathHelper.cos(f) * f2;
            } else {
                this.ocelotBackLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662f) * 1.0f * f2;
                this.ocelotBackRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662f + (float)Math.PI) * 1.0f * f2;
                this.ocelotFrontLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662f + (float)Math.PI) * 1.0f * f2;
                this.ocelotFrontRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662f) * 1.0f * f2;
                this.ocelotTail2.rotateAngleX = this.field_78163_i == 1 ? 1.7278761f + 0.7853982f * MathHelper.cos(f) * f2 : 1.7278761f + 0.47123894f * MathHelper.cos(f) * f2;
            }
        }
    }
}

