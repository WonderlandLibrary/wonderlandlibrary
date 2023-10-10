package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.init.Blocks;

public class LayerIronGolemFlower implements LayerRenderer {
   private final RenderIronGolem ironGolemRenderer;

   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.doRenderLayer((EntityIronGolem)var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public void doRenderLayer(EntityIronGolem var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (var1.getHoldRoseTick() != 0) {
         BlockRendererDispatcher var9 = Minecraft.getMinecraft().getBlockRendererDispatcher();
         GlStateManager.enableRescaleNormal();
         GlStateManager.pushMatrix();
         GlStateManager.rotate(5.0F + 180.0F * ((ModelIronGolem)this.ironGolemRenderer.getMainModel()).ironGolemRightArm.rotateAngleX / 3.1415927F, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.translate(-0.9375F, -0.625F, -0.9375F);
         float var10 = 0.5F;
         GlStateManager.scale(var10, -var10, var10);
         int var11 = var1.getBrightnessForRender(var4);
         int var12 = var11 % 65536;
         int var13 = var11 / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var12 / 1.0F, (float)var13 / 1.0F);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.ironGolemRenderer.bindTexture(TextureMap.locationBlocksTexture);
         var9.renderBlockBrightness(Blocks.red_flower.getDefaultState(), 1.0F);
         GlStateManager.popMatrix();
         GlStateManager.disableRescaleNormal();
      }

   }

   public boolean shouldCombineTextures() {
      return false;
   }

   public LayerIronGolemFlower(RenderIronGolem var1) {
      this.ironGolemRenderer = var1;
   }
}
