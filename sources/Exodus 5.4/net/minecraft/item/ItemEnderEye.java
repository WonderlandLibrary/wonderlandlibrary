/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemEnderEye
extends Item {
    public ItemEnderEye() {
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        BlockPos blockPos;
        MovingObjectPosition movingObjectPosition = this.getMovingObjectPositionFromPlayer(world, entityPlayer, false);
        if (movingObjectPosition != null && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && world.getBlockState(movingObjectPosition.getBlockPos()).getBlock() == Blocks.end_portal_frame) {
            return itemStack;
        }
        if (!world.isRemote && (blockPos = world.getStrongholdPos("Stronghold", new BlockPos(entityPlayer))) != null) {
            EntityEnderEye entityEnderEye = new EntityEnderEye(world, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
            entityEnderEye.moveTowards(blockPos);
            world.spawnEntityInWorld(entityEnderEye);
            world.playSoundAtEntity(entityPlayer, "random.bow", 0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));
            world.playAuxSFXAtEntity(null, 1002, new BlockPos(entityPlayer), 0);
            if (!entityPlayer.capabilities.isCreativeMode) {
                --itemStack.stackSize;
            }
            entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        }
        return itemStack;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        IBlockState iBlockState = world.getBlockState(blockPos);
        if (entityPlayer.canPlayerEdit(blockPos.offset(enumFacing), enumFacing, itemStack) && iBlockState.getBlock() == Blocks.end_portal_frame && !iBlockState.getValue(BlockEndPortalFrame.EYE).booleanValue()) {
            Object object;
            if (world.isRemote) {
                return true;
            }
            world.setBlockState(blockPos, iBlockState.withProperty(BlockEndPortalFrame.EYE, true), 2);
            world.updateComparatorOutputLevel(blockPos, Blocks.end_portal_frame);
            --itemStack.stackSize;
            int n = 0;
            while (n < 16) {
                double d = (float)blockPos.getX() + (5.0f + itemRand.nextFloat() * 6.0f) / 16.0f;
                double d2 = (float)blockPos.getY() + 0.8125f;
                double d3 = (float)blockPos.getZ() + (5.0f + itemRand.nextFloat() * 6.0f) / 16.0f;
                double d4 = 0.0;
                double d5 = 0.0;
                double d6 = 0.0;
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d, d2, d3, d4, d5, d6, new int[0]);
                ++n;
            }
            EnumFacing enumFacing2 = iBlockState.getValue(BlockEndPortalFrame.FACING);
            int n2 = 0;
            int n3 = 0;
            boolean bl = false;
            boolean bl2 = true;
            EnumFacing enumFacing3 = enumFacing2.rotateY();
            int n4 = -2;
            while (n4 <= 2) {
                BlockPos blockPos2 = blockPos.offset(enumFacing3, n4);
                object = world.getBlockState(blockPos2);
                if (object.getBlock() == Blocks.end_portal_frame) {
                    if (!object.getValue(BlockEndPortalFrame.EYE).booleanValue()) {
                        bl2 = false;
                        break;
                    }
                    n3 = n4;
                    if (!bl) {
                        n2 = n4;
                        bl = true;
                    }
                }
                ++n4;
            }
            if (bl2 && n3 == n2 + 2) {
                BlockPos blockPos3 = blockPos.offset(enumFacing2, 4);
                int n5 = n2;
                while (n5 <= n3) {
                    object = blockPos3.offset(enumFacing3, n5);
                    IBlockState iBlockState2 = world.getBlockState((BlockPos)object);
                    if (iBlockState2.getBlock() != Blocks.end_portal_frame || !iBlockState2.getValue(BlockEndPortalFrame.EYE).booleanValue()) {
                        bl2 = false;
                        break;
                    }
                    ++n5;
                }
                n5 = n2 - 1;
                while (n5 <= n3 + 1) {
                    blockPos3 = blockPos.offset(enumFacing3, n5);
                    int n6 = 1;
                    while (n6 <= 3) {
                        BlockPos blockPos4 = blockPos3.offset(enumFacing2, n6);
                        IBlockState iBlockState3 = world.getBlockState(blockPos4);
                        if (iBlockState3.getBlock() != Blocks.end_portal_frame || !iBlockState3.getValue(BlockEndPortalFrame.EYE).booleanValue()) {
                            bl2 = false;
                            break;
                        }
                        ++n6;
                    }
                    n5 += 4;
                }
                if (bl2) {
                    n5 = n2;
                    while (n5 <= n3) {
                        blockPos3 = blockPos.offset(enumFacing3, n5);
                        int n7 = 1;
                        while (n7 <= 3) {
                            BlockPos blockPos5 = blockPos3.offset(enumFacing2, n7);
                            world.setBlockState(blockPos5, Blocks.end_portal.getDefaultState(), 2);
                            ++n7;
                        }
                        ++n5;
                    }
                }
            }
            return true;
        }
        return false;
    }
}

