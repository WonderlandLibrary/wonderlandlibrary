package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import shadersmod.client.Shaders;

public class LayerEndermanEyes implements LayerRenderer {
   private static final ResourceLocation field_177203_a = new ResourceLocation("textures/entity/enderman/enderman_eyes.png");
   private static final String __OBFID = "CL_00002418";
   private final RenderEnderman endermanRenderer;

   public LayerEndermanEyes(RenderEnderman var1) {
      this.endermanRenderer = var1;
   }

   public boolean shouldCombineTextures() {
      return false;
   }

   public void doRenderLayer(EntityEnderman var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.endermanRenderer.bindTexture(field_177203_a);
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.blendFunc(1, 1);
      GlStateManager.disableLighting();
      GlStateManager.depthMask(!var1.isInvisible());
      char var9 = '\uf0f0';
      int var10 = var9 % 65536;
      int var11 = var9 / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var10 / 1.0F, (float)var11 / 1.0F);
      GlStateManager.enableLighting();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      if (Config.isShaders()) {
         Shaders.beginSpiderEyes();
      }

      this.endermanRenderer.getMainModel().render(var1, var2, var3, var5, var6, var7, var8);
      this.endermanRenderer.func_177105_a(var1, var4);
      GlStateManager.depthMask(true);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
   }

   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.doRenderLayer((EntityEnderman)var1, var2, var3, var4, var5, var6, var7, var8);
   }
}
