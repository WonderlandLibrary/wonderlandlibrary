package net.minecraft.client.gui.inventory;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiCrafting extends GuiContainer {
   private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation("textures/gui/container/crafting_table.png");

   public GuiCrafting(InventoryPlayer var1, World var2) {
      this(var1, var2, BlockPos.ORIGIN);
   }

   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
      int var4 = (width - this.xSize) / 2;
      int var5 = (height - this.ySize) / 2;
      drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
   }

   public GuiCrafting(InventoryPlayer var1, World var2, BlockPos var3) {
      super(new ContainerWorkbench(var1, var2, var3));
   }

   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      this.fontRendererObj.drawString(I18n.format("container.crafting"), 28.0D, 6.0D, 4210752);
      this.fontRendererObj.drawString(I18n.format("container.inventory"), 8.0D, (double)(this.ySize - 96 + 2), 4210752);
   }
}
