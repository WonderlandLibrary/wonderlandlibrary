// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GLAllocation;

public class ModelBoat extends ModelBase implements IMultipassModel
{
    public ModelRenderer[] boatSides;
    public ModelRenderer[] paddles;
    public ModelRenderer noWater;
    private final int patchList;
    
    public ModelBoat() {
        this.boatSides = new ModelRenderer[5];
        this.paddles = new ModelRenderer[2];
        this.patchList = GLAllocation.generateDisplayLists(1);
        this.boatSides[0] = new ModelRenderer(this, 0, 0).setTextureSize(128, 64);
        this.boatSides[1] = new ModelRenderer(this, 0, 19).setTextureSize(128, 64);
        this.boatSides[2] = new ModelRenderer(this, 0, 27).setTextureSize(128, 64);
        this.boatSides[3] = new ModelRenderer(this, 0, 35).setTextureSize(128, 64);
        this.boatSides[4] = new ModelRenderer(this, 0, 43).setTextureSize(128, 64);
        final int i = 32;
        final int j = 6;
        final int k = 20;
        final int l = 4;
        final int i2 = 28;
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
        this.boatSides[3].rotateAngleY = 3.1415927f;
        (this.paddles[0] = this.makePaddle(true)).setRotationPoint(3.0f, -5.0f, 9.0f);
        (this.paddles[1] = this.makePaddle(false)).setRotationPoint(3.0f, -5.0f, -9.0f);
        this.paddles[1].rotateAngleY = 3.1415927f;
        this.paddles[0].rotateAngleZ = 0.19634955f;
        this.paddles[1].rotateAngleZ = 0.19634955f;
        (this.noWater = new ModelRenderer(this, 0, 0).setTextureSize(128, 64)).addBox(-14.0f, -9.0f, -3.0f, 28, 16, 3, 0.0f);
        this.noWater.setRotationPoint(0.0f, -3.0f, 1.0f);
        this.noWater.rotateAngleX = 1.5707964f;
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        final EntityBoat entityboat = (EntityBoat)entityIn;
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        for (int i = 0; i < 5; ++i) {
            this.boatSides[i].render(scale);
        }
        this.renderPaddle(entityboat, 0, scale, limbSwing);
        this.renderPaddle(entityboat, 1, scale, limbSwing);
    }
    
    @Override
    public void renderMultipass(final Entity entityIn, final float partialTicks, final float p_187054_3_, final float p_187054_4_, final float p_187054_5_, final float p_187054_6_, final float scale) {
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.colorMask(false, false, false, false);
        this.noWater.render(scale);
        GlStateManager.colorMask(true, true, true, true);
    }
    
    protected ModelRenderer makePaddle(final boolean p_187056_1_) {
        final ModelRenderer modelrenderer = new ModelRenderer(this, 62, p_187056_1_ ? 0 : 20).setTextureSize(128, 64);
        final int i = 20;
        final int j = 7;
        final int k = 6;
        final float f = -5.0f;
        modelrenderer.addBox(-1.0f, 0.0f, -5.0f, 2, 2, 18);
        modelrenderer.addBox(p_187056_1_ ? -1.001f : 0.001f, -3.0f, 8.0f, 1, 6, 7);
        return modelrenderer;
    }
    
    protected void renderPaddle(final EntityBoat boat, final int paddle, final float scale, final float limbSwing) {
        final float f = boat.getRowingTime(paddle, limbSwing);
        final ModelRenderer modelrenderer = this.paddles[paddle];
        modelrenderer.rotateAngleX = (float)MathHelper.clampedLerp(-1.0471975803375244, -0.2617993950843811, (MathHelper.sin(-f) + 1.0f) / 2.0f);
        modelrenderer.rotateAngleY = (float)MathHelper.clampedLerp(-0.7853981633974483, 0.7853981633974483, (MathHelper.sin(-f + 1.0f) + 1.0f) / 2.0f);
        if (paddle == 1) {
            modelrenderer.rotateAngleY = 3.1415927f - modelrenderer.rotateAngleY;
        }
        modelrenderer.render(scale);
    }
}
