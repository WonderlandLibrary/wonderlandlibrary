// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiChest extends GuiContainer
{
    private static final ResourceLocation field_147017_u;
    private IInventory upperChestInventory;
    public IInventory lowerChestInventory;
    private int inventoryRows;
    private static final String __OBFID = "CL_00000749";
    
    public GuiChest(final IInventory p_i46315_1_, final IInventory p_i46315_2_) {
        super(new ContainerChest(p_i46315_1_, p_i46315_2_, Minecraft.getMinecraft().thePlayer));
        this.upperChestInventory = p_i46315_1_;
        this.lowerChestInventory = p_i46315_2_;
        this.allowUserInput = false;
        final short var3 = 222;
        final int var4 = var3 - 108;
        this.inventoryRows = p_i46315_2_.getSizeInventory() / 9;
        this.ySize = var4 + this.inventoryRows * 18;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.fontRendererObj.drawString(this.lowerChestInventory.getDisplayName().getUnformattedText(), 8.0f, 6.0f, 4210752);
        this.fontRendererObj.drawString(this.upperChestInventory.getDisplayName().getUnformattedText(), 8.0f, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiChest.field_147017_u);
        final int var4 = (this.width - this.xSize) / 2;
        final int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(var4, var5 + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
    
    static {
        field_147017_u = new ResourceLocation("textures/gui/container/generic_54.png");
    }
}
