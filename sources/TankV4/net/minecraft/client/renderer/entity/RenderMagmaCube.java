package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelMagmaCube;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.util.ResourceLocation;

public class RenderMagmaCube extends RenderLiving {
   private static final ResourceLocation magmaCubeTextures = new ResourceLocation("textures/entity/slime/magmacube.png");

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityMagmaCube)var1);
   }

   protected ResourceLocation getEntityTexture(EntityMagmaCube var1) {
      return magmaCubeTextures;
   }

   public RenderMagmaCube(RenderManager var1) {
      super(var1, new ModelMagmaCube(), 0.25F);
   }

   protected void preRenderCallback(EntityMagmaCube var1, float var2) {
      int var3 = var1.getSlimeSize();
      float var4 = (var1.prevSquishFactor + (var1.squishFactor - var1.prevSquishFactor) * var2) / ((float)var3 * 0.5F + 1.0F);
      float var5 = 1.0F / (var4 + 1.0F);
      float var6 = (float)var3;
      GlStateManager.scale(var5 * var6, 1.0F / var5 * var6, var5 * var6);
   }

   protected void preRenderCallback(EntityLivingBase var1, float var2) {
      this.preRenderCallback((EntityMagmaCube)var1, var2);
   }
}
