/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerHopper
extends Container {
    private final IInventory hopperInventory;

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
        this.hopperInventory.closeInventory(entityPlayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return this.hopperInventory.isUseableByPlayer(entityPlayer);
    }

    public ContainerHopper(InventoryPlayer inventoryPlayer, IInventory iInventory, EntityPlayer entityPlayer) {
        this.hopperInventory = iInventory;
        iInventory.openInventory(entityPlayer);
        int n = 51;
        int n2 = 0;
        while (n2 < iInventory.getSizeInventory()) {
            this.addSlotToContainer(new Slot(iInventory, n2, 44 + n2 * 18, 20));
            ++n2;
        }
        n2 = 0;
        while (n2 < 3) {
            int n3 = 0;
            while (n3 < 9) {
                this.addSlotToContainer(new Slot(inventoryPlayer, n3 + n2 * 9 + 9, 8 + n3 * 18, n2 * 18 + n));
                ++n3;
            }
            ++n2;
        }
        n2 = 0;
        while (n2 < 9) {
            this.addSlotToContainer(new Slot(inventoryPlayer, n2, 8 + n2 * 18, 58 + n));
            ++n2;
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int n) {
        ItemStack itemStack = null;
        Slot slot = (Slot)this.inventorySlots.get(n);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (n < this.hopperInventory.getSizeInventory() ? !this.mergeItemStack(itemStack2, this.hopperInventory.getSizeInventory(), this.inventorySlots.size(), true) : !this.mergeItemStack(itemStack2, 0, this.hopperInventory.getSizeInventory(), false)) {
                return null;
            }
            if (itemStack2.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemStack;
    }
}

