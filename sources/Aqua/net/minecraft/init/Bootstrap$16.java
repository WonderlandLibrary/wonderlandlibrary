package net.minecraft.init;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

static final class Bootstrap.16
extends BehaviorDefaultDispenseItem {
    private boolean field_179241_b = true;

    Bootstrap.16() {
    }

    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        World world = source.getWorld();
        BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing((int)source.getBlockMetadata()));
        BlockPumpkin blockpumpkin = (BlockPumpkin)Blocks.pumpkin;
        if (world.isAirBlock(blockpos) && blockpumpkin.canDispenserPlace(world, blockpos)) {
            if (!world.isRemote) {
                world.setBlockState(blockpos, blockpumpkin.getDefaultState(), 3);
            }
            --stack.stackSize;
        } else {
            this.field_179241_b = false;
        }
        return stack;
    }

    protected void playDispenseSound(IBlockSource source) {
        if (this.field_179241_b) {
            source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
        } else {
            source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
        }
    }
}
