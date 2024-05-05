package net.minecraft.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

class ContainerBrewingStand.Ingredient
extends Slot {
    public ContainerBrewingStand.Ingredient(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    public boolean isItemValid(ItemStack stack) {
        return stack != null ? stack.getItem().isPotionIngredient(stack) : false;
    }

    public int getSlotStackLimit() {
        return 64;
    }
}
