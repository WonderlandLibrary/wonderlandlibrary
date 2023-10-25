/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSlime
extends ModelBase {
    ModelRenderer slimeRightEye;
    ModelRenderer slimeBodies;
    ModelRenderer slimeMouth;
    ModelRenderer slimeLeftEye;

    public ModelSlime(int n) {
        this.slimeBodies = new ModelRenderer(this, 0, n);
        this.slimeBodies.addBox(-4.0f, 16.0f, -4.0f, 8, 8, 8);
        if (n > 0) {
            this.slimeBodies = new ModelRenderer(this, 0, n);
            this.slimeBodies.addBox(-3.0f, 17.0f, -3.0f, 6, 6, 6);
            this.slimeRightEye = new ModelRenderer(this, 32, 0);
            this.slimeRightEye.addBox(-3.25f, 18.0f, -3.5f, 2, 2, 2);
            this.slimeLeftEye = new ModelRenderer(this, 32, 4);
            this.slimeLeftEye.addBox(1.25f, 18.0f, -3.5f, 2, 2, 2);
            this.slimeMouth = new ModelRenderer(this, 32, 8);
            this.slimeMouth.addBox(0.0f, 21.0f, -3.5f, 1, 1, 1);
        }
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        this.slimeBodies.render(f6);
        if (this.slimeRightEye != null) {
            this.slimeRightEye.render(f6);
            this.slimeLeftEye.render(f6);
            this.slimeMouth.render(f6);
        }
    }
}

