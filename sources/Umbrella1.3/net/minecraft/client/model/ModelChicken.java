/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelChicken
extends ModelBase {
    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;
    public ModelRenderer rightWing;
    public ModelRenderer leftWing;
    public ModelRenderer bill;
    public ModelRenderer chin;
    private static final String __OBFID = "CL_00000835";

    public ModelChicken() {
        int var1 = 16;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-2.0f, -6.0f, -2.0f, 4, 6, 3, 0.0f);
        this.head.setRotationPoint(0.0f, -1 + var1, -4.0f);
        this.bill = new ModelRenderer(this, 14, 0);
        this.bill.addBox(-2.0f, -4.0f, -4.0f, 4, 2, 2, 0.0f);
        this.bill.setRotationPoint(0.0f, -1 + var1, -4.0f);
        this.chin = new ModelRenderer(this, 14, 4);
        this.chin.addBox(-1.0f, -2.0f, -3.0f, 2, 2, 2, 0.0f);
        this.chin.setRotationPoint(0.0f, -1 + var1, -4.0f);
        this.body = new ModelRenderer(this, 0, 9);
        this.body.addBox(-3.0f, -4.0f, -3.0f, 6, 8, 6, 0.0f);
        this.body.setRotationPoint(0.0f, var1, 0.0f);
        this.rightLeg = new ModelRenderer(this, 26, 0);
        this.rightLeg.addBox(-1.0f, 0.0f, -3.0f, 3, 5, 3);
        this.rightLeg.setRotationPoint(-2.0f, 3 + var1, 1.0f);
        this.leftLeg = new ModelRenderer(this, 26, 0);
        this.leftLeg.addBox(-1.0f, 0.0f, -3.0f, 3, 5, 3);
        this.leftLeg.setRotationPoint(1.0f, 3 + var1, 1.0f);
        this.rightWing = new ModelRenderer(this, 24, 13);
        this.rightWing.addBox(0.0f, 0.0f, -3.0f, 1, 4, 6);
        this.rightWing.setRotationPoint(-4.0f, -3 + var1, 0.0f);
        this.leftWing = new ModelRenderer(this, 24, 13);
        this.leftWing.addBox(-1.0f, 0.0f, -3.0f, 1, 4, 6);
        this.leftWing.setRotationPoint(4.0f, -3 + var1, 0.0f);
    }

    @Override
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        if (this.isChild) {
            float var8 = 2.0f;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 5.0f * p_78088_7_, 2.0f * p_78088_7_);
            this.head.render(p_78088_7_);
            this.bill.render(p_78088_7_);
            this.chin.render(p_78088_7_);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f / var8, 1.0f / var8, 1.0f / var8);
            GlStateManager.translate(0.0f, 24.0f * p_78088_7_, 0.0f);
            this.body.render(p_78088_7_);
            this.rightLeg.render(p_78088_7_);
            this.leftLeg.render(p_78088_7_);
            this.rightWing.render(p_78088_7_);
            this.leftWing.render(p_78088_7_);
            GlStateManager.popMatrix();
        } else {
            this.head.render(p_78088_7_);
            this.bill.render(p_78088_7_);
            this.chin.render(p_78088_7_);
            this.body.render(p_78088_7_);
            this.rightLeg.render(p_78088_7_);
            this.leftLeg.render(p_78088_7_);
            this.rightWing.render(p_78088_7_);
            this.leftWing.render(p_78088_7_);
        }
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        this.head.rotateAngleX = p_78087_5_ / 57.295776f;
        this.head.rotateAngleY = p_78087_4_ / 57.295776f;
        this.bill.rotateAngleX = this.head.rotateAngleX;
        this.bill.rotateAngleY = this.head.rotateAngleY;
        this.chin.rotateAngleX = this.head.rotateAngleX;
        this.chin.rotateAngleY = this.head.rotateAngleY;
        this.body.rotateAngleX = 1.5707964f;
        this.rightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f) * 1.4f * p_78087_2_;
        this.leftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f + (float)Math.PI) * 1.4f * p_78087_2_;
        this.rightWing.rotateAngleZ = p_78087_3_;
        this.leftWing.rotateAngleZ = -p_78087_3_;
    }
}

