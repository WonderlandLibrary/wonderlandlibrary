/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.util.math.MathHelper;

public class ModelShulker
extends ModelBase {
    public final ModelRenderer base;
    public final ModelRenderer lid;
    public ModelRenderer head;

    public ModelShulker() {
        this.textureHeight = 64;
        this.textureWidth = 64;
        this.lid = new ModelRenderer(this);
        this.base = new ModelRenderer(this);
        this.head = new ModelRenderer(this);
        this.lid.setTextureOffset(0, 0).addBox(-8.0f, -16.0f, -8.0f, 16, 12, 16);
        this.lid.setRotationPoint(0.0f, 24.0f, 0.0f);
        this.base.setTextureOffset(0, 28).addBox(-8.0f, -8.0f, -8.0f, 16, 8, 16);
        this.base.setRotationPoint(0.0f, 24.0f, 0.0f);
        this.head.setTextureOffset(0, 52).addBox(-3.0f, 0.0f, -3.0f, 6, 6, 6);
        this.head.setRotationPoint(0.0f, 12.0f, 0.0f);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        EntityShulker entityshulker = (EntityShulker)entityIn;
        float f = ageInTicks - (float)entityshulker.ticksExisted;
        float f1 = (0.5f + entityshulker.getClientPeekAmount(f)) * (float)Math.PI;
        float f2 = -1.0f + MathHelper.sin(f1);
        float f3 = 0.0f;
        if (f1 > (float)Math.PI) {
            f3 = MathHelper.sin(ageInTicks * 0.1f) * 0.7f;
        }
        this.lid.setRotationPoint(0.0f, 16.0f + MathHelper.sin(f1) * 8.0f + f3, 0.0f);
        this.lid.rotateAngleY = entityshulker.getClientPeekAmount(f) > 0.3f ? f2 * f2 * f2 * f2 * (float)Math.PI * 0.125f : 0.0f;
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180);
        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.base.render(scale);
        this.lid.render(scale);
    }
}

