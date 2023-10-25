/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemReed
extends Item {
    private Block block;

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        IBlockState iBlockState;
        IBlockState iBlockState2 = world.getBlockState(blockPos);
        Block block = iBlockState2.getBlock();
        if (block == Blocks.snow_layer && iBlockState2.getValue(BlockSnow.LAYERS) < 1) {
            enumFacing = EnumFacing.UP;
        } else if (!block.isReplaceable(world, blockPos)) {
            blockPos = blockPos.offset(enumFacing);
        }
        if (!entityPlayer.canPlayerEdit(blockPos, enumFacing, itemStack)) {
            return false;
        }
        if (itemStack.stackSize == 0) {
            return false;
        }
        if (world.canBlockBePlaced(this.block, blockPos, false, enumFacing, null, itemStack) && world.setBlockState(blockPos, iBlockState = this.block.onBlockPlaced(world, blockPos, enumFacing, f, f2, f3, 0, entityPlayer), 3)) {
            iBlockState = world.getBlockState(blockPos);
            if (iBlockState.getBlock() == this.block) {
                ItemBlock.setTileEntityNBT(world, entityPlayer, blockPos, itemStack);
                iBlockState.getBlock().onBlockPlacedBy(world, blockPos, iBlockState, entityPlayer, itemStack);
            }
            world.playSoundEffect((float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.5f, (float)blockPos.getZ() + 0.5f, this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0f) / 2.0f, this.block.stepSound.getFrequency() * 0.8f);
            --itemStack.stackSize;
            return true;
        }
        return false;
    }

    public ItemReed(Block block) {
        this.block = block;
    }
}

