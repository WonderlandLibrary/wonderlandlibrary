package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderSnowball extends Render {
   private final RenderItem field_177083_e;
   protected final Item field_177084_a;

   public RenderSnowball(RenderManager var1, Item var2, RenderItem var3) {
      super(var1);
      this.field_177084_a = var2;
      this.field_177083_e = var3;
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return TextureMap.locationBlocksTexture;
   }

   public ItemStack func_177082_d(Entity var1) {
      return new ItemStack(this.field_177084_a, 1, 0);
   }

   public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var2, (float)var4, (float)var6);
      GlStateManager.enableRescaleNormal();
      GlStateManager.scale(0.5F, 0.5F, 0.5F);
      GlStateManager.rotate(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      this.bindTexture(TextureMap.locationBlocksTexture);
      this.field_177083_e.func_181564_a(this.func_177082_d(var1), ItemCameraTransforms.TransformType.GROUND);
      GlStateManager.disableRescaleNormal();
      GlStateManager.popMatrix();
      super.doRender(var1, var2, var4, var6, var8, var9);
   }
}
