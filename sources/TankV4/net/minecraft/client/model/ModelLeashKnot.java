package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelLeashKnot extends ModelBase {
   public ModelRenderer field_110723_a;

   public ModelLeashKnot() {
      this(0, 0, 32, 32);
   }

   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(var1, var2, var3, var4, var5, var6, var7);
      this.field_110723_a.rotateAngleY = var4 / 57.295776F;
      this.field_110723_a.rotateAngleX = var5 / 57.295776F;
   }

   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
      this.field_110723_a.render(var7);
   }

   public ModelLeashKnot(int var1, int var2, int var3, int var4) {
      this.textureWidth = var3;
      this.textureHeight = var4;
      this.field_110723_a = new ModelRenderer(this, var1, var2);
      this.field_110723_a.addBox(-3.0F, -6.0F, -3.0F, 6, 8, 6, 0.0F);
      this.field_110723_a.setRotationPoint(0.0F, 0.0F, 0.0F);
   }
}
