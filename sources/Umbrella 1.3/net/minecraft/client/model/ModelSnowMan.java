/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelSnowMan
extends ModelBase {
    public ModelRenderer body;
    public ModelRenderer bottomBody;
    public ModelRenderer head;
    public ModelRenderer rightHand;
    public ModelRenderer leftHand;
    private static final String __OBFID = "CL_00000859";

    public ModelSnowMan() {
        float var1 = 4.0f;
        float var2 = 0.0f;
        this.head = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
        this.head.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, var2 - 0.5f);
        this.head.setRotationPoint(0.0f, 0.0f + var1, 0.0f);
        this.rightHand = new ModelRenderer(this, 32, 0).setTextureSize(64, 64);
        this.rightHand.addBox(-1.0f, 0.0f, -1.0f, 12, 2, 2, var2 - 0.5f);
        this.rightHand.setRotationPoint(0.0f, 0.0f + var1 + 9.0f - 7.0f, 0.0f);
        this.leftHand = new ModelRenderer(this, 32, 0).setTextureSize(64, 64);
        this.leftHand.addBox(-1.0f, 0.0f, -1.0f, 12, 2, 2, var2 - 0.5f);
        this.leftHand.setRotationPoint(0.0f, 0.0f + var1 + 9.0f - 7.0f, 0.0f);
        this.body = new ModelRenderer(this, 0, 16).setTextureSize(64, 64);
        this.body.addBox(-5.0f, -10.0f, -5.0f, 10, 10, 10, var2 - 0.5f);
        this.body.setRotationPoint(0.0f, 0.0f + var1 + 9.0f, 0.0f);
        this.bottomBody = new ModelRenderer(this, 0, 36).setTextureSize(64, 64);
        this.bottomBody.addBox(-6.0f, -12.0f, -6.0f, 12, 12, 12, var2 - 0.5f);
        this.bottomBody.setRotationPoint(0.0f, 0.0f + var1 + 20.0f, 0.0f);
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        this.head.rotateAngleY = p_78087_4_ / 57.295776f;
        this.head.rotateAngleX = p_78087_5_ / 57.295776f;
        this.body.rotateAngleY = p_78087_4_ / 57.295776f * 0.25f;
        float var8 = MathHelper.sin(this.body.rotateAngleY);
        float var9 = MathHelper.cos(this.body.rotateAngleY);
        this.rightHand.rotateAngleZ = 1.0f;
        this.leftHand.rotateAngleZ = -1.0f;
        this.rightHand.rotateAngleY = 0.0f + this.body.rotateAngleY;
        this.leftHand.rotateAngleY = (float)Math.PI + this.body.rotateAngleY;
        this.rightHand.rotationPointX = var9 * 5.0f;
        this.rightHand.rotationPointZ = -var8 * 5.0f;
        this.leftHand.rotationPointX = -var9 * 5.0f;
        this.leftHand.rotationPointZ = var8 * 5.0f;
    }

    @Override
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.body.render(p_78088_7_);
        this.bottomBody.render(p_78088_7_);
        this.head.render(p_78088_7_);
        this.rightHand.render(p_78088_7_);
        this.leftHand.render(p_78088_7_);
    }
}

