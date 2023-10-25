package net.minecraft.init;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

static final class Bootstrap.9
extends BehaviorDefaultDispenseItem {
    private final BehaviorDefaultDispenseItem field_150842_b = new BehaviorDefaultDispenseItem();

    Bootstrap.9() {
    }

    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        double d3;
        EnumFacing enumfacing = BlockDispenser.getFacing((int)source.getBlockMetadata());
        World world = source.getWorld();
        double d0 = source.getX() + (double)((float)enumfacing.getFrontOffsetX() * 1.125f);
        double d1 = source.getY() + (double)((float)enumfacing.getFrontOffsetY() * 1.125f);
        double d2 = source.getZ() + (double)((float)enumfacing.getFrontOffsetZ() * 1.125f);
        BlockPos blockpos = source.getBlockPos().offset(enumfacing);
        Material material = world.getBlockState(blockpos).getBlock().getMaterial();
        if (Material.water.equals((Object)material)) {
            d3 = 1.0;
        } else {
            if (!Material.air.equals((Object)material) || !Material.water.equals((Object)world.getBlockState(blockpos.down()).getBlock().getMaterial())) {
                return this.field_150842_b.dispense(source, stack);
            }
            d3 = 0.0;
        }
        EntityBoat entityboat = new EntityBoat(world, d0, d1 + d3, d2);
        world.spawnEntityInWorld((Entity)entityboat);
        stack.splitStack(1);
        return stack;
    }

    protected void playDispenseSound(IBlockSource source) {
        source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
    }
}
