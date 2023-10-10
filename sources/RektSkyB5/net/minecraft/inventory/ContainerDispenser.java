/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerDispenser
extends Container {
    private IInventory dispenserInventory;

    public ContainerDispenser(IInventory playerInventory, IInventory dispenserInventoryIn) {
        this.dispenserInventory = dispenserInventoryIn;
        for (int i2 = 0; i2 < 3; ++i2) {
            for (int j2 = 0; j2 < 3; ++j2) {
                this.addSlotToContainer(new Slot(dispenserInventoryIn, j2 + i2 * 3, 62 + j2 * 18, 17 + i2 * 18));
            }
        }
        for (int k2 = 0; k2 < 3; ++k2) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlotToContainer(new Slot(playerInventory, i1 + k2 * 9 + 9, 8 + i1 * 18, 84 + k2 * 18));
            }
        }
        for (int l2 = 0; l2 < 9; ++l2) {
            this.addSlotToContainer(new Slot(playerInventory, l2, 8 + l2 * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.dispenserInventory.isUseableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < 9 ? !this.mergeItemStack(itemstack1, 9, 45, true) : !this.mergeItemStack(itemstack1, 0, 9, false)) {
                return null;
            }
            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(playerIn, itemstack1);
        }
        return itemstack;
    }
}

