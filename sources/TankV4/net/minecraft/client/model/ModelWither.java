package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.MathHelper;

public class ModelWither extends ModelBase {
   private ModelRenderer[] field_82904_b;
   private ModelRenderer[] field_82905_a;

   public void setLivingAnimations(EntityLivingBase var1, float var2, float var3, float var4) {
      EntityWither var5 = (EntityWither)var1;

      for(int var6 = 1; var6 < 3; ++var6) {
         this.field_82904_b[var6].rotateAngleY = (var5.func_82207_a(var6 - 1) - var1.renderYawOffset) / 57.295776F;
         this.field_82904_b[var6].rotateAngleX = var5.func_82210_r(var6 - 1) / 57.295776F;
      }

   }

   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
      ModelRenderer[] var11;
      int var10 = (var11 = this.field_82904_b).length;

      ModelRenderer var8;
      int var9;
      for(var9 = 0; var9 < var10; ++var9) {
         var8 = var11[var9];
         var8.render(var7);
      }

      var10 = (var11 = this.field_82905_a).length;

      for(var9 = 0; var9 < var10; ++var9) {
         var8 = var11[var9];
         var8.render(var7);
      }

   }

   public ModelWither(float var1) {
      this.textureWidth = 64;
      this.textureHeight = 64;
      this.field_82905_a = new ModelRenderer[3];
      this.field_82905_a[0] = new ModelRenderer(this, 0, 16);
      this.field_82905_a[0].addBox(-10.0F, 3.9F, -0.5F, 20, 3, 3, var1);
      this.field_82905_a[1] = (new ModelRenderer(this)).setTextureSize(this.textureWidth, this.textureHeight);
      this.field_82905_a[1].setRotationPoint(-2.0F, 6.9F, -0.5F);
      this.field_82905_a[1].setTextureOffset(0, 22).addBox(0.0F, 0.0F, 0.0F, 3, 10, 3, var1);
      this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 1.5F, 0.5F, 11, 2, 2, var1);
      this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 4.0F, 0.5F, 11, 2, 2, var1);
      this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 6.5F, 0.5F, 11, 2, 2, var1);
      this.field_82905_a[2] = new ModelRenderer(this, 12, 22);
      this.field_82905_a[2].addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, var1);
      this.field_82904_b = new ModelRenderer[3];
      this.field_82904_b[0] = new ModelRenderer(this, 0, 0);
      this.field_82904_b[0].addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, var1);
      this.field_82904_b[1] = new ModelRenderer(this, 32, 0);
      this.field_82904_b[1].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, var1);
      this.field_82904_b[1].rotationPointX = -8.0F;
      this.field_82904_b[1].rotationPointY = 4.0F;
      this.field_82904_b[2] = new ModelRenderer(this, 32, 0);
      this.field_82904_b[2].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, var1);
      this.field_82904_b[2].rotationPointX = 10.0F;
      this.field_82904_b[2].rotationPointY = 4.0F;
   }

   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      float var8 = MathHelper.cos(var3 * 0.1F);
      this.field_82905_a[1].rotateAngleX = (0.065F + 0.05F * var8) * 3.1415927F;
      this.field_82905_a[2].setRotationPoint(-2.0F, 6.9F + MathHelper.cos(this.field_82905_a[1].rotateAngleX) * 10.0F, -0.5F + MathHelper.sin(this.field_82905_a[1].rotateAngleX) * 10.0F);
      this.field_82905_a[2].rotateAngleX = (0.265F + 0.1F * var8) * 3.1415927F;
      this.field_82904_b[0].rotateAngleY = var4 / 57.295776F;
      this.field_82904_b[0].rotateAngleX = var5 / 57.295776F;
   }
}
