// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.Entity;

public class ModelHorse extends ModelBase
{
    private final ModelRenderer head;
    private final ModelRenderer upperMouth;
    private final ModelRenderer lowerMouth;
    private final ModelRenderer horseLeftEar;
    private final ModelRenderer horseRightEar;
    private final ModelRenderer muleLeftEar;
    private final ModelRenderer muleRightEar;
    private final ModelRenderer neck;
    private final ModelRenderer horseFaceRopes;
    private final ModelRenderer mane;
    private final ModelRenderer body;
    private final ModelRenderer tailBase;
    private final ModelRenderer tailMiddle;
    private final ModelRenderer tailTip;
    private final ModelRenderer backLeftLeg;
    private final ModelRenderer backLeftShin;
    private final ModelRenderer backLeftHoof;
    private final ModelRenderer backRightLeg;
    private final ModelRenderer backRightShin;
    private final ModelRenderer backRightHoof;
    private final ModelRenderer frontLeftLeg;
    private final ModelRenderer frontLeftShin;
    private final ModelRenderer frontLeftHoof;
    private final ModelRenderer frontRightLeg;
    private final ModelRenderer frontRightShin;
    private final ModelRenderer frontRightHoof;
    private final ModelRenderer muleLeftChest;
    private final ModelRenderer muleRightChest;
    private final ModelRenderer horseSaddleBottom;
    private final ModelRenderer horseSaddleFront;
    private final ModelRenderer horseSaddleBack;
    private final ModelRenderer horseLeftSaddleRope;
    private final ModelRenderer horseLeftSaddleMetal;
    private final ModelRenderer horseRightSaddleRope;
    private final ModelRenderer horseRightSaddleMetal;
    private final ModelRenderer horseLeftFaceMetal;
    private final ModelRenderer horseRightFaceMetal;
    private final ModelRenderer horseLeftRein;
    private final ModelRenderer horseRightRein;
    
    public ModelHorse() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        (this.body = new ModelRenderer(this, 0, 34)).addBox(-5.0f, -8.0f, -19.0f, 10, 10, 24);
        this.body.setRotationPoint(0.0f, 11.0f, 9.0f);
        (this.tailBase = new ModelRenderer(this, 44, 0)).addBox(-1.0f, -1.0f, 0.0f, 2, 2, 3);
        this.tailBase.setRotationPoint(0.0f, 3.0f, 14.0f);
        this.tailBase.rotateAngleX = -1.134464f;
        (this.tailMiddle = new ModelRenderer(this, 38, 7)).addBox(-1.5f, -2.0f, 3.0f, 3, 4, 7);
        this.tailMiddle.setRotationPoint(0.0f, 3.0f, 14.0f);
        this.tailMiddle.rotateAngleX = -1.134464f;
        (this.tailTip = new ModelRenderer(this, 24, 3)).addBox(-1.5f, -4.5f, 9.0f, 3, 4, 7);
        this.tailTip.setRotationPoint(0.0f, 3.0f, 14.0f);
        this.tailTip.rotateAngleX = -1.3962634f;
        (this.backLeftLeg = new ModelRenderer(this, 78, 29)).addBox(-2.5f, -2.0f, -2.5f, 4, 9, 5);
        this.backLeftLeg.setRotationPoint(4.0f, 9.0f, 11.0f);
        (this.backLeftShin = new ModelRenderer(this, 78, 43)).addBox(-2.0f, 0.0f, -1.5f, 3, 5, 3);
        this.backLeftShin.setRotationPoint(4.0f, 16.0f, 11.0f);
        (this.backLeftHoof = new ModelRenderer(this, 78, 51)).addBox(-2.5f, 5.1f, -2.0f, 4, 3, 4);
        this.backLeftHoof.setRotationPoint(4.0f, 16.0f, 11.0f);
        (this.backRightLeg = new ModelRenderer(this, 96, 29)).addBox(-1.5f, -2.0f, -2.5f, 4, 9, 5);
        this.backRightLeg.setRotationPoint(-4.0f, 9.0f, 11.0f);
        (this.backRightShin = new ModelRenderer(this, 96, 43)).addBox(-1.0f, 0.0f, -1.5f, 3, 5, 3);
        this.backRightShin.setRotationPoint(-4.0f, 16.0f, 11.0f);
        (this.backRightHoof = new ModelRenderer(this, 96, 51)).addBox(-1.5f, 5.1f, -2.0f, 4, 3, 4);
        this.backRightHoof.setRotationPoint(-4.0f, 16.0f, 11.0f);
        (this.frontLeftLeg = new ModelRenderer(this, 44, 29)).addBox(-1.9f, -1.0f, -2.1f, 3, 8, 4);
        this.frontLeftLeg.setRotationPoint(4.0f, 9.0f, -8.0f);
        (this.frontLeftShin = new ModelRenderer(this, 44, 41)).addBox(-1.9f, 0.0f, -1.6f, 3, 5, 3);
        this.frontLeftShin.setRotationPoint(4.0f, 16.0f, -8.0f);
        (this.frontLeftHoof = new ModelRenderer(this, 44, 51)).addBox(-2.4f, 5.1f, -2.1f, 4, 3, 4);
        this.frontLeftHoof.setRotationPoint(4.0f, 16.0f, -8.0f);
        (this.frontRightLeg = new ModelRenderer(this, 60, 29)).addBox(-1.1f, -1.0f, -2.1f, 3, 8, 4);
        this.frontRightLeg.setRotationPoint(-4.0f, 9.0f, -8.0f);
        (this.frontRightShin = new ModelRenderer(this, 60, 41)).addBox(-1.1f, 0.0f, -1.6f, 3, 5, 3);
        this.frontRightShin.setRotationPoint(-4.0f, 16.0f, -8.0f);
        (this.frontRightHoof = new ModelRenderer(this, 60, 51)).addBox(-1.6f, 5.1f, -2.1f, 4, 3, 4);
        this.frontRightHoof.setRotationPoint(-4.0f, 16.0f, -8.0f);
        (this.head = new ModelRenderer(this, 0, 0)).addBox(-2.5f, -10.0f, -1.5f, 5, 5, 7);
        this.head.setRotationPoint(0.0f, 4.0f, -10.0f);
        this.head.rotateAngleX = 0.5235988f;
        (this.upperMouth = new ModelRenderer(this, 24, 18)).addBox(-2.0f, -10.0f, -7.0f, 4, 3, 6);
        this.upperMouth.setRotationPoint(0.0f, 3.95f, -10.0f);
        this.upperMouth.rotateAngleX = 0.5235988f;
        (this.lowerMouth = new ModelRenderer(this, 24, 27)).addBox(-2.0f, -7.0f, -6.5f, 4, 2, 5);
        this.lowerMouth.setRotationPoint(0.0f, 4.0f, -10.0f);
        this.lowerMouth.rotateAngleX = 0.5235988f;
        this.head.addChild(this.upperMouth);
        this.head.addChild(this.lowerMouth);
        (this.horseLeftEar = new ModelRenderer(this, 0, 0)).addBox(0.45f, -12.0f, 4.0f, 2, 3, 1);
        this.horseLeftEar.setRotationPoint(0.0f, 4.0f, -10.0f);
        this.horseLeftEar.rotateAngleX = 0.5235988f;
        (this.horseRightEar = new ModelRenderer(this, 0, 0)).addBox(-2.45f, -12.0f, 4.0f, 2, 3, 1);
        this.horseRightEar.setRotationPoint(0.0f, 4.0f, -10.0f);
        this.horseRightEar.rotateAngleX = 0.5235988f;
        (this.muleLeftEar = new ModelRenderer(this, 0, 12)).addBox(-2.0f, -16.0f, 4.0f, 2, 7, 1);
        this.muleLeftEar.setRotationPoint(0.0f, 4.0f, -10.0f);
        this.muleLeftEar.rotateAngleX = 0.5235988f;
        this.muleLeftEar.rotateAngleZ = 0.2617994f;
        (this.muleRightEar = new ModelRenderer(this, 0, 12)).addBox(0.0f, -16.0f, 4.0f, 2, 7, 1);
        this.muleRightEar.setRotationPoint(0.0f, 4.0f, -10.0f);
        this.muleRightEar.rotateAngleX = 0.5235988f;
        this.muleRightEar.rotateAngleZ = -0.2617994f;
        (this.neck = new ModelRenderer(this, 0, 12)).addBox(-2.05f, -9.8f, -2.0f, 4, 14, 8);
        this.neck.setRotationPoint(0.0f, 4.0f, -10.0f);
        this.neck.rotateAngleX = 0.5235988f;
        (this.muleLeftChest = new ModelRenderer(this, 0, 34)).addBox(-3.0f, 0.0f, 0.0f, 8, 8, 3);
        this.muleLeftChest.setRotationPoint(-7.5f, 3.0f, 10.0f);
        this.muleLeftChest.rotateAngleY = 1.5707964f;
        (this.muleRightChest = new ModelRenderer(this, 0, 47)).addBox(-3.0f, 0.0f, 0.0f, 8, 8, 3);
        this.muleRightChest.setRotationPoint(4.5f, 3.0f, 10.0f);
        this.muleRightChest.rotateAngleY = 1.5707964f;
        (this.horseSaddleBottom = new ModelRenderer(this, 80, 0)).addBox(-5.0f, 0.0f, -3.0f, 10, 1, 8);
        this.horseSaddleBottom.setRotationPoint(0.0f, 2.0f, 2.0f);
        (this.horseSaddleFront = new ModelRenderer(this, 106, 9)).addBox(-1.5f, -1.0f, -3.0f, 3, 1, 2);
        this.horseSaddleFront.setRotationPoint(0.0f, 2.0f, 2.0f);
        (this.horseSaddleBack = new ModelRenderer(this, 80, 9)).addBox(-4.0f, -1.0f, 3.0f, 8, 1, 2);
        this.horseSaddleBack.setRotationPoint(0.0f, 2.0f, 2.0f);
        (this.horseLeftSaddleMetal = new ModelRenderer(this, 74, 0)).addBox(-0.5f, 6.0f, -1.0f, 1, 2, 2);
        this.horseLeftSaddleMetal.setRotationPoint(5.0f, 3.0f, 2.0f);
        (this.horseLeftSaddleRope = new ModelRenderer(this, 70, 0)).addBox(-0.5f, 0.0f, -0.5f, 1, 6, 1);
        this.horseLeftSaddleRope.setRotationPoint(5.0f, 3.0f, 2.0f);
        (this.horseRightSaddleMetal = new ModelRenderer(this, 74, 4)).addBox(-0.5f, 6.0f, -1.0f, 1, 2, 2);
        this.horseRightSaddleMetal.setRotationPoint(-5.0f, 3.0f, 2.0f);
        (this.horseRightSaddleRope = new ModelRenderer(this, 80, 0)).addBox(-0.5f, 0.0f, -0.5f, 1, 6, 1);
        this.horseRightSaddleRope.setRotationPoint(-5.0f, 3.0f, 2.0f);
        (this.horseLeftFaceMetal = new ModelRenderer(this, 74, 13)).addBox(1.5f, -8.0f, -4.0f, 1, 2, 2);
        this.horseLeftFaceMetal.setRotationPoint(0.0f, 4.0f, -10.0f);
        this.horseLeftFaceMetal.rotateAngleX = 0.5235988f;
        (this.horseRightFaceMetal = new ModelRenderer(this, 74, 13)).addBox(-2.5f, -8.0f, -4.0f, 1, 2, 2);
        this.horseRightFaceMetal.setRotationPoint(0.0f, 4.0f, -10.0f);
        this.horseRightFaceMetal.rotateAngleX = 0.5235988f;
        (this.horseLeftRein = new ModelRenderer(this, 44, 10)).addBox(2.6f, -6.0f, -6.0f, 0, 3, 16);
        this.horseLeftRein.setRotationPoint(0.0f, 4.0f, -10.0f);
        (this.horseRightRein = new ModelRenderer(this, 44, 5)).addBox(-2.6f, -6.0f, -6.0f, 0, 3, 16);
        this.horseRightRein.setRotationPoint(0.0f, 4.0f, -10.0f);
        (this.mane = new ModelRenderer(this, 58, 0)).addBox(-1.0f, -11.5f, 5.0f, 2, 16, 4);
        this.mane.setRotationPoint(0.0f, 4.0f, -10.0f);
        this.mane.rotateAngleX = 0.5235988f;
        (this.horseFaceRopes = new ModelRenderer(this, 80, 12)).addBox(-2.5f, -10.1f, -7.0f, 5, 5, 12, 0.2f);
        this.horseFaceRopes.setRotationPoint(0.0f, 4.0f, -10.0f);
        this.horseFaceRopes.rotateAngleX = 0.5235988f;
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        final AbstractHorse abstracthorse = (AbstractHorse)entityIn;
        final float f = abstracthorse.getGrassEatingAmount(0.0f);
        final boolean flag = abstracthorse.isChild();
        final boolean flag2 = !flag && abstracthorse.isHorseSaddled();
        final boolean flag3 = abstracthorse instanceof AbstractChestHorse;
        final boolean flag4 = !flag && flag3 && ((AbstractChestHorse)abstracthorse).hasChest();
        final float f2 = abstracthorse.getHorseSize();
        final boolean flag5 = abstracthorse.isBeingRidden();
        if (flag2) {
            this.horseFaceRopes.render(scale);
            this.horseSaddleBottom.render(scale);
            this.horseSaddleFront.render(scale);
            this.horseSaddleBack.render(scale);
            this.horseLeftSaddleRope.render(scale);
            this.horseLeftSaddleMetal.render(scale);
            this.horseRightSaddleRope.render(scale);
            this.horseRightSaddleMetal.render(scale);
            this.horseLeftFaceMetal.render(scale);
            this.horseRightFaceMetal.render(scale);
            if (flag5) {
                this.horseLeftRein.render(scale);
                this.horseRightRein.render(scale);
            }
        }
        if (flag) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(f2, 0.5f + f2 * 0.5f, f2);
            GlStateManager.translate(0.0f, 0.95f * (1.0f - f2), 0.0f);
        }
        this.backLeftLeg.render(scale);
        this.backLeftShin.render(scale);
        this.backLeftHoof.render(scale);
        this.backRightLeg.render(scale);
        this.backRightShin.render(scale);
        this.backRightHoof.render(scale);
        this.frontLeftLeg.render(scale);
        this.frontLeftShin.render(scale);
        this.frontLeftHoof.render(scale);
        this.frontRightLeg.render(scale);
        this.frontRightShin.render(scale);
        this.frontRightHoof.render(scale);
        if (flag) {
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(f2, f2, f2);
            GlStateManager.translate(0.0f, 1.35f * (1.0f - f2), 0.0f);
        }
        this.body.render(scale);
        this.tailBase.render(scale);
        this.tailMiddle.render(scale);
        this.tailTip.render(scale);
        this.neck.render(scale);
        this.mane.render(scale);
        if (flag) {
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            final float f3 = 0.5f + f2 * f2 * 0.5f;
            GlStateManager.scale(f3, f3, f3);
            if (f <= 0.0f) {
                GlStateManager.translate(0.0f, 1.35f * (1.0f - f2), 0.0f);
            }
            else {
                GlStateManager.translate(0.0f, 0.9f * (1.0f - f2) * f + 1.35f * (1.0f - f2) * (1.0f - f), 0.15f * (1.0f - f2) * f);
            }
        }
        if (flag3) {
            this.muleLeftEar.render(scale);
            this.muleRightEar.render(scale);
        }
        else {
            this.horseLeftEar.render(scale);
            this.horseRightEar.render(scale);
        }
        this.head.render(scale);
        if (flag) {
            GlStateManager.popMatrix();
        }
        if (flag4) {
            this.muleLeftChest.render(scale);
            this.muleRightChest.render(scale);
        }
    }
    
    private float updateHorseRotation(final float p_110683_1_, final float p_110683_2_, final float p_110683_3_) {
        float f;
        for (f = p_110683_2_ - p_110683_1_; f < -180.0f; f += 360.0f) {}
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return p_110683_1_ + p_110683_3_ * f;
    }
    
    @Override
    public void setLivingAnimations(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTickTime) {
        super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
        final float f = this.updateHorseRotation(entitylivingbaseIn.prevRenderYawOffset, entitylivingbaseIn.renderYawOffset, partialTickTime);
        final float f2 = this.updateHorseRotation(entitylivingbaseIn.prevRotationYawHead, entitylivingbaseIn.rotationYawHead, partialTickTime);
        final float f3 = entitylivingbaseIn.prevRotationPitch + (entitylivingbaseIn.rotationPitch - entitylivingbaseIn.prevRotationPitch) * partialTickTime;
        float f4 = f2 - f;
        float f5 = f3 * 0.017453292f;
        if (f4 > 20.0f) {
            f4 = 20.0f;
        }
        if (f4 < -20.0f) {
            f4 = -20.0f;
        }
        if (limbSwingAmount > 0.2f) {
            f5 += MathHelper.cos(limbSwing * 0.4f) * 0.15f * limbSwingAmount;
        }
        final AbstractHorse abstracthorse = (AbstractHorse)entitylivingbaseIn;
        final float f6 = abstracthorse.getGrassEatingAmount(partialTickTime);
        final float f7 = abstracthorse.getRearingAmount(partialTickTime);
        final float f8 = 1.0f - f7;
        final float f9 = abstracthorse.getMouthOpennessAngle(partialTickTime);
        final boolean flag = abstracthorse.tailCounter != 0;
        final boolean flag2 = abstracthorse.isHorseSaddled();
        final boolean flag3 = abstracthorse.isBeingRidden();
        final float f10 = entitylivingbaseIn.ticksExisted + partialTickTime;
        final float f11 = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f);
        final float f12 = f11 * 0.8f * limbSwingAmount;
        this.head.rotationPointY = 4.0f;
        this.head.rotationPointZ = -10.0f;
        this.tailBase.rotationPointY = 3.0f;
        this.tailMiddle.rotationPointZ = 14.0f;
        this.muleRightChest.rotationPointY = 3.0f;
        this.muleRightChest.rotationPointZ = 10.0f;
        this.body.rotateAngleX = 0.0f;
        this.head.rotateAngleX = 0.5235988f + f5;
        this.head.rotateAngleY = f4 * 0.017453292f;
        this.head.rotateAngleX = f7 * (0.2617994f + f5) + f6 * 2.1816616f + (1.0f - Math.max(f7, f6)) * this.head.rotateAngleX;
        this.head.rotateAngleY = f7 * f4 * 0.017453292f + (1.0f - Math.max(f7, f6)) * this.head.rotateAngleY;
        this.head.rotationPointY = f7 * -6.0f + f6 * 11.0f + (1.0f - Math.max(f7, f6)) * this.head.rotationPointY;
        this.head.rotationPointZ = f7 * -1.0f + f6 * -10.0f + (1.0f - Math.max(f7, f6)) * this.head.rotationPointZ;
        this.tailBase.rotationPointY = f7 * 9.0f + f8 * this.tailBase.rotationPointY;
        this.tailMiddle.rotationPointZ = f7 * 18.0f + f8 * this.tailMiddle.rotationPointZ;
        this.muleRightChest.rotationPointY = f7 * 5.5f + f8 * this.muleRightChest.rotationPointY;
        this.muleRightChest.rotationPointZ = f7 * 15.0f + f8 * this.muleRightChest.rotationPointZ;
        this.body.rotateAngleX = f7 * -0.7853982f + f8 * this.body.rotateAngleX;
        this.horseLeftEar.rotationPointY = this.head.rotationPointY;
        this.horseRightEar.rotationPointY = this.head.rotationPointY;
        this.muleLeftEar.rotationPointY = this.head.rotationPointY;
        this.muleRightEar.rotationPointY = this.head.rotationPointY;
        this.neck.rotationPointY = this.head.rotationPointY;
        this.upperMouth.rotationPointY = 0.02f;
        this.lowerMouth.rotationPointY = 0.0f;
        this.mane.rotationPointY = this.head.rotationPointY;
        this.horseLeftEar.rotationPointZ = this.head.rotationPointZ;
        this.horseRightEar.rotationPointZ = this.head.rotationPointZ;
        this.muleLeftEar.rotationPointZ = this.head.rotationPointZ;
        this.muleRightEar.rotationPointZ = this.head.rotationPointZ;
        this.neck.rotationPointZ = this.head.rotationPointZ;
        this.upperMouth.rotationPointZ = 0.02f - f9;
        this.lowerMouth.rotationPointZ = f9;
        this.mane.rotationPointZ = this.head.rotationPointZ;
        this.horseLeftEar.rotateAngleX = this.head.rotateAngleX;
        this.horseRightEar.rotateAngleX = this.head.rotateAngleX;
        this.muleLeftEar.rotateAngleX = this.head.rotateAngleX;
        this.muleRightEar.rotateAngleX = this.head.rotateAngleX;
        this.neck.rotateAngleX = this.head.rotateAngleX;
        this.upperMouth.rotateAngleX = -0.09424778f * f9;
        this.lowerMouth.rotateAngleX = 0.15707964f * f9;
        this.mane.rotateAngleX = this.head.rotateAngleX;
        this.horseLeftEar.rotateAngleY = this.head.rotateAngleY;
        this.horseRightEar.rotateAngleY = this.head.rotateAngleY;
        this.muleLeftEar.rotateAngleY = this.head.rotateAngleY;
        this.muleRightEar.rotateAngleY = this.head.rotateAngleY;
        this.neck.rotateAngleY = this.head.rotateAngleY;
        this.upperMouth.rotateAngleY = 0.0f;
        this.lowerMouth.rotateAngleY = 0.0f;
        this.mane.rotateAngleY = this.head.rotateAngleY;
        this.muleLeftChest.rotateAngleX = f12 / 5.0f;
        this.muleRightChest.rotateAngleX = -f12 / 5.0f;
        float f13 = 0.2617994f * f7;
        final float f14 = MathHelper.cos(f10 * 0.6f + 3.1415927f);
        this.frontLeftLeg.rotationPointY = -2.0f * f7 + 9.0f * f8;
        this.frontLeftLeg.rotationPointZ = -2.0f * f7 + -8.0f * f8;
        this.frontRightLeg.rotationPointY = this.frontLeftLeg.rotationPointY;
        this.frontRightLeg.rotationPointZ = this.frontLeftLeg.rotationPointZ;
        this.backLeftShin.rotationPointY = this.backLeftLeg.rotationPointY + MathHelper.sin(1.5707964f + f13 + f8 * -f11 * 0.5f * limbSwingAmount) * 7.0f;
        this.backLeftShin.rotationPointZ = this.backLeftLeg.rotationPointZ + MathHelper.cos(-1.5707964f + f13 + f8 * -f11 * 0.5f * limbSwingAmount) * 7.0f;
        this.backRightShin.rotationPointY = this.backRightLeg.rotationPointY + MathHelper.sin(1.5707964f + f13 + f8 * f11 * 0.5f * limbSwingAmount) * 7.0f;
        this.backRightShin.rotationPointZ = this.backRightLeg.rotationPointZ + MathHelper.cos(-1.5707964f + f13 + f8 * f11 * 0.5f * limbSwingAmount) * 7.0f;
        final float f15 = (-1.0471976f + f14) * f7 + f12 * f8;
        final float f16 = (-1.0471976f - f14) * f7 + -f12 * f8;
        this.frontLeftShin.rotationPointY = this.frontLeftLeg.rotationPointY + MathHelper.sin(1.5707964f + f15) * 7.0f;
        this.frontLeftShin.rotationPointZ = this.frontLeftLeg.rotationPointZ + MathHelper.cos(-1.5707964f + f15) * 7.0f;
        this.frontRightShin.rotationPointY = this.frontRightLeg.rotationPointY + MathHelper.sin(1.5707964f + f16) * 7.0f;
        this.frontRightShin.rotationPointZ = this.frontRightLeg.rotationPointZ + MathHelper.cos(-1.5707964f + f16) * 7.0f;
        this.backLeftLeg.rotateAngleX = f13 + -f11 * 0.5f * limbSwingAmount * f8;
        this.backLeftShin.rotateAngleX = -0.08726646f * f7 + (-f11 * 0.5f * limbSwingAmount - Math.max(0.0f, f11 * 0.5f * limbSwingAmount)) * f8;
        this.backLeftHoof.rotateAngleX = this.backLeftShin.rotateAngleX;
        this.backRightLeg.rotateAngleX = f13 + f11 * 0.5f * limbSwingAmount * f8;
        this.backRightShin.rotateAngleX = -0.08726646f * f7 + (f11 * 0.5f * limbSwingAmount - Math.max(0.0f, -f11 * 0.5f * limbSwingAmount)) * f8;
        this.backRightHoof.rotateAngleX = this.backRightShin.rotateAngleX;
        this.frontLeftLeg.rotateAngleX = f15;
        this.frontLeftShin.rotateAngleX = (this.frontLeftLeg.rotateAngleX + 3.1415927f * Math.max(0.0f, 0.2f + f14 * 0.2f)) * f7 + (f12 + Math.max(0.0f, f11 * 0.5f * limbSwingAmount)) * f8;
        this.frontLeftHoof.rotateAngleX = this.frontLeftShin.rotateAngleX;
        this.frontRightLeg.rotateAngleX = f16;
        this.frontRightShin.rotateAngleX = (this.frontRightLeg.rotateAngleX + 3.1415927f * Math.max(0.0f, 0.2f - f14 * 0.2f)) * f7 + (-f12 + Math.max(0.0f, -f11 * 0.5f * limbSwingAmount)) * f8;
        this.frontRightHoof.rotateAngleX = this.frontRightShin.rotateAngleX;
        this.backLeftHoof.rotationPointY = this.backLeftShin.rotationPointY;
        this.backLeftHoof.rotationPointZ = this.backLeftShin.rotationPointZ;
        this.backRightHoof.rotationPointY = this.backRightShin.rotationPointY;
        this.backRightHoof.rotationPointZ = this.backRightShin.rotationPointZ;
        this.frontLeftHoof.rotationPointY = this.frontLeftShin.rotationPointY;
        this.frontLeftHoof.rotationPointZ = this.frontLeftShin.rotationPointZ;
        this.frontRightHoof.rotationPointY = this.frontRightShin.rotationPointY;
        this.frontRightHoof.rotationPointZ = this.frontRightShin.rotationPointZ;
        if (flag2) {
            this.horseSaddleBottom.rotationPointY = f7 * 0.5f + f8 * 2.0f;
            this.horseSaddleBottom.rotationPointZ = f7 * 11.0f + f8 * 2.0f;
            this.horseSaddleFront.rotationPointY = this.horseSaddleBottom.rotationPointY;
            this.horseSaddleBack.rotationPointY = this.horseSaddleBottom.rotationPointY;
            this.horseLeftSaddleRope.rotationPointY = this.horseSaddleBottom.rotationPointY;
            this.horseRightSaddleRope.rotationPointY = this.horseSaddleBottom.rotationPointY;
            this.horseLeftSaddleMetal.rotationPointY = this.horseSaddleBottom.rotationPointY;
            this.horseRightSaddleMetal.rotationPointY = this.horseSaddleBottom.rotationPointY;
            this.muleLeftChest.rotationPointY = this.muleRightChest.rotationPointY;
            this.horseSaddleFront.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
            this.horseSaddleBack.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
            this.horseLeftSaddleRope.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
            this.horseRightSaddleRope.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
            this.horseLeftSaddleMetal.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
            this.horseRightSaddleMetal.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
            this.muleLeftChest.rotationPointZ = this.muleRightChest.rotationPointZ;
            this.horseSaddleBottom.rotateAngleX = this.body.rotateAngleX;
            this.horseSaddleFront.rotateAngleX = this.body.rotateAngleX;
            this.horseSaddleBack.rotateAngleX = this.body.rotateAngleX;
            this.horseLeftRein.rotationPointY = this.head.rotationPointY;
            this.horseRightRein.rotationPointY = this.head.rotationPointY;
            this.horseFaceRopes.rotationPointY = this.head.rotationPointY;
            this.horseLeftFaceMetal.rotationPointY = this.head.rotationPointY;
            this.horseRightFaceMetal.rotationPointY = this.head.rotationPointY;
            this.horseLeftRein.rotationPointZ = this.head.rotationPointZ;
            this.horseRightRein.rotationPointZ = this.head.rotationPointZ;
            this.horseFaceRopes.rotationPointZ = this.head.rotationPointZ;
            this.horseLeftFaceMetal.rotationPointZ = this.head.rotationPointZ;
            this.horseRightFaceMetal.rotationPointZ = this.head.rotationPointZ;
            this.horseLeftRein.rotateAngleX = f5;
            this.horseRightRein.rotateAngleX = f5;
            this.horseFaceRopes.rotateAngleX = this.head.rotateAngleX;
            this.horseLeftFaceMetal.rotateAngleX = this.head.rotateAngleX;
            this.horseRightFaceMetal.rotateAngleX = this.head.rotateAngleX;
            this.horseFaceRopes.rotateAngleY = this.head.rotateAngleY;
            this.horseLeftFaceMetal.rotateAngleY = this.head.rotateAngleY;
            this.horseLeftRein.rotateAngleY = this.head.rotateAngleY;
            this.horseRightFaceMetal.rotateAngleY = this.head.rotateAngleY;
            this.horseRightRein.rotateAngleY = this.head.rotateAngleY;
            if (flag3) {
                this.horseLeftSaddleRope.rotateAngleX = -1.0471976f;
                this.horseLeftSaddleMetal.rotateAngleX = -1.0471976f;
                this.horseRightSaddleRope.rotateAngleX = -1.0471976f;
                this.horseRightSaddleMetal.rotateAngleX = -1.0471976f;
                this.horseLeftSaddleRope.rotateAngleZ = 0.0f;
                this.horseLeftSaddleMetal.rotateAngleZ = 0.0f;
                this.horseRightSaddleRope.rotateAngleZ = 0.0f;
                this.horseRightSaddleMetal.rotateAngleZ = 0.0f;
            }
            else {
                this.horseLeftSaddleRope.rotateAngleX = f12 / 3.0f;
                this.horseLeftSaddleMetal.rotateAngleX = f12 / 3.0f;
                this.horseRightSaddleRope.rotateAngleX = f12 / 3.0f;
                this.horseRightSaddleMetal.rotateAngleX = f12 / 3.0f;
                this.horseLeftSaddleRope.rotateAngleZ = f12 / 5.0f;
                this.horseLeftSaddleMetal.rotateAngleZ = f12 / 5.0f;
                this.horseRightSaddleRope.rotateAngleZ = -f12 / 5.0f;
                this.horseRightSaddleMetal.rotateAngleZ = -f12 / 5.0f;
            }
        }
        f13 = -1.3089969f + limbSwingAmount * 1.5f;
        if (f13 > 0.0f) {
            f13 = 0.0f;
        }
        if (flag) {
            this.tailBase.rotateAngleY = MathHelper.cos(f10 * 0.7f);
            f13 = 0.0f;
        }
        else {
            this.tailBase.rotateAngleY = 0.0f;
        }
        this.tailMiddle.rotateAngleY = this.tailBase.rotateAngleY;
        this.tailTip.rotateAngleY = this.tailBase.rotateAngleY;
        this.tailMiddle.rotationPointY = this.tailBase.rotationPointY;
        this.tailTip.rotationPointY = this.tailBase.rotationPointY;
        this.tailMiddle.rotationPointZ = this.tailBase.rotationPointZ;
        this.tailTip.rotationPointZ = this.tailBase.rotationPointZ;
        this.tailBase.rotateAngleX = f13;
        this.tailMiddle.rotateAngleX = f13;
        this.tailTip.rotateAngleX = -0.2617994f + f13;
    }
}
