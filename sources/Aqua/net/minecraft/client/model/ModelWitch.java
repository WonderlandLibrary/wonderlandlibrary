package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelWitch
extends ModelVillager {
    public boolean field_82900_g;
    private ModelRenderer field_82901_h = new ModelRenderer((ModelBase)this).setTextureSize(64, 128);
    private ModelRenderer witchHat;

    public ModelWitch(float p_i46361_1_) {
        super(p_i46361_1_, 0.0f, 64, 128);
        this.field_82901_h.setRotationPoint(0.0f, -2.0f, 0.0f);
        this.field_82901_h.setTextureOffset(0, 0).addBox(0.0f, 3.0f, -6.75f, 1, 1, 1, -0.25f);
        this.villagerNose.addChild(this.field_82901_h);
        this.witchHat = new ModelRenderer((ModelBase)this).setTextureSize(64, 128);
        this.witchHat.setRotationPoint(-5.0f, -10.03125f, -5.0f);
        this.witchHat.setTextureOffset(0, 64).addBox(0.0f, 0.0f, 0.0f, 10, 2, 10);
        this.villagerHead.addChild(this.witchHat);
        ModelRenderer modelrenderer = new ModelRenderer((ModelBase)this).setTextureSize(64, 128);
        modelrenderer.setRotationPoint(1.75f, -4.0f, 2.0f);
        modelrenderer.setTextureOffset(0, 76).addBox(0.0f, 0.0f, 0.0f, 7, 4, 7);
        modelrenderer.rotateAngleX = -0.05235988f;
        modelrenderer.rotateAngleZ = 0.02617994f;
        this.witchHat.addChild(modelrenderer);
        ModelRenderer modelrenderer1 = new ModelRenderer((ModelBase)this).setTextureSize(64, 128);
        modelrenderer1.setRotationPoint(1.75f, -4.0f, 2.0f);
        modelrenderer1.setTextureOffset(0, 87).addBox(0.0f, 0.0f, 0.0f, 4, 4, 4);
        modelrenderer1.rotateAngleX = -0.10471976f;
        modelrenderer1.rotateAngleZ = 0.05235988f;
        modelrenderer.addChild(modelrenderer1);
        ModelRenderer modelrenderer2 = new ModelRenderer((ModelBase)this).setTextureSize(64, 128);
        modelrenderer2.setRotationPoint(1.75f, -2.0f, 2.0f);
        modelrenderer2.setTextureOffset(0, 95).addBox(0.0f, 0.0f, 0.0f, 1, 2, 1, 0.25f);
        modelrenderer2.rotateAngleX = -0.20943952f;
        modelrenderer2.rotateAngleZ = 0.10471976f;
        modelrenderer1.addChild(modelrenderer2);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.villagerNose.offsetZ = 0.0f;
        this.villagerNose.offsetY = 0.0f;
        this.villagerNose.offsetX = 0.0f;
        float f = 0.01f * (float)(entityIn.getEntityId() % 10);
        this.villagerNose.rotateAngleX = MathHelper.sin((float)((float)entityIn.ticksExisted * f)) * 4.5f * (float)Math.PI / 180.0f;
        this.villagerNose.rotateAngleY = 0.0f;
        this.villagerNose.rotateAngleZ = MathHelper.cos((float)((float)entityIn.ticksExisted * f)) * 2.5f * (float)Math.PI / 180.0f;
        if (this.field_82900_g) {
            this.villagerNose.rotateAngleX = -0.9f;
            this.villagerNose.offsetZ = -0.09375f;
            this.villagerNose.offsetY = 0.1875f;
        }
    }
}
