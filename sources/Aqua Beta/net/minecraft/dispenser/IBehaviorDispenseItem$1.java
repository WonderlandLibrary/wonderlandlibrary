package net.minecraft.dispenser;

import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;

static final class IBehaviorDispenseItem.1
implements IBehaviorDispenseItem {
    IBehaviorDispenseItem.1() {
    }

    public ItemStack dispense(IBlockSource source, ItemStack stack) {
        return stack;
    }
}
