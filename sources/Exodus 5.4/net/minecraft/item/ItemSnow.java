/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemSnow
extends ItemBlock {
    public ItemSnow(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        IBlockState iBlockState;
        AxisAlignedBB axisAlignedBB;
        int n;
        if (itemStack.stackSize == 0) {
            return false;
        }
        if (!entityPlayer.canPlayerEdit(blockPos, enumFacing, itemStack)) {
            return false;
        }
        IBlockState iBlockState2 = world.getBlockState(blockPos);
        Block block = iBlockState2.getBlock();
        BlockPos blockPos2 = blockPos;
        if (!(enumFacing == EnumFacing.UP && block == this.block || block.isReplaceable(world, blockPos))) {
            blockPos2 = blockPos.offset(enumFacing);
            iBlockState2 = world.getBlockState(blockPos2);
            block = iBlockState2.getBlock();
        }
        if (block == this.block && (n = iBlockState2.getValue(BlockSnow.LAYERS).intValue()) <= 7 && (axisAlignedBB = this.block.getCollisionBoundingBox(world, blockPos2, iBlockState = iBlockState2.withProperty(BlockSnow.LAYERS, n + 1))) != null && world.checkNoEntityCollision(axisAlignedBB) && world.setBlockState(blockPos2, iBlockState, 2)) {
            world.playSoundEffect((float)blockPos2.getX() + 0.5f, (float)blockPos2.getY() + 0.5f, (float)blockPos2.getZ() + 0.5f, this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0f) / 2.0f, this.block.stepSound.getFrequency() * 0.8f);
            --itemStack.stackSize;
            return true;
        }
        return super.onItemUse(itemStack, entityPlayer, world, blockPos2, enumFacing, f, f2, f3);
    }

    @Override
    public int getMetadata(int n) {
        return n;
    }
}

