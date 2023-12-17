// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelSquid extends ModelBase
{
    ModelRenderer squidBody;
    ModelRenderer[] squidTentacles;
    
    public ModelSquid() {
        this.squidTentacles = new ModelRenderer[8];
        final int i = -16;
        (this.squidBody = new ModelRenderer(this, 0, 0)).addBox(-6.0f, -8.0f, -6.0f, 12, 16, 12);
        final ModelRenderer squidBody = this.squidBody;
        squidBody.rotationPointY += 8.0f;
        for (int j = 0; j < this.squidTentacles.length; ++j) {
            this.squidTentacles[j] = new ModelRenderer(this, 48, 0);
            double d0 = j * 3.141592653589793 * 2.0 / this.squidTentacles.length;
            final float f = (float)Math.cos(d0) * 5.0f;
            final float f2 = (float)Math.sin(d0) * 5.0f;
            this.squidTentacles[j].addBox(-1.0f, 0.0f, -1.0f, 2, 18, 2);
            this.squidTentacles[j].rotationPointX = f;
            this.squidTentacles[j].rotationPointZ = f2;
            this.squidTentacles[j].rotationPointY = 15.0f;
            d0 = j * 3.141592653589793 * -2.0 / this.squidTentacles.length + 1.5707963267948966;
            this.squidTentacles[j].rotateAngleY = (float)d0;
        }
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        for (final ModelRenderer modelrenderer : this.squidTentacles) {
            modelrenderer.rotateAngleX = ageInTicks;
        }
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.squidBody.render(scale);
        for (final ModelRenderer modelrenderer : this.squidTentacles) {
            modelrenderer.render(scale);
        }
    }
}
