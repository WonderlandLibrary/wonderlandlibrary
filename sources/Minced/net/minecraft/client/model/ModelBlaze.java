// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;

public class ModelBlaze extends ModelBase
{
    private final ModelRenderer[] blazeSticks;
    private final ModelRenderer blazeHead;
    
    public ModelBlaze() {
        this.blazeSticks = new ModelRenderer[12];
        for (int i = 0; i < this.blazeSticks.length; ++i) {
            (this.blazeSticks[i] = new ModelRenderer(this, 0, 16)).addBox(0.0f, 0.0f, 0.0f, 2, 8, 2);
        }
        (this.blazeHead = new ModelRenderer(this, 0, 0)).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.blazeHead.render(scale);
        for (final ModelRenderer modelrenderer : this.blazeSticks) {
            modelrenderer.render(scale);
        }
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        float f = ageInTicks * 3.1415927f * -0.1f;
        for (int i = 0; i < 4; ++i) {
            this.blazeSticks[i].rotationPointY = -2.0f + MathHelper.cos((i * 2 + ageInTicks) * 0.25f);
            this.blazeSticks[i].rotationPointX = MathHelper.cos(f) * 9.0f;
            this.blazeSticks[i].rotationPointZ = MathHelper.sin(f) * 9.0f;
            ++f;
        }
        f = 0.7853982f + ageInTicks * 3.1415927f * 0.03f;
        for (int j = 4; j < 8; ++j) {
            this.blazeSticks[j].rotationPointY = 2.0f + MathHelper.cos((j * 2 + ageInTicks) * 0.25f);
            this.blazeSticks[j].rotationPointX = MathHelper.cos(f) * 7.0f;
            this.blazeSticks[j].rotationPointZ = MathHelper.sin(f) * 7.0f;
            ++f;
        }
        f = 0.47123894f + ageInTicks * 3.1415927f * -0.05f;
        for (int k = 8; k < 12; ++k) {
            this.blazeSticks[k].rotationPointY = 11.0f + MathHelper.cos((k * 1.5f + ageInTicks) * 0.5f);
            this.blazeSticks[k].rotationPointX = MathHelper.cos(f) * 5.0f;
            this.blazeSticks[k].rotationPointZ = MathHelper.sin(f) * 5.0f;
            ++f;
        }
        this.blazeHead.rotateAngleY = netHeadYaw * 0.017453292f;
        this.blazeHead.rotateAngleX = headPitch * 0.017453292f;
    }
}
