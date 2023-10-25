package net.minecraft.init;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

static final class Bootstrap.7
extends BehaviorDefaultDispenseItem {
    Bootstrap.7() {
    }

    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        EnumFacing enumfacing = BlockDispenser.getFacing((int)source.getBlockMetadata());
        double d0 = source.getX() + (double)enumfacing.getFrontOffsetX();
        double d1 = (float)source.getBlockPos().getY() + 0.2f;
        double d2 = source.getZ() + (double)enumfacing.getFrontOffsetZ();
        EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(source.getWorld(), d0, d1, d2, stack);
        source.getWorld().spawnEntityInWorld((Entity)entityfireworkrocket);
        stack.splitStack(1);
        return stack;
    }

    protected void playDispenseSound(IBlockSource source) {
        source.getWorld().playAuxSFX(1002, source.getBlockPos(), 0);
    }
}
