package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelSlime extends ModelBase {
   ModelRenderer slimeLeftEye;
   ModelRenderer slimeBodies;
   ModelRenderer slimeMouth;
   ModelRenderer slimeRightEye;

   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
      this.slimeBodies.render(var7);
      if (this.slimeRightEye != null) {
         this.slimeRightEye.render(var7);
         this.slimeLeftEye.render(var7);
         this.slimeMouth.render(var7);
      }

   }

   public ModelSlime(int var1) {
      this.slimeBodies = new ModelRenderer(this, 0, var1);
      this.slimeBodies.addBox(-4.0F, 16.0F, -4.0F, 8, 8, 8);
      if (var1 > 0) {
         this.slimeBodies = new ModelRenderer(this, 0, var1);
         this.slimeBodies.addBox(-3.0F, 17.0F, -3.0F, 6, 6, 6);
         this.slimeRightEye = new ModelRenderer(this, 32, 0);
         this.slimeRightEye.addBox(-3.25F, 18.0F, -3.5F, 2, 2, 2);
         this.slimeLeftEye = new ModelRenderer(this, 32, 4);
         this.slimeLeftEye.addBox(1.25F, 18.0F, -3.5F, 2, 2, 2);
         this.slimeMouth = new ModelRenderer(this, 32, 8);
         this.slimeMouth.addBox(0.0F, 21.0F, -3.5F, 1, 1, 1);
      }

   }
}
