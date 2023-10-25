/*
 * Decompiled with CFR 0.152.
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
    private ModelRenderer frontLegTip;
    private ModelRenderer frontFoot;
    private ModelRenderer jaw;
    private ModelRenderer rearFoot;
    private ModelRenderer wingTip;
    private ModelRenderer spine;
    private ModelRenderer wing;
    private ModelRenderer rearLegTip;
    private ModelRenderer rearLeg;
    private float partialTicks;
    private ModelRenderer head;
    private ModelRenderer body;
    private ModelRenderer frontLeg;

    public ModelDragon(float f) {
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
        float f2 = -16.0f;
        this.head = new ModelRenderer(this, "head");
        this.head.addBox("upperlip", -6.0f, -1.0f, -8.0f + f2, 12, 5, 16);
        this.head.addBox("upperhead", -8.0f, -8.0f, 6.0f + f2, 16, 16, 16);
        this.head.mirror = true;
        this.head.addBox("scale", -5.0f, -12.0f, 12.0f + f2, 2, 4, 6);
        this.head.addBox("nostril", -5.0f, -3.0f, -6.0f + f2, 2, 2, 4);
        this.head.mirror = false;
        this.head.addBox("scale", 3.0f, -12.0f, 12.0f + f2, 2, 4, 6);
        this.head.addBox("nostril", 3.0f, -3.0f, -6.0f + f2, 2, 2, 4);
        this.jaw = new ModelRenderer(this, "jaw");
        this.jaw.setRotationPoint(0.0f, 4.0f, 8.0f + f2);
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
    public void setLivingAnimations(EntityLivingBase entityLivingBase, float f, float f2, float f3) {
        this.partialTicks = f3;
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        float f7;
        GlStateManager.pushMatrix();
        EntityDragon entityDragon = (EntityDragon)entity;
        float f8 = entityDragon.prevAnimTime + (entityDragon.animTime - entityDragon.prevAnimTime) * this.partialTicks;
        this.jaw.rotateAngleX = (float)(Math.sin(f8 * (float)Math.PI * 2.0f) + 1.0) * 0.2f;
        float f9 = (float)(Math.sin(f8 * (float)Math.PI * 2.0f - 1.0f) + 1.0);
        f9 = (f9 * f9 * 1.0f + f9 * 2.0f) * 0.05f;
        GlStateManager.translate(0.0f, f9 - 2.0f, -3.0f);
        GlStateManager.rotate(f9 * 2.0f, 1.0f, 0.0f, 0.0f);
        float f10 = -30.0f;
        float f11 = 0.0f;
        float f12 = 1.5f;
        double[] dArray = entityDragon.getMovementOffsets(6, this.partialTicks);
        float f13 = this.updateRotations(entityDragon.getMovementOffsets(5, this.partialTicks)[0] - entityDragon.getMovementOffsets(10, this.partialTicks)[0]);
        float f14 = this.updateRotations(entityDragon.getMovementOffsets(5, this.partialTicks)[0] + (double)(f13 / 2.0f));
        f10 += 2.0f;
        float f15 = f8 * (float)Math.PI * 2.0f;
        f10 = 20.0f;
        float f16 = -12.0f;
        int n = 0;
        while (n < 5) {
            double[] dArray2 = entityDragon.getMovementOffsets(5 - n, this.partialTicks);
            f7 = (float)Math.cos((float)n * 0.45f + f15) * 0.15f;
            this.spine.rotateAngleY = this.updateRotations(dArray2[0] - dArray[0]) * (float)Math.PI / 180.0f * f12;
            this.spine.rotateAngleX = f7 + (float)(dArray2[1] - dArray[1]) * (float)Math.PI / 180.0f * f12 * 5.0f;
            this.spine.rotateAngleZ = -this.updateRotations(dArray2[0] - (double)f14) * (float)Math.PI / 180.0f * f12;
            this.spine.rotationPointY = f10;
            this.spine.rotationPointZ = f16;
            this.spine.rotationPointX = f11;
            f10 = (float)((double)f10 + Math.sin(this.spine.rotateAngleX) * 10.0);
            f16 = (float)((double)f16 - Math.cos(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0);
            f11 = (float)((double)f11 - Math.sin(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0);
            this.spine.render(f6);
            ++n;
        }
        this.head.rotationPointY = f10;
        this.head.rotationPointZ = f16;
        this.head.rotationPointX = f11;
        double[] dArray3 = entityDragon.getMovementOffsets(0, this.partialTicks);
        this.head.rotateAngleY = this.updateRotations(dArray3[0] - dArray[0]) * (float)Math.PI / 180.0f * 1.0f;
        this.head.rotateAngleZ = -this.updateRotations(dArray3[0] - (double)f14) * (float)Math.PI / 180.0f * 1.0f;
        this.head.render(f6);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-f13 * f12 * 1.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(0.0f, -1.0f, 0.0f);
        this.body.rotateAngleZ = 0.0f;
        this.body.render(f6);
        int n2 = 0;
        while (n2 < 2) {
            GlStateManager.enableCull();
            f7 = f8 * (float)Math.PI * 2.0f;
            this.wing.rotateAngleX = 0.125f - (float)Math.cos(f7) * 0.2f;
            this.wing.rotateAngleY = 0.25f;
            this.wing.rotateAngleZ = (float)(Math.sin(f7) + 0.125) * 0.8f;
            this.wingTip.rotateAngleZ = -((float)(Math.sin(f7 + 2.0f) + 0.5)) * 0.75f;
            this.rearLeg.rotateAngleX = 1.0f + f9 * 0.1f;
            this.rearLegTip.rotateAngleX = 0.5f + f9 * 0.1f;
            this.rearFoot.rotateAngleX = 0.75f + f9 * 0.1f;
            this.frontLeg.rotateAngleX = 1.3f + f9 * 0.1f;
            this.frontLegTip.rotateAngleX = -0.5f - f9 * 0.1f;
            this.frontFoot.rotateAngleX = 0.75f + f9 * 0.1f;
            this.wing.render(f6);
            this.frontLeg.render(f6);
            this.rearLeg.render(f6);
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            if (n2 == 0) {
                GlStateManager.cullFace(1028);
            }
            ++n2;
        }
        GlStateManager.popMatrix();
        GlStateManager.cullFace(1029);
        GlStateManager.disableCull();
        float f17 = -((float)Math.sin(f8 * (float)Math.PI * 2.0f)) * 0.0f;
        f15 = f8 * (float)Math.PI * 2.0f;
        f10 = 10.0f;
        f16 = 60.0f;
        f11 = 0.0f;
        dArray = entityDragon.getMovementOffsets(11, this.partialTicks);
        int n3 = 0;
        while (n3 < 12) {
            dArray3 = entityDragon.getMovementOffsets(12 + n3, this.partialTicks);
            f17 = (float)((double)f17 + Math.sin((float)n3 * 0.45f + f15) * (double)0.05f);
            this.spine.rotateAngleY = (this.updateRotations(dArray3[0] - dArray[0]) * f12 + 180.0f) * (float)Math.PI / 180.0f;
            this.spine.rotateAngleX = f17 + (float)(dArray3[1] - dArray[1]) * (float)Math.PI / 180.0f * f12 * 5.0f;
            this.spine.rotateAngleZ = this.updateRotations(dArray3[0] - (double)f14) * (float)Math.PI / 180.0f * f12;
            this.spine.rotationPointY = f10;
            this.spine.rotationPointZ = f16;
            this.spine.rotationPointX = f11;
            f10 = (float)((double)f10 + Math.sin(this.spine.rotateAngleX) * 10.0);
            f16 = (float)((double)f16 - Math.cos(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0);
            f11 = (float)((double)f11 - Math.sin(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0);
            this.spine.render(f6);
            ++n3;
        }
        GlStateManager.popMatrix();
    }

    private float updateRotations(double d) {
        while (d >= 180.0) {
            d -= 360.0;
        }
        while (d < -180.0) {
            d += 360.0;
        }
        return (float)d;
    }
}

