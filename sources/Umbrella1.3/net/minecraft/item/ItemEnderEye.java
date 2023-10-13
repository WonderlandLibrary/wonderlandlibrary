/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00000026";

    public ItemEnderEye() {
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState var9 = worldIn.getBlockState(pos);
        if (playerIn.func_175151_a(pos.offset(side), side, stack) && var9.getBlock() == Blocks.end_portal_frame && !((Boolean)var9.getValue(BlockEndPortalFrame.field_176507_b)).booleanValue()) {
            if (worldIn.isRemote) {
                return true;
            }
            worldIn.setBlockState(pos, var9.withProperty(BlockEndPortalFrame.field_176507_b, Boolean.valueOf(true)), 2);
            worldIn.updateComparatorOutputLevel(pos, Blocks.end_portal_frame);
            --stack.stackSize;
            for (int var10 = 0; var10 < 16; ++var10) {
                double var11 = (float)pos.getX() + (5.0f + itemRand.nextFloat() * 6.0f) / 16.0f;
                double var13 = (float)pos.getY() + 0.8125f;
                double var15 = (float)pos.getZ() + (5.0f + itemRand.nextFloat() * 6.0f) / 16.0f;
                double var17 = 0.0;
                double var19 = 0.0;
                double var21 = 0.0;
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var11, var13, var15, var17, var19, var21, new int[0]);
            }
            EnumFacing var23 = (EnumFacing)((Object)var9.getValue(BlockEndPortalFrame.field_176508_a));
            int var24 = 0;
            int var12 = 0;
            boolean var25 = false;
            boolean var14 = true;
            EnumFacing var26 = var23.rotateY();
            for (int var16 = -2; var16 <= 2; ++var16) {
                BlockPos var28 = pos.offset(var26, var16);
                IBlockState var18 = worldIn.getBlockState(var28);
                if (var18.getBlock() != Blocks.end_portal_frame) continue;
                if (!((Boolean)var18.getValue(BlockEndPortalFrame.field_176507_b)).booleanValue()) {
                    var14 = false;
                    break;
                }
                var12 = var16;
                if (var25) continue;
                var24 = var16;
                var25 = true;
            }
            if (var14 && var12 == var24 + 2) {
                int var29;
                BlockPos var27 = pos.offset(var23, 4);
                for (var29 = var24; var29 <= var12; ++var29) {
                    BlockPos var30 = var27.offset(var26, var29);
                    IBlockState var32 = worldIn.getBlockState(var30);
                    if (var32.getBlock() == Blocks.end_portal_frame && ((Boolean)var32.getValue(BlockEndPortalFrame.field_176507_b)).booleanValue()) continue;
                    var14 = false;
                    break;
                }
                block3: for (var29 = var24 - 1; var29 <= var12 + 1; var29 += 4) {
                    var27 = pos.offset(var26, var29);
                    for (int var31 = 1; var31 <= 3; ++var31) {
                        BlockPos var33 = var27.offset(var23, var31);
                        IBlockState var20 = worldIn.getBlockState(var33);
                        if (var20.getBlock() == Blocks.end_portal_frame && ((Boolean)var20.getValue(BlockEndPortalFrame.field_176507_b)).booleanValue()) continue;
                        var14 = false;
                        continue block3;
                    }
                }
                if (var14) {
                    for (var29 = var24; var29 <= var12; ++var29) {
                        var27 = pos.offset(var26, var29);
                        for (int var31 = 1; var31 <= 3; ++var31) {
                            BlockPos var33 = var27.offset(var23, var31);
                            worldIn.setBlockState(var33, Blocks.end_portal.getDefaultState(), 2);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        BlockPos var5;
        MovingObjectPosition var4 = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, false);
        if (var4 != null && var4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && worldIn.getBlockState(var4.func_178782_a()).getBlock() == Blocks.end_portal_frame) {
            return itemStackIn;
        }
        if (!worldIn.isRemote && (var5 = worldIn.func_180499_a("Stronghold", new BlockPos(playerIn))) != null) {
            EntityEnderEye var6 = new EntityEnderEye(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ);
            var6.func_180465_a(var5);
            worldIn.spawnEntityInWorld(var6);
            worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));
            worldIn.playAuxSFXAtEntity(null, 1002, new BlockPos(playerIn), 0);
            if (!playerIn.capabilities.isCreativeMode) {
                --itemStackIn.stackSize;
            }
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        }
        return itemStackIn;
    }
}

