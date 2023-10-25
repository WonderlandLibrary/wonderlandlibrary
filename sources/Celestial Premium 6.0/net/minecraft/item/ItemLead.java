/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemLead
extends Item {
    public ItemLead() {
        this.setCreativeTab(CreativeTabs.TOOLS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
        Block block = playerIn.getBlockState(worldIn).getBlock();
        if (!(block instanceof BlockFence)) {
            return EnumActionResult.PASS;
        }
        if (!playerIn.isRemote) {
            ItemLead.attachToFence(stack, playerIn, worldIn);
        }
        return EnumActionResult.SUCCESS;
    }

    public static boolean attachToFence(EntityPlayer player, World worldIn, BlockPos fence) {
        EntityLeashKnot entityleashknot = EntityLeashKnot.getKnotForPosition(worldIn, fence);
        boolean flag = false;
        double d0 = 7.0;
        int i = fence.getX();
        int j = fence.getY();
        int k = fence.getZ();
        for (EntityLiving entityliving : worldIn.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB((double)i - 7.0, (double)j - 7.0, (double)k - 7.0, (double)i + 7.0, (double)j + 7.0, (double)k + 7.0))) {
            if (!entityliving.getLeashed() || entityliving.getLeashedToEntity() != player) continue;
            if (entityleashknot == null) {
                entityleashknot = EntityLeashKnot.createKnot(worldIn, fence);
            }
            entityliving.setLeashedToEntity(entityleashknot, true);
            flag = true;
        }
        return flag;
    }
}

