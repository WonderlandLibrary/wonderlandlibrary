package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderTNTPrimed extends Render {
   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityTNTPrimed)var1);
   }

   public RenderTNTPrimed(RenderManager var1) {
      super(var1);
      this.shadowSize = 0.5F;
   }

   public void doRender(EntityTNTPrimed var1, double var2, double var4, double var6, float var8, float var9) {
      BlockRendererDispatcher var10 = Minecraft.getMinecraft().getBlockRendererDispatcher();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var2, (float)var4 + 0.5F, (float)var6);
      float var11;
      if ((float)var1.fuse - var9 + 1.0F < 10.0F) {
         var11 = 1.0F - ((float)var1.fuse - var9 + 1.0F) / 10.0F;
         var11 = MathHelper.clamp_float(var11, 0.0F, 1.0F);
         var11 *= var11;
         var11 *= var11;
         float var12 = 1.0F + var11 * 0.3F;
         GlStateManager.scale(var12, var12, var12);
      }

      var11 = (1.0F - ((float)var1.fuse - var9 + 1.0F) / 100.0F) * 0.8F;
      this.bindEntityTexture(var1);
      GlStateManager.translate(-0.5F, -0.5F, 0.5F);
      var10.renderBlockBrightness(Blocks.tnt.getDefaultState(), var1.getBrightness(var9));
      GlStateManager.translate(0.0F, 0.0F, 1.0F);
      if (var1.fuse / 5 % 2 == 0) {
         GlStateManager.disableTexture2D();
         GlStateManager.disableLighting();
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 772);
         GlStateManager.color(1.0F, 1.0F, 1.0F, var11);
         GlStateManager.doPolygonOffset(-3.0F, -3.0F);
         GlStateManager.enablePolygonOffset();
         var10.renderBlockBrightness(Blocks.tnt.getDefaultState(), 1.0F);
         GlStateManager.doPolygonOffset(0.0F, 0.0F);
         GlStateManager.disablePolygonOffset();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.disableBlend();
         GlStateManager.enableLighting();
         GlStateManager.enableTexture2D();
      }

      GlStateManager.popMatrix();
      super.doRender(var1, var2, var4, var6, var8, var9);
   }

   protected ResourceLocation getEntityTexture(EntityTNTPrimed var1) {
      return TextureMap.locationBlocksTexture;
   }

   public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      this.doRender((EntityTNTPrimed)var1, var2, var4, var6, var8, var9);
   }
}
