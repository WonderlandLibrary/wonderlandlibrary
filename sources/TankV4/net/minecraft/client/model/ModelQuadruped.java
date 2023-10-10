package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelQuadruped extends ModelBase {
   public ModelRenderer head = new ModelRenderer(this, 0, 0);
   protected float childZOffset = 4.0F;
   public ModelRenderer leg1;
   public ModelRenderer leg2;
   protected float childYOffset = 8.0F;
   public ModelRenderer leg4;
   public ModelRenderer body;
   public ModelRenderer leg3;

   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
      if (this.isChild) {
         float var8 = 2.0F;
         GlStateManager.pushMatrix();
         GlStateManager.translate(0.0F, this.childYOffset * var7, this.childZOffset * var7);
         this.head.render(var7);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         GlStateManager.scale(1.0F / var8, 1.0F / var8, 1.0F / var8);
         GlStateManager.translate(0.0F, 24.0F * var7, 0.0F);
         this.body.render(var7);
         this.leg1.render(var7);
         this.leg2.render(var7);
         this.leg3.render(var7);
         this.leg4.render(var7);
         GlStateManager.popMatrix();
      } else {
         this.head.render(var7);
         this.body.render(var7);
         this.leg1.render(var7);
         this.leg2.render(var7);
         this.leg3.render(var7);
         this.leg4.render(var7);
      }

   }

   public ModelQuadruped(int var1, float var2) {
      this.head.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, var2);
      this.head.setRotationPoint(0.0F, (float)(18 - var1), -6.0F);
      this.body = new ModelRenderer(this, 28, 8);
      this.body.addBox(-5.0F, -10.0F, -7.0F, 10, 16, 8, var2);
      this.body.setRotationPoint(0.0F, (float)(17 - var1), 2.0F);
      this.leg1 = new ModelRenderer(this, 0, 16);
      this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, var1, 4, var2);
      this.leg1.setRotationPoint(-3.0F, (float)(24 - var1), 7.0F);
      this.leg2 = new ModelRenderer(this, 0, 16);
      this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, var1, 4, var2);
      this.leg2.setRotationPoint(3.0F, (float)(24 - var1), 7.0F);
      this.leg3 = new ModelRenderer(this, 0, 16);
      this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, var1, 4, var2);
      this.leg3.setRotationPoint(-3.0F, (float)(24 - var1), -5.0F);
      this.leg4 = new ModelRenderer(this, 0, 16);
      this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, var1, 4, var2);
      this.leg4.setRotationPoint(3.0F, (float)(24 - var1), -5.0F);
   }

   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      float var8 = 57.295776F;
      this.head.rotateAngleX = var5 / 57.295776F;
      this.head.rotateAngleY = var4 / 57.295776F;
      this.body.rotateAngleX = 1.5707964F;
      this.leg1.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
      this.leg2.rotateAngleX = MathHelper.cos(var1 * 0.6662F + 3.1415927F) * 1.4F * var2;
      this.leg3.rotateAngleX = MathHelper.cos(var1 * 0.6662F + 3.1415927F) * 1.4F * var2;
      this.leg4.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
   }
}
