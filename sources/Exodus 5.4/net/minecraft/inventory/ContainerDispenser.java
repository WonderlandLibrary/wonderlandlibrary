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

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int n) {
        ItemStack itemStack = null;
        Slot slot = (Slot)this.inventorySlots.get(n);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (n < 9 ? !this.mergeItemStack(itemStack2, 9, 45, true) : !this.mergeItemStack(itemStack2, 0, 9, false)) {
                return null;
            }
            if (itemStack2.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            if (itemStack2.stackSize == itemStack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(entityPlayer, itemStack2);
        }
        return itemStack;
    }

    public ContainerDispenser(IInventory iInventory, IInventory iInventory2) {
        int n;
        this.dispenserInventory = iInventory2;
        int n2 = 0;
        while (n2 < 3) {
            n = 0;
            while (n < 3) {
                this.addSlotToContainer(new Slot(iInventory2, n + n2 * 3, 62 + n * 18, 17 + n2 * 18));
                ++n;
            }
            ++n2;
        }
        n2 = 0;
        while (n2 < 3) {
            n = 0;
            while (n < 9) {
                this.addSlotToContainer(new Slot(iInventory, n + n2 * 9 + 9, 8 + n * 18, 84 + n2 * 18));
                ++n;
            }
            ++n2;
        }
        n2 = 0;
        while (n2 < 9) {
            this.addSlotToContainer(new Slot(iInventory, n2, 8 + n2 * 18, 142));
            ++n2;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return this.dispenserInventory.isUseableByPlayer(entityPlayer);
    }
}

