/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemDoor
extends Item {
    private Block block;

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        if (enumFacing != EnumFacing.UP) {
            return false;
        }
        IBlockState iBlockState = world.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        if (!block.isReplaceable(world, blockPos)) {
            blockPos = blockPos.offset(enumFacing);
        }
        if (!entityPlayer.canPlayerEdit(blockPos, enumFacing, itemStack)) {
            return false;
        }
        if (!this.block.canPlaceBlockAt(world, blockPos)) {
            return false;
        }
        ItemDoor.placeDoor(world, blockPos, EnumFacing.fromAngle(entityPlayer.rotationYaw), this.block);
        --itemStack.stackSize;
        return true;
    }

    public static void placeDoor(World world, BlockPos blockPos, EnumFacing enumFacing, Block block) {
        BlockPos blockPos2 = blockPos.offset(enumFacing.rotateY());
        BlockPos blockPos3 = blockPos.offset(enumFacing.rotateYCCW());
        int n = (world.getBlockState(blockPos3).getBlock().isNormalCube() ? 1 : 0) + (world.getBlockState(blockPos3.up()).getBlock().isNormalCube() ? 1 : 0);
        int n2 = (world.getBlockState(blockPos2).getBlock().isNormalCube() ? 1 : 0) + (world.getBlockState(blockPos2.up()).getBlock().isNormalCube() ? 1 : 0);
        boolean bl = world.getBlockState(blockPos3).getBlock() == block || world.getBlockState(blockPos3.up()).getBlock() == block;
        boolean bl2 = world.getBlockState(blockPos2).getBlock() == block || world.getBlockState(blockPos2.up()).getBlock() == block;
        boolean bl3 = false;
        if (bl && !bl2 || n2 > n) {
            bl3 = true;
        }
        BlockPos blockPos4 = blockPos.up();
        IBlockState iBlockState = block.getDefaultState().withProperty(BlockDoor.FACING, enumFacing).withProperty(BlockDoor.HINGE, bl3 ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT);
        world.setBlockState(blockPos, iBlockState.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER), 2);
        world.setBlockState(blockPos4, iBlockState.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 2);
        world.notifyNeighborsOfStateChange(blockPos, block);
        world.notifyNeighborsOfStateChange(blockPos4, block);
    }

    public ItemDoor(Block block) {
        this.block = block;
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
}

