package net.minecraft.init;

import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

static final class Bootstrap.5
implements IBehaviorDispenseItem {
    private final BehaviorDefaultDispenseItem field_150843_b = new BehaviorDefaultDispenseItem();

    Bootstrap.5() {
    }

    public ItemStack dispense(IBlockSource source, ItemStack stack) {
        return ItemPotion.isSplash((int)stack.getMetadata()) ? new /* Unavailable Anonymous Inner Class!! */.dispense(source, stack) : this.field_150843_b.dispense(source, stack);
    }
}
