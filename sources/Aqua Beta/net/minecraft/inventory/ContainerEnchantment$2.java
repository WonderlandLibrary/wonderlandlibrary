package net.minecraft.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

class ContainerEnchantment.2
extends Slot {
    ContainerEnchantment.2(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    public boolean isItemValid(ItemStack stack) {
        return true;
    }

    public int getSlotStackLimit() {
        return 1;
    }
}
