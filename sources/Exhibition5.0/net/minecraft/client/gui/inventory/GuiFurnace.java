// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiFurnace extends GuiContainer
{
    private static final ResourceLocation furnaceGuiTextures;
    private final InventoryPlayer field_175383_v;
    private IInventory tileFurnace;
    private static final String __OBFID = "CL_00000758";
    
    public GuiFurnace(final InventoryPlayer p_i45501_1_, final IInventory p_i45501_2_) {
        super(new ContainerFurnace(p_i45501_1_, p_i45501_2_));
        this.field_175383_v = p_i45501_1_;
        this.tileFurnace = p_i45501_2_;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final String var3 = this.tileFurnace.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(var3, this.xSize / 2 - this.fontRendererObj.getStringWidth(var3) / 2, 6.0f, 4210752);
        this.fontRendererObj.drawString(this.field_175383_v.getDisplayName().getUnformattedText(), 8.0f, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiFurnace.furnaceGuiTextures);
        final int var4 = (this.width - this.xSize) / 2;
        final int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
        if (TileEntityFurnace.func_174903_a(this.tileFurnace)) {
            final int var6 = this.func_175382_i(13);
            this.drawTexturedModalRect(var4 + 56, var5 + 36 + 12 - var6, 176, 12 - var6, 14, var6 + 1);
        }
        final int var6 = this.func_175381_h(24);
        this.drawTexturedModalRect(var4 + 79, var5 + 34, 176, 14, var6 + 1, 16);
    }
    
    private int func_175381_h(final int p_175381_1_) {
        final int var2 = this.tileFurnace.getField(2);
        final int var3 = this.tileFurnace.getField(3);
        return (var3 != 0 && var2 != 0) ? (var2 * p_175381_1_ / var3) : 0;
    }
    
    private int func_175382_i(final int p_175382_1_) {
        int var2 = this.tileFurnace.getField(1);
        if (var2 == 0) {
            var2 = 200;
        }
        return this.tileFurnace.getField(0) * p_175382_1_ / var2;
    }
    
    static {
        furnaceGuiTextures = new ResourceLocation("textures/gui/container/furnace.png");
    }
}
