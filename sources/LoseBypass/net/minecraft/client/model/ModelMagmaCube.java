/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMagmaCube;

public class ModelMagmaCube
extends ModelBase {
    ModelRenderer[] segments = new ModelRenderer[8];
    ModelRenderer core;

    public ModelMagmaCube() {
        int i = 0;
        while (true) {
            if (i >= this.segments.length) {
                this.core = new ModelRenderer(this, 0, 16);
                this.core.addBox(-2.0f, 18.0f, -2.0f, 4, 4, 4);
                return;
            }
            int j = 0;
            int k = i;
            if (i == 2) {
                j = 24;
                k = 10;
            } else if (i == 3) {
                j = 24;
                k = 19;
            }
            this.segments[i] = new ModelRenderer(this, j, k);
            this.segments[i].addBox(-4.0f, 16 + i, -4.0f, 8, 1, 8);
            ++i;
        }
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {
        EntityMagmaCube entitymagmacube = (EntityMagmaCube)entitylivingbaseIn;
        float f = entitymagmacube.prevSquishFactor + (entitymagmacube.squishFactor - entitymagmacube.prevSquishFactor) * partialTickTime;
        if (f < 0.0f) {
            f = 0.0f;
        }
        int i = 0;
        while (i < this.segments.length) {
            this.segments[i].rotationPointY = (float)(-(4 - i)) * f * 1.7f;
            ++i;
        }
    }

    @Override
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        this.core.render(scale);
        int i = 0;
        while (i < this.segments.length) {
            this.segments[i].render(scale);
            ++i;
        }
    }
}

