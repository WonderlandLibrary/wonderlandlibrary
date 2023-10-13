/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import java.util.List;
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
    private static final String __OBFID = "CL_00000045";

    public ItemLead() {
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        Block var9 = worldIn.getBlockState(pos).getBlock();
        if (var9 instanceof BlockFence) {
            if (worldIn.isRemote) {
                return true;
            }
            ItemLead.func_180618_a(playerIn, worldIn, pos);
            return true;
        }
        return false;
    }

    public static boolean func_180618_a(EntityPlayer p_180618_0_, World worldIn, BlockPos p_180618_2_) {
        EntityLeashKnot var3 = EntityLeashKnot.func_174863_b(worldIn, p_180618_2_);
        boolean var4 = false;
        double var5 = 7.0;
        int var7 = p_180618_2_.getX();
        int var8 = p_180618_2_.getY();
        int var9 = p_180618_2_.getZ();
        List var10 = worldIn.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB((double)var7 - var5, (double)var8 - var5, (double)var9 - var5, (double)var7 + var5, (double)var8 + var5, (double)var9 + var5));
        for (EntityLiving var12 : var10) {
            if (!var12.getLeashed() || var12.getLeashedToEntity() != p_180618_0_) continue;
            if (var3 == null) {
                var3 = EntityLeashKnot.func_174862_a(worldIn, p_180618_2_);
            }
            var12.setLeashedToEntity(var3, true);
            var4 = true;
        }
        return var4;
    }
}

