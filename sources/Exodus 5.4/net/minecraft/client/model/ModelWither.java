/*
 * Decompiled with CFR 0.152.
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

    public ModelWither(float f) {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.field_82905_a = new ModelRenderer[3];
        this.field_82905_a[0] = new ModelRenderer(this, 0, 16);
        this.field_82905_a[0].addBox(-10.0f, 3.9f, -0.5f, 20, 3, 3, f);
        this.field_82905_a[1] = new ModelRenderer(this).setTextureSize(this.textureWidth, this.textureHeight);
        this.field_82905_a[1].setRotationPoint(-2.0f, 6.9f, -0.5f);
        this.field_82905_a[1].setTextureOffset(0, 22).addBox(0.0f, 0.0f, 0.0f, 3, 10, 3, f);
        this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0f, 1.5f, 0.5f, 11, 2, 2, f);
        this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0f, 4.0f, 0.5f, 11, 2, 2, f);
        this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0f, 6.5f, 0.5f, 11, 2, 2, f);
        this.field_82905_a[2] = new ModelRenderer(this, 12, 22);
        this.field_82905_a[2].addBox(0.0f, 0.0f, 0.0f, 3, 6, 3, f);
        this.field_82904_b = new ModelRenderer[3];
        this.field_82904_b[0] = new ModelRenderer(this, 0, 0);
        this.field_82904_b[0].addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8, f);
        this.field_82904_b[1] = new ModelRenderer(this, 32, 0);
        this.field_82904_b[1].addBox(-4.0f, -4.0f, -4.0f, 6, 6, 6, f);
        this.field_82904_b[1].rotationPointX = -8.0f;
        this.field_82904_b[1].rotationPointY = 4.0f;
        this.field_82904_b[2] = new ModelRenderer(this, 32, 0);
        this.field_82904_b[2].addBox(-4.0f, -4.0f, -4.0f, 6, 6, 6, f);
        this.field_82904_b[2].rotationPointX = 10.0f;
        this.field_82904_b[2].rotationPointY = 4.0f;
    }

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        float f7 = MathHelper.cos(f3 * 0.1f);
        this.field_82905_a[1].rotateAngleX = (0.065f + 0.05f * f7) * (float)Math.PI;
        this.field_82905_a[2].setRotationPoint(-2.0f, 6.9f + MathHelper.cos(this.field_82905_a[1].rotateAngleX) * 10.0f, -0.5f + MathHelper.sin(this.field_82905_a[1].rotateAngleX) * 10.0f);
        this.field_82905_a[2].rotateAngleX = (0.265f + 0.1f * f7) * (float)Math.PI;
        this.field_82904_b[0].rotateAngleY = f4 / 57.295776f;
        this.field_82904_b[0].rotateAngleX = f5 / 57.295776f;
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        ModelRenderer modelRenderer;
        this.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        ModelRenderer[] modelRendererArray = this.field_82904_b;
        int n = this.field_82904_b.length;
        int n2 = 0;
        while (n2 < n) {
            modelRenderer = modelRendererArray[n2];
            modelRenderer.render(f6);
            ++n2;
        }
        modelRendererArray = this.field_82905_a;
        n = this.field_82905_a.length;
        n2 = 0;
        while (n2 < n) {
            modelRenderer = modelRendererArray[n2];
            modelRenderer.render(f6);
            ++n2;
        }
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entityLivingBase, float f, float f2, float f3) {
        EntityWither entityWither = (EntityWither)entityLivingBase;
        int n = 1;
        while (n < 3) {
            this.field_82904_b[n].rotateAngleY = (entityWither.func_82207_a(n - 1) - entityLivingBase.renderYawOffset) / 57.295776f;
            this.field_82904_b[n].rotateAngleX = entityWither.func_82210_r(n - 1) / 57.295776f;
            ++n;
        }
    }
}

