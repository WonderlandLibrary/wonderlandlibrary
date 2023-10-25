/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
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
    private GuiTextField nameField;
    private InventoryPlayer playerInventory;
    private static final ResourceLocation anvilResource = new ResourceLocation("textures/gui/container/anvil.png");
    private ContainerRepair anvil;

    @Override
    public void func_175173_a(Container container, IInventory iInventory) {
    }

    @Override
    public void sendSlotContents(Container container, int n, ItemStack itemStack) {
        if (n == 0) {
            this.nameField.setText(itemStack == null ? "" : itemStack.getDisplayName());
            this.nameField.setEnabled(itemStack != null);
            if (itemStack != null) {
                this.renameItem();
            }
        }
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        super.mouseClicked(n, n2, n3);
        this.nameField.mouseClicked(n, n2, n3);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int n, int n2) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(anvilResource);
        int n3 = (width - this.xSize) / 2;
        int n4 = (height - this.ySize) / 2;
        this.drawTexturedModalRect(n3, n4, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(n3 + 59, n4 + 20, 0, this.ySize + (this.anvil.getSlot(0).getHasStack() ? 0 : 16), 110, 16);
        if ((this.anvil.getSlot(0).getHasStack() || this.anvil.getSlot(1).getHasStack()) && !this.anvil.getSlot(2).getHasStack()) {
            this.drawTexturedModalRect(n3 + 99, n4 + 45, this.xSize, 0, 28, 21);
        }
    }

    @Override
    public void updateCraftingInventory(Container container, List<ItemStack> list) {
        this.sendSlotContents(container, 0, container.getSlot(0).getStack());
    }

    @Override
    public void sendProgressBarUpdate(Container container, int n, int n2) {
    }

    public GuiRepair(InventoryPlayer inventoryPlayer, World world) {
        Minecraft.getMinecraft();
        super(new ContainerRepair(inventoryPlayer, world, Minecraft.thePlayer));
        this.playerInventory = inventoryPlayer;
        this.anvil = (ContainerRepair)this.inventorySlots;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents((boolean)false);
        this.inventorySlots.removeCraftingFromCrafters(this);
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        super.drawScreen(n, n2, f);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.nameField.drawTextBox();
    }

    private void renameItem() {
        String string = this.nameField.getText();
        Slot slot = this.anvil.getSlot(0);
        if (slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() && string.equals(slot.getStack().getDisplayName())) {
            string = "";
        }
        this.anvil.updateItemName(string);
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("MC|ItemName", new PacketBuffer(Unpooled.buffer()).writeString(string)));
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents((boolean)true);
        int n = (width - this.xSize) / 2;
        int n2 = (height - this.ySize) / 2;
        this.nameField = new GuiTextField(0, this.fontRendererObj, n + 62, n2 + 24, 103, 12);
        this.nameField.setTextColor(-1);
        this.nameField.setDisabledTextColour(-1);
        this.nameField.setEnableBackgroundDrawing(false);
        this.nameField.setMaxStringLength(30);
        this.inventorySlots.removeCraftingFromCrafters(this);
        this.inventorySlots.onCraftGuiOpened(this);
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        if (this.nameField.textboxKeyTyped(c, n)) {
            this.renameItem();
        } else {
            super.keyTyped(c, n);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int n, int n2) {
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.fontRendererObj.drawString(I18n.format("container.repair", new Object[0]), 60.0, 6.0, 0x404040);
        if (this.anvil.maximumCost > 0) {
            int n3 = 8453920;
            boolean bl = true;
            String string = I18n.format("container.repair.cost", this.anvil.maximumCost);
            if (this.anvil.maximumCost >= 40 && !Minecraft.thePlayer.capabilities.isCreativeMode) {
                string = I18n.format("container.repair.expensive", new Object[0]);
                n3 = 0xFF6060;
            } else if (!this.anvil.getSlot(2).getHasStack()) {
                bl = false;
            } else if (!this.anvil.getSlot(2).canTakeStack(this.playerInventory.player)) {
                n3 = 0xFF6060;
            }
            if (bl) {
                int n4 = 0xFF000000 | (n3 & 0xFCFCFC) >> 2 | n3 & 0xFF000000;
                int n5 = this.xSize - 8 - this.fontRendererObj.getStringWidth(string);
                int n6 = 67;
                if (this.fontRendererObj.getUnicodeFlag()) {
                    GuiRepair.drawRect(n5 - 3, n6 - 2, this.xSize - 7, n6 + 10, -16777216);
                    GuiRepair.drawRect(n5 - 2, n6 - 1, this.xSize - 8, n6 + 9, -12895429);
                } else {
                    this.fontRendererObj.drawString(string, n5, n6 + 1, n4);
                    this.fontRendererObj.drawString(string, n5 + 1, n6, n4);
                    this.fontRendererObj.drawString(string, n5 + 1, n6 + 1, n4);
                }
                this.fontRendererObj.drawString(string, n5, n6, n3);
            }
        }
        GlStateManager.enableLighting();
    }
}

