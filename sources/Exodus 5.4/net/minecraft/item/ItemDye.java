/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ItemDye
extends Item {
    public static final int[] dyeColors = new int[]{0x1E1B1B, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 0xABABAB, 0x434343, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 0xF0F0F0};

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        int n = itemStack.getMetadata();
        return String.valueOf(super.getUnlocalizedName()) + "." + EnumDyeColor.byDyeDamage(n).getUnlocalizedName();
    }

    public static void spawnBonemealParticles(World world, BlockPos blockPos, int n) {
        Block block;
        if (n == 0) {
            n = 15;
        }
        if ((block = world.getBlockState(blockPos).getBlock()).getMaterial() != Material.air) {
            block.setBlockBoundsBasedOnState(world, blockPos);
            int n2 = 0;
            while (n2 < n) {
                double d = itemRand.nextGaussian() * 0.02;
                double d2 = itemRand.nextGaussian() * 0.02;
                double d3 = itemRand.nextGaussian() * 0.02;
                world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (float)blockPos.getX() + itemRand.nextFloat(), (double)blockPos.getY() + (double)itemRand.nextFloat() * block.getBlockBoundsMaxY(), (double)((float)blockPos.getZ() + itemRand.nextFloat()), d, d2, d3, new int[0]);
                ++n2;
            }
        }
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer entityPlayer, EntityLivingBase entityLivingBase) {
        if (entityLivingBase instanceof EntitySheep) {
            EntitySheep entitySheep = (EntitySheep)entityLivingBase;
            EnumDyeColor enumDyeColor = EnumDyeColor.byDyeDamage(itemStack.getMetadata());
            if (!entitySheep.getSheared() && entitySheep.getFleeceColor() != enumDyeColor) {
                entitySheep.setFleeceColor(enumDyeColor);
                --itemStack.stackSize;
            }
            return true;
        }
        return false;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        int n = 0;
        while (n < 16) {
            list.add(new ItemStack(item, 1, n));
            ++n;
        }
    }

    public ItemDye() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        IBlockState iBlockState;
        Block block;
        if (!entityPlayer.canPlayerEdit(blockPos.offset(enumFacing), enumFacing, itemStack)) {
            return false;
        }
        EnumDyeColor enumDyeColor = EnumDyeColor.byDyeDamage(itemStack.getMetadata());
        if (enumDyeColor == EnumDyeColor.WHITE) {
            if (ItemDye.applyBonemeal(itemStack, world, blockPos)) {
                if (!world.isRemote) {
                    world.playAuxSFX(2005, blockPos, 0);
                }
                return true;
            }
        } else if (enumDyeColor == EnumDyeColor.BROWN && (block = (iBlockState = world.getBlockState(blockPos)).getBlock()) == Blocks.log && iBlockState.getValue(BlockPlanks.VARIANT) == BlockPlanks.EnumType.JUNGLE) {
            if (enumFacing == EnumFacing.DOWN) {
                return false;
            }
            if (enumFacing == EnumFacing.UP) {
                return false;
            }
            if (world.isAirBlock(blockPos = blockPos.offset(enumFacing))) {
                IBlockState iBlockState2 = Blocks.cocoa.onBlockPlaced(world, blockPos, enumFacing, f, f2, f3, 0, entityPlayer);
                world.setBlockState(blockPos, iBlockState2, 2);
                if (!entityPlayer.capabilities.isCreativeMode) {
                    --itemStack.stackSize;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean applyBonemeal(ItemStack itemStack, World world, BlockPos blockPos) {
        IGrowable iGrowable;
        IBlockState iBlockState = world.getBlockState(blockPos);
        if (iBlockState.getBlock() instanceof IGrowable && (iGrowable = (IGrowable)((Object)iBlockState.getBlock())).canGrow(world, blockPos, iBlockState, world.isRemote)) {
            if (!world.isRemote) {
                if (iGrowable.canUseBonemeal(world, world.rand, blockPos, iBlockState)) {
                    iGrowable.grow(world, world.rand, blockPos, iBlockState);
                }
                --itemStack.stackSize;
            }
            return true;
        }
        return false;
    }
}

