/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.IMultipassModel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.math.MathHelper;

public class ModelBoat
extends ModelBase
implements IMultipassModel {
    public ModelRenderer[] boatSides = new ModelRenderer[5];
    public ModelRenderer[] paddles = new ModelRenderer[2];
    public ModelRenderer noWater;
    private final int patchList = GLAllocation.generateDisplayLists(1);

    public ModelBoat() {
        this.boatSides[0] = new ModelRenderer(this, 0, 0).setTextureSize(128, 64);
        this.boatSides[1] = new ModelRenderer(this, 0, 19).setTextureSize(128, 64);
        this.boatSides[2] = new ModelRenderer(this, 0, 27).setTextureSize(128, 64);
        this.boatSides[3] = new ModelRenderer(this, 0, 35).setTextureSize(128, 64);
        this.boatSides[4] = new ModelRenderer(this, 0, 43).setTextureSize(128, 64);
        int i = 32;
        int j = 6;
        int k = 20;
        int l = 4;
        int i1 = 28;
        this.boatSides[0].addBox(-14.0f, -9.0f, -3.0f, 28, 16, 3, 0.0f);
        this.boatSides[0].setRotationPoint(0.0f, 3.0f, 1.0f);
        this.boatSides[1].addBox(-13.0f, -7.0f, -1.0f, 18, 6, 2, 0.0f);
        this.boatSides[1].setRotationPoint(-15.0f, 4.0f, 4.0f);
        this.boatSides[2].addBox(-8.0f, -7.0f, -1.0f, 16, 6, 2, 0.0f);
        this.boatSides[2].setRotationPoint(15.0f, 4.0f, 0.0f);
        this.boatSides[3].addBox(-14.0f, -7.0f, -1.0f, 28, 6, 2, 0.0f);
        this.boatSides[3].setRotationPoint(0.0f, 4.0f, -9.0f);
        this.boatSides[4].addBox(-14.0f, -7.0f, -1.0f, 28, 6, 2, 0.0f);
        this.boatSides[4].setRotationPoint(0.0f, 4.0f, 9.0f);
        this.boatSides[0].rotateAngleX = 1.5707964f;
        this.boatSides[1].rotateAngleY = 4.712389f;
        this.boatSides[2].rotateAngleY = 1.5707964f;
        this.boatSides[3].rotateAngleY = (float)Math.PI;
        this.paddles[0] = this.makePaddle(true);
        this.paddles[0].setRotationPoint(3.0f, -5.0f, 9.0f);
        this.paddles[1] = this.makePaddle(false);
        this.paddles[1].setRotationPoint(3.0f, -5.0f, -9.0f);
        this.paddles[1].rotateAngleY = (float)Math.PI;
        this.paddles[0].rotateAngleZ = 0.19634955f;
        this.paddles[1].rotateAngleZ = 0.19634955f;
        this.noWater = new ModelRenderer(this, 0, 0).setTextureSize(128, 64);
        this.noWater.addBox(-14.0f, -9.0f, -3.0f, 28, 16, 3, 0.0f);
        this.noWater.setRotationPoint(0.0f, -3.0f, 1.0f);
        this.noWater.rotateAngleX = 1.5707964f;
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        EntityBoat entityboat = (EntityBoat)entityIn;
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        for (int i = 0; i < 5; ++i) {
            this.boatSides[i].render(scale);
        }
        this.renderPaddle(entityboat, 0, scale, limbSwing);
        this.renderPaddle(entityboat, 1, scale, limbSwing);
    }

    @Override
    public void renderMultipass(Entity p_187054_1_, float p_187054_2_, float p_187054_3_, float p_187054_4_, float p_187054_5_, float p_187054_6_, float scale) {
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.colorMask(false, false, false, false);
        this.noWater.render(scale);
        GlStateManager.colorMask(true, true, true, true);
    }

    protected ModelRenderer makePaddle(boolean p_187056_1_) {
        ModelRenderer modelrenderer = new ModelRenderer(this, 62, p_187056_1_ ? 0 : 20).setTextureSize(128, 64);
        int i = 20;
        int j = 7;
        int k = 6;
        float f = -5.0f;
        modelrenderer.addBox(-1.0f, 0.0f, -5.0f, 2, 2, 18);
        modelrenderer.addBox(p_187056_1_ ? -1.001f : 0.001f, -3.0f, 8.0f, 1, 6, 7);
        return modelrenderer;
    }

    protected void renderPaddle(EntityBoat boat, int paddle, float scale, float limbSwing) {
        float f = boat.getRowingTime(paddle, limbSwing);
        ModelRenderer modelrenderer = this.paddles[paddle];
        modelrenderer.rotateAngleX = (float)MathHelper.clampedLerp(-1.0471975803375244, -0.2617993950843811, (MathHelper.sin(-f) + 1.0f) / 2.0f);
        modelrenderer.rotateAngleY = (float)MathHelper.clampedLerp(-0.7853981633974483, 0.7853981633974483, (MathHelper.sin(-f + 1.0f) + 1.0f) / 2.0f);
        if (paddle == 1) {
            modelrenderer.rotateAngleY = (float)Math.PI - modelrenderer.rotateAngleY;
        }
        modelrenderer.render(scale);
    }
}

