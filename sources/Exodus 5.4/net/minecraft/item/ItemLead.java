/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemLead
extends Item {
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        Block block = world.getBlockState(blockPos).getBlock();
        if (block instanceof BlockFence) {
            if (world.isRemote) {
                return true;
            }
            ItemLead.attachToFence(entityPlayer, world, blockPos);
            return true;
        }
        return false;
    }

    public ItemLead() {
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    public static boolean attachToFence(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        EntityLeashKnot entityLeashKnot = EntityLeashKnot.getKnotForPosition(world, blockPos);
        boolean bl = false;
        double d = 7.0;
        int n = blockPos.getX();
        int n2 = blockPos.getY();
        int n3 = blockPos.getZ();
        for (EntityLiving entityLiving : world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB((double)n - d, (double)n2 - d, (double)n3 - d, (double)n + d, (double)n2 + d, (double)n3 + d))) {
            if (!entityLiving.getLeashed() || entityLiving.getLeashedToEntity() != entityPlayer) continue;
            if (entityLeashKnot == null) {
                entityLeashKnot = EntityLeashKnot.createKnot(world, blockPos);
            }
            entityLiving.setLeashedToEntity(entityLeashKnot, true);
            bl = true;
        }
        return bl;
    }
}

