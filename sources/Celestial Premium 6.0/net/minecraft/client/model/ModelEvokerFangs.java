/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelEvokerFangs
extends ModelBase {
    private final ModelRenderer field_191213_a = new ModelRenderer(this, 0, 0);
    private final ModelRenderer field_191214_b;
    private final ModelRenderer field_191215_c;

    public ModelEvokerFangs() {
        this.field_191213_a.setRotationPoint(-5.0f, 22.0f, -5.0f);
        this.field_191213_a.addBox(0.0f, 0.0f, 0.0f, 10, 12, 10);
        this.field_191214_b = new ModelRenderer(this, 40, 0);
        this.field_191214_b.setRotationPoint(1.5f, 22.0f, -4.0f);
        this.field_191214_b.addBox(0.0f, 0.0f, 0.0f, 4, 14, 8);
        this.field_191215_c = new ModelRenderer(this, 40, 0);
        this.field_191215_c.setRotationPoint(-1.5f, 22.0f, 4.0f);
        this.field_191215_c.addBox(0.0f, 0.0f, 0.0f, 4, 14, 8);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        float f = limbSwing * 2.0f;
        if (f > 1.0f) {
            f = 1.0f;
        }
        f = 1.0f - f * f * f;
        this.field_191214_b.rotateAngleZ = (float)Math.PI - f * 0.35f * (float)Math.PI;
        this.field_191215_c.rotateAngleZ = (float)Math.PI + f * 0.35f * (float)Math.PI;
        this.field_191215_c.rotateAngleY = (float)Math.PI;
        float f1 = (limbSwing + MathHelper.sin(limbSwing * 2.7f)) * 0.6f * 12.0f;
        this.field_191215_c.rotationPointY = this.field_191214_b.rotationPointY = 24.0f - f1;
        this.field_191213_a.rotationPointY = this.field_191214_b.rotationPointY;
        this.field_191213_a.render(scale);
        this.field_191214_b.render(scale);
        this.field_191215_c.render(scale);
    }
}

