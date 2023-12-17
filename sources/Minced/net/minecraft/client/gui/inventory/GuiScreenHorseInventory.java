// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiScreenHorseInventory extends GuiContainer
{
    private static final ResourceLocation HORSE_GUI_TEXTURES;
    private final IInventory playerInventory;
    private final IInventory horseInventory;
    private final AbstractHorse horseEntity;
    private float mousePosx;
    private float mousePosY;
    
    public GuiScreenHorseInventory(final IInventory playerInv, final IInventory horseInv, final AbstractHorse horse) {
        Minecraft.getMinecraft();
        super(new ContainerHorseInventory(playerInv, horseInv, horse, Minecraft.player));
        this.playerInventory = playerInv;
        this.horseInventory = horseInv;
        this.horseEntity = horse;
        this.allowUserInput = false;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.fontRenderer.drawString(this.horseInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GuiScreenHorseInventory.mc.getTextureManager().bindTexture(GuiScreenHorseInventory.HORSE_GUI_TEXTURES);
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        if (this.horseEntity instanceof AbstractChestHorse) {
            final AbstractChestHorse abstractchesthorse = (AbstractChestHorse)this.horseEntity;
            if (abstractchesthorse.hasChest()) {
                this.drawTexturedModalRect(i + 79, j + 17, 0, this.ySize, abstractchesthorse.getInventoryColumns() * 18, 54);
            }
        }
        if (this.horseEntity.canBeSaddled()) {
            this.drawTexturedModalRect(i + 7, j + 35 - 18, 18, this.ySize + 54, 18, 18);
        }
        if (this.horseEntity.wearsArmor()) {
            if (this.horseEntity instanceof EntityLlama) {
                this.drawTexturedModalRect(i + 7, j + 35, 36, this.ySize + 54, 18, 18);
            }
            else {
                this.drawTexturedModalRect(i + 7, j + 35, 0, this.ySize + 54, 18, 18);
            }
        }
        GuiInventory.drawEntityOnScreen(i + 51, j + 60, 17, i + 51 - this.mousePosx, j + 75 - 50 - this.mousePosY, this.horseEntity);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.mousePosx = (float)mouseX;
        this.mousePosY = (float)mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    static {
        HORSE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/horse.png");
    }
}
