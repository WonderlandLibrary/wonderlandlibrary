package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.MathHelper;

public class ModelWolf
extends ModelBase {
    public ModelRenderer wolfHeadMain;
    public ModelRenderer wolfBody;
    public ModelRenderer wolfLeg1;
    public ModelRenderer wolfLeg2;
    public ModelRenderer wolfLeg3;
    public ModelRenderer wolfLeg4;
    ModelRenderer wolfTail;
    ModelRenderer wolfMane;

    public ModelWolf() {
        float f = 0.0f;
        float f1 = 13.5f;
        this.wolfHeadMain = new ModelRenderer((ModelBase)this, 0, 0);
        this.wolfHeadMain.addBox(-3.0f, -3.0f, -2.0f, 6, 6, 4, f);
        this.wolfHeadMain.setRotationPoint(-1.0f, f1, -7.0f);
        this.wolfBody = new ModelRenderer((ModelBase)this, 18, 14);
        this.wolfBody.addBox(-4.0f, -2.0f, -3.0f, 6, 9, 6, f);
        this.wolfBody.setRotationPoint(0.0f, 14.0f, 2.0f);
        this.wolfMane = new ModelRenderer((ModelBase)this, 21, 0);
        this.wolfMane.addBox(-4.0f, -3.0f, -3.0f, 8, 6, 7, f);
        this.wolfMane.setRotationPoint(-1.0f, 14.0f, 2.0f);
        this.wolfLeg1 = new ModelRenderer((ModelBase)this, 0, 18);
        this.wolfLeg1.addBox(-1.0f, 0.0f, -1.0f, 2, 8, 2, f);
        this.wolfLeg1.setRotationPoint(-2.5f, 16.0f, 7.0f);
        this.wolfLeg2 = new ModelRenderer((ModelBase)this, 0, 18);
        this.wolfLeg2.addBox(-1.0f, 0.0f, -1.0f, 2, 8, 2, f);
        this.wolfLeg2.setRotationPoint(0.5f, 16.0f, 7.0f);
        this.wolfLeg3 = new ModelRenderer((ModelBase)this, 0, 18);
        this.wolfLeg3.addBox(-1.0f, 0.0f, -1.0f, 2, 8, 2, f);
        this.wolfLeg3.setRotationPoint(-2.5f, 16.0f, -4.0f);
        this.wolfLeg4 = new ModelRenderer((ModelBase)this, 0, 18);
        this.wolfLeg4.addBox(-1.0f, 0.0f, -1.0f, 2, 8, 2, f);
        this.wolfLeg4.setRotationPoint(0.5f, 16.0f, -4.0f);
        this.wolfTail = new ModelRenderer((ModelBase)this, 9, 18);
        this.wolfTail.addBox(-1.0f, 0.0f, -1.0f, 2, 8, 2, f);
        this.wolfTail.setRotationPoint(-1.0f, 12.0f, 8.0f);
        this.wolfHeadMain.setTextureOffset(16, 14).addBox(-3.0f, -5.0f, 0.0f, 2, 2, 1, f);
        this.wolfHeadMain.setTextureOffset(16, 14).addBox(1.0f, -5.0f, 0.0f, 2, 2, 1, f);
        this.wolfHeadMain.setTextureOffset(0, 10).addBox(-1.5f, 0.0f, -5.0f, 3, 3, 4, f);
    }

    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        super.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        if (this.isChild) {
            float f = 2.0f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)0.0f, (float)(5.0f * scale), (float)(2.0f * scale));
            this.wolfHeadMain.renderWithRotation(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale((float)(1.0f / f), (float)(1.0f / f), (float)(1.0f / f));
            GlStateManager.translate((float)0.0f, (float)(24.0f * scale), (float)0.0f);
            this.wolfBody.render(scale);
            this.wolfLeg1.render(scale);
            this.wolfLeg2.render(scale);
            this.wolfLeg3.render(scale);
            this.wolfLeg4.render(scale);
            this.wolfTail.renderWithRotation(scale);
            this.wolfMane.render(scale);
            GlStateManager.popMatrix();
        } else {
            this.wolfHeadMain.renderWithRotation(scale);
            this.wolfBody.render(scale);
            this.wolfLeg1.render(scale);
            this.wolfLeg2.render(scale);
            this.wolfLeg3.render(scale);
            this.wolfLeg4.render(scale);
            this.wolfTail.renderWithRotation(scale);
            this.wolfMane.render(scale);
        }
    }

    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {
        EntityWolf entitywolf = (EntityWolf)entitylivingbaseIn;
        this.wolfTail.rotateAngleY = entitywolf.isAngry() ? 0.0f : MathHelper.cos((float)(p_78086_2_ * 0.6662f)) * 1.4f * p_78086_3_;
        if (entitywolf.isSitting()) {
            this.wolfMane.setRotationPoint(-1.0f, 16.0f, -3.0f);
            this.wolfMane.rotateAngleX = 1.2566371f;
            this.wolfMane.rotateAngleY = 0.0f;
            this.wolfBody.setRotationPoint(0.0f, 18.0f, 0.0f);
            this.wolfBody.rotateAngleX = 0.7853982f;
            this.wolfTail.setRotationPoint(-1.0f, 21.0f, 6.0f);
            this.wolfLeg1.setRotationPoint(-2.5f, 22.0f, 2.0f);
            this.wolfLeg1.rotateAngleX = 4.712389f;
            this.wolfLeg2.setRotationPoint(0.5f, 22.0f, 2.0f);
            this.wolfLeg2.rotateAngleX = 4.712389f;
            this.wolfLeg3.rotateAngleX = 5.811947f;
            this.wolfLeg3.setRotationPoint(-2.49f, 17.0f, -4.0f);
            this.wolfLeg4.rotateAngleX = 5.811947f;
            this.wolfLeg4.setRotationPoint(0.51f, 17.0f, -4.0f);
        } else {
            this.wolfBody.setRotationPoint(0.0f, 14.0f, 2.0f);
            this.wolfBody.rotateAngleX = 1.5707964f;
            this.wolfMane.setRotationPoint(-1.0f, 14.0f, -3.0f);
            this.wolfMane.rotateAngleX = this.wolfBody.rotateAngleX;
            this.wolfTail.setRotationPoint(-1.0f, 12.0f, 8.0f);
            this.wolfLeg1.setRotationPoint(-2.5f, 16.0f, 7.0f);
            this.wolfLeg2.setRotationPoint(0.5f, 16.0f, 7.0f);
            this.wolfLeg3.setRotationPoint(-2.5f, 16.0f, -4.0f);
            this.wolfLeg4.setRotationPoint(0.5f, 16.0f, -4.0f);
            this.wolfLeg1.rotateAngleX = MathHelper.cos((float)(p_78086_2_ * 0.6662f)) * 1.4f * p_78086_3_;
            this.wolfLeg2.rotateAngleX = MathHelper.cos((float)(p_78086_2_ * 0.6662f + (float)Math.PI)) * 1.4f * p_78086_3_;
            this.wolfLeg3.rotateAngleX = MathHelper.cos((float)(p_78086_2_ * 0.6662f + (float)Math.PI)) * 1.4f * p_78086_3_;
            this.wolfLeg4.rotateAngleX = MathHelper.cos((float)(p_78086_2_ * 0.6662f)) * 1.4f * p_78086_3_;
        }
        this.wolfHeadMain.rotateAngleZ = entitywolf.getInterestedAngle(partialTickTime) + entitywolf.getShakeAngle(partialTickTime, 0.0f);
        this.wolfMane.rotateAngleZ = entitywolf.getShakeAngle(partialTickTime, -0.08f);
        this.wolfBody.rotateAngleZ = entitywolf.getShakeAngle(partialTickTime, -0.16f);
        this.wolfTail.rotateAngleZ = entitywolf.getShakeAngle(partialTickTime, -0.2f);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.wolfHeadMain.rotateAngleX = headPitch / 57.295776f;
        this.wolfHeadMain.rotateAngleY = netHeadYaw / 57.295776f;
        this.wolfTail.rotateAngleX = ageInTicks;
    }
}
