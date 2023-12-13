package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;

public class LayerCape implements LayerRenderer {
   private static final String __OBFID = "CL_00002425";
   private final RenderPlayer playerRenderer;

   public void doRenderLayer(AbstractClientPlayer var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (var1.hasPlayerInfo() && !var1.isInvisible() && var1.isWearing(EnumPlayerModelParts.CAPE) && var1.getLocationCape() != null) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.playerRenderer.bindTexture(var1.getLocationCape());
         GlStateManager.pushMatrix();
         GlStateManager.translate(0.0F, 0.0F, 0.125F);
         double var9 = var1.prevChasingPosX + (var1.chasingPosX - var1.prevChasingPosX) * (double)var4 - (var1.prevPosX + (var1.posX - var1.prevPosX) * (double)var4);
         double var11 = var1.prevChasingPosY + (var1.chasingPosY - var1.prevChasingPosY) * (double)var4 - (var1.prevPosY + (var1.posY - var1.prevPosY) * (double)var4);
         double var13 = var1.prevChasingPosZ + (var1.chasingPosZ - var1.prevChasingPosZ) * (double)var4 - (var1.prevPosZ + (var1.posZ - var1.prevPosZ) * (double)var4);
         float var15 = var1.prevRenderYawOffset + (var1.renderYawOffset - var1.prevRenderYawOffset) * var4;
         double var16 = (double)MathHelper.sin(var15 * 3.1415927F / 180.0F);
         double var18 = (double)(-MathHelper.cos(var15 * 3.1415927F / 180.0F));
         float var20 = (float)var11 * 10.0F;
         var20 = MathHelper.clamp_float(var20, -6.0F, 32.0F);
         float var21 = (float)(var9 * var16 + var13 * var18) * 100.0F;
         float var22 = (float)(var9 * var18 - var13 * var16) * 100.0F;
         if (var21 < 0.0F) {
            var21 = 0.0F;
         }

         if (var21 > 165.0F) {
            var21 = 165.0F;
         }

         float var23 = var1.prevCameraYaw + (var1.cameraYaw - var1.prevCameraYaw) * var4;
         var20 += MathHelper.sin((var1.prevDistanceWalkedModified + (var1.distanceWalkedModified - var1.prevDistanceWalkedModified) * var4) * 6.0F) * 32.0F * var23;
         if (var1.isSneaking()) {
            var20 += 25.0F;
            GlStateManager.translate(0.0F, 0.142F, -0.0178F);
         }

         GlStateManager.rotate(6.0F + var21 / 2.0F + var20, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(var22 / 2.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate(-var22 / 2.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
         this.playerRenderer.getMainModel().renderCape(0.0625F);
         GlStateManager.popMatrix();
      }

   }

   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.doRenderLayer((AbstractClientPlayer)var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public LayerCape(RenderPlayer var1) {
      this.playerRenderer = var1;
   }

   public boolean shouldCombineTextures() {
      return false;
   }
}
