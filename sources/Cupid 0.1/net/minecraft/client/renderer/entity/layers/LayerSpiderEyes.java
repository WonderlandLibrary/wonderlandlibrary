package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import shadersmod.client.Shaders;

public class LayerSpiderEyes implements LayerRenderer {
  private static final ResourceLocation SPIDER_EYES = new ResourceLocation("textures/entity/spider_eyes.png");
  
  private final RenderSpider spiderRenderer;
  
  private static final String __OBFID = "CL_00002410";
  
  public LayerSpiderEyes(RenderSpider spiderRendererIn) {
    this.spiderRenderer = spiderRendererIn;
  }
  
  public void doRenderLayer(EntitySpider entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
    this.spiderRenderer.bindTexture(SPIDER_EYES);
    GlStateManager.enableBlend();
    GlStateManager.disableAlpha();
    GlStateManager.blendFunc(1, 1);
    if (entitylivingbaseIn.isInvisible()) {
      GlStateManager.depthMask(false);
    } else {
      GlStateManager.depthMask(true);
    } 
    char c0 = '';
    int i = c0 % 65536;
    int j = c0 / 65536;
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i / 1.0F, j / 1.0F);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    if (Config.isShaders())
      Shaders.beginSpiderEyes(); 
    this.spiderRenderer.getMainModel().render((Entity)entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
    int k = entitylivingbaseIn.getBrightnessForRender(partialTicks);
    i = k % 65536;
    j = k / 65536;
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i / 1.0F, j / 1.0F);
    this.spiderRenderer.func_177105_a((EntityLiving)entitylivingbaseIn, partialTicks);
    GlStateManager.disableBlend();
    GlStateManager.enableAlpha();
  }
  
  public boolean shouldCombineTextures() {
    return false;
  }
  
  public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
    doRenderLayer((EntitySpider)entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\entity\layers\LayerSpiderEyes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */