package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiHopper extends GuiContainer {
  private static final ResourceLocation HOPPER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/hopper.png");
  
  private IInventory playerInventory;
  
  private IInventory hopperInventory;
  
  public GuiHopper(InventoryPlayer playerInv, IInventory hopperInv) {
    super((Container)new ContainerHopper(playerInv, hopperInv, (EntityPlayer)(Minecraft.getMinecraft()).thePlayer));
    this.playerInventory = (IInventory)playerInv;
    this.hopperInventory = hopperInv;
    this.allowUserInput = false;
    this.ySize = 133;
  }
  
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    this.fontRendererObj.drawString(this.hopperInventory.getDisplayName().getUnformattedText(), 8.0F, 6.0F, 4210752);
    this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0F, (this.ySize - 96 + 2), 4210752);
  }
  
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(HOPPER_GUI_TEXTURE);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this;
    drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\gui\GuiHopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */