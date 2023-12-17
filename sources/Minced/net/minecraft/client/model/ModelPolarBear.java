// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelPolarBear extends ModelQuadruped
{
    public ModelPolarBear() {
        super(12, 0.0f);
        this.textureWidth = 128;
        this.textureHeight = 64;
        (this.head = new ModelRenderer(this, 0, 0)).addBox(-3.5f, -3.0f, -3.0f, 7, 7, 7, 0.0f);
        this.head.setRotationPoint(0.0f, 10.0f, -16.0f);
        this.head.setTextureOffset(0, 44).addBox(-2.5f, 1.0f, -6.0f, 5, 3, 3, 0.0f);
        this.head.setTextureOffset(26, 0).addBox(-4.5f, -4.0f, -1.0f, 2, 2, 1, 0.0f);
        final ModelRenderer modelrenderer = this.head.setTextureOffset(26, 0);
        modelrenderer.mirror = true;
        modelrenderer.addBox(2.5f, -4.0f, -1.0f, 2, 2, 1, 0.0f);
        this.body = new ModelRenderer(this);
        this.body.setTextureOffset(0, 19).addBox(-5.0f, -13.0f, -7.0f, 14, 14, 11, 0.0f);
        this.body.setTextureOffset(39, 0).addBox(-4.0f, -25.0f, -7.0f, 12, 12, 10, 0.0f);
        this.body.setRotationPoint(-2.0f, 9.0f, 12.0f);
        final int i = 10;
        (this.leg1 = new ModelRenderer(this, 50, 22)).addBox(-2.0f, 0.0f, -2.0f, 4, 10, 8, 0.0f);
        this.leg1.setRotationPoint(-3.5f, 14.0f, 6.0f);
        (this.leg2 = new ModelRenderer(this, 50, 22)).addBox(-2.0f, 0.0f, -2.0f, 4, 10, 8, 0.0f);
        this.leg2.setRotationPoint(3.5f, 14.0f, 6.0f);
        (this.leg3 = new ModelRenderer(this, 50, 40)).addBox(-2.0f, 0.0f, -2.0f, 4, 10, 6, 0.0f);
        this.leg3.setRotationPoint(-2.5f, 14.0f, -7.0f);
        (this.leg4 = new ModelRenderer(this, 50, 40)).addBox(-2.0f, 0.0f, -2.0f, 4, 10, 6, 0.0f);
        this.leg4.setRotationPoint(2.5f, 14.0f, -7.0f);
        final ModelRenderer leg1 = this.leg1;
        --leg1.rotationPointX;
        final ModelRenderer leg2 = this.leg2;
        ++leg2.rotationPointX;
        final ModelRenderer leg3 = this.leg1;
        leg3.rotationPointZ += 0.0f;
        final ModelRenderer leg4 = this.leg2;
        leg4.rotationPointZ += 0.0f;
        final ModelRenderer leg5 = this.leg3;
        --leg5.rotationPointX;
        final ModelRenderer leg6 = this.leg4;
        ++leg6.rotationPointX;
        final ModelRenderer leg7 = this.leg3;
        --leg7.rotationPointZ;
        final ModelRenderer leg8 = this.leg4;
        --leg8.rotationPointZ;
        this.childZOffset += 2.0f;
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        if (this.isChild) {
            final float f = 2.0f;
            this.childYOffset = 16.0f;
            this.childZOffset = 4.0f;
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.6666667f, 0.6666667f, 0.6666667f);
            GlStateManager.translate(0.0f, this.childYOffset * scale, this.childZOffset * scale);
            this.head.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            GlStateManager.translate(0.0f, 24.0f * scale, 0.0f);
            this.body.render(scale);
            this.leg1.render(scale);
            this.leg2.render(scale);
            this.leg3.render(scale);
            this.leg4.render(scale);
            GlStateManager.popMatrix();
        }
        else {
            this.head.render(scale);
            this.body.render(scale);
            this.leg1.render(scale);
            this.leg2.render(scale);
            this.leg3.render(scale);
            this.leg4.render(scale);
        }
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        final float f = ageInTicks - entityIn.ticksExisted;
        float f2 = ((EntityPolarBear)entityIn).getStandingAnimationScale(f);
        f2 *= f2;
        final float f3 = 1.0f - f2;
        this.body.rotateAngleX = 1.5707964f - f2 * 3.1415927f * 0.35f;
        this.body.rotationPointY = 9.0f * f3 + 11.0f * f2;
        this.leg3.rotationPointY = 14.0f * f3 + -6.0f * f2;
        this.leg3.rotationPointZ = -8.0f * f3 + -4.0f * f2;
        final ModelRenderer leg3 = this.leg3;
        leg3.rotateAngleX -= f2 * 3.1415927f * 0.45f;
        this.leg4.rotationPointY = this.leg3.rotationPointY;
        this.leg4.rotationPointZ = this.leg3.rotationPointZ;
        final ModelRenderer leg4 = this.leg4;
        leg4.rotateAngleX -= f2 * 3.1415927f * 0.45f;
        this.head.rotationPointY = 10.0f * f3 + -12.0f * f2;
        this.head.rotationPointZ = -16.0f * f3 + -3.0f * f2;
        final ModelRenderer head = this.head;
        head.rotateAngleX += f2 * 3.1415927f * 0.15f;
    }
}
