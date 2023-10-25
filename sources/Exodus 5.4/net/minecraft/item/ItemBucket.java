/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemBucket
extends Item {
    private Block isFull;

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        boolean bl = this.isFull == Blocks.air;
        MovingObjectPosition movingObjectPosition = this.getMovingObjectPositionFromPlayer(world, entityPlayer, bl);
        if (movingObjectPosition == null) {
            return itemStack;
        }
        if (movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos blockPos = movingObjectPosition.getBlockPos();
            if (!world.isBlockModifiable(entityPlayer, blockPos)) {
                return itemStack;
            }
            if (bl) {
                if (!entityPlayer.canPlayerEdit(blockPos.offset(movingObjectPosition.sideHit), movingObjectPosition.sideHit, itemStack)) {
                    return itemStack;
                }
                IBlockState iBlockState = world.getBlockState(blockPos);
                Material material = iBlockState.getBlock().getMaterial();
                if (material == Material.water && iBlockState.getValue(BlockLiquid.LEVEL) == 0) {
                    world.setBlockToAir(blockPos);
                    entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                    return this.fillBucket(itemStack, entityPlayer, Items.water_bucket);
                }
                if (material == Material.lava && iBlockState.getValue(BlockLiquid.LEVEL) == 0) {
                    world.setBlockToAir(blockPos);
                    entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                    return this.fillBucket(itemStack, entityPlayer, Items.lava_bucket);
                }
            } else {
                if (this.isFull == Blocks.air) {
                    return new ItemStack(Items.bucket);
                }
                BlockPos blockPos2 = blockPos.offset(movingObjectPosition.sideHit);
                if (!entityPlayer.canPlayerEdit(blockPos2, movingObjectPosition.sideHit, itemStack)) {
                    return itemStack;
                }
                if (this.tryPlaceContainedLiquid(world, blockPos2) && !entityPlayer.capabilities.isCreativeMode) {
                    entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                    return new ItemStack(Items.bucket);
                }
            }
        }
        return itemStack;
    }

    public boolean tryPlaceContainedLiquid(World world, BlockPos blockPos) {
        boolean bl;
        if (this.isFull == Blocks.air) {
            return false;
        }
        Material material = world.getBlockState(blockPos).getBlock().getMaterial();
        boolean bl2 = bl = !material.isSolid();
        if (!world.isAirBlock(blockPos) && !bl) {
            return false;
        }
        if (world.provider.doesWaterVaporize() && this.isFull == Blocks.flowing_water) {
            int n = blockPos.getX();
            int n2 = blockPos.getY();
            int n3 = blockPos.getZ();
            world.playSoundEffect((float)n + 0.5f, (float)n2 + 0.5f, (float)n3 + 0.5f, "random.fizz", 0.5f, 2.6f + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8f);
            int n4 = 0;
            while (n4 < 8) {
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)n + Math.random(), (double)n2 + Math.random(), (double)n3 + Math.random(), 0.0, 0.0, 0.0, new int[0]);
                ++n4;
            }
        } else {
            if (!world.isRemote && bl && !material.isLiquid()) {
                world.destroyBlock(blockPos, true);
            }
            world.setBlockState(blockPos, this.isFull.getDefaultState(), 3);
        }
        return true;
    }

    private ItemStack fillBucket(ItemStack itemStack, EntityPlayer entityPlayer, Item item) {
        if (entityPlayer.capabilities.isCreativeMode) {
            return itemStack;
        }
        if (--itemStack.stackSize <= 0) {
            return new ItemStack(item);
        }
        if (!entityPlayer.inventory.addItemStackToInventory(new ItemStack(item))) {
            entityPlayer.dropPlayerItemWithRandomChoice(new ItemStack(item, 1, 0), false);
        }
        return itemStack;
    }

    public ItemBucket(Block block) {
        this.maxStackSize = 1;
        this.isFull = block;
        this.setCreativeTab(CreativeTabs.tabMisc);
    }
}

