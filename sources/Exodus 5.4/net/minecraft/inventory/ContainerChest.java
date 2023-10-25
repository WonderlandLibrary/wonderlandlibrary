/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerChest
extends Container {
    private IInventory lowerChestInventory;
    private int numRows;

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
        this.lowerChestInventory.closeInventory(entityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int n) {
        ItemStack itemStack = null;
        Slot slot = (Slot)this.inventorySlots.get(n);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (n < this.numRows * 9 ? !this.mergeItemStack(itemStack2, this.numRows * 9, this.inventorySlots.size(), true) : !this.mergeItemStack(itemStack2, 0, this.numRows * 9, false)) {
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

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return this.lowerChestInventory.isUseableByPlayer(entityPlayer);
    }

    public ContainerChest(IInventory iInventory, IInventory iInventory2, EntityPlayer entityPlayer) {
        int n;
        this.lowerChestInventory = iInventory2;
        this.numRows = iInventory2.getSizeInventory() / 9;
        iInventory2.openInventory(entityPlayer);
        int n2 = (this.numRows - 4) * 18;
        int n3 = 0;
        while (n3 < this.numRows) {
            n = 0;
            while (n < 9) {
                this.addSlotToContainer(new Slot(iInventory2, n + n3 * 9, 8 + n * 18, 18 + n3 * 18));
                ++n;
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < 3) {
            n = 0;
            while (n < 9) {
                this.addSlotToContainer(new Slot(iInventory, n + n3 * 9 + 9, 8 + n * 18, 103 + n3 * 18 + n2));
                ++n;
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < 9) {
            this.addSlotToContainer(new Slot(iInventory, n3, 8 + n3 * 18, 161 + n2));
            ++n3;
        }
    }

    public IInventory getLowerChestInventory() {
        return this.lowerChestInventory;
    }
}

