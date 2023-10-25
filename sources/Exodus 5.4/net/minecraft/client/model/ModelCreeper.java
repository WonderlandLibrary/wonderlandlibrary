/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelCreeper
extends ModelBase {
    public ModelRenderer leg3;
    public ModelRenderer creeperArmor;
    public ModelRenderer head;
    public ModelRenderer leg4;
    public ModelRenderer leg1;
    public ModelRenderer body;
    public ModelRenderer leg2;

    public ModelCreeper(float f) {
        int n = 6;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f);
        this.head.setRotationPoint(0.0f, n, 0.0f);
        this.creeperArmor = new ModelRenderer(this, 32, 0);
        this.creeperArmor.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, f + 0.5f);
        this.creeperArmor.setRotationPoint(0.0f, n, 0.0f);
        this.body = new ModelRenderer(this, 16, 16);
        this.body.addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, f);
        this.body.setRotationPoint(0.0f, n, 0.0f);
        this.leg1 = new ModelRenderer(this, 0, 16);
        this.leg1.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, f);
        this.leg1.setRotationPoint(-2.0f, 12 + n, 4.0f);
        this.leg2 = new ModelRenderer(this, 0, 16);
        this.leg2.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, f);
        this.leg2.setRotationPoint(2.0f, 12 + n, 4.0f);
        this.leg3 = new ModelRenderer(this, 0, 16);
        this.leg3.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, f);
        this.leg3.setRotationPoint(-2.0f, 12 + n, -4.0f);
        this.leg4 = new ModelRenderer(this, 0, 16);
        this.leg4.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, f);
        this.leg4.setRotationPoint(2.0f, 12 + n, -4.0f);
    }

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        this.head.rotateAngleY = f4 / 57.295776f;
        this.head.rotateAngleX = f5 / 57.295776f;
        this.leg1.rotateAngleX = MathHelper.cos(f * 0.6662f) * 1.4f * f2;
        this.leg2.rotateAngleX = MathHelper.cos(f * 0.6662f + (float)Math.PI) * 1.4f * f2;
        this.leg3.rotateAngleX = MathHelper.cos(f * 0.6662f + (float)Math.PI) * 1.4f * f2;
        this.leg4.rotateAngleX = MathHelper.cos(f * 0.6662f) * 1.4f * f2;
    }

    public ModelCreeper() {
        this(0.0f);
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        this.head.render(f6);
        this.body.render(f6);
        this.leg1.render(f6);
        this.leg2.render(f6);
        this.leg3.render(f6);
        this.leg4.render(f6);
    }
}

