package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMagmaCube;

public class ModelMagmaCube extends ModelBase {
   ModelRenderer[] segments = new ModelRenderer[8];
   ModelRenderer core;

   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
      this.core.render(var7);

      for(int var8 = 0; var8 < this.segments.length; ++var8) {
         this.segments[var8].render(var7);
      }

   }

   public void setLivingAnimations(EntityLivingBase var1, float var2, float var3, float var4) {
      EntityMagmaCube var5 = (EntityMagmaCube)var1;
      float var6 = var5.prevSquishFactor + (var5.squishFactor - var5.prevSquishFactor) * var4;
      if (var6 < 0.0F) {
         var6 = 0.0F;
      }

      for(int var7 = 0; var7 < this.segments.length; ++var7) {
         this.segments[var7].rotationPointY = (float)(-(4 - var7)) * var6 * 1.7F;
      }

   }

   public ModelMagmaCube() {
      for(int var1 = 0; var1 < this.segments.length; ++var1) {
         byte var2 = 0;
         int var3 = var1;
         if (var1 == 2) {
            var2 = 24;
            var3 = 10;
         } else if (var1 == 3) {
            var2 = 24;
            var3 = 19;
         }

         this.segments[var1] = new ModelRenderer(this, var2, var3);
         this.segments[var1].addBox(-4.0F, (float)(16 + var1), -4.0F, 8, 1, 8);
      }

      this.core = new ModelRenderer(this, 0, 16);
      this.core.addBox(-2.0F, 18.0F, -2.0F, 4, 4, 4);
   }
}
