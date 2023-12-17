// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;

public class ModelWither extends ModelBase
{
    private final ModelRenderer[] upperBodyParts;
    private final ModelRenderer[] heads;
    
    public ModelWither(final float p_i46302_1_) {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.upperBodyParts = new ModelRenderer[3];
        (this.upperBodyParts[0] = new ModelRenderer(this, 0, 16)).addBox(-10.0f, 3.9f, -0.5f, 20, 3, 3, p_i46302_1_);
        (this.upperBodyParts[1] = new ModelRenderer(this).setTextureSize(this.textureWidth, this.textureHeight)).setRotationPoint(-2.0f, 6.9f, -0.5f);
        this.upperBodyParts[1].setTextureOffset(0, 22).addBox(0.0f, 0.0f, 0.0f, 3, 10, 3, p_i46302_1_);
        this.upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0f, 1.5f, 0.5f, 11, 2, 2, p_i46302_1_);
        this.upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0f, 4.0f, 0.5f, 11, 2, 2, p_i46302_1_);
        this.upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0f, 6.5f, 0.5f, 11, 2, 2, p_i46302_1_);
        (this.upperBodyParts[2] = new ModelRenderer(this, 12, 22)).addBox(0.0f, 0.0f, 0.0f, 3, 6, 3, p_i46302_1_);
        this.heads = new ModelRenderer[3];
        (this.heads[0] = new ModelRenderer(this, 0, 0)).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8, p_i46302_1_);
        (this.heads[1] = new ModelRenderer(this, 32, 0)).addBox(-4.0f, -4.0f, -4.0f, 6, 6, 6, p_i46302_1_);
        this.heads[1].rotationPointX = -8.0f;
        this.heads[1].rotationPointY = 4.0f;
        (this.heads[2] = new ModelRenderer(this, 32, 0)).addBox(-4.0f, -4.0f, -4.0f, 6, 6, 6, p_i46302_1_);
        this.heads[2].rotationPointX = 10.0f;
        this.heads[2].rotationPointY = 4.0f;
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        for (final ModelRenderer modelrenderer : this.heads) {
            modelrenderer.render(scale);
        }
        for (final ModelRenderer modelrenderer2 : this.upperBodyParts) {
            modelrenderer2.render(scale);
        }
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        final float f = MathHelper.cos(ageInTicks * 0.1f);
        this.upperBodyParts[1].rotateAngleX = (0.065f + 0.05f * f) * 3.1415927f;
        this.upperBodyParts[2].setRotationPoint(-2.0f, 6.9f + MathHelper.cos(this.upperBodyParts[1].rotateAngleX) * 10.0f, -0.5f + MathHelper.sin(this.upperBodyParts[1].rotateAngleX) * 10.0f);
        this.upperBodyParts[2].rotateAngleX = (0.265f + 0.1f * f) * 3.1415927f;
        this.heads[0].rotateAngleY = netHeadYaw * 0.017453292f;
        this.heads[0].rotateAngleX = headPitch * 0.017453292f;
    }
    
    @Override
    public void setLivingAnimations(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTickTime) {
        final EntityWither entitywither = (EntityWither)entitylivingbaseIn;
        for (int i = 1; i < 3; ++i) {
            this.heads[i].rotateAngleY = (entitywither.getHeadYRotation(i - 1) - entitylivingbaseIn.renderYawOffset) * 0.017453292f;
            this.heads[i].rotateAngleX = entitywither.getHeadXRotation(i - 1) * 0.017453292f;
        }
    }
}
