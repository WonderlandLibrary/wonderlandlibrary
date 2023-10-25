/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemBed
extends Item {
    public ItemBed() {
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        if (world.isRemote) {
            return true;
        }
        if (enumFacing != EnumFacing.UP) {
            return false;
        }
        IBlockState iBlockState = world.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        boolean bl = block.isReplaceable(world, blockPos);
        if (!bl) {
            blockPos = blockPos.up();
        }
        int n = MathHelper.floor_double((double)(entityPlayer.rotationYaw * 4.0f / 360.0f) + 0.5) & 3;
        EnumFacing enumFacing2 = EnumFacing.getHorizontal(n);
        BlockPos blockPos2 = blockPos.offset(enumFacing2);
        if (entityPlayer.canPlayerEdit(blockPos, enumFacing, itemStack) && entityPlayer.canPlayerEdit(blockPos2, enumFacing, itemStack)) {
            boolean bl2;
            boolean bl3 = world.getBlockState(blockPos2).getBlock().isReplaceable(world, blockPos2);
            boolean bl4 = bl || world.isAirBlock(blockPos);
            boolean bl5 = bl2 = bl3 || world.isAirBlock(blockPos2);
            if (bl4 && bl2 && World.doesBlockHaveSolidTopSurface(world, blockPos.down()) && World.doesBlockHaveSolidTopSurface(world, blockPos2.down())) {
                IBlockState iBlockState2 = Blocks.bed.getDefaultState().withProperty(BlockBed.OCCUPIED, false).withProperty(BlockBed.FACING, enumFacing2).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);
                if (world.setBlockState(blockPos, iBlockState2, 3)) {
                    IBlockState iBlockState3 = iBlockState2.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD);
                    world.setBlockState(blockPos2, iBlockState3, 3);
                }
                --itemStack.stackSize;
                return true;
            }
            return false;
        }
        return false;
    }
}

