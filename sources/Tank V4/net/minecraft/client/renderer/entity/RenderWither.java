package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerWitherAura;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.ResourceLocation;

public class RenderWither extends RenderLiving {
   private static final ResourceLocation invulnerableWitherTextures = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
   private static final ResourceLocation witherTextures = new ResourceLocation("textures/entity/wither/wither.png");

   public void doRender(EntityWither var1, double var2, double var4, double var6, float var8, float var9) {
      BossStatus.setBossStatus(var1, true);
      super.doRender((EntityLiving)var1, var2, var4, var6, var8, var9);
   }

   public RenderWither(RenderManager var1) {
      super(var1, new ModelWither(0.0F), 1.0F);
      this.addLayer(new LayerWitherAura(this));
   }

   protected void preRenderCallback(EntityWither var1, float var2) {
      float var3 = 2.0F;
      int var4 = var1.getInvulTime();
      if (var4 > 0) {
         var3 -= ((float)var4 - var2) / 220.0F * 0.5F;
      }

      GlStateManager.scale(var3, var3, var3);
   }

   protected ResourceLocation getEntityTexture(EntityWither var1) {
      int var2 = var1.getInvulTime();
      return var2 <= 0 || var2 <= 80 && var2 / 5 % 2 == 1 ? witherTextures : invulnerableWitherTextures;
   }

   protected void preRenderCallback(EntityLivingBase var1, float var2) {
      this.preRenderCallback((EntityWither)var1, var2);
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityWither)var1);
   }

   public void doRender(EntityLiving var1, double var2, double var4, double var6, float var8, float var9) {
      this.doRender((EntityWither)var1, var2, var4, var6, var8, var9);
   }
}
