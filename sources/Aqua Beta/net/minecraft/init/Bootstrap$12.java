package net.minecraft.init;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.properties.IProperty;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

static final class Bootstrap.12
extends BehaviorDefaultDispenseItem {
    private boolean field_150839_b = true;

    Bootstrap.12() {
    }

    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        BlockPos blockpos;
        World world = source.getWorld();
        if (world.isAirBlock(blockpos = source.getBlockPos().offset(BlockDispenser.getFacing((int)source.getBlockMetadata())))) {
            world.setBlockState(blockpos, Blocks.fire.getDefaultState());
            if (stack.attemptDamageItem(1, world.rand)) {
                stack.stackSize = 0;
            }
        } else if (world.getBlockState(blockpos).getBlock() == Blocks.tnt) {
            Blocks.tnt.onBlockDestroyedByPlayer(world, blockpos, Blocks.tnt.getDefaultState().withProperty((IProperty)BlockTNT.EXPLODE, (Comparable)Boolean.valueOf((boolean)true)));
            world.setBlockToAir(blockpos);
        } else {
            this.field_150839_b = false;
        }
        return stack;
    }

    protected void playDispenseSound(IBlockSource source) {
        if (this.field_150839_b) {
            source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
        } else {
            source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
        }
    }
}
