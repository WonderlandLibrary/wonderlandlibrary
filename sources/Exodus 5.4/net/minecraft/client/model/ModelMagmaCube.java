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
    ModelRenderer core;
    ModelRenderer[] segments = new ModelRenderer[8];

    @Override
    public void setLivingAnimations(EntityLivingBase entityLivingBase, float f, float f2, float f3) {
        EntityMagmaCube entityMagmaCube = (EntityMagmaCube)entityLivingBase;
        float f4 = entityMagmaCube.prevSquishFactor + (entityMagmaCube.squishFactor - entityMagmaCube.prevSquishFactor) * f3;
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        int n = 0;
        while (n < this.segments.length) {
            this.segments[n].rotationPointY = (float)(-(4 - n)) * f4 * 1.7f;
            ++n;
        }
    }

    public ModelMagmaCube() {
        int n = 0;
        while (n < this.segments.length) {
            int n2 = 0;
            int n3 = n;
            if (n == 2) {
                n2 = 24;
                n3 = 10;
            } else if (n == 3) {
                n2 = 24;
                n3 = 19;
            }
            this.segments[n] = new ModelRenderer(this, n2, n3);
            this.segments[n].addBox(-4.0f, 16 + n, -4.0f, 8, 1, 8);
            ++n;
        }
        this.core = new ModelRenderer(this, 0, 16);
        this.core.addBox(-2.0f, 18.0f, -2.0f, 4, 4, 4);
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        this.core.render(f6);
        int n = 0;
        while (n < this.segments.length) {
            this.segments[n].render(f6);
            ++n;
        }
    }
}

