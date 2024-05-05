package net.minecraft.inventory;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

class ContainerHorseInventory.2
extends Slot {
    final /* synthetic */ EntityHorse val$horse;

    ContainerHorseInventory.2(IInventory inventoryIn, int index, int xPosition, int yPosition, EntityHorse entityHorse) {
        this.val$horse = entityHorse;
        super(inventoryIn, index, xPosition, yPosition);
    }

    public boolean isItemValid(ItemStack stack) {
        return super.isItemValid(stack) && this.val$horse.canWearArmor() && EntityHorse.isArmorItem((Item)stack.getItem());
    }

    public boolean canBeHovered() {
        return this.val$horse.canWearArmor();
    }
}
