package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelCreeper extends ModelBase {
   public ModelRenderer leg3;
   public ModelRenderer leg1;
   public ModelRenderer creeperArmor;
   public ModelRenderer body;
   public ModelRenderer head;
   public ModelRenderer leg2;
   public ModelRenderer leg4;

   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      this.head.rotateAngleY = var4 / 57.295776F;
      this.head.rotateAngleX = var5 / 57.295776F;
      this.leg1.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
      this.leg2.rotateAngleX = MathHelper.cos(var1 * 0.6662F + 3.1415927F) * 1.4F * var2;
      this.leg3.rotateAngleX = MathHelper.cos(var1 * 0.6662F + 3.1415927F) * 1.4F * var2;
      this.leg4.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
   }

   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
      this.head.render(var7);
      this.body.render(var7);
      this.leg1.render(var7);
      this.leg2.render(var7);
      this.leg3.render(var7);
      this.leg4.render(var7);
   }

   public ModelCreeper() {
      this(0.0F);
   }

   public ModelCreeper(float var1) {
      byte var2 = 6;
      this.head = new ModelRenderer(this, 0, 0);
      this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, var1);
      this.head.setRotationPoint(0.0F, (float)var2, 0.0F);
      this.creeperArmor = new ModelRenderer(this, 32, 0);
      this.creeperArmor.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, var1 + 0.5F);
      this.creeperArmor.setRotationPoint(0.0F, (float)var2, 0.0F);
      this.body = new ModelRenderer(this, 16, 16);
      this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, var1);
      this.body.setRotationPoint(0.0F, (float)var2, 0.0F);
      this.leg1 = new ModelRenderer(this, 0, 16);
      this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, var1);
      this.leg1.setRotationPoint(-2.0F, (float)(12 + var2), 4.0F);
      this.leg2 = new ModelRenderer(this, 0, 16);
      this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, var1);
      this.leg2.setRotationPoint(2.0F, (float)(12 + var2), 4.0F);
      this.leg3 = new ModelRenderer(this, 0, 16);
      this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, var1);
      this.leg3.setRotationPoint(-2.0F, (float)(12 + var2), -4.0F);
      this.leg4 = new ModelRenderer(this, 0, 16);
      this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, var1);
      this.leg4.setRotationPoint(2.0F, (float)(12 + var2), -4.0F);
   }
}
