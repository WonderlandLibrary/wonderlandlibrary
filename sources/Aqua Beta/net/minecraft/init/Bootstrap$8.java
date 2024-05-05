package net.minecraft.init;

import java.util.Random;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

static final class Bootstrap.8
extends BehaviorDefaultDispenseItem {
    Bootstrap.8() {
    }

    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        EnumFacing enumfacing = BlockDispenser.getFacing((int)source.getBlockMetadata());
        IPosition iposition = BlockDispenser.getDispensePosition((IBlockSource)source);
        double d0 = iposition.getX() + (double)((float)enumfacing.getFrontOffsetX() * 0.3f);
        double d1 = iposition.getY() + (double)((float)enumfacing.getFrontOffsetY() * 0.3f);
        double d2 = iposition.getZ() + (double)((float)enumfacing.getFrontOffsetZ() * 0.3f);
        World world = source.getWorld();
        Random random = world.rand;
        double d3 = random.nextGaussian() * 0.05 + (double)enumfacing.getFrontOffsetX();
        double d4 = random.nextGaussian() * 0.05 + (double)enumfacing.getFrontOffsetY();
        double d5 = random.nextGaussian() * 0.05 + (double)enumfacing.getFrontOffsetZ();
        world.spawnEntityInWorld((Entity)new EntitySmallFireball(world, d0, d1, d2, d3, d4, d5));
        stack.splitStack(1);
        return stack;
    }

    protected void playDispenseSound(IBlockSource source) {
        source.getWorld().playAuxSFX(1009, source.getBlockPos(), 0);
    }
}
