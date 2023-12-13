// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemLead extends Item
{
    private static final String __OBFID = "CL_00000045";
    
    public ItemLead() {
        this.setCreativeTab(CreativeTabs.tabTools);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final Block var9 = worldIn.getBlockState(pos).getBlock();
        if (!(var9 instanceof BlockFence)) {
            return false;
        }
        if (worldIn.isRemote) {
            return true;
        }
        func_180618_a(playerIn, worldIn, pos);
        return true;
    }
    
    public static boolean func_180618_a(final EntityPlayer p_180618_0_, final World worldIn, final BlockPos p_180618_2_) {
        EntityLeashKnot var3 = EntityLeashKnot.func_174863_b(worldIn, p_180618_2_);
        boolean var4 = false;
        final double var5 = 7.0;
        final int var6 = p_180618_2_.getX();
        final int var7 = p_180618_2_.getY();
        final int var8 = p_180618_2_.getZ();
        final List var9 = worldIn.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(var6 - var5, var7 - var5, var8 - var5, var6 + var5, var7 + var5, var8 + var5));
        for (final EntityLiving var11 : var9) {
            if (var11.getLeashed() && var11.getLeashedToEntity() == p_180618_0_) {
                if (var3 == null) {
                    var3 = EntityLeashKnot.func_174862_a(worldIn, p_180618_2_);
                }
                var11.setLeashedToEntity(var3, true);
                var4 = true;
            }
        }
        return var4;
    }
}
