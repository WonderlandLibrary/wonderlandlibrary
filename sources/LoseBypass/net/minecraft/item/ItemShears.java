/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ItemShears
extends Item {
    public ItemShears() {
        this.setMaxStackSize(1);
        this.setMaxDamage(238);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
        if (blockIn.getMaterial() != Material.leaves && blockIn != Blocks.web && blockIn != Blocks.tallgrass && blockIn != Blocks.vine && blockIn != Blocks.tripwire && blockIn != Blocks.wool) {
            return super.onBlockDestroyed(stack, worldIn, blockIn, pos, playerIn);
        }
        stack.damageItem(1, playerIn);
        return true;
    }

    @Override
    public boolean canHarvestBlock(Block blockIn) {
        if (blockIn == Blocks.web) return true;
        if (blockIn == Blocks.redstone_wire) return true;
        if (blockIn == Blocks.tripwire) return true;
        return false;
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block block) {
        if (block == Blocks.web) return 15.0f;
        if (block.getMaterial() == Material.leaves) return 15.0f;
        if (block == Blocks.wool) {
            return 5.0f;
        }
        float f = super.getStrVsBlock(stack, block);
        return f;
    }
}

