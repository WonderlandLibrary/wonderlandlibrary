/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class GuiRepair
extends GuiContainer
implements ICrafting {
    private static final ResourceLocation anvilResource = new ResourceLocation("textures/gui/container/anvil.png");
    private ContainerRepair anvil;
    private GuiTextField nameField;
    private InventoryPlayer playerInventory;

    public GuiRepair(InventoryPlayer inventoryIn, World worldIn) {
        super(new ContainerRepair(inventoryIn, worldIn, Minecraft.getMinecraft().thePlayer));
        this.playerInventory = inventoryIn;
        this.anvil = (ContainerRepair)this.inventorySlots;
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int i2 = (this.width - this.xSize) / 2;
        int j2 = (this.height - this.ySize) / 2;
        this.nameField = new GuiTextField(0, this.fontRendererObj, i2 + 62, j2 + 24, 103, 12);
        this.nameField.setTextColor(-1);
        this.nameField.setDisabledTextColour(-1);
        this.nameField.setEnableBackgroundDrawing(false);
        this.nameField.setMaxStringLength(30);
        this.inventorySlots.removeCraftingFromCrafters(this);
        this.inventorySlots.onCraftGuiOpened(this);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        this.inventorySlots.removeCraftingFromCrafters(this);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.fontRendererObj.drawString(I18n.format("container.repair", new Object[0]), 60, 6, 0x404040);
        if (this.anvil.maximumCost > 0) {
            int i2 = 8453920;
            boolean flag = true;
            String s2 = I18n.format("container.repair.cost", this.anvil.maximumCost);
            if (this.anvil.maximumCost >= 40 && !this.mc.thePlayer.capabilities.isCreativeMode) {
                s2 = I18n.format("container.repair.expensive", new Object[0]);
                i2 = 0xFF6060;
            } else if (!this.anvil.getSlot(2).getHasStack()) {
                flag = false;
            } else if (!this.anvil.getSlot(2).canTakeStack(this.playerInventory.player)) {
                i2 = 0xFF6060;
            }
            if (flag) {
                int j2 = 0xFF000000 | (i2 & 0xFCFCFC) >> 2 | i2 & 0xFF000000;
                int k2 = this.xSize - 8 - this.fontRendererObj.getStringWidth(s2);
                int l2 = 67;
                if (this.fontRendererObj.getUnicodeFlag()) {
                    GuiRepair.drawRect(k2 - 3, l2 - 2, this.xSize - 7, l2 + 10, -16777216);
                    GuiRepair.drawRect(k2 - 2, l2 - 1, this.xSize - 8, l2 + 9, -12895429);
                } else {
                    this.fontRendererObj.drawString(s2, k2, l2 + 1, j2);
                    this.fontRendererObj.drawString(s2, k2 + 1, l2, j2);
                    this.fontRendererObj.drawString(s2, k2 + 1, l2 + 1, j2);
                }
                this.fontRendererObj.drawString(s2, k2, l2, i2);
            }
        }
        GlStateManager.enableLighting();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.nameField.textboxKeyTyped(typedChar, keyCode)) {
            this.renameItem();
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    private void renameItem() {
        String s2 = this.nameField.getText();
        Slot slot = this.anvil.getSlot(0);
        if (slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() && s2.equals(slot.getStack().getDisplayName())) {
            s2 = "";
        }
        this.anvil.updateItemName(s2);
        this.mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("MC|ItemName", new PacketBuffer(Unpooled.buffer()).writeString(s2)));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.nameField.drawTextBox();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(anvilResource);
        int i2 = (this.width - this.xSize) / 2;
        int j2 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i2, j2, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(i2 + 59, j2 + 20, 0, this.ySize + (this.anvil.getSlot(0).getHasStack() ? 0 : 16), 110, 16);
        if ((this.anvil.getSlot(0).getHasStack() || this.anvil.getSlot(1).getHasStack()) && !this.anvil.getSlot(2).getHasStack()) {
            this.drawTexturedModalRect(i2 + 99, j2 + 45, this.xSize, 0, 28, 21);
        }
    }

    @Override
    public void updateCraftingInventory(Container containerToSend, List<ItemStack> itemsList) {
        this.sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
    }

    @Override
    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
        if (slotInd == 0) {
            this.nameField.setText(stack == null ? "" : stack.getDisplayName());
            this.nameField.setEnabled(stack != null);
            if (stack != null) {
                this.renameItem();
            }
        }
    }

    @Override
    public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue) {
    }

    @Override
    public void func_175173_a(Container p_175173_1_, IInventory p_175173_2_) {
    }
}

