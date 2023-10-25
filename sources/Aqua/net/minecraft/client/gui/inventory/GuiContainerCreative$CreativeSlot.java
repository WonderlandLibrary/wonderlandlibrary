package net.minecraft.client.gui.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

class GuiContainerCreative.CreativeSlot
extends Slot {
    private final Slot slot;

    public GuiContainerCreative.CreativeSlot(Slot p_i46313_2_, int p_i46313_3_) {
        super(p_i46313_2_.inventory, p_i46313_3_, 0, 0);
        this.slot = p_i46313_2_;
    }

    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        this.slot.onPickupFromSlot(playerIn, stack);
    }

    public boolean isItemValid(ItemStack stack) {
        return this.slot.isItemValid(stack);
    }

    public ItemStack getStack() {
        return this.slot.getStack();
    }

    public boolean getHasStack() {
        return this.slot.getHasStack();
    }

    public void putStack(ItemStack stack) {
        this.slot.putStack(stack);
    }

    public void onSlotChanged() {
        this.slot.onSlotChanged();
    }

    public int getSlotStackLimit() {
        return this.slot.getSlotStackLimit();
    }

    public int getItemStackLimit(ItemStack stack) {
        return this.slot.getItemStackLimit(stack);
    }

    public String getSlotTexture() {
        return this.slot.getSlotTexture();
    }

    public ItemStack decrStackSize(int amount) {
        return this.slot.decrStackSize(amount);
    }

    public boolean isHere(IInventory inv, int slotIn) {
        return this.slot.isHere(inv, slotIn);
    }

    static /* synthetic */ Slot access$000(GuiContainerCreative.CreativeSlot x0) {
        return x0.slot;
    }
}
