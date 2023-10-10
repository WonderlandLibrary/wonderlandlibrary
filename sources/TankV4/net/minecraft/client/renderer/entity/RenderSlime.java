package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerSlimeGel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;

public class RenderSlime extends RenderLiving {
   private static final ResourceLocation slimeTextures = new ResourceLocation("textures/entity/slime/slime.png");

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntitySlime)var1);
   }

   public void doRender(EntitySlime var1, double var2, double var4, double var6, float var8, float var9) {
      this.shadowSize = 0.25F * (float)var1.getSlimeSize();
      super.doRender((EntityLiving)var1, var2, var4, var6, var8, var9);
   }

   public void doRender(EntityLiving var1, double var2, double var4, double var6, float var8, float var9) {
      this.doRender((EntitySlime)var1, var2, var4, var6, var8, var9);
   }

   protected void preRenderCallback(EntityLivingBase var1, float var2) {
      this.preRenderCallback((EntitySlime)var1, var2);
   }

   protected void preRenderCallback(EntitySlime var1, float var2) {
      float var3 = (float)var1.getSlimeSize();
      float var4 = (var1.prevSquishFactor + (var1.squishFactor - var1.prevSquishFactor) * var2) / (var3 * 0.5F + 1.0F);
      float var5 = 1.0F / (var4 + 1.0F);
      GlStateManager.scale(var5 * var3, 1.0F / var5 * var3, var5 * var3);
   }

   protected ResourceLocation getEntityTexture(EntitySlime var1) {
      return slimeTextures;
   }

   public RenderSlime(RenderManager var1, ModelBase var2, float var3) {
      super(var1, var2, var3);
      this.addLayer(new LayerSlimeGel(this));
   }
}
