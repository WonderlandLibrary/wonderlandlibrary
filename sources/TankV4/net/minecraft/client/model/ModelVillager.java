package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelVillager extends ModelBase {
   public ModelRenderer villagerArms;
   public ModelRenderer villagerHead;
   public ModelRenderer rightVillagerLeg;
   public ModelRenderer villagerNose;
   public ModelRenderer villagerBody;
   public ModelRenderer leftVillagerLeg;

   public ModelVillager(float var1, float var2, int var3, int var4) {
      this.villagerHead = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.villagerHead.setRotationPoint(0.0F, 0.0F + var2, 0.0F);
      this.villagerHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, var1);
      this.villagerNose = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.villagerNose.setRotationPoint(0.0F, var2 - 2.0F, 0.0F);
      this.villagerNose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, var1);
      this.villagerHead.addChild(this.villagerNose);
      this.villagerBody = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.villagerBody.setRotationPoint(0.0F, 0.0F + var2, 0.0F);
      this.villagerBody.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, var1);
      this.villagerBody.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, var1 + 0.5F);
      this.villagerArms = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.villagerArms.setRotationPoint(0.0F, 0.0F + var2 + 2.0F, 0.0F);
      this.villagerArms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, var1);
      this.villagerArms.setTextureOffset(44, 22).addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, var1);
      this.villagerArms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8, 4, 4, var1);
      this.rightVillagerLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(var3, var4);
      this.rightVillagerLeg.setRotationPoint(-2.0F, 12.0F + var2, 0.0F);
      this.rightVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, var1);
      this.leftVillagerLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(var3, var4);
      this.leftVillagerLeg.mirror = true;
      this.leftVillagerLeg.setRotationPoint(2.0F, 12.0F + var2, 0.0F);
      this.leftVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, var1);
   }

   public ModelVillager(float var1) {
      this(var1, 0.0F, 64, 64);
   }

   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      this.villagerHead.rotateAngleY = var4 / 57.295776F;
      this.villagerHead.rotateAngleX = var5 / 57.295776F;
      this.villagerArms.rotationPointY = 3.0F;
      this.villagerArms.rotationPointZ = -1.0F;
      this.villagerArms.rotateAngleX = -0.75F;
      this.rightVillagerLeg.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2 * 0.5F;
      this.leftVillagerLeg.rotateAngleX = MathHelper.cos(var1 * 0.6662F + 3.1415927F) * 1.4F * var2 * 0.5F;
      this.rightVillagerLeg.rotateAngleY = 0.0F;
      this.leftVillagerLeg.rotateAngleY = 0.0F;
   }

   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
      this.villagerHead.render(var7);
      this.villagerBody.render(var7);
      this.rightVillagerLeg.render(var7);
      this.leftVillagerLeg.render(var7);
      this.villagerArms.render(var7);
   }
}
