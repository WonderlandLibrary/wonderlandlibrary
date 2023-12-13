package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

public class ChestRenderer {
   public void renderChestBrightness(Block var1, float var2) {
      GlStateManager.color(var2, var2, var2, 1.0F);
      GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
      TileEntityItemStackRenderer.instance.renderByItem(new ItemStack(var1));
   }
}
