package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelZombieVillager extends ModelBiped {
   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      super.setRotationAngles(var1, var2, var3, var4, var5, var6, var7);
      float var8 = MathHelper.sin(this.swingProgress * 3.1415927F);
      float var9 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.1415927F);
      this.bipedRightArm.rotateAngleZ = 0.0F;
      this.bipedLeftArm.rotateAngleZ = 0.0F;
      this.bipedRightArm.rotateAngleY = -(0.1F - var8 * 0.6F);
      this.bipedLeftArm.rotateAngleY = 0.1F - var8 * 0.6F;
      this.bipedRightArm.rotateAngleX = -1.5707964F;
      this.bipedLeftArm.rotateAngleX = -1.5707964F;
      ModelRenderer var10000 = this.bipedRightArm;
      var10000.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
      var10000 = this.bipedLeftArm;
      var10000.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
      var10000 = this.bipedRightArm;
      var10000.rotateAngleZ += MathHelper.cos(var3 * 0.09F) * 0.05F + 0.05F;
      var10000 = this.bipedLeftArm;
      var10000.rotateAngleZ -= MathHelper.cos(var3 * 0.09F) * 0.05F + 0.05F;
      var10000 = this.bipedRightArm;
      var10000.rotateAngleX += MathHelper.sin(var3 * 0.067F) * 0.05F;
      var10000 = this.bipedLeftArm;
      var10000.rotateAngleX -= MathHelper.sin(var3 * 0.067F) * 0.05F;
   }

   public ModelZombieVillager(float var1, float var2, boolean var3) {
      super(var1, 0.0F, 64, var3 ? 32 : 64);
      if (var3) {
         this.bipedHead = new ModelRenderer(this, 0, 0);
         this.bipedHead.addBox(-4.0F, -10.0F, -4.0F, 8, 8, 8, var1);
         this.bipedHead.setRotationPoint(0.0F, 0.0F + var2, 0.0F);
      } else {
         this.bipedHead = new ModelRenderer(this);
         this.bipedHead.setRotationPoint(0.0F, 0.0F + var2, 0.0F);
         this.bipedHead.setTextureOffset(0, 32).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, var1);
         this.bipedHead.setTextureOffset(24, 32).addBox(-1.0F, -3.0F, -6.0F, 2, 4, 2, var1);
      }

   }

   public ModelZombieVillager() {
      this(0.0F, 0.0F, false);
   }
}
