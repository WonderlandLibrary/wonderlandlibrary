package net.minecraft.client.gui.inventory;

import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

public class GuiChest extends GuiContainer
{
	public void steal() {
		if(!ClientSettings.chestStealDelay) {
			for (int ii = 0; ii < this.inventoryRows * 9; ii++) {
				Slot s = inventorySlots.inventorySlots.get(ii);
				if (s.getStack() == null) {
					continue;
				}
				handleMouseClick(s, s.slotNumber, 0, ClickType.QUICK_MOVE);
				handleMouseClick(s, s.slotNumber, 0, ClickType.PICKUP_ALL);
			}
		}
	}
	
	public boolean isEmpty() {
		for (int ii = 0; ii < this.inventoryRows * 9; ii++) {
			Slot s = inventorySlots.inventorySlots.get(ii);
			if (s.getStack() == null) {
				continue;
			}
			return false;
		}
		return true;
	}

	public void store() {
		for (int ii = 0; ii < 4 * 9; ii++) {
			Slot s = inventorySlots.inventorySlots.get(ii + inventoryRows * 9);
			if (s.getStack() == null) {
				continue;
			}
			handleMouseClick(s, s.slotNumber, 0, ClickType.QUICK_MOVE);
			handleMouseClick(s, s.slotNumber, 0, ClickType.PICKUP_ALL);
		}
	}
	
	//TODO END JIGSAW MODS
	
    /** The ResourceLocation containing the chest GUI texture. */
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private final IInventory upperChestInventory;
    private final IInventory lowerChestInventory;

    /**
     * window height is calculated with these values; the more rows, the heigher
     */
    private final int inventoryRows;

    public GuiChest(IInventory upperInv, IInventory lowerInv)
    {
        super(new ContainerChest(upperInv, lowerInv, Minecraft.getMinecraft().player));
        this.upperChestInventory = upperInv;
        this.lowerChestInventory = lowerInv;
        this.allowUserInput = false;
        int i = 222;
        int j = 114;
        this.inventoryRows = lowerInv.getSizeInventory() / 9;
        this.ySize = 114 + this.inventoryRows * 18;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.func_191948_b(mouseX, mouseY);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString(this.lowerChestInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        this.fontRenderer.drawString(this.upperChestInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}
