package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import shadersmod.client.Shaders;

public class LayerSpiderEyes implements LayerRenderer {
   private final RenderSpider spiderRenderer;
   private static final ResourceLocation SPIDER_EYES = new ResourceLocation("textures/entity/spider_eyes.png");
   private static final String __OBFID = "CL_00002410";

   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.doRenderLayer((EntitySpider)var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public LayerSpiderEyes(RenderSpider var1) {
      this.spiderRenderer = var1;
   }

   public boolean shouldCombineTextures() {
      return false;
   }

   public void doRenderLayer(EntitySpider var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.spiderRenderer.bindTexture(SPIDER_EYES);
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.blendFunc(1, 1);
      if (var1.isInvisible()) {
         GlStateManager.depthMask(false);
      } else {
         GlStateManager.depthMask(true);
      }

      char var9 = '\uf0f0';
      int var10 = var9 % 65536;
      int var11 = var9 / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var10 / 1.0F, (float)var11 / 1.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      if (Config.isShaders()) {
         Shaders.beginSpiderEyes();
      }

      this.spiderRenderer.getMainModel().render(var1, var2, var3, var5, var6, var7, var8);
      int var12 = var1.getBrightnessForRender(var4);
      var10 = var12 % 65536;
      var11 = var12 / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var10 / 1.0F, (float)var11 / 1.0F);
      this.spiderRenderer.func_177105_a(var1, var4);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
   }
}
