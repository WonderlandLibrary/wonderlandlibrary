package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderBat extends RenderLiving {
   private static final ResourceLocation batTextures = new ResourceLocation("textures/entity/bat.png");

   protected void rotateCorpse(EntityBat var1, float var2, float var3, float var4) {
      if (!var1.getIsBatHanging()) {
         GlStateManager.translate(0.0F, MathHelper.cos(var2 * 0.3F) * 0.1F, 0.0F);
      } else {
         GlStateManager.translate(0.0F, -0.1F, 0.0F);
      }

      super.rotateCorpse(var1, var2, var3, var4);
   }

   protected void rotateCorpse(EntityLivingBase var1, float var2, float var3, float var4) {
      this.rotateCorpse((EntityBat)var1, var2, var3, var4);
   }

   protected ResourceLocation getEntityTexture(EntityBat var1) {
      return batTextures;
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityBat)var1);
   }

   protected void preRenderCallback(EntityBat var1, float var2) {
      GlStateManager.scale(0.35F, 0.35F, 0.35F);
   }

   protected void preRenderCallback(EntityLivingBase var1, float var2) {
      this.preRenderCallback((EntityBat)var1, var2);
   }

   public RenderBat(RenderManager var1) {
      super(var1, new ModelBat(), 0.25F);
   }
}
