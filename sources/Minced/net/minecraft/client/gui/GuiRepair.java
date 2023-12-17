// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.inventory.Slot;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiRepair extends GuiContainer implements IContainerListener
{
    private static final ResourceLocation ANVIL_RESOURCE;
    private final ContainerRepair anvil;
    private GuiTextField nameField;
    private final InventoryPlayer playerInventory;
    
    public GuiRepair(final InventoryPlayer inventoryIn, final World worldIn) {
        Minecraft.getMinecraft();
        super(new ContainerRepair(inventoryIn, worldIn, Minecraft.player));
        this.playerInventory = inventoryIn;
        this.anvil = (ContainerRepair)this.inventorySlots;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        (this.nameField = new GuiTextField(0, this.fontRenderer, i + 62, j + 24, 103, 12)).setTextColor(-1);
        this.nameField.setDisabledTextColour(-1);
        this.nameField.setEnableBackgroundDrawing(false);
        this.nameField.setMaxStringLength(35);
        this.inventorySlots.removeListener(this);
        this.inventorySlots.addListener(this);
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        this.inventorySlots.removeListener(this);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.fontRenderer.drawString(I18n.format("container.repair", new Object[0]), 60, 6, 4210752);
        if (this.anvil.maximumCost > 0) {
            int i = 8453920;
            boolean flag = true;
            String s = I18n.format("container.repair.cost", this.anvil.maximumCost);
            Label_0160: {
                if (this.anvil.maximumCost >= 40) {
                    final Minecraft mc = GuiRepair.mc;
                    if (!Minecraft.player.capabilities.isCreativeMode) {
                        s = I18n.format("container.repair.expensive", new Object[0]);
                        i = 16736352;
                        break Label_0160;
                    }
                }
                if (!this.anvil.getSlot(2).getHasStack()) {
                    flag = false;
                }
                else if (!this.anvil.getSlot(2).canTakeStack(this.playerInventory.player)) {
                    i = 16736352;
                }
            }
            if (flag) {
                final int j = 0xFF000000 | (i & 0xFCFCFC) >> 2 | (i & 0xFF000000);
                final int k = this.xSize - 8 - this.fontRenderer.getStringWidth(s);
                final int l = 67;
                if (this.fontRenderer.getUnicodeFlag()) {
                    Gui.drawRect((float)(k - 3), 65.0f, (float)(this.xSize - 7), 77.0f, -16777216);
                    Gui.drawRect((float)(k - 2), 66.0f, (float)(this.xSize - 8), 76.0f, -12895429);
                }
                else {
                    this.fontRenderer.drawString(s, k, 68, j);
                    this.fontRenderer.drawString(s, k + 1, 67, j);
                    this.fontRenderer.drawString(s, k + 1, 68, j);
                }
                this.fontRenderer.drawString(s, k, 67, i);
            }
        }
        GlStateManager.enableLighting();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.nameField.textboxKeyTyped(typedChar, keyCode)) {
            this.renameItem();
        }
        else {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    private void renameItem() {
        String s = this.nameField.getText();
        final Slot slot = this.anvil.getSlot(0);
        if (slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() && s.equals(slot.getStack().getDisplayName())) {
            s = "";
        }
        this.anvil.updateItemName(s);
        final Minecraft mc = GuiRepair.mc;
        Minecraft.player.connection.sendPacket(new CPacketCustomPayload("MC|ItemName", new PacketBuffer(Unpooled.buffer()).writeString(s)));
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.nameField.drawTextBox();
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GuiRepair.mc.getTextureManager().bindTexture(GuiRepair.ANVIL_RESOURCE);
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(i + 59, j + 20, 0, this.ySize + (this.anvil.getSlot(0).getHasStack() ? 0 : 16), 110, 16);
        if ((this.anvil.getSlot(0).getHasStack() || this.anvil.getSlot(1).getHasStack()) && !this.anvil.getSlot(2).getHasStack()) {
            this.drawTexturedModalRect(i + 99, j + 45, this.xSize, 0, 28, 21);
        }
    }
    
    @Override
    public void sendAllContents(final Container containerToSend, final NonNullList<ItemStack> itemsList) {
        this.sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
    }
    
    @Override
    public void sendSlotContents(final Container containerToSend, final int slotInd, final ItemStack stack) {
        if (slotInd == 0) {
            this.nameField.setText(stack.isEmpty() ? "" : stack.getDisplayName());
            this.nameField.setEnabled(!stack.isEmpty());
            if (!stack.isEmpty()) {
                this.renameItem();
            }
        }
    }
    
    @Override
    public void sendWindowProperty(final Container containerIn, final int varToUpdate, final int newValue) {
    }
    
    @Override
    public void sendAllWindowProperties(final Container containerIn, final IInventory inventory) {
    }
    
    static {
        ANVIL_RESOURCE = new ResourceLocation("textures/gui/container/anvil.png");
    }
}
