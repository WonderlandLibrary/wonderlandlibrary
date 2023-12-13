package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelWitch extends ModelVillager {
   private ModelRenderer witchHat;
   private ModelRenderer field_82901_h = (new ModelRenderer(this)).setTextureSize(64, 128);
   public boolean field_82900_g;

   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(var1, var2, var3, var4, var5, var6, var7);
      this.villagerNose.offsetX = this.villagerNose.offsetY = this.villagerNose.offsetZ = 0.0F;
      float var8 = 0.01F * (float)(var7.getEntityId() % 10);
      this.villagerNose.rotateAngleX = MathHelper.sin((float)var7.ticksExisted * var8) * 4.5F * 3.1415927F / 180.0F;
      this.villagerNose.rotateAngleY = 0.0F;
      this.villagerNose.rotateAngleZ = MathHelper.cos((float)var7.ticksExisted * var8) * 2.5F * 3.1415927F / 180.0F;
      if (this.field_82900_g) {
         this.villagerNose.rotateAngleX = -0.9F;
         this.villagerNose.offsetZ = -0.09375F;
         this.villagerNose.offsetY = 0.1875F;
      }

   }

   public ModelWitch(float var1) {
      super(var1, 0.0F, 64, 128);
      this.field_82901_h.setRotationPoint(0.0F, -2.0F, 0.0F);
      this.field_82901_h.setTextureOffset(0, 0).addBox(0.0F, 3.0F, -6.75F, 1, 1, 1, -0.25F);
      this.villagerNose.addChild(this.field_82901_h);
      this.witchHat = (new ModelRenderer(this)).setTextureSize(64, 128);
      this.witchHat.setRotationPoint(-5.0F, -10.03125F, -5.0F);
      this.witchHat.setTextureOffset(0, 64).addBox(0.0F, 0.0F, 0.0F, 10, 2, 10);
      this.villagerHead.addChild(this.witchHat);
      ModelRenderer var2 = (new ModelRenderer(this)).setTextureSize(64, 128);
      var2.setRotationPoint(1.75F, -4.0F, 2.0F);
      var2.setTextureOffset(0, 76).addBox(0.0F, 0.0F, 0.0F, 7, 4, 7);
      var2.rotateAngleX = -0.05235988F;
      var2.rotateAngleZ = 0.02617994F;
      this.witchHat.addChild(var2);
      ModelRenderer var3 = (new ModelRenderer(this)).setTextureSize(64, 128);
      var3.setRotationPoint(1.75F, -4.0F, 2.0F);
      var3.setTextureOffset(0, 87).addBox(0.0F, 0.0F, 0.0F, 4, 4, 4);
      var3.rotateAngleX = -0.10471976F;
      var3.rotateAngleZ = 0.05235988F;
      var2.addChild(var3);
      ModelRenderer var4 = (new ModelRenderer(this)).setTextureSize(64, 128);
      var4.setRotationPoint(1.75F, -2.0F, 2.0F);
      var4.setTextureOffset(0, 95).addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.25F);
      var4.rotateAngleX = -0.20943952F;
      var4.rotateAngleZ = 0.10471976F;
      var3.addChild(var4);
   }
}
