// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;

public class ModelWitch extends ModelVillager
{
    public boolean holdingItem;
    private final ModelRenderer mole;
    private final ModelRenderer witchHat;
    
    public ModelWitch(final float scale) {
        super(scale, 0.0f, 64, 128);
        (this.mole = new ModelRenderer(this).setTextureSize(64, 128)).setRotationPoint(0.0f, -2.0f, 0.0f);
        this.mole.setTextureOffset(0, 0).addBox(0.0f, 3.0f, -6.75f, 1, 1, 1, -0.25f);
        this.villagerNose.addChild(this.mole);
        (this.witchHat = new ModelRenderer(this).setTextureSize(64, 128)).setRotationPoint(-5.0f, -10.03125f, -5.0f);
        this.witchHat.setTextureOffset(0, 64).addBox(0.0f, 0.0f, 0.0f, 10, 2, 10);
        this.villagerHead.addChild(this.witchHat);
        final ModelRenderer modelrenderer = new ModelRenderer(this).setTextureSize(64, 128);
        modelrenderer.setRotationPoint(1.75f, -4.0f, 2.0f);
        modelrenderer.setTextureOffset(0, 76).addBox(0.0f, 0.0f, 0.0f, 7, 4, 7);
        modelrenderer.rotateAngleX = -0.05235988f;
        modelrenderer.rotateAngleZ = 0.02617994f;
        this.witchHat.addChild(modelrenderer);
        final ModelRenderer modelrenderer2 = new ModelRenderer(this).setTextureSize(64, 128);
        modelrenderer2.setRotationPoint(1.75f, -4.0f, 2.0f);
        modelrenderer2.setTextureOffset(0, 87).addBox(0.0f, 0.0f, 0.0f, 4, 4, 4);
        modelrenderer2.rotateAngleX = -0.10471976f;
        modelrenderer2.rotateAngleZ = 0.05235988f;
        modelrenderer.addChild(modelrenderer2);
        final ModelRenderer modelrenderer3 = new ModelRenderer(this).setTextureSize(64, 128);
        modelrenderer3.setRotationPoint(1.75f, -2.0f, 2.0f);
        modelrenderer3.setTextureOffset(0, 95).addBox(0.0f, 0.0f, 0.0f, 1, 2, 1, 0.25f);
        modelrenderer3.rotateAngleX = -0.20943952f;
        modelrenderer3.rotateAngleZ = 0.10471976f;
        modelrenderer2.addChild(modelrenderer3);
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.villagerNose.offsetX = 0.0f;
        this.villagerNose.offsetY = 0.0f;
        this.villagerNose.offsetZ = 0.0f;
        final float f = 0.01f * (entityIn.getEntityId() % 10);
        this.villagerNose.rotateAngleX = MathHelper.sin(entityIn.ticksExisted * f) * 4.5f * 0.017453292f;
        this.villagerNose.rotateAngleY = 0.0f;
        this.villagerNose.rotateAngleZ = MathHelper.cos(entityIn.ticksExisted * f) * 2.5f * 0.017453292f;
        if (this.holdingItem) {
            this.villagerNose.rotateAngleX = -0.9f;
            this.villagerNose.offsetZ = -0.09375f;
            this.villagerNose.offsetY = 0.1875f;
        }
    }
}
