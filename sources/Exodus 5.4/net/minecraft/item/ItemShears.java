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
    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, BlockPos blockPos, EntityLivingBase entityLivingBase) {
        if (block.getMaterial() != Material.leaves && block != Blocks.web && block != Blocks.tallgrass && block != Blocks.vine && block != Blocks.tripwire && block != Blocks.wool) {
            return super.onBlockDestroyed(itemStack, world, block, blockPos, entityLivingBase);
        }
        itemStack.damageItem(1, entityLivingBase);
        return true;
    }

    @Override
    public float getStrVsBlock(ItemStack itemStack, Block block) {
        return block != Blocks.web && block.getMaterial() != Material.leaves ? (block == Blocks.wool ? 5.0f : super.getStrVsBlock(itemStack, block)) : 15.0f;
    }

    @Override
    public boolean canHarvestBlock(Block block) {
        return block == Blocks.web || block == Blocks.redstone_wire || block == Blocks.tripwire;
    }

    public ItemShears() {
        this.setMaxStackSize(1);
        this.setMaxDamage(238);
        this.setCreativeTab(CreativeTabs.tabTools);
    }
}

