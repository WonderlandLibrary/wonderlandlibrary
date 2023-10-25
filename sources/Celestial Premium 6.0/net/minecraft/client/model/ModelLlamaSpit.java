/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLlamaSpit
extends ModelBase {
    private final ModelRenderer field_191225_a = new ModelRenderer(this);

    public ModelLlamaSpit() {
        this(0.0f);
    }

    public ModelLlamaSpit(float p_i47225_1_) {
        int i = 2;
        this.field_191225_a.setTextureOffset(0, 0).addBox(-4.0f, 0.0f, 0.0f, 2, 2, 2, p_i47225_1_);
        this.field_191225_a.setTextureOffset(0, 0).addBox(0.0f, -4.0f, 0.0f, 2, 2, 2, p_i47225_1_);
        this.field_191225_a.setTextureOffset(0, 0).addBox(0.0f, 0.0f, -4.0f, 2, 2, 2, p_i47225_1_);
        this.field_191225_a.setTextureOffset(0, 0).addBox(0.0f, 0.0f, 0.0f, 2, 2, 2, p_i47225_1_);
        this.field_191225_a.setTextureOffset(0, 0).addBox(2.0f, 0.0f, 0.0f, 2, 2, 2, p_i47225_1_);
        this.field_191225_a.setTextureOffset(0, 0).addBox(0.0f, 2.0f, 0.0f, 2, 2, 2, p_i47225_1_);
        this.field_191225_a.setTextureOffset(0, 0).addBox(0.0f, 0.0f, 2.0f, 2, 2, 2, p_i47225_1_);
        this.field_191225_a.setRotationPoint(0.0f, 0.0f, 0.0f);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.field_191225_a.render(scale);
    }
}

