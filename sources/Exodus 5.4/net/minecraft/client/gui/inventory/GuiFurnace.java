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
    private IInventory tileFurnace;
    private final InventoryPlayer playerInventory;
    private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("textures/gui/container/furnace.png");

    private int getBurnLeftScaled(int n) {
        int n2 = this.tileFurnace.getField(1);
        if (n2 == 0) {
            n2 = 200;
        }
        return this.tileFurnace.getField(0) * n / n2;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int n, int n2) {
        String string = this.tileFurnace.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(string, this.xSize / 2 - this.fontRendererObj.getStringWidth(string) / 2, 6.0, 0x404040);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int n, int n2) {
        int n3;
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
        int n4 = (width - this.xSize) / 2;
        int n5 = (height - this.ySize) / 2;
        this.drawTexturedModalRect(n4, n5, 0, 0, this.xSize, this.ySize);
        if (TileEntityFurnace.isBurning(this.tileFurnace)) {
            n3 = this.getBurnLeftScaled(13);
            this.drawTexturedModalRect(n4 + 56, n5 + 36 + 12 - n3, 176, 12 - n3, 14, n3 + 1);
        }
        n3 = this.getCookProgressScaled(24);
        this.drawTexturedModalRect(n4 + 79, n5 + 34, 176, 14, n3 + 1, 16);
    }

    private int getCookProgressScaled(int n) {
        int n2 = this.tileFurnace.getField(2);
        int n3 = this.tileFurnace.getField(3);
        return n3 != 0 && n2 != 0 ? n2 * n / n3 : 0;
    }

    public GuiFurnace(InventoryPlayer inventoryPlayer, IInventory iInventory) {
        super(new ContainerFurnace(inventoryPlayer, iInventory));
        this.playerInventory = inventoryPlayer;
        this.tileFurnace = iInventory;
    }
}

