/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelCreeper
extends ModelBase {
    public ModelRenderer head;
    public ModelRenderer creeperArmor;
    public ModelRenderer body;
    public ModelRenderer leg1;
    public ModelRenderer leg2;
    public ModelRenderer leg3;
    public ModelRenderer leg4;
    private static final String __OBFID = "CL_00000837";

    public ModelCreeper() {
        this(0.0f);
    }

    public ModelCreeper(float p_i46366_1_) {
        int var2 = 6;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, p_i46366_1_);
        this.head.setRotationPoint(0.0f, var2, 0.0f);
        this.creeperArmor = new ModelRenderer(this, 32, 0);
        this.creeperArmor.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, p_i46366_1_ + 0.5f);
        this.creeperArmor.setRotationPoint(0.0f, var2, 0.0f);
        this.body = new ModelRenderer(this, 16, 16);
        this.body.addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, p_i46366_1_);
        this.body.setRotationPoint(0.0f, var2, 0.0f);
        this.leg1 = new ModelRenderer(this, 0, 16);
        this.leg1.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, p_i46366_1_);
        this.leg1.setRotationPoint(-2.0f, 12 + var2, 4.0f);
        this.leg2 = new ModelRenderer(this, 0, 16);
        this.leg2.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, p_i46366_1_);
        this.leg2.setRotationPoint(2.0f, 12 + var2, 4.0f);
        this.leg3 = new ModelRenderer(this, 0, 16);
        this.leg3.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, p_i46366_1_);
        this.leg3.setRotationPoint(-2.0f, 12 + var2, -4.0f);
        this.leg4 = new ModelRenderer(this, 0, 16);
        this.leg4.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, p_i46366_1_);
        this.leg4.setRotationPoint(2.0f, 12 + var2, -4.0f);
    }

    @Override
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.head.render(p_78088_7_);
        this.body.render(p_78088_7_);
        this.leg1.render(p_78088_7_);
        this.leg2.render(p_78088_7_);
        this.leg3.render(p_78088_7_);
        this.leg4.render(p_78088_7_);
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        this.head.rotateAngleY = p_78087_4_ / 57.295776f;
        this.head.rotateAngleX = p_78087_5_ / 57.295776f;
        this.leg1.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f) * 1.4f * p_78087_2_;
        this.leg2.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f + (float)Math.PI) * 1.4f * p_78087_2_;
        this.leg3.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f + (float)Math.PI) * 1.4f * p_78087_2_;
        this.leg4.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662f) * 1.4f * p_78087_2_;
    }
}

