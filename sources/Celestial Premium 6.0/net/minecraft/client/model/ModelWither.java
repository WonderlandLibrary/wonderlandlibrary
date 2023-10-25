/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.math.MathHelper;

public class ModelWither
extends ModelBase {
    private final ModelRenderer[] upperBodyParts;
    private final ModelRenderer[] heads;

    public ModelWither(float p_i46302_1_) {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.upperBodyParts = new ModelRenderer[3];
        this.upperBodyParts[0] = new ModelRenderer(this, 0, 16);
        this.upperBodyParts[0].addBox(-10.0f, 3.9f, -0.5f, 20, 3, 3, p_i46302_1_);
        this.upperBodyParts[1] = new ModelRenderer(this).setTextureSize(this.textureWidth, this.textureHeight);
        this.upperBodyParts[1].setRotationPoint(-2.0f, 6.9f, -0.5f);
        this.upperBodyParts[1].setTextureOffset(0, 22).addBox(0.0f, 0.0f, 0.0f, 3, 10, 3, p_i46302_1_);
        this.upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0f, 1.5f, 0.5f, 11, 2, 2, p_i46302_1_);
        this.upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0f, 4.0f, 0.5f, 11, 2, 2, p_i46302_1_);
        this.upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0f, 6.5f, 0.5f, 11, 2, 2, p_i46302_1_);
        this.upperBodyParts[2] = new ModelRenderer(this, 12, 22);
        this.upperBodyParts[2].addBox(0.0f, 0.0f, 0.0f, 3, 6, 3, p_i46302_1_);
        this.heads = new ModelRenderer[3];
        this.heads[0] = new ModelRenderer(this, 0, 0);
        this.heads[0].addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8, p_i46302_1_);
        this.heads[1] = new ModelRenderer(this, 32, 0);
        this.heads[1].addBox(-4.0f, -4.0f, -4.0f, 6, 6, 6, p_i46302_1_);
        this.heads[1].rotationPointX = -8.0f;
        this.heads[1].rotationPointY = 4.0f;
        this.heads[2] = new ModelRenderer(this, 32, 0);
        this.heads[2].addBox(-4.0f, -4.0f, -4.0f, 6, 6, 6, p_i46302_1_);
        this.heads[2].rotationPointX = 10.0f;
        this.heads[2].rotationPointY = 4.0f;
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        for (ModelRenderer modelrenderer : this.heads) {
            modelrenderer.render(scale);
        }
        for (ModelRenderer modelrenderer1 : this.upperBodyParts) {
            modelrenderer1.render(scale);
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        float f = MathHelper.cos(ageInTicks * 0.1f);
        this.upperBodyParts[1].rotateAngleX = (0.065f + 0.05f * f) * (float)Math.PI;
        this.upperBodyParts[2].setRotationPoint(-2.0f, 6.9f + MathHelper.cos(this.upperBodyParts[1].rotateAngleX) * 10.0f, -0.5f + MathHelper.sin(this.upperBodyParts[1].rotateAngleX) * 10.0f);
        this.upperBodyParts[2].rotateAngleX = (0.265f + 0.1f * f) * (float)Math.PI;
        this.heads[0].rotateAngleY = netHeadYaw * ((float)Math.PI / 180);
        this.heads[0].rotateAngleX = headPitch * ((float)Math.PI / 180);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {
        EntityWither entitywither = (EntityWither)entitylivingbaseIn;
        for (int i = 1; i < 3; ++i) {
            this.heads[i].rotateAngleY = (entitywither.getHeadYRotation(i - 1) - entitylivingbaseIn.renderYawOffset) * ((float)Math.PI / 180);
            this.heads[i].rotateAngleX = entitywither.getHeadXRotation(i - 1) * ((float)Math.PI / 180);
        }
    }
}

