package my.NewSnake.Tank.cosmetics.impl;

import my.NewSnake.Tank.module.modules.PLAYER.Cosmetics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class CosmeticFerramenta implements LayerRenderer {
   public ItemStack itemStack;
   private ModelPlayer playerModel;
   private final RenderPlayer renderPlayer;

   public boolean shouldCombineTextures() {
      return true;
   }

   public void doRenderLayer(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.doRenderLayer((AbstractClientPlayer)var1, var2, var3, var4, var5, var6, var7);
   }

   public CosmeticFerramenta(RenderPlayer var1) {
      this.renderPlayer = var1;
      this.playerModel = var1.getMainModel();
   }

   public void doRenderLayer(AbstractClientPlayer var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if ((new Cosmetics()).getInstance().isEnabled() && Cosmetics.Ferramenta) {
         GlStateManager.pushMatrix();
         if (var1.isSneaking()) {
            GlStateManager.rotate(30.0F, 1.0F, 0.0F, 0.0F);
         }

         GlStateManager.scale(0.8D, 0.8D, 0.8D);
         GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.translate(0.0D, 0.3D, -0.22D);
         ItemStack var9;
         if (this.itemStack == null) {
            var9 = var1.getHeldItem();
         } else {
            var9 = this.itemStack;
         }

         if (var9 != null && var9.getItem() != null) {
            Minecraft.getMinecraft().getItemRenderer().renderItem(var1, var9, ItemCameraTransforms.TransformType.GUI);
         }

         GlStateManager.popMatrix();
      }

   }

   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.doRenderLayer((AbstractClientPlayer)var1, var2, var3, var4, var5, var6, var7, var8);
   }
}
