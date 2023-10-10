package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ResourceLocation;

public class LayerCreeperCharge implements LayerRenderer {
   private final RenderCreeper creeperRenderer;
   private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
   private final ModelCreeper creeperModel = new ModelCreeper(2.0F);

   public LayerCreeperCharge(RenderCreeper var1) {
      this.creeperRenderer = var1;
   }

   public boolean shouldCombineTextures() {
      return false;
   }

   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.doRenderLayer((EntityCreeper)var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public void doRenderLayer(EntityCreeper var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (var1.getPowered()) {
         boolean var9 = var1.isInvisible();
         GlStateManager.depthMask(!var9);
         this.creeperRenderer.bindTexture(LIGHTNING_TEXTURE);
         GlStateManager.matrixMode(5890);
         GlStateManager.loadIdentity();
         float var10 = (float)var1.ticksExisted + var4;
         GlStateManager.translate(var10 * 0.01F, var10 * 0.01F, 0.0F);
         GlStateManager.matrixMode(5888);
         GlStateManager.enableBlend();
         float var11 = 0.5F;
         GlStateManager.color(var11, var11, var11, 1.0F);
         GlStateManager.disableLighting();
         GlStateManager.blendFunc(1, 1);
         this.creeperModel.setModelAttributes(this.creeperRenderer.getMainModel());
         this.creeperModel.render(var1, var2, var3, var5, var6, var7, var8);
         GlStateManager.matrixMode(5890);
         GlStateManager.loadIdentity();
         GlStateManager.matrixMode(5888);
         GlStateManager.enableLighting();
         GlStateManager.disableBlend();
         GlStateManager.depthMask(var9);
      }

   }
}
