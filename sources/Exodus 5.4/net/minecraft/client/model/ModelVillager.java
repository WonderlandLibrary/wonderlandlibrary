/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelVillager
extends ModelBase {
    public ModelRenderer villagerBody;
    public ModelRenderer villagerArms;
    public ModelRenderer villagerNose;
    public ModelRenderer leftVillagerLeg;
    public ModelRenderer rightVillagerLeg;
    public ModelRenderer villagerHead;

    public ModelVillager(float f) {
        this(f, 0.0f, 64, 64);
    }

    public ModelVillager(float f, float f2, int n, int n2) {
        this.villagerHead = new ModelRenderer(this).setTextureSize(n, n2);
        this.villagerHead.setRotationPoint(0.0f, 0.0f + f2, 0.0f);
        this.villagerHead.setTextureOffset(0, 0).addBox(-4.0f, -10.0f, -4.0f, 8, 10, 8, f);
        this.villagerNose = new ModelRenderer(this).setTextureSize(n, n2);
        this.villagerNose.setRotationPoint(0.0f, f2 - 2.0f, 0.0f);
        this.villagerNose.setTextureOffset(24, 0).addBox(-1.0f, -1.0f, -6.0f, 2, 4, 2, f);
        this.villagerHead.addChild(this.villagerNose);
        this.villagerBody = new ModelRenderer(this).setTextureSize(n, n2);
        this.villagerBody.setRotationPoint(0.0f, 0.0f + f2, 0.0f);
        this.villagerBody.setTextureOffset(16, 20).addBox(-4.0f, 0.0f, -3.0f, 8, 12, 6, f);
        this.villagerBody.setTextureOffset(0, 38).addBox(-4.0f, 0.0f, -3.0f, 8, 18, 6, f + 0.5f);
        this.villagerArms = new ModelRenderer(this).setTextureSize(n, n2);
        this.villagerArms.setRotationPoint(0.0f, 0.0f + f2 + 2.0f, 0.0f);
        this.villagerArms.setTextureOffset(44, 22).addBox(-8.0f, -2.0f, -2.0f, 4, 8, 4, f);
        this.villagerArms.setTextureOffset(44, 22).addBox(4.0f, -2.0f, -2.0f, 4, 8, 4, f);
        this.villagerArms.setTextureOffset(40, 38).addBox(-4.0f, 2.0f, -2.0f, 8, 4, 4, f);
        this.rightVillagerLeg = new ModelRenderer(this, 0, 22).setTextureSize(n, n2);
        this.rightVillagerLeg.setRotationPoint(-2.0f, 12.0f + f2, 0.0f);
        this.rightVillagerLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, f);
        this.leftVillagerLeg = new ModelRenderer(this, 0, 22).setTextureSize(n, n2);
        this.leftVillagerLeg.mirror = true;
        this.leftVillagerLeg.setRotationPoint(2.0f, 12.0f + f2, 0.0f);
        this.leftVillagerLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, f);
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        this.villagerHead.render(f6);
        this.villagerBody.render(f6);
        this.rightVillagerLeg.render(f6);
        this.leftVillagerLeg.render(f6);
        this.villagerArms.render(f6);
    }

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        this.villagerHead.rotateAngleY = f4 / 57.295776f;
        this.villagerHead.rotateAngleX = f5 / 57.295776f;
        this.villagerArms.rotationPointY = 3.0f;
        this.villagerArms.rotationPointZ = -1.0f;
        this.villagerArms.rotateAngleX = -0.75f;
        this.rightVillagerLeg.rotateAngleX = MathHelper.cos(f * 0.6662f) * 1.4f * f2 * 0.5f;
        this.leftVillagerLeg.rotateAngleX = MathHelper.cos(f * 0.6662f + (float)Math.PI) * 1.4f * f2 * 0.5f;
        this.rightVillagerLeg.rotateAngleY = 0.0f;
        this.leftVillagerLeg.rotateAngleY = 0.0f;
    }
}

