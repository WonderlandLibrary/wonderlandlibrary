// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;

public class ModelVillager extends ModelBase
{
    public ModelRenderer villagerHead;
    public ModelRenderer villagerBody;
    public ModelRenderer villagerArms;
    public ModelRenderer rightVillagerLeg;
    public ModelRenderer leftVillagerLeg;
    public ModelRenderer villagerNose;
    
    public ModelVillager(final float p_i1163_1_) {
        this(p_i1163_1_, 0.0f, 64, 64);
    }
    
    public ModelVillager(final float p_i1164_1_, final float p_i1164_2_, final int p_i1164_3_, final int p_i1164_4_) {
        (this.villagerHead = new ModelRenderer(this).setTextureSize(p_i1164_3_, p_i1164_4_)).setRotationPoint(0.0f, 0.0f + p_i1164_2_, 0.0f);
        this.villagerHead.setTextureOffset(0, 0).addBox(-4.0f, -10.0f, -4.0f, 8, 10, 8, p_i1164_1_);
        (this.villagerNose = new ModelRenderer(this).setTextureSize(p_i1164_3_, p_i1164_4_)).setRotationPoint(0.0f, p_i1164_2_ - 2.0f, 0.0f);
        this.villagerNose.setTextureOffset(24, 0).addBox(-1.0f, -1.0f, -6.0f, 2, 4, 2, p_i1164_1_);
        this.villagerHead.addChild(this.villagerNose);
        (this.villagerBody = new ModelRenderer(this).setTextureSize(p_i1164_3_, p_i1164_4_)).setRotationPoint(0.0f, 0.0f + p_i1164_2_, 0.0f);
        this.villagerBody.setTextureOffset(16, 20).addBox(-4.0f, 0.0f, -3.0f, 8, 12, 6, p_i1164_1_);
        this.villagerBody.setTextureOffset(0, 38).addBox(-4.0f, 0.0f, -3.0f, 8, 18, 6, p_i1164_1_ + 0.5f);
        (this.villagerArms = new ModelRenderer(this).setTextureSize(p_i1164_3_, p_i1164_4_)).setRotationPoint(0.0f, 0.0f + p_i1164_2_ + 2.0f, 0.0f);
        this.villagerArms.setTextureOffset(44, 22).addBox(-8.0f, -2.0f, -2.0f, 4, 8, 4, p_i1164_1_);
        this.villagerArms.setTextureOffset(44, 22).addBox(4.0f, -2.0f, -2.0f, 4, 8, 4, p_i1164_1_);
        this.villagerArms.setTextureOffset(40, 38).addBox(-4.0f, 2.0f, -2.0f, 8, 4, 4, p_i1164_1_);
        (this.rightVillagerLeg = new ModelRenderer(this, 0, 22).setTextureSize(p_i1164_3_, p_i1164_4_)).setRotationPoint(-2.0f, 12.0f + p_i1164_2_, 0.0f);
        this.rightVillagerLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i1164_1_);
        this.leftVillagerLeg = new ModelRenderer(this, 0, 22).setTextureSize(p_i1164_3_, p_i1164_4_);
        this.leftVillagerLeg.mirror = true;
        this.leftVillagerLeg.setRotationPoint(2.0f, 12.0f + p_i1164_2_, 0.0f);
        this.leftVillagerLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, p_i1164_1_);
    }
    
    @Override
    public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        this.villagerHead.render(scale);
        this.villagerBody.render(scale);
        this.rightVillagerLeg.render(scale);
        this.leftVillagerLeg.render(scale);
        this.villagerArms.render(scale);
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        this.villagerHead.rotateAngleY = netHeadYaw / 57.295776f;
        this.villagerHead.rotateAngleX = headPitch / 57.295776f;
        this.villagerArms.rotationPointY = 3.0f;
        this.villagerArms.rotationPointZ = -1.0f;
        this.villagerArms.rotateAngleX = -0.75f;
        this.rightVillagerLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount * 0.5f;
        this.leftVillagerLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 1.4f * limbSwingAmount * 0.5f;
        this.rightVillagerLeg.rotateAngleY = 0.0f;
        this.leftVillagerLeg.rotateAngleY = 0.0f;
    }
}
