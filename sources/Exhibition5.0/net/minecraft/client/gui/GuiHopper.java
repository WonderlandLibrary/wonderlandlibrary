// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiHopper extends GuiContainer
{
    private static final ResourceLocation field_147085_u;
    private IInventory field_147084_v;
    private IInventory field_147083_w;
    private static final String __OBFID = "CL_00000759";
    
    public GuiHopper(final InventoryPlayer p_i1092_1_, final IInventory p_i1092_2_) {
        super(new ContainerHopper(p_i1092_1_, p_i1092_2_, Minecraft.getMinecraft().thePlayer));
        this.field_147084_v = p_i1092_1_;
        this.field_147083_w = p_i1092_2_;
        this.allowUserInput = false;
        this.ySize = 133;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.fontRendererObj.drawString(this.field_147083_w.getDisplayName().getUnformattedText(), 8.0f, 6.0f, 4210752);
        this.fontRendererObj.drawString(this.field_147084_v.getDisplayName().getUnformattedText(), 8.0f, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiHopper.field_147085_u);
        final int var4 = (this.width - this.xSize) / 2;
        final int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
    }
    
    static {
        field_147085_u = new ResourceLocation("textures/gui/container/hopper.png");
    }
}
