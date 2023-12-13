// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.model;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;

public class ModelBlaze extends ModelBase
{
    private ModelRenderer[] blazeSticks;
    private ModelRenderer blazeHead;
    private static final String __OBFID = "CL_00000831";
    
    public ModelBlaze() {
        this.blazeSticks = new ModelRenderer[12];
        for (int var1 = 0; var1 < this.blazeSticks.length; ++var1) {
            (this.blazeSticks[var1] = new ModelRenderer(this, 0, 16)).addBox(0.0f, 0.0f, 0.0f, 2, 8, 2);
        }
        (this.blazeHead = new ModelRenderer(this, 0, 0)).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
    }
    
    @Override
    public void render(final Entity p_78088_1_, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.blazeHead.render(p_78088_7_);
        for (int var8 = 0; var8 < this.blazeSticks.length; ++var8) {
            this.blazeSticks[var8].render(p_78088_7_);
        }
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity p_78087_7_) {
        float var8 = p_78087_3_ * 3.1415927f * -0.1f;
        for (int var9 = 0; var9 < 4; ++var9) {
            this.blazeSticks[var9].rotationPointY = -2.0f + MathHelper.cos((var9 * 2 + p_78087_3_) * 0.25f);
            this.blazeSticks[var9].rotationPointX = MathHelper.cos(var8) * 9.0f;
            this.blazeSticks[var9].rotationPointZ = MathHelper.sin(var8) * 9.0f;
            ++var8;
        }
        var8 = 0.7853982f + p_78087_3_ * 3.1415927f * 0.03f;
        for (int var9 = 4; var9 < 8; ++var9) {
            this.blazeSticks[var9].rotationPointY = 2.0f + MathHelper.cos((var9 * 2 + p_78087_3_) * 0.25f);
            this.blazeSticks[var9].rotationPointX = MathHelper.cos(var8) * 7.0f;
            this.blazeSticks[var9].rotationPointZ = MathHelper.sin(var8) * 7.0f;
            ++var8;
        }
        var8 = 0.47123894f + p_78087_3_ * 3.1415927f * -0.05f;
        for (int var9 = 8; var9 < 12; ++var9) {
            this.blazeSticks[var9].rotationPointY = 11.0f + MathHelper.cos((var9 * 1.5f + p_78087_3_) * 0.5f);
            this.blazeSticks[var9].rotationPointX = MathHelper.cos(var8) * 5.0f;
            this.blazeSticks[var9].rotationPointZ = MathHelper.sin(var8) * 5.0f;
            ++var8;
        }
        this.blazeHead.rotateAngleY = p_78087_4_ / 57.295776f;
        this.blazeHead.rotateAngleX = p_78087_5_ / 57.295776f;
    }
}
