package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.util.MathHelper;

public class ModelRabbit extends ModelBase {
   private float field_178701_m = 0.0F;
   private float field_178699_n = 0.0F;
   ModelRenderer rabbitLeftThigh;
   ModelRenderer rabbitNose;
   ModelRenderer rabbitRightEar;
   ModelRenderer rabbitBody;
   ModelRenderer rabbitTail;
   ModelRenderer rabbitRightFoot;
   ModelRenderer rabbitLeftArm;
   ModelRenderer rabbitLeftEar;
   ModelRenderer rabbitHead;
   ModelRenderer rabbitRightArm;
   ModelRenderer rabbitRightThigh;
   ModelRenderer rabbitLeftFoot;

   public ModelRabbit() {
      this.setTextureOffset("head.main", 0, 0);
      this.setTextureOffset("head.nose", 0, 24);
      this.setTextureOffset("head.ear1", 0, 10);
      this.setTextureOffset("head.ear2", 6, 10);
      this.rabbitLeftFoot = new ModelRenderer(this, 26, 24);
      this.rabbitLeftFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
      this.rabbitLeftFoot.setRotationPoint(3.0F, 17.5F, 3.7F);
      this.rabbitLeftFoot.mirror = true;
      this.setRotationOffset(this.rabbitLeftFoot, 0.0F, 0.0F, 0.0F);
      this.rabbitRightFoot = new ModelRenderer(this, 8, 24);
      this.rabbitRightFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
      this.rabbitRightFoot.setRotationPoint(-3.0F, 17.5F, 3.7F);
      this.rabbitRightFoot.mirror = true;
      this.setRotationOffset(this.rabbitRightFoot, 0.0F, 0.0F, 0.0F);
      this.rabbitLeftThigh = new ModelRenderer(this, 30, 15);
      this.rabbitLeftThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
      this.rabbitLeftThigh.setRotationPoint(3.0F, 17.5F, 3.7F);
      this.rabbitLeftThigh.mirror = true;
      this.setRotationOffset(this.rabbitLeftThigh, -0.34906584F, 0.0F, 0.0F);
      this.rabbitRightThigh = new ModelRenderer(this, 16, 15);
      this.rabbitRightThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
      this.rabbitRightThigh.setRotationPoint(-3.0F, 17.5F, 3.7F);
      this.rabbitRightThigh.mirror = true;
      this.setRotationOffset(this.rabbitRightThigh, -0.34906584F, 0.0F, 0.0F);
      this.rabbitBody = new ModelRenderer(this, 0, 0);
      this.rabbitBody.addBox(-3.0F, -2.0F, -10.0F, 6, 5, 10);
      this.rabbitBody.setRotationPoint(0.0F, 19.0F, 8.0F);
      this.rabbitBody.mirror = true;
      this.setRotationOffset(this.rabbitBody, -0.34906584F, 0.0F, 0.0F);
      this.rabbitLeftArm = new ModelRenderer(this, 8, 15);
      this.rabbitLeftArm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
      this.rabbitLeftArm.setRotationPoint(3.0F, 17.0F, -1.0F);
      this.rabbitLeftArm.mirror = true;
      this.setRotationOffset(this.rabbitLeftArm, -0.17453292F, 0.0F, 0.0F);
      this.rabbitRightArm = new ModelRenderer(this, 0, 15);
      this.rabbitRightArm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
      this.rabbitRightArm.setRotationPoint(-3.0F, 17.0F, -1.0F);
      this.rabbitRightArm.mirror = true;
      this.setRotationOffset(this.rabbitRightArm, -0.17453292F, 0.0F, 0.0F);
      this.rabbitHead = new ModelRenderer(this, 32, 0);
      this.rabbitHead.addBox(-2.5F, -4.0F, -5.0F, 5, 4, 5);
      this.rabbitHead.setRotationPoint(0.0F, 16.0F, -1.0F);
      this.rabbitHead.mirror = true;
      this.setRotationOffset(this.rabbitHead, 0.0F, 0.0F, 0.0F);
      this.rabbitRightEar = new ModelRenderer(this, 52, 0);
      this.rabbitRightEar.addBox(-2.5F, -9.0F, -1.0F, 2, 5, 1);
      this.rabbitRightEar.setRotationPoint(0.0F, 16.0F, -1.0F);
      this.rabbitRightEar.mirror = true;
      this.setRotationOffset(this.rabbitRightEar, 0.0F, -0.2617994F, 0.0F);
      this.rabbitLeftEar = new ModelRenderer(this, 58, 0);
      this.rabbitLeftEar.addBox(0.5F, -9.0F, -1.0F, 2, 5, 1);
      this.rabbitLeftEar.setRotationPoint(0.0F, 16.0F, -1.0F);
      this.rabbitLeftEar.mirror = true;
      this.setRotationOffset(this.rabbitLeftEar, 0.0F, 0.2617994F, 0.0F);
      this.rabbitTail = new ModelRenderer(this, 52, 6);
      this.rabbitTail.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 2);
      this.rabbitTail.setRotationPoint(0.0F, 20.0F, 7.0F);
      this.rabbitTail.mirror = true;
      this.setRotationOffset(this.rabbitTail, -0.3490659F, 0.0F, 0.0F);
      this.rabbitNose = new ModelRenderer(this, 32, 9);
      this.rabbitNose.addBox(-0.5F, -2.5F, -5.5F, 1, 1, 1);
      this.rabbitNose.setRotationPoint(0.0F, 16.0F, -1.0F);
      this.rabbitNose.mirror = true;
      this.setRotationOffset(this.rabbitNose, 0.0F, 0.0F, 0.0F);
   }

   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      float var8 = var3 - (float)var7.ticksExisted;
      EntityRabbit var9 = (EntityRabbit)var7;
      this.rabbitNose.rotateAngleX = this.rabbitHead.rotateAngleX = this.rabbitRightEar.rotateAngleX = this.rabbitLeftEar.rotateAngleX = var5 * 0.017453292F;
      this.rabbitNose.rotateAngleY = this.rabbitHead.rotateAngleY = var4 * 0.017453292F;
      this.rabbitRightEar.rotateAngleY = this.rabbitNose.rotateAngleY - 0.2617994F;
      this.rabbitLeftEar.rotateAngleY = this.rabbitNose.rotateAngleY + 0.2617994F;
      this.field_178701_m = MathHelper.sin(var9.func_175521_o(var8) * 3.1415927F);
      this.rabbitLeftThigh.rotateAngleX = this.rabbitRightThigh.rotateAngleX = (this.field_178701_m * 50.0F - 21.0F) * 0.017453292F;
      this.rabbitLeftFoot.rotateAngleX = this.rabbitRightFoot.rotateAngleX = this.field_178701_m * 50.0F * 0.017453292F;
      this.rabbitLeftArm.rotateAngleX = this.rabbitRightArm.rotateAngleX = (this.field_178701_m * -40.0F - 11.0F) * 0.017453292F;
   }

   public void setLivingAnimations(EntityLivingBase var1, float var2, float var3, float var4) {
   }

   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
      if (this.isChild) {
         float var8 = 2.0F;
         GlStateManager.pushMatrix();
         GlStateManager.translate(0.0F, 5.0F * var7, 2.0F * var7);
         this.rabbitHead.render(var7);
         this.rabbitLeftEar.render(var7);
         this.rabbitRightEar.render(var7);
         this.rabbitNose.render(var7);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         GlStateManager.scale(1.0F / var8, 1.0F / var8, 1.0F / var8);
         GlStateManager.translate(0.0F, 24.0F * var7, 0.0F);
         this.rabbitLeftFoot.render(var7);
         this.rabbitRightFoot.render(var7);
         this.rabbitLeftThigh.render(var7);
         this.rabbitRightThigh.render(var7);
         this.rabbitBody.render(var7);
         this.rabbitLeftArm.render(var7);
         this.rabbitRightArm.render(var7);
         this.rabbitTail.render(var7);
         GlStateManager.popMatrix();
      } else {
         this.rabbitLeftFoot.render(var7);
         this.rabbitRightFoot.render(var7);
         this.rabbitLeftThigh.render(var7);
         this.rabbitRightThigh.render(var7);
         this.rabbitBody.render(var7);
         this.rabbitLeftArm.render(var7);
         this.rabbitRightArm.render(var7);
         this.rabbitHead.render(var7);
         this.rabbitRightEar.render(var7);
         this.rabbitLeftEar.render(var7);
         this.rabbitTail.render(var7);
         this.rabbitNose.render(var7);
      }

   }

   private void setRotationOffset(ModelRenderer var1, float var2, float var3, float var4) {
      var1.rotateAngleX = var2;
      var1.rotateAngleY = var3;
      var1.rotateAngleZ = var4;
   }
}
