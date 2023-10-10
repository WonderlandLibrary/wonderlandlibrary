package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import shadersmod.client.Shaders;

public class LayerEnderDragonEyes implements LayerRenderer {
   private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/enderdragon/dragon_eyes.png");
   private final RenderDragon dragonRenderer;
   private static final String __OBFID = "CL_00002419";

   public LayerEnderDragonEyes(RenderDragon var1) {
      this.dragonRenderer = var1;
   }

   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.doRenderLayer((EntityDragon)var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public boolean shouldCombineTextures() {
      return false;
   }

   public void doRenderLayer(EntityDragon var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.dragonRenderer.bindTexture(TEXTURE);
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.blendFunc(1, 1);
      GlStateManager.disableLighting();
      GlStateManager.depthFunc(514);
      char var9 = '\uf0f0';
      int var10 = var9 % 65536;
      int var11 = var9 / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var10 / 1.0F, (float)var11 / 1.0F);
      GlStateManager.enableLighting();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      if (Config.isShaders()) {
         Shaders.beginSpiderEyes();
      }

      this.dragonRenderer.getMainModel().render(var1, var2, var3, var5, var6, var7, var8);
      this.dragonRenderer.func_177105_a(var1, var4);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.depthFunc(515);
   }
}
