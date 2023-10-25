package net.minecraft.init;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

static final class Bootstrap.14
extends BehaviorDefaultDispenseItem {
    Bootstrap.14() {
    }

    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        World world = source.getWorld();
        BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing((int)source.getBlockMetadata()));
        EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (double)blockpos.getX() + 0.5, (double)blockpos.getY(), (double)blockpos.getZ() + 0.5, (EntityLivingBase)null);
        world.spawnEntityInWorld((Entity)entitytntprimed);
        world.playSoundAtEntity((Entity)entitytntprimed, "game.tnt.primed", 1.0f, 1.0f);
        --stack.stackSize;
        return stack;
    }
}
