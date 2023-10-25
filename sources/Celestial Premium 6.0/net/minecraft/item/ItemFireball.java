/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFireball
extends Item {
    public ItemFireball() {
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
        ItemStack itemstack;
        if (playerIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        if (!stack.canPlayerEdit(worldIn = worldIn.offset(hand), hand, itemstack = stack.getHeldItem(pos))) {
            return EnumActionResult.FAIL;
        }
        if (playerIn.getBlockState(worldIn).getMaterial() == Material.AIR) {
            playerIn.playSound(null, worldIn, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0f, (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2f + 1.0f);
            playerIn.setBlockState(worldIn, Blocks.FIRE.getDefaultState());
        }
        if (!stack.capabilities.isCreativeMode) {
            itemstack.func_190918_g(1);
        }
        return EnumActionResult.SUCCESS;
    }
}

