package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;

public class ModelIronGolem extends ModelBase {
   public ModelRenderer ironGolemLeftArm;
   public ModelRenderer ironGolemRightLeg;
   public ModelRenderer ironGolemHead;
   public ModelRenderer ironGolemBody;
   public ModelRenderer ironGolemLeftLeg;
   public ModelRenderer ironGolemRightArm;

   public ModelIronGolem(float var1) {
      this(var1, -7.0F);
   }

   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      this.ironGolemHead.rotateAngleY = var4 / 57.295776F;
      this.ironGolemHead.rotateAngleX = var5 / 57.295776F;
      this.ironGolemLeftLeg.rotateAngleX = -1.5F * this.func_78172_a(var1, 13.0F) * var2;
      this.ironGolemRightLeg.rotateAngleX = 1.5F * this.func_78172_a(var1, 13.0F) * var2;
      this.ironGolemLeftLeg.rotateAngleY = 0.0F;
      this.ironGolemRightLeg.rotateAngleY = 0.0F;
   }

   public ModelIronGolem() {
      this(0.0F);
   }

   private float func_78172_a(float var1, float var2) {
      return (Math.abs(var1 % var2 - var2 * 0.5F) - var2 * 0.25F) / (var2 * 0.25F);
   }

   public void setLivingAnimations(EntityLivingBase var1, float var2, float var3, float var4) {
      EntityIronGolem var5 = (EntityIronGolem)var1;
      int var6 = var5.getAttackTimer();
      if (var6 > 0) {
         this.ironGolemRightArm.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float)var6 - var4, 10.0F);
         this.ironGolemLeftArm.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float)var6 - var4, 10.0F);
      } else {
         int var7 = var5.getHoldRoseTick();
         if (var7 > 0) {
            this.ironGolemRightArm.rotateAngleX = -0.8F + 0.025F * this.func_78172_a((float)var7, 70.0F);
            this.ironGolemLeftArm.rotateAngleX = 0.0F;
         } else {
            this.ironGolemRightArm.rotateAngleX = (-0.2F + 1.5F * this.func_78172_a(var2, 13.0F)) * var3;
            this.ironGolemLeftArm.rotateAngleX = (-0.2F - 1.5F * this.func_78172_a(var2, 13.0F)) * var3;
         }
      }

   }

   public ModelIronGolem(float var1, float var2) {
      short var3 = 128;
      short var4 = 128;
      this.ironGolemHead = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.ironGolemHead.setRotationPoint(0.0F, 0.0F + var2, -2.0F);
      this.ironGolemHead.setTextureOffset(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8, 10, 8, var1);
      this.ironGolemHead.setTextureOffset(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2, 4, 2, var1);
      this.ironGolemBody = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.ironGolemBody.setRotationPoint(0.0F, 0.0F + var2, 0.0F);
      this.ironGolemBody.setTextureOffset(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18, 12, 11, var1);
      this.ironGolemBody.setTextureOffset(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9, 5, 6, var1 + 0.5F);
      this.ironGolemRightArm = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.ironGolemRightArm.setRotationPoint(0.0F, -7.0F, 0.0F);
      this.ironGolemRightArm.setTextureOffset(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4, 30, 6, var1);
      this.ironGolemLeftArm = (new ModelRenderer(this)).setTextureSize(var3, var4);
      this.ironGolemLeftArm.setRotationPoint(0.0F, -7.0F, 0.0F);
      this.ironGolemLeftArm.setTextureOffset(60, 58).addBox(9.0F, -2.5F, -3.0F, 4, 30, 6, var1);
      this.ironGolemLeftLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(var3, var4);
      this.ironGolemLeftLeg.setRotationPoint(-4.0F, 18.0F + var2, 0.0F);
      this.ironGolemLeftLeg.setTextureOffset(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, var1);
      this.ironGolemRightLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(var3, var4);
      this.ironGolemRightLeg.mirror = true;
      this.ironGolemRightLeg.setTextureOffset(60, 0).setRotationPoint(5.0F, 18.0F + var2, 0.0F);
      this.ironGolemRightLeg.addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, var1);
   }

   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
      this.ironGolemHead.render(var7);
      this.ironGolemBody.render(var7);
      this.ironGolemLeftLeg.render(var7);
      this.ironGolemRightLeg.render(var7);
      this.ironGolemRightArm.render(var7);
      this.ironGolemLeftArm.render(var7);
   }
}
