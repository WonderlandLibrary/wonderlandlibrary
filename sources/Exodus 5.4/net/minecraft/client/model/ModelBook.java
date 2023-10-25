/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBook
extends ModelBase {
    public ModelRenderer bookSpine;
    public ModelRenderer flippingPageLeft;
    public ModelRenderer pagesLeft;
    public ModelRenderer flippingPageRight;
    public ModelRenderer coverLeft;
    public ModelRenderer pagesRight;
    public ModelRenderer coverRight = new ModelRenderer(this).setTextureOffset(0, 0).addBox(-6.0f, -5.0f, 0.0f, 6, 10, 0);

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        float f7 = (MathHelper.sin(f * 0.02f) * 0.1f + 1.25f) * f4;
        this.coverRight.rotateAngleY = (float)Math.PI + f7;
        this.coverLeft.rotateAngleY = -f7;
        this.pagesRight.rotateAngleY = f7;
        this.pagesLeft.rotateAngleY = -f7;
        this.flippingPageRight.rotateAngleY = f7 - f7 * 2.0f * f2;
        this.flippingPageLeft.rotateAngleY = f7 - f7 * 2.0f * f3;
        this.pagesRight.rotationPointX = MathHelper.sin(f7);
        this.pagesLeft.rotationPointX = MathHelper.sin(f7);
        this.flippingPageRight.rotationPointX = MathHelper.sin(f7);
        this.flippingPageLeft.rotationPointX = MathHelper.sin(f7);
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        this.coverRight.render(f6);
        this.coverLeft.render(f6);
        this.bookSpine.render(f6);
        this.pagesRight.render(f6);
        this.pagesLeft.render(f6);
        this.flippingPageRight.render(f6);
        this.flippingPageLeft.render(f6);
    }

    public ModelBook() {
        this.coverLeft = new ModelRenderer(this).setTextureOffset(16, 0).addBox(0.0f, -5.0f, 0.0f, 6, 10, 0);
        this.pagesRight = new ModelRenderer(this).setTextureOffset(0, 10).addBox(0.0f, -4.0f, -0.99f, 5, 8, 1);
        this.pagesLeft = new ModelRenderer(this).setTextureOffset(12, 10).addBox(0.0f, -4.0f, -0.01f, 5, 8, 1);
        this.flippingPageRight = new ModelRenderer(this).setTextureOffset(24, 10).addBox(0.0f, -4.0f, 0.0f, 5, 8, 0);
        this.flippingPageLeft = new ModelRenderer(this).setTextureOffset(24, 10).addBox(0.0f, -4.0f, 0.0f, 5, 8, 0);
        this.bookSpine = new ModelRenderer(this).setTextureOffset(12, 0).addBox(-1.0f, -5.0f, 0.0f, 2, 10, 0);
        this.coverRight.setRotationPoint(0.0f, 0.0f, -1.0f);
        this.coverLeft.setRotationPoint(0.0f, 0.0f, 1.0f);
        this.bookSpine.rotateAngleY = 1.5707964f;
    }
}

