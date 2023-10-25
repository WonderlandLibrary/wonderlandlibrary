package net.minecraft.client.gui.inventory;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiDispenser extends GuiContainer {
  private static final ResourceLocation dispenserGuiTextures = new ResourceLocation("textures/gui/container/dispenser.png");
  
  private final InventoryPlayer playerInventory;
  
  public IInventory dispenserInventory;
  
  public GuiDispenser(InventoryPlayer playerInv, IInventory dispenserInv) {
    super((Container)new ContainerDispenser((IInventory)playerInv, dispenserInv));
    this.playerInventory = playerInv;
    this.dispenserInventory = dispenserInv;
  }
  
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    String s = this.dispenserInventory.getDisplayName().getUnformattedText();
    this.fontRendererObj.drawString(s, (this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2), 6.0F, 4210752);
    this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0F, (this.ySize - 96 + 2), 4210752);
  }
  
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(dispenserGuiTextures);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this;
    drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\gui\inventory\GuiDispenser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */