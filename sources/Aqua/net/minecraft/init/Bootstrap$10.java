package net.minecraft.init;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

static final class Bootstrap.10
extends BehaviorDefaultDispenseItem {
    private final BehaviorDefaultDispenseItem field_150841_b = new BehaviorDefaultDispenseItem();

    Bootstrap.10() {
    }

    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        ItemBucket itembucket = (ItemBucket)stack.getItem();
        BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing((int)source.getBlockMetadata()));
        if (itembucket.tryPlaceContainedLiquid(source.getWorld(), blockpos)) {
            stack.setItem(Items.bucket);
            stack.stackSize = 1;
            return stack;
        }
        return this.field_150841_b.dispense(source, stack);
    }
}
