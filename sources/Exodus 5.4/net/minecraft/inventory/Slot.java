/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class Slot {
    public final IInventory inventory;
    private final int slotIndex;
    public int yDisplayPosition;
    public int xDisplayPosition;
    public int slotNumber;

    public void putStack(ItemStack itemStack) {
        this.inventory.setInventorySlotContents(this.slotIndex, itemStack);
        this.onSlotChanged();
    }

    public boolean isHere(IInventory iInventory, int n) {
        return iInventory == this.inventory && n == this.slotIndex;
    }

    public int getSlotStackLimit() {
        return this.inventory.getInventoryStackLimit();
    }

    public boolean isItemValid(ItemStack itemStack) {
        return true;
    }

    public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack itemStack) {
        this.onSlotChanged();
    }

    public void onSlotChanged() {
        this.inventory.markDirty();
    }

    public String getSlotTexture() {
        return null;
    }

    public Slot(IInventory iInventory, int n, int n2, int n3) {
        this.inventory = iInventory;
        this.slotIndex = n;
        this.xDisplayPosition = n2;
        this.yDisplayPosition = n3;
    }

    public void onSlotChange(ItemStack itemStack, ItemStack itemStack2) {
        int n;
        if (itemStack != null && itemStack2 != null && itemStack.getItem() == itemStack2.getItem() && (n = itemStack2.stackSize - itemStack.stackSize) > 0) {
            this.onCrafting(itemStack, n);
        }
    }

    public ItemStack getStack() {
        return this.inventory.getStackInSlot(this.slotIndex);
    }

    public ItemStack decrStackSize(int n) {
        return this.inventory.decrStackSize(this.slotIndex, n);
    }

    public boolean canTakeStack(EntityPlayer entityPlayer) {
        return true;
    }

    public int getItemStackLimit(ItemStack itemStack) {
        return this.getSlotStackLimit();
    }

    public boolean getHasStack() {
        return this.getStack() != null;
    }

    protected void onCrafting(ItemStack itemStack, int n) {
    }

    public boolean canBeHovered() {
        return true;
    }

    protected void onCrafting(ItemStack itemStack) {
    }
}

