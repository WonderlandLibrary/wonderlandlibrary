// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemRedstone extends Item
{
    public ItemRedstone() {
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
        final BlockPos blockpos = flag ? pos : pos.offset(facing);
        final ItemStack itemstack = player.getHeldItem(hand);
        if (player.canPlayerEdit(blockpos, facing, itemstack) && worldIn.mayPlace(worldIn.getBlockState(blockpos).getBlock(), blockpos, false, facing, null) && Blocks.REDSTONE_WIRE.canPlaceBlockAt(worldIn, blockpos)) {
            worldIn.setBlockState(blockpos, Blocks.REDSTONE_WIRE.getDefaultState());
            if (player instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, blockpos, itemstack);
            }
            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }
}
