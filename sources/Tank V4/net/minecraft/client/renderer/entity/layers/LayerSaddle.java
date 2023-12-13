package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.ResourceLocation;

public class LayerSaddle implements LayerRenderer {
   private final RenderPig pigRenderer;
   private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/pig/pig_saddle.png");
   private final ModelPig pigModel = new ModelPig(0.5F);

   public boolean shouldCombineTextures() {
      return false;
   }

   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.doRenderLayer((EntityPig)var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public void doRenderLayer(EntityPig var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (var1.getSaddled()) {
         this.pigRenderer.bindTexture(TEXTURE);
         this.pigModel.setModelAttributes(this.pigRenderer.getMainModel());
         this.pigModel.render(var1, var2, var3, var5, var6, var7, var8);
      }

   }

   public LayerSaddle(RenderPig var1) {
      this.pigRenderer = var1;
   }
}
