// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.Entity;

public class ModelLlama extends ModelQuadruped
{
    private final ModelRenderer chest1;
    private final ModelRenderer chest2;
    
    public ModelLlama(final float p_i47226_1_) {
        super(15, p_i47226_1_);
        this.textureWidth = 128;
        this.textureHeight = 64;
        (this.head = new ModelRenderer(this, 0, 0)).addBox(-2.0f, -14.0f, -10.0f, 4, 4, 9, p_i47226_1_);
        this.head.setRotationPoint(0.0f, 7.0f, -6.0f);
        this.head.setTextureOffset(0, 14).addBox(-4.0f, -16.0f, -6.0f, 8, 18, 6, p_i47226_1_);
        this.head.setTextureOffset(17, 0).addBox(-4.0f, -19.0f, -4.0f, 3, 3, 2, p_i47226_1_);
        this.head.setTextureOffset(17, 0).addBox(1.0f, -19.0f, -4.0f, 3, 3, 2, p_i47226_1_);
        (this.body = new ModelRenderer(this, 29, 0)).addBox(-6.0f, -10.0f, -7.0f, 12, 18, 10, p_i47226_1_);
        this.body.setRotationPoint(0.0f, 5.0f, 2.0f);
        (this.chest1 = new ModelRenderer(this, 45, 28)).addBox(-3.0f, 0.0f, 0.0f, 8, 8, 3, p_i47226_1_);
        this.chest1.setRotationPoint(-8.5f, 3.0f, 3.0f);
        this.chest1.rotateAngleY = 1.5707964f;
        (this.chest2 = new ModelRenderer(this, 45, 41)).addBox(-3.0f, 0.0f, 0.0f, 8, 8, 3, p_i47226_1_);
        this.chest2.setRotationPoint(5.5f, 3.0f, 3.0f);
        this.chest2.rotateAngleY = 1.5707964f;
        final int i = 4;
        final int j = 14;
        (this.leg1 = new ModelRenderer(this, 29, 29)).addBox(-2.0f, 0.0f, -2.0f, 4, 14, 4, p_i47226_1_);
        this.leg1.setRotationPoint(-2.5f, 10.0f, 6.0f);
        (this.leg2 = new ModelRenderer(this, 29, 29)).addBox(-2.0f, 0.0f, -2.0f, 4, 14, 4, p_i47226_1_);
        this.leg2.setRotationPoint(2.5f, 10.0f, 6.0f);
        (this.leg3 = new ModelRenderer(this, 29, 29)).addBox(-2.0f, 0.0f, -2.0f, 4, 14, 4, p_i47226_1_);
        this.leg3.setRotationPoint(-2.5f, 10.0f, -4.0f);
        (this.leg4 = new ModelRenderer(this, 29, 29)).addBox(-2.0f, 0.0f, -2.0f, 4, 14, 4, p_i47226_1_);
        this.leg4.setRotationPoint(2.5f, 10.0f, -4.0f);
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
        final AbstractChestHorse abstractchesthorse = (AbstractChestHorse)entityIn;
        final boolean flag = !abstractchesthorse.isChild() && abstractchesthorse.hasChest();
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        if (this.isChild) {
            final float f = 2.0f;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, this.childYOffset * scale, this.childZOffset * scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            final float f2 = 0.7f;
            GlStateManager.scale(0.71428573f, 0.64935064f, 0.7936508f);
            GlStateManager.translate(0.0f, 21.0f * scale, 0.22f);
            this.head.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            final float f3 = 1.1f;
            GlStateManager.scale(0.625f, 0.45454544f, 0.45454544f);
            GlStateManager.translate(0.0f, 33.0f * scale, 0.0f);
            this.body.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.45454544f, 0.41322312f, 0.45454544f);
            GlStateManager.translate(0.0f, 33.0f * scale, 0.0f);
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
        if (flag) {
            this.chest1.render(scale);
            this.chest2.render(scale);
        }
    }
}
