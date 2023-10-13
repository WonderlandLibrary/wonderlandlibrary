/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;

public class ModelDragon
extends ModelBase {
    private ModelRenderer head;
    private ModelRenderer spine;
    private ModelRenderer jaw;
    private ModelRenderer body;
    private ModelRenderer rearLeg;
    private ModelRenderer frontLeg;
    private ModelRenderer rearLegTip;
    private ModelRenderer frontLegTip;
    private ModelRenderer rearFoot;
    private ModelRenderer frontFoot;
    private ModelRenderer wing;
    private ModelRenderer wingTip;
    private float partialTicks;
    private static final String __OBFID = "CL_00000870";

    public ModelDragon(float p_i46360_1_) {
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.setTextureOffset("body.body", 0, 0);
        this.setTextureOffset("wing.skin", -56, 88);
        this.setTextureOffset("wingtip.skin", -56, 144);
        this.setTextureOffset("rearleg.main", 0, 0);
        this.setTextureOffset("rearfoot.main", 112, 0);
        this.setTextureOffset("rearlegtip.main", 196, 0);
        this.setTextureOffset("head.upperhead", 112, 30);
        this.setTextureOffset("wing.bone", 112, 88);
        this.setTextureOffset("head.upperlip", 176, 44);
        this.setTextureOffset("jaw.jaw", 176, 65);
        this.setTextureOffset("frontleg.main", 112, 104);
        this.setTextureOffset("wingtip.bone", 112, 136);
        this.setTextureOffset("frontfoot.main", 144, 104);
        this.setTextureOffset("neck.box", 192, 104);
        this.setTextureOffset("frontlegtip.main", 226, 138);
        this.setTextureOffset("body.scale", 220, 53);
        this.setTextureOffset("head.scale", 0, 0);
        this.setTextureOffset("neck.scale", 48, 0);
        this.setTextureOffset("head.nostril", 112, 0);
        float var2 = -16.0f;
        this.head = new ModelRenderer(this, "head");
        this.head.addBox("upperlip", -6.0f, -1.0f, -8.0f + var2, 12, 5, 16);
        this.head.addBox("upperhead", -8.0f, -8.0f, 6.0f + var2, 16, 16, 16);
        this.head.mirror = true;
        this.head.addBox("scale", -5.0f, -12.0f, 12.0f + var2, 2, 4, 6);
        this.head.addBox("nostril", -5.0f, -3.0f, -6.0f + var2, 2, 2, 4);
        this.head.mirror = false;
        this.head.addBox("scale", 3.0f, -12.0f, 12.0f + var2, 2, 4, 6);
        this.head.addBox("nostril", 3.0f, -3.0f, -6.0f + var2, 2, 2, 4);
        this.jaw = new ModelRenderer(this, "jaw");
        this.jaw.setRotationPoint(0.0f, 4.0f, 8.0f + var2);
        this.jaw.addBox("jaw", -6.0f, 0.0f, -16.0f, 12, 4, 16);
        this.head.addChild(this.jaw);
        this.spine = new ModelRenderer(this, "neck");
        this.spine.addBox("box", -5.0f, -5.0f, -5.0f, 10, 10, 10);
        this.spine.addBox("scale", -1.0f, -9.0f, -3.0f, 2, 4, 6);
        this.body = new ModelRenderer(this, "body");
        this.body.setRotationPoint(0.0f, 4.0f, 8.0f);
        this.body.addBox("body", -12.0f, 0.0f, -16.0f, 24, 24, 64);
        this.body.addBox("scale", -1.0f, -6.0f, -10.0f, 2, 6, 12);
        this.body.addBox("scale", -1.0f, -6.0f, 10.0f, 2, 6, 12);
        this.body.addBox("scale", -1.0f, -6.0f, 30.0f, 2, 6, 12);
        this.wing = new ModelRenderer(this, "wing");
        this.wing.setRotationPoint(-12.0f, 5.0f, 2.0f);
        this.wing.addBox("bone", -56.0f, -4.0f, -4.0f, 56, 8, 8);
        this.wing.addBox("skin", -56.0f, 0.0f, 2.0f, 56, 0, 56);
        this.wingTip = new ModelRenderer(this, "wingtip");
        this.wingTip.setRotationPoint(-56.0f, 0.0f, 0.0f);
        this.wingTip.addBox("bone", -56.0f, -2.0f, -2.0f, 56, 4, 4);
        this.wingTip.addBox("skin", -56.0f, 0.0f, 2.0f, 56, 0, 56);
        this.wing.addChild(this.wingTip);
        this.frontLeg = new ModelRenderer(this, "frontleg");
        this.frontLeg.setRotationPoint(-12.0f, 20.0f, 2.0f);
        this.frontLeg.addBox("main", -4.0f, -4.0f, -4.0f, 8, 24, 8);
        this.frontLegTip = new ModelRenderer(this, "frontlegtip");
        this.frontLegTip.setRotationPoint(0.0f, 20.0f, -1.0f);
        this.frontLegTip.addBox("main", -3.0f, -1.0f, -3.0f, 6, 24, 6);
        this.frontLeg.addChild(this.frontLegTip);
        this.frontFoot = new ModelRenderer(this, "frontfoot");
        this.frontFoot.setRotationPoint(0.0f, 23.0f, 0.0f);
        this.frontFoot.addBox("main", -4.0f, 0.0f, -12.0f, 8, 4, 16);
        this.frontLegTip.addChild(this.frontFoot);
        this.rearLeg = new ModelRenderer(this, "rearleg");
        this.rearLeg.setRotationPoint(-16.0f, 16.0f, 42.0f);
        this.rearLeg.addBox("main", -8.0f, -4.0f, -8.0f, 16, 32, 16);
        this.rearLegTip = new ModelRenderer(this, "rearlegtip");
        this.rearLegTip.setRotationPoint(0.0f, 32.0f, -4.0f);
        this.rearLegTip.addBox("main", -6.0f, -2.0f, 0.0f, 12, 32, 12);
        this.rearLeg.addChild(this.rearLegTip);
        this.rearFoot = new ModelRenderer(this, "rearfoot");
        this.rearFoot.setRotationPoint(0.0f, 31.0f, 4.0f);
        this.rearFoot.addBox("main", -9.0f, 0.0f, -20.0f, 18, 6, 24);
        this.rearLegTip.addChild(this.rearFoot);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
        this.partialTicks = p_78086_4_;
    }

    @Override
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        float var21;
        GlStateManager.pushMatrix();
        EntityDragon var8 = (EntityDragon)p_78088_1_;
        float var9 = var8.prevAnimTime + (var8.animTime - var8.prevAnimTime) * this.partialTicks;
        this.jaw.rotateAngleX = (float)(Math.sin(var9 * (float)Math.PI * 2.0f) + 1.0) * 0.2f;
        float var10 = (float)(Math.sin(var9 * (float)Math.PI * 2.0f - 1.0f) + 1.0);
        var10 = (var10 * var10 * 1.0f + var10 * 2.0f) * 0.05f;
        GlStateManager.translate(0.0f, var10 - 2.0f, -3.0f);
        GlStateManager.rotate(var10 * 2.0f, 1.0f, 0.0f, 0.0f);
        float var11 = -30.0f;
        float var13 = 0.0f;
        float var14 = 1.5f;
        double[] var15 = var8.getMovementOffsets(6, this.partialTicks);
        float var16 = this.updateRotations(var8.getMovementOffsets(5, this.partialTicks)[0] - var8.getMovementOffsets(10, this.partialTicks)[0]);
        float var17 = this.updateRotations(var8.getMovementOffsets(5, this.partialTicks)[0] + (double)(var16 / 2.0f));
        var11 += 2.0f;
        float var18 = var9 * (float)Math.PI * 2.0f;
        var11 = 20.0f;
        float var12 = -12.0f;
        for (int var19 = 0; var19 < 5; ++var19) {
            double[] var20 = var8.getMovementOffsets(5 - var19, this.partialTicks);
            var21 = (float)Math.cos((float)var19 * 0.45f + var18) * 0.15f;
            this.spine.rotateAngleY = this.updateRotations(var20[0] - var15[0]) * (float)Math.PI / 180.0f * var14;
            this.spine.rotateAngleX = var21 + (float)(var20[1] - var15[1]) * (float)Math.PI / 180.0f * var14 * 5.0f;
            this.spine.rotateAngleZ = -this.updateRotations(var20[0] - (double)var17) * (float)Math.PI / 180.0f * var14;
            this.spine.rotationPointY = var11;
            this.spine.rotationPointZ = var12;
            this.spine.rotationPointX = var13;
            var11 = (float)((double)var11 + Math.sin(this.spine.rotateAngleX) * 10.0);
            var12 = (float)((double)var12 - Math.cos(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0);
            var13 = (float)((double)var13 - Math.sin(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0);
            this.spine.render(p_78088_7_);
        }
        this.head.rotationPointY = var11;
        this.head.rotationPointZ = var12;
        this.head.rotationPointX = var13;
        double[] var22 = var8.getMovementOffsets(0, this.partialTicks);
        this.head.rotateAngleY = this.updateRotations(var22[0] - var15[0]) * (float)Math.PI / 180.0f * 1.0f;
        this.head.rotateAngleZ = -this.updateRotations(var22[0] - (double)var17) * (float)Math.PI / 180.0f * 1.0f;
        this.head.render(p_78088_7_);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-var16 * var14 * 1.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(0.0f, -1.0f, 0.0f);
        this.body.rotateAngleZ = 0.0f;
        this.body.render(p_78088_7_);
        for (int var23 = 0; var23 < 2; ++var23) {
            GlStateManager.enableCull();
            var21 = var9 * (float)Math.PI * 2.0f;
            this.wing.rotateAngleX = 0.125f - (float)Math.cos(var21) * 0.2f;
            this.wing.rotateAngleY = 0.25f;
            this.wing.rotateAngleZ = (float)(Math.sin(var21) + 0.125) * 0.8f;
            this.wingTip.rotateAngleZ = -((float)(Math.sin(var21 + 2.0f) + 0.5)) * 0.75f;
            this.rearLeg.rotateAngleX = 1.0f + var10 * 0.1f;
            this.rearLegTip.rotateAngleX = 0.5f + var10 * 0.1f;
            this.rearFoot.rotateAngleX = 0.75f + var10 * 0.1f;
            this.frontLeg.rotateAngleX = 1.3f + var10 * 0.1f;
            this.frontLegTip.rotateAngleX = -0.5f - var10 * 0.1f;
            this.frontFoot.rotateAngleX = 0.75f + var10 * 0.1f;
            this.wing.render(p_78088_7_);
            this.frontLeg.render(p_78088_7_);
            this.rearLeg.render(p_78088_7_);
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            if (var23 != 0) continue;
            GlStateManager.cullFace(1028);
        }
        GlStateManager.popMatrix();
        GlStateManager.cullFace(1029);
        GlStateManager.disableCull();
        float var24 = -((float)Math.sin(var9 * (float)Math.PI * 2.0f)) * 0.0f;
        var18 = var9 * (float)Math.PI * 2.0f;
        var11 = 10.0f;
        var12 = 60.0f;
        var13 = 0.0f;
        var15 = var8.getMovementOffsets(11, this.partialTicks);
        for (int var25 = 0; var25 < 12; ++var25) {
            var22 = var8.getMovementOffsets(12 + var25, this.partialTicks);
            var24 = (float)((double)var24 + Math.sin((float)var25 * 0.45f + var18) * (double)0.05f);
            this.spine.rotateAngleY = (this.updateRotations(var22[0] - var15[0]) * var14 + 180.0f) * (float)Math.PI / 180.0f;
            this.spine.rotateAngleX = var24 + (float)(var22[1] - var15[1]) * (float)Math.PI / 180.0f * var14 * 5.0f;
            this.spine.rotateAngleZ = this.updateRotations(var22[0] - (double)var17) * (float)Math.PI / 180.0f * var14;
            this.spine.rotationPointY = var11;
            this.spine.rotationPointZ = var12;
            this.spine.rotationPointX = var13;
            var11 = (float)((double)var11 + Math.sin(this.spine.rotateAngleX) * 10.0);
            var12 = (float)((double)var12 - Math.cos(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0);
            var13 = (float)((double)var13 - Math.sin(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0);
            this.spine.render(p_78088_7_);
        }
        GlStateManager.popMatrix();
    }

    private float updateRotations(double p_78214_1_) {
        while (p_78214_1_ >= 180.0) {
            p_78214_1_ -= 360.0;
        }
        while (p_78214_1_ < -180.0) {
            p_78214_1_ += 360.0;
        }
        return (float)p_78214_1_;
    }
}

