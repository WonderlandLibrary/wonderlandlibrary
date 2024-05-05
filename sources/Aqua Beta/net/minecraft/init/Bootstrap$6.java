package net.minecraft.init;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

static final class Bootstrap.6
extends BehaviorDefaultDispenseItem {
    Bootstrap.6() {
    }

    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        EnumFacing enumfacing = BlockDispenser.getFacing((int)source.getBlockMetadata());
        double d0 = source.getX() + (double)enumfacing.getFrontOffsetX();
        double d1 = (float)source.getBlockPos().getY() + 0.2f;
        double d2 = source.getZ() + (double)enumfacing.getFrontOffsetZ();
        Entity entity = ItemMonsterPlacer.spawnCreature((World)source.getWorld(), (int)stack.getMetadata(), (double)d0, (double)d1, (double)d2);
        if (entity instanceof EntityLivingBase && stack.hasDisplayName()) {
            ((EntityLiving)entity).setCustomNameTag(stack.getDisplayName());
        }
        stack.splitStack(1);
        return stack;
    }
}
