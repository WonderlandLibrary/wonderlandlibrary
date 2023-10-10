package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelHumanoidHead extends ModelSkeletonHead {
   private final ModelRenderer head = new ModelRenderer(this, 32, 0);

   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(var1, var2, var3, var4, var5, var6, var7);
      this.head.rotateAngleY = this.skeletonHead.rotateAngleY;
      this.head.rotateAngleX = this.skeletonHead.rotateAngleX;
   }

   public ModelHumanoidHead() {
      super(0, 0, 64, 64);
      this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.25F);
      this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
   }

   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      super.render(var1, var2, var3, var4, var5, var6, var7);
      this.head.render(var7);
   }
}
