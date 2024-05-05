package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiScreenHorseInventory
extends GuiContainer {
    private static final ResourceLocation horseGuiTextures = new ResourceLocation("textures/gui/container/horse.png");
    private IInventory playerInventory;
    private IInventory horseInventory;
    private EntityHorse horseEntity;
    private float mousePosx;
    private float mousePosY;

    public GuiScreenHorseInventory(IInventory playerInv, IInventory horseInv, EntityHorse horse) {
        super((Container)new ContainerHorseInventory(playerInv, horseInv, horse, (EntityPlayer)Minecraft.getMinecraft().thePlayer));
        this.playerInventory = playerInv;
        this.horseInventory = horseInv;
        this.horseEntity = horse;
        this.allowUserInput = false;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString(this.horseInventory.getDisplayName().getUnformattedText(), 8, 6, 0x404040);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.mc.getTextureManager().bindTexture(horseGuiTextures);
        int i = (width - this.xSize) / 2;
        int j = (height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        if (this.horseEntity.isChested()) {
            this.drawTexturedModalRect(i + 79, j + 17, 0, this.ySize, 90, 54);
        }
        if (this.horseEntity.canWearArmor()) {
            this.drawTexturedModalRect(i + 7, j + 35, 0, this.ySize + 54, 18, 18);
        }
        GuiInventory.drawEntityOnScreen((int)(i + 51), (int)(j + 60), (int)17, (float)((float)(i + 51) - this.mousePosx), (float)((float)(j + 75 - 50) - this.mousePosY), (EntityLivingBase)this.horseEntity);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.mousePosx = mouseX;
        this.mousePosY = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
