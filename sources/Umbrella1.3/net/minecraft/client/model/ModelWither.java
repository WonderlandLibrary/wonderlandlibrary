/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.MathHelper;

public class ModelWither
extends ModelBase {
    private ModelRenderer[] field_82905_a;
    private ModelRenderer[] field_82904_b;
    private static final String __OBFID = "CL_00000867";

    public ModelWither(float p_i46302_1_) {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.field_82905_a = new ModelRenderer[3];
        this.field_82905_a[0] = new ModelRenderer(this, 0, 16);
        this.field_82905_a[0].addBox(-10.0f, 3.9f, -0.5f, 20, 3, 3, p_i46302_1_);
        this.field_82905_a[1] = new ModelRenderer(this).setTextureSize(this.textureWidth, this.textureHeight);
        this.field_82905_a[1].setRotationPoint(-2.0f, 6.9f, -0.5f);
        this.field_82905_a[1].setTextureOffset(0, 22).addBox(0.0f, 0.0f, 0.0f, 3, 10, 3, p_i46302_1_);
        this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0f, 1.5f, 0.5f, 11, 2, 2, p_i46302_1_);
        this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0f, 4.0f, 0.5f, 11, 2, 2, p_i46302_1_);
        this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0f, 6.5f, 0.5f, 11, 2, 2, p_i46302_1_);
        this.field_82905_a[2] = new ModelRenderer(this, 12, 22);
        this.field_82905_a[2].addBox(0.0f, 0.0f, 0.0f, 3, 6, 3, p_i46302_1_);
        this.field_82904_b = new ModelRenderer[3];
        this.field_82904_b[0] = new ModelRenderer(this, 0, 0);
        this.field_82904_b[0].addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8, p_i46302_1_);
        this.field_82904_b[1] = new ModelRenderer(this, 32, 0);
        this.field_82904_b[1].addBox(-4.0f, -4.0f, -4.0f, 6, 6, 6, p_i46302_1_);
        this.field_82904_b[1].rotationPointX = -8.0f;
        this.field_82904_b[1].rotationPointY = 4.0f;
        this.field_82904_b[2] = new ModelRenderer(this, 32, 0);
        this.field_82904_b[2].addBox(-4.0f, -4.0f, -4.0f, 6, 6, 6, p_i46302_1_);
        this.field_82904_b[2].rotationPointX = 10.0f;
        this.field_82904_b[2].rotationPointY = 4.0f;
    }

    @Override
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        for (ModelRenderer var11 : this.field_82904_b) {
            var11.render(p_78088_7_);
        }
        for (ModelRenderer var11 : this.field_82905_a) {
            var11.render(p_78088_7_);
        }
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        float var8 = MathHelper.cos(p_78087_3_ * 0.1f);
        this.field_82905_a[1].rotateAngleX = (0.065f + 0.05f * var8) * (float)Math.PI;
        this.field_82905_a[2].setRotationPoint(-2.0f, 6.9f + MathHelper.cos(this.field_82905_a[1].rotateAngleX) * 10.0f, -0.5f + MathHelper.sin(this.field_82905_a[1].rotateAngleX) * 10.0f);
        this.field_82905_a[2].rotateAngleX = (0.265f + 0.1f * var8) * (float)Math.PI;
        this.field_82904_b[0].rotateAngleY = p_78087_4_ / 57.295776f;
        this.field_82904_b[0].rotateAngleX = p_78087_5_ / 57.295776f;
    }

    @Override
    public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
        EntityWither var5 = (EntityWither)p_78086_1_;
        for (int var6 = 1; var6 < 3; ++var6) {
            this.field_82904_b[var6].rotateAngleY = (var5.func_82207_a(var6 - 1) - p_78086_1_.renderYawOffset) / 57.295776f;
            this.field_82904_b[var6].rotateAngleX = var5.func_82210_r(var6 - 1) / 57.295776f;
        }
    }
}

