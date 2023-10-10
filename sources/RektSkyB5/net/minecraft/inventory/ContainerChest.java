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

    public ContainerChest(IInventory playerInventory, IInventory chestInventory, EntityPlayer player) {
        this.lowerChestInventory = chestInventory;
        this.numRows = chestInventory.getSizeInventory() / 9;
        chestInventory.openInventory(player);
        int i2 = (this.numRows - 4) * 18;
        for (int j2 = 0; j2 < this.numRows; ++j2) {
            for (int k2 = 0; k2 < 9; ++k2) {
                this.addSlotToContainer(new Slot(chestInventory, k2 + j2 * 9, 8 + k2 * 18, 18 + j2 * 18));
            }
        }
        for (int l2 = 0; l2 < 3; ++l2) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlotToContainer(new Slot(playerInventory, j1 + l2 * 9 + 9, 8 + j1 * 18, 103 + l2 * 18 + i2));
            }
        }
        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i2));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.lowerChestInventory.isUseableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < this.numRows * 9 ? !this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true) : !this.mergeItemStack(itemstack1, 0, this.numRows * 9, false)) {
                return null;
            }
            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.lowerChestInventory.closeInventory(playerIn);
    }

    public IInventory getLowerChestInventory() {
        return this.lowerChestInventory;
    }
}

