package net.minecraft.client.renderer.entity.layers;

import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;

public class LayerEnderDragonDeath implements LayerRenderer {
   public boolean shouldCombineTextures() {
      return false;
   }

   public void doRenderLayer(EntityDragon var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (var1.deathTicks > 0) {
         Tessellator var9 = Tessellator.getInstance();
         WorldRenderer var10 = var9.getWorldRenderer();
         RenderHelper.disableStandardItemLighting();
         float var11 = ((float)var1.deathTicks + var4) / 200.0F;
         float var12 = 0.0F;
         if (var11 > 0.8F) {
            var12 = (var11 - 0.8F) / 0.2F;
         }

         Random var13 = new Random(432L);
         GlStateManager.disableTexture2D();
         GlStateManager.shadeModel(7425);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 1);
         GlStateManager.disableAlpha();
         GlStateManager.enableCull();
         GlStateManager.depthMask(false);
         GlStateManager.pushMatrix();
         GlStateManager.translate(0.0F, -1.0F, -2.0F);

         for(int var14 = 0; (float)var14 < (var11 + var11 * var11) / 2.0F * 60.0F; ++var14) {
            GlStateManager.rotate(var13.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(var13.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(var13.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(var13.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(var13.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(var13.nextFloat() * 360.0F + var11 * 90.0F, 0.0F, 0.0F, 1.0F);
            float var15 = var13.nextFloat() * 20.0F + 5.0F + var12 * 10.0F;
            float var16 = var13.nextFloat() * 2.0F + 1.0F + var12 * 2.0F;
            var10.begin(6, DefaultVertexFormats.POSITION_COLOR);
            var10.pos(0.0D, 0.0D, 0.0D).color(255, 255, 255, (int)(255.0F * (1.0F - var12))).endVertex();
            var10.pos(-0.866D * (double)var16, (double)var15, (double)(-0.5F * var16)).color(255, 0, 255, 0).endVertex();
            var10.pos(0.866D * (double)var16, (double)var15, (double)(-0.5F * var16)).color(255, 0, 255, 0).endVertex();
            var10.pos(0.0D, (double)var15, (double)(1.0F * var16)).color(255, 0, 255, 0).endVertex();
            var10.pos(-0.866D * (double)var16, (double)var15, (double)(-0.5F * var16)).color(255, 0, 255, 0).endVertex();
            var9.draw();
         }

         GlStateManager.popMatrix();
         GlStateManager.depthMask(true);
         GlStateManager.disableCull();
         GlStateManager.disableBlend();
         GlStateManager.shadeModel(7424);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.enableTexture2D();
         GlStateManager.enableAlpha();
         RenderHelper.enableStandardItemLighting();
      }

   }

   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.doRenderLayer((EntityDragon)var1, var2, var3, var4, var5, var6, var7, var8);
   }
}
