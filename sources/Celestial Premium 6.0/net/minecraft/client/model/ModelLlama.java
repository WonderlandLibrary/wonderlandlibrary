/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractChestHorse;

public class ModelLlama
extends ModelQuadruped {
    private final ModelRenderer field_191226_i;
    private final ModelRenderer field_191227_j;

    public ModelLlama(float p_i47226_1_) {
        super(15, p_i47226_1_);
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-2.0f, -14.0f, -10.0f, 4, 4, 9, p_i47226_1_);
        this.head.setRotationPoint(0.0f, 7.0f, -6.0f);
        this.head.setTextureOffset(0, 14).addBox(-4.0f, -16.0f, -6.0f, 8, 18, 6, p_i47226_1_);
        this.head.setTextureOffset(17, 0).addBox(-4.0f, -19.0f, -4.0f, 3, 3, 2, p_i47226_1_);
        this.head.setTextureOffset(17, 0).addBox(1.0f, -19.0f, -4.0f, 3, 3, 2, p_i47226_1_);
        this.body = new ModelRenderer(this, 29, 0);
        this.body.addBox(-6.0f, -10.0f, -7.0f, 12, 18, 10, p_i47226_1_);
        this.body.setRotationPoint(0.0f, 5.0f, 2.0f);
        this.field_191226_i = new ModelRenderer(this, 45, 28);
        this.field_191226_i.addBox(-3.0f, 0.0f, 0.0f, 8, 8, 3, p_i47226_1_);
        this.field_191226_i.setRotationPoint(-8.5f, 3.0f, 3.0f);
        this.field_191226_i.rotateAngleY = 1.5707964f;
        this.field_191227_j = new ModelRenderer(this, 45, 41);
        this.field_191227_j.addBox(-3.0f, 0.0f, 0.0f, 8, 8, 3, p_i47226_1_);
        this.field_191227_j.setRotationPoint(5.5f, 3.0f, 3.0f);
        this.field_191227_j.rotateAngleY = 1.5707964f;
        int i = 4;
        int j = 14;
        this.leg1 = new ModelRenderer(this, 29, 29);
        this.leg1.addBox(-2.0f, 0.0f, -2.0f, 4, 14, 4, p_i47226_1_);
        this.leg1.setRotationPoint(-2.5f, 10.0f, 6.0f);
        this.leg2 = new ModelRenderer(this, 29, 29);
        this.leg2.addBox(-2.0f, 0.0f, -2.0f, 4, 14, 4, p_i47226_1_);
        this.leg2.setRotationPoint(2.5f, 10.0f, 6.0f);
        this.leg3 = new ModelRenderer(this, 29, 29);
        this.leg3.addBox(-2.0f, 0.0f, -2.0f, 4, 14, 4, p_i47226_1_);
        this.leg3.setRotationPoint(-2.5f, 10.0f, -4.0f);
        this.leg4 = new ModelRenderer(this, 29, 29);
        this.leg4.addBox(-2.0f, 0.0f, -2.0f, 4, 14, 4, p_i47226_1_);
        this.leg4.setRotationPoint(2.5f, 10.0f, -4.0f);
        this.leg1.rotationPointX -= 1.0f;
        this.leg2.rotationPointX += 1.0f;
        this.leg1.rotationPointZ += 0.0f;
        this.leg2.rotationPointZ += 0.0f;
        this.leg3.rotationPointX -= 1.0f;
        this.leg4.rotationPointX += 1.0f;
        this.leg3.rotationPointZ -= 1.0f;
        this.leg4.rotationPointZ -= 1.0f;
        this.childZOffset += 2.0f;
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        AbstractChestHorse abstractchesthorse = (AbstractChestHorse)entityIn;
        boolean flag = !abstractchesthorse.isChild() && abstractchesthorse.func_190695_dh();
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        if (this.isChild) {
            float f = 2.0f;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, this.childYOffset * scale, this.childZOffset * scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            float f1 = 0.7f;
            GlStateManager.scale(0.71428573f, 0.64935064f, 0.7936508f);
            GlStateManager.translate(0.0f, 21.0f * scale, 0.22f);
            this.head.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            float f2 = 1.1f;
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
        } else {
            this.head.render(scale);
            this.body.render(scale);
            this.leg1.render(scale);
            this.leg2.render(scale);
            this.leg3.render(scale);
            this.leg4.render(scale);
        }
        if (flag) {
            this.field_191226_i.render(scale);
            this.field_191227_j.render(scale);
        }
    }
}

