// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiCrafting extends GuiContainer
{
    private static final ResourceLocation craftingTableGuiTextures;
    private static final String __OBFID = "CL_00000750";
    
    public GuiCrafting(final InventoryPlayer p_i45504_1_, final World worldIn) {
        this(p_i45504_1_, worldIn, BlockPos.ORIGIN);
    }
    
    public GuiCrafting(final InventoryPlayer p_i45505_1_, final World worldIn, final BlockPos p_i45505_3_) {
        super(new ContainerWorkbench(p_i45505_1_, worldIn, p_i45505_3_));
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 28.0f, 6.0f, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8.0f, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiCrafting.craftingTableGuiTextures);
        final int var4 = (this.width - this.xSize) / 2;
        final int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
    }
    
    static {
        craftingTableGuiTextures = new ResourceLocation("textures/gui/container/crafting_table.png");
    }
}
