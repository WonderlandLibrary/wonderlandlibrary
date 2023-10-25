package net.minecraft.inventory;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

class ContainerHorseInventory.1
extends Slot {
    ContainerHorseInventory.1(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    public boolean isItemValid(ItemStack stack) {
        return super.isItemValid(stack) && stack.getItem() == Items.saddle && !this.getHasStack();
    }
}
