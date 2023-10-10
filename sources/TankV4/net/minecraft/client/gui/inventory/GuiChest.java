package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiChest extends GuiContainer {
   public static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
   public IInventory upperChestInventory;
   public IInventory lowerChestInventory;
   private int inventoryRows;

   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
      int var4 = (width - this.xSize) / 2;
      int var5 = (height - this.ySize) / 2;
      drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
      drawTexturedModalRect(var4, var5 + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
   }

   public GuiChest(IInventory var1, IInventory var2) {
      Minecraft.getMinecraft();
      super(new ContainerChest(var1, var2, Minecraft.thePlayer));
      this.upperChestInventory = var1;
      this.lowerChestInventory = var2;
      this.allowUserInput = false;
      short var3 = 222;
      int var4 = var3 - 108;
      this.inventoryRows = var2.getSizeInventory() / 9;
      this.ySize = var4 + this.inventoryRows * 18;
   }

   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      this.fontRendererObj.drawString(this.lowerChestInventory.getDisplayName().getUnformattedText(), 8.0D, 6.0D, 4210752);
      this.fontRendererObj.drawString(this.upperChestInventory.getDisplayName().getUnformattedText(), 8.0D, (double)(this.ySize - 96 + 2), 4210752);
   }
}
