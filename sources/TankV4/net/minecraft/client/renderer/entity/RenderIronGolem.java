package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerIronGolemFlower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;

public class RenderIronGolem extends RenderLiving {
   private static final ResourceLocation ironGolemTextures = new ResourceLocation("textures/entity/iron_golem.png");

   protected ResourceLocation getEntityTexture(EntityIronGolem var1) {
      return ironGolemTextures;
   }

   protected void rotateCorpse(EntityIronGolem var1, float var2, float var3, float var4) {
      super.rotateCorpse(var1, var2, var3, var4);
      if ((double)var1.limbSwingAmount >= 0.01D) {
         float var5 = 13.0F;
         float var6 = var1.limbSwing - var1.limbSwingAmount * (1.0F - var4) + 6.0F;
         float var7 = (Math.abs(var6 % var5 - var5 * 0.5F) - var5 * 0.25F) / (var5 * 0.25F);
         GlStateManager.rotate(6.5F * var7, 0.0F, 0.0F, 1.0F);
      }

   }

   protected void rotateCorpse(EntityLivingBase var1, float var2, float var3, float var4) {
      this.rotateCorpse((EntityIronGolem)var1, var2, var3, var4);
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityIronGolem)var1);
   }

   public RenderIronGolem(RenderManager var1) {
      super(var1, new ModelIronGolem(), 0.5F);
      this.addLayer(new LayerIronGolemFlower(this));
   }
}
