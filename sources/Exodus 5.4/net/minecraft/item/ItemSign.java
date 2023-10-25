/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemSign
extends Item {
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        if (enumFacing == EnumFacing.DOWN) {
            return false;
        }
        if (!world.getBlockState(blockPos).getBlock().getMaterial().isSolid()) {
            return false;
        }
        if (!entityPlayer.canPlayerEdit(blockPos = blockPos.offset(enumFacing), enumFacing, itemStack)) {
            return false;
        }
        if (!Blocks.standing_sign.canPlaceBlockAt(world, blockPos)) {
            return false;
        }
        if (world.isRemote) {
            return true;
        }
        if (enumFacing == EnumFacing.UP) {
            int n = MathHelper.floor_double((double)((entityPlayer.rotationYaw + 180.0f) * 16.0f / 360.0f) + 0.5) & 0xF;
            world.setBlockState(blockPos, Blocks.standing_sign.getDefaultState().withProperty(BlockStandingSign.ROTATION, n), 3);
        } else {
            world.setBlockState(blockPos, Blocks.wall_sign.getDefaultState().withProperty(BlockWallSign.FACING, enumFacing), 3);
        }
        --itemStack.stackSize;
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntitySign && !ItemBlock.setTileEntityNBT(world, entityPlayer, blockPos, itemStack)) {
            entityPlayer.openEditSign((TileEntitySign)tileEntity);
        }
        return true;
    }

    public ItemSign() {
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
}

