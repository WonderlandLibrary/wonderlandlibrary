package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;

public class LayerSlimeGel implements LayerRenderer<EntitySlime> {
  private final RenderSlime slimeRenderer;
  
  private final ModelBase slimeModel = (ModelBase)new ModelSlime(0);
  
  public LayerSlimeGel(RenderSlime slimeRendererIn) {
    this.slimeRenderer = slimeRendererIn;
  }
  
  public void doRenderLayer(EntitySlime entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
    if (!entitylivingbaseIn.isInvisible()) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableNormalize();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      this.slimeModel.setModelAttributes(this.slimeRenderer.getMainModel());
      this.slimeModel.render((Entity)entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
      GlStateManager.disableBlend();
      GlStateManager.disableNormalize();
    } 
  }
  
  public boolean shouldCombineTextures() {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\entity\layers\LayerSlimeGel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */