package net.minecraft.item;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/*
 * Exception performing whole class analysis ignored.
 */
static final class ItemMinecart.1
extends BehaviorDefaultDispenseItem {
    private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();

    ItemMinecart.1() {
    }

    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        double d3;
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection;
        EnumFacing enumfacing = BlockDispenser.getFacing((int)source.getBlockMetadata());
        World world = source.getWorld();
        double d0 = source.getX() + (double)enumfacing.getFrontOffsetX() * 1.125;
        double d1 = Math.floor((double)source.getY()) + (double)enumfacing.getFrontOffsetY();
        double d2 = source.getZ() + (double)enumfacing.getFrontOffsetZ() * 1.125;
        BlockPos blockpos = source.getBlockPos().offset(enumfacing);
        IBlockState iblockstate = world.getBlockState(blockpos);
        BlockRailBase.EnumRailDirection enumRailDirection = blockrailbase$enumraildirection = iblockstate.getBlock() instanceof BlockRailBase ? (BlockRailBase.EnumRailDirection)iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
        if (BlockRailBase.isRailBlock((IBlockState)iblockstate)) {
            d3 = blockrailbase$enumraildirection.isAscending() ? 0.6 : 0.1;
        } else {
            if (iblockstate.getBlock().getMaterial() != Material.air || !BlockRailBase.isRailBlock((IBlockState)world.getBlockState(blockpos.down()))) {
                return this.behaviourDefaultDispenseItem.dispense(source, stack);
            }
            IBlockState iblockstate1 = world.getBlockState(blockpos.down());
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection1 = iblockstate1.getBlock() instanceof BlockRailBase ? (BlockRailBase.EnumRailDirection)iblockstate1.getValue(((BlockRailBase)iblockstate1.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            d3 = enumfacing != EnumFacing.DOWN && blockrailbase$enumraildirection1.isAscending() ? -0.4 : -0.9;
        }
        EntityMinecart entityminecart = EntityMinecart.getMinecart((World)world, (double)d0, (double)(d1 + d3), (double)d2, (EntityMinecart.EnumMinecartType)ItemMinecart.access$000((ItemMinecart)((ItemMinecart)stack.getItem())));
        if (stack.hasDisplayName()) {
            entityminecart.setCustomNameTag(stack.getDisplayName());
        }
        world.spawnEntityInWorld((Entity)entityminecart);
        stack.splitStack(1);
        return stack;
    }

    protected void playDispenseSound(IBlockSource source) {
        source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
    }
}
