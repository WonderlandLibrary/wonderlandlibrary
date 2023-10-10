package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;

public class LayerDeadmau5Head implements LayerRenderer {
   private final RenderPlayer playerRenderer;

   public void doRenderLayer(AbstractClientPlayer var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (var1.getName().equals("deadmau5") && var1.hasSkin() && !var1.isInvisible()) {
         this.playerRenderer.bindTexture(var1.getLocationSkin());

         for(int var9 = 0; var9 < 2; ++var9) {
            float var10 = var1.prevRotationYaw + (var1.rotationYaw - var1.prevRotationYaw) * var4 - (var1.prevRenderYawOffset + (var1.renderYawOffset - var1.prevRenderYawOffset) * var4);
            float var11 = var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var4;
            GlStateManager.pushMatrix();
            GlStateManager.rotate(var10, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(var11, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(0.375F * (float)(var9 * 2 - 1), 0.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.375F, 0.0F);
            GlStateManager.rotate(-var11, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-var10, 0.0F, 1.0F, 0.0F);
            float var12 = 1.3333334F;
            GlStateManager.scale(var12, var12, var12);
            this.playerRenderer.getMainModel().renderDeadmau5Head(0.0625F);
            GlStateManager.popMatrix();
         }
      }

   }

   public LayerDeadmau5Head(RenderPlayer var1) {
      this.playerRenderer = var1;
   }

   public boolean shouldCombineTextures() {
      return true;
   }

   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.doRenderLayer((AbstractClientPlayer)var1, var2, var3, var4, var5, var6, var7, var8);
   }
}
