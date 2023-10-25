/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemRedstone
extends Item {
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        BlockPos blockPos2;
        boolean bl = world.getBlockState(blockPos).getBlock().isReplaceable(world, blockPos);
        BlockPos blockPos3 = blockPos2 = bl ? blockPos : blockPos.offset(enumFacing);
        if (!entityPlayer.canPlayerEdit(blockPos2, enumFacing, itemStack)) {
            return false;
        }
        Block block = world.getBlockState(blockPos2).getBlock();
        if (!world.canBlockBePlaced(block, blockPos2, false, enumFacing, null, itemStack)) {
            return false;
        }
        if (Blocks.redstone_wire.canPlaceBlockAt(world, blockPos2)) {
            --itemStack.stackSize;
            world.setBlockState(blockPos2, Blocks.redstone_wire.getDefaultState());
            return true;
        }
        return false;
    }

    public ItemRedstone() {
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
}

