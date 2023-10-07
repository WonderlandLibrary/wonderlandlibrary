// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiDispenser extends GuiContainer
{
    private static final ResourceLocation dispenserGuiTextures;
    private final InventoryPlayer field_175376_w;
    public IInventory field_175377_u;
    private static final String __OBFID = "CL_00000765";
    
    public GuiDispenser(final InventoryPlayer p_i45503_1_, final IInventory p_i45503_2_) {
        super(new ContainerDispenser(p_i45503_1_, p_i45503_2_));
        this.field_175376_w = p_i45503_1_;
        this.field_175377_u = p_i45503_2_;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final String var3 = this.field_175377_u.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(var3, this.xSize / 2 - this.fontRendererObj.getStringWidth(var3) / 2, 6.0f, 4210752);
        this.fontRendererObj.drawString(this.field_175376_w.getDisplayName().getUnformattedText(), 8.0f, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiDispenser.dispenserGuiTextures);
        final int var4 = (this.width - this.xSize) / 2;
        final int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
    }
    
    static {
        dispenserGuiTextures = new ResourceLocation("textures/gui/container/dispenser.png");
    }
}
