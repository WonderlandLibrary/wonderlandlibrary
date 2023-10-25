/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.util.math.MathHelper;

public class ModelParrot
extends ModelBase {
    ModelRenderer field_192764_a;
    ModelRenderer field_192765_b;
    ModelRenderer field_192766_c;
    ModelRenderer field_192767_d;
    ModelRenderer field_192768_e;
    ModelRenderer field_192769_f;
    ModelRenderer field_192770_g;
    ModelRenderer field_192771_h;
    ModelRenderer field_192772_i;
    ModelRenderer field_192773_j;
    ModelRenderer field_192774_k;
    private State field_192775_l = State.STANDING;

    public ModelParrot() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.field_192764_a = new ModelRenderer(this, 2, 8);
        this.field_192764_a.addBox(-1.5f, 0.0f, -1.5f, 3, 6, 3);
        this.field_192764_a.setRotationPoint(0.0f, 16.5f, -3.0f);
        this.field_192765_b = new ModelRenderer(this, 22, 1);
        this.field_192765_b.addBox(-1.5f, -1.0f, -1.0f, 3, 4, 1);
        this.field_192765_b.setRotationPoint(0.0f, 21.07f, 1.16f);
        this.field_192766_c = new ModelRenderer(this, 19, 8);
        this.field_192766_c.addBox(-0.5f, 0.0f, -1.5f, 1, 5, 3);
        this.field_192766_c.setRotationPoint(1.5f, 16.94f, -2.76f);
        this.field_192767_d = new ModelRenderer(this, 19, 8);
        this.field_192767_d.addBox(-0.5f, 0.0f, -1.5f, 1, 5, 3);
        this.field_192767_d.setRotationPoint(-1.5f, 16.94f, -2.76f);
        this.field_192768_e = new ModelRenderer(this, 2, 2);
        this.field_192768_e.addBox(-1.0f, -1.5f, -1.0f, 2, 3, 2);
        this.field_192768_e.setRotationPoint(0.0f, 15.69f, -2.76f);
        this.field_192769_f = new ModelRenderer(this, 10, 0);
        this.field_192769_f.addBox(-1.0f, -0.5f, -2.0f, 2, 1, 4);
        this.field_192769_f.setRotationPoint(0.0f, -2.0f, -1.0f);
        this.field_192768_e.addChild(this.field_192769_f);
        this.field_192770_g = new ModelRenderer(this, 11, 7);
        this.field_192770_g.addBox(-0.5f, -1.0f, -0.5f, 1, 2, 1);
        this.field_192770_g.setRotationPoint(0.0f, -0.5f, -1.5f);
        this.field_192768_e.addChild(this.field_192770_g);
        this.field_192771_h = new ModelRenderer(this, 16, 7);
        this.field_192771_h.addBox(-0.5f, 0.0f, -0.5f, 1, 2, 1);
        this.field_192771_h.setRotationPoint(0.0f, -1.75f, -2.45f);
        this.field_192768_e.addChild(this.field_192771_h);
        this.field_192772_i = new ModelRenderer(this, 2, 18);
        this.field_192772_i.addBox(0.0f, -4.0f, -2.0f, 0, 5, 4);
        this.field_192772_i.setRotationPoint(0.0f, -2.15f, 0.15f);
        this.field_192768_e.addChild(this.field_192772_i);
        this.field_192773_j = new ModelRenderer(this, 14, 18);
        this.field_192773_j.addBox(-0.5f, 0.0f, -0.5f, 1, 2, 1);
        this.field_192773_j.setRotationPoint(1.0f, 22.0f, -1.05f);
        this.field_192774_k = new ModelRenderer(this, 14, 18);
        this.field_192774_k.addBox(-0.5f, 0.0f, -0.5f, 1, 2, 1);
        this.field_192774_k.setRotationPoint(-1.0f, 22.0f, -1.05f);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.field_192764_a.render(scale);
        this.field_192766_c.render(scale);
        this.field_192767_d.render(scale);
        this.field_192765_b.render(scale);
        this.field_192768_e.render(scale);
        this.field_192773_j.render(scale);
        this.field_192774_k.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        float f = ageInTicks * 0.3f;
        this.field_192768_e.rotateAngleX = headPitch * ((float)Math.PI / 180);
        this.field_192768_e.rotateAngleY = netHeadYaw * ((float)Math.PI / 180);
        this.field_192768_e.rotateAngleZ = 0.0f;
        this.field_192768_e.rotationPointX = 0.0f;
        this.field_192764_a.rotationPointX = 0.0f;
        this.field_192765_b.rotationPointX = 0.0f;
        this.field_192767_d.rotationPointX = -1.5f;
        this.field_192766_c.rotationPointX = 1.5f;
        if (this.field_192775_l != State.FLYING) {
            if (this.field_192775_l == State.SITTING) {
                return;
            }
            if (this.field_192775_l == State.PARTY) {
                float f1 = MathHelper.cos(entityIn.ticksExisted);
                float f2 = MathHelper.sin(entityIn.ticksExisted);
                this.field_192768_e.rotationPointX = f1;
                this.field_192768_e.rotationPointY = 15.69f + f2;
                this.field_192768_e.rotateAngleX = 0.0f;
                this.field_192768_e.rotateAngleY = 0.0f;
                this.field_192768_e.rotateAngleZ = MathHelper.sin(entityIn.ticksExisted) * 0.4f;
                this.field_192764_a.rotationPointX = f1;
                this.field_192764_a.rotationPointY = 16.5f + f2;
                this.field_192766_c.rotateAngleZ = -0.0873f - ageInTicks;
                this.field_192766_c.rotationPointX = 1.5f + f1;
                this.field_192766_c.rotationPointY = 16.94f + f2;
                this.field_192767_d.rotateAngleZ = 0.0873f + ageInTicks;
                this.field_192767_d.rotationPointX = -1.5f + f1;
                this.field_192767_d.rotationPointY = 16.94f + f2;
                this.field_192765_b.rotationPointX = f1;
                this.field_192765_b.rotationPointY = 21.07f + f2;
                return;
            }
            this.field_192773_j.rotateAngleX += MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
            this.field_192774_k.rotateAngleX += MathHelper.cos(limbSwing * 0.6662f + (float)Math.PI) * 1.4f * limbSwingAmount;
        }
        this.field_192768_e.rotationPointY = 15.69f + f;
        this.field_192765_b.rotateAngleX = 1.015f + MathHelper.cos(limbSwing * 0.6662f) * 0.3f * limbSwingAmount;
        this.field_192765_b.rotationPointY = 21.07f + f;
        this.field_192764_a.rotationPointY = 16.5f + f;
        this.field_192766_c.rotateAngleZ = -0.0873f - ageInTicks;
        this.field_192766_c.rotationPointY = 16.94f + f;
        this.field_192767_d.rotateAngleZ = 0.0873f + ageInTicks;
        this.field_192767_d.rotationPointY = 16.94f + f;
        this.field_192773_j.rotationPointY = 22.0f + f;
        this.field_192774_k.rotationPointY = 22.0f + f;
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {
        this.field_192772_i.rotateAngleX = -0.2214f;
        this.field_192764_a.rotateAngleX = 0.4937f;
        this.field_192766_c.rotateAngleX = -0.69813174f;
        this.field_192766_c.rotateAngleY = (float)(-Math.PI);
        this.field_192767_d.rotateAngleX = -0.69813174f;
        this.field_192767_d.rotateAngleY = (float)(-Math.PI);
        this.field_192773_j.rotateAngleX = -0.0299f;
        this.field_192774_k.rotateAngleX = -0.0299f;
        this.field_192773_j.rotationPointY = 22.0f;
        this.field_192774_k.rotationPointY = 22.0f;
        if (entitylivingbaseIn instanceof EntityParrot) {
            EntityParrot entityparrot = (EntityParrot)entitylivingbaseIn;
            if (entityparrot.func_192004_dr()) {
                this.field_192773_j.rotateAngleZ = -0.34906584f;
                this.field_192774_k.rotateAngleZ = 0.34906584f;
                this.field_192775_l = State.PARTY;
                return;
            }
            if (entityparrot.isSitting()) {
                float f = 1.9f;
                this.field_192768_e.rotationPointY = 17.59f;
                this.field_192765_b.rotateAngleX = 1.5388988f;
                this.field_192765_b.rotationPointY = 22.97f;
                this.field_192764_a.rotationPointY = 18.4f;
                this.field_192766_c.rotateAngleZ = -0.0873f;
                this.field_192766_c.rotationPointY = 18.84f;
                this.field_192767_d.rotateAngleZ = 0.0873f;
                this.field_192767_d.rotationPointY = 18.84f;
                this.field_192773_j.rotationPointY += 1.0f;
                this.field_192774_k.rotationPointY += 1.0f;
                this.field_192773_j.rotateAngleX += 1.0f;
                this.field_192774_k.rotateAngleX += 1.0f;
                this.field_192775_l = State.SITTING;
            } else if (entityparrot.func_192002_a()) {
                this.field_192773_j.rotateAngleX += 0.69813174f;
                this.field_192774_k.rotateAngleX += 0.69813174f;
                this.field_192775_l = State.FLYING;
            } else {
                this.field_192775_l = State.STANDING;
            }
            this.field_192773_j.rotateAngleZ = 0.0f;
            this.field_192774_k.rotateAngleZ = 0.0f;
        }
    }

    static enum State {
        FLYING,
        STANDING,
        SITTING,
        PARTY;

    }
}

