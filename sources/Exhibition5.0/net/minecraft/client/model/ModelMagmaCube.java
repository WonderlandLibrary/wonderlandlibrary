// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.EntityLivingBase;

public class ModelMagmaCube extends ModelBase
{
    ModelRenderer[] segments;
    ModelRenderer core;
    private static final String __OBFID = "CL_00000842";
    
    public ModelMagmaCube() {
        this.segments = new ModelRenderer[8];
        for (int var1 = 0; var1 < this.segments.length; ++var1) {
            byte var2 = 0;
            int var3;
            if ((var3 = var1) == 2) {
                var2 = 24;
                var3 = 10;
            }
            else if (var1 == 3) {
                var2 = 24;
                var3 = 19;
            }
            (this.segments[var1] = new ModelRenderer(this, var2, var3)).addBox(-4.0f, 16 + var1, -4.0f, 8, 1, 8);
        }
        (this.core = new ModelRenderer(this, 0, 16)).addBox(-2.0f, 18.0f, -2.0f, 4, 4, 4);
    }
    
    @Override
    public void setLivingAnimations(final EntityLivingBase p_78086_1_, final float p_78086_2_, final float p_78086_3_, final float p_78086_4_) {
        final EntityMagmaCube var5 = (EntityMagmaCube)p_78086_1_;
        float var6 = var5.prevSquishFactor + (var5.squishFactor - var5.prevSquishFactor) * p_78086_4_;
        if (var6 < 0.0f) {
            var6 = 0.0f;
        }
        for (int var7 = 0; var7 < this.segments.length; ++var7) {
            this.segments[var7].rotationPointY = -(4 - var7) * var6 * 1.7f;
        }
    }
    
    @Override
    public void render(final Entity p_78088_1_, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.core.render(p_78088_7_);
        for (int var8 = 0; var8 < this.segments.length; ++var8) {
            this.segments[var8].render(p_78088_7_);
        }
    }
}
