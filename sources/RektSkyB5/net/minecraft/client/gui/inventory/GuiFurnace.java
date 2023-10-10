/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;

public class GuiFurnace
extends GuiContainer {
    private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("textures/gui/container/furnace.png");
    private final InventoryPlayer playerInventory;
    private IInventory tileFurnace;

    public GuiFurnace(InventoryPlayer playerInv, IInventory furnaceInv) {
        super(new ContainerFurnace(playerInv, furnaceInv));
        this.playerInventory = playerInv;
        this.tileFurnace = furnaceInv;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s2 = this.tileFurnace.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s2, this.xSize / 2 - this.fontRendererObj.getStringWidth(s2) / 2, 6, 0x404040);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
        int i2 = (this.width - this.xSize) / 2;
        int j2 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i2, j2, 0, 0, this.xSize, this.ySize);
        if (TileEntityFurnace.isBurning(this.tileFurnace)) {
            int k2 = this.getBurnLeftScaled(13);
            this.drawTexturedModalRect(i2 + 56, j2 + 36 + 12 - k2, 176, 12 - k2, 14, k2 + 1);
        }
        int l2 = this.getCookProgressScaled(24);
        this.drawTexturedModalRect(i2 + 79, j2 + 34, 176, 14, l2 + 1, 16);
    }

    private int getCookProgressScaled(int pixels) {
        int i2 = this.tileFurnace.getField(2);
        int j2 = this.tileFurnace.getField(3);
        return j2 != 0 && i2 != 0 ? i2 * pixels / j2 : 0;
    }

    private int getBurnLeftScaled(int pixels) {
        int i2 = this.tileFurnace.getField(1);
        if (i2 == 0) {
            i2 = 200;
        }
        return this.tileFurnace.getField(0) * pixels / i2;
    }
}

