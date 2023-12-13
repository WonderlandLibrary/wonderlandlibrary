// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.model;

import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;

public class ModelWither extends ModelBase
{
    private ModelRenderer[] field_82905_a;
    private ModelRenderer[] field_82904_b;
    private static final String __OBFID = "CL_00000867";
    
    public ModelWither(final float p_i46302_1_) {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.field_82905_a = new ModelRenderer[3];
        (this.field_82905_a[0] = new ModelRenderer(this, 0, 16)).addBox(-10.0f, 3.9f, -0.5f, 20, 3, 3, p_i46302_1_);
        (this.field_82905_a[1] = new ModelRenderer(this).setTextureSize(this.textureWidth, this.textureHeight)).setRotationPoint(-2.0f, 6.9f, -0.5f);
        this.field_82905_a[1].setTextureOffset(0, 22).addBox(0.0f, 0.0f, 0.0f, 3, 10, 3, p_i46302_1_);
        this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0f, 1.5f, 0.5f, 11, 2, 2, p_i46302_1_);
        this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0f, 4.0f, 0.5f, 11, 2, 2, p_i46302_1_);
        this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0f, 6.5f, 0.5f, 11, 2, 2, p_i46302_1_);
        (this.field_82905_a[2] = new ModelRenderer(this, 12, 22)).addBox(0.0f, 0.0f, 0.0f, 3, 6, 3, p_i46302_1_);
        this.field_82904_b = new ModelRenderer[3];
        (this.field_82904_b[0] = new ModelRenderer(this, 0, 0)).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8, p_i46302_1_);
        (this.field_82904_b[1] = new ModelRenderer(this, 32, 0)).addBox(-4.0f, -4.0f, -4.0f, 6, 6, 6, p_i46302_1_);
        this.field_82904_b[1].rotationPointX = -8.0f;
        this.field_82904_b[1].rotationPointY = 4.0f;
        (this.field_82904_b[2] = new ModelRenderer(this, 32, 0)).addBox(-4.0f, -4.0f, -4.0f, 6, 6, 6, p_i46302_1_);
        this.field_82904_b[2].rotationPointX = 10.0f;
        this.field_82904_b[2].rotationPointY = 4.0f;
    }
    
    @Override
    public void render(final Entity p_78088_1_, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        for (final ModelRenderer var11 : this.field_82904_b) {
            var11.render(p_78088_7_);
        }
        for (final ModelRenderer var11 : this.field_82905_a) {
            var11.render(p_78088_7_);
        }
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity p_78087_7_) {
        final float var8 = MathHelper.cos(p_78087_3_ * 0.1f);
        this.field_82905_a[1].rotateAngleX = (0.065f + 0.05f * var8) * 3.1415927f;
        this.field_82905_a[2].setRotationPoint(-2.0f, 6.9f + MathHelper.cos(this.field_82905_a[1].rotateAngleX) * 10.0f, -0.5f + MathHelper.sin(this.field_82905_a[1].rotateAngleX) * 10.0f);
        this.field_82905_a[2].rotateAngleX = (0.265f + 0.1f * var8) * 3.1415927f;
        this.field_82904_b[0].rotateAngleY = p_78087_4_ / 57.295776f;
        this.field_82904_b[0].rotateAngleX = p_78087_5_ / 57.295776f;
    }
    
    @Override
    public void setLivingAnimations(final EntityLivingBase p_78086_1_, final float p_78086_2_, final float p_78086_3_, final float p_78086_4_) {
        final EntityWither var5 = (EntityWither)p_78086_1_;
        for (int var6 = 1; var6 < 3; ++var6) {
            this.field_82904_b[var6].rotateAngleY = (var5.func_82207_a(var6 - 1) - p_78086_1_.renderYawOffset) / 57.295776f;
            this.field_82904_b[var6].rotateAngleX = var5.func_82210_r(var6 - 1) / 57.295776f;
        }
    }
}
