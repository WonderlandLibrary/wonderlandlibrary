package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderWither;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class LayerWitherAura implements LayerRenderer {
   private final ModelWither witherModel = new ModelWither(0.5F);
   private final RenderWither witherRenderer;
   private static final ResourceLocation WITHER_ARMOR = new ResourceLocation("textures/entity/wither/wither_armor.png");

   public boolean shouldCombineTextures() {
      return false;
   }

   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.doRenderLayer((EntityWither)var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public LayerWitherAura(RenderWither var1) {
      this.witherRenderer = var1;
   }

   public void doRenderLayer(EntityWither var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (var1.isArmored()) {
         GlStateManager.depthMask(!var1.isInvisible());
         this.witherRenderer.bindTexture(WITHER_ARMOR);
         GlStateManager.matrixMode(5890);
         GlStateManager.loadIdentity();
         float var9 = (float)var1.ticksExisted + var4;
         float var10 = MathHelper.cos(var9 * 0.02F) * 3.0F;
         float var11 = var9 * 0.01F;
         GlStateManager.translate(var10, var11, 0.0F);
         GlStateManager.matrixMode(5888);
         GlStateManager.enableBlend();
         float var12 = 0.5F;
         GlStateManager.color(var12, var12, var12, 1.0F);
         GlStateManager.disableLighting();
         GlStateManager.blendFunc(1, 1);
         this.witherModel.setLivingAnimations(var1, var2, var3, var4);
         this.witherModel.setModelAttributes(this.witherRenderer.getMainModel());
         this.witherModel.render(var1, var2, var3, var5, var6, var7, var8);
         GlStateManager.matrixMode(5890);
         GlStateManager.loadIdentity();
         GlStateManager.matrixMode(5888);
         GlStateManager.enableLighting();
         GlStateManager.disableBlend();
      }

   }
}
