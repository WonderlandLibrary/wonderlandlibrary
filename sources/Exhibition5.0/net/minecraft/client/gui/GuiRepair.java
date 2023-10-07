// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.inventory.Slot;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C17PacketCustomPayload;
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
import net.minecraft.inventory.ICrafting;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiRepair extends GuiContainer implements ICrafting
{
    private static final ResourceLocation anvilResource;
    private ContainerRepair anvil;
    private GuiTextField nameField;
    private InventoryPlayer playerInventory;
    private static final String __OBFID = "CL_00000738";
    
    public GuiRepair(final InventoryPlayer p_i45508_1_, final World worldIn) {
        super(new ContainerRepair(p_i45508_1_, worldIn, Minecraft.getMinecraft().thePlayer));
        this.playerInventory = p_i45508_1_;
        this.anvil = (ContainerRepair)this.inventorySlots;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        final int var1 = (this.width - this.xSize) / 2;
        final int var2 = (this.height - this.ySize) / 2;
        (this.nameField = new GuiTextField(0, this.fontRendererObj, var1 + 62, var2 + 24, 103, 12)).setTextColor(-1);
        this.nameField.setDisabledTextColour(-1);
        this.nameField.setEnableBackgroundDrawing(false);
        this.nameField.setMaxStringLength(40);
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
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.fontRendererObj.drawString(I18n.format("container.repair", new Object[0]), 60.0f, 6.0f, 4210752);
        if (this.anvil.maximumCost > 0) {
            int var3 = 8453920;
            boolean var4 = true;
            String var5 = I18n.format("container.repair.cost", this.anvil.maximumCost);
            if (this.anvil.maximumCost >= 40 && !this.mc.thePlayer.capabilities.isCreativeMode) {
                var5 = I18n.format("container.repair.expensive", new Object[0]);
                var3 = 16736352;
            }
            else if (!this.anvil.getSlot(2).getHasStack()) {
                var4 = false;
            }
            else if (!this.anvil.getSlot(2).canTakeStack(this.playerInventory.player)) {
                var3 = 16736352;
            }
            if (var4) {
                final int var6 = 0xFF000000 | (var3 & 0xFCFCFC) >> 2 | (var3 & 0xFF000000);
                final int var7 = this.xSize - 8 - this.fontRendererObj.getStringWidth(var5);
                final byte var8 = 67;
                if (this.fontRendererObj.getUnicodeFlag()) {
                    Gui.drawRect(var7 - 3, var8 - 2, this.xSize - 7, var8 + 10, -16777216);
                    Gui.drawRect(var7 - 2, var8 - 1, this.xSize - 8, var8 + 9, -12895429);
                }
                else {
                    this.fontRendererObj.drawString(var5, var7, var8 + 1, var6);
                    this.fontRendererObj.drawString(var5, var7 + 1, var8, var6);
                    this.fontRendererObj.drawString(var5, var7 + 1, var8 + 1, var6);
                }
                this.fontRendererObj.drawString(var5, var7, var8, var3);
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
        String var1 = this.nameField.getText();
        final Slot var2 = this.anvil.getSlot(0);
        if (var2 != null && var2.getHasStack() && !var2.getStack().hasDisplayName() && var1.equals(var2.getStack().getDisplayName())) {
            var1 = "";
        }
        this.anvil.updateItemName(var1);
        this.mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("MC|ItemName", new PacketBuffer(Unpooled.buffer()).writeString(var1)));
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.nameField.drawTextBox();
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiRepair.anvilResource);
        final int var4 = (this.width - this.xSize) / 2;
        final int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(var4 + 59, var5 + 20, 0, this.ySize + (this.anvil.getSlot(0).getHasStack() ? 0 : 16), 110, 16);
        if ((this.anvil.getSlot(0).getHasStack() || this.anvil.getSlot(1).getHasStack()) && !this.anvil.getSlot(2).getHasStack()) {
            this.drawTexturedModalRect(var4 + 99, var5 + 45, this.xSize, 0, 28, 21);
        }
    }
    
    @Override
    public void updateCraftingInventory(final Container p_71110_1_, final List p_71110_2_) {
        this.sendSlotContents(p_71110_1_, 0, p_71110_1_.getSlot(0).getStack());
    }
    
    @Override
    public void sendSlotContents(final Container p_71111_1_, final int p_71111_2_, final ItemStack p_71111_3_) {
        if (p_71111_2_ == 0) {
            this.nameField.setText((p_71111_3_ == null) ? "" : p_71111_3_.getDisplayName());
            this.nameField.setEnabled(p_71111_3_ != null);
            if (p_71111_3_ != null) {
                this.renameItem();
            }
        }
    }
    
    @Override
    public void sendProgressBarUpdate(final Container p_71112_1_, final int p_71112_2_, final int p_71112_3_) {
    }
    
    @Override
    public void func_175173_a(final Container p_175173_1_, final IInventory p_175173_2_) {
    }
    
    static {
        anvilResource = new ResourceLocation("textures/gui/container/anvil.png");
    }
}
