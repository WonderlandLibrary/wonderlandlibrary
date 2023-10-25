/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelWitch
extends ModelVillager {
    private ModelRenderer witchHat;
    private ModelRenderer field_82901_h = new ModelRenderer(this).setTextureSize(64, 128);
    public boolean field_82900_g;

    public ModelWitch(float f) {
        super(f, 0.0f, 64, 128);
        this.field_82901_h.setRotationPoint(0.0f, -2.0f, 0.0f);
        this.field_82901_h.setTextureOffset(0, 0).addBox(0.0f, 3.0f, -6.75f, 1, 1, 1, -0.25f);
        this.villagerNose.addChild(this.field_82901_h);
        this.witchHat = new ModelRenderer(this).setTextureSize(64, 128);
        this.witchHat.setRotationPoint(-5.0f, -10.03125f, -5.0f);
        this.witchHat.setTextureOffset(0, 64).addBox(0.0f, 0.0f, 0.0f, 10, 2, 10);
        this.villagerHead.addChild(this.witchHat);
        ModelRenderer modelRenderer = new ModelRenderer(this).setTextureSize(64, 128);
        modelRenderer.setRotationPoint(1.75f, -4.0f, 2.0f);
        modelRenderer.setTextureOffset(0, 76).addBox(0.0f, 0.0f, 0.0f, 7, 4, 7);
        modelRenderer.rotateAngleX = -0.05235988f;
        modelRenderer.rotateAngleZ = 0.02617994f;
        this.witchHat.addChild(modelRenderer);
        ModelRenderer modelRenderer2 = new ModelRenderer(this).setTextureSize(64, 128);
        modelRenderer2.setRotationPoint(1.75f, -4.0f, 2.0f);
        modelRenderer2.setTextureOffset(0, 87).addBox(0.0f, 0.0f, 0.0f, 4, 4, 4);
        modelRenderer2.rotateAngleX = -0.10471976f;
        modelRenderer2.rotateAngleZ = 0.05235988f;
        modelRenderer.addChild(modelRenderer2);
        ModelRenderer modelRenderer3 = new ModelRenderer(this).setTextureSize(64, 128);
        modelRenderer3.setRotationPoint(1.75f, -2.0f, 2.0f);
        modelRenderer3.setTextureOffset(0, 95).addBox(0.0f, 0.0f, 0.0f, 1, 2, 1, 0.25f);
        modelRenderer3.rotateAngleX = -0.20943952f;
        modelRenderer3.rotateAngleZ = 0.10471976f;
        modelRenderer2.addChild(modelRenderer3);
    }

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        super.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        this.villagerNose.offsetZ = 0.0f;
        this.villagerNose.offsetY = 0.0f;
        this.villagerNose.offsetX = 0.0f;
        float f7 = 0.01f * (float)(entity.getEntityId() % 10);
        this.villagerNose.rotateAngleX = MathHelper.sin((float)entity.ticksExisted * f7) * 4.5f * (float)Math.PI / 180.0f;
        this.villagerNose.rotateAngleY = 0.0f;
        this.villagerNose.rotateAngleZ = MathHelper.cos((float)entity.ticksExisted * f7) * 2.5f * (float)Math.PI / 180.0f;
        if (this.field_82900_g) {
            this.villagerNose.rotateAngleX = -0.9f;
            this.villagerNose.offsetZ = -0.09375f;
            this.villagerNose.offsetY = 0.1875f;
        }
    }
}

