/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemLilyPad
extends ItemColored {
    public ItemLilyPad(Block block) {
        super(block, false);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        MovingObjectPosition movingObjectPosition = this.getMovingObjectPositionFromPlayer(world, entityPlayer, true);
        if (movingObjectPosition == null) {
            return itemStack;
        }
        if (movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos blockPos = movingObjectPosition.getBlockPos();
            if (!world.isBlockModifiable(entityPlayer, blockPos)) {
                return itemStack;
            }
            if (!entityPlayer.canPlayerEdit(blockPos.offset(movingObjectPosition.sideHit), movingObjectPosition.sideHit, itemStack)) {
                return itemStack;
            }
            BlockPos blockPos2 = blockPos.up();
            IBlockState iBlockState = world.getBlockState(blockPos);
            if (iBlockState.getBlock().getMaterial() == Material.water && iBlockState.getValue(BlockLiquid.LEVEL) == 0 && world.isAirBlock(blockPos2)) {
                world.setBlockState(blockPos2, Blocks.waterlily.getDefaultState());
                if (!entityPlayer.capabilities.isCreativeMode) {
                    --itemStack.stackSize;
                }
                entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
            }
        }
        return itemStack;
    }

    @Override
    public int getColorFromItemStack(ItemStack itemStack, int n) {
        return Blocks.waterlily.getRenderColor(Blocks.waterlily.getStateFromMeta(itemStack.getMetadata()));
    }
}

