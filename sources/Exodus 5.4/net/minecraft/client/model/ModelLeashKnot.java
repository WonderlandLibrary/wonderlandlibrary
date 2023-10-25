/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLeashKnot
extends ModelBase {
    public ModelRenderer field_110723_a;

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        super.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        this.field_110723_a.rotateAngleY = f4 / 57.295776f;
        this.field_110723_a.rotateAngleX = f5 / 57.295776f;
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        this.field_110723_a.render(f6);
    }

    public ModelLeashKnot() {
        this(0, 0, 32, 32);
    }

    public ModelLeashKnot(int n, int n2, int n3, int n4) {
        this.textureWidth = n3;
        this.textureHeight = n4;
        this.field_110723_a = new ModelRenderer(this, n, n2);
        this.field_110723_a.addBox(-3.0f, -6.0f, -3.0f, 6, 8, 6, 0.0f);
        this.field_110723_a.setRotationPoint(0.0f, 0.0f, 0.0f);
    }
}

