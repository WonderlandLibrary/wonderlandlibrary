package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerCreeperCharge;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderCreeper extends RenderLiving {
   private static final ResourceLocation creeperTextures = new ResourceLocation("textures/entity/creeper/creeper.png");

   protected ResourceLocation getEntityTexture(EntityCreeper var1) {
      return creeperTextures;
   }

   protected void preRenderCallback(EntityCreeper var1, float var2) {
      float var3 = var1.getCreeperFlashIntensity(var2);
      float var4 = 1.0F + MathHelper.sin(var3 * 100.0F) * var3 * 0.01F;
      var3 = MathHelper.clamp_float(var3, 0.0F, 1.0F);
      var3 *= var3;
      var3 *= var3;
      float var5 = (1.0F + var3 * 0.4F) * var4;
      float var6 = (1.0F + var3 * 0.1F) / var4;
      GlStateManager.scale(var5, var6, var5);
   }

   protected int getColorMultiplier(EntityCreeper var1, float var2, float var3) {
      float var4 = var1.getCreeperFlashIntensity(var3);
      if ((int)(var4 * 10.0F) % 2 == 0) {
         return 0;
      } else {
         int var5 = (int)(var4 * 0.2F * 255.0F);
         var5 = MathHelper.clamp_int(var5, 0, 255);
         return var5 << 24 | 16777215;
      }
   }

   protected int getColorMultiplier(EntityLivingBase var1, float var2, float var3) {
      return this.getColorMultiplier((EntityCreeper)var1, var2, var3);
   }

   protected void preRenderCallback(EntityLivingBase var1, float var2) {
      this.preRenderCallback((EntityCreeper)var1, var2);
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityCreeper)var1);
   }

   public RenderCreeper(RenderManager var1) {
      super(var1, new ModelCreeper(), 0.5F);
      this.addLayer(new LayerCreeperCharge(this));
   }
}
