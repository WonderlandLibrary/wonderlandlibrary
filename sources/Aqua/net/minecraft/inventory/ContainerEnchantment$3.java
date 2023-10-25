package net.minecraft.inventory;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

class ContainerEnchantment.3
extends Slot {
    ContainerEnchantment.3(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() == Items.dye && EnumDyeColor.byDyeDamage((int)stack.getMetadata()) == EnumDyeColor.BLUE;
    }
}
