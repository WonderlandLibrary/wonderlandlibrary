// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.EntityLivingBase;

public class ModelMagmaCube extends ModelBase
{
    ModelRenderer[] segments;
    ModelRenderer core;
    
    public ModelMagmaCube() {
        this.segments = new ModelRenderer[8];
        for (int i = 0; i < this.segments.length; ++i) {
            int j = 0;
            int k;
            if ((k = i) == 2) {
                j = 24;
                k = 10;
            }
            else if (i == 3) {
                j = 24;
                k = 19;
            }
            (this.segments[i] = new ModelRenderer(this, j, k)).addBox(-4.0f, (float)(16 + i), -4.0f, 8, 1, 8);
        }
        (this.core = new ModelRenderer(this, 0, 16)).addBox(-2.0f, 18.0f, -2.0f, 4, 4, 4);
    }
    
    @Override
    public void setLivingAnimations(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTickTime) {
        final EntityMagmaCube entitymagmacube = (EntityMagmaCube)entitylivingbaseIn;
        float f = entitymagmacube.prevSquishFactor + (entitymagmacube.squishFactor - entitymagmacube.prevSquishFactor) * partialTickTime;
        if (f < 0.0f) {
            f = 0.0f;
        }
        for (int i = 0; i < this.segments.length; ++i) {
            this.segments[i].rotationPointY = -(4 - i) * f * 1.7f;
        }
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.core.render(scale);
        for (final ModelRenderer modelrenderer : this.segments) {
            modelrenderer.render(scale);
        }
    }
}
