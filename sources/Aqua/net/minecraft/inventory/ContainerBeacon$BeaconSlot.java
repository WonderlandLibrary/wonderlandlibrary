package net.minecraft.inventory;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

class ContainerBeacon.BeaconSlot
extends Slot {
    public ContainerBeacon.BeaconSlot(IInventory p_i1801_2_, int p_i1801_3_, int p_i1801_4_, int p_i1801_5_) {
        super(p_i1801_2_, p_i1801_3_, p_i1801_4_, p_i1801_5_);
    }

    public boolean isItemValid(ItemStack stack) {
        return stack == null ? false : stack.getItem() == Items.emerald || stack.getItem() == Items.diamond || stack.getItem() == Items.gold_ingot || stack.getItem() == Items.iron_ingot;
    }

    public int getSlotStackLimit() {
        return 1;
    }
}
