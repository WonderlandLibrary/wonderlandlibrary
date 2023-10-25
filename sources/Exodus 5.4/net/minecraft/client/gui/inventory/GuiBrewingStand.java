/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiBrewingStand
extends GuiContainer {
    private final InventoryPlayer playerInventory;
    private static final ResourceLocation brewingStandGuiTextures = new ResourceLocation("textures/gui/container/brewing_stand.png");
    private IInventory tileBrewingStand;

    @Override
    protected void drawGuiContainerForegroundLayer(int n, int n2) {
        String string = this.tileBrewingStand.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(string, this.xSize / 2 - this.fontRendererObj.getStringWidth(string) / 2, 6.0, 0x404040);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0, this.ySize - 96 + 2, 0x404040);
    }

    public GuiBrewingStand(InventoryPlayer inventoryPlayer, IInventory iInventory) {
        super(new ContainerBrewingStand(inventoryPlayer, iInventory));
        this.playerInventory = inventoryPlayer;
        this.tileBrewingStand = iInventory;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int n, int n2) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(brewingStandGuiTextures);
        int n3 = (width - this.xSize) / 2;
        int n4 = (height - this.ySize) / 2;
        this.drawTexturedModalRect(n3, n4, 0, 0, this.xSize, this.ySize);
        int n5 = this.tileBrewingStand.getField(0);
        if (n5 > 0) {
            int n6 = (int)(28.0f * (1.0f - (float)n5 / 400.0f));
            if (n6 > 0) {
                this.drawTexturedModalRect(n3 + 97, n4 + 16, 176, 0, 9, n6);
            }
            int n7 = n5 / 2 % 7;
            switch (n7) {
                case 0: {
                    n6 = 29;
                    break;
                }
                case 1: {
                    n6 = 24;
                    break;
                }
                case 2: {
                    n6 = 20;
                    break;
                }
                case 3: {
                    n6 = 16;
                    break;
                }
                case 4: {
                    n6 = 11;
                    break;
                }
                case 5: {
                    n6 = 6;
                    break;
                }
                case 6: {
                    n6 = 0;
                }
            }
            if (n6 > 0) {
                this.drawTexturedModalRect(n3 + 65, n4 + 14 + 29 - n6, 185, 29 - n6, 12, n6);
            }
        }
    }
}

