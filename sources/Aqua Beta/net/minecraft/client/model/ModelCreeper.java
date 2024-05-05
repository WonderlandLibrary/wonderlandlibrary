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

    public ModelCreeper() {
        this(0.0f);
    }

    public ModelCreeper(float p_i46366_1_) {
        int i = 6;
        this.head = new ModelRenderer((ModelBase)this, 0, 0);
        this.head.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, p_i46366_1_);
        this.head.setRotationPoint(0.0f, (float)i, 0.0f);
        this.creeperArmor = new ModelRenderer((ModelBase)this, 32, 0);
        this.creeperArmor.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, p_i46366_1_ + 0.5f);
        this.creeperArmor.setRotationPoint(0.0f, (float)i, 0.0f);
        this.body = new ModelRenderer((ModelBase)this, 16, 16);
        this.body.addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, p_i46366_1_);
        this.body.setRotationPoint(0.0f, (float)i, 0.0f);
        this.leg1 = new ModelRenderer((ModelBase)this, 0, 16);
        this.leg1.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, p_i46366_1_);
        this.leg1.setRotationPoint(-2.0f, (float)(12 + i), 4.0f);
        this.leg2 = new ModelRenderer((ModelBase)this, 0, 16);
        this.leg2.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, p_i46366_1_);
        this.leg2.setRotationPoint(2.0f, (float)(12 + i), 4.0f);
        this.leg3 = new ModelRenderer((ModelBase)this, 0, 16);
        this.leg3.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, p_i46366_1_);
        this.leg3.setRotationPoint(-2.0f, (float)(12 + i), -4.0f);
        this.leg4 = new ModelRenderer((ModelBase)this, 0, 16);
        this.leg4.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, p_i46366_1_);
        this.leg4.setRotationPoint(2.0f, (float)(12 + i), -4.0f);
    }

    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        this.head.render(scale);
        this.body.render(scale);
        this.leg1.render(scale);
        this.leg2.render(scale);
        this.leg3.render(scale);
        this.leg4.render(scale);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        this.head.rotateAngleY = netHeadYaw / 57.295776f;
        this.head.rotateAngleX = headPitch / 57.295776f;
        this.leg1.rotateAngleX = MathHelper.cos((float)(limbSwing * 0.6662f)) * 1.4f * limbSwingAmount;
        this.leg2.rotateAngleX = MathHelper.cos((float)(limbSwing * 0.6662f + (float)Math.PI)) * 1.4f * limbSwingAmount;
        this.leg3.rotateAngleX = MathHelper.cos((float)(limbSwing * 0.6662f + (float)Math.PI)) * 1.4f * limbSwingAmount;
        this.leg4.rotateAngleX = MathHelper.cos((float)(limbSwing * 0.6662f)) * 1.4f * limbSwingAmount;
    }
}
