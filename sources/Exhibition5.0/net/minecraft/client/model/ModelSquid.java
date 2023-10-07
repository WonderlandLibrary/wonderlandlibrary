// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelSquid extends ModelBase
{
    ModelRenderer squidBody;
    ModelRenderer[] squidTentacles;
    private static final String __OBFID = "CL_00000861";
    
    public ModelSquid() {
        this.squidTentacles = new ModelRenderer[8];
        final byte var1 = -16;
        (this.squidBody = new ModelRenderer(this, 0, 0)).addBox(-6.0f, -8.0f, -6.0f, 12, 16, 12);
        final ModelRenderer squidBody = this.squidBody;
        squidBody.rotationPointY += 24 + var1;
        for (int var2 = 0; var2 < this.squidTentacles.length; ++var2) {
            this.squidTentacles[var2] = new ModelRenderer(this, 48, 0);
            double var3 = var2 * 3.141592653589793 * 2.0 / this.squidTentacles.length;
            final float var4 = (float)Math.cos(var3) * 5.0f;
            final float var5 = (float)Math.sin(var3) * 5.0f;
            this.squidTentacles[var2].addBox(-1.0f, 0.0f, -1.0f, 2, 18, 2);
            this.squidTentacles[var2].rotationPointX = var4;
            this.squidTentacles[var2].rotationPointZ = var5;
            this.squidTentacles[var2].rotationPointY = 31 + var1;
            var3 = var2 * 3.141592653589793 * -2.0 / this.squidTentacles.length + 1.5707963267948966;
            this.squidTentacles[var2].rotateAngleY = (float)var3;
        }
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity p_78087_7_) {
        for (final ModelRenderer var11 : this.squidTentacles) {
            var11.rotateAngleX = p_78087_3_;
        }
    }
    
    @Override
    public void render(final Entity p_78088_1_, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.squidBody.render(p_78088_7_);
        for (int var8 = 0; var8 < this.squidTentacles.length; ++var8) {
            this.squidTentacles[var8].render(p_78088_7_);
        }
    }
}
