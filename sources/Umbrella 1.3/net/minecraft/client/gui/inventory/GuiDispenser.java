/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiDispenser
extends GuiContainer {
    private static final ResourceLocation dispenserGuiTextures = new ResourceLocation("textures/gui/container/dispenser.png");
    private final InventoryPlayer field_175376_w;
    public IInventory field_175377_u;
    private static final String __OBFID = "CL_00000765";

    public GuiDispenser(InventoryPlayer p_i45503_1_, IInventory p_i45503_2_) {
        super(new ContainerDispenser(p_i45503_1_, p_i45503_2_));
        this.field_175376_w = p_i45503_1_;
        this.field_175377_u = p_i45503_2_;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String var3 = this.field_175377_u.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(var3, this.xSize / 2 - this.fontRendererObj.getStringWidth(var3) / 2, 6.0, 0x404040);
        this.fontRendererObj.drawString(this.field_175376_w.getDisplayName().getUnformattedText(), 8.0, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(dispenserGuiTextures);
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
    }
}

