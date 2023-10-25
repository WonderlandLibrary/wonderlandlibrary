/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemHoe
extends Item {
    protected Item.ToolMaterial theToolMaterial;

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        if (!entityPlayer.canPlayerEdit(blockPos.offset(enumFacing), enumFacing, itemStack)) {
            return false;
        }
        IBlockState iBlockState = world.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        if (enumFacing != EnumFacing.DOWN && world.getBlockState(blockPos.up()).getBlock().getMaterial() == Material.air) {
            if (block == Blocks.grass) {
                return this.useHoe(itemStack, entityPlayer, world, blockPos, Blocks.farmland.getDefaultState());
            }
            if (block == Blocks.dirt) {
                switch (iBlockState.getValue(BlockDirt.VARIANT)) {
                    case DIRT: {
                        return this.useHoe(itemStack, entityPlayer, world, blockPos, Blocks.farmland.getDefaultState());
                    }
                    case COARSE_DIRT: {
                        return this.useHoe(itemStack, entityPlayer, world, blockPos, Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                    }
                }
            }
        }
        return false;
    }

    public String getMaterialName() {
        return this.theToolMaterial.toString();
    }

    public ItemHoe(Item.ToolMaterial toolMaterial) {
        this.theToolMaterial = toolMaterial;
        this.maxStackSize = 1;
        this.setMaxDamage(toolMaterial.getMaxUses());
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    protected boolean useHoe(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, IBlockState iBlockState) {
        world.playSoundEffect((float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.5f, (float)blockPos.getZ() + 0.5f, iBlockState.getBlock().stepSound.getStepSound(), (iBlockState.getBlock().stepSound.getVolume() + 1.0f) / 2.0f, iBlockState.getBlock().stepSound.getFrequency() * 0.8f);
        if (world.isRemote) {
            return true;
        }
        world.setBlockState(blockPos, iBlockState);
        itemStack.damageItem(1, entityPlayer);
        return true;
    }
}

