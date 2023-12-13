/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00001742";

    public ContainerChest(IInventory p_i45801_1_, IInventory p_i45801_2_, EntityPlayer p_i45801_3_) {
        int var6;
        int var5;
        this.lowerChestInventory = p_i45801_2_;
        this.numRows = p_i45801_2_.getSizeInventory() / 9;
        p_i45801_2_.openInventory(p_i45801_3_);
        int var4 = (this.numRows - 4) * 18;
        for (var5 = 0; var5 < this.numRows; ++var5) {
            for (var6 = 0; var6 < 9; ++var6) {
                this.addSlotToContainer(new Slot(p_i45801_2_, var6 + var5 * 9, 8 + var6 * 18, 18 + var5 * 18));
            }
        }
        for (var5 = 0; var5 < 3; ++var5) {
            for (var6 = 0; var6 < 9; ++var6) {
                this.addSlotToContainer(new Slot(p_i45801_1_, var6 + var5 * 9 + 9, 8 + var6 * 18, 103 + var5 * 18 + var4));
            }
        }
        for (var5 = 0; var5 < 9; ++var5) {
            this.addSlotToContainer(new Slot(p_i45801_1_, var5, 8 + var5 * 18, 161 + var4));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.lowerChestInventory.isUseableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(index);
        if (var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();
            if (index < this.numRows * 9 ? !this.mergeItemStack(var5, this.numRows * 9, this.inventorySlots.size(), true) : !this.mergeItemStack(var5, 0, this.numRows * 9, false)) {
                return null;
            }
            if (var5.stackSize == 0) {
                var4.putStack(null);
            } else {
                var4.onSlotChanged();
            }
        }
        return var3;
    }

    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        this.lowerChestInventory.closeInventory(p_75134_1_);
    }

    public IInventory getLowerChestInventory() {
        return this.lowerChestInventory;
    }
}

