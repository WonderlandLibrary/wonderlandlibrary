package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiChest
extends GuiContainer {
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    public IInventory upperChestInventory;
    public IInventory lowerChestInventory;
    private int inventoryRows;

    public GuiChest(IInventory upperInv, IInventory lowerInv) {
        super((Container)new ContainerChest(upperInv, lowerInv, (EntityPlayer)Minecraft.getMinecraft().thePlayer));
        this.upperChestInventory = upperInv;
        this.lowerChestInventory = lowerInv;
        this.allowUserInput = false;
        int i = 222;
        int j = i - 108;
        this.inventoryRows = lowerInv.getSizeInventory() / 9;
        this.ySize = j + this.inventoryRows * 18;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString(this.lowerChestInventory.getDisplayName().getUnformattedText(), 8, 6, 0x404040);
        this.fontRendererObj.drawString(this.upperChestInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (width - this.xSize) / 2;
        int j = (height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }

    public IInventory getLowerChestInventory() {
        return this.lowerChestInventory;
    }
}
