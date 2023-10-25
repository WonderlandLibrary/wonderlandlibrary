/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelSnowMan
extends ModelBase {
    public ModelRenderer head;
    public ModelRenderer bottomBody;
    public ModelRenderer rightHand;
    public ModelRenderer leftHand;
    public ModelRenderer body;

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        this.body.render(f6);
        this.bottomBody.render(f6);
        this.head.render(f6);
        this.rightHand.render(f6);
        this.leftHand.render(f6);
    }

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        super.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        this.head.rotateAngleY = f4 / 57.295776f;
        this.head.rotateAngleX = f5 / 57.295776f;
        this.body.rotateAngleY = f4 / 57.295776f * 0.25f;
        float f7 = MathHelper.sin(this.body.rotateAngleY);
        float f8 = MathHelper.cos(this.body.rotateAngleY);
        this.rightHand.rotateAngleZ = 1.0f;
        this.leftHand.rotateAngleZ = -1.0f;
        this.rightHand.rotateAngleY = 0.0f + this.body.rotateAngleY;
        this.leftHand.rotateAngleY = (float)Math.PI + this.body.rotateAngleY;
        this.rightHand.rotationPointX = f8 * 5.0f;
        this.rightHand.rotationPointZ = -f7 * 5.0f;
        this.leftHand.rotationPointX = -f8 * 5.0f;
        this.leftHand.rotationPointZ = f7 * 5.0f;
    }

    public ModelSnowMan() {
        float f = 4.0f;
        float f2 = 0.0f;
        this.head = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
        this.head.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f2 - 0.5f);
        this.head.setRotationPoint(0.0f, 0.0f + f, 0.0f);
        this.rightHand = new ModelRenderer(this, 32, 0).setTextureSize(64, 64);
        this.rightHand.addBox(-1.0f, 0.0f, -1.0f, 12, 2, 2, f2 - 0.5f);
        this.rightHand.setRotationPoint(0.0f, 0.0f + f + 9.0f - 7.0f, 0.0f);
        this.leftHand = new ModelRenderer(this, 32, 0).setTextureSize(64, 64);
        this.leftHand.addBox(-1.0f, 0.0f, -1.0f, 12, 2, 2, f2 - 0.5f);
        this.leftHand.setRotationPoint(0.0f, 0.0f + f + 9.0f - 7.0f, 0.0f);
        this.body = new ModelRenderer(this, 0, 16).setTextureSize(64, 64);
        this.body.addBox(-5.0f, -10.0f, -5.0f, 10, 10, 10, f2 - 0.5f);
        this.body.setRotationPoint(0.0f, 0.0f + f + 9.0f, 0.0f);
        this.bottomBody = new ModelRenderer(this, 0, 36).setTextureSize(64, 64);
        this.bottomBody.addBox(-6.0f, -12.0f, -6.0f, 12, 12, 12, f2 - 0.5f);
        this.bottomBody.setRotationPoint(0.0f, 0.0f + f + 20.0f, 0.0f);
    }
}

