package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelSkeletonHead extends ModelBase {
   public ModelRenderer skeletonHead;

   public ModelSkeletonHead() {
      this(0, 35, 64, 64);
   }

   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
      this.skeletonHead.render(var7);
   }

   public ModelSkeletonHead(int var1, int var2, int var3, int var4) {
      this.textureWidth = var3;
      this.textureHeight = var4;
      this.skeletonHead = new ModelRenderer(this, var1, var2);
      this.skeletonHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
      this.skeletonHead.setRotationPoint(0.0F, 0.0F, 0.0F);
   }

   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(var1, var2, var3, var4, var5, var6, var7);
      this.skeletonHead.rotateAngleY = var4 / 57.295776F;
      this.skeletonHead.rotateAngleX = var5 / 57.295776F;
   }
}
