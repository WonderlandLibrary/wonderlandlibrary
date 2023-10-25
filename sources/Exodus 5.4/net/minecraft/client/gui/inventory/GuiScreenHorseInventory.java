/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiScreenHorseInventory
extends GuiContainer {
    private float mousePosY;
    private IInventory horseInventory;
    private static final ResourceLocation horseGuiTextures = new ResourceLocation("textures/gui/container/horse.png");
    private float mousePosx;
    private IInventory playerInventory;
    private EntityHorse horseEntity;

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.mousePosx = n;
        this.mousePosY = n2;
        super.drawScreen(n, n2, f);
    }

    public GuiScreenHorseInventory(IInventory iInventory, IInventory iInventory2, EntityHorse entityHorse) {
        Minecraft.getMinecraft();
        super(new ContainerHorseInventory(iInventory, iInventory2, entityHorse, Minecraft.thePlayer));
        this.playerInventory = iInventory;
        this.horseInventory = iInventory2;
        this.horseEntity = entityHorse;
        this.allowUserInput = false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int n, int n2) {
        this.fontRendererObj.drawString(this.horseInventory.getDisplayName().getUnformattedText(), 8.0, 6.0, 0x404040);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int n, int n2) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(horseGuiTextures);
        int n3 = (width - this.xSize) / 2;
        int n4 = (height - this.ySize) / 2;
        this.drawTexturedModalRect(n3, n4, 0, 0, this.xSize, this.ySize);
        if (this.horseEntity.isChested()) {
            this.drawTexturedModalRect(n3 + 79, n4 + 17, 0, this.ySize, 90, 54);
        }
        if (this.horseEntity.canWearArmor()) {
            this.drawTexturedModalRect(n3 + 7, n4 + 35, 0, this.ySize + 54, 18, 18);
        }
        GuiInventory.drawEntityOnScreen(n3 + 51, n4 + 60, 17, (float)(n3 + 51) - this.mousePosx, (float)(n4 + 75 - 50) - this.mousePosY, this.horseEntity);
    }
}

