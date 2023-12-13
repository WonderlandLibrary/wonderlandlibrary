package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelSnowMan extends ModelBase {
   public ModelRenderer bottomBody;
   public ModelRenderer rightHand;
   public ModelRenderer head;
   public ModelRenderer body;
   public ModelRenderer leftHand;

   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
      this.body.render(var7);
      this.bottomBody.render(var7);
      this.head.render(var7);
      this.rightHand.render(var7);
      this.leftHand.render(var7);
   }

   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(var1, var2, var3, var4, var5, var6, var7);
      this.head.rotateAngleY = var4 / 57.295776F;
      this.head.rotateAngleX = var5 / 57.295776F;
      this.body.rotateAngleY = var4 / 57.295776F * 0.25F;
      float var8 = MathHelper.sin(this.body.rotateAngleY);
      float var9 = MathHelper.cos(this.body.rotateAngleY);
      this.rightHand.rotateAngleZ = 1.0F;
      this.leftHand.rotateAngleZ = -1.0F;
      this.rightHand.rotateAngleY = 0.0F + this.body.rotateAngleY;
      this.leftHand.rotateAngleY = 3.1415927F + this.body.rotateAngleY;
      this.rightHand.rotationPointX = var9 * 5.0F;
      this.rightHand.rotationPointZ = -var8 * 5.0F;
      this.leftHand.rotationPointX = -var9 * 5.0F;
      this.leftHand.rotationPointZ = var8 * 5.0F;
   }

   public ModelSnowMan() {
      float var1 = 4.0F;
      float var2 = 0.0F;
      this.head = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
      this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, var2 - 0.5F);
      this.head.setRotationPoint(0.0F, 0.0F + var1, 0.0F);
      this.rightHand = (new ModelRenderer(this, 32, 0)).setTextureSize(64, 64);
      this.rightHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2, var2 - 0.5F);
      this.rightHand.setRotationPoint(0.0F, 0.0F + var1 + 9.0F - 7.0F, 0.0F);
      this.leftHand = (new ModelRenderer(this, 32, 0)).setTextureSize(64, 64);
      this.leftHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2, var2 - 0.5F);
      this.leftHand.setRotationPoint(0.0F, 0.0F + var1 + 9.0F - 7.0F, 0.0F);
      this.body = (new ModelRenderer(this, 0, 16)).setTextureSize(64, 64);
      this.body.addBox(-5.0F, -10.0F, -5.0F, 10, 10, 10, var2 - 0.5F);
      this.body.setRotationPoint(0.0F, 0.0F + var1 + 9.0F, 0.0F);
      this.bottomBody = (new ModelRenderer(this, 0, 36)).setTextureSize(64, 64);
      this.bottomBody.addBox(-6.0F, -12.0F, -6.0F, 12, 12, 12, var2 - 0.5F);
      this.bottomBody.setRotationPoint(0.0F, 0.0F + var1 + 20.0F, 0.0F);
   }
}
