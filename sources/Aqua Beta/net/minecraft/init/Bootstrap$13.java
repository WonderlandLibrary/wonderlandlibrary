package net.minecraft.init;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

static final class Bootstrap.13
extends BehaviorDefaultDispenseItem {
    private boolean field_150838_b = true;

    Bootstrap.13() {
    }

    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        if (EnumDyeColor.WHITE == EnumDyeColor.byDyeDamage((int)stack.getMetadata())) {
            BlockPos blockpos;
            World world = source.getWorld();
            if (ItemDye.applyBonemeal((ItemStack)stack, (World)world, (BlockPos)(blockpos = source.getBlockPos().offset(BlockDispenser.getFacing((int)source.getBlockMetadata()))))) {
                if (!world.isRemote) {
                    world.playAuxSFX(2005, blockpos, 0);
                }
            } else {
                this.field_150838_b = false;
            }
            return stack;
        }
        return super.dispenseStack(source, stack);
    }

    protected void playDispenseSound(IBlockSource source) {
        if (this.field_150838_b) {
            source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
        } else {
            source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
        }
    }
}
